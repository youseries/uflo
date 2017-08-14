/**
 * Created by Jacky.Gao on 2017-07-10.
 */
import BaseNode from './BaseNode.js';
import joinSVG from './svg/join.svg';

export default class JoinNode  extends BaseNode{
    getSvgIcon(){
        return joinSVG;
    }
    toXML(){
        const json=this.toJSON();
        json.type="JoinNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps}`;
        if(this.multiplicity){
            xml+=` multiplicity="${this.multiplicity}"`;
        }
        if(this.percentMultiplicity){
            xml+=` percent-multiplicity="${this.percentMultiplicity}"`;
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