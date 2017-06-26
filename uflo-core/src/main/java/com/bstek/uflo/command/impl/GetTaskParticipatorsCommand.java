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

import org.hibernate.criterion.Restrictions;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.task.TaskParticipator;

/**
 * @author Jacky.gao
 * @since 2013年9月10日
 */
public class GetTaskParticipatorsCommand implements Command<List<TaskParticipator>> {
	private long taskId;
	public GetTaskParticipatorsCommand(long taskId){
		this.taskId=taskId;
	}
	@SuppressWarnings("unchecked")
	public List<TaskParticipator> execute(Context context) {
		return context.getSession().createCriteria(TaskParticipator.class).add(Restrictions.eq("taskId", taskId)).list();
	}

}
