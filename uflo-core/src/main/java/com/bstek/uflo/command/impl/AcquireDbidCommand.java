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

import org.hibernate.LockMode;
import org.hibernate.Session;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ContextProperty;

/**
 * @author Jacky.gao
 * @since 2013年7月30日
 */
public class AcquireDbidCommand implements Command<Long>{
	private static final String ID_KEY="dbid";
	private int blockSize;
	public AcquireDbidCommand(int blockSize){
		this.blockSize=blockSize;
	}
	@SuppressWarnings("unchecked")
	public Long execute(Context context) {
		long nextId=0;
		Session session=context.getSession();
		List<ContextProperty> list=session.createQuery("from "+ContextProperty.class.getName()+" as p where p.key=:key").setString("key", ID_KEY).setLockMode("p", LockMode.PESSIMISTIC_WRITE).list();
		if(list.size()>0){
			ContextProperty prop=list.get(0);
			nextId=Long.valueOf(prop.getValue());
			prop.setValue(String.valueOf(nextId+blockSize));
			session.update(prop);
		}else{
			ContextProperty prop=new ContextProperty();
			prop.setKey(ID_KEY);
			prop.setValue(String.valueOf(blockSize));
			session.save(prop);
		}
		return nextId+1;
	}
}
