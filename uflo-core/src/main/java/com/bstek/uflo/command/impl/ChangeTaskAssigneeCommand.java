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

import org.apache.commons.lang.StringUtils;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.task.Task;

/**
 * @author Jacky.gao
 * @since 2013年10月10日
 */
public class ChangeTaskAssigneeCommand implements Command<Object> {
	private long taskId;
	private String username;
	public ChangeTaskAssigneeCommand(long taskId,String username){
		this.taskId=taskId;
		this.username=username;
	}
	public Object execute(Context context) {
		Task task=context.getTaskService().getTask(taskId);
		if(StringUtils.isNotEmpty(task.getOwner())){
			task.setAssignee(username);
			context.getSession().update(task);
		}else{
			throw new IllegalArgumentException("Task "+taskId+" is not the owner,So can not change task assignee.");
		}
		return null;
	}
}
