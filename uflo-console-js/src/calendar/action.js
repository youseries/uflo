/**
 * Created by Jacky.Gao on 2016/12/15.
 */
import {ajax} from '../Utils.js';
export const LOAD_CALENDAR='load_calendar';
export const ADD_CALENDAR='add_calendar';
export const ADD_CALENDAR_DATE='add_calendar_date';
export const DEL_CALENDAR='del_calendar';
export const DEL_CALENDAR_DATE='del_calendar_date';
export const LOAD_CALENDAR_DATE='load_calendar_date';

export function loadCalendar(){
    return function (dispatch){
        const url=window._server+"/calendar/loadCalendars";
        ajax(url,{},function(data){
           dispatch({type:LOAD_CALENDAR,data});
        });
    }
};
export function addCalendar(data){
    return function (dispatch){
        const url=window._server+"/calendar/saveCalendarDef";
        ajax(url,data,function(result){
           dispatch({type:ADD_CALENDAR,data:result});
        });
    }
};
export function deleteCalendar(id){
    return function (dispatch){
        const url=window._server+'/calendar/deleteCalendarDef';
        ajax(url,{id},function(){
           dispatch({type:DEL_CALENDAR,id});
        });
    }
};
export function loadCalendarDate(id,type){
    return function (dispatch){
        const url=window._server+'/calendar/loadCalendarDate';
        ajax(url,{id},function(data){
            dispatch({type:LOAD_CALENDAR_DATE,result:{data,type,calendarId:id}});
        });
    }
};
export function deleteCalendarDate(data){
    return function (dispatch){
        const url=window._server+"/calendar/deleteCalendarDate";
        ajax(url,{id:data.id},function(){
            dispatch({type:DEL_CALENDAR_DATE,data});
        });
    }
};
export function addCalendarDate(data){
    return function (dispatch){
        const url=window._server+"/calendar/saveCalendarDate";
        ajax(url,data,function(result){
           dispatch({type:ADD_CALENDAR_DATE,data:result});
        });
    }
};