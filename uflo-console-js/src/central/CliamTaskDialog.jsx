/**
 * Created by Jacky.Gao on 2016/12/11.
 */
import React,{Component} from 'react';
import ReactDOM from 'react-dom';
import {connect} from 'react-redux';
import CommonDialog from '../dialog/CommonDialog.jsx';
import * as event from './event.js';
import Grid from '../grid/component/Grid.jsx';
import * as actions from './action.js';
import {MsgBox} from 'flowdesigner';

class CliamTaskDialog extends Component{
    constructor(props){
        super(props);
        this.state={title:''};
    }
    componentDidMount(){
        const dispatch=this.props.dispatch;
        event.eventEmitter.on(event.OPEN_CLIAM_TASK_DIALOG,(data)=>{
            $(ReactDOM.findDOMNode(this)).modal('show');
            this.setState(data);
            const taskId=data.taskId;
            dispatch(actions.loadCliamUsers(taskId));
        });
        event.eventEmitter.on(event.CLOSE_CLIAM_TASK_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    render(){
        const {title,taskId,data}=this.state;
        const {dispatch,cliamUsers}=this.props;

        const userOptions=[];
        cliamUsers.forEach(function(p,index){
            userOptions.push(
                <option key={index}>{p.user}</option>
            );
        });
        const body = (
            <div>
                选择任务领取人：
                <select className="form-control cliamUserSelect" style={{display:"inline-block",width:"330px",marginTop:'15px'}}>
                    {userOptions}
                </select>
            </div>
        );
        const buttons=[{
            name:'确认',
            className:'btn btn-primary',
            icon:'glyphicon glyphicon-ok-sign',
            click:function () {
                const user=$(".cliamUserSelect").val();
                if(!user || user.length<1){
                    bootbox.alert("请选择任务领取人！");
                    return;
                }
                MsgBox.confirm("真的要将这个任务分派给["+user+"]吗？",function(){
                    dispatch(actions.cliamTask(taskId,user));
                    event.eventEmitter.emit(event.CLOSE_CLIAM_TASK_DIALOG);
                });
            }
        }];
        return (
            <CommonDialog title={title} body={body} buttons={buttons}/>
        );
    }
};
function select(state){
    return {cliamUsers:state.cliamUsers};
};
export default connect(select)(CliamTaskDialog);