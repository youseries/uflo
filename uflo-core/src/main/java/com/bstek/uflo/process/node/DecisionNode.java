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
package com.bstek.uflo.process.node;

import org.apache.commons.lang.StringUtils;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.flow.SequenceFlowImpl;
import com.bstek.uflo.process.handler.DecisionHandler;

/**
 * @author Jacky.gao
 * @since 2013年8月8日
 */
public class DecisionNode extends Node {
	private static final long serialVersionUID = -6950253419921784972L;
	private String expression;
	private String handlerBean;
	private DecisionType decisionType;
	@Override
	public boolean enter(Context context, ProcessInstance processInstance) {
		return true;
	}

	@Override
	public String leave(Context context, ProcessInstance processInstance,String flowName) {
		if(decisionType.equals(DecisionType.Handler)){
			DecisionHandler handler=(DecisionHandler)context.getApplicationContext().getBean(handlerBean);
			flowName=handler.handle(context, processInstance);
		}else{
			Object obj=context.getExpressionContext().eval(processInstance, expression);
			if(obj instanceof String){
				flowName=(String)obj;					
			}else{
				throw new IllegalArgumentException("Expression ["+expression+"] value type is not a String.");
			}
		}
		if(StringUtils.isEmpty(flowName)){
			throw new IllegalArgumentException("DecisionNode must be specify handlerBean or expression at least one");
		}
		SequenceFlowImpl flow=getFlow(flowName);
		if(flow==null){
			throw new IllegalArgumentException("Sequence flow ["+flowName+"] is not exist!");
		}
		flow.execute(context, processInstance);
		return flow.getName();
	}
	
	@Override
	public void cancel(Context context, ProcessInstance processInstance) {
	}

	@Override
	public NodeType getType() {
		return NodeType.Decision;
	}
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getHandlerBean() {
		return handlerBean;
	}

	public void setHandlerBean(String handlerBean) {
		this.handlerBean = handlerBean;
	}

	public DecisionType getDecisionType() {
		return decisionType;
	}

	public void setDecisionType(DecisionType decisionType) {
		this.decisionType = decisionType;
	}
}
