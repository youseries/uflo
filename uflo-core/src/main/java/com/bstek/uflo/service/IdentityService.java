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

import com.bstek.uflo.process.assign.PageQuery;
import com.bstek.uflo.process.assign.Entity;

/**
 * @author Jacky.gao
 * @since 2013年8月12日
 */
public interface IdentityService {
	public static final String USER_TYPE="user";
	public static final String DEPT_TYPE="udept";
	public static final String POSITION_TYPE="position";
	public static final String GROUP_TYPE="group";
	public static final String DEPT_POSITION_TYPE="dept-position";
	
	public static final String BEAN_ID="uflo.identityService";
	void userPageQuery(PageQuery<Entity> query);
	void deptPageQuery(PageQuery<Entity> query,String parentId);
	void positionPageQuery(PageQuery<Entity> query,String parentId);
	void groupPageQuery(PageQuery<Entity> query,String parentId);
	
	Collection<String> getUsersByGroup(String group);
	Collection<String> getUsersByPosition(String position);
	Collection<String> getUsersByDept(String dept);
	Collection<String> getUsersByDeptAndPosition(String dept,String position);
}
