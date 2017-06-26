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

import java.util.List;

import com.bstek.uflo.model.variable.Variable;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public interface ProcessVariableQuery extends Query<List<Variable>>{
	ProcessVariableQuery processInstanceId(long processInstanceId);
	ProcessVariableQuery rootprocessInstanceId(long rootProcessInstanceId);
	ProcessVariableQuery key(String key);
	ProcessVariableQuery page(int firstResult, int maxResults);
	ProcessVariableQuery addOrderAsc(String property);
	ProcessVariableQuery addOrderDesc(String property);
}
