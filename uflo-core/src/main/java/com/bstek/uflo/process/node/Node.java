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
package com.bstek.uflo.process.node;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.uflo.command.impl.SaveHistoryActivityCommand;
import com.bstek.uflo.diagram.NodeDiagram;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.HistoryActivity;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.flow.SequenceFlowImpl;
import com.bstek.uflo.process.handler.NodeEventHandler;

/**
 * @author Jacky.gao
 * @since 2013年8月7日
 */
public abstract class Node implements java.io.Serializable{
	private static final long serialVersionUID = -7347322856264296067L;
	private String name;
	private String label;
	private String description;
	private long processId;
	private NodeDiagram diagram;
	private List<SequenceFlowImpl> sequenceFlows;
	private String eventHandlerBean;
	
	/**
	 * 进入当前节点的需要做的工作
	 * @param context 上下文对象
	 * @param processInstance 当前节点所在的流程实例对象
	 * @return 返回是否执行当前节点的leave动作
	 */
	public abstract boolean enter(Context context,ProcessInstance processInstance);

	/**
	 * 取消当前节点的需要做的工作
	 * @param context 上下文对象
	 * @param processInstance 当前节点所在的流程实例对象
	 */
	public abstract void cancel(Context context,ProcessInstance processInstance);
	

	/**
	 * 离开当前节点的需要做的工作
	 * @param context 上下文对象
	 * @param processInstance 当前节点所在的流程实例对象
	 * @param flowName 要沿着哪条sequenceFlow离开
	 * @return 返回离开当前节点的sequenceFlow的name
	 */
	public abstract String leave(Context context,ProcessInstance processInstance,String flowName);
	
	private HistoryActivity saveActivityHistory(Context context,ProcessInstance processInstance,boolean isEnd,String leaveFlowName){
		return context.getCommandService().executeCommand(new SaveHistoryActivityCommand(processInstance, this,isEnd,leaveFlowName));
	}
	
	public HistoryActivity createActivityHistory(Context context,ProcessInstance processInstance){
		return saveActivityHistory(context,processInstance,false,null);
	}
	public HistoryActivity completeActivityHistory(Context context,ProcessInstance processInstance,String flowName){
		return saveActivityHistory(context,processInstance,true,flowName);
	}
	
	protected String leaveNode(Context context,ProcessInstance processInstance,String flowName){
		if(StringUtils.isNotEmpty(flowName)){
			SequenceFlowImpl flow=getFlow(flowName);
			if(flow==null){
				throw new IllegalArgumentException("Sequence flow ["+flowName+"] not exist!");
			}
			flow.execute(context, processInstance);
			return flow.getName();
		}
		for(SequenceFlowImpl flow:sequenceFlows){
			if(flow.canExecute(context, processInstance)){
				flow.execute(context, processInstance);
				return flow.getName();
			}
		}
		throw new IllegalArgumentException("All the sequence flow can not be perform!");
	}
	
	private void executeEventHandler(Context context,ProcessInstance processInstance,boolean enter){
		if(StringUtils.isNotEmpty(eventHandlerBean)){
			NodeEventHandler handler=(NodeEventHandler)context.getApplicationContext().getBean(eventHandlerBean);
			if(enter){
				handler.enter(this, processInstance, context);
			}else{
				handler.leave(this, processInstance, context);				
			}
		}
	}
	
	public void doEnterEventHandler(Context context,ProcessInstance processInstance){
		executeEventHandler(context, processInstance, true);
	}
	public void doLeaveEventHandler(Context context,ProcessInstance processInstance){
		executeEventHandler(context, processInstance, false);
	}
	
	protected SequenceFlowImpl getFlow(String flowName){
		SequenceFlowImpl flow=null;
		for(SequenceFlowImpl f:getSequenceFlows()){
			String name=f.getName();
			if(flowName.equals(name)){
				flow=f;
				break;
			}
		}
		return flow;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public NodeDiagram getDiagram() {
		return diagram;
	}

	public void setDiagram(NodeDiagram diagram) {
		this.diagram = diagram;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<SequenceFlowImpl> getSequenceFlows() {
		return sequenceFlows;
	}

	public void setSequenceFlows(List<SequenceFlowImpl> sequenceFlows) {
		this.sequenceFlows = sequenceFlows;
	}

	public long getProcessId() {
		return processId;
	}

	public void setProcessId(long processId) {
		this.processId = processId;
	}

	public String getEventHandlerBean() {
		return eventHandlerBean;
	}

	public void setEventHandlerBean(String eventHandlerBean) {
		this.eventHandlerBean = eventHandlerBean;
	}
}
