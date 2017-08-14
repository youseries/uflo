/**
 * Created by Jacky.Gao on 2017-07-10.
 */
import BaseTool from './BaseTool.js';
import SubprocessNode from './SubprocessNode.js';
import SubprocessVariableDialog from './dialog/SubprocessVariableDialog.js';

export default class SubprocessTool  extends BaseTool{
    getType(){
        return '子流程';
    }
    getIcon(){
        return `<i class="uflo uflo-subprocess" style="color:#737383"></i>`
    }
    newNode(){
        return new SubprocessNode();
    }
    getConfigs(){
        return {
            in:-1,
            out:-1,
            single:false
        };
    }
    buildSubprocessConfig(target){
        const group=$(`<div class="form-group uflo-group"></div>`);

        let type='name';
        if(target.subprocessKey){
            type='key';
        }else if(target.subprocessId){
            type="id";
        }

        const nameGroup=$('<div class="form-group uflo-group">');
        group.append(nameGroup);
        const nameRadio=$(`<label><input type="radio" ${type==='name' ? 'checked' : ''} name="subprocess_type_radio">子流程名称</label>`);
        nameGroup.append(nameRadio);
        const nameEditor=$(`<input type="text" value="${target.subprocessName || ''}" class="form-control uflo-text-editor" style="width: 290px;margin-left: 4px">`);
        nameGroup.append(nameEditor);
        nameEditor.change(function(){
            target.subprocessName=$(this).val();
            target.subprocessKey=null;
            target.subprocessId=null;
        });

        const keyGroup=$('<div class="form-group uflo-group">');
        group.append(keyGroup);
        const keyRadio=$(`<label><input type="radio" ${type==='key' ? 'checked' : ''} name="subprocess_type_radio">子流程KEY</label>`);
        keyGroup.append(keyRadio);
        const keyEditor=$(`<input type="text" value="${target.subprocessKey || ''}"  class="form-control uflo-text-editor" style="width: 290px;margin-left: 4px">`);
        keyGroup.append(keyEditor);
        keyEditor.change(function(){
            target.subprocessKey=$(this).val();
            target.subprocessName=null;
            target.subprocessId=null;
        });

        const idGroup=$('<div class="form-group uflo-group">');
        group.append(idGroup);
        const idRadio=$(`<label><input type="radio" ${type==='id' ? 'checked' : ''} name="subprocess_type_radio">子流程ID</label>`);
        idGroup.append(idRadio);
        const idEditor=$(`<input type="text" value="${target.subprocessId || ''}" class="form-control uflo-text-editor" style="width: 305px;margin-left: 4px">`);
        idGroup.append(idEditor);
        idEditor.change(function(){
            target.subprocessId=$(this).val();
            target.subprocessName=null;
            target.subprocessKey=null;
        });

        nameEditor.attr('readOnly',true);
        keyEditor.attr('readOnly',true);
        idEditor.attr('readOnly',true);
        if(type==='name'){
            nameEditor.attr('readOnly',false);
        }else if(type==='key'){
            keyEditor.attr('readOnly',false);
        }else if(type==='id'){
            idEditor.attr('readOnly',false);
        }

        nameRadio.children('input').click(function(){
            nameEditor.attr('readOnly',false);
            keyEditor.attr('readOnly',true);
            idEditor.attr('readOnly',true);
        });
        keyRadio.children('input').click(function(){
            nameEditor.attr('readOnly',true);
            keyEditor.attr('readOnly',false);
            idEditor.attr('readOnly',true);
        });
        idRadio.children('input').click(function(){
            nameEditor.attr('readOnly',true);
            keyEditor.attr('readOnly',true);
            idEditor.attr('readOnly',false);
        });

        const completeTaskGroup=$(`<div class="form-group uflo-group" style="margin-top: 10px"><label>完成子流程开始节点可能存在的任务：</label></div>`);
        group.append(completeTaskGroup);
        const yesRadio=$(`<span><input type="radio" ${target.completeStartTask ? 'checked' : ''} style="margin-left: 5px;" name="complete_subprocess_start_node_task_radio">是</span>`);
        completeTaskGroup.append(yesRadio);
        const noRadio=$(`<span><input type="radio" ${!target.completeStartTask ? 'checked' : ''} style="margin-left: 10px;" name="complete_subprocess_start_node_task_radio">否</span>`);
        completeTaskGroup.append(noRadio);
        yesRadio.children('input').click(function(){
            target.completeStartTask=true;
        });
        noRadio.children('input').click(function(){
            target.completeStartTask=false;
        });
        const tabContainer=$(`<div></div>`);
        group.append(tabContainer);
        const ul=$(`<ul class="nav nav-tabs">
            <li class="active"><a href="#in_subprocess_var_tab" data-toggle="tab">传入子流程变量配置</a></li>
            <li><a href="#out_subprocess_var_tab" data-toggle="tab">传回父流程变量配置</a></li>
        </ul>`);
        tabContainer.append(ul);
        const tab=$(`<div class="tab-content"></div>`);
        tabContainer.append(tab);
        tab.append(this.buildInVariableConfig(target));
        tab.append(this.buildOutVariableConfig(target));
        return group;
    }

    buildInVariableConfig(target){
        const group=$(`<div class="tab-pane fade in active" id="in_subprocess_var_tab"></div>`);
        const addButton=$(`<button type="button" class="btn btn-primary" style="float: right;height: 30px;padding-top: 3px;margin-bottom: 2px">添加</button>`);
        group.append(addButton);
        let inVariables=[];
        if(target.inVariables){
            inVariables=target.inVariables;
        }else{
            target.inVariables=inVariables;
        }
        const _this=this;
        const dialog=new SubprocessVariableDialog();
        addButton.click(()=>{
            dialog.show({},function(variable){
                inVariables.push(variable);
                _this.buildTableBody(body,variable,inVariables,dialog);
            });
        });
        const table=$(`<table  class="table table-bordered">
            <thead>
                <tr style="background: #fdfdfd"><td>父流程变量名</td><td>传入到子流程变量名</td><td style="width: 70px">操作</td></tr>
            </thead>
        </table>`);
        group.append(table);
        const body=$(`<tbody></tbody>`);
        table.append(body);
        for(let variable of inVariables){
            this.buildTableBody(body,variable,inVariables,dialog);
        }
        return group;
    }
    buildOutVariableConfig(target){
        const group=$(`<div class="tab-pane fade" id="out_subprocess_var_tab"></div>`);
        const addButton=$(`<button type="button" class="btn btn-primary" style="float: right;height: 30px;padding-top: 3px;margin-bottom: 2px">添加</button>`);
        group.append(addButton);
        let outVariables=[];
        if(target.outVariables){
            outVariables=target.outVariables;
        }else{
            target.outVariables=outVariables;
        }
        const _this=this;
        const dialog=new SubprocessVariableDialog();
        addButton.click(()=>{
            dialog.show({},function(variable){
                outVariables.push(variable);
                _this.buildTableBody(body,variable,outVariables,dialog);
            });
        });
        const table=$(`<table  class="table table-bordered">
            <thead>
                <tr style="background: #fdfdfd"><td>子流程变量名</td><td>传回父流程变量名</td><td style="width: 70px">操作</td></tr>
            </thead>
        </table>`);
        group.append(table);
        const body=$(`<tbody></tbody>`);
        table.append(body);
        for(let variable of outVariables){
            this.buildTableBody(body,variable,outVariables,dialog);
        }
        return group;
    }

    buildTableBody(body,variable,variables,dialog){
        const tr=$(`<tr></tr>`);
        body.append(tr);
        const intd=$(`<td>${variable.inParameterKey}</td>`);
        const outtd=$(`<td>${variable.outParameterKey}</td>`);
        tr.append(intd);
        tr.append(outtd);
        const optd=$(`<td></td>`);
        tr.append(optd);
        const edit=$(`<i class="glyphicon glyphicon-edit" style="color: green;font-size: 12pt;cursor: pointer"></i>`);
        optd.append(edit);
        edit.click(function(){
            dialog.show(variable,function(v){
                intd.html(v.inParameterKey);
                outtd.html(v.outParameterKey);
            });
        });
        const del=$(`<i class="glyphicon glyphicon-trash" style="color: red;font-size: 12pt;cursor: pointer;margin-left: 10px"></i>`);
        optd.append(del);
        del.click(function(){
            const index=variables.indexOf(variable);
            variables.splice(index,1);
            tr.remove();
        });
    }

    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$(`<div></div>`);
            g.append(_this.getCommonProperties(this));
            g.append(_this.buildSubprocessConfig(this));
            return g;
        }
    }
}