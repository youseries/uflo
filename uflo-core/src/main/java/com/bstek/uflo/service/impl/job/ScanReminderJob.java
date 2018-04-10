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
package com.bstek.uflo.service.impl.job;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bstek.uflo.model.task.reminder.TaskReminder;
import com.bstek.uflo.service.SchedulerService;
import com.bstek.uflo.service.TaskService;

/**
 * @author Jacky.gao
 * @since 2017年12月19日
 */
public class ScanReminderJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScanReminderJobDetail detail=(ScanReminderJobDetail)context.getJobDetail();
		TaskService taskService=detail.getTaskService();
		List<Long> reminderTaskList=detail.getReminderTaskList();
		List<TaskReminder> reminders=taskService.getAllTaskReminders();
		SchedulerService schedulerService=detail.getSchedulerService();
		List<Long> list=new ArrayList<Long>();
		for(TaskReminder reminder:reminders){
			long taskId=reminder.getTaskId();
			list.add(reminder.getId());
			if(reminderTaskList.contains(taskId)){
				continue;
			}
			schedulerService.addReminderJob(reminder,null,null);
			reminderTaskList.add(reminder.getId());
		}
		List<Long> removeList=new ArrayList<Long>();
		for(long reminderId:reminderTaskList){
			if(list.contains(reminderId)){
				continue;
			}
			schedulerService.deleteJob(reminderId);
			removeList.add(reminderId);
		}
		for(Long reminderId:removeList){
			reminderTaskList.remove(reminderId);
		}
	}
}
