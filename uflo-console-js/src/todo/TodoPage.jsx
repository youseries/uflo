/**
 * Created by Jacky.Gao on 2016/12/7.
 */
import React,{Component} from 'react';
import {connect} from 'react-redux';
import Grid from '../grid/component/Grid.jsx';
import * as event from './event.js';
import Pagination from '../pagination/Pagination.jsx';
import * as actions from './action.js';
import Dialog from '../dialog/Dialog.jsx';
import * as dialogEvent from '../dialog/event.js';

class TodoPage extends Component{
    componentDidMount(){
        event.eventEmitter.on(event.SHOW_TASK_DIAGRAM,function(data){
            const taskId=data.taskId,taskName=data.taskName;
            const url=window._server+"/diagram?taskId="+taskId;
            const title="任务["+taskName+"]流程图";
            dialogEvent.eventEmitter.emit(dialogEvent.SHOW_DIALOG,{title,url,executeCloseCallback:false});
        });
        event.eventEmitter.on(event.OPEN_TASK_PAGE,function(data){
            let url=data.url;
            if(!url){
                alert("任务["+data.taskName+"]未定义处理任务的URL");
                return;
            }
            if(url.length<2) {
                alert("任务[" + data.taskName + "]定义处理任务的URL[" + url + "]不合法");
                return;
            }
            if(url){
                let prefix=url.substring(0,1);
                if(prefix!=="/"){
                    url="/"+url;
                }
            }
            url = window._contextPath + url;
            url+="?taskId=" + data.id + "&taskName="+ data.taskName + "&businessId=" + data.businessId + "&processInstanceId=" + data.processInstanceId + "&processId=" + data.processId + "&rootProcessInstanceId=" + data.rootProcessInstanceId;
            url=encodeURI(encodeURI(url));
            const title="处理任务["+data.taskName+"]";
            dialogEvent.eventEmitter.emit(dialogEvent.SHOW_DIALOG,{title,url,executeCloseCallback:true});
        });
        const dispatch=this.props.dispatch;
        event.eventEmitter.on(event.CLIAM_TASK,function(data){
            bootbox.confirm("你确定要领取任务["+data.taskName+"]?",function(){
                dispatch(actions.claimTask(window.cliamList.pageIndex,window.cliamList.pageSize,data.id));
            });
        });
    }
    render(){
        const {todoList,cliamList,historyList,dispatch}=this.props;
        window.todoList=todoList;
        window.cliamList=cliamList;
        window.historyList=historyList;

        const todoListHeaders=[
            {id:'todo-id',name:'id',label:'任务编号',width:'80px'},
            {id:'todo-taskName',name:'taskName',label:'任务名称',filterable:true,width:'200px',filterCallback:function(data){
                dispatch(actions.loadTodoList(window.todoList.pageIndex,window.todoList.pageSize,data));
            }},
            {id:'todo-subject',name:'subject',label:'标题',width:'300px'},
            {id:'todo-description',name:'description',label:'描述'},
            {id:'todo-type',name:'type',label:'类型',width:'100px',mapping:{Normal:'普通任务',Countersign:'会签任务',Participative:'领取的任务'}},
            {id:'todo-createDate',name:'createDate',label:'创建日期',dateFormat:'yyyy-MM-dd HH:mm:ss',width:'160px'}
        ];
        const cliamListHeaders=[
            {id:'todo-id',name:'id',label:'任务编号',width:'80px'},
            {id:'todo-taskName',name:'taskName',label:'任务名称',filterable:true,width:'200px',filterCallback:function(data){
                dispatch(actions.loadCliamList(window.cliamList.pageIndex,window.cliamList.pageSize,data));
            }},
            {id:'todo-subject',name:'subject',label:'标题',width:'300px'},
            {id:'todo-description',name:'description',label:'描述'},
            {id:'todo-createDate',name:'createDate',label:'创建日期',dateFormat:'yyyy-MM-dd HH:mm:ss',width:'160px'}
        ];
        const historyListHeaders=[
            {id:'todo-taskName',name:'taskName',label:'任务名称',filterable:true,width:'200px',filterCallback:function(data){
                dispatch(actions.loadHistory(window.historyList.pageIndex,window.historyList.pageSize,data));
            }},
            {id:'todo-subject',name:'subject',label:'标题',width:'300px'},
            {id:'todo-description',name:'description',label:'描述'},
            {id:'todo-createDate',name:'createDate',label:'创建日期',dateFormat:'yyyy-MM-dd HH:mm:ss',width:'160px'},
            {id:'todo-endDate',name:'endDate',label:'完成日期',dateFormat:'yyyy-MM-dd HH:mm:ss',width:'160px'}
        ];
        const todoOperationCol={
            width:'80px',
            operations:[
                {
                    label:'查看流程图',
                    icon:'glyphicon glyphicon-eye-open',
                    style:{fontSize:'18px',color:'#4CAF50',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        event.eventEmitter.emit(event.SHOW_TASK_DIAGRAM,{taskId:rowData.id,taskName:rowData.taskName});
                    }
                },
                {
                    label:'处理该任务',
                    icon:'glyphicon glyphicon-check',
                    style:{fontSize:'18px',color:'#9C27B0',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        event.eventEmitter.emit(event.OPEN_TASK_PAGE,rowData);
                    }
                }
            ]
        };
        const cliamOperationCol={
            width:'80px',
            operations:[
                {
                    label:'查看流程图',
                    icon:'glyphicon glyphicon-eye-open',
                    style:{fontSize:'18px',color:'#4CAF50',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        event.eventEmitter.emit(event.SHOW_TASK_DIAGRAM,{taskId:rowData.id,taskName:rowData.taskName});
                    }
                },
                {
                    label:'领取该任务',
                    icon:'glyphicon glyphicon-ok-sign',
                    style:{fontSize:'18px',color:'#9C27B0',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        event.eventEmitter.emit(event.CLIAM_TASK,rowData);
                    }
                }
            ]
        };
        const historyOperationCol={
            width:'80px',
            operations:[
                {
                    label:'查看流程图',
                    icon:'glyphicon glyphicon-eye-open',
                    style:{fontSize:'18px',color:'#4CAF50',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        event.eventEmitter.emit(event.SHOW_TASK_DIAGRAM,{taskId:rowData.taskId,taskName:rowData.taskName});
                    }
                }
            ]
        };
        return (
            <div>
                <ul className="nav nav-tabs">
                    <li className="active">
                        <a href="#todolist" data-toggle="tab" onClick={function(){
                            dispatch(actions.loadTodoList(window.todoList.pageIndex,window.todoList.pageSize));
                        }}>待处理的任务</a>
                    </li>
                    <li>
                        <a href="#cliamlist" data-toggle="tab" onClick={function(){
                            dispatch(actions.loadCliamList(window.cliamList.pageIndex,window.cliamList.pageSize));
                        }
                        }>待领取的任务</a>
                    </li>
                    <li>
                        <a href="#historylist" data-toggle="tab" onClick={function(){
                            dispatch(actions.loadHistory(window.historyList.pageIndex,window.historyList.pageSize));
                        }}>处理过的任务</a>
                    </li>
                </ul>
                <div className="tab-content">
                    <div className="tab-pane fade in active" id="todolist">
                        <Grid headers={todoListHeaders} rows={todoList.result} operationConfig={todoOperationCol}/>
                        <Pagination pageSize={todoList.pageSize} pageIndex={todoList.pageIndex} total={todoList.total} enableSetPageSize={true} changePageCallback={(pageInfo)=>{
                            dispatch(actions.loadTodoList(pageInfo.pageIndex,pageInfo.pageSize));
                        }}/>
                    </div>
                    <div className="tab-pane fade" id="cliamlist">
                        <Grid headers={cliamListHeaders} rows={cliamList.result} operationConfig={cliamOperationCol}/>
                        <Pagination pageSize={cliamList.pageSize} pageIndex={cliamList.pageIndex} total={cliamList.total} enableSetPageSize={true}  changePageCallback={(pageInfo)=>{
                            dispatch(actions.loadCliamList(pageInfo.pageIndex,pageInfo.pageSize));
                        }}/>
                    </div>
                    <div className="tab-pane fade" id="historylist">
                        <Grid headers={historyListHeaders} rows={historyList.result} operationConfig={historyOperationCol}/>
                        <Pagination pageSize={historyList.pageSize} pageIndex={historyList.pageIndex} total={historyList.total} enableSetPageSize={true}  changePageCallback={(pageInfo)=>{
                            dispatch(actions.loadHistory(pageInfo.pageIndex,pageInfo.pageSize));
                        }}/>
                    </div>
                </div>
                <Dialog callbackOnClose={function(){
                        dispatch(actions.loadTodoList(window.todoList.pageIndex,window.todoList.pageSize));
                }}/>
            </div>
        );
    }
};

function select(state){
    return {
        todoList:state.todo.data,
        cliamList:state.cliam.data,
        historyList:state.history.data
    };
};

export default connect(select)(TodoPage);