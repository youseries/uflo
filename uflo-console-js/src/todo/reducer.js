/**
 * Created by Jacky.Gao on 2016/12/7.
 */
import {combineReducers} from 'redux';
import * as ACTIONS from './action.js';


function todo(state={},action){
    switch (action.type){
        case ACTIONS.LOAD_TODO_LIST:
            let data=action.data;
            return Object.assign({},state.prototype,{data});
        default:
            return {data:{result:[],pageIndex:1,pageSize:10,total:0}};
    }
};

function cliam(state={},action){
    switch (action.type){
        case ACTIONS.LOAD_CLIAM_LIST:
            let data=action.data;
            return Object.assign({},state.prototype,{data});
        default:
            return {data:{result:[],pageIndex:1,pageSize:10,total:0}};
    }
};

function history(state={},action){
    switch (action.type){
        case ACTIONS.LOAD_HISTORY_LIST:
            let data=action.data;
            return Object.assign({},state.prototype,{data});
        default:
            return {data:{result:[],pageIndex:1,pageSize:10,total:0}};
    }
};

export default combineReducers({
    todo,cliam,history
});

