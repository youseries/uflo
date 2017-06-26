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

import org.hibernate.Session;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskType;
import com.bstek.uflo.query.TaskQuery;

/**
 * @author Jacky.gao
 * @since 2013年10月15日
 */
public class DeleteCountersignCommand implements Command<Object> {
	private Task task;
	public DeleteCountersignCommand(Task task){
		this.task=task;
	}
	public Object execute(Context context) {
		if(!TaskType.Countersign.equals(task.getType())){
			throw new IllegalArgumentException("Task "+task.getId()+" is not a countersign task.");
		}
		Session session=context.getSession();
		TaskQuery query=context.getTaskService().createTaskQuery();
		query.processInstanceId(task.getProcessInstanceId());
		query.nodeName(task.getNodeName());
		int count=0;
		for(Task t:query.list()){
			count=t.getCountersignCount();
			t.setCountersignCount(count-1);
			session.update(t);
		}
		session.delete(task);
		return null;
	}
}
