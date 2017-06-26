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
package com.bstek.uflo.model.calendar;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013年9月23日
 */
@Entity
@Table(name="UFLO_CALENDAR_DATE")
public class CalendarDate  implements java.io.Serializable{
	private static final long serialVersionUID = -9078588793964776579L;
	@Id
	@Column(name="ID_")
	private long id;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="CALENDAR_ID_")
	private long calendarId;
	
	@Column(name="RANGE_START_TIME_",length=60)
	private String rangeStartTime;
	
	@Column(name="RANGE_END_TIME_",length=60)
	private String rangeEndTime;
	
	@Column(name="CALENDAR_DATE_")
	private Date calendarDate;
	@Column(name="DAY_OF_MONTH_")
	private int dayOfMonth;
	@Column(name="MONTH_OF_YEAR_")
	private int monthOfYear;
	@Column(name="DAY_OF_WEEK_")
	private int dayOfWeek;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public long getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(long calendarId) {
		this.calendarId = calendarId;
	}
	public Date getCalendarDate() {
		return calendarDate;
	}
	public void setCalendarDate(Date calendarDate) {
		this.calendarDate = calendarDate;
	}
	public int getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public int getMonthOfYear() {
		return monthOfYear;
	}
	public void setMonthOfYear(int monthOfYear) {
		this.monthOfYear = monthOfYear;
	}
	public String getRangeStartTime() {
		return rangeStartTime;
	}
	public void setRangeStartTime(String rangeStartTime) {
		this.rangeStartTime = rangeStartTime;
	}
	public String getRangeEndTime() {
		return rangeEndTime;
	}
	public void setRangeEndTime(String rangeEndTime) {
		this.rangeEndTime = rangeEndTime;
	}
}
