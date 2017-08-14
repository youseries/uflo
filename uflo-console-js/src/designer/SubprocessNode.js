/**
 * Created by Jacky.Gao on 2017-07-10.
 */
import BaseNode from './BaseNode.js';
import subprocessSVG from './svg/subprocess.svg';

export default class SubprocessNode  extends BaseNode{
    getSvgIcon(){
        return subprocessSVG;
    }
    toXML(){
        const json=this.toJSON();
        json.type="SubprocessNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps}`;
        if(this.subprocessKey){
            xml+=`subprocess-key="${this.subprocessKey}"`;
        }
        if(this.subprocessId){
            xml+=`subprocess-id="${this.subprocessId}"`;
        }
        if(this.subprocessName){
            xml+=`subprocess-name="${this.subprocessName}"`;
        }
        if(this.completeStartTask){
            xml+=`complete-start-task="${this.completeStartTask}"`;
        }else{
            xml+=`complete-start-task="false"`;
        }
        xml+=`>`;
        if(this.description){
            xml+=` <description><![CDATA[${this.description}]]></description>`;
        }
        for(let variable of this.outVariables || []){
            xml+=`<out-subprocess-variable in-parameter-key="${variable.inParameterKey}" out-parameter-key="${variable.outParameterKey}"/>`;
        }
        for(let variable of this.inVariables || []){
            xml+=`<in-subprocess-variable in-parameter-key="${variable.inParameterKey}" out-parameter-key="${variable.outParameterKey}"/>`;
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