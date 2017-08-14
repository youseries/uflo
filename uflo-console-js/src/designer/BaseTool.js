/**
 * Created by jacky on 2016/7/18.
 */
import {Tool} from 'flowdesigner';
import EventSelectDialog from './dialog/EventSelectDialog.js';

export default class BaseTool extends Tool{
    getCommonProperties(target){
        const group=$(`<fieldset style="padding: 10px;border:solid 1px #dddddd;border-radius: 8px;margin-bottom: 10px;margin-top: 10px">
        <legend style="width: auto;margin-bottom: 1px;border-bottom:none;font-size: inherit;color: #4b4b4b;">基本属性</legend></fieldset>`);

        const displayNameGroup=$(`<div class="uflo-group form-group"><label>显示名称：</label></div>`);
        group.append(displayNameGroup);
        const displayNameEditor=$(`<input type="text" class="uflo-text-editor form-control" value="${target.label || ''}" style="width: 280px">`);
        displayNameGroup.append(displayNameEditor);
        displayNameEditor.change(function(){
            const value=$(this).val();
            if(!value || value===""){
                target.text.attr('text',target.name);
            }else{
                target.text.attr('text',value);
            }
            target.label=value;
        });
        if(target.label){
            target.text.attr('text',target.label);
        }

        const eventGroup=$(`<div class="uflo-group form-group" title="一个实现了com.bstek.uflo.process.handler.NodeEventHandler接口配置在Spring中bean的id，一旦配置在流程进入及离开该节点时会触发这个实现类"><label>事件Bean：</label></div>`);
        const eventEditor=$(`<input type="text" class="uflo-text-editor form-control" value="${target.eventHandlerBean || ''}" style="width: 215px">`);
        eventEditor.change(function(){
            target.eventHandlerBean=$(this).val();
        });
        eventGroup.append(eventEditor);

        const selectButton=$(`<button type="button" class="btn btn-default" style="height: 26px;padding-top: 2px;margin-left: 2px">选择</button>`);
        eventGroup.append(selectButton);
        const selectDialog=new EventSelectDialog('一个实现了com.bstek.uflo.process.handler.NodeEventHandler接口配置在Spring中bean的id');
        selectButton.click(()=>{
            selectDialog.show('NodeEventHandler',function(beanId){
                eventEditor.val(beanId);
                target.eventHandlerBean=beanId;
            });
        });

        group.append(eventGroup);

        const descGroup=$(`<div class="uflo-group form-group"  style="margin-bottom: 0px !important;"><label style="vertical-align: top">描述：</label></div>`);
        const descEditor=$(`<textarea rows="2" class="form-control" style="display: inline-block;width: 306px">${target.description || ''}</textarea>`);
        descGroup.append(descEditor);
        group.append(descGroup);
        descEditor.change(function(){
            target.description=$(this).val();
        });
        return group;
    }
}