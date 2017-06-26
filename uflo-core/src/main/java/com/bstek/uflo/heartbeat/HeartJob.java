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

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bstek.uflo.model.Heartbeat;

/**
 * @author Jacky.gao
 * @since 2016年12月29日
 */
public class HeartJob implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
		HeartJobDetail detail=(HeartJobDetail)context.getJobDetail();
		String instanceName=detail.getCurrentInstanceName();
		Session session=detail.getSessionFactory().openSession();
		try{
			String hql="from "+Heartbeat.class.getName()+" b where b.instanceName=:instanceName order by b.date desc";
			Query query=session.createQuery(hql).setString("instanceName",instanceName);
			@SuppressWarnings("unchecked")
			List<Heartbeat> beats=query.list();
			Date now=new Date();
			Heartbeat beat=null;
			if(beats.size()>0){
				beat=beats.get(0);
			}else{
				beat=new Heartbeat();
				beat.setId(UUID.randomUUID().toString());
				beat.setInstanceName(instanceName);
			}
			beat.setDate(now);
			session.saveOrUpdate(beat);
		}catch(Exception ex){
			throw new JobExecutionException(ex);
		}finally{
			session.flush();
			session.close();
		}
	}
}
