/**
 * Created by Jacky.Gao on 2017-07-10.
 */
import BaseTool from './BaseTool.js';
import EndTerminalNode from './EndTerminalNode.js';

export default class EndTerminalTool  extends BaseTool{
    getType(){
        return '结束流程';
    }
    getIcon(){
        return `<i class="uflo uflo-end-terminate" style="color:#737383"></i>`
    }
    newNode(){
        return new EndTerminalNode();
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