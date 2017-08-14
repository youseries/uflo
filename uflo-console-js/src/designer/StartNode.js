/**
 * Created by Jacky.Gao on 2017-07-04.
 */
import BaseNode from './BaseNode.js';
import startSVG from './svg/start.svg';

export default class StartNode extends BaseNode{
    getSvgIcon(){
        return startSVG;
    }
    toXML(){
        const json=this.toJSON();
        json.type="StartNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps}`;
        if(this.taskName){
            xml+=` task-name="${this.taskName}"`;
        }
        if(this.url){
            xml+=` url="${this.url}"`;
        }
        xml+='>';
        if(this.description){
            xml+=` <description><![CDATA[${this.description}]]></description>`;
        }
        xml+=this.getFromConnectionsXML();
        xml+=`</${nodeName}>`;
        return xml;
    }
    initFromJson(json){
        super.initFromJson(json);
    }
    validate(){
        let errorInfo=super.validate();
        return errorInfo;
    }
};