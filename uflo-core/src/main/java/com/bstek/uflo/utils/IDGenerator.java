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

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.StaleStateException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.command.impl.AcquireDbidCommand;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class IDGenerator implements ApplicationContextAware{
	private static Log log = LogFactory.getLog(IDGenerator.class);
	private CommandService commandService;
	private static IDGenerator generator;
	private static Random random = new Random();
	private long nextId;
	private long lastId=-1;
	private int blockSize=5000;
	private int maxAttempts = 5;

	public synchronized long nextId() {
		if (lastId < nextId) {
			for (int attempts = maxAttempts; (attempts > 0); attempts--) {
				try {
					AcquireDbidCommand command = new AcquireDbidCommand(blockSize);
					nextId = commandService.executeCommandInNewTransaction(command);
					lastId = nextId + blockSize - 1;
					break;
				} catch (StaleStateException e) {
					attempts--;
					if (attempts == 0) {
						throw new IllegalStateException("couldn't acquire block of ids, tried "+ maxAttempts + " times");
					}
					// if there are still attempts left, first wait a bit
					int millis = 20 + random.nextInt(200);
					log.debug("optimistic locking failure while trying to acquire id block.  retrying in "+ millis + " millis");
					try {
						Thread.sleep(millis);
					} catch (InterruptedException e1) {
						log.debug("waiting after id block locking failure got interrupted");
					}
				}
			}
		}
		return nextId++;
	}
	
	public static IDGenerator getInstance(){
		return generator;
	}

	public void setCommandService(CommandService commandService) {
		this.commandService = commandService;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		IDGenerator.generator=this;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
}
