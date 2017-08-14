package com.bstek.uflo.console.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Jacky.gao
 * @since 2017年7月14日
 */
public class ProcessProviderUtils implements ApplicationContextAware{
	private static List<ProcessProvider> providers=null;
	
	public static ProcessProvider getProcessProvider(String fileName){
		for(ProcessProvider p:providers){
			if(p.support(fileName)){
				return p;
			}
		}
		throw new RuntimeException("Unsupport process file :"+fileName);
	}
	public static ProcessProvider getProcessProviderByName(String providerName){
		for(ProcessProvider p:providers){
			if(p.getName().equals(providerName)){
				return p;
			}
		}
		throw new RuntimeException("Unsupport process provider :"+providerName);
	}
	public static List<ProcessProvider> getProviders() {
		return providers;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<ProcessProvider> coll=applicationContext.getBeansOfType(ProcessProvider.class).values();
		providers=new ArrayList<ProcessProvider>();
		for(ProcessProvider p:coll){
			if(p.isDisabled()){
				continue;
			}
			providers.add(p);
		}
	}
}
