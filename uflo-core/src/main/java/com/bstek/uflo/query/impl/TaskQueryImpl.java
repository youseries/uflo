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
package com.bstek.uflo.query.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.command.impl.QueryCountCommand;
import com.bstek.uflo.command.impl.QueryListCommand;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.model.task.TaskType;
import com.bstek.uflo.query.QueryJob;
import com.bstek.uflo.query.TaskQuery;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class TaskQueryImpl implements TaskQuery,QueryJob{
	private String url;
	private String businessId;
	private String owner;
	private long processInstanceId;
	private Date createDateLessThen;
	private Date createDateLessThenOrEquals;
	private Date createDateGreaterThen;
	private Date createDateGreaterThenOrEquals;
	private long rootProcessInstanceId;
	private Date dueDateLessThen;
	private Date dueDateLessThenOrEquals;
	private Date dueDateGreaterThen;
	private Date dueDateGreaterThenOrEquals;
	private Boolean countersign;
	private TaskType type;
	private String name;
	private String subject;
	private String nodeName;
	private Integer progress;
	private String priority;
	private int firstResult;
	private int maxResults;
	private List<TaskState> states=new ArrayList<TaskState>();
	private List<TaskState> prevstates=new ArrayList<TaskState>();
	private List<String> ascOrders=new ArrayList<String>();
	private List<String> descOrders=new ArrayList<String>();
	private List<String> participators=new ArrayList<String>();
	private List<String> assignees=new ArrayList<String>();
	private List<Long> processIds=new ArrayList<Long>();
	private CommandService commandService;
	public TaskQueryImpl(CommandService commandService){
		this.commandService=commandService;
	}
	public List<Task> list() {
		return commandService.executeCommand(new QueryListCommand<List<Task>>(this));
	}

	public int count() {
		return commandService.executeCommand(new QueryCountCommand(this));
	}

	public Criteria getCriteria(Session session,boolean queryCount) {
		Criteria criteria=session.createCriteria(Task.class);
		buildCriteria(criteria,queryCount);
		return criteria;
	}

	private void buildCriteria(Criteria criteria,boolean queryCount){
		if(!queryCount && firstResult>0){
			criteria.setFirstResult(firstResult);			
		}
		if(!queryCount && maxResults>0){
			criteria.setMaxResults(maxResults);			
		}
		if(processIds.size()>0){
			if(processIds.size()==1){
				long processId=processIds.get(0);
				criteria.add(Restrictions.eq("processId",processId));
			}else{
				criteria.add(Restrictions.in("processId",processIds));				
			}
		}
		if(StringUtils.isNotEmpty(owner)){
			criteria.add(Restrictions.eq("owner", owner));
		}
		if(assignees.size()>0){
			if(assignees.size()==1){
				String assignee=assignees.get(0);
				criteria.add(Restrictions.eq("assignee", assignee));
			}else{
				criteria.add(Restrictions.in("assignee", assignees));				
			}
		}
		if(countersign!=null){
			criteria.add(Restrictions.eq("countersign", countersign));
		}
		if(type!=null){
			criteria.add(Restrictions.eq("type", type));
		}
		if(processInstanceId>0){
			criteria.add(Restrictions.eq("processInstanceId",processInstanceId));
		}
		if(rootProcessInstanceId>0){
			criteria.add(Restrictions.eq("rootProcessInstanceId",rootProcessInstanceId));
		}
		if(StringUtils.isNotEmpty(url)){
			criteria.add(Restrictions.like("url", url));
		}
		if(StringUtils.isNotEmpty(subject)){
			criteria.add(Restrictions.like("subject", subject));
		}
		if(StringUtils.isNotEmpty(name)){
			criteria.add(Restrictions.like("taskName", name));
		}
		if(StringUtils.isNotEmpty(nodeName)){
			criteria.add(Restrictions.eq("nodeName", nodeName));
		}
		if(StringUtils.isNotEmpty(businessId)){
			criteria.add(Restrictions.eq("businessId", businessId));
		}
		if(StringUtils.isNotEmpty(priority)){
			criteria.add(Restrictions.eq("priority", priority));
		}
		if(progress!=null){			
			criteria.add(Restrictions.eq("progress", progress));
		}
		if(createDateLessThen!=null){
			criteria.add(Restrictions.lt("createDate", createDateLessThen));
		}
		if(createDateGreaterThen!=null){
			criteria.add(Restrictions.gt("createDate", createDateGreaterThen));
		}
		if(createDateLessThenOrEquals!=null){
			criteria.add(Restrictions.le("createDate", createDateLessThenOrEquals));
		}
		if(createDateGreaterThenOrEquals!=null){
			criteria.add(Restrictions.ge("createDate", createDateGreaterThenOrEquals));
		}
		
		if(dueDateLessThen!=null){
			criteria.add(Restrictions.lt("duedate", dueDateLessThen));
		}
		if(dueDateGreaterThen!=null){
			criteria.add(Restrictions.gt("duedate", dueDateGreaterThen));
		}
		if(dueDateLessThenOrEquals!=null){
			criteria.add(Restrictions.le("duedate", dueDateLessThenOrEquals));
		}
		if(dueDateGreaterThenOrEquals!=null){
			criteria.add(Restrictions.ge("duedate", dueDateGreaterThenOrEquals));
		}
		
		if(!queryCount){
			for(String ascProperty:ascOrders){
				criteria.addOrder(Order.asc(ascProperty));
			}
			for(String descProperty:descOrders){
				criteria.addOrder(Order.desc(descProperty));
			}
		}
		int i=0,size=states.size();
		if(size==1){
			criteria.add(Restrictions.eq("state", states.get(0)));
		}else if(size>1){
			LogicalExpression top=null;
			while(i<size){
				TaskState state=states.get(i);
				if(top==null){
					i++;
					TaskState nextState=states.get(i);
					top=Restrictions.or(Restrictions.eq("state", state), Restrictions.eq("state", nextState));
				}else{
					top=Restrictions.or(top, Restrictions.eq("state", state));
				}
				i++;
			}	
			criteria.add(top);
		}
		
		i=0;
		size=prevstates.size();
		if(size==1){
			criteria.add(Restrictions.eq("prevState", prevstates.get(0)));
		}else if(size>1){
			LogicalExpression top=null;
			while(i<size){
				TaskState state=prevstates.get(i);
				if(top==null){
					i++;
					TaskState nextState=prevstates.get(i);
					top=Restrictions.or(Restrictions.eq("prevState", state), Restrictions.eq("prevState", nextState));
				}else{
					top=Restrictions.or(top, Restrictions.eq("prevState", state));
				}
				i++;
			}	
			criteria.add(top);
		}
		if(participators.size()>0){
			Criteria participatorCriteria=criteria.createCriteria("taskParticipators");
			Disjunction disjunction=Restrictions.disjunction();
			for(String user:participators){
				disjunction.add(Restrictions.eq("user",user));
			}
			participatorCriteria.add(disjunction);
		}
	}
		
	public TaskQuery addAssignee(String assignee) {
		assignees.add(assignee);
		return this;
	}
	
	public TaskQuery businessId(String businessId) {
		this.businessId=businessId;
		return this;
	}
	
	public TaskQuery addOrderAsc(String property){
		ascOrders.add(property);
		return this;
	}

	public TaskQuery addOrderDesc(String property){
		descOrders.add(property);
		return this;
	}
	
	public TaskQuery assignee(String assignee) {
		assignees.add(assignee);
		return this;
	}

	public TaskQuery owner(String owner) {
		this.owner=owner;
		return this;
	}

	public TaskQuery addTaskState(TaskState state) {
		states.add(state);
		return this;
	}

	public TaskQuery addPrevTaskState(TaskState state) {
		prevstates.add(state);
		return this;
	}

	public TaskQuery processInstanceId(long processInstanceId) {
		this.processInstanceId=processInstanceId;
		return this;
	}

	public TaskQuery createDateLessThen(Date createDateLessThen) {
		this.createDateLessThen=createDateLessThen;
		return this;
	}

	public TaskQuery createDateLessThenOrEquals(Date createDateLessThenOrEquals) {
		this.createDateLessThenOrEquals=createDateLessThenOrEquals;
		return this;
	}

	public TaskQuery createDateGreaterThen(Date createDateGreaterThen) {
		this.createDateGreaterThen=createDateGreaterThen;
		return this;
	}

	public TaskQuery createDateGreaterThenOrEquals(Date createDateGreaterThenOrEquals) {
		this.createDateGreaterThenOrEquals=createDateGreaterThenOrEquals;
		return this;
	}
	public TaskQuery dueDateLessThen(Date dueDateLessThen) {
		this.dueDateLessThen=dueDateLessThen;
		return this;
	}
	
	public TaskQuery dueDateLessThenOrEquals(Date dueDateLessThenOrEquals) {
		this.dueDateLessThenOrEquals=dueDateLessThenOrEquals;
		return this;
	}
	
	public TaskQuery dueDateGreaterThen(Date dueDateGreaterThen) {
		this.dueDateGreaterThen=dueDateGreaterThen;
		return this;
	}
	
	public TaskQuery dueDateGreaterThenOrEquals(Date dueDateGreaterThenOrEquals) {
		this.dueDateGreaterThenOrEquals=dueDateGreaterThenOrEquals;
		return this;
	}

	public TaskQuery urlLike(String url) {
		this.url=url;
		return this;
	}
	
	public TaskQuery subjectLike(String subject) {
		this.subject=subject;
		return this;
	}

	public TaskQuery countersign(boolean countersign) {
		this.countersign=countersign;
		return this;
	}
	

	public TaskQuery taskType(TaskType type) {
		this.type=type;
		return this;
	}

	public TaskQuery addParticipator(String user) {
		participators.add(user);
		return this;
	}
	
	public TaskQuery processId(long processId) {
		this.processIds.add(processId);
		return this;
	}
	public TaskQuery addProcessId(long processId) {
		this.processIds.add(processId);
		return this;
	}
	public TaskQuery rootProcessInstanceId(long rootProcessInstanceId) {
		this.rootProcessInstanceId=rootProcessInstanceId;
		return this;
	}

	public TaskQuery nameLike(String name) {
		this.name=name;
		return this;
	}
	
	@Override
	public TaskQuery priority(String priority) {
		this.priority=priority;
		return this;
	}
	
	@Override
	public TaskQuery progress(int progress) {
		this.progress=progress;
		return this;
	}
	
	public TaskQuery nodeName(String nodeName) {
		this.nodeName=nodeName;
		return this;
	}
	
	public TaskQuery page(int firstResult, int maxResults) {
		this.firstResult=firstResult;
		this.maxResults=maxResults;
		return this;
	}
}
