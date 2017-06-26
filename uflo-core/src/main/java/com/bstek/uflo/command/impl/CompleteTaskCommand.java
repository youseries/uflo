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

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.process.listener.TaskListener;
import com.bstek.uflo.process.node.Node;
import com.bstek.uflo.process.node.TaskNode;
import com.bstek.uflo.process.node.reminder.DueDefinition;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.SchedulerService;
import com.bstek.uflo.service.TaskOpinion;

/**
 * @author Jacky.gao
 * @since 2013年8月7日
 */
public class CompleteTaskCommand implements Command<Task> {
	private Task task;
	private String flowName;
	private TaskOpinion opinion;
	private Map<String,Object> variables;
	public CompleteTaskCommand(Task task,String flowName,TaskOpinion opinion,Map<String,Object> variables){
		this.task=task;
		this.opinion=opinion;
		this.flowName=flowName;
		this.variables=variables;
	}
	public Task execute(Context context) {
		TaskState state=task.getState();
		if(!state.equals(TaskState.InProgress) 
				&& !state.equals(TaskState.Forwarded) 
				&& !state.equals(TaskState.Rollback) 
				&& !state.equals(TaskState.Withdraw)){
			throw new IllegalStateException("Please change task state to inProgress first!");
		}
		if(state.equals(TaskState.InProgress)){
			task.setProgress(100);
		}
		task.setState(TaskState.Completed);
		task.setEndDate(new Date()); 
		Session session=context.getSession();
		if(opinion!=null){
			task.setOpinion(opinion.getOpinion());
		}
		session.update(task);
		/*
		if(task.getType().equals(TaskType.Participative)){
			session.createQuery("delete "+TaskParticipator.class.getName()+" where taskId=:taskId").setLong("taskId", task.getId()).executeUpdate();			
		}
		session.delete(task);
		*/
		ProcessService processService=context.getProcessService();
		ProcessInstance processInstance=processService.getProcessInstanceById(task.getProcessInstanceId());
		SchedulerService schedulerService=(SchedulerService)context.getApplicationContext().getBean(SchedulerService.BEAN_ID);
		ProcessDefinition process=processService.getProcessById(task.getProcessId());
		Node node=process.getNode(task.getNodeName());
		if(node instanceof TaskNode){
			TaskNode taskNode=(TaskNode)node;
			DueDefinition dueDefinition=taskNode.getCustomDueDefinition(processInstance, task, context.getApplicationContext());
			if(dueDefinition==null){
				dueDefinition=taskNode.getDueDefinition();
			}
			if(dueDefinition!=null){
				schedulerService.removeReminderJob(task);			
			}
			String taskListenerBean=taskNode.getTaskListenerBean();
			if(StringUtils.isNotEmpty(taskListenerBean)){
				TaskListener taskListener=(TaskListener)context.getApplicationContext().getBean(taskListenerBean);
				taskListener.onTaskComplete(context, task);
			}
		}
		context.getCommandService().executeCommand(new SaveHistoryTaskCommand(task,processInstance));
		if(variables!=null && variables.size()>0){
			context.getExpressionContext().addContextVariables(processInstance, variables);
			context.getCommandService().executeCommand(new SaveProcessInstanceVariablesCommand(processInstance, variables));			
		}
		node.leave(context,processInstance,flowName);
		return task;
	}
}
