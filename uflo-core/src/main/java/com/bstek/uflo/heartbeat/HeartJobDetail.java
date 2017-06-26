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

/**
 * @author Jacky.gao
 * @since 2016年12月29日
 */
public class HeartJobDetail extends JobDetailImpl {
	private static final long serialVersionUID = 1653542848201958245L;
	private SessionFactory sessionFactory;
	private String currentInstanceName;
	public HeartJobDetail(SessionFactory sessionFactory,String currentInstanceName) {
		this.sessionFactory = sessionFactory;
		this.currentInstanceName = currentInstanceName;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public String getCurrentInstanceName() {
		return currentInstanceName;
	}
	public void setCurrentInstanceName(String currentInstanceName) {
		this.currentInstanceName = currentInstanceName;
	}
	
}
