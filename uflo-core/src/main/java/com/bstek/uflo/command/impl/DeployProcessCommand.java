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
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessDefinition;

/**
 * @author Jacky.gao
 * @since 2013年8月3日
 */
public class DeployProcessCommand implements Command<ProcessDefinition> {
	private ProcessDefinition process;
	private boolean update=false;
	public DeployProcessCommand(ProcessDefinition process,boolean update){
		this.process=process;
		this.update=update;
	}
	
	@SuppressWarnings("unchecked")
	public ProcessDefinition execute(Context context) {
		Session session=context.getSession();
		String key=process.getKey();
		if(!update && StringUtils.isNotEmpty(key)){
			int size=session.createCriteria(ProcessDefinition.class).add(Restrictions.eq("key", key)).list().size();
			if(size>0){
				throw new IllegalArgumentException("Process definition "+process.getName()+"'s key "+key+" is not the only one!");
			}
		}
		int newVersion=1;
		if(!update){
			List<ProcessDefinition> processes=session.createCriteria(ProcessDefinition.class).add(Restrictions.eq("name",process.getName())).addOrder(Order.desc("version")).list();
			if(processes.size()>0){
				newVersion=processes.get(0).getVersion()+1;
				process.setVersion(newVersion);
			}else{
				process.setVersion(newVersion);
			}
		}
		if(StringUtils.isEmpty(key)){
			key=process.getName()+"-"+newVersion;
			process.setKey(key);
		}
		if(update){
			session.update(process);
		}else{
			session.save(process);			
		}
		return process;
	}

}
