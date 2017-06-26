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

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.query.QueryJob;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class QueryCountCommand implements Command<Integer> {
	private QueryJob job;
	public QueryCountCommand(QueryJob job){
		this.job=job;
	}
	public Integer execute(Context context) {
		Criteria criteria=job.getCriteria(context.getSession(),true);
		Object obj=criteria.setProjection(Projections.rowCount()).uniqueResult();
		if(obj==null)return 0;
		if(obj instanceof Integer){
			return (Integer)obj;
		}else{
			return ((Long)obj).intValue();
		}
	}
}
