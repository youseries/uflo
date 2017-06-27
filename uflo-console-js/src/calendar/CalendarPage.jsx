/**
 * Created by Jacky.Gao on 2016/12/15.
 */
import React,{Component} from 'react';
import Splitter from '../splitter/component/Splitter.jsx';
import Grid from '../grid/component/Grid.jsx';
import {connect} from 'react-redux';
import * as event from './event.js';
import * as actions from './action.js';
import CalendarDefDialog from './CalendarDefDialog.jsx';
import DateGrid from './DateGrid.jsx';

class CalendarPage extends Component{
    render(){
        const {calendarDefs,calendarDate,dispatch}=this.props;
        const type=calendarDate.type || '';
        const calendarId=calendarDate.calendarId || '';
        const calendarDefHeaders=[
            {id:'def-name',name:'name',label:'日历名称',width:'200px',filterable:true},
            {id:'def-type',name:'type',label:'类型',width:'130px',mapping:{holiday:'假日',weekly:'星期',monthly:'月中的某些天',annual:'每年都经历的日期',daily:'每天的某段时间'}},
            {id:'def-desc',name:'desc',label:'描述'}
        ];
        const calendarDefCol={
            width:'70px',
            operations:[
                {
                    label:'删除当前日历',
                    icon:'glyphicon glyphicon-remove',
                    style:{fontSize:'18px',color:'#F44336',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        bootbox.confirm("真的要删除当前日历【"+rowData.name+"】吗？",function(result){
                            if(!result)return;
                            dispatch(actions.deleteCalendar(rowData.id))
                        });
                    }
                },
                {
                    label:'修改当前日历',
                    icon:'glyphicon glyphicon-edit',
                    style:{fontSize:'18px',color:'#4CAF50',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        event.eventEmitter.emit(event.SHOW_CALENDAR_DIALOG,{title:'修改日历信息',data:rowData});
                    }
                }
            ]
        };
        return (
            <Splitter orientation='vertical' position='50%'>
                <div>
                    <button type="button" className="btn btn-danger" style={{margin:'1px'}} onClick={()=>{
                        event.eventEmitter.emit(event.SHOW_CALENDAR_DIALOG,{title:'添加日历',data:{}});
                    }}><i className="glyphicon glyphicon-plus"></i> 添加日历</button>
                    <Grid headers={calendarDefHeaders} rows={calendarDefs} operationConfig={calendarDefCol} rowClick={function(rowData){
                        dispatch(actions.loadCalendarDate(rowData.id,rowData.type));
                    }}/>
                    <CalendarDefDialog dispatch={dispatch}/>
                </div>
                <div>
                    <DateGrid dispatch={dispatch} type={type} data={calendarDate.data} calendarId={calendarId}></DateGrid>
                </div>
            </Splitter>
        );
    };
};
function select(state){
    return {
        calendarDefs:state.calendar,
        calendarDate:state.calendarDate
    }
}
export default connect(select)(CalendarPage);