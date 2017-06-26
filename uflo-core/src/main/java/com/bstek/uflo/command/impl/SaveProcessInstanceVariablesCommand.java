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
import java.util.Map;

import org.hibernate.Session;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.variable.BlobVariable;
import com.bstek.uflo.model.variable.TextVariable;
import com.bstek.uflo.model.variable.Variable;
import com.bstek.uflo.query.ProcessVariableQuery;
import com.bstek.uflo.query.impl.ProcessVariableQueryImpl;
import com.bstek.uflo.utils.IDGenerator;

/**
 * @author Jacky.gao
 * @since 2013年7月31日
 */
public class SaveProcessInstanceVariablesCommand implements Command<Object> {
	private Map<String,Object> variables;
	private ProcessInstance processInstance;
	public SaveProcessInstanceVariablesCommand(ProcessInstance processInstance,Map<String,Object> variables){
		this.processInstance=processInstance;
		this.variables=variables;
	}
	public Object execute(Context context) {
		Session session=context.getSession();
		for(String key:variables.keySet()){
			ProcessVariableQuery query=new ProcessVariableQueryImpl(context.getCommandService());
			query.processInstanceId(processInstance.getId());
			query.key(key);
			List<Variable> oldVars=query.list();
			for(Variable var:oldVars){
				session.delete(var);
				if(var instanceof TextVariable){
					session.delete(((TextVariable)var).getBlob());
				}
				if(var instanceof BlobVariable){
					session.delete(((BlobVariable)var).getBlob());
				}
			}
			Object obj=variables.get(key);
			if(obj==null){
				throw new IllegalArgumentException("Variable ["+key+"] value can not be null.");
			}
			Variable var=Variable.newVariable(obj, context);
			var.setId(IDGenerator.getInstance().nextId());
			var.setKey(key);
			var.setProcessInstanceId(processInstance.getId());
			var.setRootProcessInstanceId(processInstance.getRootId());
			context.getExpressionContext().addContextVariables(processInstance, variables);
			session.save(var);
		}
		return null;
	}

}
	
