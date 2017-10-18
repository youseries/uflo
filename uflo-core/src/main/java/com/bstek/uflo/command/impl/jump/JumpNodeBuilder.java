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
package com.bstek.uflo.command.impl.jump;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.process.flow.SequenceFlow;
import com.bstek.uflo.process.flow.SequenceFlowImpl;
import com.bstek.uflo.process.node.DecisionNode;
import com.bstek.uflo.process.node.ForeachNode;
import com.bstek.uflo.process.node.ForkNode;
import com.bstek.uflo.process.node.JoinNode;
import com.bstek.uflo.process.node.Node;
import com.bstek.uflo.process.node.StartNode;
import com.bstek.uflo.process.node.TaskNode;

/**
 * @author Jacky.gao
 * @since 2013年9月27日
 */
public class JumpNodeBuilder {
	private ProcessDefinition process;
	private List<SequenceFlow> flowStore=new ArrayList<SequenceFlow>();
	private Map<String,JumpNode> maps=new HashMap<String,JumpNode>();
	public JumpNodeBuilder(ProcessDefinition process){
		this.process=process;
	}
	public Map<String,JumpNode> buildSimulationTasks(){
		StartNode node=process.getStartNode();
		JumpNode jumpNode=new JumpNode(node.getName());
		jumpNode.setTask(true);
		jumpNode.setLevel(1);
		jumpNode.setLabel(node.getLabel());
		maps.put(jumpNode.getName(), jumpNode);
		simulation(node,jumpNode);
		return maps;
	}
	
	private void simulation(Node parentNode,JumpNode jumpNode){
		List<SequenceFlowImpl> flows=parentNode.getSequenceFlows();
		if(flows==null || flows.size()==0)return;
		for(SequenceFlow flow:flows){
			if(flowStore.contains(flow)){
				continue;
			}else{
				flowStore.add(flow);
			}
			String to=flow.getToNode();
			Node toNode=process.getNode(to);
			JumpNode nextJumpNode=new JumpNode(toNode.getName());
			nextJumpNode.setLabel(toNode.getLabel());
			if(jumpNode.getParent().size()>0){
				nextJumpNode.getParent().addAll(jumpNode.getParent());
			}
			if(parentNode instanceof ForkNode || parentNode instanceof ForeachNode){
				nextJumpNode.setLevel(jumpNode.getLevel()+1);
				nextJumpNode.addParent(parentNode.getName()+"-"+flow.getName());
			}else if(parentNode instanceof JoinNode){
				nextJumpNode.decreaseParent();
				nextJumpNode.setLevel(jumpNode.getLevel()-1);				
			}else{
				nextJumpNode.setLevel(jumpNode.getLevel());				
			}
			if(!maps.containsKey(nextJumpNode.getName()) && !(toNode instanceof ForkNode) && !(toNode instanceof JoinNode) && !(toNode instanceof ForeachNode) && !(toNode instanceof DecisionNode)){
				maps.put(nextJumpNode.getName(), nextJumpNode);
			}
			if(toNode instanceof TaskNode){
				nextJumpNode.setTask(true);
			}
			simulation(toNode,nextJumpNode);
		}
	}
}
