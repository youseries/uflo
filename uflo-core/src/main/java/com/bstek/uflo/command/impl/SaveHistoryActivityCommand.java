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
package com.bstek.uflo.command.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.HistoryActivity;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.node.Node;
import com.bstek.uflo.utils.IDGenerator;

/**
 * @author Jacky.gao
 * @since 2013年7月31日
 */
public class SaveHistoryActivityCommand implements Command<HistoryActivity> {
	private ProcessInstance processInstance;
	private Node node;
	private boolean isEnd;
	private String leaveFlowName;
	public SaveHistoryActivityCommand(ProcessInstance processInstance,Node node,boolean isEnd,String leaveFlowName){
		this.processInstance=processInstance;
		this.node=node;
		this.isEnd=isEnd;
		this.leaveFlowName=leaveFlowName;
	}
	@SuppressWarnings("unchecked")
	public HistoryActivity execute(Context context) {
		Session session=context.getSession();
		/**
		 * 首先尝试取没有结束日期的当前节点的历史节点，如果有，那么就简单的为其添加结束时间，如果没有则创建一个新的，且保持结束日期为空
		 * 如果没有未结束的历史节点，那么就尝试按结束日期倒序排取最晚结束的历史结束，然后更新其结束日期即可。
		 * */
		HistoryActivity hisActivity=(HistoryActivity)session.createCriteria(HistoryActivity.class)
				.add(Restrictions.eq("processInstanceId", processInstance.getId()))
				.add(Restrictions.isNull("endDate"))
				.add(Restrictions.eq("nodeName",node.getName())).uniqueResult();
		if(isEnd){
			if(hisActivity==null){
				List<HistoryActivity> historyActivities=session.createCriteria(HistoryActivity.class)
						.add(Restrictions.eq("processInstanceId", processInstance.getId()))
						.add(Restrictions.isNotNull("endDate"))
						.addOrder(Order.desc("createDate"))
						.add(Restrictions.eq("nodeName",node.getName())).list();
				if(historyActivities.size()>0){
					hisActivity=historyActivities.get(0);
				}
			}
			if(hisActivity==null){
				return null;
			}
			hisActivity.setEndDate(new Date());
			if(StringUtils.isNotEmpty(leaveFlowName)){
				hisActivity.setLeaveFlowName(leaveFlowName);				
			}
		}else{
			if(hisActivity==null){
				hisActivity=new HistoryActivity();
				hisActivity.setCreateDate(new Date());
				hisActivity.setHistoryProcessInstanceId(processInstance.getHistoryProcessInstanceId());
				hisActivity.setNodeName(node.getName());
				hisActivity.setId(IDGenerator.getInstance().nextId());
				hisActivity.setDescription(node.getDescription());
				hisActivity.setProcessId(node.getProcessId());
				hisActivity.setRootProcessInstanceId(processInstance.getRootId());
				hisActivity.setProcessInstanceId(processInstance.getId());
				hisActivity.setLeaveFlowName(leaveFlowName);
			}
		}
		session.saveOrUpdate(hisActivity);
		return hisActivity;
	}
}
