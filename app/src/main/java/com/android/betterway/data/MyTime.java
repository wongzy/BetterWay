package com.android.betterway.data;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class MyTime {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    public MyTime(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return year + "年" + month + "月" + day + "日" + hour + "时" + minute + "分";
    }
    public String getSingleTime() {
        return hour + ":" + (minute < 10 ? "0" + minute: minute);
    }
    public String getDateTime() {
        return year + "年" + month + "月" + day + "日";
    }

    /**
     * 将MyTime类转化为MyDate类
     * @return 得到的MyDate类
     */
    public MyDate parseToMyDate() {
        return new MyDate(year, month, day);
    }
}
