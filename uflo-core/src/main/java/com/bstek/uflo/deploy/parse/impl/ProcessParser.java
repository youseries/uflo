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

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.bstek.uflo.deploy.parse.AbstractParser;
import com.bstek.uflo.deploy.parse.Parser;
import com.bstek.uflo.diagram.ProcessDiagram;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.process.node.Node;
import com.bstek.uflo.process.node.StartNode;
import com.bstek.uflo.process.swimlane.Swimlane;
import com.bstek.uflo.utils.EnvironmentUtils;

/**
 * @author Jacky.gao
 * @since 2013年7月30日
 */
public class ProcessParser extends AbstractParser{
	private static ProcessParser processParser;
	private static final Log log=LogFactory.getLog(ProcessParser.class);
	public static ProcessDefinition parseProcess(byte[] bytes,long processId,boolean parseChildren) throws Exception{
		ByteArrayInputStream bin=new ByteArrayInputStream(bytes);
		try{
			SAXReader reader=new SAXReader();
			Document document=reader.read(bin);
			Element root=document.getRootElement();
			if(processParser.support(root)){
				ProcessDefinition pd=(ProcessDefinition)processParser.parse(root,processId,parseChildren);
				return pd;
			}
			return null;
		}finally{
			IOUtils.closeQuietly(bin);
		}
	}
	
	public Object parse(Element element,long processId,boolean parseChildren) {
		ProcessDefinition process=new ProcessDefinition();
		process.setId(processId);
		process.setEventHandlerBean(unescape(element.attributeValue("event-handler-bean")));
		process.setName(unescape(element.attributeValue("name")));
		process.setStartProcessUrl(unescape(element.attributeValue("start-process-url")));
		process.setKey(unescape(element.attributeValue("key")));
		process.setCategoryId(unescape(element.attributeValue("category-id")));
		process.setCategory(unescape(element.attributeValue("category")));
		String effectDateStr=unescape(element.attributeValue("effect-date"));
		if(StringUtils.isNotEmpty(effectDateStr)){
			SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				process.setEffectDate(sd.parse(effectDateStr));
			} catch (ParseException e) {
				log.error("Process definition "+process.getName()+" effect date is invalid:"+effectDateStr);
			}
		}
		if(StringUtils.isEmpty(process.getCategoryId())){
			process.setCategoryId(EnvironmentUtils.getEnvironment().getCategoryId());
		}
		Element descElement=element.element("description");
		if(descElement!=null){
			String desc=descElement.getTextTrim();
			desc=(desc.length()>120?desc.substring(0,120):desc);
			process.setDescription(unescape(desc));
		}
		if(parseChildren){
			List<Node> nodes=new ArrayList<Node>();
			List<Swimlane> swimlanes=new ArrayList<Swimlane>();
			for(Object obj:element.elements()){
				if(!(obj instanceof Element)){
					continue;
				}
				Element childElement=(Element)obj;
				for(Parser parser:parsers){
					if(!parser.support(childElement)){
						continue;
					}
					Object processElement=parser.parse(childElement,processId,parseChildren);
					if(processElement instanceof Node){
						Node node=(Node)processElement;
						node.setProcessId(processId);
						nodes.add(node);
					}
					if(processElement instanceof StartNode){
						process.setStartNode((StartNode)processElement);
					}
					if(processElement instanceof Swimlane){
						swimlanes.add((Swimlane)processElement);
					}
					break;
				}
			}
			process.setSwimlanes(swimlanes);
			process.setNodes(nodes);
		}
		if(parseChildren){
			process.setDiagram(buildProcessDiagram(process));			
		}
		return process;
	}
	
	private ProcessDiagram buildProcessDiagram(ProcessDefinition process){
		ProcessDiagramBudiler processDiagramBuilder=new ProcessDiagramBudiler(process);
		return processDiagramBuilder.getDiagram();
	}
	

	public boolean support(Element element) {
		return element.getName().equals("uflo-process");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		super.setApplicationContext(applicationContext);
		ProcessParser.processParser=this;
	}
}
