/**
 * Created by Jacky.Gao on 2017-07-10.
 */
import BaseTool from './BaseTool.js';
import ForeachNode from './ForeachNode.js';
import EventSelectDialog from './dialog/EventSelectDialog.js';

export default class ForeachTool extends BaseTool{
    getType(){
        return '动态分支';
    }
    getIcon(){
        return `<i class="uflo uflo-foreach" style="color:#737383"></i>`
    }
    newNode(){
        return new ForeachNode();
    }
    getConfigs(){
        return {
            in:-1,
            out:-1,
            single:false
        };
    }
    buildProperties(target){
        const group=$(`<div class="form-group uflo-group"></div>`);
        const varnameGroup=$(`<div class="form-group uflo-group"><label>写入分支的变量名：</label></div>`);
        group.append(varnameGroup);
        const varnameEditor=$(`<input type="text" class="form-control uflo-text-editor" style="width: 250px;">`);
        varnameEditor.val(target.variable || '');
        varnameGroup.append(varnameEditor);
        varnameEditor.change(function(){
            target.variable=$(this).val();
        });

        const sourceGroup=$(`<fieldset style="padding: 10px;border:solid 1px #dddddd;border-radius: 8px;margin-bottom: 10px;margin-top: 10px">
        <legend style="width: auto;margin-bottom: 1px;border-bottom:none;font-size: inherit;color: #4b4b4b;">集合类型变量来源配置</legend></fieldset>`);
        group.append(sourceGroup);

        if(!target.foreachType){
            target.foreachType="In";
        }
        const varGroup=$(`<div class="form-group uflo-group"></div>`);
        sourceGroup.append(varGroup);
        const varRadio=$(`<label><input type="radio" ${target.foreachType==='In' ? 'checked' : ''} name="var_source_type_radio">流程变量</label>`);
        varGroup.append(varRadio);
        const varEditor=$(`<input type="text" class="form-control uflo-text-editor" style="width: 280px;margin-left: 4px">`);
        varEditor.val(target.processVariable || '');
        varGroup.append(varEditor);
        varEditor.change(function(){
            target.processVariable=$(this).val();
        });

        const beanGroup=$(`<div class="form-group uflo-group"></div>`);
        sourceGroup.append(beanGroup);
        const beanRadio=$(`<label><input type="radio" ${target.foreachType==='Handler' ? 'checked' : ''} name="var_source_type_radio">实现类Bean</label>`);
        beanGroup.append(beanRadio);
        const tip="一个实现了com.bstek.uflo.process.handler.ForeachHandler接口配置在Spring中的Bean ID";
        const beanEditor=$(`<input type="text" placeholder="${tip}" title="${tip}" class="form-control uflo-text-editor" style="width: 205px;margin-left: 4px">`);
        beanEditor.val(target.handlerBean || '');
        beanGroup.append(beanEditor);
        beanEditor.change(function(){
            target.handlerBean=$(this).val();
        });
        const dialog=new EventSelectDialog(`选择${tip}`);
        const selectButton=$(`<button type="button" class="btn btn-default" style="height: 26px;padding-top: 2px;margin-left: 2px">选择</button>`);
        beanGroup.append(selectButton);
        selectButton.click(function(){
            dialog.show('ForeachHandler',function(beanId){
                target.handlerBean=beanId;
                beanEditor.val(beanId);
            });
        });
        varEditor.prop('readOnly',true);
        beanEditor.prop('readOnly',true);
        selectButton.attr('disabled',true);
        if(target.foreachType==='In'){
            varEditor.prop('readOnly',false);
        }else{
            beanEditor.prop('readOnly',false);
            selectButton.attr('disabled',false);
        }
        varRadio.children('input').click(function(){
            varEditor.prop('readOnly',false);
            beanEditor.prop('readOnly',true);
            selectButton.attr('disabled',true);
            target.foreachType='In';
        });
        beanRadio.children('input').click(function(){
            varEditor.prop('readOnly',true);
            beanEditor.prop('readOnly',false);
            selectButton.attr('disabled',false);
            target.foreachType='Handler';
        });

        return group;
    }
    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$(`<div></div>`);
            g.append(_this.getCommonProperties(this));
            g.append(_this.buildProperties(this));
            return g;
        }
    }
}