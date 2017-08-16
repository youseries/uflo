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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bstek.uflo.deploy.validate.Validator;

/**
 * @author Jacky
 * @since 2013年9月15日
 */
public class ProcessValidator implements Validator {
	private List<Validator> validators;
	public ProcessValidator(){
		validators=new ArrayList<Validator>();
		validators.add(new ActionNodeValidator());
		validators.add(new DecisionNodeValidator());
		validators.add(new EndNodeValidator());
		validators.add(new ForeachNodeValidator());
		validators.add(new ForkNodeValidator());
		validators.add(new StartNodeValidator());
		validators.add(new SubprocessNodeValidator());
		validators.add(new SwimlaneValidator());
		validators.add(new TaskNodeValidator());
	}
	public void validate(Element element, List<String> errors,List<String> nodeNames) {
		boolean hasStart=false;
		boolean hasEnd=false;
		String name=element.getAttribute("name");
		if(StringUtils.isEmpty(name)){
			errors.add("流程模版未定义名称");
		}
		NodeList nodeList=element.getChildNodes();
		for(int i=0;i<nodeList.getLength();i++){
			Node node=nodeList.item(i);
			if(!(node instanceof Element)){
				continue;
			}
			Element childElement=(Element)node;
			for(Validator validator:validators){
				if(validator.support(childElement)){
					if(validator instanceof StartNodeValidator){
						hasStart=true;
					}
					if(validator instanceof EndNodeValidator){
						hasEnd=true;
					}
					validator.validate(childElement, errors,nodeNames);
				}
			}
		}
		if(!hasStart){
			errors.add("流程模版中未定义开始节点。");				
		}
		if(!hasEnd){
			errors.add("流程模版中未定义结束节点。");				
		}
	}

	public boolean support(Element element) {
		return element.getNodeName().equals("uflo-process");
	}
	
	public String getNodeName() {
		return "流程模版";
	}
}
