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
package com.bstek.uflo.deploy.validate.impl;

import java.util.List;

import org.w3c.dom.Element;

/**
 * @author Jacky
 * @since 2013年9月15日
 */
public class StartNodeValidator extends NodeValidator {

	public void validate(Element element, List<String> errors,List<String> nodeNames) {
		super.validate(element, errors,nodeNames);
	}

	public boolean support(Element element) {
		return element.getNodeName().equals("start");
	}

	
	public String getNodeName() {
		return "开始";
	}
}
