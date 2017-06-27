/**
 * Created by Jacky.Gao on 2016/12/10.
 */
import {combineReducers} from 'redux';
import {ajax} from '../Utils.js';
export const LOAD_PROCESS="load_process";
export const LOAD_PROCESS_INSTANCE="load_process_instance";
export const LOAD_TASK='load_task';
export const LOAD_SEQ_FLOWS='load_seq_flows';
export const LOAD_CLIMA_USERS='load_cliam_users';
export const LOAD_JUMP_TASKS='load_jump_tasks';
export const LOAD_HISTORY_PROCESS_INSTANCE='load_history_process_instance';
export const LOAD_HISTORY_TASK='load_history_task';

export function loadHistoryProcessInstance(pageIndex,pageSize,processId){
    return function (dispatch){
        const url=window._server+"/central/loadHistoryProcessInstance";
        ajax(url,{pageIndex,pageSize,processId},function(data){
            dispatch({type:LOAD_HISTORY_PROCESS_INSTANCE,data});
        })
    }
};

export function loadHistoryTask(historyProcessInstanceId){
    return function (dispatch){
        const url=window._server+"/central/loadHistoryTasks";
        ajax(url,{historyProcessInstanceId},function(data){
            dispatch({type:LOAD_HISTORY_TASK,data});
        });
    }
}

export function loadProcess(pageIndex,pageSize,name){
    if(name===undefined || name===null){
        name=window.searchProcessName;
    }else{
        window.searchProcessName=name;
    }
    return function (dispatch){
        const url=window._server+"/central/loadProcess";
        ajax(url,{pageIndex,pageSize,name},function(data){
            dispatch({type:LOAD_PROCESS,data});
        });
    }
}

export function loadProcessInstance(pageIndex,pageSize,processId){
    return function (dispatch){
        const url=window._server+"/central/loadProcessInstance";
        ajax(url,{pageIndex,pageSize,processId},function(data){
            dispatch({type:LOAD_PROCESS_INSTANCE,data});
        });
    };
};

export function loadTask(processInstanceId){
    return function (dispatch){
        const url=window._server+"/central/loadTask";
        ajax(url,{processInstanceId},function(data){
            dispatch({type:LOAD_TASK,data});
        });
    }
};

export function deleteProcess(pageIndex,pageSize,processId){
    return function (dispatch){
        const url=window._server+"/central/deleteProcess";
        ajax(url,{processId},function(){
            dispatch(loadProcess(pageIndex,pageSize));
        })
    }
};

export function deleteProcessInstance(pageIndex,pageSize,processId,processInstanceId){
    return function (dispatch){
        const url=window._server+"/central/deleteProcessInstance";
        ajax(url,{processInstanceId},function(){
            dispatch(loadProcessInstance(pageIndex,pageSize,processId));
        });
    }
};

export function startProcess(pageIndex,pageSize,processId,variables){
    return function (dispatch){
        const url=window._server+"/central/startProcess";
        ajax(url,{processId,variables},function(){
            dispatch(loadProcessInstance(pageIndex,pageSize,processId));
        });
    }
};

export function loadSequenceFlows(taskId){
    return function (dispatch){
        const url=window._server+"/central/loadSequenceFlows";
        ajax(url,{taskId},function(data){
            dispatch({type:LOAD_SEQ_FLOWS,data});
        });
    }
};

export function completeTask(taskId,flowName,variables){
    return function (dispatch){
        const url=window._server+"/central/completeTask";
        ajax(url,{taskId,flowName,variables},function(){
            dispatch(loadProcessInstance(window.processInstancePageData.pageIndex, window.processInstancePageData.pageSize, window.currentProcessId));
        });
    }
};

export function loadCliamUsers(taskId){
    return function (dispatch){
        const url=window._server+"/central/loadCliamUsers";
        ajax(url,{taskId},function(data){
           dispatch({type:LOAD_CLIMA_USERS,data});
        });
    };
};

export function loadJumpTasks(taskId){
    return function (dispatch){
        const url=window._server+"/central/loadJumpTasks";
        ajax(url,{taskId},function(data){
            dispatch({type:LOAD_JUMP_TASKS,data});
        });
    }
};

export function jumpTask(taskId,nodeName){
    return function (dispatch){
        const url=window._server+"/central/jumpTask";
        ajax(url,{taskId,nodeName},function(){
            dispatch(loadTask(window.currentProcessInstanceId));
        })
    }
}

export function cliamTask(taskId,user){
    return function (dispatch){
        const url=window._server+"/central/cliamTask";
        ajax(url,{taskId,user},function(){
            dispatch(loadTask(window.currentProcessInstanceId));
        });
    }
};