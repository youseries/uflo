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
package com.bstek.uflo.deploy.parse;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.bstek.uflo.process.security.Authority;
import com.bstek.uflo.process.security.ComponentAuthority;

/**
 * @author Jacky.gao
 * @since 2013年12月9日
 */
public abstract class AbstractTaskParser extends AbstractParser {
	protected List<ComponentAuthority> parseComponentAuthorities(Element element){
		List<ComponentAuthority> list=new ArrayList<ComponentAuthority>();
		for(Object obj:element.elements()){
			if(!(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(!ele.getName().equals("component-authority")){
				continue;
			}
			String component=unescape(ele.attributeValue("component"));
			Authority authority=Authority.valueOf(ele.attributeValue("authority"));
			list.add(new ComponentAuthority(component,authority));
		}
		return list;
	}


}
