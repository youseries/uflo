/**
 * Created by Jacky.Gao on 2016/12/15.
 */
import {combineReducers} from 'redux';
import * as ACTIONS from './action.js';

export function calendar(state=[],action){
    switch (action.type){
        case ACTIONS.LOAD_CALENDAR:
            return action.data;
        case ACTIONS.ADD_CALENDAR:
            let newData=[];
            newData=newData.concat(state);
            newData.push(action.data);
            return newData;
        case ACTIONS.DEL_CALENDAR:
            newData=[];
            for(let c of state){
                if(c.id===action.id){
                    continue;
                }
                newData.push(c);
            }
            return newData;
        default:
            return state;
    }
};

export function calendarDate(state=[],action){
    switch (action.type){
        case ACTIONS.LOAD_CALENDAR_DATE:
            return action.result;
        case ACTIONS.DEL_CALENDAR:
            return {};
        case ACTIONS.ADD_CALENDAR_DATE:
            let newData=[];
            newData=newData.concat(state.data);
            newData.push(action.data);
            return Object.assign({},state,{data:newData});
        case ACTIONS.DEL_CALENDAR_DATE:
            newData=[];
            const id=action.data.id;
            for(let d of state.data){
                if(d.id===id){
                    continue;
                }
                newData.push(d);
            }
            return Object.assign({},state,{data:newData});
        default:
            return state;
    }
};
export default combineReducers({
    calendar,calendarDate
});
