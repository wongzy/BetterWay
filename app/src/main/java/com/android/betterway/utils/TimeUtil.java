package com.android.betterway.utils;



import com.android.betterway.data.MyDate;
import com.android.betterway.data.MyTime;

import java.util.Calendar;
import java.util.Date;


/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public final class TimeUtil {
    public static final int ERRORCOUNT = -10000;
    private TimeUtil() {
    }

    /**
     * 获得当前日期
     * @return 当前日期
     */
    public static MyDate getDayTime() {
        Calendar calendar = Calendar.getInstance();
        return new MyDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }
    public static long getDayLong() {
        return new Date().getTime();
    }
    /**
     * 获得当前时间（精确到分钟）
     * @return 当前时间
     */
    public static MyTime getMinuteTime() {
        Calendar calendar = Calendar.getInstance();;
        return new MyTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
    }

    /**
     * 获得两个日期中间相差的日期数
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 相差天数（算上开始那天）
     */
    public static int getDayDuration(MyDate startDate, MyDate endDate) {
        if (startDate.getYear() == 0 && startDate.getMonth() == 0 && startDate.getDay()==0) {
            return 8;
        }
        Date date1 = new Date(startDate.getYear(), startDate.getMonth()-1, startDate.getDay());
        Date date2 = new Date(endDate.getYear(), endDate.getMonth()-1, endDate.getDay());
        int i = (int) (date2.getTime() - date1.getTime()) / (1000 * 3600 * 24);
        LogUtil.i("getDayDuration", i + "天");
        return i;
    }

    /**
     * 获得两个时间之间相隔的分钟数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 相隔分钟
     */
    public static long getTimeDuration(MyTime startTime, MyTime endTime) {
        int duration;
        if (startTime.getYear() == endTime.getYear() && startTime.getMonth() == endTime.getMonth()
                && startTime.getDay() == endTime.getDay()) {
            duration = endTime.getMinute() - startTime.getMinute() + (endTime.getHour() - startTime.getHour()) * 60;
        } else {
            duration = ERRORCOUNT;
        }
        return duration;
    }

    /**
     * 某个事件加上消耗分钟数后的时间
     * @param myTime 某个时间
     * @param duration 消耗分钟数
     * @return 获得的新时间
     */
    public static MyTime myTimeAddDuration(MyTime myTime, int  duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(myTime.getYear(), myTime.getMonth() - 1, myTime.getDay(), myTime.getHour(), myTime.getMinute());
        calendar.add(Calendar.MINUTE, duration);
        return new MyTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
    }

    /**
     * 将int转化为MyDate类，便于计算时间
     * @param dateInt int格式的日期
     * @return Mydate格式的日期
     */
    public static MyDate intToMyDate(int dateInt) {
        int year = dateInt / 10000;
        dateInt -= year * 10000;
        int month = dateInt / 100;
        dateInt -= month * 100;
        int day = dateInt;
        return new MyDate(year, month, day);
    }

    public static String minteintToString(int minutes) {
        int hour = minutes / 60;
        int minute = minutes % 60;
        StringBuffer stringBuffer = new StringBuffer("");
        if (hour > 0) {
            String Hour = hour + "小时";
            stringBuffer.append(Hour);
        }
        if (minute > 0) {
            String Minute = minute + "分钟";
            stringBuffer.append(Minute);
        }
        return stringBuffer.toString();
    }

    public static int SecondsToMinutes(int seconds) {
        return seconds / 60;
    }

    public static MyTime longToMyTime(long l) {
        int hour = (int) l / 100;
        int min = (int) l % 100;
        return new MyTime(hour, min);
    }

    public static MyTime longToTotalMyTime(long l) {
        long year = l / 100000000L;
        l -= year * 100000000L;
        long month = l / 1000000L;
        l -= month * 1000000L;
        long day = l /10000L;
        l -= day * 10000L;
        long hour = l / 100L;
        l -= hour * 100L;
        long minute = l;
        return new MyTime((int)year, (int)month, (int)day, (int)hour, (int)minute);
    }
}
