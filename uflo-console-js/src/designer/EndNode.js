/**
 * Created by Jacky.Gao on 2017-07-10.
 */
import BaseNode from './BaseNode.js';
import endSVG from './svg/end.svg';

export default class EndNode  extends BaseNode{
    getSvgIcon(){
        return endSVG;
    }
    toXML(){
        const json=this.toJSON();
        json.type="EndNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps} terminate="false">`;
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