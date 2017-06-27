/**
 * Created by Jacky.Gao on 2016/12/7.
 */
import events from 'events';

export const OPEN_TASK_PAGE='open_task_page';
export const CLOSE_TASK_PAGE='close_task_page';
export const SHOW_TASK_DIAGRAM='show_task_diagram';
export const CLIAM_TASK="cliam_task";
export const RELOAD_TODO='reload_todo';
export const RELOAD_CLIAM='reload_cliam';
export const RELOAD_HISTORY='reload_history';

export const eventEmitter=new events.EventEmitter();
