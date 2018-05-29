/**
 * Created by Jacky.Gao on 2016/12/7.
 */
import React,{Component} from 'react';
import {uniqueID} from '../Utils.js';

class Pagination extends Component{
    constructor(props){
        super(props);
    }
    render(){
        const {pageSize,pageIndex,total,enableSetPageSize,changePageCallback} = this.props;
        const totalPage=Math.ceil(total/pageSize);
        let pageStart=1;
        let pageEnd=5;
        if(pageIndex>3){
            pageStart=pageIndex-2;
            pageEnd=pageIndex+2;
        }
        if(pageEnd>totalPage){
            pageStart=totalPage-5;
            pageEnd=totalPage;
            pageStart=pageStart<1 ? 1 :pageStart;
            pageEnd=pageEnd<1 ? 1 : pageEnd;
        }
        const pages=[];
        if(enableSetPageSize){
            const options=[];
            for(let i=10;i<=100;i+=5){
                options.push(<option key={i}>{i}</option>);
            }
            pages.push(
                <li key={uniqueID()} style={{color:'#607D8B'}}>每页记录数：
                    <select defaultValue={pageSize} className="form-control"  style={{display:'inline-block',width:'80px',height:'30px',marginRight:'5px'}} onChange={(e)=>{
                        const currentPageSize=e.target.value;
                        changePageCallback.call(this,{
                            pageSize:currentPageSize,
                            pageIndex:1
                        })
                    }}>
                        {options}
                    </select>
                </li>
            );
        }
        if(pageStart>1){
            pages.push(
                <li key={uniqueID()} style={{color:'#607D8B'}}>
                    <a href="###" style={{float:'none'}} onClick={(e)=>{
                        changePageCallback.call(this,{pageSize,pageIndex:1});
                    }}>首页</a>
                </li>
            );
            pages.push(
                <li key={uniqueID()}>
                    <a href="###"  style={{float:'none'}} onClick={(e)=>{
                        changePageCallback.call(this,{pageSize,pageIndex:pageStart-1});
                    }}>&laquo;</a>
                </li>
            );
        }
        for(let i=pageStart;i<=pageEnd;i++){
            pages.push(
                <li key={uniqueID()} className={pageIndex===i ? 'active' : ''}>
                    <a href="###"  style={{float:'none'}}  onClick={(e)=>{
                        changePageCallback.call(this,{pageSize,pageIndex:i});
                    }}>{i}</a>
                </li>
            )
        }
        if(pageEnd<totalPage){
            pages.push(
                <li key={uniqueID()}  style={{color:'#607D8B'}}>
                    <a href="###"  style={{float:'none'}} onClick={(e)=>{
                        changePageCallback.call(this,{pageSize,pageIndex:pageEnd+1});
                    }}>&raquo;</a>
                </li>
            );
            pages.push(
                <li key={uniqueID()} style={{color:'#607D8B'}}>
                    <a href="###"  style={{float:'none'}} onClick={(e)=>{
                        changePageCallback.call(this,{pageSize,pageIndex:totalPage});
                    }}>尾页</a>
                </li>
            );
        }
        pages.push(
            <li key={uniqueID()} style={{color:'#607D8B',marginLeft:'5px'}}>
                共{total}条记录
            </li>
        );
        return (
            <ul className="pagination" style={{marginTop:'10px'}}>
                {pages}
            </ul>
        );
    }
};

export default Pagination;