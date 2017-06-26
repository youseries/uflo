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

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.apache.commons.lang.StringUtils;

import com.bstek.uflo.command.impl.SaveHistoryTaskCommand;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.CancelTaskInterceptor;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.model.task.TaskType;
import com.bstek.uflo.process.security.ComponentAuthority;
import com.bstek.uflo.service.StartProcessInfo;
import com.bstek.uflo.utils.IDGenerator;

/**
 * @author Jacky.gao
 * @since 2013年7月31日
 */
public class StartNode extends Node {
	private static final long serialVersionUID = 1L;
	private String taskName;
	private String url;
	private String formTemplate;
	private List<FormElement> formElements;
	private List<ComponentAuthority> componentAuthorities;
	@Override
	public boolean enter(Context context,ProcessInstance processInstance) {
		StartProcessInfo startProcessInfo=(StartProcessInfo)processInstance.getMetadata(StartProcessInfo.KEY);
		Task task=new Task();
		task.setId(IDGenerator.getInstance().nextId());
		task.setNodeName(getName());
		if(StringUtils.isEmpty(taskName)){
			taskName=getName();
		}
		task.setRootProcessInstanceId(processInstance.getRootId());
		task.setTaskName(context.getExpressionContext().evalString(processInstance, taskName));
		task.setDescription(getDescription());
		task.setCreateDate(new Date());
		task.setProcessId(getProcessId());
		task.setProcessInstanceId(processInstance.getId());
		task.setPrevTask(processInstance.getCurrentTask());
		task.setBusinessId(processInstance.getBusinessId());
		String targetUrl=getUrl();
		if(StringUtils.isEmpty(targetUrl)){
			targetUrl=getFormTemplate();
		}
		task.setUrl(context.getExpressionContext().evalString(processInstance, targetUrl));
		task.setType(TaskType.Normal);
		if(startProcessInfo!=null){
			task.setOwner(startProcessInfo.getPromoter());
			task.setSubject(startProcessInfo.getSubject());
			task.setAssignee(startProcessInfo.getPromoter());
			if(startProcessInfo.isCompleteStartTask()){
				task.setState(TaskState.Completed);
				task.setOpinion(startProcessInfo.getCompleteStartTaskOpinion());
				context.getCommandService().executeCommand(new SaveHistoryTaskCommand(task,processInstance));
				return true;
			}
		}else{
			task.setOwner(processInstance.getPromoter());
			task.setAssignee(processInstance.getPromoter());
			task.setSubject(processInstance.getSubject());
		}
		task.setState(TaskState.Created);
		context.getSession().save(task);			
		return false;
	}
	@Override
	public String leave(Context context,ProcessInstance processInstance,String flowName) {
		return leaveNode(context, processInstance, flowName);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void cancel(Context context, ProcessInstance processInstance) {
		Session session=context.getSession();
		Collection<Task> tasks=session.createCriteria(Task.class).add(Restrictions.eq("processInstanceId", processInstance.getId())).list();
		cancelTasks(context, processInstance, tasks);
	}

	private void cancelTasks(Context context, ProcessInstance processInstance, Collection<Task> tasks) {
		Session session=context.getSession();
		Collection<CancelTaskInterceptor> interceptors=context.getApplicationContext().getBeansOfType(CancelTaskInterceptor.class).values();
		for(Task task:tasks){
			for(CancelTaskInterceptor interceptor:interceptors){
				interceptor.intercept(context, task);
			}
			session.delete(task);
			task.setState(TaskState.Canceled);
			context.getCommandService().executeCommand(new SaveHistoryTaskCommand(task,processInstance));
		}
	}
	
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<FormElement> getFormElements() {
		return formElements;
	}
	public void setFormElements(List<FormElement> formElements) {
		this.formElements = formElements;
	}
	public String getFormTemplate() {
		return formTemplate;
	}
	public void setFormTemplate(String formTemplate) {
		this.formTemplate = formTemplate;
	}
	public List<ComponentAuthority> getComponentAuthorities() {
		return componentAuthorities;
	}
	public void setComponentAuthorities(
			List<ComponentAuthority> componentAuthorities) {
		this.componentAuthorities = componentAuthorities;
	}
}
