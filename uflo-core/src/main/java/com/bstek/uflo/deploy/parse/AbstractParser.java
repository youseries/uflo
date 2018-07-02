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
package com.bstek.uflo.deploy.parse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.uflo.deploy.StringTools;
import com.bstek.uflo.diagram.NodeDiagram;
import com.bstek.uflo.process.assign.Assignee;
import com.bstek.uflo.process.flow.SequenceFlowImpl;
import com.bstek.uflo.process.node.FormElement;
import com.bstek.uflo.process.node.Mapping;
import com.bstek.uflo.process.node.Node;
import com.bstek.uflo.process.node.UserData;
import com.bstek.uflo.process.security.Authority;
import com.bstek.uflo.utils.Utils;


/**
 * @author Jacky.gao
 * @since 2013年7月30日
 */
public abstract class AbstractParser implements Parser,ApplicationContextAware {
	protected Collection<Parser> parsers;
	
	protected String unescape(String str){
		if(StringUtils.isEmpty(str))return str;
		return StringTools.unescape(str);
	}
	
	protected NodeDiagram parseDiagram(Element element){
		NodeDiagram diagram=new NodeDiagram();
		String g=element.attributeValue("g");
		String name=element.attributeValue("name");
		String label=element.attributeValue("label");
		diagram.setLabel(label);			
		diagram.setName(name);
		if(StringUtils.isNotBlank(g)){
			String[] info=g.split(",");
			if(info.length!=4){
				throw new IllegalArgumentException("Node "+element.attributeValue("name")+" diagram info is invalide!");
			}
			BigDecimal x=Utils.toBigDecimal(info[0]);
			diagram.setX(x.intValue());
			BigDecimal y=Utils.toBigDecimal(info[1]);
			diagram.setY(y.intValue());
			BigDecimal w=Utils.toBigDecimal(info[2]);
			diagram.setWidth(w.intValue());
			BigDecimal h=Utils.toBigDecimal(info[3]);
			diagram.setHeight(h.intValue());			
		}
		String x=element.attributeValue("x");
		String y=element.attributeValue("y");
		String width=element.attributeValue("width");
		String height=element.attributeValue("height");
		if(StringUtils.isNotBlank(x)){
			BigDecimal bd=Utils.toBigDecimal(x);
			diagram.setX(bd.intValue());
		}
		if(StringUtils.isNotBlank(y)){
			BigDecimal bd=Utils.toBigDecimal(y);
			diagram.setY(bd.intValue());
		}
		if(StringUtils.isNotBlank(width)){
			BigDecimal bd=Utils.toBigDecimal(width);
			diagram.setWidth(bd.intValue());
		}
		if(StringUtils.isNotBlank(height)){
			BigDecimal bd=Utils.toBigDecimal(height);
			diagram.setHeight(bd.intValue());
		}
		return diagram;
	}

	
	protected void parseNodeCommonInfo(Element element, Node node) {
		node.setName(unescape(element.attributeValue("name")));
		String label=element.attributeValue("label");
		if(StringUtils.isNotEmpty(label)){
			node.setLabel(unescape(label));
		}
		Element desc=element.element("description");
		if(desc!=null){
			node.setDescription(unescape(desc.getTextTrim()));
		}
		node.setEventHandlerBean(unescape(element.attributeValue("event-handler-bean")));
		int x=0,y=0,width=80,height=40;
		String g = element.attributeValue("g");
		if(StringUtils.isNotBlank(g)){
			StringTokenizer tokenizer=new StringTokenizer(g,",");
			if (tokenizer.countTokens() == 4) {
				x=Integer.valueOf(tokenizer.nextToken());
				y=Integer.valueOf(tokenizer.nextToken());
				width=Integer.valueOf(tokenizer.nextToken());
				height=Integer.valueOf(tokenizer.nextToken());
			}
		}
		node.setX(x);
		node.setY(y);
		node.setWidth(width);
		node.setHeight(height);
		String xAttr=element.attributeValue("x");
		String yAttr=element.attributeValue("y");
		String widthAttr=element.attributeValue("width");
		String heightAttr=element.attributeValue("height");
		if(StringUtils.isNotBlank(xAttr)){
			BigDecimal bd=new BigDecimal(xAttr);
			node.setX(bd.intValue());
		}
		if(StringUtils.isNotBlank(yAttr)){
			BigDecimal bd=new BigDecimal(yAttr);
			node.setY(bd.intValue());
		}
		if(StringUtils.isNotBlank(widthAttr)){
			BigDecimal bd=new BigDecimal(widthAttr);
			node.setWidth(bd.intValue());
		}
		if(StringUtils.isNotBlank(heightAttr)){
			BigDecimal bd=new BigDecimal(heightAttr);
			node.setHeight(bd.intValue());
		}
		
	}
	
	protected List<Assignee> parserAssignees(Element element){
		List<Assignee> assignees=new ArrayList<Assignee>();
		for(Object obj:element.elements()){
			if(!(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(!ele.getName().equals("assignee")){
				continue;
			}
			String id=unescape(ele.attributeValue("id"));
			String name=unescape(ele.attributeValue("name"));
			String providerId=unescape(ele.attributeValue("provider-id"));
			Assignee assignee=new Assignee();
			assignee.setId(id);
			assignee.setName(name);
			assignee.setProviderId(providerId);
			assignees.add(assignee);
		}
		return assignees;
	}
	
	protected List<UserData> parseUserData(Element element){
		List<UserData> data=new ArrayList<UserData>();
		for(Object object:element.elements()){
			if(!(object instanceof Element))continue;
			Element ele=(Element)object;
			if(!ele.getName().equals("user-data"))continue;
			data.add(new UserData(ele.attributeValue("key"),ele.attributeValue("value")));
		}
		return data;
	}
	
	protected List<FormElement> parseFormElements(Element element){
		List<FormElement> formElements=new ArrayList<FormElement>();
		for(Object object:element.elements()){
			if(!(object instanceof Element))continue;
			Element ele=(Element)object;
			if(!ele.getName().equals("form-element"))continue;
			FormElement formElement=new FormElement();
			formElement.setName(ele.attributeValue("name"));
			formElement.setCaption(ele.attributeValue("caption"));
			formElement.setDataType(ele.attributeValue("data-type"));
			formElement.setDefaultValue(ele.attributeValue("default-value"));
			formElement.setEditorType(ele.attributeValue("editor-type"));
			formElement.setRequired(Boolean.valueOf(ele.attributeValue("required")));
			formElement.setAuthority(Authority.valueOf(ele.attributeValue("authority")));
			List<Mapping> mappings=null;
			for(Object obj:ele.elements()){
				if(!(obj instanceof Element)){
					continue;
				}
				Element mappingElement=(Element)obj;
				if(!mappingElement.getName().equals("mapping")){
					continue;
				}
				if(mappings==null){
					mappings=new ArrayList<Mapping>();
				}
				Mapping mapping=new Mapping();
				mapping.setKey(mappingElement.attributeValue("key"));
				mapping.setLabel(mappingElement.attributeValue("label"));
				mappings.add(mapping);
			}
			formElement.setMappings(mappings);
			formElements.add(formElement);
		}
		return formElements;
	}
	
	protected List<SequenceFlowImpl> parseFlowElement(Element element,long processId,boolean parseChildren){
		List<SequenceFlowImpl> flows=new ArrayList<SequenceFlowImpl>();
		for(Object obj:element.elements()){
			if(!(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			for(Parser parser:parsers){
				if(!parser.support(ele)){
					continue;
				}
				Object processElement=parser.parse(ele,processId,parseChildren);
				if(processElement instanceof SequenceFlowImpl){
					SequenceFlowImpl flow=(SequenceFlowImpl)processElement;
					flows.add(flow);
				}
				break;
			}
		}
		return flows;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		parsers=applicationContext.getBeansOfType(Parser.class).values();
	}
}
