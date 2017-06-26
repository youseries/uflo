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
package com.bstek.uflo.service.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.command.impl.DeleteProcessDefinitionCommand;
import com.bstek.uflo.command.impl.DeleteProcessInstanceCommand;
import com.bstek.uflo.command.impl.DeleteProcessVariableCommand;
import com.bstek.uflo.command.impl.GetExpressionValueCommand;
import com.bstek.uflo.command.impl.GetProcessByKeyCommand;
import com.bstek.uflo.command.impl.GetProcessCommand;
import com.bstek.uflo.command.impl.GetProcessInstanceCommand;
import com.bstek.uflo.command.impl.GetProcessInstanceVariableCommand;
import com.bstek.uflo.command.impl.SaveProcessInstanceVariablesCommand;
import com.bstek.uflo.command.impl.StartProcessInstanceCommand;
import com.bstek.uflo.deploy.ProcessDeployer;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.variable.Variable;
import com.bstek.uflo.query.ProcessInstanceQuery;
import com.bstek.uflo.query.ProcessQuery;
import com.bstek.uflo.query.ProcessVariableQuery;
import com.bstek.uflo.query.impl.ProcessInstanceQueryImpl;
import com.bstek.uflo.query.impl.ProcessQueryImpl;
import com.bstek.uflo.query.impl.ProcessVariableQueryImpl;
import com.bstek.uflo.service.CacheService;
import com.bstek.uflo.service.ProcessInterceptor;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.StartProcessInfo;
import com.bstek.uflo.utils.EnvironmentUtils;

/**
 * @author Jacky.gao
 * @since 2013年7月29日
 */
public class DefaultProcessService implements ProcessService,ApplicationContextAware{
	private Collection<ProcessInterceptor> processInterceptors;
	private ProcessDeployer processDeployer;
	private CommandService commandService;
	
	public synchronized ProcessDefinition getProcessById(long processId) {
		CacheService cache=EnvironmentUtils.getEnvironment().getCache();
		if(cache.containsProcessDefinition(processId)){
			return cache.getProcessDefinition(processId);
		}else{
			ProcessDefinition process=commandService.executeCommand(new GetProcessCommand(processId,null,0,null));
			cache.putProcessDefinition(process.getId(),process);
			return process;
		}
	}
	
	public void deleteProcessVariable(String key, long processInstanceId) {
		commandService.executeCommand(new DeleteProcessVariableCommand(key,processInstanceId));
	}
	
	public ProcessDefinition getProcessByKey(String key) {
		CacheService cache=EnvironmentUtils.getEnvironment().getCache();
		for(ProcessDefinition pd:cache.loadAllProcessDefinitions()){
			if(pd.getKey()==null)continue;
			if(pd.getKey().equals(key)){
				return pd;
			}
		}
		ProcessDefinition process=commandService.executeCommand(new GetProcessByKeyCommand(key));
		if(process!=null){
			cache.putProcessDefinition(process.getId(), process);	
		}
		return process;
	}

	public ProcessDefinition getProcessByName(String processName) {
		return commandService.executeCommand(new GetProcessCommand(0,processName,0,null));
	}
	
	public ProcessDefinition getProcessByName(String processName,String categoryId) {
		return commandService.executeCommand(new GetProcessCommand(0,processName,0,categoryId));
	}
	
	public  synchronized ProcessDefinition getProcessByName(String processName, final int version) {
		CacheService cache=EnvironmentUtils.getEnvironment().getCache();
		ProcessDefinition target=null;
		for(ProcessDefinition p:cache.loadAllProcessDefinitions()){
			if(p.getName().equals(processName) && p.getVersion()==version){
				target=p;
				break;
			}
		}
		if(target!=null){
			return target;
		}else{
			ProcessDefinition process=commandService.executeCommand(new GetProcessCommand(0,processName,version,null));
			cache.putProcessDefinition(process.getId(), process);
			return process;
		}
	}
	
	public ProcessInstance startProcessByKey(String key,StartProcessInfo startProcessInfo) {
		ProcessDefinition process=getProcessByKey(key);
		if(process==null){
			throw new IllegalArgumentException("Process definition ["+key+"] was not exist!");
		}
		checkProcessEffectDate(process);
		return startProcess(process,startProcessInfo,startProcessInfo.getVariables());
	}

	public ProcessInstance startProcessById(long processId,StartProcessInfo startProcessInfo) {
		ProcessDefinition process=getProcessById(processId);
		if(process==null){
			throw new IllegalArgumentException("Process definition ["+processId+"] was not exist!");
		}
		checkProcessEffectDate(process);
		return startProcess(process,startProcessInfo,startProcessInfo.getVariables());
	}
	
	private void checkProcessEffectDate(ProcessDefinition process){
		Date effectDate=process.getEffectDate();
		if(effectDate==null){
			return;
		}
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(effectDate.getTime()>(new Date()).getTime()){
			throw new IllegalStateException("Process definition "+process.getName()+" effect date is "+sd.format(effectDate)+".");
		}
	}
	
	public ProcessInstance startProcessByName(String processName,StartProcessInfo startProcessInfo) {
		ProcessDefinition process=getProcessByName(processName);
		if(process==null){
			throw new IllegalArgumentException("Process definition ["+processName+"] was not exist!");
		}
		checkProcessEffectDate(process);
		return startProcess(process,startProcessInfo,startProcessInfo.getVariables());
	}

	private ProcessInstance startProcess(ProcessDefinition process,StartProcessInfo startProcessInfo,Map<String,Object> variables){
		return commandService.executeCommand(new StartProcessInstanceCommand(process,variables,startProcessInfo,0));
	}

	public ProcessInstance startProcessByName(String processName,StartProcessInfo startProcessInfo, int version) {
		ProcessDefinition process=getProcessByName(processName,version);
		if(process==null){
			throw new IllegalArgumentException("Process definition ["+processName+"-"+version+"] was not exist!");
		}
		checkProcessEffectDate(process);
		return startProcess(process,startProcessInfo,startProcessInfo.getVariables());
	}

	public synchronized ProcessDefinition deployProcess(ZipInputStream zipInputStream) {
		return processDeployer.deploy(zipInputStream);
	}
	public synchronized ProcessDefinition deployProcess(InputStream inputStream) {
		return processDeployer.deploy(inputStream);
	}
	
	public ProcessDefinition deployProcess(InputStream inputStream,long processId) {
		CacheService cache=EnvironmentUtils.getEnvironment().getCache();
		int version=getProcessById(processId).getVersion();
		ProcessDefinition process=processDeployer.deploy(inputStream,version,processId);
		cache.putProcessDefinition(processId, process);
		doProcessInterceptors(process,true);
		return process;
	}

	public synchronized ProcessInstance getProcessInstanceById(long processInstanceId) {
		return commandService.executeCommand(new GetProcessInstanceCommand(processInstanceId));
	}
	public void setProcessDeployer(ProcessDeployer processDeployer) {
		this.processDeployer = processDeployer;
	}

	public void setCommandService(CommandService commandService) {
		this.commandService = commandService;
	}

	public void saveProcessVariable(long processInstanceId,String key, Object value) {
		Map<String,Object> variables=new HashMap<String,Object>();
		variables.put(key, value);
		saveProcessVariables(processInstanceId, variables);
	}
	
	public void saveProcessVariables(long processInstanceId,Map<String, Object> variables) {
		ProcessInstance pi=getProcessInstanceById(processInstanceId);
		commandService.executeCommand(new SaveProcessInstanceVariablesCommand(pi,variables));
	}
	
	public List<Variable> getProcessVariables(long processInstanceId) {
		ProcessVariableQuery query=new ProcessVariableQueryImpl(commandService);
		query.processInstanceId(processInstanceId);
		return query.list();
	}

	public List<Variable> getProcessVariables(ProcessInstance processInstance) {
		ProcessVariableQuery query=new ProcessVariableQueryImpl(commandService);
		query.rootprocessInstanceId(processInstance.getRootId());
		return query.list();
	}

	public Object getProcessVariable(String key,ProcessInstance processInstance) {
		Object obj=commandService.executeCommand(new GetExpressionValueCommand(processInstance.getId(),key));
		if(obj!=null)return obj;
		Variable var=commandService.executeCommand(new GetProcessInstanceVariableCommand(key,processInstance));
		if(var!=null){
			return var.getValue();
		}
		return null;
	}

	public Object getProcessVariable(String key, long processInstanceId) {
		Object obj=commandService.executeCommand(new GetExpressionValueCommand(processInstanceId,key));
		if(obj!=null)return obj;
		ProcessInstance pi=getProcessInstanceById(processInstanceId);
		return getProcessVariable(key, pi);
	}

	public void deleteProcessInstance(ProcessInstance processInstance) {
		commandService.executeCommand(new DeleteProcessInstanceCommand(processInstance));
	}

	public void deleteProcessInstanceById(long processInstanceId) {
		deleteProcessInstance(getProcessInstanceById(processInstanceId));
	}

	public ProcessInstanceQuery createProcessInstanceQuery() {
		return new ProcessInstanceQueryImpl(commandService);
	}

	public ProcessVariableQuery createProcessVariableQuery() {
		return new ProcessVariableQueryImpl(commandService);
	}
	
	public ProcessQuery createProcessQuery() {
		return new ProcessQueryImpl(commandService);
	}
	
	public void deleteProcess(long processId) {
		ProcessDefinition processDefinition=getProcessById(processId);
		deleteProcess(processDefinition);
	}
	public void deleteProcess(String processKey) {
		ProcessDefinition processDefinition=getProcessByKey(processKey);
		deleteProcess(processDefinition);
	}
	
	public void updateProcessForMemory(long processId) {
		ProcessDefinition process=commandService.executeCommand(new GetProcessCommand(processId,null,0,null));
		if(process!=null){
			CacheService cache=EnvironmentUtils.getEnvironment().getCache();
			cache.putProcessDefinition(process.getId(), process);	
		}
	}
	
	public void deleteProcess(ProcessDefinition processDefinition) {
		commandService.executeCommand(new DeleteProcessDefinitionCommand(processDefinition));
		CacheService cache=EnvironmentUtils.getEnvironment().getCache();
		cache.removeProcessDefinition(processDefinition.getId());
		doProcessInterceptors(processDefinition,false);
	}
	
	public void deleteProcessFromMemory(long processId) {
		CacheService cache=EnvironmentUtils.getEnvironment().getCache();
		cache.removeProcessDefinition(processId);
	}
	public void deleteProcessFromMemory(ProcessDefinition processDefinition) {
		CacheService cache=EnvironmentUtils.getEnvironment().getCache();
		cache.removeProcessDefinition(processDefinition.getId());
	}
	public void deleteProcessFromMemory(String processKey) {
		ProcessDefinition processDefinition=getProcessByKey(processKey);
		CacheService cache=EnvironmentUtils.getEnvironment().getCache();
		cache.removeProcessDefinition(processDefinition.getId());
	}
	
	private void doProcessInterceptors(ProcessDefinition process,boolean update){
		for(ProcessInterceptor interceptor:processInterceptors){
			if(update){
				interceptor.updateProcess(process);
			}else{
				interceptor.deleteProcess(process);
			}
		}
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		processInterceptors=applicationContext.getBeansOfType(ProcessInterceptor.class).values();
	}
}
