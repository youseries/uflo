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

import java.util.Collections;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import org.quartz.Calendar;
import org.quartz.impl.calendar.BaseCalendar;

/**
 * <p>
 * 一个用来实现换休的Calendar，它的作用是定义某些特定日期，将这些特定日期强行作为工作日，而不管其它Calendar是否将这个日期设置为节假日，<br>
 * 也就是说这个特定日期有可能被其它Calendar设置成休息日，但这里却强行将这个日期设置成工作日，以实现换休功能。
 * </p>
 * @author Jacky.gao
 * @since 2014年9月25日
 */
public class WorkdayCalendar extends BaseCalendar {
	private static final long serialVersionUID = 4588095268779531440L;
    private TreeSet<Date> workdays = new TreeSet<Date>();
    public WorkdayCalendar() {
	}
    public WorkdayCalendar(Calendar baseCalendar) {
    	super(baseCalendar);
    }
    
    /**
     * 对于工作日的判断与节假日正好相反，只需要简单判断当前给定日期是否是工作日即可，如果是工作日，那么就直接返回true，否则返回false
     */
    @Override
    public boolean isTimeIncluded(long timeStamp) {
        Date lookFor = getStartOfDayJavaCalendar(timeStamp).getTime();
        boolean include=workdays.contains(lookFor);
        return include;
    }

    /**
     * 返回下一个休息时间，这里首先判断其父calendar，让父返回下一个时间，<br>
     * 再拿这个时间与当前工作日比较，如果是工作日，那么重复进行父calendar返回休息时间
     */
    @Override
    public long getNextIncludedTime(long timeStamp) {
         return timeStamp;
    }

    public void addIncludedDate(Date includedDate) {
        Date date = getStartOfDayJavaCalendar(includedDate.getTime()).getTime();
        this.workdays.add(date);
    }

    public void removeIncludedDate(Date dateToRemove) {
        Date date = getStartOfDayJavaCalendar(dateToRemove.getTime()).getTime();
        workdays.remove(date);
    }

    public SortedSet<Date> getIncludedDates() {
        return Collections.unmodifiableSortedSet(workdays);
    }
}
