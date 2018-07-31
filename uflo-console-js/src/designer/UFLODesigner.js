/**
 * Created by Jacky.Gao on 2017-07-05.
 */
import {Node,FlowDesigner,MsgBox} from 'flowdesigner';
import BaseNode from './BaseNode.js';
import EventSelectDialog from './dialog/EventSelectDialog.js';

export default class UfloDesigner extends FlowDesigner{
    constructor(containerId) {
        super(containerId);
        this.setInfo('未保存文件');
    }
    toXML(){
        if(!this.name || this.name.length<1){
            MsgBox.alert('流程名称不能为空!');
            return;
        }
        if(!this.validate()){
            return;
        }
        if(this.toJSON().length<2){
            MsgBox.alert('流程至少要包含一个开始节点和一个结束节点!');
            return;
        }
        let xml='<?xml version="1.0" encoding="utf-8"?>';
        xml+=`<uflo-process name="${this.name}"`;
        if(this.key){
            xml+=` key="${this.key}"`
        }
        if(this.categoryId){
            xml+=` category-id="${this.categoryId}"`;
        }
        if(this.category){
            xml+=` category="${this.category}"`;
        }
        if(this.url){
            xml+=` start-process-url="${this.url}"`;
        }
        if(this.eventHandlerBean){
            xml+=` event-handler-bean="${this.eventHandlerBean}"`
        }
        if(this.effectDate){
            xml+=` effect-date="${this.effectDate}"`;
        }
        xml+=`>`;
        for(let figure of this.context.allFigures){
            if(!(figure instanceof BaseNode)){
                continue;
            }
            xml+=figure.toXML();
        }
        xml+='</uflo-process>';
        return xml;
    }
    fromJson(json){
        this.name=json.name;
        this.key=json.key;
        this.url=json.startProcessUrl;
        this.eventHandlerBean=json.eventHandlerBean;
        this.categoryId=json.categoryId;
        this.category=json.category;
        this.effectDate=json.effectDate;
        this.description=json.description;
        for(let nodeJson of json.nodes){
            nodeJson.w=nodeJson.width;
            nodeJson.h=nodeJson.height;
            switch (nodeJson.type){
                case "Action":
                    nodeJson.type='动作';
                    break;
                case "Task":
                    nodeJson.type='人工任务';
                    break;
                case "CountersignTask":
                    nodeJson.type='会签任务';
                    break;
                case "Foreach":
                    nodeJson.type='动态分支';
                    break;
                case "Subprocess":
                    nodeJson.type='子流程';
                    break;
                case "Decision":
                    nodeJson.type='路由决策';
                    break;
                case "End":
                    nodeJson.type='结束分支';
                    break;
                case "Start":
                    nodeJson.type='开始';
                    break;
                case "TerminalEnd":
                    nodeJson.type='结束流程';
                    break;
                case "Fork":
                    nodeJson.type='分支';
                    break;
                case "Join":
                    nodeJson.type='聚合';
                    break;
            }
            nodeJson.connections=nodeJson.sequenceFlows || [];
            for(let conn of nodeJson.connections){
                conn.to=conn.toNode;
            }
            this.addNode(nodeJson);
        }
        for(let figure of this.context.allFigures){
            if(!(figure instanceof Node)){
                continue;
            }
            figure._buildConnections();
        }
    }

    buildProperties(target){
        const g=$('<div></div>');
        const flowNameGroup=$(`<div class="form-group"><label>流程名称：</label></div>`);
        g.append(flowNameGroup);
        const flowNameText=$(`<input type="text" class="form-control uflo-text-editor" style="width: 300px">`);
        flowNameGroup.append(flowNameText);
        flowNameText.val(target.name);
        flowNameText.change(function(){
            target.name=$(this).val();
        });

        const keyGroup=$(`<div class="form-group"><label>关键字：</label></div>`);
        g.append(keyGroup);
        const keyText=$(`<input type="text" class="form-control uflo-text-editor" style="width: 315px">`);
        keyGroup.append(keyText);
        keyText.val(target.key);
        keyText.change(function(){
           target.key=$(this).val();
        });

        const urlGroup=$(`<div class="form-group"><label>URL：</label></div>`);
        g.append(urlGroup);
        const urlText=$(`<input type="text" class="form-control uflo-text-editor" style="width: 330px">`);
        urlGroup.append(urlText);
        urlText.val(target.url);
        urlText.change(function(){
           target.url=$(this).val();
        });

        const eventGroup=$(`<div class="form-group"><label>事件Bean：</label></div>`);
        g.append(eventGroup);
        const tip="一个实现了com.bstek.uflo.process.handler.ProcessEventHandler接口并配置到Spring中的Bean ID"
        const eventText=$(`<input type="text" class="form-control uflo-text-editor" title="${tip}" style="width: 240px">`);
        eventGroup.append(eventText);
        eventText.val(target.eventHandlerBean);
        const eventSelectButton=$(`<button type="button" class="btn btn-default" style="height: 26px;padding-top: 2px;margin-left: 2px;">选择</button>`);
        eventGroup.append(eventSelectButton);
        eventText.change(function(){
            target.eventHandlerBean=$(this).val();
        });
        const dialog=new EventSelectDialog(`选择${tip}`);
        eventSelectButton.click(function(){
            dialog.show('ProcessEventHandler',function(beanId){
                target.eventHandlerBean=beanId;
                eventText.val(beanId);
            });
        });

        const categoryIdGroup=$(`<div class="form-group"><label>分类ID：</label></div>`);
        g.append(categoryIdGroup);
        const categoryIdText=$(`<input type="text" class="form-control uflo-text-editor" style="width: 315px">`);
        categoryIdGroup.append(categoryIdText);
        categoryIdText.val(target.categoryId);
        categoryIdText.change(function(){
            target.categoryId=$(this).val();
        });

        if(window._categories.length>0){
            const categoryGroup=$(`<div class="form-group"><label>业务分类：</label></div>`);
            g.append(categoryGroup);
            const categorySelect=$(`<select class="form-control uflo-text-editor" style="width: 300px"></select>`);
            for(let category of window._categories){
                categorySelect.append(`<option value="${category}">${category}</option>`);
            }
            categoryGroup.append(categorySelect);
            categorySelect.val(target.category);
            categorySelect.change(function(){
                target.category=$(this).val();
            });
        }

        const effectDateGroup=$(`<div class="form-group"><label>生效日期：</label></div>`);
        g.append(effectDateGroup);
        const effectDateText=$(`<input type="datetime" placeholder="日期格式为yyyy-MM-dd HH:mm:ss" title="日期格式为yyyy-MM-dd HH:mm:ss" class="form-control uflo-text-editor" style="width: 300px">`);
        effectDateGroup.append(effectDateText);
        effectDateText.val(target.effectDate);
        effectDateText.change(function(){
            target.effectDate=$(this).val();
        });

        const descGroup=$(`<div class="form-group"><label style="vertical-align: top">描述：</label></div>`);
        g.append(descGroup);
        const descText=$(`<textarea rows="5" class="form-control uflo-text-editor" style="width: 330px;height:80px !important"></textarea>`);
        descGroup.append(descText);
        descText.val(target.description);
        descText.change(function(){
            target.description=$(this).val();
        });

        return g;
    }

    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$('<div></div>');
            g.append(_this.buildProperties(this));
            return g;
        }
    }
}