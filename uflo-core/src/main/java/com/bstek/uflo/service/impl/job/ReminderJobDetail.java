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

import org.quartz.impl.JobDetailImpl;

import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.process.handler.ReminderHandler;

/**
 * @author Jacky.gao
 * @since 2013年8月21日
 */
public class ReminderJobDetail extends JobDetailImpl {
	private static final long serialVersionUID = -5400934958181696022L;
	private ProcessInstance processInstance;
	private Task task;
	private ReminderHandler reminderHandlerBean;
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public ReminderHandler getReminderHandlerBean() {
		return reminderHandlerBean;
	}

	public void setReminderHandlerBean(ReminderHandler reminderHandlerBean) {
		this.reminderHandlerBean = reminderHandlerBean;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
}
