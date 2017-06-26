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
package com.bstek.uflo.service;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author Jacky.gao
 * @since 2013年8月20日
 */
@XmlRootElement(name = "StartProcessInfo")
public class StartProcessInfo {
	public static final String KEY = StartProcessInfo.class.getName();
	private String tag;
	private String promoter;
	private String sequenceFlowName;
	private String businessId;
	private String subject;
	private String completeStartTaskOpinion;
	private Map<String, Object> variables;
	private boolean completeStartTask = true;

	public StartProcessInfo() {
	}

	public StartProcessInfo(String promoter) {
		this.promoter = promoter;
	}

	public StartProcessInfo setBusinessId(String businessId) {
		this.businessId = businessId;
		return this;
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

	public String getSequenceFlowName() {
		return sequenceFlowName;
	}

	public void setSequenceFlowName(String sequenceFlowName) {
		this.sequenceFlowName = sequenceFlowName;
	}

	public boolean isCompleteStartTask() {
		return completeStartTask;
	}

	public void setCompleteStartTask(boolean completeStartTask) {
		this.completeStartTask = completeStartTask;
	}

	public String getCompleteStartTaskOpinion() {
		return completeStartTaskOpinion;
	}

	public void setCompleteStartTaskOpinion(String completeStartTaskOpinion) {
		this.completeStartTaskOpinion = completeStartTaskOpinion;
	}

	@XmlElement(name = "variables")
	@XmlJavaTypeAdapter(MapAdapter.class)
	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}
}
