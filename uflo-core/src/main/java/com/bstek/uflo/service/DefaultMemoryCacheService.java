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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.bstek.uflo.expr.impl.ProcessMapContext;
import com.bstek.uflo.model.ProcessDefinition;

/**
 * @author Jacky.gao
 * @since 2016年12月9日
 */
public class DefaultMemoryCacheService implements CacheService{
	private Map<Long,ProcessDefinition> processDefinitionMap=new HashMap<Long,ProcessDefinition>();
	private Map<Long,ProcessMapContext> contextMap=new Hashtable<Long,ProcessMapContext>();
	@Override
	public ProcessDefinition getProcessDefinition(long processId) {
		return processDefinitionMap.get(processId);
	}
	@Override
	public void putProcessDefinition(long processId, ProcessDefinition process) {
		processDefinitionMap.put(processId, process);
	}
	@Override
	public boolean containsProcessDefinition(long processId) {
		return processDefinitionMap.containsKey(processId);
	}
	@Override
	public Collection<ProcessDefinition> loadAllProcessDefinitions() {
		return processDefinitionMap.values();
	}
	@Override
	public void removeProcessDefinition(long processId) {
		processDefinitionMap.remove(processId);
	}
	
	@Override
	public ProcessMapContext getContext(long processInstanceId) {
		return contextMap.get(processInstanceId);
	}
	@Override
	public void putContext(long processInstanceId, ProcessMapContext context) {
		contextMap.put(processInstanceId, context);
	}
	@Override
	public void removeContext(long processInstanceId) {
		contextMap.remove(processInstanceId);
	}
	@Override
	public boolean containsContext(long processInstanceId) {
		return contextMap.containsKey(processInstanceId);
	}
}
