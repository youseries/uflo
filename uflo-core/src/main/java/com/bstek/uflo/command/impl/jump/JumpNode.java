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
package com.bstek.uflo.command.impl.jump;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2013年9月27日
 */
public class JumpNode {
	private List<String> parent=new LinkedList<String>();
	private int level;
	private String name;
	private String label;
	private boolean isTask;
	public JumpNode(){}
	public JumpNode(String name) {
		this.name = name;
	}

	public boolean isTask() {
		return isTask;
	}

	public void setTask(boolean isTask) {
		this.isTask = isTask;
	}
	public void addParent(String name){
		this.parent.add(name);
	}
	
	public void decreaseParent(){
		int size=parent.size();
		if(size>0){
			this.parent.remove((parent.size()-1));			
		}
	}
	
	public List<String> getParent() {
		return parent;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
