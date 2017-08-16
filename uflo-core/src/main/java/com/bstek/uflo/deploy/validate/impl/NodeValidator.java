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

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import com.bstek.uflo.deploy.validate.Validator;

/**
 * @author Jacky
 * @since 2013年9月15日
 */
public abstract class NodeValidator implements Validator {

	public void validate(Element element, List<String> errors,List<String> nodeNames) {
		String name=element.getAttribute("name");
		if(StringUtils.isEmpty(name)){
			errors.add(getNodeName()+"节点未定义名称");
		}else{
			if(nodeNames.contains(name)){
				errors.add("有一个以上名为"+name+"的节点，在一个流程模版当中每个节点名都需要唯一");
			}else{
				nodeNames.add(name);
			}
		}
	}
}
