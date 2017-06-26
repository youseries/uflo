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
package com.bstek.uflo.expr.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.JexlException;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.uflo.expr.ExpressionContext;
import com.bstek.uflo.expr.ExpressionProvider;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.variable.Variable;
import com.bstek.uflo.query.ProcessInstanceQuery;
import com.bstek.uflo.service.CacheService;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.utils.EnvironmentUtils;


/**
 * @author Jacky.gao
 * @since 2013年8月8日
 */
public class ExpressionContextImpl implements ExpressionContext,ApplicationContextAware,InitializingBean {
	private Log log=LogFactory.getLog(getClass());
	private Collection<ExpressionProvider> providers;
	private static final JexlEngine jexl = new JexlEngine();
	private ProcessService processService;
    static {
    	//jexl.setCache(512);
       jexl.setLenient(false);
       jexl.setSilent(false);
    }
	public MapContext createContext(ProcessInstance processInstance,Map<String,Object> variables){
		ProcessMapContext context=new ProcessMapContext();
		if(variables!=null && variables.size()>0){
			for(String key:variables.keySet()){
				context.set(key, variables.get(key));
			}
		}
		for(ExpressionProvider provider:providers){
			if(provider.support(processInstance)){
				Map<String,Object> data=provider.getData(processInstance);
				if(data!=null && data.size()>0){
					for(String key:data.keySet()){
						context.set(key, data.get(key));
					}
				}
			}
		}
		CacheService cacheService=EnvironmentUtils.getEnvironment().getCache();
		cacheService.putContext(processInstance.getId(), context);
		return context;
	}
	
	public boolean removeContext(ProcessInstance processInstance){
		long id=processInstance.getId();
		CacheService cacheService=EnvironmentUtils.getEnvironment().getCache();
		if(cacheService.containsContext(id)){
			cacheService.removeContext(id);
			return true;
		}
		return false;
	}
	
	public void removeContextVariables(long processInstanceId, String key) {
		CacheService cacheService=EnvironmentUtils.getEnvironment().getCache();
		ProcessMapContext context=cacheService.getContext(processInstanceId);
		if(context!=null){
			ProcessMapContext processMap=(ProcessMapContext)context;
			processMap.getMap().remove(key);
		}
	}

	
	public synchronized String evalString(ProcessInstance processInstance, String str) {
		return parseExpression(str,processInstance);
	}

	public synchronized Object eval(long processInstanceId, String expression) {
		expression=expression.trim();
		if(expression.startsWith("${") && expression.endsWith("}")){
			expression=expression.substring(2,expression.length()-1);
		}else{
			return expression;
		}
		CacheService cacheService=EnvironmentUtils.getEnvironment().getCache();
		ProcessMapContext context=cacheService.getContext(processInstanceId);
		if(context==null){
			buildProcessInstanceContext(processService.getProcessInstanceById(processInstanceId));
			context=cacheService.getContext(processInstanceId);
		}
		if(context==null){
			log.warn("ProcessInstance "+processInstanceId+" variable context is not exist!");
			return null;
		}
		Object obj=null;
		try{
			obj=jexl.createExpression(expression).evaluate(context);
		}catch(JexlException ex){
			log.info("Named "+expression+" variable was not found in ProcessInstance "+processInstanceId);
		}
		return obj;			
	}
	
	private void buildProcessInstanceContext(ProcessInstance processInstance){
		List<Variable> variables=processService.getProcessVariables(processInstance.getId());
		Map<String,Object> variableMap=new HashMap<String,Object>();
		for(Variable var:variables){
			variableMap.put(var.getKey(), var.getValue());
		}
		createContext(processInstance, variableMap);
	}
	
	private String parseExpression(String str,ProcessInstance processInstance){
		if(StringUtils.isEmpty(str))return str;
		Pattern p=Pattern.compile("\\$\\{[^\\}][0-9a-zA-Z]([^\\}]*[0-9a-zA-Z])\\}");
		Matcher m=p.matcher(str);
		StringBuffer sb=new StringBuffer();
		int i=0;
		while(m.find()){
			String expr=m.group();
			Object obj=eval(processInstance, expr);
			String evalValue=(obj==null?null:obj.toString());
			sb.append(str.substring(i, m.start()));
			sb.append(evalValue);
			i=m.end();
		}
		if(sb.length()>0){
			sb.append(str.substring(i));
			return sb.toString();			
		}else{
			return str;
		}
	}

	public synchronized Object eval(ProcessInstance processInstance, String expression) {
		return getProcessExpressionValue(processInstance,expression);
	}
 
	private Object getProcessExpressionValue(ProcessInstance processInstance,String expression){
		Object obj=eval(processInstance.getId(),expression);
		if(obj!=null){
			return obj;
		}else if(processInstance.getParentId()>0){
			ProcessInstance parentProcessInstance=processService.getProcessInstanceById(processInstance.getParentId());
			return getProcessExpressionValue(parentProcessInstance, expression);
		}else{
			List<ProcessInstance> children=new ArrayList<ProcessInstance>();
			retriveAllChildProcessInstance(children,processInstance.getId());
			for(ProcessInstance pi:children){
				Object result=eval(pi.getId(),expression);
				if(result!=null){
					return result;
				}
			}
			return null;
		}
	}
	
	private void retriveAllChildProcessInstance(List<ProcessInstance> children,long parentId){
		ProcessInstanceQuery query=processService.createProcessInstanceQuery();
		query.parentId(parentId);
		List<ProcessInstance> list=query.list();
		for(ProcessInstance pi:list){
			retriveAllChildProcessInstance(children,pi.getId());
		}
		children.addAll(list);
	}
	
	public void addContextVariables(ProcessInstance processInstance,Map<String, Object> variables) {
		if(variables==null || variables.size()<1){
			return ;
		}
		CacheService cacheService=EnvironmentUtils.getEnvironment().getCache();
		ProcessMapContext context=cacheService.getContext(processInstance.getId());
		if(context==null){
			buildProcessInstanceContext(processInstance);
			context=cacheService.getContext(processInstance.getId());
		}
		if(context==null){
			throw new IllegalArgumentException("ProcessInstance ["+processInstance.getId()+"] expression context is not exist!");
		}
		for(String key:variables.keySet()){
			context.set(key, variables.get(key));
		}
	}
	
	public void moveContextToParent(ProcessInstance processInstance) {
		long parentId=processInstance.getParentId();
		if(parentId<1){
			return;
		}
		CacheService cacheService=EnvironmentUtils.getEnvironment().getCache();
		ProcessMapContext parentContext=cacheService.getContext(parentId);
		if(parentContext==null){
			buildProcessInstanceContext(processService.getProcessInstanceById(parentId));
			parentContext=cacheService.getContext(parentId);
		}
		if(parentContext==null){
			throw new IllegalArgumentException("ProcessInstance "+parentId+" context is not exist!");
		}
		ProcessMapContext context=cacheService.getContext(processInstance.getId());
		if(context==null){
			buildProcessInstanceContext(processInstance);
			context=cacheService.getContext(processInstance.getId());
		}
		if(context==null){
			throw new IllegalArgumentException("ProcessInstance "+processInstance.getId()+" context is not exist!");
		}
		Map<String,Object> map=((ProcessMapContext)context).getMap();
		for(String key:map.keySet()){
			parentContext.set(key, map.get(key));
		}
	}

	public void setProcessService(ProcessService processService) {
		this.processService = processService;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		providers=applicationContext.getBeansOfType(ExpressionProvider.class).values();
	}
	
	public void initExpressionContext(){
		CacheService cacheService=EnvironmentUtils.getEnvironment().getCache();
		ProcessInstanceQuery query=processService.createProcessInstanceQuery();
		List<ProcessInstance> instances=query.list();
		for(ProcessInstance pi:instances){
			ProcessMapContext context=new ProcessMapContext();
			for(Variable var:processService.getProcessVariables(pi.getId())){
				context.set(var.getKey(),var.getValue());
			}
			cacheService.putContext(pi.getId(),context);
		}
	}

	public void afterPropertiesSet() throws Exception {
		//initExpressionContext();
	}
}
