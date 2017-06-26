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
 * @since 2014年7月7日
 */
public class DueAction {
	private int day;
	private int hour;
	private int minute;
	private String handlerBean;
	private List<CalendarInfo> calendarInfos;
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
	public String getHandlerBean() {
		return handlerBean;
	}
	public void setHandlerBean(String handlerBean) {
		this.handlerBean = handlerBean;
	}
	public List<CalendarInfo> getCalendarInfos() {
		return calendarInfos;
	}
	public void setCalendarInfos(List<CalendarInfo> calendarInfos) {
		this.calendarInfos = calendarInfos;
	}
}
