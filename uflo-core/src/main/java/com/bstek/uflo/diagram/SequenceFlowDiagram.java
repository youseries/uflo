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
public class SequenceFlowDiagram extends Diagram implements Cloneable{
	private static final long serialVersionUID = 1L;
	private List<Point> points;
	private String to;
	private String labelPosition;
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(String labelPosition) {
		this.labelPosition = labelPosition;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		SequenceFlowDiagram diagram=(SequenceFlowDiagram)super.clone();
		if(points!=null){
			List<Point> list=new ArrayList<Point>();
			for(Point p:points){
				list.add((Point)p.clone());
			}
			diagram.setPoints(list);
		}
		return diagram;
	}
}
