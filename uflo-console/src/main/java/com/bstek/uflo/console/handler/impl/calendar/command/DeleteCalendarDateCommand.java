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
package com.bstek.uflo.console.handler.impl.calendar.command;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.calendar.CalendarDate;

/**
 * @author Jacky.gao
 * @since 2016年12月15日
 */
public class DeleteCalendarDateCommand implements Command<CalendarDate> {
	private CalendarDate date;

	public DeleteCalendarDateCommand(CalendarDate date) {
		this.date = date;
	}

	public CalendarDate execute(Context context) {
		context.getSession().delete(this.date);
		return null;
	}
}
