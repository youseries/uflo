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

@Entity
@DiscriminatorValue("Character")
public class CharacterVariable extends Variable {
	@Column(name="CHARACTER_VALUE_")
	private Character characterValue;

	public CharacterVariable(){}
	
	public CharacterVariable(Character value){
		this.characterValue=value;
	}
	@Override
	public Object getValue() {
		return characterValue;
	}

	public Character getCharacterValue() {
		return characterValue;
	}

	public void setCharacterValue(Character characterValue) {
		this.characterValue = characterValue;
	}
	@Override
	public VariableType getType() {
		return VariableType.Character;
	}
}
