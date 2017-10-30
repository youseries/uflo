/**
 * Created by Jacky.Gao on 2017-07-10.
 */
import BaseNode from './BaseNode.js';
import decisionSVG from './svg/decision.svg';

export default class DecisionNode  extends BaseNode{
    getSvgIcon(){
        return decisionSVG;
    }
    toXML(){
        const json=this.toJSON();
        json.type="DecisionNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps}`;
        if(this.handlerBean){
            xml+=` handler-bean="${this.handlerBean}"`;
            xml+=` decision-type="Handler"`;
        }else{
            xml+=` decision-type="Expression"`;
        }
        xml+=`>`;
        if(!this.handlerBean && this.expression){
            xml+=`<expression>`;
            xml+=`<![CDATA[${this.expression}]]>`;
            xml+=`</expression>`;
        }
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