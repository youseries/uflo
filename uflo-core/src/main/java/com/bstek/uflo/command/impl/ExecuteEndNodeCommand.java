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

import org.hibernate.Session;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.ProcessInstanceState;
import com.bstek.uflo.process.node.EndNode;

/**
 * @author Jacky.gao
 * @since 2013年7月26日
 */
public class ExecuteEndNodeCommand implements Command<Object> {
	private EndNode endNode;
	private ProcessInstance processInstance;
	public ExecuteEndNodeCommand(EndNode endNode,ProcessInstance processInstance){
		this.endNode=endNode;
		this.processInstance=processInstance;
	}
	public Object execute(Context context) {
		Session session=context.getSession();
		processInstance.setState(ProcessInstanceState.End);
		session.delete(processInstance);
		if(endNode.isTerminate()){
			removeProcessInstances(processInstance,session);
		}
		return null;
	}

	private void removeProcessInstances(ProcessInstance pi,Session session){
		long pid=pi.getParentId();
		if(pid>0){
			ProcessInstance parent=(ProcessInstance)session.get(ProcessInstance.class, pid);
			parent.setState(ProcessInstanceState.End);
			session.delete(parent);
			removeProcessInstances(parent,session);
		}
	}
}
