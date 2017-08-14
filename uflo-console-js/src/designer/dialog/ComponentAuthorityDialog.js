/**
 * Created by Jacky.Gao on 2017-07-14.
 */
import {MsgBox} from 'flowdesigner';

export default class ComponentAuthorityDialog{
    constructor(){
        this.dialog=$(`<div class="modal fade" role="dialog" aria-hidden="true" style="z-index: 10000">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title">
                            组件权限信息
                        </h4>
                    </div>
                    <div class="modal-body"></div>
                    <div class="modal-footer">
                    </div>
                </div>
            </div>
        </div>`);
        const body=this.dialog.find('.modal-body'),footer=this.dialog.find(".modal-footer");
        this.initBody(body,footer);
    }
    initBody(body,footer){
        const nameGroup=$(`<div class="form-group uflo-group"><label>组件名称：</label></div>`);
        body.append(nameGroup);
        this.nameEditor=$(`<input type="text" class="form-control uflo-text-editor" style="width: 530px;">`);
        nameGroup.append(this.nameEditor);

        const valueGroup=$(`<div class="form-group uflo-group"><label>权限：</label></div>`);
        body.append(valueGroup);
        this.authoritySelect=$(`<select class="form-control uflo-text-editor" style="width: 530px;">
            <option value="Read">只读</option>
            <option value="ReadAndWrite">可操作</option>
            <option value="Hide">不可见</option>
        </select>`);
        valueGroup.append(this.authoritySelect);

        const saveButton=$(`<button type="button" class="btn btn-default">保存</button>`);
        footer.append(saveButton);
        saveButton.click(()=>{
            if(!this.nameEditor.val() || this.nameEditor.val()===''){
                MsgBox.alert('请输入组件名称');
                return;
            }
            if(!this.authoritySelect.val() || this.authoritySelect.val()===''){
                MsgBox.alert('请选择权限信息');
                return;
            }
            this.callback.call(this,{component:this.nameEditor.val(),authority:this.authoritySelect.val()});
            this.dialog.modal('hide');
        });
    }

    show(data,callback){
        this.dialog.modal('show');
        this.callback=callback;
        this.nameEditor.val(data.component);
        this.authoritySelect.val(data.authority);
    }
}