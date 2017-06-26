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
package com.bstek.uflo.process.assign.impl;

import java.util.Collection;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.assign.Entity;
import com.bstek.uflo.process.assign.PageQuery;
import com.bstek.uflo.service.IdentityService;

/**
 * @author Jacky.gao
 * @since 2013年8月20日
 */
public class DeptAssigneeProvider extends AbstractAssigneeProvider {
	private IdentityService identityService;
	private boolean disabledDeptAssigneeProvider;
	public boolean isTree() {
		return true;
	}
	
	public String getName() {
		return "指定部门";
	}
	
	public void queryEntities(PageQuery<Entity> pageQuery, String parentId) {
		identityService.deptPageQuery(pageQuery,parentId);
	}
	
	public Collection<String> getUsers(String entityId,Context context,ProcessInstance processInstance) {
		return identityService.getUsersByDept(entityId);
	}

	public boolean disable() {
		return disabledDeptAssigneeProvider;
	}
	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	public boolean isDisabledDeptAssigneeProvider() {
		return disabledDeptAssigneeProvider;
	}

	public void setDisabledDeptAssigneeProvider(boolean disabledDeptAssigneeProvider) {
		this.disabledDeptAssigneeProvider = disabledDeptAssigneeProvider;
	}
}
