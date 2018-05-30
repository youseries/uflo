/**
 * Created by Jacky.Gao on 2017-07-10.
 */
import BaseTool from './BaseTool.js';
import ForkNode from './ForkNode.js';
import EventSelectDialog from './dialog/EventSelectDialog.js';

export default class ForkTool  extends BaseTool{
    getType(){
        return '分支';
    }
    getIcon(){
        return `<i class="uflo uflo-fork" style="color:#737383"></i>`
    }
    newNode(){
        return new ForkNode();
    }
    getConfigs(){
        return {
            in:-1,
            out:-1,
            single:false
        };
    }
    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$(`<div></div>`);
            g.append(_this.getCommonProperties(this));
            return g;
        }
    }

    buildConnectionCondition(target){
        const group=$(`<fieldset style="padding: 10px;border:solid 1px #dddddd;border-radius: 8px;margin-bottom: 10px;margin-top: 10px">
        <legend style="width: auto;margin-bottom: 1px;border-bottom:none;font-size: inherit;color: #4b4b4b;">连线条件配置</legend></fieldset>`);

        const noneConditionRadio=$(`<div class="form-group uflo-group"><input type="radio" ${!target.conditionType ? 'checked' : ''} name="connection_condition_type_radio">无条件</div>`);
        group.append(noneConditionRadio);

        const exprGroup=$(`<div class="form-group uflo-group"></div>`);
        group.append(exprGroup);
        const exprConditionRadio=$(`<span><input type="radio" ${target.conditionType==='Expression' ? 'checked' : ''} name="connection_condition_type_radio">表达式条件</span>`);
        exprGroup.append(exprConditionRadio);
        const exprEditor=$(`<input type="text" class="form-control uflo-text-editor" style="width: 270px;margin-left: 2px">`);
        exprGroup.append(exprEditor);
        exprEditor.val(target.expression || '');
        exprEditor.change(function(){
            target.expression=$(this).val();
        });

        const beanGroup=$(`<div class="form-group uflo-group"></div>`);
        group.append(beanGroup);
        const beanConditionRadio=$(`<span><input type="radio" ${target.conditionType==='Handler' ? 'checked' : ''} name="connection_condition_type_radio">条件Bean</span>`);
        beanGroup.append(beanConditionRadio);
        const tip='一个实现了com.bstek.uflo.process.handler.ConditionHandler接口，配置在Spring中的Bean Id';
        const beanEditor=$(`<input type="text" value="${target.handlerBean || ''}" title="${tip}" class="form-control uflo-text-editor" style="width: 220px;margin-left: 2px">`);
        beanGroup.append(beanEditor);
        beanEditor.change(function(){
            target.handlerBean=$(this).val();
        });
        const beanSelectButton=$(`<button type="button" class="btn btn-default" style="padding-top: 2px;margin-left: 3px;height: 26px;">选择</button>`);
        beanGroup.append(beanSelectButton);
        exprEditor.prop('readOnly',true);
        beanEditor.prop('readOnly',true);
        beanSelectButton.attr('disabled',true);
        if(target.conditionType==='Expression'){
            exprEditor.prop('readOnly',false);
            exprConditionRadio.children('input').attr('checked',true);
        }else if(target.conditionType==='Handler'){
            beanEditor.prop('readOnly',false);
            beanConditionRadio.children('input').attr('checked',true);
        }else{
            noneConditionRadio.children('input').attr('checked',true);
        }

        const dialog=new EventSelectDialog(`选择${tip}`);
        beanSelectButton.click(()=>{
            dialog.show('ConditionHandler',function(beanId){
                beanEditor.val(beanId);
                target.handlerBean=beanId;
            });
        });
        noneConditionRadio.children('input').click(function(){
            exprEditor.prop('readOnly',true);
            beanEditor.prop('readOnly',true);
            beanSelectButton.attr('disabled',true);
            target.conditionType=null;
            target.handlerBean=null;
            target.expression=null;
        });

        exprConditionRadio.children('input').click(function(){
            exprEditor.prop('readOnly',false);
            beanEditor.prop('readOnly',true);
            beanSelectButton.attr('disabled',true);
            target.conditionType='Expression';
            target.handlerBean=null;
        });

        beanConditionRadio.children('input').click(function(){
            exprEditor.prop('readOnly',true);
            beanEditor.prop('readOnly',false);
            beanSelectButton.attr('disabled',false);
            target.conditionType='Handler';
            target.expression=null;
        });

        return group;
    }

    getConnectionPropertiesProducer(){
        const _this=this;
        return function (target){
            const g=$(`<div></div>`);
            g.append(_this.buildConnectionCondition(this));
            return g;
        }
    }
}