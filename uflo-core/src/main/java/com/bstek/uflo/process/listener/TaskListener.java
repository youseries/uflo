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
 * @since 2014年5月28日
 */
public interface TaskListener {
	/**
	 * 在流程实例流转到人工任务节点时触发该方法，此时还未开始创建任务。<br>
	 * 该方法的返回值决定流程实例是否在当前人工任务节点上创建任务并停留，<br>
	 * 1.当返回值为false,那么就继续执行人工任务动作，创建对应的人工任务.<br>
	 * 2.当返回值为true时，则沿当前人工任务下默认连线(如果存在多条连接，则系统随机取一条)进入下一个流程节点.<br>
	 * @param context 上下文对象
	 * @param processInstance 流程实例对象
	 * @param node 当前的人工任务节点对象
	 * @return 返回连接名称，为false，则不处理，否则则离开当前节点.
	 */
	boolean beforeTaskCreate(Context context,ProcessInstance processInstance,TaskNode node);
	
	/**
	 * 人工任务创建后触发该方法，此时人工任务虽然已创建，但还未持久化，<br>
	 * 所以在该方法当前可以对任务属性进行修改，修改结果将影响人工任务的创建.
	 * 需要注意的时，如果当前节点有多个任务产生（如会签），那么每个任务都会触发该方法调用.
	 * @param context 上下文对象
	 * @param task 当前节点上产生的一个人工任务.
	 */
	void onTaskCreate(Context context,Task task);
	
	/**
	 * 当前节点上人工任务完成时将触发该方法调用，如果当前节点生成有多个任务（比如会签），那么每个任务完成时都会触发该方法调用.
	 * @param context 上下文对象
	 * @param task 当前节点上一个已完成的人工任务.
	 */
	void onTaskComplete(Context context,Task task);
}
