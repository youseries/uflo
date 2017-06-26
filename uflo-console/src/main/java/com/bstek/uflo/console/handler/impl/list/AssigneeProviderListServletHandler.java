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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.uflo.console.handler.impl.WriteJsonServletHandler;
import com.bstek.uflo.process.assign.Assignee;
import com.bstek.uflo.process.assign.AssigneeProvider;
import com.bstek.uflo.process.assign.Entity;
import com.bstek.uflo.process.assign.PageQuery;
import com.bstek.uflo.utils.EnvironmentUtils;

/**
 * @author Jacky.gao
 * @since 2016年12月8日
 */
public class AssigneeProviderListServletHandler extends WriteJsonServletHandler implements ApplicationContextAware {
	Map<String,AssigneeProvider> assigneeProviderMaps=new HashMap<String,AssigneeProvider>();
	private boolean debug;
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginUser=EnvironmentUtils.getEnvironment().getLoginUser();
		if(loginUser==null && !debug){
			throw new IllegalArgumentException("Current run mode is not debug.");			
		}
		request.setCharacterEncoding("utf-8");
		String pageIndex=request.getParameter("pageIndex");
		if(StringUtils.isEmpty(pageIndex))pageIndex="1";
		String pageSize=request.getParameter("pageSize");
		if(StringUtils.isEmpty(pageSize))pageSize="1000";
		String parentId=request.getParameter("parentId");
		if(parentId!=null && parentId.equals("null")){
			parentId=null;
		}
		String providerId=request.getParameter("providerId");
		PageQuery<Entity> pageQuery= new PageQuery<Entity>(Integer.valueOf(pageIndex),Integer.valueOf(pageSize));
		if(StringUtils.isNotEmpty(providerId)){
			AssigneeInfo info = buildAssigneeInfo(parentId, pageQuery,providerId,true);
			writeObjectToJson(response,info);
		}else{
			List<AssigneeInfo> result=new ArrayList<AssigneeInfo>();
			for(String beanId:assigneeProviderMaps.keySet()){
				AssigneeInfo info = buildAssigneeInfo(parentId, pageQuery,beanId,false);
				result.add(info);
			}
			writeObjectToJson(response,result);
		}
	}
	
	private AssigneeInfo buildAssigneeInfo(String parentId,PageQuery<Entity> pageQuery, String beanId,boolean buildEntity) {
		AssigneeProvider provider=assigneeProviderMaps.get(beanId);
		provider.queryEntities(pageQuery, parentId);
		AssigneeInfo info=new AssigneeInfo();
		info.setName(provider.getName());
		info.setTree(provider.isTree());
		info.setProviderId(beanId);
		if(buildEntity){
			List<Assignee> assignees=new ArrayList<Assignee>();
			Collection<Entity> entitys=pageQuery.getResult();
			if(entitys!=null){
				for(Entity entity:entitys){
					Assignee assignee=new Assignee();
					assignee.setId(entity.getId());
					assignee.setName(entity.getName());
					assignee.setProviderId(beanId);
					assignee.setChosen(entity.isChosen());
					assignees.add(assignee);
				}
			}
			info.setAssignees(assignees);
			info.setCount(pageQuery.getRecordCount());
		}
		return info;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		Map<String,AssigneeProvider> map=applicationContext.getBeansOfType(AssigneeProvider.class);
		for(String beanId:map.keySet()){
			AssigneeProvider provider=map.get(beanId);
			if(!provider.disable()){
				assigneeProviderMaps.put(beanId, provider);
			}
		}
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public String url() {
		return "/assignproviderlist";
	}
}
