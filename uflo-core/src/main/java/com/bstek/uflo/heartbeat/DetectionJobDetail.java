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
package com.bstek.uflo.heartbeat;

import org.hibernate.SessionFactory;
import org.quartz.impl.JobDetailImpl;

import com.bstek.uflo.service.SchedulerService;

/**
 * @author Jacky.gao
 * @since 2013-5-6
 */
public class DetectionJobDetail extends JobDetailImpl {
	private static final long serialVersionUID = 422572385273663713L;
	private SessionFactory sessionFactory;
	private String currentInstanceName;
	private String[] jobInstanceNames;
	private SchedulerService schedulerService;
	public DetectionJobDetail(SessionFactory sessionFactory,String currentInstanceName, String[] jobInstanceNames,SchedulerService schedulerService) {
		this.sessionFactory = sessionFactory;
		this.currentInstanceName = currentInstanceName;
		this.jobInstanceNames = jobInstanceNames;
		this.schedulerService=schedulerService;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public String getCurrentInstanceName() {
		return currentInstanceName;
	}
	public String[] getJobInstanceNames() {
		return jobInstanceNames;
	}
	public SchedulerService getSchedulerService() {
		return schedulerService;
	}
}
