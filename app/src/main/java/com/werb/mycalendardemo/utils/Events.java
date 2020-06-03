package com.werb.mycalendardemo.utils;


import com.werb.mycalendardemo.models.DayItem;

import java.util.Calendar;
import java.util.Date;

/**
 * Events emitted by the bus provider.
 * EventBus可能出现的event事件类型
 * 用于传递事件
 */
public class Events {

    /**
     * Day的点击事件
     */
    public static class DayClickedEvent {

        public Calendar mCalendar;
        public DayItem mDayItem;

        public DayClickedEvent(DayItem dayItem) {
            this.mCalendar = Calendar.getInstance();
            this.mCalendar.setTime(dayItem.getDate());
            this.mDayItem = dayItem;
        }

        public Calendar getCalendar() {
            return mCalendar;
        }

        public DayItem getDay() {
            return mDayItem;
        }
    }

    /**
     * 回到今天时间
     */
    public static class GoBackToDay{

    }

    /**
     * Calendar的滑动事件
     */
    public static class CalendarScrolledEvent {
    }

    /**
     * 日程View的触摸事件
     */
    public static class AgendaListViewTouchedEvent {
    }


    /**
     * 暂时理解为 事件与日期匹配
     */
    public static class EventsFetched {
    }


    public static class ForecastFetched {
    }

    public static class CalendarMonthEvent{

        public String month;

        public CalendarMonthEvent(String month) {
            this.month = month;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }
    }

    public static class CalendarSignButtonEvent{

        public boolean isSigned;
        public String nowDate;
        public boolean isToday;
        public DayItem dayItem;

        public CalendarSignButtonEvent(boolean isSigned, String nowDate, boolean isToday, DayItem dayItem) {
            this.isSigned = isSigned;
            this.nowDate = nowDate;
            this.isToday = isToday;
            this.dayItem = dayItem;
        }

        public boolean getIsSigned() {
            return isSigned;
        }

        public void setMonth(boolean isSigned) {
            this.isSigned = isSigned;
        }
    }

    /**
     * 签到事件
     */
    public static class SignedEvent {

        public DayItem dayItem;

        public SignedEvent(DayItem dayItem) {
            this.dayItem = dayItem;
        }
    }

}
