/**
 * Created by Jacky.Gao on 2017-07-10.
 */
import BaseTool from './BaseTool.js';
import EndNode from './EndNode.js';

export default class EndTool  extends BaseTool{
    getType(){
        return '结束分支';
    }
    getIcon(){
        return `<i class="uflo uflo-end" style="color:#737383"></i>`
    }
    newNode(){
        return new EndNode();
    }
    getConfigs(){
        return {
            in:-1,
            out:0,
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
}