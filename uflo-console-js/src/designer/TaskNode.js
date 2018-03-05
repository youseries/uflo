/**
 * Created by Jacky.Gao on 2017-07-06.
 */
import BaseNode from './BaseNode.js';
import taskSVG from './svg/task.svg';

export default class TaskNode extends BaseNode{
    constructor(){
        super();
        this.assignmentType='ProcessPromoter';
    }
    getSvgIcon(){
        return taskSVG;
    }
    toXML(){
        const nodeInfo=`task-type="Normal"`;
        return this.buildTaskXml(nodeInfo);
    }

    buildTaskXml(nodeInfo){
        const json=this.toJSON();
        json.type="TaskNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps} ${nodeInfo}`;
        if(this.taskName){
            xml+=` task-name="${this.taskName}"`;
        }
        if(this.url){
            xml+=` url="${this.url}"`;
        }
        if(this.taskListenerBean){
            xml+=` task-listener-bean="${this.taskListenerBean}"`;
        }
        if(this.assignmentType){
            xml+=` assignment-type="${this.assignmentType}"`;
        }
        if(this.expression){
            xml+=` expression="${this.expression}"`;
        }
        if(this.assignmentHandlerBean){
            xml+=` assignment-handler-bean="${this.assignmentHandlerBean}"`;
        }
        if(this.allowSpecifyAssignee){
            xml+=` allow-specify-assignee="${this.allowSpecifyAssignee}"`;
        }else{
            xml+=` allow-specify-assignee="false"`;
        }
        xml+='>';

        for(let assign of this.assignees || []){
            xml+=`<assignee provider-id="${assign.providerId}" name="${assign.name}" id="${assign.id}"/>`;
        }

        if(this.dueDefinition){
            xml+=`<due`;
            if(this.dueDefinition.day){
                xml+=` day="${this.dueDefinition.day}"`;
            }
            if(this.dueDefinition.hour){
                xml+=` hour="${this.dueDefinition.hour}"`;
            }
            if(this.dueDefinition.minute){
                xml+=` minute="${this.dueDefinition.minute}"`;
            }
            xml+=`>`;
            for(let calendar of this.dueDefinition.calendarInfos || []){
                xml+=`<calendar-provider name="${calendar.name}" id="${calendar.id}"/>`;
            }
            if(this.dueDefinition.reminder){
                const reminder=this.dueDefinition.reminder;
                if(reminder.unit){
                    xml+=`<period-reminder handler-bean="${reminder.handlerBean}" unit="${reminder.unit}" repeat="${reminder.repeat}">`;
                    for(let calendar of reminder.calendarInfos || []){
                        xml+=`<calendar-provider name="${calendar.name}" id="${calendar.id}"/>`;
                    }
                    xml+=`</period-reminder>`;
                }else{
                    xml+=`<once-reminder handler-bean="${reminder.handlerBean}"/>`;
                }
            }
            const dueAction=this.dueDefinition.dueAction;
            if(dueAction && dueAction.handlerBean){
                xml+=`<due-action handler-bean="${dueAction.handlerBean || ''}" hour="${dueAction.hour || 0}" day="${dueAction.day || 0}"
                 minute="${dueAction.minute || 0}">`;
                for(let calendar of dueAction.calendarInfos || []){
                    xml+=`<calendar-provider name="${calendar.name}" id="${calendar.id}"/>`;
                }
                xml+=`</due-action>`;
            }
            xml+=`</due>`;
        }
        for(let data of this.componentAuthorities || []){
            xml+=`<component-authority component="${data.component}" authority="${data.authority}"/>`;
        }
        for(let data of this.userData || []){
            xml+=`<user-data value="${data.value}" key="${data.key}"/>`;
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