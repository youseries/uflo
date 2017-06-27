/**
 * Created by Jacky.Gao on 2016/12/10.
 */
import {combineReducers} from 'redux'
import * as ACTION from './action.js';

export function process(state={pageIndex:1,pageSize:10,total:0,data:[]},action){
    switch (action.type){
        case ACTION.LOAD_PROCESS:
            const data=action.data;
            return Object.assign({},state.prototype,data);
        default:
            return state;
    }
};
export function processInstance(state={pageIndex:1,pageSize:10,total:0,data:[]},action){
    switch (action.type){
        case ACTION.LOAD_PROCESS_INSTANCE:
            const data=action.data;
            return Object.assign({},state.prototype,data);
        default:
            return state;
    }
};

export function task(state=[],action){
    switch (action.type){
        case ACTION.LOAD_TASK:
            return action.data;
        case ACTION.LOAD_SEQ_FLOWS:
            return state;
        case ACTION.LOAD_CLIMA_USERS:
            return state;
        case ACTION.LOAD_JUMP_TASKS:
            return state;
        default:
            return [];
    }
};

export function seqFlows(state=[],action){
    switch (action.type){
        case ACTION.LOAD_SEQ_FLOWS:
            return action.data;
        default:
            return [];
    }
};

export function cliamUsers(state=[],action){
    switch (action.type){
        case ACTION.LOAD_CLIMA_USERS:
            return action.data;
        default:
            return [];
    }
};

export function jumpTasks(state=[],action){
    switch (action.type){
        case ACTION.LOAD_JUMP_TASKS:
            return action.data;
        default:
            return [];
    }
};

export function historyProcessInstance(state=state={pageIndex:1,pageSize:10,total:0,data:[]},action){
    switch (action.type){
        case ACTION.LOAD_HISTORY_PROCESS_INSTANCE:
            return action.data;
        default:
            return state;
    }
};
export function historyTask(state=[],action){
    switch (action.type){
        case ACTION.LOAD_HISTORY_TASK:
            return action.data;
        default:
            return [];
    }
};

export default combineReducers({
   process,processInstance,task,seqFlows,cliamUsers,jumpTasks,historyProcessInstance,historyTask
});