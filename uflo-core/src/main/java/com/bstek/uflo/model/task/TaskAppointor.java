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
package com.bstek.uflo.model.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013年8月19日
 */
@Entity
@Table(name="UFLO_TASK_APPOINTOR")
public class TaskAppointor {
	@Id
	@Column(name="ID_")
	private long id;

	@Column(name="PROCESS_INSTANCE_ID_")
	private long processInstanceId;
	
	@Column(name="TASK_NODE_NAME_",length=60)
	private String taskNodeName;

	@Column(name="OWNER_",length=60)
	private String owner;
	
	@Column(name="APPOINTOR_",length=60)
	private String appointor;
	
	@Column(name="APPOINTOR_NODE_",length=60)
	private String appointorNode;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getTaskNodeName() {
		return taskNodeName;
	}
	public void setTaskNodeName(String taskNodeName) {
		this.taskNodeName = taskNodeName;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getAppointor() {
		return appointor;
	}
	public void setAppointor(String appointor) {
		this.appointor = appointor;
	}
	public String getAppointorNode() {
		return appointorNode;
	}
	public void setAppointorNode(String appointorNode) {
		this.appointorNode = appointorNode;
	}
}
