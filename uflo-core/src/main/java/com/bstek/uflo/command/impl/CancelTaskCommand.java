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

import org.hibernate.Session;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.SchedulerService;
import com.bstek.uflo.service.TaskOpinion;

/**
 * @author Jacky.gao
 * @since 2017年5月2日
 */
public class CancelTaskCommand implements Command<Task> {
	private Task task;
	private TaskOpinion opinion;
	public CancelTaskCommand(Task task,TaskOpinion opinion){
		this.task=task;
		this.opinion=opinion;
	}
	public Task execute(Context context) {
		Session session=context.getSession();
		task.setState(TaskState.Canceled);
		if(opinion!=null){
			task.setOpinion(opinion.getOpinion());
			task.setEndDate(new Date());
		}
		session.update(task);
		ProcessService processService=context.getProcessService();
		ProcessInstance processInstance=processService.getProcessInstanceById(task.getProcessInstanceId());
		context.getCommandService().executeCommand(new SaveHistoryTaskCommand(task,processInstance));
		SchedulerService schedulerService=(SchedulerService)context.getApplicationContext().getBean(SchedulerService.BEAN_ID);
		schedulerService.removeReminderJob(task);
		return task;
	}
}
