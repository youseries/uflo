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
package com.bstek.uflo.process.swimlane;

import java.util.List;

import com.bstek.uflo.process.assign.Assignee;
import com.bstek.uflo.process.node.AssignmentType;


/**
 * @author Jacky.gao
 * @since 2013年8月12日
 */
public class Swimlane implements java.io.Serializable{
	private static final long serialVersionUID = -2899019518648252638L;
	private String name;
	private String description;
	private AssignmentType assignmentType;
	private List<Assignee> assignees;
	private String expression;
	private String assignmentHandlerBean;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Assignee> getAssignees() {
		return assignees;
	}
	public void setAssignees(List<Assignee> assignees) {
		this.assignees = assignees;
	}
	public String getAssignmentHandlerBean() {
		return assignmentHandlerBean;
	}
	public void setAssignmentHandlerBean(String assignmentHandlerBean) {
		this.assignmentHandlerBean = assignmentHandlerBean;
	}
	public AssignmentType getAssignmentType() {
		return assignmentType;
	}
	public void setAssignmentType(AssignmentType assignmentType) {
		this.assignmentType = assignmentType;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
}
