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
package com.bstek.uflo.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.variable.Variable;
import com.bstek.uflo.query.ProcessInstanceQuery;
import com.bstek.uflo.query.ProcessQuery;
import com.bstek.uflo.query.ProcessVariableQuery;

/**
 * @author Jacky.gao
 * @since 2013年7月29日
 */
public interface ProcessService {
	public static final String BEAN_ID="uflo.processService";
	/**
	 * 根据流程模版ID，返回流程模版对象
	 * @param processId 流程模版ID
	 * @return 返回流程模版对象
	 */
	ProcessDefinition getProcessById(long processId);
	/**
	 * 根据流程模版Key，返回流程模版对象
	 * @param key 流程模版Key
	 * @return 返回流程模版对象
	 */
	ProcessDefinition getProcessByKey(String key);
	/** 
	 * 根据流程模版的名称，返回与该名字匹配最新发布的流程模版对象
	 * @param processName 流程模版名称
	 * @return 返回流程模版对象
	 */
	ProcessDefinition getProcessByName(String processName);
	/** 
	 * 根据流程模版的名称及分类ID，返回与该名字匹配最新发布的流程模版对象
	 * @param processName 流程模版名称
	 * @param categoryId 分类ID
	 * @return 返回流程模版对象
	 */
	ProcessDefinition getProcessByName(String processName,String categoryId);
	/**
	 * 根据流程模版的名称与版本号，返回与该名字与版本号匹配最流程模版对象
	 * @param processName 流程模版名称
	 * @param version 版本号
	 * @return 返回流程模版对象
	 */
	ProcessDefinition getProcessByName(String processName,int version);
	
	/**
	 * 根据流程模版ID，开启一个流程实例
	 * @param processId 流程模版ID
	 * @param startProcessInfo 开启流程实例时所需要的各种信息的包装对象
	 * @return 返回开启成功的流程实例对象
	 */
	ProcessInstance startProcessById(long processId,StartProcessInfo startProcessInfo);
	
	/**
	 * 根据流程模版key，开启一个流程实例
	 * @param key 流程模版key
	 * @param startProcessInfo 开启流程实例时所需要的各种信息的包装对象
	 * @return 返回开启成功的流程实例对象
	 */
	ProcessInstance startProcessByKey(String key,StartProcessInfo startProcessInfo);
	
	/**
	 * 根据流程模版的名称，根据该名称流程模版最新版本开启一个流程实例
	 * @param processName 流程模版名称
	 * @param startProcessInfo 开启流程实例时所需要的各种信息的包装对象
	 * @return 返回开启成功的流程实例对象
	 */
	ProcessInstance startProcessByName(String processName,StartProcessInfo startProcessInfo);
	/**
	 * 根据流程模版的名称与版本号，开启一个流程实例
	 * @param processName 流程模版名称
	 * @param startProcessInfo 开启流程实例时所需要的各种信息的包装对象
	 * @param version 版本号
	 * @return 返回开启成功的流程实例对象
	 */
	ProcessInstance startProcessByName(String processName,StartProcessInfo startProcessInfo,int version);
	
	/**
	 * 删除一个指定的流程实例对象，与这个流程实例相关的人工任务也将会被删除
	 * @param processInstance 流程实例对象
	 */
	void deleteProcessInstance(ProcessInstance processInstance);
	/**
	 * 删除指定流程实例ID对应的流程实例对象
	 * @param processInstanceId 流程实例ID
	 */
	void deleteProcessInstanceById(long processInstanceId);
	
	/**
	 * 从一个压缩文件包中部署一个新的流程模版
	 * @param zipInputStream 一个压缩文件输入流
	 * @return 部署成功后的流程模版对象
	 */
	ProcessDefinition deployProcess(ZipInputStream zipInputStream);
	
	/**
	 * 从一个文件流中部署一个新的流程模版
	 * @param inputStream 文件流
	 * @return 部署成功后的流程模版对象
	 */
	ProcessDefinition deployProcess(InputStream inputStream);
	
	/**
	 * 更新一个流程模版，用指定InputStream中包含的流程模版对象来替换指定ID的流程模版对象
	 * @param inputStream 新的流程模版流对象
	 * @param processId 要替换的目标流程模版ID
	 * @return 更新成功后的流程模版对象
	 */
	ProcessDefinition deployProcess(InputStream inputStream,long processId);
	
	/**
	 * 根据给定的流程实例ID，返回对应的流程实例对象
	 * @param processInstanceId 流程实例ID
	 * @return 返回流程实例对象
	 */
	ProcessInstance getProcessInstanceById(long processInstanceId);
	/**
	 * 根据流程实例ID，返回与该流程实例相关的所有的流程变量
	 * @param processInsanceId 流程实例ID
	 * @return 返回与该流程实例相关的所有的流程变量集合
	 */
	List<Variable> getProcessVariables(long processInsanceId);
	/**
	 * 根据流程实例对象，返回与该流程实例相关的所有的流程变量
	 * @param processInsance 流程实例对象
	 * @return 返回与该流程实例相关的所有的流程变量集合
	 */
	List<Variable> getProcessVariables(ProcessInstance processInsance);
	/**
	 * 获取指定流程实例上的指定key的流程变量的值
	 * @param key 流程变量的key
	 * @param processInstance 流程实例对象
	 * @return 流程变量值
	 */
	Object getProcessVariable(String key,ProcessInstance processInstance);
	/**
	 * 获取指定流程实例ID上对应的流程实例中指定key的流程变量的值
	 * @param key 流程变量的key
	 * @param processInsanceId 流程实例ID
	 * @return 流程变量值
	 */
	Object getProcessVariable(String key,long processInsanceId);
	/**
	 * 删除指定流程实例ID中指定key的流程变量值
	 * @param key 流程变量的key
	 * @param processInstanceId 流程实例ID
	 */
	void deleteProcessVariable(String key,long processInstanceId);
	
	/**
	 * 向指定流程实例ID对应的流程实例中添加流程变量
	 * @param processInstanceId 流程实例ID
	 * @param key 流程变量的key
	 * @param value 对应的流程变量的值
	 */
	void saveProcessVariable(long processInstanceId,String key,Object value);
	/**
	 * 向指定流程实例ID对应的流程实例中批量添加流程变量
	 * @param processInstanceId 流程实例ID
	 * @param variables 要添加的流程变量的Map
	 */
	void saveProcessVariables(long processInstanceId,Map<String,Object> variables);
	
	/**
	 * @return 返回创建成功的流程实例查询对象
	 */
	ProcessInstanceQuery createProcessInstanceQuery();
	/**
	 * @return 返回创建成功的流程变量查询对象
	 */
	ProcessVariableQuery createProcessVariableQuery();
	
	/**
	 * @return 创建创建成功的流程模版查询对象
	 */
	ProcessQuery createProcessQuery();
	
	/**
	 * 删除一个指定ID的流程模版对象，与该模版相关的所有实例及任务都将被删除
	 * @param processId 流程模版ID
	 */
	void deleteProcess(long processId);
	/**
	 * 删除一个指定KEY的流程模版对象，与该模版相关的所有实例及任务都将被删除
	 * @param processKey 流程模版KEY
	 */
	void deleteProcess(String processKey);
	/**
	 * 删除一个指定流程模版对象，与该模版相关的所有实例及任务都将被删除
	 * @param processDefinition 流程模版对象
	 */
	void deleteProcess(ProcessDefinition processDefinition);
	
	/**
	 * 根据给定的流程模版ID，更新当前内存中保存的对应的流程模版对象
	 * @param processId 流程模版ID
	 */
	void updateProcessForMemory(long processId);
	
	/**
	 * 从本地内存中移除指定的流程模版ID对应的流程模版对象
	 * @param processId 流程模版ID
	 */
	void deleteProcessFromMemory(long processId);
	/**
	 * 从本地内存中移除指定的流程模版KEY对应的流程模版对象
	 * @param processKey 流程模版KEY
	 */
	void deleteProcessFromMemory(String processKey);
	/**
	 * 从本地内存中移除指定的流程模版对象
	 * @param processDefinition 流程模版对象
	 */
	void deleteProcessFromMemory(ProcessDefinition processDefinition);
}
