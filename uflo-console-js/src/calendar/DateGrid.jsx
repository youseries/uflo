/**
 * Created by Jacky.Gao on 2016/12/16.
 */
import React,{Component} from 'react';
import Grid from '../grid/component/Grid.jsx';
import CalendarDateDialog from './CalendarDateDialog.jsx';
import * as event from './event.js';
import * as actions from './action.js';

export default class DateGrid extends Component{
    render(){
        const {data,dispatch,type,calendarId}=this.props;
        const headers=[{id:'week-name',name:'name',label:'名称',filterable:true}];
        if(type==='holiday'){
            headers.push({id:'d-calendarDate',name:'calendarDate',label:'日期',format:'',width:'150px',dateFormat:'yyyy-MM-dd'});
        }else if(type==='weekly'){
            headers.push({id:'d-dayOfWeek',name:'dayOfWeek',label:'星期',format:'',width:'100px',
                mapping:{2:'星期一',3:'星期二',4:'星期三',5:'星期四',6:'星期五',7:'星期六',1:'星期日'}
            });
        }else if(type==='monthly'){
            headers.push({id:'d-dayOfMonth',name:'dayOfMonth',label:'日',format:'',width:'80px'});
        }else if(type==='annual'){
            headers.push({id:'d-monthOfYear',name:'monthOfYear',label:'月',format:'',width:'80px'});
            headers.push({id:'d-dayOfMonth',name:'dayOfMonth',label:'日',format:'',width:'80px'});
        }else if(type==='daily'){
            headers.push({id:'d-rangeStartTime',name:'rangeStartTime',label:'开始时间',format:'',width:'150px'});
            headers.push({id:'d-rangeEndTime',name:'rangeEndTime',label:'结束时间',format:'',width:'150px'});
        }
        const colConfig={
            width:'70px',
            operations:[
                {
                    label:'删除',
                    icon:'glyphicon glyphicon-remove',
                    style:{fontSize:'18px',color:'#F44336',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        bootbox.confirm("真的要删除当前日期["+rowData.name+"]吗？",function(result){
                            if(!result)return;
                            dispatch(actions.deleteCalendarDate({type,calendarId,id:rowData.id}));
                        });
                    }
                }
            ]
        };
        return (
            <div>
                <button type="button" onClick={()=>{
                    if(!type || type===''){
                        bootbox.alert("请先选择日历！");
                        return;
                    }
                    event.eventEmitter.emit(event.SHOW_CALENDAR_DATE_DIALOG,{data:{calendarId}});
                }} className="btn btn-primary" style={{margin:'1px'}}><i className="glyphicon glyphicon-plus-sign"></i> 添加日期</button>
                <Grid headers={headers} operationConfig={colConfig} rows={data || []}></Grid>
                <CalendarDateDialog dispatch={dispatch} type={type}/>
            </div>
        );
    }
}