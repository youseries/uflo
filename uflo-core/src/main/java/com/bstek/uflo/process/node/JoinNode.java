/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.uflo.process.node;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.expr.ExpressionContext;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.ProcessInstanceState;
import com.bstek.uflo.model.variable.Variable;
import com.bstek.uflo.query.ProcessVariableQuery;
import com.bstek.uflo.service.ProcessService;

/**
 * @author Jacky.gao
 * @since 2013年7月31日
 */
public class JoinNode extends Node {
	private static final long serialVersionUID = 1L;
	private int multiplicity;
	private int percentMultiplicity;
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean enter(Context context, ProcessInstance processInstance) {
		Session session=context.getSession();
		processInstance.setState(ProcessInstanceState.End);
		ProcessService processService=context.getProcessService();
		ExpressionContext expressionContext=context.getExpressionContext();
		expressionContext.moveContextToParent(processInstance);
		expressionContext.removeContext(processInstance);
		long parentId=processInstance.getParentId();
		
		ProcessInstance parentProcessInstance=processService.getProcessInstanceById(parentId);
		createActivityHistory(context, parentProcessInstance);
		doEnterEventHandler(context, parentProcessInstance);
		
		ProcessVariableQuery query=processService.createProcessVariableQuery();
		query.processInstanceId(processInstance.getId());
		for(Variable var:query.list()){
			var.setProcessInstanceId(parentId);
			session.update(var);
		}
		session.delete(processInstance);
		List<ProcessInstance> noneCompleteProcessInstances=session.createCriteria(ProcessInstance.class).add(Restrictions.eq("parentId", parentId)).list();
		int parallelCount=processInstance.getParallelInstanceCount();
		int completedCount=parallelCount-noneCompleteProcessInstances.size();
		ProcessDefinition pd=processService.getProcessById(processInstance.getProcessId());
		if(multiplicity>0){
			if(completedCount>=multiplicity){
				for(ProcessInstance pi:noneCompleteProcessInstances){
					Node node=pd.getNode(pi.getCurrentNode());
					node.cancel(context, processInstance);
					session.createQuery("delete "+Variable.class.getName()+" where processInstanceId=:piId").setLong("piId", pi.getId()).executeUpdate();
					processService.deleteProcessInstance(pi);
					node.completeActivityHistory(context, pi,null);
					expressionContext.removeContext(pi);
				}
				return true;
			}
		}else if(percentMultiplicity>0){
			double percent=Double.valueOf(percentMultiplicity)/Double.valueOf(100);
			double alreadyCompletedPercent=(Double.valueOf(completedCount)/Double.valueOf(parallelCount));
			if(alreadyCompletedPercent>=percent){
				for(ProcessInstance pi:noneCompleteProcessInstances){
					Node node=pd.getNode(pi.getCurrentNode());
					node.cancel(context, pi);
					session.createQuery("delete "+Variable.class.getName()+" where processInstanceId=:piId").setLong("piId", pi.getId()).executeUpdate();
					processService.deleteProcessInstance(pi);
					node.completeActivityHistory(context, pi,null);
					expressionContext.removeContext(pi);
				}
				return true;
			}
		}else if(noneCompleteProcessInstances.size()==0){
			return true;
		}
		return false;
	}

	@Override
	public String leave(Context context, ProcessInstance processInstance,String flowName) {
		Session session=context.getSession();
		long parentProcessInstanceId=processInstance.getParentId();
		ProcessInstance parentProcessInstance=(ProcessInstance)session.createCriteria(ProcessInstance.class).add(Restrictions.eq("id", parentProcessInstanceId)).uniqueResult();
		ProcessDefinition pd=context.getProcessService().getProcessById(processInstance.getProcessId());
		Node node=pd.getNode(parentProcessInstance.getCurrentNode());
		node.doLeaveEventHandler(context, parentProcessInstance);
		node.completeActivityHistory(context, parentProcessInstance, flowName);
		parentProcessInstance.setCurrentNode(getName());
		return leaveNode(context, parentProcessInstance, flowName);
	}
	@Override
	public void cancel(Context context, ProcessInstance processInstance) {
	}

	@Override
	public NodeType getType() {
		return NodeType.Join;
	}
	
	public int getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(int multiplicity) {
		this.multiplicity = multiplicity;
	}

	public int getPercentMultiplicity() {
		return percentMultiplicity;
	}

	public void setPercentMultiplicity(int percentMultiplicity) {
		this.percentMultiplicity = percentMultiplicity;
	}
}
