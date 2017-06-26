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
package com.bstek.uflo.console.handler.impl.diagram;

import java.text.SimpleDateFormat;
import java.util.List;

import com.bstek.uflo.diagram.TaskDiagramInfoProvider;
import com.bstek.uflo.diagram.TaskInfo;

/**
 * @author Jacky.gao
 * @since 2016年12月29日
 */
public class DefaultTaskDiagramInfoProvider implements TaskDiagramInfoProvider {
	private boolean disableDefaultTaskDiagramInfoProvider;
	@Override
	public boolean disable() {
		return disableDefaultTaskDiagramInfoProvider;
	}

	@Override
	public String getInfo(String nodeName, List<TaskInfo> tasks) {
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sb=null;
		if(tasks!=null && tasks.size()>0){
			sb=new StringBuffer();
			if(tasks.size()>1){
				for(int i=0;i<tasks.size();i++){
					TaskInfo task=tasks.get(i);
					sb.append("任务"+(i+1)+":\r");
					sb.append("  所有人："+task.getOwner()+"\r");
					sb.append("  处理人："+task.getAssignee()+"\r");
					sb.append("  创建时间："+sd.format(task.getCreateDate())+"\r");
					if(task.getEndDate()!=null){
						sb.append("  完成时间："+sd.format(task.getEndDate())+"\r");						
					}
				}								
			}else{
				TaskInfo task=tasks.get(0);
				sb.append("所有人："+task.getOwner()+"\r");
				sb.append("处理人："+task.getAssignee()+"\r");
				sb.append("创建时间："+sd.format(task.getCreateDate())+"\r");
				if(task.getEndDate()!=null){
					sb.append("完成时间："+sd.format(task.getEndDate())+"\r");						
				}
			}
		}
		if(sb!=null){
			return sb.toString();
		}else{
			return null;
		}
	}
	
	public void setDisableDefaultTaskDiagramInfoProvider(
			boolean disableDefaultTaskDiagramInfoProvider) {
		this.disableDefaultTaskDiagramInfoProvider = disableDefaultTaskDiagramInfoProvider;
	}
}
