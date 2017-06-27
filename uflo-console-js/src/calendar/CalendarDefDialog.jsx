/**
 * Created by Jacky.Gao on 2016/12/16.
 */
import React,{Component} from 'react';
import ReactDOM from 'react-dom';
import {MsgBox} from 'flowdesigner';
import CommonDialog from '../dialog/CommonDialog.jsx';
import * as actions from './action.js';
import * as event from './event.js';

export default class CalendarDefDialog extends Component{
    constructor(props){
        super(props);
        this.state={data:{},title:''};
    }
    componentDidMount(){
        event.eventEmitter.on(event.SHOW_CALENDAR_DIALOG,(data)=>{
            $(ReactDOM.findDOMNode(this)).modal('show');
            this.setState(data);
        });
        event.eventEmitter.on(event.CLOSE_CALENDAR_DIALOG,()=>{
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
        const {dispatch}=this.props;
        const {data,title}=this.state;
        const body=(
            <div style={{margin:'10px'}}>
                <div>
                    <span>名称：</span>
                    <input type="text" className="form-control" name="name" style={{display:'inline-block',width:'480px',margin:'5px'}} onChange={this.changeHandler.bind(this)} value={data.name || ''}></input>
                </div>
                <div>
                    <span>类型：</span>
                    <select className="form-control" value={data.type || ''} name="type" onChange={this.changeHandler.bind(this)} disabled={data.type ? 'disabled' : ''} style={{display:'inline-block',width:'480px',margin:'5px'}}>
                        <option value="holiday">假日</option>
                        <option value="weekly">星期</option>
                        <option value="monthly">月中的某些天</option>
                        <option value="annual">每年都经历的日期</option>
                        <option value="daily">每天的某段时间</option>
                    </select>
                </div>
                <div>
                    <span>描述：</span>
                    <input type="text" value={data.desc} className="form-control" name="desc" value={data.desc || ''} onChange={this.changeHandler.bind(this)} style={{display:'inline-block',width:'480px',margin:'5px'}}></input>
                </div>
            </div>
        );
        const buttons=[{
            name:'确认',
            className:'btn btn-primary',
            icon:'glyphicon glyphicon-ok-sign',
            click:function () {
                const name=data.name;
                if(!name || name===''){
                    MsgBox.alert("请输入日历名称");
                    return;
                }
                const desc=data.desc;
                if(!desc || desc===''){
                    MsgBox.alert("请输入描述");
                    return;
                }
                const type=data.type;
                if(!type || type===''){
                    data.type='holiday';
                }
                dispatch(actions.addCalendar(data));
                event.eventEmitter.emit(event.CLOSE_CALENDAR_DIALOG);
            }
        }];
        return (
            <CommonDialog title={title} body={body} buttons={buttons}></CommonDialog>
        );
    }
}