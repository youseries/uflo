/**
 * Created by Jacky.Gao on 2016/12/11.
 */
import '../../node_modules/bootstrap/dist/css/bootstrap.css';
import React from 'react';
import ReactDOM from 'react-dom';
import {createStore,applyMiddleware} from 'redux';
import {Provider} from 'react-redux';
import thunk from 'redux-thunk';

import * as action from './action.js';
import reducer from './reducer.js';
import CentralPage from './CentralPage.jsx';

const store=createStore(reducer,applyMiddleware(thunk));
store.dispatch(action.loadProcess(1,10));
$(document).ready(function(){
   ReactDOM.render(
      <Provider store={store}>
          <CentralPage/>
      </Provider>,
       document.getElementById("container")
   ) ;
});