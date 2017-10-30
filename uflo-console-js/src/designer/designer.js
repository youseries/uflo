/**
 * Created by Jacky.Gao on 2017-07-05.
 */
import './css/uflo.css';
import './css/iconfont.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import StartTool from './StartTool.js';
import TaskTool from './TaskTool.js';
import CountersignTaskTool from './CountersignTaskTool.js';
import ActionTool from './ActionTool.js';
import SubprocessTool from './SubprocessTool.js';
import DecisionTool from './DecisionTool.js';
import ForkTool from './ForkTool.js';
import ForeachTool from './ForeachTool.js';
import JoinTool from './JoinTool.js';
import EndTool from './EndTool.js';
import EndTerminalTool from './EndTerminalTool.js';
import UfloDesigner from './UfloDesigner.js';

import OpenDialog from './dialog/OpenDialog.js';
import SaveDialog from './dialog/SaveDialog.js';

import {getParameter} from '../Utils.js';
import {MsgBox} from 'flowdesigner';

$(document).ready(function(){
    const designer=new UfloDesigner('container');
    designer.addTool(new StartTool());
    designer.addTool(new TaskTool());
    designer.addTool(new CountersignTaskTool());
    designer.addTool(new ActionTool());
    designer.addTool(new SubprocessTool());
    designer.addTool(new DecisionTool());
    designer.addTool(new ForkTool());
    designer.addTool(new ForeachTool());
    designer.addTool(new JoinTool());
    designer.addTool(new EndTool());
    designer.addTool(new EndTerminalTool());
    buildButtons(designer);
    designer.buildDesigner();
    loadFile(designer);
});

function loadFile(designer){
    const name=getParameter('p');
    if(!name){
        return;
    }
    designer.setInfo(name);
    designer.processFile=name;
    const url=window._server+"/designer/openFile";
    $.ajax({
        url,
        type:"POST",
        data:{name},
        success:(process)=>{
            designer.fromJson(process);
        },
        error:()=>{
            MsgBox.alert(`加载流程模版文件${name}失败！`);
        }
    });
};

function buildButtons(designer){
    const openDialog=new OpenDialog();
    designer.addButton({
        icon:'<i class="uflo uflo-open"></i>',
        tip:'打开流程模版文件',
        click:function(){
            openDialog.show();
        }
    });
    const saveDialog=new SaveDialog();
    designer.addButton({
        icon:'<i class="uflo uflo-save"></i>',
        tip:'保存流程模版文件',
        click:function(){
            const content=designer.toXML();
            if(!content){
                return;
            }
            if(!designer.processFile){
                saveDialog.show(designer,function(fileName){
                    designer.processFile=fileName;
                    designer.setInfo(fileName);
                });
            }else{
                const url=window._server+'/designer/saveFile';
                const name=designer.processFile;
                $.ajax({
                    url,
                    type:"POST",
                    data:{fileName:name,content},
                    success:()=>{
                        MsgBox.alert('保存成功！');
                    },
                    error:()=>{
                        MsgBox.alert('保存失败！');
                    }
                });
            }
        }
    });
    designer.addButton({
        icon:'<i class="uflo uflo-saveas"></i>',
        tip:'另存流程模版文件',
        click:function(){
            const content=designer.toXML();
            if(!content){
                return;
            }
            saveDialog.show(designer,function(fileName){
                designer.processFile=fileName;
                designer.setInfo(fileName);
            });
        }
    });
    designer.addButton({
        icon:'<i class="uflo uflo-deploy"></i>',
        tip:'部署流程模版',
        click:function(){
            const content=designer.toXML();
            if(!content){
                return;
            }
            MsgBox.confirm('确实要部署当前流程模版到当前应用中吗？',function(){
                const url=window._server+'/designer/deploy';
                $.ajax({
                    url,
                    data:{content},
                    success:function(){
                        MsgBox.alert('部署成功！');
                    },
                    error:function(){
                        MsgBox.alert('部署失败！');
                    }
                });
            });
        }
    })
}

