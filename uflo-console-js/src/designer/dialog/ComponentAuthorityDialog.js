/**
 * Created by Jacky.Gao on 2017-07-14.
 */
import {MsgBox} from 'flowdesigner';

export default class CustomDataDialog{
    constructor(){
        this.dialog=$(`<div class="modal fade" role="dialog" aria-hidden="true" style="z-index: 10000">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title">
                            自定义数据
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
        const nameGroup=$(`<div class="form-group uflo-group"><label>键：</label></div>`);
        body.append(nameGroup);
        this.nameEditor=$(`<input type="text" class="form-control uflo-text-editor" style="width: 530px;">`);
        nameGroup.append(this.nameEditor);

        const valueGroup=$(`<div class="form-group uflo-group"><label>值：</label></div>`);
        body.append(valueGroup);
        this.valueEditor=$(`<input type="text" class="form-control uflo-text-editor" style="width: 530px;">`);
        valueGroup.append(this.valueEditor);

        const saveButton=$(`<button type="button" class="btn btn-default">保存</button>`);
        footer.append(saveButton);
        saveButton.click(()=>{
            if(!this.nameEditor.val() || this.nameEditor.val()===''){
                MsgBox.alert('请输入键');
                return;
            }
            if(!this.valueEditor.val() || this.valueEditor.val()===''){
                MsgBox.alert('请输入值');
                return;
            }
            this.callback.call(this,{key:this.nameEditor.val(),value:this.valueEditor.val()});
            this.dialog.modal('hide');
        });
    }

    show(data,callback){
        this.dialog.modal('show');
        this.callback=callback;
        this.nameEditor.val(data.key);
        this.valueEditor.val(data.value);
    }
}