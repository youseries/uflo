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

import com.bstek.uflo.process.node.ForeachType;

/**
 * @author Jacky
 * @since 2013年9月15日
 */
public class ForeachNodeValidator extends NodeValidator {

	@Override
	public void validate(Element element, List<String> errors,List<String> nodeNames) {
		super.validate(element, errors,nodeNames);
		String var=element.getAttribute("var");
		if(StringUtils.isEmpty(var)){
			errors.add("动态分支节点的写入分支变量名属性不能为空");
		}
		String type=element.getAttribute("foreach-type");
		if(StringUtils.isEmpty(type)){
			errors.add("动态分支节点的集合类型变量来源属性不能为空");			
		}else{
			ForeachType foreachType=ForeachType.valueOf(type);
			if(foreachType.equals(ForeachType.In) && StringUtils.isEmpty(element.getAttribute("in"))){
				errors.add("动态分支节点的集合类型变量来源为流程变量时，流程变量属性不能为空");							
			}
			if(foreachType.equals(ForeachType.Handler) && StringUtils.isEmpty(element.getAttribute("handler-bean"))){
				errors.add("动态分支节点的集合类型变量来源为实现类Bean时，实现类Bean属性不能为空");							
			}
		}
	}
	
	public boolean support(Element element) {
		return element.getNodeName().equals("foreach");
	}

	public String getNodeName() {
		return "动态分支";
	}
}
