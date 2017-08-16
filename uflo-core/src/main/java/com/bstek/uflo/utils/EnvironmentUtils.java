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

import java.util.Collection;

import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.PlatformTransactionManager;

import com.bstek.uflo.env.EnvironmentProvider;
import com.bstek.uflo.service.CacheService;
import com.bstek.uflo.service.DefaultMemoryCacheService;

/**
 * @author Jacky.gao
 * @since 2013年9月17日
 */
public class EnvironmentUtils implements ApplicationContextAware{
	private EnvironmentProvider provider;
	private static EnvironmentUtils environment;
	private ApplicationContext applicationContext;
	private CacheService cacheService;
	public static EnvironmentUtils getEnvironment(){
		return environment;
	}
	public SessionFactory getSessionFactory(){
		return provider.getSessionFactory();
	}
	
	public PlatformTransactionManager getPlatformTransactionManager(){
		return provider.getPlatformTransactionManager();
	}
	
	public CacheService getCache(){
		return cacheService;
	}
	
	public String getLoginUser(){
		return provider.getLoginUser();
	}
	
	public String getCategoryId(){
		return provider.getCategoryId();
	}
	
	public EnvironmentProvider getEnvironmentProvider(){
		return provider;
	}
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
		Collection<EnvironmentProvider> providers=applicationContext.getBeansOfType(EnvironmentProvider.class).values();
		if(providers.size()==0){
			throw new RuntimeException("You must be implementation "+EnvironmentProvider.class.getName()+" interface when use uflo!");
		}
		provider=providers.iterator().next();
		Collection<CacheService> cacheServices=applicationContext.getBeansOfType(CacheService.class).values();
		if(cacheServices.size()>0){
			cacheService=cacheServices.iterator().next();
		}else{
			cacheService=new DefaultMemoryCacheService();
		}
		environment=this;
		new Splash().print();
	}
}
