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

import com.bstek.uflo.process.node.DecisionType;

/**
 * @author Jacky
 * @since 2013年9月15日
 */
public class DecisionNodeValidator extends NodeValidator {

	@Override
	public void validate(Element element, List<String> errors,List<String> nodeNames) {
		super.validate(element, errors,nodeNames);
		String type=element.getAttribute("decision-type");
		if(StringUtils.isEmpty(type)){
			errors.add("路由决策节点必须要指定决策条件判断方式");
		}else{
			DecisionType decisionType=DecisionType.valueOf(type);
			if(decisionType.equals(DecisionType.Expression)){
				if(StringUtils.isEmpty(element.getAttribute("expression"))){
					NodeList nodeList=element.getChildNodes();
					boolean hasExpr=false;
					for(int i=0;i<nodeList.getLength();i++){
						Node node=nodeList.item(i);
						if(!(node instanceof Element)){
							continue;
						}
						Element childElement=(Element)node;
						if(childElement.getNodeName().equals("expression")){
							hasExpr=true;
							break;
						}
					}
					if(!hasExpr){
						errors.add("路由决策节点条件判断方式表达式时，必须要指定一个具体表达式");						
					}
				}
			}
			if(decisionType.equals(DecisionType.Handler) && StringUtils.isEmpty(element.getAttribute("handler-bean"))){
				errors.add("路由决策节点条件判断方式实现类Bean时，必须要指定一个具体实现类Bean");				
			}
		}
	}
	
	public boolean support(Element element) {
		return element.getNodeName().equals("decision");
	}

	public String getNodeName() {
		return "路由决策";
	}
}
