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
package com.bstek.uflo.model.variable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.bstek.uflo.env.Context;

/**
 * @author Jacky.gao
 * @since 2013年8月1日
 */
@Entity
@Table(name="UFLO_VARIABLE")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE_",length=30)
@DiscriminatorValue("")
public abstract class Variable {
	@Id
	@Column(name="ID_")
	private long id;

	@Column(name="KEY_",length=60)
	private String key;
	
	@Column(name="PROCESS_INSTANCE_ID_")
	private long processInstanceId;
	
	@Column(name="ROOT_PROCESS_INSTANCE_ID_")
	private long rootProcessInstanceId;
	
	public static Variable newVariable(Object value,Context context){
		Variable variable=null;
		if(value instanceof Date){
			variable=new DateVariable((Date)value);
		}else if(value instanceof String){
			String str=(String)value;
			if(str.length()>255){
				variable=new TextVariable(str,context);
			}else{
				variable=new StringVariable((String)value);				
			}
		}else if(value instanceof Double){
			variable=new DoubleVariable((Double)value);
		}else if(value instanceof Float){
			variable=new FloatVariable((Float)value);			
		}else if(value instanceof Long){
			variable=new LongVariable((Long)value);
		}else if(value instanceof Integer){
			variable=new IntegerVariable((Integer)value);
		}else if(value instanceof Boolean){
			variable=new BooleanVariable((Boolean)value);			
		}else if(value instanceof Short){
			variable=new ShortVariable((Short)value);			
		}else if(value instanceof Byte){
			variable=new ByteVariable((Byte)value);			
		}else if(value instanceof Character){
			variable=new CharacterVariable((Character)value);			
		}else{
			if(!(value instanceof java.io.Serializable)){
				throw new IllegalArgumentException("Variable value ["+value.getClass().getName()+"] must implement the java.io.Serializable interface");
			}
			variable=new BlobVariable(value,context);
		}
		return variable;
	}
	
	public abstract Object getValue();
	
	public abstract VariableType getType();
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public long getRootProcessInstanceId() {
		return rootProcessInstanceId;
	}

	public void setRootProcessInstanceId(long rootProcessInstanceId) {
		this.rootProcessInstanceId = rootProcessInstanceId;
	}
}
