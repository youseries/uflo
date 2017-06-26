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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.bstek.uflo.model.Heartbeat;
import com.bstek.uflo.service.SchedulerService;
import com.bstek.uflo.utils.EnvironmentUtils;

/**
 * @author Jacky.gao
 * @since 2013-5-6
 */
public class HeartbeatDetectionJob implements Job{
	private String heartJobCronExpression="0/30 * * * * ?";
	public void execute(JobExecutionContext context) throws JobExecutionException {
		DetectionJobDetail jobDetail=(DetectionJobDetail)context.getJobDetail();
		Session session=jobDetail.getSessionFactory().openSession();
		try {
			String currentInstanceName=jobDetail.getCurrentInstanceName();
			Operation operation=detection(session,jobDetail.getJobInstanceNames(),currentInstanceName);
			if(operation.equals(Operation.reset)){
				SchedulerService service=jobDetail.getSchedulerService();
				service.resetScheduer();
				
				Heartbeat beat=new Heartbeat();
				Calendar c=Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.SECOND, 1);
				beat.setDate(c.getTime());
				beat.setId(UUID.randomUUID().toString());
				beat.setInstanceName(currentInstanceName);
				session.save(beat);
				
				initHeartJob(currentInstanceName, service.getScheduler());
			}
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}finally{
			session.flush();
			session.close();
		}
	}
	
	private void initHeartJob(String currentInstanceName,Scheduler scheduler) throws Exception{
		HeartJobDetail heartJobDetail=buildHeartJobDetail(currentInstanceName);
		Trigger heartJobTrigger=buildHeartJobTrigger();
		scheduler.scheduleJob(heartJobDetail, heartJobTrigger);
	}
	
	private HeartJobDetail buildHeartJobDetail(String currentInstanceName){
		SessionFactory sessionFactory=EnvironmentUtils.getEnvironment().getSessionFactory();
		HeartJobDetail jobDetail=new HeartJobDetail(sessionFactory,currentInstanceName);
		jobDetail.setKey(new JobKey("UfloHeartJob","uflo_background_system_job"));
		jobDetail.setJobClass(HeartJob.class);
		return jobDetail;
	}
	
	private Trigger buildHeartJobTrigger() {
		CronTriggerImpl trigger=new CronTriggerImpl();
		trigger.setName("UfloHeartJobTrigger");
		try {
			trigger.setCronExpression(heartJobCronExpression);
			return trigger;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * 当实例列表中只有一个，且是当前实例时就重启
	 * @param session Hibernate Session对象
	 * @param instanceNames 排队等待的实例名列表，如InsA,InsB,InsC,InsD
	 * @param currentInstanceName 当前服务器实例名
	 */
	@SuppressWarnings("unchecked")
	private Operation detection(Session session,String[] clusterJobInstanceNames,String currentInstanceName) {
		Query query=session.createQuery("from "+Heartbeat.class.getName()+" b order by b.date desc");
		List<Heartbeat> heartbeats=query.setMaxResults(1).list();
		int currentPos=getPosition(clusterJobInstanceNames, currentInstanceName)+1;
		if(heartbeats.size()>0){
			Date now=new Date();
			Heartbeat heartbeat=heartbeats.get(0);
			Date beatDate=heartbeat.getDate();
			Calendar beatCalendar=Calendar.getInstance();
			beatCalendar.setTime(beatDate);
			String beatInstanceName=heartbeat.getInstanceName();
			int secondUnit=40;
			int beatPos=getPosition(clusterJobInstanceNames, beatInstanceName)+1;
			if(!currentInstanceName.equals(beatInstanceName)){
				int currentSecond=currentPos*secondUnit;
				if(currentPos>beatPos){
					beatCalendar.add(Calendar.SECOND,currentSecond);
				}else if(currentPos<beatPos){
					currentSecond=(currentPos+(clusterJobInstanceNames.length-beatPos))*secondUnit;
					beatCalendar.add(Calendar.SECOND,currentSecond);
				}
			}else{
				beatCalendar.add(Calendar.SECOND,secondUnit*clusterJobInstanceNames.length);				
			}
			if(now.compareTo(beatCalendar.getTime())>0){
				//当前时间大于心跳时间+currentSecond,说明当前运行JOB的实例挂了
				return Operation.reset;
			}
		}else{
			if(currentPos==1)return Operation.reset;
		}
		return Operation.donothing;
	}
	private int getPosition(String[] instanceNames,String instanceName){
		int pos=0;
		for(int i=0;i<instanceNames.length;i++){
			String name=instanceNames[i];
			if(name.equals(instanceName)){
				pos=i;
			}
		}
		return pos;
	}
	enum Operation{
		reset,donothing
	}
}
