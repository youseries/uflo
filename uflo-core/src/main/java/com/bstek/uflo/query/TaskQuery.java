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
package com.bstek.uflo.query;

import java.util.Date;
import java.util.List;

import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.model.task.TaskType;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public interface TaskQuery extends Query<List<Task>> {
	TaskQuery assignee(String assignee);
	TaskQuery addAssignee(String assignee);
	TaskQuery owner(String owner);
	TaskQuery addTaskState(TaskState state);
	TaskQuery addPrevTaskState(TaskState state);
	TaskQuery processInstanceId(long processInstanceId);
	TaskQuery rootProcessInstanceId(long rootProcessInstanceId);
	TaskQuery createDateLessThen(Date date);
	TaskQuery createDateLessThenOrEquals(Date date);
	TaskQuery createDateGreaterThen(Date date);
	TaskQuery createDateGreaterThenOrEquals(Date date);
	TaskQuery dueDateLessThen(Date date);
	TaskQuery dueDateLessThenOrEquals(Date date);
	TaskQuery dueDateGreaterThen(Date date);
	TaskQuery dueDateGreaterThenOrEquals(Date date);
	TaskQuery urlLike(String url);
	TaskQuery subjectLike(String subject);
	TaskQuery countersign(boolean countersign);
	TaskQuery taskType(TaskType type);
	TaskQuery processId(long processId);
	TaskQuery addProcessId(long processId);
	TaskQuery nameLike(String name);
	TaskQuery nodeName(String nodeName);
	TaskQuery businessId(String businessId);
	TaskQuery page(int firstResult, int maxResults);
	TaskQuery addOrderAsc(String property);
	TaskQuery addOrderDesc(String property);
	TaskQuery addParticipator(String user);
	TaskQuery priority(String priority);
	TaskQuery progress(int progress);
}
