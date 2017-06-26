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

import java.util.Map;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.service.TaskOpinion;
import com.bstek.uflo.service.TaskService;

/**
 * @author Jacky.gao
 * @since 2013年9月24日
 */
public class WithdrawTaskCommand implements Command<Object> {
	private Map<String, Object> variables;
	private Task task;
	private TaskOpinion opinion;
	public WithdrawTaskCommand(Task task,Map<String, Object> variables,TaskOpinion opinion){
		this.variables=variables;
		this.opinion=opinion;
		this.task=task;
	}
	public Object execute(Context context) {
		TaskService taskService=context.getTaskService();
		if(!taskService.canWithdraw(task)){
			throw new IllegalArgumentException("Task "+task.getTaskName()+" can not forward to "+task.getPrevTask()+" node.");
		}
		taskService.forward(task, task.getPrevTask(), variables,opinion,TaskState.Withdraw);
		return null;
	}
}
