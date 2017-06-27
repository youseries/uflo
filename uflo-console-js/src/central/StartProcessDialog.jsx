/**
 * Created by Jacky.Gao on 2016/12/11.
 */
import React,{Component} from 'react';
import ReactDOM from 'react-dom';
import CommonDialog from '../dialog/CommonDialog.jsx';
import * as event from './event.js';
import Grid from '../grid/component/Grid.jsx';
import * as actions from './action.js';
import {MsgBox} from 'flowdesigner';

export default class StartProcessDialog extends Component{
    constructor(props){
        super(props);
        this.state={title:'',data:[]};
    }
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_START_PROCESS_DIALOG,(data)=>{
            $(ReactDOM.findDOMNode(this)).modal('show');
            this.setState(data);
        });
        event.eventEmitter.on(event.CLOSE_START_PROCESS_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    render(){
        const {title,pageIndex,pageSize,processId,data}=this.state;
        const {dispatch}=this.props;
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
            </div>
        );
        const buttons=[{
            name:'开启流程实例',
            className:'btn btn-primary',
            icon:'glyphicon glyphicon-play',
            click:function () {
                MsgBox.confirm("真的要开启新的实例吗？",function(){
                    var variables=JSON.stringify(_this.state.data);
                    dispatch(actions.startProcess(pageIndex,pageSize,processId,variables));
                    event.eventEmitter.emit(event.CLOSE_START_PROCESS_DIALOG);
                });
            }
        }];
        return (
            <CommonDialog title={title} body={body} buttons={buttons}/>
        );
    }
};