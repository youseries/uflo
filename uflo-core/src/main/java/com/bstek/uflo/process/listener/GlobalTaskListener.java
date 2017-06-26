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
package com.bstek.uflo.process.listener;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.process.node.TaskNode;

/**
 * @author Jacky.gao
 * @since 2017年5月16日
 */
public interface GlobalTaskListener {
	/**
	 * 在流程实例流转到人工任务节点时触发该方法，此时还未开始创建任务
	 * @param context 上下文对象
	 * @param processInstance 流程实例对象
	 * @param node 当前的人工任务节点对象
	 */
	void beforeTaskCreate(Context context,ProcessInstance processInstance,TaskNode node);
	
	/**
	 * 人工任务创建后触发该方法，此时人工任务虽然已创建，但还未持久化，<br>
	 * 所以在该方法当前可以对任务属性进行修改，修改结果将影响人工任务的创建.
	 * 需要注意的时，如果当前节点有多个任务产生（如会签），那么每个任务都会触发该方法调用.
	 * @param context 上下文对象
	 * @param task 当前节点上产生的一个人工任务.
	 */
	void onTaskCreate(Context context,Task task);
}
