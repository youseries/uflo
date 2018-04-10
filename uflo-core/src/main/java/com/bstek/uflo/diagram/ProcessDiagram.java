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
package com.bstek.uflo.diagram;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2013年9月8日
 */
public class ProcessDiagram extends Diagram implements Cloneable{
	private static final long serialVersionUID = 4764263010367898866L;
	private int width;
	private int height;
	private boolean showTime;
	private List<NodeDiagram> nodeDiagrams;

	public List<NodeDiagram> getNodeDiagrams() {
		return nodeDiagrams;
	}

	public void setNodeDiagrams(List<NodeDiagram> nodeDiagrams) {
		this.nodeDiagrams = nodeDiagrams;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public boolean isShowTime() {
		return showTime;
	}

	public void setShowTime(boolean showTime) {
		this.showTime = showTime;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		ProcessDiagram diagram=(ProcessDiagram)super.clone();
		List<NodeDiagram> list=new ArrayList<NodeDiagram>();
		for(NodeDiagram d:nodeDiagrams){
			list.add((NodeDiagram)d.clone());
		}
		diagram.setNodeDiagrams(list);
		return diagram;
	}
}
