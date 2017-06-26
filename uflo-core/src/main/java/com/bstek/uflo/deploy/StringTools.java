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
package com.bstek.uflo.deploy;

import org.apache.commons.lang.StringUtils;

public class StringTools {
	public static String escape(String str) {
		if (StringUtils.isEmpty(str))
			return str;
		str = replaceString(str, "&", "&amp;");
		str = replaceString(str, "<", "&lt;");
		str = replaceString(str, ">", "&gt;");
		str = replaceString(str, "&apos;", "&apos;");
		str = replaceString(str, "\"", "&quot;");
		return str;
	}

	public static String unescape(String str) {
		str = replaceString(str, "&lt;", "<");
		str = replaceString(str, "&gt;", ">");
		str = replaceString(str, "&apos;", "&apos;");
		str = replaceString(str, "&quot;", "\"");
		str = replaceString(str, "&amp;", "&");
		return str;
	}

	private static String replaceString(String strData, String regex,
			String replacement) {
		if (strData == null) {
			return null;
		}
		int index;
		index = strData.indexOf(regex);
		String strNew = "";
		if (index >= 0) {
			while (index >= 0) {
				strNew += strData.substring(0, index) + replacement;
				strData = strData.substring(index + regex.length());
				index = strData.indexOf(regex);
			}
			strNew += strData;
			return strNew;
		}
		return strData;
	}
}
