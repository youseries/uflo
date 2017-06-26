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
package com.bstek.uflo.env;

import org.hibernate.Session;
import org.springframework.context.ApplicationContext;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.expr.ExpressionContext;
import com.bstek.uflo.service.IdentityService;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.TaskService;

/**
 * @author Jacky.gao
 * @since 2013年8月12日
 */
public interface Context {
	Session getSession();
	ApplicationContext getApplicationContext();
	CommandService getCommandService();
	ProcessService getProcessService();
	ExpressionContext getExpressionContext();
	IdentityService getIdentityService();
	TaskService getTaskService();
}
