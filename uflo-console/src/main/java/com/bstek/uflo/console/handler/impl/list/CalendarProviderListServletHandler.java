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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.uflo.console.handler.impl.WriteJsonServletHandler;
import com.bstek.uflo.process.node.reminder.CalendarInfo;
import com.bstek.uflo.process.node.reminder.CalendarProvider;
import com.bstek.uflo.utils.EnvironmentUtils;

/**
 * @author Jacky.gao
 * @since 2016年12月8日
 */
public class CalendarProviderListServletHandler extends WriteJsonServletHandler implements ApplicationContextAware{
	private Collection<CalendarProvider> calendarProviders=null;
	
	private boolean debug;
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String loginUser=EnvironmentUtils.getEnvironment().getLoginUser();
		if(loginUser==null && !debug){
			throw new IllegalArgumentException("Current run mode is not debug.");			
		}
		writeObjectToJson(resp, buildCalendarInfos());
	}
	
	private List<CalendarInfo> buildCalendarInfos(){
		List<CalendarInfo> providers=new ArrayList<CalendarInfo>();
		for(CalendarProvider provider:calendarProviders){
			providers.addAll(provider.getCalendarInfos());				
		}
		return providers;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		calendarProviders=applicationContext.getBeansOfType(CalendarProvider.class).values();
		
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public String url() {
		return "/calendarproviderlist";
	}
}
