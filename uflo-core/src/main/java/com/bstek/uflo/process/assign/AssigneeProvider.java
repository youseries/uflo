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

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;


/**
 * @author Jacky.gao
 * @since 2013年8月17日
 */
public interface AssigneeProvider {
	/**
	 * 设计器层面是否要用树形结构进行展示
	 * @return 返回true，表示设计器会用树形加载当前任务处理人列表
	 */
	boolean isTree();
	/**
	 * @return 返回当前任务处理人提供者名称，比如员工列表，部门列表等
	 */
	String getName();
	/**
	 * 分页方式查询返回具体的任务处理人，可以是具体的人，也可以是部门等之类容器型对象
	 * @param pageQuery 用于包装分页信息的查询对象
	 * @param parentId 上级实体对象的ID，可能为空
	 */
	void queryEntities(PageQuery<Entity> pageQuery,String parentId);
	/**
	 * 根据指定的处理人ID，返回具体的任务处理人用户名
	 * @param entityId 处理人ID，可能是一个用户的用户名，这样就是直接返回这个用户名，也可能是一个部门的ID，那么就是返回这个部门下的所有用户的用户名等 
	 * @param context context 流程上下文对象
	 * @param ProcessInstance processInstance 流程实例对象
	 * @return 返回一个或多个任务处理人的ID
	 */
	Collection<String> getUsers(String entityId,Context context,ProcessInstance processInstance);
	/**
	 * @return 是否禁用当前任务处理人提供器
	 */
	boolean disable();
}
