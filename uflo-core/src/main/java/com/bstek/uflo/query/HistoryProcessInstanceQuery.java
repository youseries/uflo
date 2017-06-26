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
package com.bstek.uflo.query;

import java.util.Date;
import java.util.List;

import com.bstek.uflo.model.HistoryProcessInstance;

/**
 * @author Jacky.gao
 * @since 2013年9月17日
 */
public interface HistoryProcessInstanceQuery extends Query<List<HistoryProcessInstance>>{
	HistoryProcessInstanceQuery processId(long processId);

	HistoryProcessInstanceQuery page(int firstResult, int maxResults);

	HistoryProcessInstanceQuery addOrderAsc(String property);

	HistoryProcessInstanceQuery addOrderDesc(String property);

	HistoryProcessInstanceQuery createDateLessThen(Date date);

	HistoryProcessInstanceQuery createDateLessThenOrEquals(Date date);

	HistoryProcessInstanceQuery createDateGreaterThen(Date date);

	HistoryProcessInstanceQuery createDateGreaterThenOrEquals(Date date);
	
	HistoryProcessInstanceQuery businessId(String businessId);
	
	HistoryProcessInstanceQuery tag(String businessId);
	
	HistoryProcessInstanceQuery promoter(String promoter);
}
