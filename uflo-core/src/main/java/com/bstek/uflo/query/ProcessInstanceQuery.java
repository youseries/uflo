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

import com.bstek.uflo.model.ProcessInstance;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public interface ProcessInstanceQuery extends Query<List<ProcessInstance>>{

	ProcessInstanceQuery processId(long processId);
	
	ProcessInstanceQuery parentId(long parentId);
	
	ProcessInstanceQuery rootId(long rootId);

	ProcessInstanceQuery page(int firstResult, int maxResults);

	ProcessInstanceQuery businessId(String businessId);
	
	ProcessInstanceQuery promoter(String businessId);
	
	ProcessInstanceQuery addOrderAsc(String property);

	ProcessInstanceQuery addOrderDesc(String property);

	ProcessInstanceQuery createDateLessThen(Date date);

	ProcessInstanceQuery createDateLessThenOrEquals(Date date);

	ProcessInstanceQuery createDateGreaterThen(Date date);

	ProcessInstanceQuery createDateGreaterThenOrEquals(Date date);

}
