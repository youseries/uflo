/**
 * Created by Jacky.Gao on 2017-07-10.
 */
import BaseTool from './BaseTool.js';
import ActionNode from './ActionNode.js';
import EventSelectDialog from './dialog/EventSelectDialog.js';

export default class ActionTool  extends BaseTool{
    getType(){
        return '动作';
    }
    getIcon(){
        return `<i class="uflo uflo-action" style="color:#737383"></i>`
    }
    newNode(){
        return new ActionNode();
    }
    getConfigs(){
        return {
            in:-1,
            out:-1,
            single:false
        };
    }
    buildActionBean(target){
        const group=$(`<div class="form-group uflo-group"><label>动作Bean：</label></div>`);
        const tip="一个实现了com.bstek.uflo.process.handler.ActionHandler接口并配置到Spring中的Bean ID"
        const beanEditor=$(`<input type="text" value="${target.handlerBean || ''}" placeholder="${tip}" title="${tip}" class="form-control uflo-text-editor" style="width: 265px;">`);
        group.append(beanEditor);
        beanEditor.change(function(){
            target.handlerBean=$(this).val();
        });
        const selectButton=$(`<button type="button" class="btn btn-default" style="padding: 2px;height: 26px;display: inline-block;margin-left: 2px">选择</button>`);
        group.append(selectButton);
        const dialog=new EventSelectDialog(`选择${tip}`);
        selectButton.click(function(){
            dialog.show('ActionHandler',function(beanId){
                target.handlerBean=beanId;
                beanEditor.val(beanId);
            });
        });
        return group;
    }
    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$(`<div></div>`);
            g.append(_this.getCommonProperties(this));
            g.append(_this.buildActionBean(this));
            return g;
        }
    }
}