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
package com.bstek.uflo.process.assign;

import java.util.Collection;

/**
 * @author Jacky.gao
 * @since 2013年8月17日
 */
public class PageQuery<T> {
	private String id;
	private String name;
	private int pageIndex;
	private int pageSize;
	private boolean tree;
	private Collection<T> result;
	private int recordCount;
	private Entity queryParameter;
	
	public PageQuery(int pageIndex,int pageSize){
		this.pageIndex=pageIndex;
		this.pageSize=pageSize;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Collection<T> getResult() {
		return result;
	}
	public void setResult(Collection<T> result) {
		this.result = result;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public Entity getQueryParameter() {
		return queryParameter;
	}

	public void setQueryParameter(Entity queryParameter) {
		this.queryParameter = queryParameter;
	}

	public boolean isTree() {
		return tree;
	}
	public void setTree(boolean tree) {
		this.tree = tree;
	}
}
