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

import com.bstek.uflo.model.ProcessDefinition;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public interface ProcessQuery extends Query<List<ProcessDefinition>>{
	ProcessQuery id(long id);
	ProcessQuery categoryId(String id);
	ProcessQuery createDateLessThen(Date date);
	ProcessQuery createDateLessThenOrEquals(Date date);
	ProcessQuery createDateGreaterThen(Date date);
	ProcessQuery createDateGreaterThenOrEquals(Date date);
	ProcessQuery nameLike(String name);
	ProcessQuery keyLike(String key);
	ProcessQuery subjectLike(String subject);
	ProcessQuery version(int version);
	ProcessQuery page(int firstResult, int maxResults);
	ProcessQuery addOrderAsc(String property);
	ProcessQuery addOrderDesc(String property);
}
