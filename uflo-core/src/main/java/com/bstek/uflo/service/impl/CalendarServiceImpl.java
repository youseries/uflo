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
package com.bstek.uflo.service.impl;

import java.util.Collection;
import java.util.GregorianCalendar;

import org.quartz.Calendar;
import org.quartz.impl.calendar.AnnualCalendar;
import org.quartz.impl.calendar.DailyCalendar;
import org.quartz.impl.calendar.HolidayCalendar;
import org.quartz.impl.calendar.MonthlyCalendar;
import org.quartz.impl.calendar.WeeklyCalendar;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.command.impl.GetAllCalendarDefCommand;
import com.bstek.uflo.command.impl.GetCalendarDateCommand;
import com.bstek.uflo.command.impl.GetCalendarDefCommand;
import com.bstek.uflo.model.calendar.CalendarDate;
import com.bstek.uflo.model.calendar.CalendarDef;
import com.bstek.uflo.model.calendar.CalendarType;
import com.bstek.uflo.process.node.calendar.WorkdayCalendar;
import com.bstek.uflo.service.CalendarService;

/**
 * @author Jacky.gao
 * @since 2013年9月23日
 */
public class CalendarServiceImpl implements CalendarService {
	private CommandService commandService;
	public Collection<CalendarDef> getAllCalendarDefs() {
		return commandService.executeCommand(new GetAllCalendarDefCommand());
	}

	public CalendarDef getCalendarDef(long calendarId) {
		return commandService.executeCommand(new GetCalendarDefCommand(calendarId));
	}
	
	public Collection<CalendarDate> getCalendarDate(long calendarId) {
		return commandService.executeCommand(new GetCalendarDateCommand(calendarId));
	}
	
	public Calendar getCalendar(long calendarId) {
		CalendarDef def=getCalendarDef(calendarId);
		Collection<CalendarDate> dates=getCalendarDate(calendarId);
		Calendar baseCalendar=buildCalendar(def,dates);
		return baseCalendar;
	}
	
	private Calendar buildCalendar(CalendarDef calendarDef,Collection<CalendarDate> dates){
		CalendarType type=calendarDef.getType();
		if(type.equals(CalendarType.holiday)){
			HolidayCalendar calendar=new HolidayCalendar();
			if(dates!=null){
				for(CalendarDate d:dates){
					calendar.addExcludedDate(d.getCalendarDate());
				}
			}
			return calendar;
		}else if(type.equals(CalendarType.workday)){
			WorkdayCalendar calendar=new WorkdayCalendar();
			if(dates!=null){
				for(CalendarDate d:dates){
					calendar.addIncludedDate(d.getCalendarDate());
				}
			}
			return calendar;
		}else if(type.equals(CalendarType.annual)){
			AnnualCalendar calendar=new AnnualCalendar();
			if(dates!=null){
				for(CalendarDate d:dates){
					java.util.Calendar c=new GregorianCalendar();
					c.set(java.util.Calendar.MONTH,d.getMonthOfYear());
					c.set(java.util.Calendar.DAY_OF_MONTH,d.getDayOfMonth());
					calendar.setDayExcluded(c,true);
				}
			}
			return calendar;
		}else if(type.equals(CalendarType.monthly)){
			MonthlyCalendar calendar=new MonthlyCalendar();
			if(dates!=null){
				for(CalendarDate d:dates){
					calendar.setDayExcluded(d.getDayOfMonth(),true);
				}
			}
			return calendar;
		}else if(type.equals(CalendarType.weekly)){
			WeeklyCalendar calendar=new WeeklyCalendar();
			calendar.setDayExcluded(java.util.Calendar.SUNDAY, false);
			calendar.setDayExcluded(java.util.Calendar.SATURDAY, false);
			if(dates!=null){
				for(CalendarDate d:dates){
					calendar.setDayExcluded(d.getDayOfWeek(),true);
				}
			}
			return calendar;
		}else if(type.equals(CalendarType.daily)){
			if(dates!=null){
				DailyCalendar calendar=null;
				for(CalendarDate d:dates){
					calendar=new DailyCalendar(calendar,d.getRangeStartTime(),d.getRangeEndTime());
				}
				return calendar;
			}
		}
		return null;			
	}

	public CommandService getCommandService() {
		return commandService;
	}

	public void setCommandService(CommandService commandService) {
		this.commandService = commandService;
	}
}
