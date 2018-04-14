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
package com.bstek.uflo.command.impl;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.HistoryActivity;
import com.bstek.uflo.model.HistoryTask;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskParticipator;
import com.bstek.uflo.model.variable.Variable;
import com.bstek.uflo.process.node.Node;
import com.bstek.uflo.process.node.TaskNode;
import com.bstek.uflo.service.SchedulerService;

/**
 * 递归删除取消指定流程实例及其下子流程实现及他们所在的各个节点
 * 
 * @author Jacky.gao
 * @since 2013年8月8日
 */
public class DeleteProcessInstanceCommand implements Command<Object> {
	private ProcessInstance processInstance;
	public DeleteProcessInstanceCommand(ProcessInstance processInstance){
		this.processInstance=processInstance;
	}
	public Object execute(Context context) {
		ProcessDefinition process=context.getProcessService().getProcessById(processInstance.getProcessId());
		deleteProcessInstance(context,processInstance,process);
		return null;
	}
	@SuppressWarnings("unchecked")
	public Object deleteProcessInstance(Context context,ProcessInstance pi,ProcessDefinition process) {
		Session session=context.getSession();
		List<ProcessInstance> instances=session.createQuery("from "+ProcessInstance.class.getName()+" where parentId=:parentId").setLong("parentId",pi.getId()).list();
		for(ProcessInstance instance:instances){
			ProcessDefinition subProcess=context.getProcessService().getProcessById(instance.getProcessId());
			deleteProcessInstance(context,instance,subProcess);
			session.createQuery("delete "+Variable.class.getName()+" where processInstanceId=:pIId").setLong("pIId",instance.getId()).executeUpdate();
		}
		session.createQuery("delete "+Variable.class.getName()+" where processInstanceId=:processInstanceId or rootProcessInstanceId=:rootId")
			.setLong("processInstanceId",pi.getId()).setLong("rootId",pi.getRootId()).executeUpdate();
		
		
		String currentNodeName=pi.getCurrentNode();
		if(StringUtils.isNotBlank(currentNodeName)){
			Node currentNode=process.getNode(currentNodeName);
			if(currentNode!=null && !(currentNode instanceof TaskNode)){
				currentNode.cancel(context, pi);					
			}
		}
		
		session.createQuery("delete "+HistoryTask.class.getName()+" where processInstanceId=:processInstanceId or rootProcessInstanceId=:rootId")
			.setLong("processInstanceId", pi.getId()).setLong("rootId",pi.getRootId()).executeUpdate();
		session.createQuery("delete "+HistoryActivity.class.getName()+" where processInstanceId=:processInstanceId or rootProcessInstanceId=:rootId")
	 		.setLong("processInstanceId", pi.getId()).setLong("rootId",pi.getRootId()).executeUpdate();
		
		Collection<Task> tasks=session.createCriteria(Task.class).add(Restrictions.eq("processInstanceId", pi.getId())).list();
		SchedulerService schedulerService=(SchedulerService)context.getApplicationContext().getBean(SchedulerService.BEAN_ID);
		for(Task task:tasks){
			schedulerService.removeReminderJob(task);
			session.createQuery("delete "+TaskParticipator.class.getName()+" where taskId=:taskId").setLong("taskId", task.getId()).executeUpdate();
			session.delete(task);
		}
		session.delete(pi);
		context.getExpressionContext().removeContext(pi);
		return null;
	}
}
