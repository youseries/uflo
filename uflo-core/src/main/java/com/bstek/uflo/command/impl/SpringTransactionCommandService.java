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

import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.env.EnvironmentProvider;
import com.bstek.uflo.env.impl.ContextImpl;
import com.bstek.uflo.expr.ExpressionContext;
import com.bstek.uflo.service.IdentityService;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.TaskService;
import com.bstek.uflo.utils.EnvironmentUtils;

/**
 * @author Jacky.gao
 * @since 2013年7月30日
 */
public class SpringTransactionCommandService implements CommandService,ApplicationContextAware {
	private ContextImpl context;
	private SessionFactory sessionFactory;
	private PlatformTransactionManager platformTransactionManager;
	private int springPropagationBehaviour = TransactionDefinition.PROPAGATION_REQUIRED;
	private int newSpringPropagationBehaviour = TransactionDefinition.PROPAGATION_REQUIRES_NEW;
	public <T> T executeCommand(final Command<T> command) {
		TransactionTemplate template = new TransactionTemplate(platformTransactionManager);
	    template.setPropagationBehavior(springPropagationBehaviour);
	    return template.execute(new TransactionCallback<T>() {
			public T doInTransaction(TransactionStatus status) {
				return command.execute(context);
			}
	    	
		});
	}
	
	public <T> T executeCommandInNewTransaction(final Command<T> command) {
		TransactionTemplate template = new TransactionTemplate(platformTransactionManager);
		template.setPropagationBehavior(newSpringPropagationBehaviour);
		return template.execute(new TransactionCallback<T>() {
			public T doInTransaction(TransactionStatus status) {
				return command.execute(context);
			}
			
		});
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.platformTransactionManager=EnvironmentUtils.getEnvironment().getPlatformTransactionManager();
		if(this.platformTransactionManager==null){
			throw new RuntimeException("The "+EnvironmentProvider.class.getName()+" implements class's method 'getPlatformTransactionManager' can not return null.");
		}
		this.sessionFactory=EnvironmentUtils.getEnvironment().getSessionFactory();
		if(this.sessionFactory==null){
			throw new RuntimeException("The "+EnvironmentProvider.class.getName()+" implements class's method 'getSessionFactory' can not return null.");
		}
		context=new ContextImpl();
		context.setCommandService(this);
		context.setApplicationContext(applicationContext);
		context.setSessionFactory(sessionFactory);
		context.setProcessService((ProcessService)applicationContext.getBean(ProcessService.BEAN_ID));
		context.setExpressionContext((ExpressionContext)applicationContext.getBean(ExpressionContext.BEAN_ID));
		context.setIdentityService((IdentityService)applicationContext.getBean(IdentityService.BEAN_ID));
		context.setTaskService((TaskService)applicationContext.getBean(TaskService.BEAN_ID));
	}
}
