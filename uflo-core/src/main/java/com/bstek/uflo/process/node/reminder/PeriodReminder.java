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

import com.bstek.uflo.model.task.DateUnit;

/**
 * @author Jacky.gao
 * @since 2013年8月20日
 */
public class PeriodReminder extends Reminder {
	private static final long serialVersionUID = 1L;
	private int repeat;
	private DateUnit unit;
	private List<CalendarInfo> calendarInfos;
	public int getRepeat() {
		return repeat;
	}
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	public DateUnit getUnit() {
		return unit;
	}
	public void setUnit(DateUnit unit) {
		this.unit = unit;
	}
	public List<CalendarInfo> getCalendarInfos() {
		return calendarInfos;
	}
	public void setCalendarInfos(
			List<CalendarInfo> calendarInfos) {
		this.calendarInfos = calendarInfos;
	}
}
