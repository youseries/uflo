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
package com.bstek.uflo.console.handler.impl.diagram;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.bstek.uflo.console.handler.impl.RenderPageServletHandler;
import com.bstek.uflo.diagram.NodeDiagram;
import com.bstek.uflo.diagram.ProcessDiagram;
import com.bstek.uflo.diagram.SequenceFlowDiagram;
import com.bstek.uflo.diagram.TaskDiagramInfoProvider;
import com.bstek.uflo.diagram.TaskInfo;
import com.bstek.uflo.model.HistoryActivity;
import com.bstek.uflo.model.HistoryProcessInstance;
import com.bstek.uflo.model.HistoryTask;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.process.node.Node;
import com.bstek.uflo.process.node.StartNode;
import com.bstek.uflo.process.node.TaskNode;
import com.bstek.uflo.query.HistoryTaskQuery;
import com.bstek.uflo.query.TaskQuery;
import com.bstek.uflo.service.HistoryService;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.TaskService;

/**
 * @author Jacky.gao
 * @since 2016年12月8日
 */
public class ProcessDiagramServletHandler extends RenderPageServletHandler{
	private boolean showTime;
	private String passedNodeBgcolor;
	private String passedNodeFontColor;
	private int passedNodeFontSize;
	private String passedNodeBorderColor;
	private String passedConnectionColor;
	private String passedConnectionFontColor;
	private int passedConnectionFontSize;

	private String multiCurrentNodeBgcolor;
	private String multiCurrentNodeFontColor;
	private int multiCurrentNodeFontSize;
	private String multiCurrentNodeBorderColor;

	private String connectionColor;
	private String connectionFontColor;
	private int connectionFontSize;
	private String nodeBgcolor;
	private String nodeFontColor;
	private int nodeFontSize;
	private String nodeBorderColor;

	private String currentNodeBgcolor;
	private String currentNodeFontColor;
	private int currentNodeFontSize;
	private String currentNodeBorderColor;

	private ProcessService processService;
	private TaskService taskService;
	private HistoryService historyService;
	private List<TaskDiagramInfoProvider> providers;
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method=retriveMethod(req);
		if(method!=null){
			invokeMethod(method, req, resp);
		}else{
			VelocityContext context = new VelocityContext();
			context.put("contextPath", req.getContextPath());
			String taskId=req.getParameter("taskId");
			String processKey = req.getParameter("processKey");
			String processInstanceId = req.getParameter("processInstanceId");
			String processId = req.getParameter("processId");
			StringBuffer sb=new StringBuffer();
			sb.append("{");
			if(StringUtils.isNotBlank(taskId)){
				sb.append("taskId:"+taskId);
			}
			if(StringUtils.isNotBlank(processKey)){
				if(sb.length()>1){
					sb.append(",");
				}
				sb.append("processKey:'"+processKey+"'");
			}
			if(StringUtils.isNotBlank(processInstanceId)){
				if(sb.length()>1){
					sb.append(",");
				}
				sb.append("processInstanceId:"+processInstanceId+"");
			}
			if(StringUtils.isNotBlank(processId)){
				if(sb.length()>1){
					sb.append(",");
				}
				sb.append("processId:"+processId+"");
			}
			sb.append("}");
			if(sb.length()<5){
				throw new ServletException("Show process diagram need one parameter:taskId or processKey or processInstanceId or processId");
			}
			context.put("parameters",sb.toString());
			resp.setContentType("text/html");
			resp.setCharacterEncoding("utf-8");
			Template template=ve.getTemplate("html/diagram.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}

	public void loadDiagramData(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		ProcessDiagram diagram = null;
		String processKey = req.getParameter("processKey");
		Long taskId = parseLong(req.getParameter("taskId"));
		Long processInstanceId = parseLong(req.getParameter("processInstanceId"));
		Long processId = parseLong(req.getParameter("processId"));
		if (taskId != null) {
			Task task = taskService.getTask(taskId);
			if (task == null) {
				HistoryTask historyTask = historyService.getHistoryTask(taskId);
				if (historyTask != null) {
					processInstanceId = historyTask.getProcessInstanceId();
					diagram = buildDiagramByProcessId(historyTask.getProcessId());
					try {
						diagram = (ProcessDiagram)diagram.clone();
						resetProcessDiagramDefaultStyle(diagram);
					} catch (CloneNotSupportedException e) {
						throw new ServletException(e);
					}
					rebuildProcessDiagram(diagram, processInstanceId);
				} else {
					throw new IllegalArgumentException("Task " + taskId + " is not exist!");
				}
			} else {
				processInstanceId = task.getProcessInstanceId();
				diagram = buildDiagramByTaskId(task);
			}
		} else if (processInstanceId != null) {
			diagram = buildDiagramByProcessInstanceId(processInstanceId);
		} else if (processId != null) {
			diagram = buildDiagramByProcessId(processId);
		} else if (StringUtils.isNotEmpty(processKey)) {
			diagram = buildDiagramByProcessKey(processKey);
		}
		if (diagram == null) {
			throw new IllegalArgumentException("There is not enough information to load process diagram!");
		}
		if (processInstanceId != null && processInstanceId != 0l) {
			ProcessInstance pi = processService.getProcessInstanceById(Long.valueOf(processInstanceId));
			HistoryProcessInstance hpi = null;
			if(pi==null){
				hpi=historyService.getHistoryProcessInstance(processInstanceId);
			}
			Long rootProcessInstanceId = null;
			if (pi != null) {
				rootProcessInstanceId = pi.getRootId();
			} else if (hpi != null) {
				rootProcessInstanceId = hpi.getProcessInstanceId();
			} else {
				throw new IllegalArgumentException("ProcessInstance ["+processInstanceId+"] not exist.");
			}
			ProcessDefinition pd = null;
			if(pi!=null){
				pd=processService.getProcessById(pi.getProcessId());
			}
			if(hpi!=null){
				pd=processService.getProcessById(hpi.getProcessId());
			}
			List<HistoryTask> historyTasks=queryHistoryTasks(rootProcessInstanceId);
			
			List<NodeDiagram> nodeDiagrams = diagram.getNodeDiagrams();
			for (NodeDiagram nodeDiagram : nodeDiagrams) {
				Node node = pd.getNode(nodeDiagram.getName());
				if (!(node instanceof TaskNode) && !(node instanceof StartNode)) {
					continue;
				}
				TaskQuery query = taskService.createTaskQuery();
				query.rootProcessInstanceId(rootProcessInstanceId);
				query.nodeName(node.getName());
				query.addTaskState(TaskState.Created);
				query.addTaskState(TaskState.InProgress);
				query.addTaskState(TaskState.Ready);
				query.addTaskState(TaskState.Suspended);
				query.addTaskState(TaskState.Reserved);
				List<Task> tasks = query.list();
				StringBuffer sb = null;
				for (TaskDiagramInfoProvider provider : providers) {
					String info = null;
					List<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
					if (tasks.size() > 0) {
						taskInfoList.addAll(buildTaskInfos(tasks));
					}
					List<HistoryTask> hisTasks = filterHistoryTasks(historyTasks,node.getName());
					if (hisTasks.size() > 0) {
						taskInfoList.addAll(buildHistoryTaskInfos(hisTasks));
					}
					info = provider.getInfo(node.getName(), taskInfoList);
					if (StringUtils.isNotEmpty(info)) {
						if (sb == null) {
							sb = new StringBuffer();
						}
						sb.append(info);
					}
				}
				if (sb != null) {
					nodeDiagram.setInfo(sb.toString());
				}
			}
		}
		diagram.setShowTime(showTime);
		writeObjectToJson(res, diagram);
	}
	
	private List<HistoryTask> filterHistoryTasks(List<HistoryTask> historyTasks,String nodeName){
		List<HistoryTask> result=new ArrayList<HistoryTask>();
		for(HistoryTask hisTask:historyTasks){
			if(hisTask.getNodeName().equals(nodeName)){
				result.add(hisTask);
			}
		}
		return result;
	}
	
	private List<HistoryTask> queryHistoryTasks(long rootProcessInstanceId){
		HistoryTaskQuery historyTaskQuery = historyService.createHistoryTaskQuery();
		historyTaskQuery.rootProcessInstanceId(rootProcessInstanceId);
		List<HistoryTask> historyTasks = historyTaskQuery.list();
		return historyTasks;
	}

	private List<TaskInfo> buildTaskInfos(List<Task> tasks) {
		List<TaskInfo> infos = new ArrayList<TaskInfo>();
		for (Task task : tasks) {
			TaskInfo info = new TaskInfo();
			info.setAssignee(task.getAssignee());
			info.setBusinessId(task.getBusinessId());
			info.setCreateDate(task.getCreateDate());
			info.setDescription(task.getDescription());
			info.setDuedate(task.getDuedate());
			info.setOpinion(task.getOpinion());
			info.setOwner(task.getOwner());
			info.setProcessId(task.getProcessId());
			info.setProcessInstanceId(task.getProcessInstanceId());
			info.setState(task.getState());
			info.setTaskId(task.getId());
			info.setTaskName(task.getTaskName());
			info.setTaskId(task.getId());
			info.setType(task.getType());
			info.setUrl(task.getUrl());
			infos.add(info);
		}
		return infos;
	}

	private List<TaskInfo> buildHistoryTaskInfos(List<HistoryTask> tasks) {
		List<TaskInfo> infos = new ArrayList<TaskInfo>();
		for (HistoryTask task : tasks) {
			TaskInfo info = new TaskInfo();
			info.setAssignee(task.getAssignee());
			info.setBusinessId(task.getBusinessId());
			info.setCreateDate(task.getCreateDate());
			info.setDescription(task.getDescription());
			info.setDuedate(task.getDuedate());
			info.setEndDate(task.getEndDate());
			info.setOpinion(task.getOpinion());
			info.setOwner(task.getOwner());
			info.setProcessId(task.getProcessId());
			info.setProcessInstanceId(task.getProcessInstanceId());
			info.setState(task.getState());
			info.setTaskId(task.getId());
			info.setTaskName(task.getTaskName());
			info.setTaskId(task.getId());
			info.setType(task.getType());
			info.setUrl(task.getUrl());
			infos.add(info);
		}
		return infos;
	}

	private ProcessDiagram buildDiagramByTaskId(Task task) {
		ProcessInstance pi = processService.getProcessInstanceById(task.getProcessInstanceId());
		ProcessDefinition pd = processService.getProcessById(task.getProcessId());
		ProcessDiagram diagram = pd.getDiagram();
		try {
			diagram = (ProcessDiagram)diagram.clone();
			resetProcessDiagramDefaultStyle(diagram);
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		rebuildProcessDiagram(diagram, pi.getRootId());
		for (NodeDiagram nodeDiagram : diagram.getNodeDiagrams()) {
			if (nodeDiagram.getName().equals(task.getNodeName())) {
				nodeDiagram.setBackgroundColor(multiCurrentNodeBgcolor);
				nodeDiagram.setBorderColor(multiCurrentNodeBorderColor);
				nodeDiagram.setFontColor(multiCurrentNodeFontColor);
				nodeDiagram.setFontSize(multiCurrentNodeFontSize);
				nodeDiagram.setFontBold(true);
				break;
			}
		}
		return diagram;
	}

	private ProcessDiagram buildDiagramByProcessInstanceId(Long processInstanceId) {
		ProcessInstance pi = processService.getProcessInstanceById(processInstanceId);
		long processId=0,rootId=0;
		if (pi != null) {
			processId=pi.getProcessId();
			rootId=pi.getRootId();
		}else{
			HistoryProcessInstance hpi=historyService.getHistoryProcessInstance(processInstanceId); 
			if(hpi!=null){
				processId=hpi.getProcessId();
				rootId=hpi.getProcessInstanceId();
			}else{
				throw new IllegalArgumentException("ProcessInstance " + processInstanceId + " is not exist!");				
			}
		}
		ProcessDefinition pd = processService.getProcessById(processId);
		ProcessDiagram diagram = pd.getDiagram();
		try {
			diagram = (ProcessDiagram)diagram.clone();
			resetProcessDiagramDefaultStyle(diagram);
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		rebuildProcessDiagram(diagram,rootId);
		return diagram;
	}

	private ProcessDiagram buildDiagramByProcessId(Long processId) {
		ProcessDefinition pd = processService.getProcessById(processId);
		ProcessDiagram diagram = pd.getDiagram();
		resetProcessDiagramDefaultStyle(diagram);
		return diagram;
	}

	private ProcessDiagram buildDiagramByProcessKey(String processKey) {
		ProcessDefinition pd = processService.getProcessByKey(processKey);
		ProcessDiagram diagram = pd.getDiagram();
		resetProcessDiagramDefaultStyle(diagram);
		return diagram;
	}

	private void rebuildProcessDiagram(ProcessDiagram diagram, long processInstanceId) {
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		Map<String, HistoryActivity> map = new HashMap<String, HistoryActivity>();
		List<HistoryActivity> activities = historyService.getHistoryActivitysByProcesssInstanceId(processInstanceId);
		for (HistoryActivity hisActivity : activities) {
			String nodeName = hisActivity.getNodeName();
			if (countMap.containsKey(nodeName)) {
				int count = countMap.get(nodeName);
				count++;
				countMap.put(nodeName, count);
			} else {
				countMap.put(nodeName, 1);
			}
		}
		for (NodeDiagram d : diagram.getNodeDiagrams()) {
			String nodeName = d.getName();
			HistoryActivity activity = getHistoryActivity(activities, nodeName);
			if (activity == null) {
				continue;
			}
			map.put(nodeName, activity);
			if (countMap.containsKey(nodeName)) {
				int count = countMap.get(nodeName);
				d.setTime(count);
			}
			String icon = d.getIcon();
			String resultIcon = null;
			if (activity.getEndDate() == null) {
				resultIcon = icon.substring(0, icon.lastIndexOf("/")) + "/current" + icon.substring(icon.lastIndexOf("/"));
				d.setFontColor(currentNodeFontColor);
				d.setBackgroundColor(currentNodeBgcolor);
				d.setBorderColor(currentNodeBorderColor);
				d.setFontColor(currentNodeFontColor);
				d.setFontSize(currentNodeFontSize);
			} else {
				resultIcon = icon.substring(0, icon.lastIndexOf("/")) + "/passed" + icon.substring(icon.lastIndexOf("/"));
				d.setFontColor(passedNodeFontColor);
				d.setBorderColor(passedNodeBorderColor);
				d.setBackgroundColor(passedNodeBgcolor);
				d.setFontSize(passedNodeFontSize);
			}
			d.setIcon(resultIcon);
		}

		for (NodeDiagram d : diagram.getNodeDiagrams()) {
			String sourceName = d.getName();
			HistoryActivity source = map.get(sourceName);
			if (source == null || d.getSequenceFlowDiagrams() == null) {
				continue;
			}
			for (SequenceFlowDiagram flowDiagram : d.getSequenceFlowDiagrams()) {
				HistoryActivity target = map.get(flowDiagram.getTo());
				if (target == null) {
					continue;
				}
				/*
				 * String leaveFlowName=source.getLeaveFlowName(); if(leaveFlowName==null ||
				 * leaveFlowName.equals(flowDiagram.getName())){ flowDiagram.setBorderColor("200,200,200");
				 * flowDiagram.setFontColor("150,150,150"); }
				 */
				flowDiagram.setBorderColor(passedConnectionColor);
				flowDiagram.setFontColor(passedConnectionFontColor);
				flowDiagram.setFontSize(passedConnectionFontSize);
			}
		}
	}

	private void resetProcessDiagramDefaultStyle(ProcessDiagram diagram) {
		for (NodeDiagram d : diagram.getNodeDiagrams()) {
			d.setBackgroundColor(nodeBgcolor);
			d.setBorderColor(nodeBorderColor);
			d.setFontColor(nodeFontColor);
			d.setFontSize(nodeFontSize);
			List<SequenceFlowDiagram> list = d.getSequenceFlowDiagrams();
			if (list == null) {
				continue;
			}
			for (SequenceFlowDiagram flow : list) {
				flow.setBorderColor(connectionColor);
				flow.setFontColor(connectionFontColor);
				flow.setFontSize(connectionFontSize);
			}
		}
	}

	private HistoryActivity getHistoryActivity(List<HistoryActivity> activities, String nodeName) {
		List<HistoryActivity> result = new ArrayList<HistoryActivity>();
		for (HistoryActivity activity : activities) {
			if (activity.getNodeName().equals(nodeName)) {
				result.add(activity);
			}
		}
		HistoryActivity target = null;
		for (HistoryActivity ac : result) {
			if (ac.getEndDate() == null) {
				target = ac;
				break;
			}
		}
		if (target == null && result.size() > 0) {
			return result.get(0);
		} else {
			return target;
		}
	}

	public void afterPropertiesSet() throws Exception {
		providers = new ArrayList<TaskDiagramInfoProvider>();
		Collection<TaskDiagramInfoProvider> parentProviders = applicationContext.getBeansOfType(TaskDiagramInfoProvider.class).values();
		for (TaskDiagramInfoProvider provider : parentProviders) {
			if (!provider.disable()) {
				providers.add(provider);
			}
		}
	}

	private static Long parseLong(Object obj) {
		Long val;
		if (obj == null) {
			val = null;
		} else if (obj instanceof Number) {
			val = ((Number) obj).longValue();
		} else {
			try {
				val = Long.valueOf(obj.toString());
			} catch (NumberFormatException e) {
				val = null;
			}
		}
		return val;
	}

	public void setProcessService(ProcessService processService) {
		this.processService = processService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public void setShowTime(boolean showTime) {
		this.showTime = showTime;
	}

	public void setPassedNodeBgcolor(String passedNodeBgcolor) {
		this.passedNodeBgcolor = passedNodeBgcolor;
	}

	public void setPassedNodeFontColor(String passedNodeFontColor) {
		this.passedNodeFontColor = passedNodeFontColor;
	}

	public void setPassedNodeFontSize(int passedNodeFontSize) {
		this.passedNodeFontSize = passedNodeFontSize;
	}

	public void setPassedNodeBorderColor(String passedNodeBorderColor) {
		this.passedNodeBorderColor = passedNodeBorderColor;
	}

	public void setPassedConnectionColor(String passedConnectionColor) {
		this.passedConnectionColor = passedConnectionColor;
	}

	public void setPassedConnectionFontColor(String passedConnectionFontColor) {
		this.passedConnectionFontColor = passedConnectionFontColor;
	}

	public void setConnectionColor(String connectionColor) {
		this.connectionColor = connectionColor;
	}

	public void setConnectionFontColor(String connectionFontColor) {
		this.connectionFontColor = connectionFontColor;
	}

	public void setNodeBgcolor(String nodeBgcolor) {
		this.nodeBgcolor = nodeBgcolor;
	}

	public void setNodeFontColor(String nodeFontColor) {
		this.nodeFontColor = nodeFontColor;
	}

	public void setNodeFontSize(int nodeFontSize) {
		this.nodeFontSize = nodeFontSize;
	}

	public void setNodeBorderColor(String nodeBorderColor) {
		this.nodeBorderColor = nodeBorderColor;
	}

	public void setCurrentNodeBgcolor(String currentNodeBgcolor) {
		this.currentNodeBgcolor = currentNodeBgcolor;
	}

	public void setCurrentNodeFontColor(String currentNodeFontColor) {
		this.currentNodeFontColor = currentNodeFontColor;
	}

	public void setCurrentNodeFontSize(int currentNodeFontSize) {
		this.currentNodeFontSize = currentNodeFontSize;
	}

	public void setCurrentNodeBorderColor(String currentNodeBorderColor) {
		this.currentNodeBorderColor = currentNodeBorderColor;
	}

	public void setConnectionFontSize(int connectionFontSize) {
		this.connectionFontSize = connectionFontSize;
	}

	public void setPassedConnectionFontSize(int passedConnectionFontSize) {
		this.passedConnectionFontSize = passedConnectionFontSize;
	}

	public void setMultiCurrentNodeBgcolor(String multiCurrentNodeBgcolor) {
		this.multiCurrentNodeBgcolor = multiCurrentNodeBgcolor;
	}

	public void setMultiCurrentNodeBorderColor(String multiCurrentNodeBorderColor) {
		this.multiCurrentNodeBorderColor = multiCurrentNodeBorderColor;
	}

	public void setMultiCurrentNodeFontColor(String multiCurrentNodeFontColor) {
		this.multiCurrentNodeFontColor = multiCurrentNodeFontColor;
	}

	public void setMultiCurrentNodeFontSize(int multiCurrentNodeFontSize) {
		this.multiCurrentNodeFontSize = multiCurrentNodeFontSize;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		super.setApplicationContext(applicationContext);
		Collection<TaskDiagramInfoProvider> coll=applicationContext.getBeansOfType(TaskDiagramInfoProvider.class).values();
		providers=new ArrayList<TaskDiagramInfoProvider>();
		for(TaskDiagramInfoProvider p:coll){
			if(p.disable()){
				continue;
			}
			providers.add(p);
		}
	}
	
	@Override
	public String url() {
		return "/diagram";
	}
}
