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
package com.bstek.uflo.process.handler;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.node.Node;

/**
 * 节点事件监听接口
 * @author Jacky.gao
 * @since 2013年8月20日
 */
public interface NodeEventHandler {
	/**
	 * 进入节点后触发的方法
	 * @param node 当前节点对象
	 * @param processInstance 当前流程实例对象
	 * @param context 流程上下文
	 */
	void enter(Node node,ProcessInstance processInstance,Context context);
	
	/**
	 * 离开节点后触发的方法
	 * @param node 当前节点对象
	 * @param processInstance 当前流程实例对象
	 * @param context 流程上下文
	 */
	void leave(Node node,ProcessInstance processInstance,Context context);
}
