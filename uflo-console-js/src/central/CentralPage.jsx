/**
 * Created by Jacky.Gao on 2016/12/10.
 */
import React,{Component} from 'react';
import {connect} from 'react-redux';
import Grid from '../grid/component/Grid.jsx';
import Splitter from '../splitter/component/Splitter.jsx';
import Pagination from '../pagination/Pagination.jsx';
import * as actions from './action.js';
import Dialog from '../dialog/Dialog.jsx';
import CommonDialog from '../dialog/CommonDialog.jsx';
import  * as TodoEvent from '../todo/event.js';
import * as dialogEvent from '../dialog/event.js';
import StartProcessDialog from './StartProcessDialog.jsx';
import CompleteTaskDialog from './CompleteTaskDialog.jsx';
import CliamTaskDialog from './CliamTaskDialog.jsx';
import TaskJumpDialog from './TaskJumpDialog.jsx';
import UploadProcessDialog from './UploadProcessDialog.jsx';
import * as event from './event.js';

class CentralPage extends Component{
    componentDidMount(){
        TodoEvent.eventEmitter.on(TodoEvent.SHOW_TASK_DIAGRAM,function(data){
            const taskId=data.taskId,processId=data.processId,processInstanceId=data.processInstanceId,title=data.title;
            let url=window._server+"/diagram";
            if(taskId){
                url+="?taskId="+taskId;
            }
            if(processId){
                url+="?processId="+processId;
            }
            if(processInstanceId){
                url+="?processInstanceId="+processInstanceId;
            }
            dialogEvent.eventEmitter.emit(dialogEvent.SHOW_DIALOG,{title,url});
        });
    }
    render(){
        const {process,processInstance,task,historyProcessInstance,historyTask,dispatch}=this.props;
        window.processPageData=process;
        window.processInstancePageData=process;
        const processHeaders=[
            {id:'process-id',name:'id',label:'模版ID',width:'60px'},
            {id:'process-name',name:'name',label:'流程模版名称',filterable:true,width:'110px',filterCallback:function(data){
                dispatch(actions.loadProcess(window.processPageData.pageIndex,window.processPageData.pageSize,data));
            }},
            {id:'process-key',name:'key',label:'关键字'},
            {id:'process-version',name:'version',label:'版本',width:'50px'},
            {id:'process-createDate',name:'createDate',label:'创建日期',dateFormat:'yyyy-MM-dd HH:mm:ss',width:'155px'}
        ];
        const processInstanceHeaders=[
            {id:'processInstance-id',name:'id',label:'流程实例ID',width:'100px'},
            {id:'processInstance-createDate',name:'createDate',label:'创建日期',dateFormat:'yyyy-MM-dd HH:mm:ss',width:'160px'},
            {id:'processInstance-currentNode',name:'currentNode',label:'所在节点',width:'180px'},
            {id:'processInstance-promoter',name:'promoter',label:'发起人'}
        ];
        const historyProcessInstanceHeaders=[
            {id:'historyProcessInstance-id',name:'id',label:'历史流程实例ID',width:'120px'},
            {id:'historyProcessInstance-createDate',name:'createDate',label:'创建日期',dateFormat:'yyyy-MM-dd HH:mm:ss',width:'160px'},
            {id:'historyProcessInstance-endDate',name:'endDate',label:'完成日期',dateFormat:'yyyy-MM-dd HH:mm:ss',width:'160px'},
            {id:'historyProcessInstance-promoter',name:'promoter',label:'发起人'}
        ];
        const historyTaskHeaders=[
            {id:'historyTask-id',name:'id',label:'历史任务ID',width:'90px'},
            {id:'historyTask-taskName',name:'taskName',label:'名称',filterable:true},
            {id:'historyTask-assignee',name:'assignee',label:'处理人',width:'110px',filterable:true},
            {id:'historyTask-type',name:'type',label:'类型',width:'100px',width:'60px',mapping:{Normal:'普通',Countersign:'会签',Participative:'竞争'}},
            {id:'historyTask-createDate',name:'createDate',label:'创建日期',width:'150px',dateFormat:'yyyy-MM-dd HH:mm:ss'},
            {id:'historyTask-endDate',name:'endDate',label:'结束日期',width:'150px',dateFormat:'yyyy-MM-dd HH:mm:ss'}
        ];
        const taskHeaders=[
            {id:'task-id',name:'id',label:'任务ID',width:'80px'},
            {id:'task-taskName',name:'taskName',label:'名称',filterable:true},
            {id:'task-assignee',name:'assignee',label:'处理人',width:'110px',filterable:true},
            {id:'task-type',name:'type',label:'类型',width:'100px',width:'60px',mapping:{Normal:'普通',Countersign:'会签',Participative:'竞争'}},
            {id:'task-createDate',name:'createDate',label:'创建日期',width:'150px',dateFormat:'yyyy-MM-dd HH:mm:ss'}
        ];
        const processCol={
            width:'100px',
            operations:[
                {
                    label:'删除当前流程模版',
                    icon:'glyphicon glyphicon-remove',
                    style:{fontSize:'18px',color:'#F44336',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        bootbox.confirm("真的要删除流程模版["+rowData.name+"]吗？",function(result){
                            if(!result)return;
                            dispatch(actions.deleteProcess(window.processPageData.pageIndex,window.processPageData.pageSize,rowData.id));
                        });
                    }
                },
                {
                    label:'查看当前流程模版的流程图',
                    icon:'glyphicon glyphicon-eye-open',
                    style:{fontSize:'18px',color:'#3949AB',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        TodoEvent.eventEmitter.emit(TodoEvent.SHOW_TASK_DIAGRAM,{processId:rowData.id,title:"流程模版["+rowData.name+"]的流程图"});
                    }
                },
                {
                    label:'开启当前流程模版新的流程实例',
                    icon:'glyphicon glyphicon-play-circle',
                    style:{fontSize:'18px',color:'#2E7D32',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        event.eventEmitter.emit(event.OPEN_START_PROCESS_DIALOG, {
                            pageIndex: window.processInstancePageData.pageIndex,
                            pageSize: window.processInstancePageData.pageSize,
                            processId: rowData.id,
                            data:[],
                            title:"开启流程模版["+rowData.name+"]的新流程实例"
                        });
                    }
                }
            ]
        };
        const processInstanceCol={
            width:'70px',
            operations:[
                {
                    label:'删除当前流程实例',
                    icon:'glyphicon glyphicon-remove',
                    style:{fontSize:'18px',color:'#F44336',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        bootbox.confirm("真的要删除流程实例["+rowData.id+"]吗？",function(result){
                            if(!result)return;
                            dispatch(actions.deleteProcessInstance(window.processInstancePageData.pageIndex, window.processInstancePageData.pageSize, window.currentProcessId, rowData.id));
                        });
                    }
                }
            ]
        };
        const historyTaskCol={
            width:'50px',
            operations:[
                {
                    label:'查看当前任务的流程图',
                    icon:'glyphicon glyphicon-eye-open',
                    style:{fontSize:'18px',color:'#3949AB',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        TodoEvent.eventEmitter.emit(TodoEvent.SHOW_TASK_DIAGRAM,{taskId:rowData.taskId,title:"历史任务"+rowData.taskName+"的流程图"});
                    }
                }
            ]
        };
        const taskCol={
            width:'120px',
            operations:[
                {
                    label:'完成当前任务',
                    icon:'glyphicon glyphicon-ok-sign',
                    style:{fontSize:'18px',color:'#8BC34A',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        if(rowData.type==='Participative' && (!rowData.assignee || rowData.assignee.length<1)){
                            bootbox.alert("当前任务是[竞争]类型，请先领取再进行此操作！");
                        }else{
                            event.eventEmitter.emit(event.OPEN_COMPLETE_TASK_DIALOG, {
                                taskId: rowData.id,
                                title:"完成任务["+rowData.taskName+"]",
                                data:[]
                            });
                        }
                    }
                },
                {
                    label:'领取当前任务',
                    icon:'glyphicon glyphicon-upload',
                    style:{fontSize:'18px',color:'#9C27B0',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        if(rowData.type==='Participative'){
                            if(rowData.assignee && rowData.assignee.length>1){
                                bootbox.alert("任务已被["+rowData.assignee+"]领取，不能进行此操作！");
                            }else{
                                event.eventEmitter.emit(event.OPEN_CLIAM_TASK_DIALOG,{taskId:rowData.id,title:"领取任务["+rowData.taskName+"]"});
                            }
                        }else{
                            bootbox.alert("当前任务类型不是[竞争]类型，不能领取！");
                        }
                    }
                },
                {
                    label:'跳转到其它任务节点',
                    icon:'glyphicon glyphicon-random',
                    style:{fontSize:'18px',color:'#009688',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        event.eventEmitter.emit(event.OPEN_JUMP_TASK_DIALOG,{taskId:rowData.id,title:"任务["+rowData.taskName+"]跳转"});
                    }
                },
                {
                    label:'查看当前任务的流程图',
                    icon:'glyphicon glyphicon-eye-open',
                    style:{fontSize:'18px',color:'#3949AB',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        TodoEvent.eventEmitter.emit(TodoEvent.SHOW_TASK_DIAGRAM,{taskId:rowData.id,title:"任务"+rowData.taskName+"的流程图"});
                    }
                }
            ]
        };
        return (
            <Splitter  orientation='vertical' position='50%'>
                <div>
                    <button className="btn btn-default" style={{margin:'4px'}} onClick={function(){
                        event.eventEmitter.emit(event.OPEN_UPLOAD_PROCESS_DIALOG);
                    }}><i className="glyphicon glyphicon-cloud-upload"></i> 上传流程模版</button>
                    <Grid headers={processHeaders} rows={process.result} operationConfig={processCol} rowClick={function(rowData){
                        window.currentProcessId=rowData.id;
                        dispatch(actions.loadProcessInstance(1,10,rowData.id));
                        dispatch(actions.loadHistoryProcessInstance(1,10,rowData.id));
                    }}/>
                    <Pagination pageSize={process.pageSize} pageIndex={process.pageIndex} total={process.total} enableSetPageSize={true} changePageCallback={(pageInfo)=>{
                            dispatch(actions.loadProcess(pageInfo.pageIndex,pageInfo.pageSize));
                        }}/>
                    <StartProcessDialog dispatch={dispatch}/>
                    <Dialog/>
                </div>
                <div>
                    <ul className="nav nav-tabs">
                        <li className="active">
                            <a href="#currentTab" data-toggle="tab">运行中实例与任务</a>
                        </li>
                        <li>
                            <a href="#historyTab" data-toggle="tab">历史实例与任务</a>
                        </li>
                    </ul>
                    <div className="tab-content">
                        <div id="currentTab" className="tab-pane fade in active">
                            <div>
                                <Grid headers={processInstanceHeaders} rows={processInstance.result} operationConfig={processInstanceCol} rowClick={function(rowData){
                                    window.currentProcessInstanceId=rowData.id;
                                    dispatch(actions.loadTask(rowData.id))
                                }}/>
                                <Pagination pageSize={processInstance.pageSize} pageIndex={processInstance.pageIndex} total={processInstance.total} enableSetPageSize={true} changePageCallback={(pageInfo)=>{
                                    dispatch(actions.loadProcessInstance(pageInfo.pageIndex,pageInfo.pageSize,window.currentProcessId));
                                }}/>
                            </div>
                            <div style={{marginTop:'10px'}}>
                                <Grid headers={taskHeaders} rows={task} operationConfig={taskCol}/>
                                <CompleteTaskDialog dispatch={dispatch}/>
                                <CliamTaskDialog dispatch={dispatch}/>
                                <TaskJumpDialog dispatch={dispatch}/>
                                <UploadProcessDialog dispatch={dispatch}/>
                            </div>
                        </div>
                        <div id="historyTab" className="tab-pane fade">
                            <div>
                                <Grid headers={historyProcessInstanceHeaders} rows={historyProcessInstance.result} rowClick={function(rowData){
                                    dispatch(actions.loadHistoryTask(rowData.id))
                                }}/>
                                <Pagination pageSize={historyProcessInstance.pageSize} pageIndex={historyProcessInstance.pageIndex} total={historyProcessInstance.total} enableSetPageSize={true} changePageCallback={(pageInfo)=>{
                                    dispatch(actions.loadHistoryProcessInstance(pageInfo.pageIndex,pageInfo.pageSize,window.currentProcessId));
                                }}/>
                            </div>
                            <div style={{marginTop:'10px'}}>
                                <Grid headers={historyTaskHeaders} rows={historyTask} operationConfig={historyTaskCol}/>
                            </div>
                        </div>
                    </div>
                </div>
            </Splitter>
        );
    }
};
function select(state){
    return {
        process:state.process,
        processInstance:state.processInstance,
        task:state.task,
        historyProcessInstance:state.historyProcessInstance,
        historyTask:state.historyTask
    };
}
export default connect(select)(CentralPage);