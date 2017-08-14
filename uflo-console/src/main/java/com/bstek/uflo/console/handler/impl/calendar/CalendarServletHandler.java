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
package com.bstek.uflo.console.handler.impl.calendar;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.command.impl.GetAllCalendarDefCommand;
import com.bstek.uflo.command.impl.GetCalendarDateCommand;
import com.bstek.uflo.console.handler.impl.RenderPageServletHandler;
import com.bstek.uflo.console.handler.impl.calendar.command.DeleteCalendarDateCommand;
import com.bstek.uflo.console.handler.impl.calendar.command.DeleteCalendarDefCommand;
import com.bstek.uflo.console.handler.impl.calendar.command.SaveCalendarDateCommand;
import com.bstek.uflo.console.handler.impl.calendar.command.SaveCalendarDefCommand;
import com.bstek.uflo.model.calendar.CalendarDate;
import com.bstek.uflo.model.calendar.CalendarDef;
import com.bstek.uflo.model.calendar.CalendarType;
import com.bstek.uflo.utils.IDGenerator;

/**
 * @author Jacky.gao
 * @since 2016年12月15日
 */
public class CalendarServletHandler extends RenderPageServletHandler {
	private CommandService commandService;
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method=retriveMethod(req);
		if(method!=null){
			invokeMethod(method, req, resp);
		}else{
			VelocityContext context = new VelocityContext();
			context.put("contextPath", req.getContextPath());
			resp.setContentType("text/html");
			resp.setCharacterEncoding("utf-8");
			Template template=ve.getTemplate("uflo-html/calendar.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}
	
	public void loadCalendars(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Collection<CalendarDef> coll=commandService.executeCommand(new GetAllCalendarDefCommand());
		writeObjectToJson(resp, coll);
	}
	public void loadCalendarDate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long calendarId=Long.valueOf(req.getParameter("id"));
		Collection<CalendarDate> coll=commandService.executeCommand(new GetCalendarDateCommand(calendarId));
		writeObjectToJson(resp, coll);
	}
	
	public void saveCalendarDef(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id=req.getParameter("id");
		String name=req.getParameter("name");
		String type=req.getParameter("type");
		String desc=req.getParameter("desc");
		CalendarDef def=new CalendarDef();
		def.setDesc(desc);
		def.setName(name);
		def.setType(CalendarType.valueOf(type));
		if(StringUtils.isNotBlank(id)){
			def.setId(Long.valueOf(id));			
		}else{
			def.setId(IDGenerator.getInstance().nextId());
		}
		commandService.executeCommand(new SaveCalendarDefCommand(def));
		writeObjectToJson(resp, def);
	}
	
	public void deleteCalendarDef(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id=req.getParameter("id");
		CalendarDef def=new CalendarDef();
		def.setId(Long.valueOf(id));
		commandService.executeCommand(new DeleteCalendarDefCommand(def));
	}
	
	public void deleteCalendarDate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id=req.getParameter("id");
		CalendarDate d=new CalendarDate();
		d.setId(Long.valueOf(id));
		commandService.executeCommand(new DeleteCalendarDateCommand(d));
	}
	
	public void saveCalendarDate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id=req.getParameter("id");
		String name=req.getParameter("name");
		String calendarId=req.getParameter("calendarId");
		String rangeStartTime=req.getParameter("rangeStartTime");
		String rangeEndTime=req.getParameter("rangeEndTime");
		String calendarDate=req.getParameter("calendarDate");
		String dayOfMonth=req.getParameter("dayOfMonth");
		String monthOfYear=req.getParameter("monthOfYear");
		String dayOfWeek=req.getParameter("dayOfWeek");
		CalendarDate d=new CalendarDate();
		if(StringUtils.isNotBlank(id)){
			d.setId(Long.valueOf(id));
		}else{
			d.setId(IDGenerator.getInstance().nextId());
		}
		d.setName(name);
		d.setCalendarId(Long.valueOf(calendarId));
		d.setRangeStartTime(rangeStartTime);
		d.setRangeEndTime(rangeEndTime);
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(StringUtils.isNotBlank(calendarDate)){
				d.setCalendarDate(sd.parse(calendarDate));
			}
		} catch (ParseException e) {
			throw new ServletException(e);
		}
		if(StringUtils.isNotBlank(dayOfMonth)){
			d.setDayOfMonth(Integer.valueOf(dayOfMonth));			
		}
		if(StringUtils.isNotBlank(monthOfYear)){
			d.setMonthOfYear(Integer.valueOf(monthOfYear));			
		}
		if(StringUtils.isNotBlank(dayOfWeek)){
			d.setDayOfWeek(Integer.valueOf(dayOfWeek));			
		}
		commandService.executeCommand(new SaveCalendarDateCommand(d));
		writeObjectToJson(resp, d);
	}
	
	
	public void setCommandService(CommandService commandService) {
		this.commandService = commandService;
	}

	@Override
	public String url() {
		return "/calendar";
	}
}
