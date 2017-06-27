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

class CompleteTaskDialog extends Component{
    constructor(props){
        super(props);
        this.state={title:'',data:[]};
    }
    componentDidMount(){
        const dispatch=this.props.dispatch;
        event.eventEmitter.on(event.OPEN_COMPLETE_TASK_DIALOG,(data)=>{
            $(ReactDOM.findDOMNode(this)).modal('show');
            this.setState(data);
            const taskId=data.taskId;
            dispatch(actions.loadSequenceFlows(taskId));
        });
        event.eventEmitter.on(event.CLOSE_COMPLETE_TASK_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    render(){
        const {title,taskId,data}=this.state;
        const {dispatch,flows}=this.props;
        const headers=[
            {id:'v-key',name:'key',label:'变量名',editable:true,width:'160px'},
            {id:'v-value',name:'value',label:'值',editable:true,width:'160px'}
        ];
        const _this=this;
        const operationConfig={
            width:'80px',
            operations:[
                {
                    label:'删除',
                    icon:'glyphicon glyphicon-trash',
                    style:{fontSize:'18px',color:'#d9534f',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex){
                        data.splice(rowIndex,1);
                        _this.setState({data});
                    }
                }
            ]
        };
        const flowOptions=[];
        flows.forEach(function(flow,index){
            flowOptions.push(
                <option key={index}>{flow.name}</option>
            );
        });
        if(flowOptions.length===0){
            flowOptions.push(
                <option key="default" value="">默认</option>
            );
        }
        const body = (
            <div>
                <div style={{margin:'2px'}}>
                    <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                        <button className="btn btn-primary" type="button" onClick={(e)=>{
                            data.push({});
                            _this.setState({data});
                        }}><i className="glyphicon glyphicon-plus-sign"></i> 添加流程变量</button>
                    </div>
                </div>
                <Grid headers={headers} rows={data} dispatch={dispatch} operationConfig={operationConfig}/>
                <div style={{marginTop:'10px'}}>
                    选择流转的路径：
                    <select className="form-control flowNameSelect" style={{display:"inline-block",width:"330px",marginTop:'15px'}}>
                        {flowOptions}
                    </select>
                </div>
            </div>
        );
        const buttons=[{
            name:'完成这个任务',
            className:'btn btn-primary',
            icon:'glyphicon glyphicon-ok-sign',
            click:function () {
                MsgBox.confirm("真的要完成这个任务吗？",function(){
                    const variables=JSON.stringify(_this.state.data);
                    const flowName=$(".flowNameSelect").val();
                    dispatch(actions.completeTask(taskId,flowName,variables));
                    event.eventEmitter.emit(event.CLOSE_COMPLETE_TASK_DIALOG);
                });
            }
        }];
        return (
            <CommonDialog title={title} body={body} buttons={buttons}/>
        );
    }
};
function select(state){
    return {flows:state.seqFlows};
};
export default connect(select)(CompleteTaskDialog);