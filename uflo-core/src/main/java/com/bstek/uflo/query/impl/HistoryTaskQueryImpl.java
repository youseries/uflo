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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.command.impl.QueryCountCommand;
import com.bstek.uflo.command.impl.QueryListCommand;
import com.bstek.uflo.model.HistoryTask;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.model.task.TaskType;
import com.bstek.uflo.query.HistoryTaskQuery;
import com.bstek.uflo.query.QueryJob;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class HistoryTaskQueryImpl implements HistoryTaskQuery,QueryJob{
	private String url;
	private String assignee;
	private String owner;
	private String businessId;
	private long processInstanceId;
	private long historyProcessInstanceId;
	private long rootProcessInstanceId;
	private Date createDateLessThen;
	private Date createDateLessThenOrEquals;
	private Date createDateGreaterThen;
	private Date createDateGreaterThenOrEquals;
	private Date endDateLessThen;
	private Date endDateLessThenOrEquals;
	private Date endDateGreaterThen;
	private Date endDateGreaterThenOrEquals;
	private Boolean countersign;
	private TaskType type;
	private long processId;
	private long taskId;
	private String name;
	private String nodeName;
	private int firstResult;
	private int maxResults;
	private List<TaskState> states=new ArrayList<TaskState>();
	private List<TaskState> prevstates=new ArrayList<TaskState>();
	private List<String> ascOrders=new ArrayList<String>();
	private List<String> descOrders=new ArrayList<String>();
	private CommandService commandService;
	public HistoryTaskQueryImpl(CommandService commandService){
		this.commandService=commandService;
	}
	public List<HistoryTask> list() {
		return commandService.executeCommand(new QueryListCommand<List<HistoryTask>>(this));
	}

	public int count() {
		return commandService.executeCommand(new QueryCountCommand(this));
	}

	public Criteria getCriteria(Session session,boolean queryCount) {
		Criteria criteria=session.createCriteria(HistoryTask.class);
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
		if(processId>0){
			criteria.add(Restrictions.eq("processId",processId));
		}
		if(StringUtils.isNotEmpty(owner)){
			criteria.add(Restrictions.eq("owner", owner));
		}
		if(StringUtils.isNotEmpty(businessId)){
			criteria.add(Restrictions.eq("businessId", businessId));
		}
		if(StringUtils.isNotEmpty(assignee)){
			criteria.add(Restrictions.eq("assignee", assignee));
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
		if(taskId>0){
			criteria.add(Restrictions.eq("taskId",taskId));
		}
		if(historyProcessInstanceId>0){
			criteria.add(Restrictions.eq("historyProcessInstanceId",historyProcessInstanceId));
		}
		if(StringUtils.isNotEmpty(url)){
			criteria.add(Restrictions.like("url", url));
		}
		if(StringUtils.isNotEmpty(name)){
			criteria.add(Restrictions.like("taskName", name));
		}
		if(StringUtils.isNotEmpty(nodeName)){
			criteria.add(Restrictions.like("nodeName", nodeName));
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
		if(endDateLessThen!=null){
			criteria.add(Restrictions.lt("endDate", endDateLessThen));
		}
		if(endDateGreaterThen!=null){
			criteria.add(Restrictions.gt("endDate", endDateGreaterThen));
		}
		if(endDateLessThenOrEquals!=null){
			criteria.add(Restrictions.le("endDate", endDateLessThenOrEquals));
		}
		if(endDateGreaterThenOrEquals!=null){
			criteria.add(Restrictions.ge("endDate", endDateGreaterThenOrEquals));
		}
		if(!queryCount){
			for(String ascProperty:ascOrders){
				criteria.addOrder(Order.asc(ascProperty));
			}
			for(String descProperty:descOrders){
				criteria.addOrder(Order.desc(descProperty));
			}
		}
		for(TaskState state:states){
			criteria.add(Restrictions.eq("state", state));
		}
		for(TaskState state:prevstates){
			criteria.add(Restrictions.eq("prevState", state));
		}
	}
	
	public HistoryTaskQuery addOrderAsc(String property){
		ascOrders.add(property);
		return this;
	}

	public HistoryTaskQuery addOrderDesc(String property){
		descOrders.add(property);
		return this;
	}
	
	public HistoryTaskQuery assignee(String assignee) {
		this.assignee=assignee;
		return this;
	}

	public HistoryTaskQuery owner(String owner) {
		this.owner=owner;
		return this;
	}

	public HistoryTaskQuery addTaskState(TaskState state) {
		states.add(state);
		return this;
	}

	public HistoryTaskQuery addPrevTaskState(TaskState state) {
		prevstates.add(state);
		return this;
	}

	public HistoryTaskQuery processInstanceId(long processInstanceId) {
		this.processInstanceId=processInstanceId;
		return this;
	}
	
	
	public HistoryTaskQuery rootProcessInstanceId(long rootProcessInstanceId) {
		this.rootProcessInstanceId=rootProcessInstanceId;
		return this;
	}
	
	public HistoryTaskQuery taskId(long taskId) {
		this.taskId=taskId;
		return this;
	}

	public HistoryTaskQuery createDateLessThen(Date createDateLessThen) {
		this.createDateLessThen=createDateLessThen;
		return this;
	}

	public HistoryTaskQuery createDateLessThenOrEquals(Date createDateLessThenOrEquals) {
		this.createDateLessThenOrEquals=createDateLessThenOrEquals;
		return this;
	}

	public HistoryTaskQuery createDateGreaterThen(Date createDateGreaterThen) {
		this.createDateGreaterThen=createDateGreaterThen;
		return this;
	}

	public HistoryTaskQuery createDateGreaterThenOrEquals(Date createDateGreaterThenOrEquals) {
		this.createDateGreaterThenOrEquals=createDateGreaterThenOrEquals;
		return this;
	}
	public HistoryTaskQuery endDateLessThen(Date endDateLessThen) {
		this.endDateLessThen=endDateLessThen;
		return this;
	}
	
	public HistoryTaskQuery endDateLessThenOrEquals(Date endDateLessThenOrEquals) {
		this.endDateLessThenOrEquals=endDateLessThenOrEquals;
		return this;
	}
	
	public HistoryTaskQuery endDateGreaterThen(Date endDateGreaterThen) {
		this.endDateGreaterThen=endDateGreaterThen;
		return this;
	}
	
	public HistoryTaskQuery endDateGreaterThenOrEquals(Date endDateGreaterThenOrEquals) {
		this.endDateGreaterThenOrEquals=endDateGreaterThenOrEquals;
		return this;
	}

	public HistoryTaskQuery urlLike(String url) {
		this.url=url;
		return this;
	}
	public HistoryTaskQuery businessId(String businessId) {
		this.businessId=businessId;
		return this;
	}

	public HistoryTaskQuery countersign(boolean countersign) {
		this.countersign=countersign;
		return this;
	}
	

	public HistoryTaskQuery taskType(TaskType type) {
		this.type=type;
		return this;
	}
	
	public HistoryTaskQuery processId(long processId) {
		this.processId=processId;
		return this;
	}
	public HistoryTaskQuery historyProcessInstanceId(long historyProcessInstanceId) {
		this.historyProcessInstanceId=historyProcessInstanceId;
		return this;
	}

	public HistoryTaskQuery nameLike(String name) {
		this.name=name;
		return this;
	}
	
	public HistoryTaskQuery nodeName(String nodeName) {
		this.nodeName=nodeName;
		return this;
	}
	public HistoryTaskQuery page(int firstResult, int maxResults) {
		this.firstResult=firstResult;
		this.maxResults=maxResults;
		return this;
	}
}
