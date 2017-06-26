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
package com.bstek.uflo.deploy.parse.impl;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.uflo.deploy.parse.AbstractParser;
import com.bstek.uflo.process.node.AssignmentType;
import com.bstek.uflo.process.swimlane.Swimlane;

/**
 * @author Jacky.gao
 * @since 2013年8月13日
 */
public class SwimlaneParser extends AbstractParser {

	public Object parse(Element element, long processId, boolean parseChildren) {
		Swimlane swimlane=new Swimlane();
		String name=element.attributeValue("name");
		swimlane.setName(unescape(name));
		Element descElement=element.element("description");
		if(descElement!=null){
			String desc=unescape(descElement.getTextTrim());
			desc=(desc.length()>120?desc.substring(0,120):desc);
			swimlane.setDescription(desc);
		}
		String type=element.attributeValue("assignment-type");
		if(StringUtils.isNotEmpty(type)){
			swimlane.setAssignmentType(AssignmentType.valueOf(type));			
		}
		swimlane.setAssignmentHandlerBean(unescape(element.attributeValue("assignment-handler-bean")));
		swimlane.setExpression(unescape(element.attributeValue("expression")));
		AssignmentType assignmentType=swimlane.getAssignmentType();
		if(assignmentType!=null && (assignmentType.equals(AssignmentType.Assignee))){
			swimlane.setAssignees(parserAssignees(element));
		}
		return swimlane;
	}

	public boolean support(Element element) {
		return element.getName().equals("swimlane");
	}
}
