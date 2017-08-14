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

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.uflo.deploy.parse.AbstractTaskParser;
import com.bstek.uflo.diagram.NodeDiagram;
import com.bstek.uflo.diagram.ShapeType;
import com.bstek.uflo.model.task.DateUnit;
import com.bstek.uflo.model.task.TaskType;
import com.bstek.uflo.process.node.AssignmentType;
import com.bstek.uflo.process.node.TaskNode;
import com.bstek.uflo.process.node.reminder.CalendarInfo;
import com.bstek.uflo.process.node.reminder.DueAction;
import com.bstek.uflo.process.node.reminder.DueDefinition;
import com.bstek.uflo.process.node.reminder.PeriodReminder;
import com.bstek.uflo.process.node.reminder.Reminder;

/**
 * @author Jacky.gao
 * @since 2013年7月30日
 */
public class TaskParser extends AbstractTaskParser {

	public Object parse(Element element,long processId,boolean parseChildren) {
		TaskNode node=new TaskNode();
		node.setProcessId(processId);
		parseNodeCommonInfo(element, node);
		node.setSequenceFlows(parseFlowElement(element,processId,parseChildren));
		String countersignMultiplicity=element.attributeValue("countersign-multiplicity");
		if(StringUtils.isNotEmpty(countersignMultiplicity)){
			node.setCountersignMultiplicity(Integer.valueOf(countersignMultiplicity));			
		}
		String countersignPercentMultiplicity=element.attributeValue("countersign-percent-multiplicity");
		if(StringUtils.isNotEmpty(countersignPercentMultiplicity)){
			node.setCountersignPercentMultiplicity(Integer.valueOf(countersignPercentMultiplicity));			
		}
		String countersignExpression=element.attributeValue("countersign-expression");
		if(StringUtils.isNotEmpty(countersignExpression)){
			node.setCountersignExpression(countersignExpression);
		}
		String countersignHandler=element.attributeValue("countersign-handler");
		if(StringUtils.isNotEmpty(countersignHandler)){
			node.setCountersignHandler(countersignHandler);
		}
		String taskListenerBean=element.attributeValue("task-listener-bean");
		if(StringUtils.isNotEmpty(taskListenerBean)){
			node.setTaskListenerBean(taskListenerBean);
		}
		node.setAssignmentType(AssignmentType.valueOf(element.attributeValue("assignment-type")));
		node.setAssignmentHandlerBean(unescape(element.attributeValue("assignment-handler-bean")));
		node.setSwimlane(unescape(element.attributeValue("swimlane")));
		node.setExpression(unescape(element.attributeValue("expression")));
		if(node.getAssignmentType().equals(AssignmentType.Assignee)){
			node.setAssignees(parserAssignees(element));
		}
		
		String allowSpecifyAssigne=element.attributeValue("allow-specify-assignee");
		if(StringUtils.isNotEmpty(allowSpecifyAssigne)){
			node.setAllowSpecifyAssignee(Boolean.valueOf(allowSpecifyAssigne));
		}
		String taskType=element.attributeValue("task-type");
		if(StringUtils.isNotEmpty(taskType)){
			node.setTaskType(TaskType.valueOf(taskType));			
		}
		node.setUrl(unescape(element.attributeValue("url")));
		node.setFormTemplate(unescape(element.attributeValue("form-template")));
		node.setTaskName(unescape(element.attributeValue("task-name")));
		node.setComponentAuthorities(parseComponentAuthorities(element));
		node.setDueDefinition(parseDueDefinition(element));
		node.setFormElements(parseFormElements(element));
		node.setUserData(parseUserData(element));
		NodeDiagram diagram=parseDiagram(element);
		if(TaskType.Countersign.equals(node.getTaskType())){
			diagram.setIcon("/icons/task-countersign.svg");						
		}else{
			diagram.setIcon("/icons/task.svg");			
		}
		diagram.setShapeType(ShapeType.Rectangle);
		diagram.setBorderWidth(1);
		diagram.setBorderColor("3, 104, 154");
		diagram.setBackgroundColor("250, 250, 250");
		node.setDiagram(diagram);
		return node;
	}
	
	private DueDefinition parseDueDefinition(Element element){
		DueDefinition reminderDef=null;
		for(Object obj:element.elements()){
			if(!(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(!ele.getName().equals("due")){
				continue;
			}
			if(reminderDef==null){
				reminderDef=new DueDefinition();
			}
			String dayStr=ele.attributeValue("day");
			if(StringUtils.isNotEmpty(dayStr)){
				reminderDef.setDay(Integer.valueOf(dayStr));				
			}
			String dayHour=ele.attributeValue("hour");
			if(StringUtils.isNotEmpty(dayHour)){
				reminderDef.setHour(Integer.valueOf(dayHour));				
			}
			String minuteStr=ele.attributeValue("minute");
			if(StringUtils.isNotEmpty(minuteStr)){
				reminderDef.setMinute(Integer.valueOf(minuteStr));				
			}
			reminderDef.setCalendarInfos(parseCalendarProviders(ele));
			for(Object child:ele.elements()){
				if(!(child instanceof Element)){
					continue;
				}
				Element childEle=(Element)child;
				String name=childEle.getName();
				
				if(name.equals("once-reminder")){
					Reminder reminder=new Reminder();
					reminder.setHandlerBean(unescape(childEle.attributeValue("handler-bean")));
					reminderDef.setReminder(reminder);
				}else if(name.equals("period-reminder")){
					PeriodReminder reminder=new PeriodReminder();
					reminder.setHandlerBean(unescape(childEle.attributeValue("handler-bean")));
					reminder.setRepeat(Integer.valueOf(childEle.attributeValue("repeat")));
					reminder.setUnit(DateUnit.valueOf(childEle.attributeValue("unit")));
					reminder.setCalendarInfos(parseCalendarProviders(childEle));
					reminderDef.setReminder(reminder);
				}else if(name.equals("due-action")){
					DueAction action=new DueAction();
					String day=childEle.attributeValue("day");
					if(StringUtils.isNotEmpty(day)){
						action.setDay(Integer.valueOf(day));
					}
					String hour=childEle.attributeValue("hour");
					if(StringUtils.isNotEmpty(hour)){
						action.setHour(Integer.valueOf(hour));
					}
					String minute=childEle.attributeValue("minute");
					if(StringUtils.isNotEmpty(minute)){
						action.setMinute(Integer.valueOf(minute));
					}
					action.setCalendarInfos(parseCalendarProviders(childEle));
					action.setHandlerBean(unescape(childEle.attributeValue("handler-bean")));
					reminderDef.setDueAction(action);
				}
			}
			break;
		}
		return reminderDef;
	}
	
	private List<CalendarInfo> parseCalendarProviders(Element element){
		List<CalendarInfo> list=new ArrayList<CalendarInfo>();
		for(Object obj:element.elements()){
			if(!(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(!ele.getName().equals("calendar-provider")){
				continue;				
			}
			CalendarInfo info=new CalendarInfo();
			info.setId(unescape(ele.attributeValue("id")));
			info.setName(unescape(ele.attributeValue("name")));
			list.add(info);
		}
		return list;
	}

	public boolean support(Element element) {
		return element.getName().equals("task");
	}
}
