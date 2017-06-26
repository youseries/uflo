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
package com.bstek.uflo.model.variable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Jacky.gao
 * @since 2013年8月1日
 */
@Entity
@DiscriminatorValue("Double")
public class DoubleVariable extends Variable {
	@Column(name="DOUBLE_VALUE_")
	private double doubleValue;

	public DoubleVariable(){}
	public DoubleVariable(double value){
		this.doubleValue=value;
	}
	
	public double getDoubleValue() {
		return doubleValue;
	}
	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}
	@Override
	public Object getValue() {
		return this.doubleValue;
	}
	@Override
	public VariableType getType() {
		return VariableType.Double;
	}
}
