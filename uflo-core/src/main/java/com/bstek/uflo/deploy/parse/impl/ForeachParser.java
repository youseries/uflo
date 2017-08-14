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
import com.bstek.uflo.process.node.ForeachNode;
import com.bstek.uflo.process.node.ForeachType;

/**
 * @author Jacky.gao
 * @since 2013年8月13日
 */
public class ForeachParser extends AbstractParser {

	public Object parse(Element element, long processId, boolean parseChildren) {
		ForeachNode node=new ForeachNode();
		node.setProcessId(processId);
		parseNodeCommonInfo(element, node);
		node.setSequenceFlows(parseFlowElement(element,processId,parseChildren));
		String type=element.attributeValue("foreach-type");
		if(StringUtils.isNotEmpty(type)){
			node.setForeachType(ForeachType.valueOf(type));			
		}
		node.setVariable(unescape(element.attributeValue("var")));
		if(StringUtils.isNotBlank(element.attributeValue("process-variable"))){
			node.setProcessVariable(unescape(element.attributeValue("process-variable")));			
		}else{
			node.setProcessVariable(unescape(element.attributeValue("in")));						
		}
		node.setHandlerBean(unescape(element.attributeValue("handler-bean")));
		NodeDiagram diagram=parseDiagram(element);
		diagram.setIcon("/icons/foreach.svg");
		diagram.setShapeType(ShapeType.Circle);
		diagram.setBorderWidth(1);
		node.setDiagram(diagram);
		return node;
	}

	public boolean support(Element element) {
		return element.getName().equals("foreach");
	}

}
