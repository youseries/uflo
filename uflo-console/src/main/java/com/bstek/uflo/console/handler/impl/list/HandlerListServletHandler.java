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
package com.bstek.uflo.console.handler.impl.list;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.uflo.console.handler.impl.WriteJsonServletHandler;
import com.bstek.uflo.process.handler.ActionHandler;
import com.bstek.uflo.process.handler.AssignmentHandler;
import com.bstek.uflo.process.handler.ConditionHandler;
import com.bstek.uflo.process.handler.CountersignHandler;
import com.bstek.uflo.process.handler.DecisionHandler;
import com.bstek.uflo.process.handler.ForeachHandler;
import com.bstek.uflo.process.handler.NodeEventHandler;
import com.bstek.uflo.process.handler.ProcessEventHandler;
import com.bstek.uflo.process.handler.ReminderHandler;
import com.bstek.uflo.process.node.FormTemplateProvider;
import com.bstek.uflo.utils.EnvironmentUtils;

/**
 * @author Jacky.gao
 * @since 2016年12月8日
 */
public class HandlerListServletHandler extends WriteJsonServletHandler implements ApplicationContextAware{
	private Map<String,Set<String>> handerMap;
	private boolean debug;
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String loginUser=EnvironmentUtils.getEnvironment().getLoginUser();
		if(loginUser==null && !debug){
			throw new IllegalArgumentException("Current run mode is not debug.");			
		}
		String handler=req.getParameter("handler");
		if(StringUtils.isEmpty(handler)){
			throw new IllegalArgumentException("Parameter handler can not be null");
		}
		writeObjectToJson(resp, handerMap.get(handler));
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		handerMap=new HashMap<String,Set<String>>();
		handerMap.put(NodeEventHandler.class.getSimpleName(), applicationContext.getBeansOfType(NodeEventHandler.class).keySet());
		handerMap.put(ProcessEventHandler.class.getSimpleName(), applicationContext.getBeansOfType(ProcessEventHandler.class).keySet());
		handerMap.put(DecisionHandler.class.getSimpleName(), applicationContext.getBeansOfType(DecisionHandler.class).keySet());
		handerMap.put(AssignmentHandler.class.getSimpleName(), applicationContext.getBeansOfType(AssignmentHandler.class).keySet());
		handerMap.put(ConditionHandler.class.getSimpleName(), applicationContext.getBeansOfType(ConditionHandler.class).keySet());
		handerMap.put(ActionHandler.class.getSimpleName(), applicationContext.getBeansOfType(ActionHandler.class).keySet());
		handerMap.put(ForeachHandler.class.getSimpleName(), applicationContext.getBeansOfType(ForeachHandler.class).keySet());
		handerMap.put(ReminderHandler.class.getSimpleName(), applicationContext.getBeansOfType(ReminderHandler.class).keySet());
		handerMap.put(CountersignHandler.class.getSimpleName(), applicationContext.getBeansOfType(CountersignHandler.class).keySet());
		Set<String> set=new HashSet<String>();
		for(FormTemplateProvider provider:applicationContext.getBeansOfType(FormTemplateProvider.class).values()){
			set.add(provider.getFormTemplate());
		}
		handerMap.put(FormTemplateProvider.class.getSimpleName(), set);
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public String url() {
		return "/handlerlist";
	}
}
