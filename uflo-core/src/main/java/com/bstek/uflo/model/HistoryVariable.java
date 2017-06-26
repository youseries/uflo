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
package com.bstek.uflo.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.springframework.util.SerializationUtils;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.variable.VariableType;

@Entity
@Table(name="UFLO_HIS_VARIABLE")
public class HistoryVariable {
	@Id
	@Column(name="ID_")
	private long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name="TYPE_",length=15)
	private VariableType type;
	
	@Column(name="HIS_PROCESS_INSTANCE_ID_")
	private long historyProcessInstanceId;
	
	@Column(name="KEY_",length=60)
	private String key;
	
	@Column(name="VALUE_")
	private String stringValue;
	
	@Transient
	private Object variableObject;
	
	public Object getVariableObject(){
		return variableObject;
	}
	
	public void init(Context context){
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		Session session=context.getSession();
		if(type.equals(VariableType.Blob) || type.equals(VariableType.Text)){
			HistoryBlob blob=(HistoryBlob)session.get(HistoryBlob.class,Long.valueOf(stringValue));
			variableObject=SerializationUtils.deserialize(blob.getBlobValue());
		}else if(type.equals(VariableType.String)){
			variableObject=stringValue;
		}else if(type.equals(VariableType.Date)){
			try {
				variableObject=sd.parse(stringValue);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}else if(type.equals(VariableType.Integer)){
			variableObject=Integer.valueOf(stringValue);
		}else if(type.equals(VariableType.Character)){
			variableObject=Character.valueOf((char)(stringValue.getBytes()[0]));
		}else if(type.equals(VariableType.Double)){
			variableObject=Double.valueOf(stringValue);
		}else if(type.equals(VariableType.Float)){
			variableObject=Float.valueOf(stringValue);
		}else if(type.equals(VariableType.Long)){
			variableObject=Long.valueOf(stringValue);
		}else if(type.equals(VariableType.Byte)){
			variableObject=Byte.valueOf(stringValue);
		}else if(type.equals(VariableType.Short)){
			variableObject=Short.valueOf(stringValue);
		}else if(type.equals(VariableType.Boolean)){
			variableObject=Boolean.valueOf(stringValue);
		}
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getHistoryProcessInstanceId() {
		return historyProcessInstanceId;
	}
	public void setHistoryProcessInstanceId(long historyProcessInstanceId) {
		this.historyProcessInstanceId = historyProcessInstanceId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public VariableType getType() {
		return type;
	}

	public void setType(VariableType type) {
		this.type = type;
	}
}
