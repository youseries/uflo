/**
 * Created by Jacky.Gao on 2017-07-14.
 */
import {MsgBox} from 'flowdesigner';

export default class SaveDialog{
    constructor(){
        this.dialog=$(`<div class="modal fade" role="dialog" aria-hidden="true" style="z-index: 10000">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title">
                            保存流程模版文件
                        </h4>
                    </div>
                    <div class="modal-body"></div>
                    <div class="modal-footer">
                    </div>
                </div>
            </div>
        </div>`);
        this.providerMap=new Map();
        const body=this.dialog.find('.modal-body'),footer=this.dialog.find(".modal-footer");
        this.initBody(body,footer);
    }
    initBody(body,footer){
        this.providers=[];
        this.files=[];
        const _this=this;

        const nameGroup=$(`<div class="form-group"><label>流程模版名称：</label></div>`);
        body.append(nameGroup);
        const nameEditor=$(`<input type="text" placeholder="输入流程模版文件名" class="form-control" style="display: inline-block;width: 360px;">`);
        nameGroup.append(nameEditor);

        const providerGroup=$(`<div class="form-group"><label>模版文件来源：</label></div>`);
        body.append(providerGroup);
        this.providerSelect=$(`<select class="form-control" style="display: inline-block;width: 360px;"></select>`);
        providerGroup.append(this.providerSelect);
        this.providerSelect.change(function(){
            const value=$(this).val();
            let target=_this.loadTargetProvider(value);
            _this.loadFiles(target);
        });
        const table=$(`<table class="table table-bordered">
            <thead>
                <tr style="background: #eeeeee">
                    <td>文件名</td>
                    <td style="width: 150px">修改日期</td>
                    <td style="width: 50px">打开</td>
                    <td style="width: 50px">删除</td>
                </tr>
            </thead>
        </table>`);
        this.tableBody=$(`<tbody></tbody>`);
        table.append(this.tableBody);
        body.append(table);

        const saveButton=$(`<button type="button" class="btn btn-default"><i class="glyphicon glyphicon-floppy-disk"></i> 保存</button>`);
        footer.append(saveButton);
        saveButton.click(()=>{
            let exist=false;
            const name=nameEditor.val();
            if(!name){
                MsgBox.alert('请输入流程模版名称！');
                return;
            }
            for(let file of this.files){
                if(file.name===name || file.name===(name+'.uflo.xml')){
                    exist=true;
                    break;
                }
            }
            if(exist){
                MsgBox.alert(`流程模版文件[${name}]已存在!`);
                return;
            }
            const providerName=this.providerSelect.val();
            if(!providerName){
                MsgBox.alert('请选择模版文件存储目的地！');
                return;
            }
            const content=this.designer.toXML();
            if(!content){
                return;
            }
            const provider=this.providerMap.get(providerName);
            let fullName=provider.prefix+name;
            if(fullName.indexOf('.uflo.xml')===-1){
                fullName+='.uflo.xml';
            }
            const url=window._server+'/designer/saveFile';
            $.ajax({
                url,
                type:"POST",
                data:{fileName:fullName,content},
                success:()=>{
                    if(this.callback){
                        this.callback.call(this,fullName);
                    }
                    this.dialog.modal('hide');
                },
                error:()=>{
                    MsgBox.alert('保存失败！');
                }
            });
        });
    }
    loadTargetProvider(targetName){
        let target=null;
        for(let p of this.providers){
            if(p.name===targetName){
                target=p;
                break;
            }
        }
        if(target==null){
            throw `${targetName} provider not exist!`;
        }
        return target;
    }

    loadFiles(provider){
        const _this=this;
        this.tableBody.empty();
        const url=window._server+'/designer/loadProcessProviderFiles';
        $.ajax({
            url,
            data:{name:provider.name},
            type:'POST',
            success:function(files){
                _this.files=files;
                _this.buildTable(files,provider);
            },
            error:function(){
                MsgBox.alert(`加载${provider.name}中流程模版文件失败！`);
            }
        });
    }

    buildTable(files,provider){
        for(let file of files){
            const tr=$(`<tr><td>${file.name}</td><td>${file.updateDate}</td></tr>`);
            const openTD=$(`<td></td>`);
            tr.append(openTD);
            const openIcon=$(`<i class="glyphicon glyphicon-folder-open" style="color: green;cursor: pointer"></i>`);
            openTD.append(openIcon);
            openIcon.click(function(){
                const fullName=provider.prefix+file.name;
                const url=window._server+'/designer?_u='+fullName;
                window.open(url,'_self');
            });

            const delTD=$(`<td></td>`);
            tr.append(delTD);
            const delIcon=$(`<i class="glyphicon glyphicon-trash" style="color: red;cursor: pointer"></i>`);
            delTD.append(delIcon);
            const fullName=provider.prefix+file.name;
            delIcon.click(function(){
                MsgBox.confirm(`真的要删除文件[${file.name}]吗？`,function(){
                    const url=window._server+'/designer/deleteFile';
                    $.ajax({
                        url,
                        data:{fileName:fullName},
                        type:'POST',
                        success:function(){
                            tr.remove();
                        },
                        error:function(){
                            MsgBox.alert('删除操作失败！');
                        }
                    });
                });
            });
            this.tableBody.append(tr);
        }
    }

    show(designer,callback){
        this.designer=designer;
        this.callback=callback;
        this.dialog.modal('show');
        this.providerSelect.empty();
        this.providerMap.clear();
        const _this=this;
        const url=window._server+'/designer/loadProcessProviders';
        $.ajax({
            url,
            type:'POST',
            success:function(data){
                let index=0;
                _this.providers=data;
                for(let item of data){
                    _this.providerMap.set(item.name,item);
                    _this.providerSelect.append(`<option ${index===0 ? 'checked' : ''}>${item.name}</option>`);
                    if(index===0){
                        _this.loadFiles(item);
                    }
                    index++;
                }
            },
            error:function(){
                MsgBox.alert('加载流程模版存储提供者列表失败！');
            }
        });
    }
}