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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.springframework.util.SerializationUtils;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.Blob;
import com.bstek.uflo.utils.IDGenerator;

/**
 * @author Jacky.gao
 * @since 2013年9月24日
 */
@Entity
@DiscriminatorValue("Text")
public class TextVariable extends Variable {
	@Column(name="BLOB_ID_")
	private long blobId;
	
	@Transient
	private String text;
	
	@Transient
	private Blob blob;

	public TextVariable(){}
	
	public TextVariable(String value,Context context){
		Blob lob=new Blob(value);
		long id=IDGenerator.getInstance().nextId();
		lob.setId(id);
		setBlobId(id);
		Session session=context.getSession();
		session.save(lob);
	}
	
	public void initValue(Context context){
		Session session=context.getSession();
		blob=(Blob)session.get(Blob.class,blobId);
		text=(String)SerializationUtils.deserialize(blob.getBlobValue());
	}
	
	public Blob getBlob(){
		return blob;
	}
	
	@Override
	public String getValue() {
		return text;
	}

	@Override
	public VariableType getType() {
		return VariableType.Text;
	}
	
	public long getBlobId() {
		return blobId;
	}

	public void setBlobId(long blobId) {
		this.blobId = blobId;
	}
}
