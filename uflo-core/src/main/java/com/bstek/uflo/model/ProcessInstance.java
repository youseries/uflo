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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Jacky.gao
 * @since 2013年7月25日
 */
@XmlRootElement(name="ProcessInstance")
@Entity
@Table(name="UFLO_PROCESS_INSTANCE")
public class ProcessInstance{
	@Id
	@Column(name="ID_")
	private long id;
	
	@Column(name="PROCESS_ID_")
	private long processId;
	
	@Column(name="PARENT_ID_")
	private long parentId;
	
	@Column(name="ROOT_ID_")
	private long rootId;
	
	@Column(name="HIS_PROCESS_INSTANCE_ID_")
	private long historyProcessInstanceId;
	
	@Column(name="CREATE_DATE_")
	private Date createDate;

	@Enumerated(EnumType.STRING)
	@Column(name="STATE_",length=20)
	private ProcessInstanceState state;
	
	@Column(name="CURRENT_NODE_",length=60)
	private String currentNode;
	
	@Column(name="CURRENT_TASK_",length=60)
	private String currentTask;
	
	@Column(name="PARALLEL_INSTANCE_COUNT_")
	private int parallelInstanceCount;
	
	@Column(name="PROMOTER_",length=60)
	private String promoter;
	
	@Column(name="BUSINESS_ID_",length=60)
	private String businessId;
	
	@Column(name="SUBJECT_",length=200)
	private String subject;
	
	@Column(name="TAG_",length=30)
	private String tag;
	
	@Transient
	private Map<String,Object> metadata=new HashMap<String,Object>();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getProcessId() {
		return processId;
	}
	public void setProcessId(long processId) {
		this.processId = processId;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	
	public String getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}
	public long getRootId() {
		return rootId;
	}
	public void setRootId(long rootId) {
		this.rootId = rootId;
	}
	public long getHistoryProcessInstanceId() {
		return historyProcessInstanceId;
	}
	public void setHistoryProcessInstanceId(long historyProcessInstanceId) {
		this.historyProcessInstanceId = historyProcessInstanceId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public ProcessInstanceState getState() {
		return state;
	}
	public void setState(ProcessInstanceState state) {
		this.state = state;
	}
	public String getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(String currentNode) {
		this.currentNode = currentNode;
	}
	public int getParallelInstanceCount() {
		return parallelInstanceCount;
	}
	public void setParallelInstanceCount(int parallelInstanceCount) {
		this.parallelInstanceCount = parallelInstanceCount;
	}
	public String getPromoter() {
		return promoter;
	}
	public void setPromoter(String promoter) {
		this.promoter = promoter;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public void addMetadata(String key,Object value) {
		metadata.put(key, value);
	}
	public Object getMetadata(String key) {
		return metadata.get(key);
	}
}
