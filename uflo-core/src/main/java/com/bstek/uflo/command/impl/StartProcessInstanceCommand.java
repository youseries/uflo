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

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.ProcessInstanceState;
import com.bstek.uflo.process.handler.ProcessEventHandler;
import com.bstek.uflo.process.node.StartNode;
import com.bstek.uflo.service.StartProcessInfo;
import com.bstek.uflo.utils.IDGenerator;
import com.bstek.uflo.utils.ProcessListenerUtils;

/**
 * @author Jacky.gao
 * @since 2013年7月31日
 */
public class StartProcessInstanceCommand implements Command<ProcessInstance> {
	private ProcessDefinition process;
	private Map<String,Object> variables;
	private StartProcessInfo startProcessInfo;
	private long parentProcessInstanceId;
	public StartProcessInstanceCommand(ProcessDefinition process,Map<String,Object> variables,StartProcessInfo startProcessInfo,long parentProcessInstanceId){
		this.process=process;
		this.variables=variables;
		this.startProcessInfo=startProcessInfo;
		this.parentProcessInstanceId=parentProcessInstanceId;
	}
	public ProcessInstance execute(Context context) {
		ProcessInstance processInstance=new ProcessInstance();
		long piId=IDGenerator.getInstance().nextId();
		processInstance.addMetadata(StartProcessInfo.KEY, startProcessInfo);
		processInstance.setId(piId);
		processInstance.setRootId(piId);
		processInstance.setState(ProcessInstanceState.Start);
		processInstance.setProcessId(process.getId());
		processInstance.setCreateDate(new Date());
		processInstance.setPromoter(startProcessInfo.getPromoter());
		processInstance.setBusinessId(startProcessInfo.getBusinessId());
		processInstance.setSubject(startProcessInfo.getSubject());
		processInstance.setTag(startProcessInfo.getTag());
		if(parentProcessInstanceId>0){
			processInstance.setParentId(parentProcessInstanceId);
		}
		processInstance.setHistoryProcessInstanceId(IDGenerator.getInstance().nextId());
		if(variables!=null && variables.size()>0){
			context.getCommandService().executeCommand(new SaveProcessInstanceVariablesCommand(processInstance, variables));
		}
		context.getExpressionContext().createContext(processInstance, variables);
		StartNode startNode=process.getStartNode();
		processInstance.setCurrentTask(startNode.getName());
		context.getSession().save(processInstance);
		context.getCommandService().executeCommand(new SaveHistoryProcessInstanceCommand(processInstance));
		processInstance.setCurrentNode(startNode.getName());
		ProcessListenerUtils.fireProcessStartListers(processInstance, context);
		String processEventHandlerBean=process.getEventHandlerBean();
		if(StringUtils.isNotEmpty(processEventHandlerBean)){
			ProcessEventHandler bean=(ProcessEventHandler)context.getApplicationContext().getBean(processEventHandlerBean);
			bean.start(processInstance, context);
		}
		startNode.createActivityHistory(context, processInstance);
		boolean isLeave=startNode.enter(context,processInstance);
		startNode.doEnterEventHandler(context, processInstance);
		if(isLeave){
			startNode.leave(context, processInstance, startProcessInfo.getSequenceFlowName());
		}
		return processInstance;
	}
}
