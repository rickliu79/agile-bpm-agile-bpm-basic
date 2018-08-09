package com.dstz.base.core.util.time;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dstz.base.api.constant.StringConstants;

/**
 * @描述 日期转换、计算工具类(可以参考使用DateUtils的方法)
 * @构建组 x5-base-core
 * @作者 hugh zhuang
 * @邮箱 zhuangxh@jee-soft.cn
 * @日期 2014-01-13 下午4:23:32
 */
public class DateUtil {

    @SuppressWarnings("unused")
    private static final Log logger = LogFactory.getLog(DateUtil.class);

    public static Calendar toCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    /**
     * 将传入时间初始化为当天的最初时间（即00时00分00秒）
     *
     * @param date 时间
     * @return 当天最初时间
     */
    public static Date setAsBegin(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 将传入时间初始化为当天的结束时间（即23时59分59秒）
     *
     * @param date 时间
     * @return 当天结束时间
     */
    public static Date setAsEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 取当前系统日期，并按指定格式或者是默认格式返回
     *
     * @param style 指定格式
     * @return 当前系统日期（字符串）
     */
    public static String getCurrentTime(String style) {
        if (StringUtils.isEmpty(style))
            style = StringConstants.DATE_FORMAT_DATETIME;
        return DateFormatUtil.format(new Date(), style);
    }

    /**
     * 取当前系统日期，并按指定格式（yyyy-MM-dd HH:mm:ss ）
     *
     * @return 当前系统日期
     */
    public static String getCurrentTime() {
        return getCurrentTime("");
    }

    /**
     * 取当前系统日期（日期格式）
     *
     * @return 当前系统日期（日期）
     */
    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 取当前系统日期
     *
     * @return 当前系统日期（Long格式）
     */
    public static long getCurrentTimeInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * 得到两日期间所有日期，含起始和结束日期
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @throws Exception
     */
    public static Date[] getDaysBetween(Date startDate, Date endDate) {
        // 计算之间有多少天
        long day = (startDate.getTime() - endDate.getTime())
                / (24 * 60 * 60 * 1000) > 0 ? (startDate.getTime() - endDate
                .getTime()) / (24 * 60 * 60 * 1000)
                : (endDate.getTime() - startDate.getTime())
                / (24 * 60 * 60 * 1000);
        // 声明日期数组
        Date[] dateArr = new Date[Integer.valueOf(String.valueOf(day + 1))];
        // 将日期加进去
        for (int i = 0; i < dateArr.length; i++) {
            if (i == 0) {
                dateArr[i] = setAsBegin(startDate);
            } else {
                Date nextDay = DateUtils.addDays(startDate, 1);
                startDate = DateUtil.setAsBegin(nextDay);
                dateArr[i] = startDate;
            }
        }
        return dateArr;
    }

    /**
     * 取得指定年月的天数
     *
     * @param year 实际年份
     * @param mon  实际月份 数值范围是1~12
     * @return
     */
    public static int getDaysOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);

    }

    /**
     * 取得某月第一天为星期几。<br>
     * 星期天为1。 星期六为7。
     *
     * @param year
     * @param mon
     * @return
     */
    public static int getWeekDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 比较两个时间大小，结束时间是否大于开始时间 传入的是字符串，会转成日期对象在进行比较。
     *
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static boolean compare(String beginDateStr, String endDateStr) {
        try {
            Date beginDate = DateFormatUtil.parse(beginDateStr);
            Date endDate = DateFormatUtil.parse(endDateStr);
            return beginDate.compareTo(endDate) < 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 比较两个时间大小，结束时间是否大于开始时间 传入的是字符串，会转成日期对象在进行比较。
     *
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static int compareTo(String beginDateStr, String endDateStr) {
        try {
            Date beginDate = DateFormatUtil.parse(beginDateStr);
            Date endDate = DateFormatUtil.parse(endDateStr);
            return beginDate.compareTo(endDate);
        } catch (Exception e) {
            return -2;
        }
    }

    /**
     * 取得日期。
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return
     */
    public static Date getDate(int year, int month, int date) {
        return getDate(year, month, date, 0, 0, 0);
    }

    /**
     * 取得日期。
     *
     * @param year      年
     * @param month     月
     * @param date      日
     * @param hourOfDay 小时
     * @param minute    分钟
     * @param second    秒
     * @return
     */
    public static Date getDate(int year, int month, int date, int hourOfDay,
                               int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date, hourOfDay, minute, second);
        return cal.getTime();
    }

    /**
     * 获取开始和结束时间的毫秒差
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static long getTime(Date startTime, Date endTime) {
        return endTime.getTime() - startTime.getTime();
    }

    /**
     * 获取指定时间到系统时间的持续时间
     *
     * @param date 指定时间
     * @return
     */
    public static String getDurationTime(Date date) {
        return getDurationTime(date, new Date());
    }

    /**
     * 获取持续时间
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getDurationTime(Date startTime, Date endTime) {
        if (startTime == null || endTime == null)
            return "";
        Long millseconds = getTime(startTime, new Date());
        return getTime(millseconds);
    }

    /**
     * 根据长整形的毫秒数返回字符串类型的时间段
     *
     * @param millseconds 毫秒数
     * @return
     */
    public static String getTime(Long millseconds) {
        StringBuffer time = new StringBuffer();
        if (millseconds == null)
            return "";
        int days = (int) (long) millseconds / 1000 / 60 / 60 / 24;
        if (days > 0)
            time.append(days).append("天");
        long hourMillseconds = millseconds - days * 1000 * 60 * 60 * 24;
        int hours = (int) hourMillseconds / 1000 / 60 / 60;
        if (hours > 0)
            time.append(hours).append("小时");
        long minuteMillseconds = millseconds - days * 1000 * 60 * 60 * 24
                - hours * 1000 * 60 * 60;
        int minutes = (int) minuteMillseconds / 1000 / 60;
        if (minutes > 0)
            time.append(minutes).append("分钟");
        return time.toString();
    }
}
