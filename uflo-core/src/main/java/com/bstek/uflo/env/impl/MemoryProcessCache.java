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
package com.bstek.uflo.env.impl;

import java.util.Hashtable;
import java.util.Map;

import com.bstek.uflo.env.ProcessCache;

/**
 * @author Jacky.gao
 * @since 2016年4月13日
 */
public class MemoryProcessCache implements ProcessCache {
	private Map<String,Object> map=new Hashtable<String,Object>();
	public void store(String key, Object obj) {
		map.put(key, obj);
	}

	public Object retrive(String key) {
		return map.get(key);
	}
}
