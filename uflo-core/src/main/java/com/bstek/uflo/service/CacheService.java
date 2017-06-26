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
package com.bstek.uflo.service;

import java.util.Collection;

import com.bstek.uflo.expr.impl.ProcessMapContext;
import com.bstek.uflo.model.ProcessDefinition;

/**
 * @author Jacky.gao
 * @since 2016年12月9日
 */
public interface CacheService {
	ProcessDefinition getProcessDefinition(long processId);
	void putProcessDefinition(long processId,ProcessDefinition process);
	boolean containsProcessDefinition(long processId);
	Collection<ProcessDefinition> loadAllProcessDefinitions();
	void removeProcessDefinition(long processId);
	ProcessMapContext getContext(long processInstanceId);
	void putContext(long processInstanceId,ProcessMapContext context);
	void removeContext(long processInstanceId);
	boolean containsContext(long processInstanceId);
}
