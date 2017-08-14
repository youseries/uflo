/**
 * Created by Jacky.Gao on 2017-07-10.
 */
import BaseTool from './BaseTool.js';
import DecisionNode from './DecisionNode.js';
import EventSelectDialog from './dialog/EventSelectDialog.js';

export default class DecisionTool  extends BaseTool{
    getType(){
        return '路由决策';
    }
    getIcon(){
        return `<i class="uflo uflo-decision" style="color:#737383"></i>`
    }
    newNode(){
        return new DecisionNode();
    }
    getConfigs(){
        return {
            in:-1,
            out:-1,
            single:false
        };
    }
    buildConfig(target){
        const group=$(`<div class="form-group uflo-group"></div>`);
        let type='expr';
        if(target.handlerBean){
            type='handler';
        }
        const exprGroup=$(`<div class="form-group uflo-group"></div>`);
        group.append(exprGroup);
        const exprRadio=$(`<label><input type="radio" name="decision_type_radio">条件表达式</label>`);
        exprGroup.append(exprRadio);
        const exprEditor=$(`<input type="text" value="${target.expression || ''}" class="form-control uflo-text-editor" style="width: 290px;margin-left: 4px">`);
        exprGroup.append(exprEditor);
        exprEditor.change(function(){
            target.expression=$(this).val();
        });

        const beanGroup=$(`<div class="form-group uflo-group"></div>`);
        group.append(beanGroup);
        const beanRadio=$(`<label><input type="radio" name="decision_type_radio">决策Bean ID</label>`);
        beanGroup.append(beanRadio);
        const tip="一个实现了com.bstek.uflo.process.handler.DecisionHandler接口并配置到Spring中的Bean ID"
        const beanEditor=$(`<input type="text" value="${target.handlerBean || ''}" placeholder="${tip}" title="${tip}" class="form-control uflo-text-editor" style="width: 245px;margin-left: 4px">`);
        beanGroup.append(beanEditor);
        beanEditor.change(function(){
            target.handlerBean=$(this).val();
        });

        const selectButton=$(`<button type="button" class="btn btn-default" style="padding: 2px;height: 26px;display: inline-block;margin-left: 2px">选择</button>`);
        beanGroup.append(selectButton);
        const dialog=new EventSelectDialog(`选择${tip}`);
        selectButton.click(function(){
            dialog.show('DecisionHandler',function(beanId){
                target.handlerBean=beanId;
                beanEditor.val(beanId);
            })
        });
        exprEditor.prop('readOnly',true);
        beanEditor.prop('readOnly',true);
        selectButton.attr('disabled',true);
        if(type==='expr'){
            exprRadio.children('input').attr('checked',true);
            exprEditor.prop('readOnly',false);
        }else{
            beanRadio.children('input').attr('checked',true);
            beanEditor.prop('readOnly',false);
            selectButton.attr('disabled',false);
        }
        exprRadio.children('input').click(function(){
            exprEditor.prop('readOnly',false);
            beanEditor.prop('readOnly',true);
            selectButton.attr('disabled',true);
            target.handlerBean=null;
        });

        beanRadio.children('input').click(function(){
            exprEditor.prop('readOnly',true);
            beanEditor.prop('readOnly',false);
            selectButton.attr('disabled',false);
            target.expression=null;
        });

        return group;
    }
    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$(`<div></div>`);
            g.append(_this.getCommonProperties(this));
            g.append(_this.buildConfig(this));
            return g;
        }
    }
}