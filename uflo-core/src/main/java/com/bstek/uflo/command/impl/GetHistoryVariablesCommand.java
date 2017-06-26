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
import org.hibernate.criterion.Restrictions;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.HistoryVariable;

/**
 * @author Jacky.gao
 * @since 2013年9月28日
 */
public class GetHistoryVariablesCommand implements Command<List<HistoryVariable>> {
	private long historyProcessInstanceId;
	public GetHistoryVariablesCommand(long historyProcessInstanceId){
		this.historyProcessInstanceId=historyProcessInstanceId;
	}
	@SuppressWarnings("unchecked")
	public List<HistoryVariable> execute(Context context) {
		Criteria criteria=context.getSession().createCriteria(HistoryVariable.class);
		criteria.add(Restrictions.eq("historyProcessInstanceId", historyProcessInstanceId));
		List<HistoryVariable> vars=criteria.list();
		for(HistoryVariable var:vars){
			var.init(context);
		}
		return vars;
	}
}
