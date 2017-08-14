/**
 * Created by jacky on 2016/7/18.
 */
import BaseTool from './BaseTool.js';
import StartNode from './StartNode.js';

export default class StartTool extends BaseTool{
    getType(){
        return '开始';
    }
    getIcon(){
        return `<i class="uflo uflo-start" style="color:#737383"></i>`
    }
    newNode(){
        return new StartNode();
    }
    getConfigs(){
        return {
            in:0,
            out:1,
            single:true
        };
    }
    buildTaskConfig(target){
        const group=$(`<fieldset style="padding: 10px;border:solid 1px #dddddd;border-radius: 8px;margin-bottom: 10px;margin-top: 10px">
        <legend style="width: auto;margin-bottom: 1px;border-bottom:none;font-size: inherit;color: #4b4b4b;">任务配置</legend></fieldset>`);
        const taskNameGroup=$(`<div class="form-group uflo-group"><label>任务名称：</label></div>`);
        const taskNameEditor=$(`<input type="text" class="form-control uflo-text-editor" value="${target.taskName || ''}" style="width:280px">`);
        taskNameGroup.append(taskNameEditor);
        group.append(taskNameGroup);
        taskNameEditor.change(function(){
            const value=$(this).val();
            target.taskName=value;
        });

        const urlGroup=$(`<div class="form-group uflo-group"><label>URL：</label></div>`);
        const urlEditor=$(`<input type="text" class="form-control uflo-text-editor" value="${target.url || ''}" style="width:308px">`);
        urlGroup.append(urlEditor);
        group.append(urlGroup);
        urlEditor.change(function(){
            target.url=$(this).val();
        });

        return group;
    }

    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$(`<div></div>`);
            g.append(_this.getCommonProperties(this));
            g.append(_this.buildTaskConfig(this));
            return g;
        }
    }
}