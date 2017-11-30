/**
 * Created by Jacky.Gao on 2017-07-10.
 */
import BaseNode from './BaseNode.js';
import foreachSVG from './svg/foreach.svg';

export default class ForeachNode  extends BaseNode{
    getSvgIcon(){
        return foreachSVG;
    }
    toXML(){
        const json=this.toJSON();
        json.type="ForeachNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps}`;
        xml+=` foreach-type="${this.foreachType}"`;
        xml+=` var="${this.variable}"`;
        if(this.foreachType==='In'){
            xml+=` process-variable="${this.processVariable}"`;
        }else{
            xml+=` handler-bean="${this.handlerBean}"`;
        }
        xml+=`>`;
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
}