/**
 * Created by Jacky.Gao on 2017-07-14.
 */
import {MsgBox} from 'flowdesigner';

export default class AssignProviderListDialog{
    constructor(){
        this.dialog=$(`<div class="modal fade" role="dialog" aria-hidden="true" style="z-index: 10000">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title">
                            选择参与者
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
        this.providerMap=new Map();
        this.tip=$(`<span>当前应用中实现了com.bstek.uflo.process.assign.AssigneeProvider接口的的任务处理人提供者在下面的列表当中，选择一条继续</span>`);
        body.append(this.tip);
        body.append(`<div class="form-group"></div>`);

        const _this=this;
        this.providerSelect=$(`<select class="form-control" size="10"></select>`);
        body.append(this.providerSelect);

        this.prevButton=$(`<button type="button" class="btn btn-default">上一步</button>`);
        footer.append(this.prevButton);
        this.nextButton=$(`<button type="button" class="btn btn-default">下一步</button>`);
        footer.append(this.nextButton);

        this.tableContainer=$(`<div class="form-group"></div>`);
        this.assignTable=$(`<table class="table table-bordered"><thead>
            <tr style="background: #eeeeee"><td>ID</td><td>名称</td><td style="50px">选择</td></tr></thead>
        </table>`);
        this.assignTableBody=$(`<tbody></tbody>`);
        this.assignTable.append(this.assignTableBody);
        this.tableContainer.append(this.assignTable);
        body.append(this.tableContainer);
        this.pageGroup=$(`<div class="form-group"><label>翻页：</label></div>`);
        this.tableContainer.append(this.pageGroup);

        this.treeContainer=$(`<div class="tree" style="margin-left: 10px"></div>`);
        body.append(this.treeContainer);
        this.tree=$(`<ul style="padding-left: 10px;"></ul>`);
        this.treeContainer.append(this.tree);

        this.prevButton.click(()=>{
            this.tableContainer.hide();
            this.treeContainer.hide();
            this.providerSelect.show();
            this.tip.show();
            this.prevButton.attr("disabled",true);
            this.nextButton.attr("disabled",false);
        });
        this.nextButton.click(()=>{
            const providerId=this.providerSelect.val();
            if(!providerId){
                MsgBox.alert('请先选择一个参与对象提供者！');
                return;
            }
            const provider=this.providerMap.get(providerId);
            if(!provider){
                MsgBox.alert('参数对象提供者不存在！');
                return;
            }
            this.tip.hide();
            this.providerSelect.hide();
            if(provider.tree){
                this.treeContainer.show();
                this.tableContainer.hide();
                this.buildTree(provider);
            }else{
                this.tableContainer.show();
                this.treeContainer.hide();
                this.buildAssignTable(provider);
            }
            this.prevButton.attr("disabled",false);
            this.nextButton.attr("disabled",true);
        });
    }

    buildTree(provider){
        const url=window._server+"/assignproviderlist";
        const _this=this;
        $.ajax({
            url,
            type:'POST',
            data:{providerId:provider.providerId},
            success:function(data){
                _this.tree.empty();
                for(let d of data.assignees || []){
                    _this.buildTreeNode(_this.tree,d);
                }
            }
        })
    }

    buildTreeNode(parent,nodeData){
        const li=$(`<li></li>`);
        parent.append(li);
        if(nodeData.chosen){
            const node=$(`<span><a href="###"><i class="uflo uflo-minus"></i> ${nodeData.name}</a></span>`);
            li.append(node);
            node.click(()=>{
                const result=this.callback.call(this,{providerId:nodeData.providerId,id:nodeData.id,name:nodeData.name});
                if(result){
                    this.dialog.modal('hide');
                }
            });
        }else{
            const _this=this;
            const node=$(`<span>${nodeData.name}</span>`);
            li.append(node);
            const open=$(`<i class="uflo uflo-plus" style="color: #005fd3;cursor: pointer;margin-right:2px"></i>`);
            node.prepend(open);
            let ul=$(`<ul></ul>`);
            li.append(ul);
            let isopen=false;
            open.click(function(){
                if(isopen){
                    isopen=false;
                    open.removeClass('uflo-minus');
                    open.addClass('uflo-plus');
                    ul.empty();
                }else{
                    isopen=true;
                    _this.buildTreeChildren(ul,nodeData.providerId,nodeData.id);
                    open.removeClass('uflo-plus');
                    open.addClass('uflo-minus');
                }
            });
        }
    }

    buildTreeChildren(parent,providerId,id){
        const url=window._server+"/assignproviderlist";
        const _this=this;
        $.ajax({
            url,
            data:{providerId,parentId:id},
            type:'POST',
            success:function(data){
                for(let d of data.assignees || []){
                    _this.buildTreeNode(parent,d);
                }
            }
        });
    }

    buildAssignTable(provider){
        this.assignTableBody.empty();
        const pageSize=10;
        const totalPage=parseInt(provider.count/pageSize) + ((provider.count % pageSize) > 0 ? 1 : 0);
        if(this.pageSelect){
            this.pageSelect.remove();
        }
        this.pageSelect=$(`<select class="form-control" style="display: inline-block;width: inherit;"></select>`);
        this.pageGroup.append(this.pageSelect);
        for(let i=1;i<=totalPage;i++){
            const option=$(`<option>${i}</option>`);
            this.pageSelect.append(option);
        }
        const _this=this;
        this.pageSelect.change(function(){
            const pageIndex=$(this).val();
            _this.loadAssignTableData(provider.providerId,pageIndex,pageSize);
        });
        this.loadAssignTableData(provider.providerId,1,pageSize);
    }

    loadAssignTableData(providerId,pageIndex,pageSize){
        const url=window._server+"/assignproviderlist";
        const _this=this;
        $.ajax({
            url,
            type:"POST",
            data:{providerId,pageIndex,pageSize},
            success:function(data){
                _this.buildAssignTableData(data);

            },
            error:function(){
                alert('加载数据失败！');
            }
        });
    }

    buildAssignTableData(data){
        this.assignTableBody.empty();
        for(let d of data.assignees || []){
            const tr=$(`<tr><td>${d.id}</td><td>${d.name}</td></tr>`);
            const selectTD=$(`<td style="width: 60px"></td>`);
            tr.append((selectTD));
            const selectIcon=$(`<i class="glyphicon glyphicon-hand-up" style="color: green;font-size: 14pt;cursor: pointer"></i>`);
            selectTD.append(selectIcon);
            this.assignTableBody.append(tr);
            selectIcon.click(()=>{
                const result=this.callback.call(this,{providerId:d.providerId,id:d.id,name:d.name});
                if(result){
                    this.dialog.modal('hide');
                }
            });
        }
    }

    loadProviders(){
        const _this=this;
        const url=window._server+'/assignproviderlist';
        $.ajax({
            url,
            data:{},
            type:'POST',
            success:function(data){
                _this.buildSelect(data);
            },
            error:function(){
                MsgBox.alert(`加载任务参与者失败！`);
            }
        });
    }

    buildSelect(data){
        this.providerSelect.empty();
        this.providerMap.clear();
        for(let provider of data){
            let newOption=$(`<option value="${provider.providerId}">${provider.name}</option>`);
            this.providerSelect.append(newOption);
            this.providerMap.set(provider.providerId,provider);
        }
    }

    show(callback){
        this.dialog.modal('show');
        this.loadProviders();
        this.tableContainer.hide();
        this.treeContainer.hide();
        this.providerSelect.show();
        this.prevButton.attr("disabled",true);
        this.nextButton.attr("disabled",false);
        this.callback=callback;
    }
}