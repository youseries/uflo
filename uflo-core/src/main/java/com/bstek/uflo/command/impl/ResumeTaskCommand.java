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

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskState;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class ResumeTaskCommand implements Command<Task> {
	private Task task;
	public ResumeTaskCommand(Task task){
		this.task=task;
	}
	public Task execute(Context context) {
		TaskState state=task.getState();
		if(!state.equals(TaskState.Suspended)){
			throw new IllegalStateException("Only state is suspended task can be resume!");
		}
		task.setState(task.getPrevState());
		task.setPrevState(state);
		context.getSession().update(task);
		return null;
	}

}
