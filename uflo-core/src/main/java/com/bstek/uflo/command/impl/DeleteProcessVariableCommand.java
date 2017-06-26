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

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.variable.Variable;

/**
 * @author Jacky.gao
 * @since 2013年10月11日
 */
public class DeleteProcessVariableCommand implements Command<Object> {
	private String key;
	private long processInstanceId;
	public DeleteProcessVariableCommand(String key,long processInstanceId){
		this.key=key;
		this.processInstanceId=processInstanceId;
	}
	public Object execute(Context context) {
		String hql="delete "+Variable.class.getName()+" where processInstanceId=:pid and key=:key";
		context.getSession().createQuery(hql).setLong("pid", processInstanceId).setString("key",key).executeUpdate();
		context.getExpressionContext().removeContextVariables(processInstanceId, key);
		return null;
	}
}
