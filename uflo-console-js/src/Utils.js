/**
 * Created by Jacky.Gao on 2016/12/7.
 */

let __ui_id=1;
export function uniqueID(){
    const id='_ui_'+(__ui_id++);
    return id;
};

export function ajax(url,data,successCallback,errorCallback){
    if(!successCallback){
        successCallback=function(){};
    };
    if(!errorCallback){
        errorCallback=function(){
            alert("服务端出错.");
        };
    }
    $.ajax({
        url,
        data,
        type:'POST',
        success:successCallback,
        error:errorCallback
    });
};
export function formatDate(date,format){
    if(typeof date === 'number'){
        date=new Date(date);
    }
    if(typeof date==='string'){
        return date;
    }
    var o = {
        "M+" : date.getMonth()+1,
        "d+" : date.getDate(),
        "H+" : date.getHours(),
        "m+" : date.getMinutes(),
        "s+" : date.getSeconds()
    };
    if(/(y+)/.test(format))
        format=format.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(format))
            format = format.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return format;
};

export function getParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return r[2];
    return null;
};