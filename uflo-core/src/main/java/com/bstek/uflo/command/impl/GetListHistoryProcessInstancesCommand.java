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

import org.hibernate.criterion.Restrictions;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.HistoryProcessInstance;

/**
 * @author Jacky.gao
 * @since 2013年9月12日
 */
public class GetListHistoryProcessInstancesCommand implements Command<List<HistoryProcessInstance>> {
	private long processId;
	public GetListHistoryProcessInstancesCommand(long processId){
		this.processId=processId;
	}
	@SuppressWarnings("unchecked")
	public List<HistoryProcessInstance> execute(Context context) {
		return context.getSession().createCriteria(HistoryProcessInstance.class).add(Restrictions.eq("processId", processId)).list();
	}
}
