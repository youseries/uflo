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
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.query.ProcessQuery;
import com.bstek.uflo.query.QueryJob;
import com.bstek.uflo.utils.EnvironmentUtils;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class ProcessQueryImpl implements ProcessQuery,QueryJob{
	private long id;
	private String name;
	private String key;
	private String categoryId;
	private String subject;
	private int version;
	private int firstResult;
	private int maxResults;
	private Date createDateLessThen;
	private Date createDateLessThenOrEquals;
	private Date createDateGreaterThen;
	private Date createDateGreaterThenOrEquals;
	private List<String> ascOrders=new ArrayList<String>();
	private List<String> descOrders=new ArrayList<String>();
	private CommandService commandService;
	public ProcessQueryImpl(CommandService commandService){
		this.commandService=commandService;
	}
	public Criteria getCriteria(Session session,boolean queryCount) {
		Criteria criteria=session.createCriteria(ProcessDefinition.class);
		buildCriteria(criteria,queryCount);
		return criteria;
	}
	
	public List<ProcessDefinition> list() {
		return commandService.executeCommand(new QueryListCommand<List<ProcessDefinition>>(this));
	}

	public int count() {
		return commandService.executeCommand(new QueryCountCommand(this));
	}

	private void buildCriteria(Criteria criteria,boolean queryCount){
		if(!queryCount && firstResult>0){
			criteria.setFirstResult(firstResult);			
		}
		if(!queryCount && maxResults>0){
			criteria.setMaxResults(maxResults);			
		}
		if(id>0){
			criteria.add(Restrictions.eq("id", id));
		}
		if(StringUtils.isNotEmpty(name)){
			criteria.add(Restrictions.like("name", name));
		}
		if(StringUtils.isNotEmpty(key)){
			criteria.add(Restrictions.like("key", key));
		}
		if(StringUtils.isNotEmpty(subject)){
			criteria.add(Restrictions.like("subject", subject));
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
		if(StringUtils.isNotEmpty(categoryId)){
			criteria.add(Restrictions.eq("categoryId", categoryId));
		}else{
			categoryId=EnvironmentUtils.getEnvironment().getCategoryId();
			if(StringUtils.isNotEmpty(categoryId)){
				criteria.add(Restrictions.eq("categoryId", categoryId));
			}
		}
		if(version>0){
			criteria.add(Restrictions.eq("version", Integer.valueOf(version)));
		}
		if(!queryCount){
			for(String ascProperty:ascOrders){
				criteria.addOrder(Order.asc(ascProperty));
			}
			for(String descProperty:descOrders){
				criteria.addOrder(Order.desc(descProperty));
			}
		}
	}
	
	public ProcessQuery createDateGreaterThen(Date date) {
		this.createDateGreaterThen=date;
		return this;
	}
	public ProcessQuery createDateGreaterThenOrEquals(Date date) {
		this.createDateGreaterThenOrEquals=date;
		return this;
	}
	public ProcessQuery createDateLessThen(Date date) {
		this.createDateLessThen=date;
		return this;
	}
	public ProcessQuery createDateLessThenOrEquals(Date date) {
		this.createDateLessThenOrEquals=date;
		return this;
	}
	
	public ProcessQuery addOrderAsc(String property){
		ascOrders.add(property);
		return this;
	}

	public ProcessQuery addOrderDesc(String property){
		descOrders.add(property);
		return this;
	}
	
	public ProcessQuery id(long id) {
		this.id=id;
		return this;
	}
	
	public ProcessQuery categoryId(String categoryId) {
		this.categoryId=categoryId;
		return this;
	}

	public ProcessQuery nameLike(String name) {
		this.name=name;
		return this;
	}
	
	public ProcessQuery subjectLike(String subject) {
		this.subject=subject;
		return this;
	}
	
	public ProcessQuery keyLike(String key) {
		this.key=key;
		return this;
	}

	public ProcessQuery version(int version) {
		this.version=version;
		return this;
	}

	public ProcessQuery page(int firstResult, int maxResults) {
		this.firstResult=firstResult;
		this.maxResults=maxResults;
		return this;
	}
}
