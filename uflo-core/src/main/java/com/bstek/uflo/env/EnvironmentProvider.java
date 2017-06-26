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
package com.bstek.uflo.env;

import org.hibernate.SessionFactory;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Jacky.gao
 * @since 2013年9月17日
 */
public interface EnvironmentProvider {
	/**
	 * @return 返回流程引擎需要使用的Hibernate SessionFactory
	 */
	SessionFactory getSessionFactory();
	/**
	 * @return 返回与当前SessionFactory绑定的PlatformTransactionManager对象
	 */
	PlatformTransactionManager getPlatformTransactionManager();
	/**
	 * @return 返回当前系统的登录用户
	 */
	String getLoginUser();
	/**
	 * @return 返回当前系统分类ID
	 */
	String getCategoryId();
}
