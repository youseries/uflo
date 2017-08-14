/**
 * Created by Jacky.Gao on 2017-07-14.
 */
import {MsgBox} from 'flowdesigner';

export default class EventSelectDialog{
    constructor(title){
        this.dialog=$(`<div class="modal fade" role="dialog" aria-hidden="true" style="z-index: 10000">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title">
                            ${title || 'Bean选择'}
                        </h4>
                    </div>
                    <div class="modal-body"></div>
                    <div class="modal-footer">
                    </div>
                </div>
            </div>
        </div>`);
        const body=this.dialog.find('.modal-body'),footer=this.dialog.find(".modal-footer");
        this.initBody(body,footer);
    }
    initBody(body,footer){
        const table=$(`<table class="table table-bordered"><thead><tr style="background: #eeeeee">
            <td>Bean ID</td><td style="width: 50px;">选择</td>
        </tr></thead></table>`);
        body.append(table);
        this.tbody=$(`<tbody></tbody>`);
        table.append(this.tbody);
    }

    show(handler,callback){
        this.dialog.modal('show');
        this.tbody.empty();
        const _this=this;
        const url=window._server+"/handlerlist";
        $.ajax({
            url,
            type:'POST',
            data:{handler},
            success:(data)=>{
                for(let d of data){
                    const tr=$(`<tr><td>${d}</td></tr>`);
                    this.tbody.append(tr);
                    const td=$(`<td></td>`);
                    tr.append(td);
                    const icon=$(`<i class="glyphicon glyphicon-hand-up" style="cursor: pointer;color: green;font-size: 12pt"></i>`);
                    td.append(icon);
                    icon.click(()=>{
                        callback.call(this,d);
                        _this.dialog.modal('hide');
                    });
                }
            },
            error:function(){
                MsgBox.alert("加载["+handler+"]失败!");
            }
        });
    }
}