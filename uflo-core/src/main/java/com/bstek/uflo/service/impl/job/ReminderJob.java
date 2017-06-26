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

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bstek.uflo.process.handler.ReminderHandler;

/**
 * @author Jacky.gao
 * @since 2013年8月21日
 */
public class ReminderJob implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ReminderJobDetail jobDetail=(ReminderJobDetail)context.getJobDetail();
		ReminderHandler handler=jobDetail.getReminderHandlerBean();
		handler.execute(jobDetail.getProcessInstance(), jobDetail.getTask());
	}
}
