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

/**
 * 流程实例开始后及结束后触发的事件
 * @author Jacky.gao
 * @since 2013年8月20日
 */
public interface ProcessEventHandler {
	/**
	 * 流程实例开始后触发的方法
	 * @param processInstance 流程实例
	 * @param context 流程实例上下文
	 */
	void start(ProcessInstance processInstance,Context context);
	
	/**
	 * 流程实例结束后触发的方法
	 * @param processInstance 流程实例
	 * @param context 流程实例上下文
	 */
	void end(ProcessInstance processInstance,Context context);
}
