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

import org.hibernate.Session;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.HistoryTask;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskParticipator;
import com.bstek.uflo.model.task.TaskType;
import com.bstek.uflo.service.SchedulerService;

/**
 * @author Jacky.gao
 * @since 2017年5月2日
 */
public class DeleteTaskByNodeCommand implements Command<Integer> {
	private long processInstanceId;
	private String nodeName;
	public DeleteTaskByNodeCommand(long processInstanceId,String nodeName){
		this.processInstanceId=processInstanceId;
		this.nodeName=nodeName;
	}
	@SuppressWarnings("unchecked")
	public Integer execute(Context context) {
		Session session=context.getSession();
		String hql="from "+Task.class.getName()+" where nodeName=:nodeName and (processInstanceId=:processInstanceId or rootProcessInstanceId=:rootProcessInstanceId)";
		List<Task> list=session.createQuery(hql)
				.setString("nodeName", nodeName)
				.setLong("processInstanceId", processInstanceId)
				.setLong("rootProcessInstanceId", processInstanceId)
				.list();
		SchedulerService schedulerService=(SchedulerService)context.getApplicationContext().getBean(SchedulerService.BEAN_ID);
		for(Task task:list){
			if(task.getType().equals(TaskType.Participative)){
				session.createQuery("delete "+TaskParticipator.class.getName()+" where taskId=:taskId").setLong("taskId", task.getId()).executeUpdate();			
			}
			hql="delete "+HistoryTask.class.getName()+" where taskId=:taskId";
			session.createQuery(hql).setLong("taskId", task.getId()).executeUpdate();
			session.delete(task);
			schedulerService.removeReminderJob(task);
		}
		return list.size();
	}
}
