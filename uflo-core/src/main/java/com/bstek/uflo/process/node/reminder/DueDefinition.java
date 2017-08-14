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
package com.bstek.uflo.process.node.reminder;

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2013年8月20日
 */
public class DueDefinition implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private int day;
	private int hour;
	private int minute;
	private int businessDayHours;
	private List<CalendarInfo> calendarInfos;
	private Reminder reminder;
	private DueAction dueAction;
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public List<CalendarInfo> getCalendarInfos() {
		return calendarInfos;
	}
	public void setCalendarInfos(List<CalendarInfo> calendarInfos) {
		this.calendarInfos = calendarInfos;
	}
	public Reminder getReminder() {
		return reminder;
	}
	public void setReminder(Reminder reminder) {
		this.reminder = reminder;
	}
	public DueAction getDueAction() {
		return dueAction;
	}
	public void setDueAction(DueAction dueAction) {
		this.dueAction = dueAction;
	}
	public int getBusinessDayHours() {
		return businessDayHours;
	}
	public void setBusinessDayHours(int businessDayHours) {
		this.businessDayHours = businessDayHours;
	}
}
