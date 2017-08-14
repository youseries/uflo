/**
 * Created by Jacky.Gao on 2017-07-10.
 */
import BaseTool from './BaseTool.js';
import JoinNode from './JoinNode.js';

export default class JoinTool  extends BaseTool{
    getType(){
        return '聚合';
    }
    getIcon(){
        return `<i class="uflo uflo-join" style="color:#737383"></i>`
    }
    newNode(){
        return new JoinNode();
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
        const checkbox=$(`<div class="checkbox"><label><input type="checkbox">启用分支到达数配置</label></div>`);
        group.append(checkbox);

        const numGroup=$(`<div class="form-group uflo-group"></div>`);
        group.append(numGroup);
        const numRadio=$(`<label><input type="radio" name="var_source_type_radio">数字</label>`);
        numGroup.append(numRadio);
        const numEditor=$(`<input type="text" value="${target.multiplicity || ''}" class="form-control uflo-text-editor" style="width: 332px;margin-left: 4px">`);

        numGroup.append(numEditor);
        numEditor.change(function(){
            target.multiplicity=$(this).val();
        });
        const percentGroup=$(`<div class="form-group uflo-group"></div>`);

        group.append(percentGroup);
        const percentRadio=$(`<label><input type="radio" name="var_source_type_radio">百分比</label>`);
        percentGroup.append(percentRadio);
        const percentEditor=$(`<input type="text" value="${target.percentMultiplicity || ''}" class="form-control uflo-text-editor" style="width: 305px;margin-left: 4px">`);
        percentGroup.append(percentEditor);

        percentEditor.change(function(){
            target.percentMultiplicity=$(this).val();
        });
        percentGroup.append(`<span>%</span>`);
        if(target.multiplicity){
            numRadio.children('input').attr('checked',true);
            percentEditor.prop("readOnly",true);
        }

        if(target.percentMultiplicity){
            percentRadio.children('input').attr('checked',true);
            numEditor.prop("readOnly",true);
        }

        numRadio.children('input').click(function(){
            numEditor.prop("readOnly",false);
            percentEditor.prop("readOnly",true);
            target.percentMultiplicity=null;
        });

        percentRadio.children('input').click(function(){
            numEditor.prop("readOnly",true);
            percentEditor.prop("readOnly",false);
            target.multiplicity=null;
        });

        checkbox.find('input').change(function(){
            const value=$(this).prop('checked');
            if(value){
                numGroup.show();
                percentGroup.show();
                numRadio.children('input').attr('checked',true);
                percentEditor.prop("readOnly",true);
            }else{
                numGroup.hide();
                percentGroup.hide();
                target.multiplicity=null;
                target.percentMultiplicity=null;
            }
        });
        numGroup.hide();
        percentGroup.hide();
        if(target.multiplicity || target.percentMultiplicity){
            checkbox.find('input').attr('checked',true);
            numGroup.show();
            percentGroup.show();
        }
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