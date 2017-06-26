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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.HistoryActivity;

/**
 * @author Jacky.gao
 * @since 2013年9月12日
 */
public class GetHistoryActivitiyCommand implements Command<List<HistoryActivity>> {
	private long instanceId;
	private boolean isProcessInstanceId;
	public GetHistoryActivitiyCommand(long instanceId,boolean isProcessInstanceId){
		this.instanceId=instanceId;
		this.isProcessInstanceId=isProcessInstanceId;
	}
	@SuppressWarnings("unchecked")
	public List<HistoryActivity> execute(Context context) {
		Criteria criteria=context.getSession().createCriteria(HistoryActivity.class);
		if(isProcessInstanceId){
			criteria.add(Restrictions.eq("rootProcessInstanceId", instanceId));
		}else{
			criteria.add(Restrictions.eq("historyProcessInstanceId", instanceId));
		}
		criteria.addOrder(Order.desc("endDate"));
		return criteria.list();
	}
}
