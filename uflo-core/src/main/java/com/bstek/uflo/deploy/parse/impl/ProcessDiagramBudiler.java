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
package com.bstek.uflo.deploy.parse.impl;

import java.util.ArrayList;
import java.util.List;

import com.bstek.uflo.diagram.NodeDiagram;
import com.bstek.uflo.diagram.Point;
import com.bstek.uflo.diagram.ProcessDiagram;
import com.bstek.uflo.diagram.SequenceFlowDiagram;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.process.flow.SequenceFlowImpl;
import com.bstek.uflo.process.node.Node;

/**
 * @author Jacky.gao
 * @since 2013年9月8日
 */
public class ProcessDiagramBudiler {
	private int width=0;
	private int height=0;
	private int nodeWidth=0;
	private int nodeHeight=0;
	private ProcessDefinition process;
	private ProcessDiagram diagram;
	private List<SequenceFlowImpl> processedSequenceFLows=new ArrayList<SequenceFlowImpl>();
	public ProcessDiagramBudiler(ProcessDefinition process){
		this.process=process;
		createDiagram();
	}
	
	private void createDiagram(){
		buildSize(process.getStartNode());		
		diagram=new ProcessDiagram();
		diagram.setName(process.getName());
		diagram.setWidth(width+nodeWidth+30);
		diagram.setHeight(height+nodeHeight+10);
		List<NodeDiagram> nodeDiagrams=new ArrayList<NodeDiagram>();
		for(Node node:process.getNodes()){
			NodeDiagram diagram=node.getDiagram();
			if(node.getSequenceFlows()!=null && node.getSequenceFlows().size()>0){
				List<SequenceFlowDiagram> flowDiagrams=new ArrayList<SequenceFlowDiagram>();
				for(SequenceFlowImpl flow:node.getSequenceFlows()){
					flowDiagrams.add(flow.getDiagram());
				}
				diagram.setSequenceFlowDiagrams(flowDiagrams);
			}
			nodeDiagrams.add(diagram);
			
		}
		diagram.setNodeDiagrams(nodeDiagrams);
	}
	private void buildSize(Node targetNode){
		NodeDiagram d=targetNode.getDiagram();
		if(d.getX()>width){
			width=d.getX();
		}
		if(d.getY()>height){
			height=d.getY();
		}
		if(d.getWidth()>nodeWidth){
			nodeWidth=d.getWidth();
		}
		if(d.getHeight()>nodeHeight){
			nodeHeight=d.getHeight();
		}
		List<SequenceFlowImpl> flows=targetNode.getSequenceFlows();
		if(flows==null)return;
		for(SequenceFlowImpl flow:flows){
			if(processedSequenceFLows.contains(flow)){
				continue;
			}else{
				processedSequenceFLows.add(flow);
			}
			SequenceFlowDiagram diagram=flow.getDiagram();
			buildPoint(diagram.getPoints());
			Node node=process.getNode(flow.getToNode());
			buildSize(node);
		}
	}
	
	private void buildPoint(List<Point> points){
		if(points==null)return;
		for(Point p:points){
			if(p.getX()>width){
				width=p.getX();
			}
			if(p.getY()>height){
				height=p.getY();
			}
		}
	}

	public ProcessDiagram getDiagram(){
		return diagram;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
