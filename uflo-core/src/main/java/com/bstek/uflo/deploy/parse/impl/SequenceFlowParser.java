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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.uflo.deploy.parse.Parser;
import com.bstek.uflo.diagram.Point;
import com.bstek.uflo.diagram.SequenceFlowDiagram;
import com.bstek.uflo.process.flow.ConditionType;
import com.bstek.uflo.process.flow.SequenceFlowImpl;

/**
 * @author Jacky.gao
 * @since 2013年8月5日
 */
public class SequenceFlowParser implements Parser {

	public Object parse(Element element,long processId,boolean parseChildren) {
		SequenceFlowImpl flow=new SequenceFlowImpl();
		flow.setProcessId(processId);
		flow.setName(unescape(element.attributeValue("name")));
		flow.setToNode(unescape((element.attributeValue("to"))));
		String conditionType=element.attributeValue("condition-type");
		if(StringUtils.isNotEmpty(conditionType)){
			flow.setConditionType(ConditionType.valueOf(conditionType));			
			flow.setExpression(element.attributeValue("expression"));
			flow.setHandlerBean(element.attributeValue("handler-bean"));
		}
		flow.setDiagram(parseDiagram(element));
		String g=element.attributeValue("g");
		if(StringUtils.isNotBlank(g)){
			int pos=g.indexOf(":");
			if(pos>-1){
				g=g.substring(0,pos);
				g=g.replaceAll(";", ",");
			}else{
				g=null;
			}
		}
		flow.setG(g);
		return flow;
	}

	private SequenceFlowDiagram parseDiagram(Element element){
		SequenceFlowDiagram diagram=new SequenceFlowDiagram();
		diagram.setBorderColor("0,69,123");
		diagram.setFontColor("0,69,123");
		diagram.setBorderWidth(2);
		String name=element.attributeValue("name");
		diagram.setTo(element.attributeValue("to"));
		diagram.setName(name);
		String g=element.attributeValue("g");
		if(StringUtils.isEmpty(g))return diagram;
		String[] pointInfos=null;
		if(org.apache.commons.lang.StringUtils.isNotEmpty(name)){
			String[] info=g.split(":");
			if(info.length==1){
				diagram.setLabelPosition(info[0]);				
				return diagram;
			}
			pointInfos=info[0].split(";");
			diagram.setLabelPosition(info[1]);				
		}else{
			String[] info=g.split(":");
			if(info.length==0){
				pointInfos=g.split(";");
			}else{
				pointInfos=info[0].split(";");
			}
			if(pointInfos.length==0){
				return diagram;
			}
		}
		diagram.setPoints(buildPoint(pointInfos));
		return diagram;
	}
	
	private List<Point> buildPoint(String[] info){
		List<Point> points=new ArrayList<Point>();
		for(String diagram:info){
			String[] d=diagram.split(",");
			if(StringUtils.isEmpty(d[0])){
				continue;
			}
			Point point=new Point();
			point.setX(Integer.valueOf(d[0]));
			point.setY(Integer.valueOf(d[1]));
			points.add(point);
		}
		return points;
	}
	
	public boolean support(Element element) {
		return element.getName().equals("sequence-flow");
	}
	
	protected String unescape(String str){
		if(StringUtils.isEmpty(str))return str;
		str=StringEscapeUtils.escapeXml(str);
		return StringEscapeUtils.unescapeXml(str);
	}
}
