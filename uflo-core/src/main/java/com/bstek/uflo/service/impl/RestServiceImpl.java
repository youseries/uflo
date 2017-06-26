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
package com.bstek.uflo.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.bstek.uflo.service.RestService;

/**
 * @author Jacky.gao
 * @since 2013年9月22日
 */
public class RestServiceImpl implements RestService{
	private String baseUrl;
	private HttpHeaders headers;
	private RestTemplate template=new RestTemplate();
	private List<HttpMessageConverter<?>> converters;
	public RestServiceImpl(String username,String password){
		headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
			return;
		}
		headers.add("uflo.access.username", username);
		headers.add("uflo.access.password", password);
	}
	public <T> ResponseEntity<T>  post(String uri,Object obj,Class<T> responseClazz){
		if(obj==null){
			obj="blank";
		}
		HttpEntity<Object> entity=null;
		if(headers!=null){
			entity=new HttpEntity<Object>(obj,headers);
		}else{
			entity=new HttpEntity<Object>(obj);			
		}
		return template.postForEntity(baseUrl+uri,entity,responseClazz);
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
	public List<HttpMessageConverter<?>> getConverters() {
		return converters;
	}
	public void setConverters(List<HttpMessageConverter<?>> converters) {
		this.converters = converters;
		template.setMessageConverters(converters);
	}
}
