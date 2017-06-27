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

class TaskJumpDialog extends Component{
    constructor(props){
        super(props);
        this.state={title:''};
    }
    componentDidMount(){
        const dispatch=this.props.dispatch;
        event.eventEmitter.on(event.OPEN_JUMP_TASK_DIALOG,(data)=>{
            $(ReactDOM.findDOMNode(this)).modal('show');
            this.setState(data);
            const taskId=data.taskId;
            dispatch(actions.loadJumpTasks(taskId));
        });
        event.eventEmitter.on(event.CLOSE_JUMP_TASK_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    render(){
        const {title,taskId}=this.state;
        const {dispatch,taskNames}=this.props;

        const taskOptions=[];
        taskNames.forEach(function(p,index){
            taskOptions.push(
                <option key={index}>{p.name}</option>
            );
        });
        const body = (
            <div>
                选择要跳转的目标节点：
                <select className="form-control jumpNodeSelect" style={{display:"inline-block",width:"330px",marginTop:'15px'}}>
                    {taskOptions}
                </select>
            </div>
        );
        const buttons=[{
            name:'确认',
            className:'btn btn-primary',
            icon:'glyphicon glyphicon-ok-sign',
            click:function () {
                const node=$(".jumpNodeSelect").val();
                if(!node || node.length<1){
                    bootbox.alert("请选择目标节点！");
                    return;
                }
                MsgBox.confirm("真的要跳转到["+node+"]节点吗？",function(){
                    dispatch(actions.jumpTask(taskId,node));
                    event.eventEmitter.emit(event.CLOSE_JUMP_TASK_DIALOG);
                });
            }
        }];
        return (
            <CommonDialog title={title} body={body} buttons={buttons}/>
        );
    }
};
function select(state){
    return {taskNames:state.jumpTasks};
};
export default connect(select)(TaskJumpDialog);