/**
 * Created by Jacky.Gao on 2016/12/7.
 */
import {ajax} from '../Utils.js';

export const LOAD_TODO_LIST='load_todo_list';
export const LOAD_CLIAM_LIST='load_cliam_list';
export const LOAD_HISTORY_LIST='load_history_list';

export function loadTodoList(pageIndex,pageSize,taskName){
    if(taskName===undefined || taskName===null){
        taskName=window.todoTaskName;
    }else{
        window.todoTaskName=taskName;
    }
    return function (dispatch){
        const url=window._server+"/todo/loadTodo";
        ajax(url,{pageIndex,pageSize,taskName},function(data){
            dispatch({type:LOAD_TODO_LIST,data})
        });
    }
};

export function loadCliamList(pageIndex,pageSize,taskName){
    if(taskName===undefined || taskName===null){
        taskName=window.claimTaskName;
    }else{
        window.claimTaskName=taskName;
    }
    return function (dispatch){
        const url=window._server+"/todo/loadCliam";
        ajax(url,{pageIndex,pageSize,taskName},function(data){
            dispatch({type:LOAD_CLIAM_LIST,data});
        });
    }
};

export function claimTask(pageIndex,pageSize,taskId,taskName){
    if(taskName===undefined || taskName===null){
        taskName=window.claimTaskName;
    }else{
        window.claimTaskName=taskName;
    }
    return function (dispatch){
        const url=window._server+"/todo/cliamTask";
        ajax(url,{taskId},function(){
           dispatch(loadCliamList(pageIndex,pageSize,taskName));
        });
    }
}


export function loadHistory(pageIndex,pageSize,taskName){
    if(taskName===undefined || taskName===null){
        taskName=window.historyTaskName;
    }else{
        window.historyTaskName=taskName;
    }
    return function (dispatch){
        const url=window._server+"/todo/loadHistory";
        ajax(url,{pageIndex,pageSize,taskName},function(data){
            dispatch({type:LOAD_HISTORY_LIST,data})
        });
    }
};