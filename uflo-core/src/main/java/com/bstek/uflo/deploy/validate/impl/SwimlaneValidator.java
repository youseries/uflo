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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bstek.uflo.process.node.AssignmentType;

/**
 * @author Jacky
 * @since 2013年9月15日
 */
public class SwimlaneValidator extends NodeValidator {

	public void validate(Element element, List<String> errors,List<String> nodeNames) {
		super.validate(element, errors,nodeNames);
		String assignmentType=element.getAttribute("assignment-type");
		if(StringUtils.isEmpty(assignmentType)){
			errors.add("泳道必须要定义任务的分配方式");			
		}
		AssignmentType at=AssignmentType.valueOf(assignmentType);
		if(at.equals(AssignmentType.Expression) && StringUtils.isEmpty(element.getAttribute("expression"))){
			errors.add("泳道的任务分配方式为表达式时，必须要定义一个具体的表达式，如${startDate}");						
		}
		if(at.equals(AssignmentType.Handler) && StringUtils.isEmpty(element.getAttribute("assignment-handler-bean"))){
			errors.add("泳道的任务分配方式为指定Bean时，必须要定义一个具体的实现了AssignmentHandler的类的Bean");						
		}
		if(at.equals(AssignmentType.Swimlane) && StringUtils.isEmpty(element.getAttribute("swimlane"))){
			errors.add("泳道的任务分配方式为泳道时，必须要定义一个具体的泳道名称");						
		}
		if(at.equals(AssignmentType.Assignee)){
			boolean hasAssignee=false;
			NodeList nodeList=element.getChildNodes();
			for(int i=0;i<nodeList.getLength();i++){
				Node node=nodeList.item(i);
				if(!(node instanceof Element)){
					continue;
				}
				Element e=(Element)node;
				if(e.getNodeName().equals("assignee")){
					hasAssignee=true;
				}
			}
			if(!hasAssignee){
				errors.add("泳道的任务分配方式为指定参与者时，至少要定义一个具体的参与者信息");										
			}
		}
	}
	
	public boolean support(Element element) {
		return element.getNodeName().equals("swimlane");
	}

	public String getNodeName() {
		return "泳道";
	}
}
