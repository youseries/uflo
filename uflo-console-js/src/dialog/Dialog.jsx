/**
 * Created by Jacky.Gao on 2016/12/8.
 */
import React,{Component} from 'react';
import ReactDOM from 'react-dom';
import * as event from './event.js';
class Dialog extends Component{
    constructor(props){
        super(props);
        this.state={title:'',url:'',executeCloseCallback:false};
    }
    componentDidMount(){
        const $dom=$(ReactDOM.findDOMNode(this));
        $dom.on('show.bs.modal',function () {
            let zIndex=1050;
            $(document).find('.modal').each(function (index,modal) {
                const zindex=$(modal).css('z-index');
                if(zindex && zindex!=='' && !isNaN(zindex)){
                    const z=parseInt(zindex);
                    if(z>zIndex){
                        zIndex=z;
                    }
                }
            });
            $dom.css('z-index',zIndex+1);
            const width=$(window).width()-200;
            $dom.find(".modal-dialog").css("width",width+"px");
        });
        const callbackOnClose=this.props.callbackOnClose;
        const _this=this;
        if(callbackOnClose){
            $dom.on('hide.bs.modal',function(){
                if(_this.state.executeCloseCallback){
                    callbackOnClose.call(this);
                }
            });
        }
        event.eventEmitter.on(event.SHOW_DIALOG,(data)=>{
            const $dom= $(ReactDOM.findDOMNode(this));
            const windowHeight=$(window).height()-200;
            $dom.find("iframe").attr("height",windowHeight);
            $dom.modal({backdrop: 'static', keyboard: false});
            const {title,url,executeCloseCallback}=data;
            this.setState({title,url,executeCloseCallback});
        });
        event.eventEmitter.on(event.CLOSE_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
        window.dialogEvent=event;
    }
    render(){
        const {url,title}=this.state;
        return (
            <div className='modal fade' tabIndex="-1" role="dialog" aria-hidden="true" style={{'overflow':'auto'}}>
                <div className='modal-dialog modal-lg'>
                    <div className="modal-content">
                        <div className="modal-header">
                            <button type="button" className="close" data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h4 className="modal-title" id="myModalLabel">
                                {title}
                                <div className="text-danger" style={{fontSize:'12pt'}}>{this.props.info ? this.props.info : null}</div>
                            </h4>
                        </div>
                        <div className="modal-body" style={{padding:'10px'}}>
                            <iframe frameBorder="0" width="100%" src={url} style={{'overflow' : 'scroll'}}></iframe>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
};
export default Dialog;