/**
 * Created by Jacky.Gao on 2016/12/7.
 */
import '../../node_modules/bootstrap/dist/css/bootstrap.css';

import React from 'react';
import ReactDOM from 'react-dom';
import {createStore,applyMiddleware} from 'redux';
import thunk from 'redux-thunk';
import {Provider} from 'react-redux';
import reducer from './reducer.js';
import * as action from './action.js';
import TodoPage from './TodoPage.jsx';

$(document).ready(function(){
    const store=createStore(reducer,applyMiddleware(thunk));
    store.dispatch(action.loadTodoList(1,10));
    ReactDOM.render(
        <Provider store={store}>
            <TodoPage/>
        </Provider>,
        document.getElementById("container")
    );
});