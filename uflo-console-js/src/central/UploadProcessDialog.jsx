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

export default class UploadProcessDialog extends Component{
    constructor(props){
        super(props);
        this.state={title:''};
    }
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_UPLOAD_PROCESS_DIALOG,(data)=>{
            $(ReactDOM.findDOMNode(this)).modal('show');
        });
        event.eventEmitter.on(event.CLOSE_UPLOAD_PROCESS_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    render(){
        const dispatch=this.props.dispatch;
        const url=window._server+"/deploy";
        const body = (
            <div>
                <form className="uploadProcessForm" action={url} target="_uploadProcessFrame" method="post" encType="multipart/form-data">
                    选择流程模版文件：
                    <input type="file" name="processFile" style={{width:'200px',height:'30px',color:'#2196F3',display:'inline-block'}}></input>
                </form>
                <iframe onLoad={function(e){
                    const result=e.target.contentWindow.document.body.innerText;
                    const json=JSON.parse(result);
                    if(json.result==='fail'){
                        MsgBox.alert("上传失败!");
                    }else{
                        MsgBox.alert("上传成功!");
                        dispatch(actions.loadProcess(window.processPageData.pageIndex,window.processPageData.pageSize));
                        event.eventEmitter.emit(event.CLOSE_UPLOAD_PROCESS_DIALOG);
                    }
                }} name="_uploadProcessFrame" style={{width:'0px',height:'0px'}}></iframe>
            </div>
        );
        const buttons=[{
            name:'上传',
            className:'btn btn-primary',
            icon:'glyphicon glyphicon-cloud-upload',
            click:function () {
                MsgBox.confirm("确认要上传当前选择的模版？", function () {
                        $(".uploadProcessForm").submit();
                });
            }
        }];
        return (
            <CommonDialog title='流程模版上传' body={body} buttons={buttons}/>
        );
    }
};