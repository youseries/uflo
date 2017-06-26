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

import org.hibernate.Session;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.Blob;
import com.bstek.uflo.model.HistoryActivity;
import com.bstek.uflo.model.HistoryProcessInstance;
import com.bstek.uflo.model.HistoryTask;
import com.bstek.uflo.model.HistoryVariable;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.variable.Variable;
import com.bstek.uflo.query.ProcessInstanceQuery;
import com.bstek.uflo.service.ProcessService;

/**
 * @author Jacky.gao
 * @since 2013年9月9日
 */
public class DeleteProcessDefinitionCommand implements Command<Object> {
	private ProcessDefinition processDefinition;
	public DeleteProcessDefinitionCommand(ProcessDefinition processDefinition){
		this.processDefinition=processDefinition;
	}
	@SuppressWarnings("unchecked")
	public Object execute(Context context) {
		ProcessService processService=context.getProcessService();
		ProcessInstanceQuery query=context.getProcessService().createProcessInstanceQuery();
		query.processId(processDefinition.getId());
		Session session=context.getSession();
		for(ProcessInstance pi:query.list()){
			processService.deleteProcessInstance(pi);
			session.createQuery("delete "+Variable.class.getName()+" where processInstanceId=:processInstanceId").setLong("processInstanceId", pi.getId()).executeUpdate();
		}
		
		List<HistoryProcessInstance> hisInstances=session.createQuery("from "+HistoryProcessInstance.class.getName()+" where processId=:processId").setLong("processId", processDefinition.getId()).list();
		for(HistoryProcessInstance instance:hisInstances){
			session.createQuery("delete "+HistoryVariable.class.getName()+" where historyProcessInstanceId=:historyProcessInstanceId").setLong("historyProcessInstanceId", instance.getId()).executeUpdate();
		}
		
		session.createQuery("delete "+Blob.class.getName()+" where processId=:processId").setLong("processId", processDefinition.getId()).executeUpdate();
		session.createQuery("delete "+HistoryProcessInstance.class.getName()+" where processId=:processId").setLong("processId", processDefinition.getId()).executeUpdate();
		session.createQuery("delete "+HistoryTask.class.getName()+" where processId=:processId").setLong("processId", processDefinition.getId()).executeUpdate();
		session.createQuery("delete "+HistoryActivity.class.getName()+" where processId=:processId").setLong("processId", processDefinition.getId()).executeUpdate();
		session.delete(processDefinition);
		return null;
	}
}
