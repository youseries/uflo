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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.uflo.command.impl.SaveProcessInstanceVariablesCommand;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.ProcessInstanceState;
import com.bstek.uflo.process.handler.ForeachHandler;
import com.bstek.uflo.utils.IDGenerator;

/**
 * @author Jacky.gao
 * @since 2013年8月8日
 */
public class ForeachNode extends Node {
	private static final long serialVersionUID = -7072013520247348803L;
	private String variable;
	private ForeachType foreachType;
	private String processVariable;
	private String handlerBean;
	@SuppressWarnings("unchecked")
	@Override
	public boolean enter(Context context, ProcessInstance processInstance) {
		Collection<Object> coll=null;
		if(StringUtils.isEmpty(variable)){
			throw new IllegalArgumentException("ForeachNode var property can not be null");
		}
		if(foreachType.equals(ForeachType.Handler)){
			ForeachHandler handler=(ForeachHandler)context.getApplicationContext().getBean(handlerBean);
			coll=handler.handle(processInstance, context);
			if(coll==null || coll.size()==0){
				throw new IllegalArgumentException("ForeachNode ["+handlerBean+"] return value is invalid!");
			}
		}else{
			if(StringUtils.isEmpty(processVariable)){
				throw new IllegalArgumentException("ForeachNode processVariable property can not be null");
			}
			Object variableObj=context.getExpressionContext().eval(processInstance, processVariable);
			if(variableObj instanceof Object[]){
				coll=new ArrayList<Object>();
				Object objs[]=(Object[])variableObj;
				for(Object obj:objs){
					coll.add(obj);
				}
			}else if(variableObj instanceof Collection){
				coll=(Collection<Object>)variableObj;
			}else{
				throw new IllegalArgumentException("ForeachHandler ["+processVariable+"] return value type must be a collection or a array!");
			}
		}
		createSubprocessInstance(context, processInstance, coll);
		return false;
	}


	private void createSubprocessInstance(Context context,ProcessInstance processInstance,Collection<Object> coll) {
		for(Object obj:coll){
			ProcessInstance subProcessInstance=new ProcessInstance();
			subProcessInstance.setId(IDGenerator.getInstance().nextId());
			subProcessInstance.setProcessId(getProcessId());
			subProcessInstance.setParentId(processInstance.getId());
			subProcessInstance.setCreateDate(new Date());
			subProcessInstance.setState(ProcessInstanceState.Start);
			subProcessInstance.setRootId(processInstance.getRootId());
			subProcessInstance.setParallelInstanceCount(coll.size());
			subProcessInstance.setPromoter(processInstance.getPromoter());
			subProcessInstance.setHistoryProcessInstanceId(processInstance.getHistoryProcessInstanceId());
			subProcessInstance.setCurrentTask(processInstance.getCurrentTask());
			subProcessInstance.setBusinessId(processInstance.getBusinessId());
			subProcessInstance.setTag(processInstance.getTag());
			subProcessInstance.setSubject(processInstance.getSubject());
			context.getSession().save(subProcessInstance);
			Map<String,Object> variables=new HashMap<String,Object>();
			variables.put(variable, obj);
			context.getCommandService().executeCommand(new SaveProcessInstanceVariablesCommand(subProcessInstance, variables));
			context.getExpressionContext().createContext(subProcessInstance, variables);
			getSequenceFlows().get(0).execute(context, subProcessInstance);
		}
	}


	@Override
	public String leave(Context context, ProcessInstance processInstance,String flowName) {
		return null;
	}
	@Override
	public void cancel(Context context, ProcessInstance processInstance) {
		
	}
	
	@Override
	public NodeType getType() {
		return NodeType.Foreach;
	}

	public ForeachType getForeachType() {
		return foreachType;
	}


	public void setForeachType(ForeachType foreachType) {
		this.foreachType = foreachType;
	}


	public String getProcessVariable() {
		return processVariable;
	}

	public void setProcessVariable(String processVariable) {
		this.processVariable = processVariable;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getHandlerBean() {
		return handlerBean;
	}

	public void setHandlerBean(String handlerBean) {
		this.handlerBean = handlerBean;
	}
}
