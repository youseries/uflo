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
package com.bstek.uflo.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bstek.uflo.process.assign.Entity;
import com.bstek.uflo.process.assign.PageQuery;
import com.bstek.uflo.service.IdentityService;

/**
 * @author Jacky.gao
 * @since 2013年8月13日
 */
public class DefaultIdentityService implements IdentityService {

	public Collection<String> getUsersByGroup(String group) {
		List<String> users=new ArrayList<String>();
		for(int i=1;i<5;i++){
			users.add("user-"+group+"-"+i);
		}
		return users;
	}

	public Collection<String> getUsersByPosition(String position) {
		List<String> users=new ArrayList<String>();
		for(int i=1;i<5;i++){
			users.add("user-"+position+"-"+i);
		}
		return users;
	}

	public Collection<String> getUsersByDept(String dept) {
		List<String> users=new ArrayList<String>();
		for(int i=1;i<5;i++){
			users.add("user-"+dept+"-"+i);
		}
		return users;
	}

	public Collection<String> getUsersByDeptAndPosition(String dept,String position) {
		List<String> users=new ArrayList<String>();
		for(int i=1;i<5;i++){
			users.add("user-"+dept+"-"+position+"-"+i);
		}
		return users;
	}

	public void userPageQuery(PageQuery<Entity> page) {
		page.setRecordCount(400);
		int index=page.getPageIndex();
		int size=page.getPageSize();
		List<Entity> entitys=new ArrayList<Entity>();
		Entity parameter=page.getQueryParameter();
		String id=null;
		if(parameter!=null){
			id=parameter.getId();
		}
		for(int i=(index-1)*size;i<(index*size);i++){
			if(id!=null){
				if(!String.valueOf(i).equals(id)){
					continue;
				}				
			}
			Entity entity=new Entity("user"+i,"测试用户"+i);
			entitys.add(entity);
		}
		page.setResult(entitys);
	}

	public void deptPageQuery(PageQuery<Entity> page,String parentId) {
		parentId=(parentId==null?"":parentId);
		page.setRecordCount(400);
		int index=page.getPageIndex();
		int size=10;
		List<Entity> entitys=new ArrayList<Entity>();
		Entity parameter=page.getQueryParameter();
		String id=null;
		if(parameter!=null){
			id=parameter.getId();
		}
		for(int i=(index-1)*size;i<(index*size);i++){
			if(id!=null){
				if(!String.valueOf(i).equals(id)){
					continue;
				}				
			}
			Entity entity=new Entity("dept"+parentId+i,"测试部门"+i);
			if(i>4){
				entity.setChosen(false);
			}
			entitys.add(entity);
			
		}
		page.setResult(entitys);
	}

	public void positionPageQuery(PageQuery<Entity> query,String parentId) {
		
	}

	public void groupPageQuery(PageQuery<Entity> query,String parentId) {
		
	}

}
