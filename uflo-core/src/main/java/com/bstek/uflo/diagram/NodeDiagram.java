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
public class NodeDiagram extends Diagram implements Cloneable{
	private static final long serialVersionUID = -7041158184263583810L;
	private int width;
	private int height;
	private int x;
	private int y;
	private String backgroundColor="220,220,220";
	private String icon;
	private boolean fontBold;
	private String info;
	public NodeDiagram(){
		setBorderColor("18,42,136");
		setFontColor("18,42,136");
	}
	/**
	 * 流程实例经过当前节点的次数
	 */
	private int time;
	private String owner;
	
	private List<SequenceFlowDiagram> sequenceFlowDiagrams;
	
	private ShapeType shapeType;
	
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
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public ShapeType getShapeType() {
		return shapeType;
	}
	public void setShapeType(ShapeType shapeType) {
		this.shapeType = shapeType;
	}
	public List<SequenceFlowDiagram> getSequenceFlowDiagrams() {
		return sequenceFlowDiagrams;
	}
	public void setSequenceFlowDiagrams(List<SequenceFlowDiagram> sequenceFlowDiagrams) {
		this.sequenceFlowDiagrams = sequenceFlowDiagrams;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isFontBold() {
		return fontBold;
	}
	public void setFontBold(boolean fontBold) {
		this.fontBold = fontBold;
	}
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		NodeDiagram diagram=(NodeDiagram)super.clone();
		diagram.setShapeType(shapeType);
		if(sequenceFlowDiagrams!=null){
			List<SequenceFlowDiagram> list=new ArrayList<SequenceFlowDiagram>();
			for(SequenceFlowDiagram d:sequenceFlowDiagrams){
				list.add((SequenceFlowDiagram)d.clone());
			}
			diagram.setSequenceFlowDiagrams(list);
		}
		return diagram;
	}
}
