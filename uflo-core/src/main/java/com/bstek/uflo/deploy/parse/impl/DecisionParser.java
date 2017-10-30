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
import com.bstek.uflo.diagram.NodeDiagram;
import com.bstek.uflo.diagram.ShapeType;
import com.bstek.uflo.process.node.DecisionNode;
import com.bstek.uflo.process.node.DecisionType;

/**
 * @author Jacky.gao
 * @since 2013年8月13日
 */
public class DecisionParser extends AbstractParser {

	public Object parse(Element element, long processId, boolean parseChildren) {
		DecisionNode node=new DecisionNode();
		node.setProcessId(processId);
		parseNodeCommonInfo(element, node);
		node.setSequenceFlows(parseFlowElement(element,processId,parseChildren));
		node.setDecisionType(DecisionType.valueOf(element.attributeValue("decision-type")));
		if(node.getDecisionType().equals(DecisionType.Expression)){
			for(Object obj:element.elements()){
				if(!(obj instanceof Element)){
					continue;
				}
				Element ele=(Element)obj;
				if(ele.getName().equals("expression")){
					node.setExpression(ele.getText());
					break;
				}
			}
			if(StringUtils.isBlank(node.getExpression())){
				node.setExpression(element.attributeValue("expression"));
			}
		}
		node.setHandlerBean(unescape(element.attributeValue("handler-bean")));
		NodeDiagram diagram=parseDiagram(element);
		diagram.setIcon("/icons/decision.svg");
		diagram.setShapeType(ShapeType.Circle);
		diagram.setBorderWidth(1);
		node.setDiagram(diagram);
		return node;
	}

	public boolean support(Element element) {
		return element.getName().equals("decision");
	}

}
