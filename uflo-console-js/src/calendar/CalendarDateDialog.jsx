/**
 * Created by Jacky.Gao on 2016/12/16.
 */
import React,{Component} from 'react';
import ReactDOM from 'react-dom';
import {MsgBox} from 'flowdesigner';
import CommonDialog from '../dialog/CommonDialog.jsx';
import * as actions from './action.js';
import * as event from './event.js';

export default class CalendarDateDialog extends Component{
    constructor(props){
        super(props);
        this.state={data:{}};
    }
    componentDidMount(){
        event.eventEmitter.on(event.SHOW_CALENDAR_DATE_DIALOG,(data)=>{
            $(ReactDOM.findDOMNode(this)).modal('show');
            if(!data.dayOfWeek){
                data.dayOfWeek=2;
            }
            if(!data.dayOfMonth){
                data.dayOfMonth=1;
            }
            if(!data.monthOfYear){
                data.monthOfYear=1;
            }
            this.setState(data);
        });
        event.eventEmitter.on(event.CLOSE_CALENDAR_DATE_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    changeHandler(e){
        const name=e.target.name;
        const value=e.target.value;
        const data=this.state.data;
        data[name]=value;
        this.setState({data});
    }
    render(){
        const {dispatch,type}=this.props;
        const {data,title}=this.state;
        const items=[];
        if(type==='holiday'){
            items.push(
                <div key="holiday">
                    <span>日期：</span>
                    <input type="text" value={data.calendarDate} title="日期格式：yyyy-MM-dd" className="form-control" name="calendarDate" value={data.calendarDate || ''} onChange={this.changeHandler.bind(this)} style={{display:'inline-block',width:'480px',margin:'5px'}}></input>
                </div>
            );
        }else if(type==='weekly'){
            items.push(
                <div key="week">
                    <span>星期：</span>
                    <select className="form-control" name="dayOfWeek" value={data.dayOfWeek || ''} onChange={this.changeHandler.bind(this)} style={{display:'inline-block',width:'480px',margin:'5px'}}>
                        <option value="2">星期一</option>
                        <option value="3">星期二</option>
                        <option value="4">星期三</option>
                        <option value="5">星期四</option>
                        <option value="6">星期五</option>
                        <option value="7">星期六</option>
                        <option value="1">星期日</option>
                    </select>
                </div>
            );
        }else if(type==='daily'){
            items.push(
                <div key="daily">
                    <div>
                        <span>开始时间：</span>
                        <input type="text" title="时间格式：HH:mm:ss" className="form-control" name="rangeStartTime" value={data.rangeStartTime || ''} onChange={this.changeHandler.bind(this)} style={{display:'inline-block',width:'450px',margin:'5px'}}></input>
                    </div>
                    <div>
                        <span>结束时间：</span>
                        <input type="text" title="时间格式：HH:mm:ss" className="form-control" name="rangeEndTime" value={data.rangeEndTime || ''} onChange={this.changeHandler.bind(this)} style={{display:'inline-block',width:'450px',margin:'5px'}}></input>
                    </div>
                </div>
            );
        }else if(type==='monthly'){
            const months=[];
            for(var i=1;i<32;i++){
                months.push(
                    <option key={i} value={i}>{i}日</option>
                )
            }
            items.push(
                <div key="month" style={{marginLeft:'15px'}}>
                    <span>日：</span>
                    <select className="form-control" name="dayOfMonth" value={data.dayOfMonth || ''} onChange={this.changeHandler.bind(this)} style={{display:'inline-block',width:'480px',margin:'5px'}}>
                        {months}
                    </select>
                </div>
            )
        }else if(type==='annual'){
            const months=[];
            for(let i=1;i<13;i++){
                months.push(
                    <option key={i} value={i}>{i}月</option>
                );
            }
            const days=[];
            for(let i=1;i<32;i++){
                days.push(
                    <option key={i} value={i}>{i}号</option>
                );
            }
            items.push(
                <div key="annual">
                    <div style={{marginLeft:'15px'}}>
                        <span>月：</span>
                        <select className="form-control" name="monthOfYear" value={data.monthOfYear || ''} onChange={this.changeHandler.bind(this)} style={{display:'inline-block',width:'480px',margin:'5px'}}>
                            {months}
                        </select>
                    </div>
                    <div style={{marginLeft:'15px'}}>
                        <span>日：</span>
                        <select className="form-control" name="dayOfMonth" value={data.dayOfMonth || ''} onChange={this.changeHandler.bind(this)} style={{display:'inline-block',width:'480px',margin:'5px'}}>
                            {days}
                        </select>
                    </div>
                </div>
            );
        }
        const body=(
            <div style={{margin:'10px'}}>
                <div>
                    <span>名称：</span>
                    <input type="text" className="form-control" name="name" style={{display:'inline-block',width:'480px',margin:'5px'}} onChange={this.changeHandler.bind(this)} value={data.name || ''}></input>
                </div>
                {items}
            </div>
        );
        const buttons=[{
            name:'确认',
            className:'btn btn-primary',
            icon:'glyphicon glyphicon-ok-sign',
            click:function () {
                const name=data.name;
                if(!name || name===''){
                    MsgBox.alert("请输入名称");
                    return;
                }
                if(!type || type===''){
                    MsgBox.alert("请选择日历类型");
                    return;
                }
                data.type=type;
                if(type==='holiday'){
                    if(!data.calendarDate){
                        MsgBox.alert("请输入日期!");
                        return;
                    }
                }else if(type==='weekly'){
                    if(!data.dayOfWeek){
                        MsgBox.alert("请选择星期!");
                        return;
                    }
                }else if(type==='monthly'){
                    if(!data.dayOfMonth){
                        MsgBox.alert("请选择日期!");
                        return;
                    }
                }else if(type==='annual'){
                    if(!data.dayOfMonth){
                        MsgBox.alert("请选择日期!");
                        return;
                    }
                    if(!data.monthOfYear){
                        MsgBox.alert("请选择日期!");
                        return;
                    }
                }else if(type==='daily'){
                    if(!data.rangeStartTime){
                        MsgBox.alert("请输入开始时间!");
                        return;
                    }
                    if(!data.rangeStartTime){
                        MsgBox.alert("请输入结束时间!");
                        return;
                    }
                }
                dispatch(actions.addCalendarDate(data));
                event.eventEmitter.emit(event.CLOSE_CALENDAR_DATE_DIALOG);
            }
        }];
        return (
            <CommonDialog title='添加日期' body={body} buttons={buttons}></CommonDialog>
        );
    }
}