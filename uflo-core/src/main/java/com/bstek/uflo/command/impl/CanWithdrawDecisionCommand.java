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
package com.bstek.uflo.command.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.command.impl.jump.JumpNode;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.process.node.Node;
import com.bstek.uflo.process.node.StartNode;
import com.bstek.uflo.process.node.TaskNode;

/**
 * @author Jacky.gao
 * @since 2013年9月24日
 */
public class CanWithdrawDecisionCommand implements Command<Boolean> {
	public Task task;
	public CanWithdrawDecisionCommand(Task task){
		this.task=task;
	}
	public Boolean execute(Context context) {
		ProcessDefinition pd=context.getProcessService().getProcessById(task.getProcessId());
		String prevTaskName=task.getPrevTask();
		if(StringUtils.isEmpty(prevTaskName)){
			return false;
		}
		if(prevTaskName.equals(task.getNodeName())){
			return false;
		}
		List<JumpNode> nodes=context.getTaskService().getAvaliableRollbackTaskNodes(task);
		boolean canJump=false;
		for(JumpNode jumpNode:nodes){
			if(jumpNode.getName().equals(prevTaskName)){
				canJump=true;
				break;
			}
		}
		if(!canJump){
			return false;
		}
		
		Node currentNode=pd.getNode(task.getNodeName());
		Node node=pd.getNode(prevTaskName);
		if(node!=null && (node instanceof TaskNode || node instanceof StartNode)){
			if(node instanceof StartNode && currentNode instanceof StartNode){
				return false;
			}else{
				return true;				
			}
		}else{
			return false;
		}
	}
}
