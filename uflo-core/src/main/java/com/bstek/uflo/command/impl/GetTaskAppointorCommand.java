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

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.task.TaskAppointor;

/**
 * @author Jacky.gao
 * @since 2013年8月19日
 */
public class GetTaskAppointorCommand implements Command<List<TaskAppointor>> {
	private String taskNodeName;
	private long processInstanceId;
	public GetTaskAppointorCommand(String taskNodeName,long processInstanceId){
		this.taskNodeName=taskNodeName;
		this.processInstanceId=processInstanceId;
	}
	@SuppressWarnings("unchecked")
	public List<TaskAppointor> execute(Context context) {
		Criteria criteria=context.getSession().createCriteria(TaskAppointor.class);
		criteria.add(Restrictions.eq("processInstanceId", processInstanceId));
		criteria.add(Restrictions.eq("taskNodeName", taskNodeName));
		return criteria.list();
	}
}
