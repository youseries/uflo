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

import java.util.List;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.process.node.TaskNode;
import com.bstek.uflo.service.ProcessService;

/**
 * @author Jacky.gao
 * @since 2013年10月20日
 */
public class GetTaskNodeAssigneesCommand implements Command<List<String>> {
	private long taskId;
	private String taskNodeName;
	public GetTaskNodeAssigneesCommand(long taskId,String taskNodeName){
		this.taskId=taskId;
		this.taskNodeName=taskNodeName;
	}
	public List<String> execute(Context context) {
		ProcessService processService=context.getProcessService();
		Task task=context.getTaskService().getTask(taskId);
		ProcessInstance processInstance=processService.getProcessInstanceById(task.getProcessInstanceId());
		ProcessDefinition pd=processService.getProcessById(processInstance.getProcessId());
		TaskNode node=(TaskNode)pd.getNode(taskNodeName);
		return node.getAssigneeUsers(context, processInstance);
	}
}
