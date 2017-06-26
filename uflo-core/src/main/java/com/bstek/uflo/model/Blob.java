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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.util.SerializationUtils;

/**
 * @author Jacky.gao
 * @since 2013年9月27日
 */
@Entity
@Table(name="UFLO_BLOB")
public class Blob {
	@Id
	@Column(name="ID_")
	private long id;
	
	@Column(name="NAME_",length=60)
	private String name;
	
	@Column(name="PROCESS_ID_")
	private long processId;
	
	@Lob
	@Column(name="BLOB_VALUE_",length=1024000)
	private byte[] blobValue;

	public Blob(){}
	public Blob(Object obj){
		byte[] b=SerializationUtils.serialize(obj);
		setBlobValue(b);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getProcessId() {
		return processId;
	}

	public void setProcessId(long processId) {
		this.processId = processId;
	}

	public byte[] getBlobValue() {
		return blobValue;
	}

	public void setBlobValue(byte[] blobValue) {
		this.blobValue = blobValue;
	}
}
