/**
 * Created by Jacky.Gao on 2016/12/15.
 */
import '../../node_modules/bootstrap/dist/css/bootstrap.css';

import React from 'react';
import ReactDOM from 'react-dom';
import {createStore,applyMiddleware} from 'redux';
import {Provider} from 'react-redux';
import thunk from 'redux-thunk';

import * as action from './action.js';
import reducer from './reducer.js';
import CalendarPage from './CalendarPage.jsx';

$(document).ready(function(){
    const store=createStore(reducer,applyMiddleware(thunk));
    store.dispatch(action.loadCalendar());
    ReactDOM.render(
        <Provider store={store}>
            <CalendarPage/>
        </Provider>,
        document.getElementById("container")
    );
});