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
package com.bstek.uflo.process.node.reminder.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.quartz.Calendar;

import com.bstek.uflo.model.calendar.CalendarDef;
import com.bstek.uflo.process.node.reminder.CalendarInfo;
import com.bstek.uflo.process.node.reminder.CalendarProvider;
import com.bstek.uflo.service.CalendarService;

/**
 * @author Jacky.gao
 * @since 2013年9月23日
 */
public class UfloCalendarProvider implements CalendarProvider {
	private CalendarService calendarService;
	public Calendar getCalendar(String calendarId) {
		return calendarService.getCalendar(Long.valueOf(calendarId));
	}

	public List<CalendarInfo> getCalendarInfos() {
		Collection<CalendarDef> defs=calendarService.getAllCalendarDefs();
		List<CalendarInfo> list=new ArrayList<CalendarInfo>();
		for(CalendarDef def:defs){
			CalendarInfo info=new CalendarInfo();
			info.setId(String.valueOf(def.getId()));
			info.setName(def.getName());
			list.add(info);
		}
		return list;
	}

	public CalendarService getCalendarService() {
		return calendarService;
	}

	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
}
