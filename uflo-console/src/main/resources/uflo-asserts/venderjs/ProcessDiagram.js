/**
 * Created by Jacky.Gao on 2016/12/8.
 */
var ProcessDiagram=function(){
};
ProcessDiagram.prototype.show=function(containerId,parameters,contextPath){
    this.containerId=containerId;
    if(!parameters){
        alert("parameters can not be null.")
    }
    var _this=this;
    if(!contextPath){
        contextPath="";
    }
    this.contextPath=contextPath;
    var remoteUrl=contextPath+"/diagram/loadDiagramData";
    $.ajax({
        url:remoteUrl,
        data:parameters,
        type:"POST",
        success:function(data){
            _this._buildDiagram(data);
        },
        error:function(req,info,error){
            alert("Error:"+info);
        }
    });
};
ProcessDiagram.prototype._buildDiagram=function(diagram){
    var nodes={};
    this.paper=new Raphael(document.getElementById(this.containerId),diagram.width, diagram.height);
    var nodeDiagrams=diagram.nodeDiagrams;
    for(var i=0;i<nodeDiagrams.length;i++){
        var nodeDiagram=nodeDiagrams[i];
        var node=this._buildNode(nodeDiagram,diagram.showTime);
        nodes[nodeDiagram.name]=node;
    }
    for(var i=0;i<nodeDiagrams.length;i++){
        var nodeDiagram=nodeDiagrams[i];
        var sequenceFlows=nodeDiagram.sequenceFlowDiagrams;
        if(!sequenceFlows){
            continue;
        }
        for(var j=0;j<sequenceFlows.length;j++){
            var flow=sequenceFlows[j];
            var to=flow.to;
            var toNode=nodes[to];
            if(!toNode){
                throw new Error("Node "+to+" is not exist.");
            }
            var fromNode=nodes[nodeDiagram.name];
            if(!fromNode){
                throw new Error("Node "+fromNode+" is not exist.");
            }
            this._buildConnection(fromNode,toNode,flow);
        }
    }
};
ProcessDiagram.prototype._buildConnection=function(fromNode,toNode,flow){
    var fromDotX = fromNode.attr("x")+fromNode.attr("width")/2;
    var fromDotY = fromNode.attr("y")+fromNode.attr("height")/2;
    var orgToDotX = toNode.attr("x")+toNode.attr("width")/2;
    var orgToDotY = toNode.attr("y")+toNode.attr("height")/2;
    var toDotX=orgToDotX;
    var toDotY=orgToDotY;
    var points=flow.points;
    if(points && points.length>0){
        toDotX = points[0].x;
        toDotY = points[0].y;
    }
    var startInfo="M "+fromDotX+" "+fromDotY+" L "+toDotX+" "+toDotY+"";
    var fromNodeEdgePathInfo=this._getRectEdgePathInfo(fromNode);
    var fromDot=Raphael.pathIntersection(startInfo,fromNodeEdgePathInfo);
    var connectionInfo="M "+fromDot[0].x+" "+fromDot[0].y;
    var newPoints=[];
    newPoints.push({"x":fromDot[0].x,"y":fromDot[0].y});
    if(points){
        for(var i=0;i<points.length;i++){
            var point=points[i];
            connectionInfo+=" L "+ point.x+" "+point.y;
            newPoints.push(point);
        }
    }
    if(points && points.length>0){
        var p=points[points.length-1];
        fromDotX = p.x;
        fromDotY = p.y;
    }
    var endInfo="M "+fromDotX+" "+fromDotY + " L "+orgToDotX+" "+orgToDotY;
    var toNodeEdgePathInfo=this._getRectEdgePathInfo(toNode);
    var toDot=Raphael.pathIntersection(endInfo,toNodeEdgePathInfo);
    newPoints.push({"x":toDot[0].x,"y":toDot[0].y});
    connectionInfo+=" L "+toDot[0].x+" "+toDot[0].y+"";
    var labelPosition=flow.labelPosition;
    if(labelPosition){
        var point=labelPosition.split(",");
        if(point.length==2){
            var len=newPoints.length;
            var cx,cy;
            if(len % 2==0){
                var p1=newPoints[len/2-1];
                var p2=newPoints[len/2];
                cx=Math.abs(p1.x-p2.x)/2+(p1.x>p2.x?p2.x:p1.x)+parseInt(point[0]);
                cy=Math.abs(p1.y-p2.y)/2+(p1.y>p2.y?p2.y:p1.y)+parseInt(point[1]);
            }else{
                var p=newPoints[parseInt(len/2)];
                cx=p.x+parseInt(point[0]);
                cy=p.y+parseInt(point[1]);
            }
            var flowName=flow.name;
            if(flowName){
                var flowNameText=this.paper.text(cx,cy,flowName);
                flowNameText.attr("font-size",parseInt(flow.fontSize));
                var textWidth=flowNameText.getBBox().width;
                var textHeight=flowNameText.getBBox().height;
                flowNameText.attr({"x":cx+textWidth/2,"y":cy+textHeight/2,"fill":"rgb("+flow.borderColor+")"});
            }
        }
    }
    var connection=this.paper.path(connectionInfo);
    connection.attr({"arrow-end":"block-wide-long","stroke-width":flow.borderWidth,"stroke":"rgb("+flow.borderColor+")"});
    return connection;
};
ProcessDiagram.prototype._getRectEdgePathInfo=function(rectNode){
    var p1 = rectNode.attr("x");
    var p2 = rectNode.attr("y");
    var p3 = rectNode.attr("x")+rectNode.attr("width");
    var p4 = rectNode.attr("y");
    var p5 = rectNode.attr("x")+rectNode.attr("width");
    var p6 = rectNode.attr("y")+rectNode.attr("height");
    var p7 = rectNode.attr("x");
    var p8 = rectNode.attr("y")+rectNode.attr("height");
    return "M "+p1+" "+p2+" L "+p3+" "+p4+" L "+p5+" "+p6+" L "+p7+"  "+p8+" L "+p1+"  "+p2+"";
};
ProcessDiagram.prototype._buildNode=function(nodeDiagram,showTime){
    var width=nodeDiagram.width;
    var height=nodeDiagram.height;
    var icon=nodeDiagram.icon;
    icon=this.contextPath+"/res"+icon;
    var node=this.paper.rect(nodeDiagram.x,nodeDiagram.y,width,height,5);
    var x=nodeDiagram.x+width/2;
    var y=nodeDiagram.y+height/2;
    var time=_buildTime(nodeDiagram.time);
    var text,image;
    var info=nodeDiagram.name;
    if(nodeDiagram.label && nodeDiagram.label.length>0){
        info=nodeDiagram.label;
    }
    if(nodeDiagram.shapeType=="Circle"){
        node.attr("stroke-width","0");
        text=this.paper.text(x,y+32/2,info);
        text.attr("font-size",parseInt(nodeDiagram.fontSize));
        var textHeight=text.getBBox().height;
        var imgX=x-32/2;
        var imgY=y-textHeight/2-32/2;
        image=this.paper.image(icon,imgX,imgY,32,32);
        if(time!=0 && showTime){
            var timeText=this.paper.text(nodeDiagram.x+width-15,nodeDiagram.y,time);
            timeText.attr("font-size","16");
        }
    }else{
        text=this.paper.text(x+32/2,y,info);
        text.attr("font-size",parseInt(nodeDiagram.fontSize));
        var textWidth=text.getBBox().width;
        var imgX=x-textWidth/2-32/2-5;
        var imgY=y-32/2;
        image=this.paper.image(icon,imgX,imgY,32,32);
        node.attr("fill","rgb("+nodeDiagram.backgroundColor+")");
        node.attr("stroke-width",nodeDiagram.borderWidth);
        node.attr("stroke","rgb("+nodeDiagram.borderColor+")");
        if(time!=0 && showTime){
            var timeText=this.paper.text(nodeDiagram.x+width,nodeDiagram.y-8,time);
            timeText.attr("font-size","16");
        }
    }
    text.attr({"fill":"rgb("+nodeDiagram.fontColor+")"});
    var fontBold=nodeDiagram.fontBold;
    if(fontBold){
        var underline=this._buildTextUnderline(text);
        underline.attr("stroke","rgb("+nodeDiagram.fontColor+")");
    }
    if(nodeDiagram.info){
        image.attr("title",nodeDiagram.info);
        node.attr("title",nodeDiagram.info);
    }
    return node;
};
ProcessDiagram.prototype._buildTextUnderline=function(textElement) {
    var textBBox = textElement.getBBox();
    var textUnderline = this.paper.path("M"+textBBox.x+" "+(textBBox.y+textBBox.height)+"L"+(textBBox.x+textBBox.width)+" "+(textBBox.y+textBBox.height));
    return textUnderline;
}
function _buildTime(time){
    switch(time){
        case 0:
            return 0;
        case 1:
            return "①";
        case 2:
            return "②";
        case 3:
            return "③";
        case 4:
            return "④";
        case 5:
            return "⑤";
        case 6:
            return "⑥";
        case 7:
            return "⑦";
        case 8:
            return "⑧";
        case 9:
            return "⑨";
        case 10:
            return "⑩";
        case 11:
            return "⑪";
        case 12:
            return "⑫";
        case 13:
            return "⑬";
        case 14:
            return "⑭";
        case 15:
            return "⑮";
        case 16:
            return "⑯";
        case 17:
            return "⑰";
        case 18:
            return "⑱";
        case 19:
            return "⑲";
        case 20:
            return "⑳";
    }
    return time;
}