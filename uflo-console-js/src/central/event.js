/**
 * Created by Jacky.Gao on 2016/12/11.
 */
import events from 'events';
export const OPEN_START_PROCESS_DIALOG="open_start_process_dialog";
export const CLOSE_START_PROCESS_DIALOG="close_start_process_dialog";
export const OPEN_COMPLETE_TASK_DIALOG="open_complete_task_dialog";
export const CLOSE_COMPLETE_TASK_DIALOG="close_complete_task_dialog";
export const OPEN_CLIAM_TASK_DIALOG="open_cliam_task_dialog";
export const CLOSE_CLIAM_TASK_DIALOG="close_cliam_task_dialog";
export const OPEN_JUMP_TASK_DIALOG="open_jump_task_dialog";
export const CLOSE_JUMP_TASK_DIALOG="close_jump_task_dialog";
export const OPEN_UPLOAD_PROCESS_DIALOG="open_upload_process_dialog";
export const CLOSE_UPLOAD_PROCESS_DIALOG="close_upload_process_dialog";

export const eventEmitter=new events.EventEmitter();