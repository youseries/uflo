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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.uflo.diagram.ProcessDiagram;
import com.bstek.uflo.process.node.Node;
import com.bstek.uflo.process.node.StartNode;
import com.bstek.uflo.process.security.ComponentAuthority;
import com.bstek.uflo.process.swimlane.Swimlane;
/**
 * @author Jacky.gao
 * @since 2013年8月18日
 */
@Entity
@Table(name="UFLO_PROCESS")
public class ProcessDefinition implements java.io.Serializable{
	private static final long serialVersionUID = -1328642749306459546L;

	@Id
	@Column(name="ID_")
	private long id;
	
	@Column(name="NAME_",length=60)
	private String name;
	
	@Column(name="KEY_",length=60)
	private String key;
	
	@Column(name="START_PROCESS_URL_",length=120)
	private String startProcessUrl;
	
	@Column(name="VERSION_")
	private int version;
	
	@Column(name="CREATE_DATE_")
	private Date createDate;
	
	@Column(name="EFFECT_DATE_")
	private Date effectDate;
	
	@Column(name="CATEGORY_ID_",length=60)
	private String categoryId;
	
	@Column(name="CATEGORY_",length=60)
	private String category;
	
	@Column(name="DESCRIPTION_")
	private String description;
	
	@Transient
	private String eventHandlerBean;
	
	@Transient
	private StartNode startNode;

	@Transient
	private List<Node> nodes;
	
	@Transient
	private List<Swimlane> swimlanes;
	
	@Transient
	private List<ComponentAuthority> componentSecuritys;

	@Transient
	private ProcessDiagram diagram;
	
	public Node getNode(String name){
		for(Node node:nodes){
			if(node.getName().equals(name)){
				return node;
			}
		}
		return null;
	}
	
	public Swimlane getSwimlane(String name){
		for(Swimlane swimlane:swimlanes){
			if(swimlane.getName().equals(name)){
				return swimlane;
			}
		}
		return null;
	}

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

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getStartProcessUrl() {
		return startProcessUrl;
	}
	public void setStartProcessUrl(String startProcessUrl) {
		this.startProcessUrl = startProcessUrl;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public StartNode getStartNode() {
		return startNode;
	}

	public void setStartNode(StartNode startNode) {
		this.startNode = startNode;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Swimlane> getSwimlanes() {
		return swimlanes;
	}

	public void setSwimlanes(List<Swimlane> swimlanes) {
		this.swimlanes = swimlanes;
	}

	public List<ComponentAuthority> getComponentSecuritys() {
		return componentSecuritys;
	}

	public void setComponentSecuritys(List<ComponentAuthority> componentSecuritys) {
		this.componentSecuritys = componentSecuritys;
	}

	public String getEventHandlerBean() {
		return eventHandlerBean;
	}

	public void setEventHandlerBean(String eventHandlerBean) {
		this.eventHandlerBean = eventHandlerBean;
	}

	public ProcessDiagram getDiagram() {
		return diagram;
	}

	public void setDiagram(ProcessDiagram diagram) {
		this.diagram = diagram;
	}
}
