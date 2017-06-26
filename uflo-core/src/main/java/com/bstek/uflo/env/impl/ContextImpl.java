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
package com.bstek.uflo.env.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.expr.ExpressionContext;
import com.bstek.uflo.service.IdentityService;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.TaskService;

/**
 * @author Jacky.gao
 * @since 2013年7月30日
 */
public class ContextImpl implements ApplicationContextAware,Context{
	private SessionFactory sessionFactory;
	private ApplicationContext applicationContext;
	private CommandService commandService;
	private ProcessService processService;
	private ExpressionContext expressionContext;
	private IdentityService identityService;
	private TaskService taskService;
	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public CommandService getCommandService() {
		return commandService;
	}
	public void setCommandService(CommandService commandService) {
		this.commandService = commandService;
	}
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}
	public ProcessService getProcessService() {
		return processService;
	}
	public void setProcessService(ProcessService processService) {
		this.processService = processService;
	}
	public ExpressionContext getExpressionContext() {
		return expressionContext;
	}
	public void setExpressionContext(ExpressionContext expressionContext) {
		this.expressionContext = expressionContext;
	}
	public IdentityService getIdentityService() {
		return identityService;
	}
	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
	public TaskService getTaskService() {
		return taskService;
	}
}
