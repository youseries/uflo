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
package com.bstek.uflo.console.handler.impl.todo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.bstek.uflo.console.handler.impl.PageData;
import com.bstek.uflo.console.handler.impl.RenderPageServletHandler;
import com.bstek.uflo.model.HistoryTask;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.query.HistoryTaskQuery;
import com.bstek.uflo.query.TaskQuery;
import com.bstek.uflo.service.HistoryService;
import com.bstek.uflo.service.TaskService;
import com.bstek.uflo.utils.EnvironmentUtils;

/**
 * @author Jacky.gao
 * @since 2016年12月7日
 */
public class TodoServletHandler extends RenderPageServletHandler {
	private TaskService taskService;
	private HistoryService historyService;
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
			Template template=ve.getTemplate("uflo-html/todo.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}
	public void claimTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String loginUsername=EnvironmentUtils.getEnvironment().getLoginUser();
		String taskId=req.getParameter("taskId");
		taskService.claim(Long.valueOf(taskId), loginUsername);
	}

	public void loadTodo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String loginUsername=EnvironmentUtils.getEnvironment().getLoginUser();
		String taskName=req.getParameter("taskName");
		int pageSize=Integer.valueOf(req.getParameter("pageSize"));
		int pageIndex=Integer.valueOf(req.getParameter("pageIndex"));
		int firstResult=(pageIndex-1)*pageSize;
		TaskQuery query=taskService.createTaskQuery();
		query.addTaskState(TaskState.Created);
		query.addTaskState(TaskState.InProgress);
		query.addTaskState(TaskState.Ready);
		query.addTaskState(TaskState.Suspended);
		query.addTaskState(TaskState.Reserved);
		query.addAssignee(loginUsername).addOrderDesc("createDate").page(firstResult, pageSize);
		if(StringUtils.isNotBlank(taskName)){
			query.nameLike("%"+taskName+"%");
		}
		int total=query.count();
		List<Task> tasks=query.list();
		PageData pageData=new PageData(tasks,pageSize,pageIndex,total);
		writeObjectToJson(resp, pageData);
	}
	
	public void loadClaim(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String loginUsername=EnvironmentUtils.getEnvironment().getLoginUser();
		int pageSize=Integer.valueOf(req.getParameter("pageSize"));
		int pageIndex=Integer.valueOf(req.getParameter("pageIndex"));
		String taskName=req.getParameter("taskName");
		int firstResult=(pageIndex-1)*pageSize;
		TaskQuery query=taskService.createTaskQuery();
		if(StringUtils.isNotBlank(taskName)){
			query.nameLike("%"+taskName+"%");
		}
		query.addTaskState(TaskState.Ready).addParticipator(loginUsername).addOrderDesc("createDate").page(firstResult, pageSize);
		int total=query.count();
		List<Task> tasks=query.list();
		PageData pageData=new PageData(tasks,pageSize,pageIndex,total);
		writeObjectToJson(resp, pageData);
	}
	
	public void loadHistory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String loginUsername=EnvironmentUtils.getEnvironment().getLoginUser();
		int pageSize=Integer.valueOf(req.getParameter("pageSize"));
		int pageIndex=Integer.valueOf(req.getParameter("pageIndex"));
		String taskName=req.getParameter("taskName");
		int firstResult=(pageIndex-1)*pageSize;
		HistoryTaskQuery query=historyService.createHistoryTaskQuery();
		if(StringUtils.isNotBlank(taskName)){
			query.nameLike("%"+taskName+"%");
		}
		query.assignee(loginUsername).addOrderDesc("endDate").page(firstResult, pageSize);
		int total=query.count();
		List<HistoryTask> tasks=query.list();
		PageData pageData=new PageData(tasks,pageSize,pageIndex,total);
		writeObjectToJson(resp, pageData);
	}
	
	
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
	
	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}
	
	@Override
	public String url() {
		return "/todo";
	}
}
