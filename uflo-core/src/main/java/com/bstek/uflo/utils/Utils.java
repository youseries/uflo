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
package com.bstek.uflo.utils;

import java.math.BigDecimal;

/**
 * @author Jacky.gao
 * @since 2018年7月2日
 */
public class Utils {
	public static BigDecimal toBigDecimal(Object obj){
		if(obj==null){
			return null;
		}
		if(obj instanceof BigDecimal){
			return (BigDecimal)obj;
		}else if(obj instanceof String){
			if(obj.toString().trim().equals("")){
				return new BigDecimal(0);
			}
			try{
				String str=obj.toString().trim();
				return new BigDecimal(str);				
			}catch(Exception ex){
				throw new RuntimeException("Can not convert "+obj+" to BigDecimal.");
			}
		}else if(obj instanceof Number){
			Number n=(Number)obj;
			return new BigDecimal(n.doubleValue());
		}
		throw new RuntimeException("Can not convert "+obj+" to BigDecimal.");
	}
}
