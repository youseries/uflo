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
import com.bstek.uflo.model.HistoryTask;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.utils.EnvironmentUtils;
import com.bstek.uflo.utils.IDGenerator;

/**
 * @author Jacky.gao
 * @since 2013年7月31日
 */
public class SaveHistoryTaskCommand implements Command<HistoryTask> {
	private Task task;
	private ProcessInstance processInstance;
	public SaveHistoryTaskCommand(Task task,ProcessInstance processInstance){
		this.task=task;
		this.processInstance=processInstance;
	}
	public HistoryTask execute(Context context) {
		Session session=context.getSession();
		HistoryTask hisTask=new HistoryTask();
		hisTask.setId(IDGenerator.getInstance().nextId());
		hisTask.setDescription(task.getDescription());
		hisTask.setProcessId(task.getProcessId());
		hisTask.setHistoryProcessInstanceId(processInstance.getHistoryProcessInstanceId());
		hisTask.setCreateDate(task.getCreateDate());
		hisTask.setProcessInstanceId(processInstance.getRootId());
		hisTask.setRootProcessInstanceId(processInstance.getRootId());
		hisTask.setTaskName(task.getTaskName());
		hisTask.setAssignee(task.getAssignee());
		hisTask.setNodeName(task.getNodeName());
		hisTask.setState(task.getState());
		String loginUser=EnvironmentUtils.getEnvironment().getLoginUser();
		if(loginUser!=null && loginUser.equals(task.getAssignee())){
			hisTask.setEndDate(new Date());				
			hisTask.setOpinion(task.getOpinion());
		}		
		hisTask.setCreateDate(task.getCreateDate());
		hisTask.setBusinessId(task.getBusinessId());
		hisTask.setDuedate(task.getDuedate());
		hisTask.setOwner(task.getOwner());
		hisTask.setType(task.getType());
		hisTask.setUrl(task.getUrl());
		hisTask.setTaskId(task.getId());
		hisTask.setSubject(task.getSubject());
		session.save(hisTask);
		return hisTask;
	}

}
