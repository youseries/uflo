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
package com.bstek.uflo.process.node;

/**
 * @author Jacky.gao
 * @since 2013年8月8日
 */
public class SubprocessVariable implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String inParameterKey;
	private String outParameterKey;
	public SubprocessVariable(String inParameterKey,String outParameterKey){
		this.inParameterKey=inParameterKey;
		this.outParameterKey=outParameterKey;
	}
	public String getInParameterKey() {
		return inParameterKey;
	}
	public void setInParameterKey(String inParameterKey) {
		this.inParameterKey = inParameterKey;
	}
	public String getOutParameterKey() {
		return outParameterKey;
	}
	public void setOutParameterKey(String outParameterKey) {
		this.outParameterKey = outParameterKey;
	}
}
