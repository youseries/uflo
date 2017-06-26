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
package com.bstek.uflo.process.node.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Jacky.gao
 * @since 2013年9月18日
 */
public class BusinessCalendar {
	public static final String BEAN_ID="uflo.businessCalendar";
	private int businessDayHours;
	
	public Date calEndDate(Calendar startDate,Calendar endDate,org.quartz.Calendar baseCalendar){
		int holidayMinutes=calHolidayMinutes(startDate,endDate,baseCalendar);
		if(holidayMinutes==0){
			return endDate.getTime();
		}
		startDate.setTime(endDate.getTime());
		endDate.add(Calendar.MINUTE, holidayMinutes);
		return calEndDate(startDate,endDate,baseCalendar);
	}

	private int calHolidayMinutes(Calendar startDate,Calendar endDate,org.quartz.Calendar baseCalendar) {
		if(startDate.getTimeInMillis()==endDate.getTimeInMillis()){
			return 0;
		}
		int count=0;
		while(startDate.getTimeInMillis()<endDate.getTimeInMillis()){
			if(!baseCalendar.isTimeIncluded(startDate.getTime().getTime())){
				count++;
			}
			startDate.add(Calendar.MINUTE, 1);
		}
		return count;
	}

	public int getBusinessDayHours() {
		return businessDayHours;
	}

	public void setBusinessDayHours(int businessDayHours) {
		this.businessDayHours = businessDayHours;
	}
}
