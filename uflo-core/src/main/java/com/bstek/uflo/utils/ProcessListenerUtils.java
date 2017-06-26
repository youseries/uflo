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
package com.bstek.uflo.utils;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.listener.ProcessListener;

/**
 * @author Jacky.gao
 * @since 2013年11月18日
 */
public class ProcessListenerUtils {
	public static void fireProcessStartListers(ProcessInstance processInstance,Context context){
		fireProcessListers(processInstance,context,true);
	}
	public static void fireProcessEndListers(ProcessInstance processInstance,Context context){
		fireProcessListers(processInstance,context,false);		
	}
	private static void fireProcessListers(ProcessInstance processInstance,Context context,boolean isStart){
		for(ProcessListener listener:context.getApplicationContext().getBeansOfType(ProcessListener.class).values()){
			if(isStart){
				listener.processStart(processInstance, context);
			}else{
				listener.processEnd(processInstance, context);				
			}
		}
	}
}
