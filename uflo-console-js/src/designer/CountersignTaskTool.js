/**
 * Created by Jacky.Gao on 2017-07-07.
 */
import TaskTool from './TaskTool.js';
import CountersignTaskNode from './CountersignTaskNode.js';
import EventSelectDialog from './dialog/EventSelectDialog.js';

export default class CountersignTaskTool extends TaskTool{
    getType(){
        return '会签任务';
    }
    getIcon(){
        return `<i class="uflo uflo-task-countersign" style="color:#737383"></i>`
    }
    newNode(){
        return new CountersignTaskNode();
    }
    buildTaskBasicConfig(target){
        const group=super.buildTaskBasicConfig(target);
        const g=$(`<fieldset style="padding: 2px 10px;border:solid 1px #dddddd;border-radius: 8px;margin-bottom: 10px;margin-top: 10px">
            <legend style="width: auto;margin-bottom: 1px;border-bottom:none;font-size: inherit;color: #4b4b4b;"><label>会签任务完成规则</label></legend></fieldset>`);
        group.append(g);

        let completeRule='all';
        if(target.countersignMultiplicity){
            completeRule='num';
        }else if(target.countersignPercentMultiplicity){
            completeRule='percent';
        }else if(target.countersignExpression){
            completeRule='expr';
        }else if(target.countersignHandler){
            completeRule='handler';
        }

        const allGroup=$(`<div class="form-group uflo-group"></div>`);
        g.append(allGroup);
        const allRadio=$(`<span style="font-size: 12px"><input type="radio" ${completeRule==='all' ? 'checked' : ''} name="countersign_complete_rule_radio">所有任务全部完成</span>`);
        allGroup.append(allRadio);

        const numGroup=$(`<div class="form-group uflo-group"></div>`);
        g.append(numGroup);
        const numRadio=$(`<span style="font-size: 12px"><input type="radio" ${completeRule==='num' ? 'checked' : ''} name="countersign_complete_rule_radio">完成指定任务数</span>`);
        numGroup.append(numRadio);
        const numEditor=$(`<input type="number" value="${target.countersignMultiplicity || ''}" class="form-control uflo-text-editor" style="width: 225px;margin-left: 10px">`);
        numGroup.append(numEditor);
        numEditor.change(function(){
            target.countersignMultiplicity=$(this).val();
            target.countersignPercentMultiplicity=null;
            target.countersignExpression=null;
            target.countersignHandler=null;
        });

        const percentGroup=$(`<div class="form-group uflo-group"></div>`);
        g.append(percentGroup);
        const percentRadio=$(`<span style="font-size: 12px"><input type="radio" ${completeRule==='percent' ? 'checked' : ''} name="countersign_complete_rule_radio">完成任务百分比</span>`);
        percentGroup.append(percentRadio);
        const percentEditor=$(`<input type="number" value="${target.countersignPercentMultiplicity || ''}" class="form-control uflo-text-editor" style="width: 210px;margin-left: 10px">`);
        percentGroup.append(percentEditor);
        percentGroup.append("<span>%</span>");
        percentEditor.change(function(){
            target.countersignPercentMultiplicity=$(this).val();
            target.countersignExpression=null;
            target.countersignHandler=null;
            target.countersignMultiplicity=null;
        });


        const exprGroup=$(`<div class="form-group uflo-group"></div>`);
        g.append(exprGroup);
        const exprRadio=$(`<span style="font-size: 12px"><input type="radio" ${completeRule==='expr' ? 'checked' : ''} name="countersign_complete_rule_radio">表达式</span>`);
        exprGroup.append(exprRadio);
        const exprEditor=$(`<input type="text" class="form-control uflo-text-editor" style="width: 275px;margin-left: 10px">`);
        exprEditor.val(target.countersignExpression || '');
        exprGroup.append(exprEditor);
        exprEditor.change(function(){
            target.countersignExpression=$(this).val();
            target.countersignHandler=null;
            target.countersignMultiplicity=null;
            target.countersignPercentMultiplicity=null;
        });

        const beanGroup=$(`<div class="form-group uflo-group"></div>`);
        g.append(beanGroup);
        const beanRadio=$(`<span style="font-size: 12px"><input type="radio" ${completeRule==='handler' ? 'checked' : ''} name="countersign_complete_rule_radio">指定Bean</span>`);
        beanGroup.append(beanRadio);
        const beanEditor=$(`<input type="text" value="${target.countersignHandler || ''}" title="一个实现了com.bstek.uflo.process.handler.CountersignHandler接口配置在spring中的bean的id" class="form-control uflo-text-editor" style="width: 203px;margin-left: 10px">`);
        beanGroup.append(beanEditor);
        beanEditor.change(function(){
            target.countersignHandler=$(this).val();
            target.countersignExpression=null;
            target.countersignMultiplicity=null;
            target.countersignPercentMultiplicity=null;
        });

        const selectButton=$(`<button type="button" class="btn btn-default" style="height: 26px;padding-top: 2px;margin-left: 2px">选择</button>`);
        beanGroup.append(selectButton);
        const dialog=new EventSelectDialog('选择一个实现了com.bstek.uflo.process.handler.CountersignHandler接口配置在spring中的bean的id');
        selectButton.click(function(){
            dialog.show('CountersignHandler',function(beanId){
                target.countersignHandler=beanId;
                beanEditor.val(beanId);
            });
        });

        numEditor.attr('readOnly',true);
        percentEditor.attr('readOnly',true);
        exprEditor.attr('readOnly',true);
        beanEditor.attr('readOnly',true);
        selectButton.attr('disabled',true);
        if(completeRule==='num'){
            numEditor.attr('readOnly',false);
        }else if(completeRule==='percent'){
            percentEditor.attr('readOnly',false);
        }else if(completeRule==='expr'){
            exprEditor.attr('readOnly',false);
        }else if(completeRule==='handler'){
            beanEditor.attr('readOnly',false);
            selectButton.attr('disabled',false);
        }

        allRadio.children('input').click(function(){
            numEditor.attr('readOnly',true);
            percentEditor.attr('readOnly',true);
            exprEditor.attr('readOnly',true);
            beanEditor.attr('readOnly',true);
            selectButton.attr('disabled',true);
        });

        numRadio.children('input').click(function(){
            numEditor.attr('readOnly',false);
            percentEditor.attr('readOnly',true);
            exprEditor.attr('readOnly',true);
            beanEditor.attr('readOnly',true);
            selectButton.attr('disabled',true);
        });

        percentRadio.children('input').click(function(){
            numEditor.attr('readOnly',true);
            percentEditor.attr('readOnly',false);
            exprEditor.attr('readOnly',true);
            beanEditor.attr('readOnly',true);
            selectButton.attr('disabled',true);
        });

        exprRadio.children('input').click(function(){
            numEditor.attr('readOnly',true);
            percentEditor.attr('readOnly',true);
            exprEditor.attr('readOnly',false);
            beanEditor.attr('readOnly',true);
            selectButton.attr('disabled',true);
        });

        beanRadio.children('input').click(function(){
            numEditor.attr('readOnly',true);
            percentEditor.attr('readOnly',true);
            exprEditor.attr('readOnly',true);
            beanEditor.attr('readOnly',false);
            selectButton.attr('disabled',false);
        });

        return group;
    }
    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$(`<div></div>`);
            g.append(_this.buildTab(this));
            return g;
        }
    }
};