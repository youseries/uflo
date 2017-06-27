/**
 * Created by Jacky.Gao on 2016/12/16.
 */
import events from 'events';

export const SHOW_CALENDAR_DIALOG='show_calendar_dialog';
export const CLOSE_CALENDAR_DIALOG='close_calendar_dialog';
export const SHOW_CALENDAR_DATE_DIALOG='show_calendar_date_dialog';
export const CLOSE_CALENDAR_DATE_DIALOG='close_calendar_date_dialog';

export const eventEmitter=new events.EventEmitter();