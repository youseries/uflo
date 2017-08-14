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
package com.bstek.uflo.console.handler.impl.central;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.codehaus.jackson.map.ObjectMapper;

import com.bstek.uflo.command.impl.jump.JumpNode;
import com.bstek.uflo.console.handler.impl.PageData;
import com.bstek.uflo.console.handler.impl.RenderPageServletHandler;
import com.bstek.uflo.model.HistoryProcessInstance;
import com.bstek.uflo.model.HistoryTask;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskParticipator;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.process.flow.SequenceFlowImpl;
import com.bstek.uflo.process.node.Node;
import com.bstek.uflo.query.HistoryProcessInstanceQuery;
import com.bstek.uflo.query.HistoryTaskQuery;
import com.bstek.uflo.query.ProcessInstanceQuery;
import com.bstek.uflo.query.ProcessQuery;
import com.bstek.uflo.query.TaskQuery;
import com.bstek.uflo.service.HistoryService;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.StartProcessInfo;
import com.bstek.uflo.service.TaskService;
import com.bstek.uflo.utils.EnvironmentUtils;

/**
 * @author Jacky.gao
 * @since 2016年12月10日
 */
public class CentralServletHandler extends RenderPageServletHandler{
	private ProcessService processService;
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
			Template template=ve.getTemplate("uflo-html/central.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}
	public void loadHistoryProcessInstance(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long processId=Long.valueOf(req.getParameter("processId"));
		int pageIndex=Integer.valueOf(req.getParameter("pageIndex"));
		int pageSize=Integer.valueOf(req.getParameter("pageSize"));
		HistoryProcessInstanceQuery historyProcessInstanceQuery = historyService.createHistoryProcessInstanceQuery();
		historyProcessInstanceQuery.processId(processId);
		historyProcessInstanceQuery.addOrderDesc("createDate");
		historyProcessInstanceQuery.page((pageIndex - 1) * pageSize, pageSize);
		int total=historyProcessInstanceQuery.count();
		List<HistoryProcessInstance> list=historyProcessInstanceQuery.list();
		writeObjectToJson(resp, new PageData(list,pageSize,pageIndex,total));
	}
	public void loadHistoryTasks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long historyProcessInstanceId=Long.valueOf(req.getParameter("historyProcessInstanceId"));
		HistoryTaskQuery historyTaskQuery = historyService.createHistoryTaskQuery();
		historyTaskQuery.historyProcessInstanceId(historyProcessInstanceId);
		List<HistoryTask> list=historyTaskQuery.list();
		writeObjectToJson(resp, list);
	}
	
	public void loadSequenceFlows(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long taskId=Long.valueOf(req.getParameter("taskId"));
		Task task = taskService.getTask(taskId);
		ProcessDefinition pd = processService.getProcessById(task.getProcessId());
		Node node = pd.getNode(task.getNodeName());
		List<SequenceFlowImpl> flows=new ArrayList<SequenceFlowImpl>();
		List<SequenceFlowImpl> list=node.getSequenceFlows();
		if(list!=null){
			for(SequenceFlowImpl flow:list){
				String flowName=flow.getName();
				if(flowName!=null && !flowName.startsWith(TaskService.TEMP_FLOW_NAME_PREFIX)){
					flows.add(flow);
				}
			}
		}
		writeObjectToJson(resp, flows);
	}
	public void loadProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name=req.getParameter("name");
		int pageIndex=Integer.valueOf(req.getParameter("pageIndex"));
		int pageSize=Integer.valueOf(req.getParameter("pageSize"));
		ProcessQuery query = processService.createProcessQuery();
		query.addOrderDesc("createDate");
		if(StringUtils.isNotBlank(name)){
			query.nameLike("%"+name+"%");
		}
		int firstResult=(pageIndex-1)*pageSize;
		query.page(firstResult, pageSize);
		int total=query.count();
		List<ProcessDefinition> processes=query.list();
		PageData page=new PageData(processes,pageSize,pageIndex,total);
		writeObjectToJson(resp, page);
	}
	public void loadProcessInstance(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ProcessInstanceQuery query = processService.createProcessInstanceQuery();
		long processId=Long.valueOf(req.getParameter("processId"));
		int pageIndex=Integer.valueOf(req.getParameter("pageIndex"));
		int pageSize=Integer.valueOf(req.getParameter("pageSize"));
		int firstResult=(pageIndex-1)*pageSize;
		query.page(firstResult, pageSize);
		query.processId(processId);
		query.addOrderDesc("createDate");
		int total=query.count();
		List<ProcessInstance> list=query.list();
		writeObjectToJson(resp, new PageData(list,pageSize,pageIndex,total));
	}
	public void jumpTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long taskId=Long.valueOf(req.getParameter("taskId"));
		String nodeName=req.getParameter("nodeName");
		taskService.forward(taskId, nodeName);
	}
	
	public void loadJumpTasks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long taskId=Long.valueOf(req.getParameter("taskId"));
		List<JumpNode> nodes=taskService.getAvaliableForwardTaskNodes(taskId);
		writeObjectToJson(resp, nodes);
	}
	public void cliamTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long taskId=Long.valueOf(req.getParameter("taskId"));
		String user=req.getParameter("user");
		taskService.claim(taskId, user);
	}
	public void loadCliamUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long taskId=Long.valueOf(req.getParameter("taskId"));
		List<TaskParticipator> list=taskService.getTaskParticipators(taskId);
		writeObjectToJson(resp, list);
	}
	public void loadTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TaskQuery query = taskService.createTaskQuery();
		query.addTaskState(TaskState.Created);
		query.addTaskState(TaskState.InProgress);
		query.addTaskState(TaskState.Ready);
		query.addTaskState(TaskState.Suspended);
		query.addTaskState(TaskState.Reserved);
		Long processInstanceId=Long.valueOf(req.getParameter("processInstanceId"));
		query.processInstanceId(processInstanceId);
		query.addOrderDesc("createDate");
		writeObjectToJson(resp, query.list());
	}
	public void startProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long processId=Long.valueOf(req.getParameter("processId"));
		StartProcessInfo startProcessInfo = new StartProcessInfo(EnvironmentUtils.getEnvironment().getLoginUser());
		startProcessInfo.setCompleteStartTask(true);
		String variables=req.getParameter("variables");
		Map<String,Object> variableMaps=buildVariables(variables);
		if(variableMaps!=null){
			startProcessInfo.setVariables(variableMaps);
		}
		processService.startProcessById(processId, startProcessInfo);
	}
	public void deleteProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long processId=Long.valueOf(req.getParameter("processId"));
		processService.deleteProcess(processId);
	}
	public void deleteProcessInstance(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long processInstanceId=Long.valueOf(req.getParameter("processInstanceId"));
		processService.deleteProcessInstanceById(processInstanceId);
	}
	public void completeTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long taskId=Long.valueOf(req.getParameter("taskId"));
		String sequenceFlowName=req.getParameter("flowName");
		String variables=req.getParameter("variables");
		Map<String,Object> variableMaps=buildVariables(variables);
		taskService.start(taskId);
		if(variableMaps!=null){
			if(StringUtils.isBlank(sequenceFlowName)){
				taskService.complete(taskId, variableMaps);							
			}else{
				taskService.complete(taskId,sequenceFlowName,variableMaps);											
			}
		}else{
			taskService.complete(taskId);						
		}
	}
	
	private Map<String,Object> buildVariables(String variables){
		if(StringUtils.isBlank(variables)){
			return null;
		}
		ObjectMapper mapper=new ObjectMapper();
		try {
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> list=mapper.readValue(variables, ArrayList.class);
			Map<String,Object> map=new HashMap<String,Object>();
			for(Map<String,Object> m:list){
				String key=m.get("key").toString();
				Object value=m.get("value");
				map.put(key, value);
			}
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}
	public void setProcessService(ProcessService processService) {
		this.processService = processService;
	}
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
	@Override
	public String url() {
		return "/central";
	}
}
