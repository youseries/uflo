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

import java.util.ArrayList;
import java.util.List;

import org.quartz.impl.calendar.BaseCalendar;
import org.quartz.impl.calendar.DailyCalendar;

/**
 * @author Jacky.gao
 * @since 2015年1月12日
 */
public class MultipleCalendar extends BaseCalendar{
	private static final long serialVersionUID = -4142756339211533719L;
	private List<WorkdayCalendar> workdayCalendars=new ArrayList<WorkdayCalendar>();
	private List<DailyCalendar> dailyCalendars=new ArrayList<DailyCalendar>();
	private List<BaseCalendar> calendars=new ArrayList<BaseCalendar>();
	public void addCalendar(BaseCalendar calendar){
		if(calendar instanceof WorkdayCalendar){
			workdayCalendars.add((WorkdayCalendar)calendar);
		}else if(calendar instanceof DailyCalendar){
			dailyCalendars.add((DailyCalendar)calendar);
		}else{
			calendars.add(calendar);
		}
	}
	@Override
	public boolean isTimeIncluded(long timeStamp) {
		for(WorkdayCalendar calendar:workdayCalendars){
			if(calendar.isTimeIncluded(timeStamp)){
				for(DailyCalendar dc:dailyCalendars){
					if(dc.isTimeIncluded(timeStamp)==false){
						return false;
					}
				}
				return true;
			}
		}
		for(BaseCalendar calendar:calendars){
			if(calendar.isTimeIncluded(timeStamp)==false){
				return false;
			}
		}
		for(DailyCalendar calendar:dailyCalendars){
			if(calendar.isTimeIncluded(timeStamp)==false){
				return false;
			}
		}
		return true;
	}
	@Override
	public long getNextIncludedTime(long timeStamp) {
        java.util.Calendar day=null;
        for(BaseCalendar calendar:calendars){
        	timeStamp = calendar.getNextIncludedTime(timeStamp);
        	day = getStartOfDayJavaCalendar(timeStamp);
        	while (isTimeIncluded(day.getTime().getTime()) == false) {
        		day.add(java.util.Calendar.DATE, 1);
        	}
        }
        for(DailyCalendar calendar:dailyCalendars){
        	timeStamp = calendar.getNextIncludedTime(timeStamp);
        	day = getStartOfDayJavaCalendar(timeStamp);
        	while (isTimeIncluded(day.getTime().getTime()) == false) {
        		day.add(java.util.Calendar.DATE, 1);
        	}
        }
        return day.getTime().getTime();
	}
}
