package com.dstz.base.core.util.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dstz.base.api.constant.StringConstants;


public class DateFormatUtil {
	
    /**
     * yyyy-MM-dd 时间格式
     */
    public static final DateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
            StringConstants.DATE_FORMAT_DATE);
    /**
     * yyyy-MM-dd HH:mm:ss 时间格式
     */
    public static final DateFormat DATE_FORMAT_DATETIME = new SimpleDateFormat(
            StringConstants.DATE_FORMAT_DATETIME);
    /**
     * yyyy-MM-dd HH:mm 时间格式
     */
    public static final DateFormat DATE_FORMAT_DATETIME_NOSECOND = new SimpleDateFormat(
            StringConstants.DATE_FORMAT_DATETIME_NOSECOND);

    /**
     * yyyy-MM-dd HH 时间格式
     */
    public static final DateFormat DATE_FORMAT_DATETIME_NOMINUTE = new SimpleDateFormat(
            StringConstants.DATE_FORMAT_DATETIME_NOMINUTE);
    /**
     * HH:mm:ss 时间格式
     */
    public static final DateFormat DATE_FORMAT_TIME = new SimpleDateFormat(
            StringConstants.DATE_FORMAT_TIME);
    /**
     * HH:mm 时间格式
     */
    public static final DateFormat DATE_FORMAT_TIME_NOSECOND = new SimpleDateFormat(
            StringConstants.DATE_FORMAT_TIME_NOSECOND);
    /**
     * yyyy-MM-dd HH:mm:ss.SSS 时间格式
     */
    public static final DateFormat DATE_FORMAT_TIMESTAMP = new SimpleDateFormat(
            StringConstants.DATE_FORMAT_TIMESTAMP);

    private static final Log logger = LogFactory.getLog(DateFormatUtil.class);

    /**
     * 根据日期字符串是否含有时间决定转换为日期还是日期时间还是时间
     *
     * @param dateString 时间字符串
     * @return 格式化的时间
     * @throws ParseException
     */
    public static Date parse(String dateString){
    	try {
    		if (dateString.trim().indexOf(StringConstants.SPACE) > 0
                    && dateString.trim().indexOf(StringConstants.DOT) > 0) {
                return new java.sql.Timestamp(DATE_FORMAT_TIMESTAMP.parse(
                        dateString).getTime());
            } else if (dateString.trim().indexOf(StringConstants.SPACE) > 0) {
                if (dateString.trim().indexOf(StringConstants.COLON) > 0) {
                    // 如果有两个:，则有时分秒,一个冒号只有时分
                    if (dateString.trim().indexOf(StringConstants.COLON) != dateString
                            .trim().lastIndexOf(StringConstants.COLON)) {
                        return new java.sql.Timestamp(DATE_FORMAT_DATETIME.parse(
                                dateString).getTime());
                    } else {
                        return new java.sql.Timestamp(DATE_FORMAT_DATETIME_NOSECOND
                                .parse(dateString).getTime());
                    }
                } else {
                    return new java.sql.Timestamp(DATE_FORMAT_DATETIME_NOMINUTE
                            .parse(dateString).getTime());
                }
            } else if (dateString.indexOf(StringConstants.COLON) > 0) {
                // 如果有两个:，则有时分秒,一个冒号只有时分
                if (dateString.trim().indexOf(StringConstants.COLON) != dateString
                        .trim().lastIndexOf(StringConstants.COLON)) {
                    return new java.sql.Time(DATE_FORMAT_TIME.parse(dateString)
                            .getTime());
                } else {
                    return new java.sql.Time(DATE_FORMAT_TIME_NOSECOND.parse(
                            dateString).getTime());
                }
            }
            return new java.sql.Date(DATE_FORMAT_DATE.parse(dateString).getTime());
    	}catch (Exception e) {
			throw new RuntimeException(e);
		}
        
    }


    /**
     * 按指定的格式输出string到date
     *
     * @param dateString 时间字符串
     * @param style      格式化参数 （请使用{@link StringConstants}的变量）
     * @return 格式化的时间
     * @throws ParseException
     */
    public static Date parse(String dateString, String style) {
        DateFormat dateFormat = new SimpleDateFormat(style);
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (Exception e) {
            // 如果前端时间格式有略微不匹配、进行损失精度格式化
            date = parse(dateString);
        }
        return date;
    }

    /**
     * 将日期字符串转成日期对象，该字符串支持的格式是传入的格式数组
     *
     * @param dateString 日期字符串
     * @param style      日期格式数组（请使用{@link StringConstants}的变量）
     * @return
     */
    public static Date parse(String dateString, String... style) {
        Date date = null;
        if (StringUtils.isEmpty(dateString))
            return date;
        try {
            date = DateUtils.parseDate(dateString, style);
        } catch (Exception ex) {
            logger.error("Pase the Date(" + dateString + ") occur errors:"
                    + ex.getMessage());
        }
        return date;
    }

    /**
     * 格式化输出date到string
     *
     * @param date  时间
     * @param style 格式化参数
     * @return
     */
    public static String format(Date date, String style) {
        DateFormat dateFormat = new SimpleDateFormat(style);
        return dateFormat.format(date);
    }

    /**
     * 将string(yyyy-MM-dd)转化为日期
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String dateString) throws ParseException {
        return DATE_FORMAT_DATE.parse(dateString);
    }

    /**
     * 按格式(yyyy-MM-dd)输出date到string
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT_DATE.format(date);
    }

    /**
     * 将string(yyyy-MM-dd HH:mm:ss)转化为日期
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parseDateTime(String dateString) throws ParseException {
        return DATE_FORMAT_DATETIME.parse(dateString);
    }

    /**
     * 按格式(yyyy-MM-dd HH:mm:ss )输出date到string
     *
     * @param date
     * @return
     */
    public static String formaDatetTime(Date date) {
        return DATE_FORMAT_DATETIME.format(date);
    }

    /**
     * 按格式(yyyy-MM-dd HH:mm )输出date到string
     *
     * @param date
     * @return
     */
    public static String formatTimeNoSecond(Date date) {
        return DATE_FORMAT_DATETIME_NOSECOND.format(date);
    }

    /**
     * 按格式(yyyy-MM-dd HH:mm )输出 string到date
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parseTimeNoSecond(String dateString)
            throws ParseException {
        return DATE_FORMAT_DATETIME_NOSECOND.parse(dateString);
    }

    /**
     * 根据长整型毫秒数返回时间形式。 日期格式为yyyy-MM-dd HH:mm:ss，例子:2007-01-23 13:45:21
     *
     * @param millisecond 毫秒数
     * @return
     */
    public static String format(long millisecond) {
        Date date = new Date(millisecond);
        return format(date, StringConstants.DATE_FORMAT_DATETIME);
    }

    /**
     * 根据长整型毫秒数和指定的时间格式返回时间字符串。
     *
     * @param millisecond 毫秒数
     * @param style       指定格式
     * @return
     */
    public static String format(long millisecond, String style) {
        Date date = new Date(millisecond);
        return format(date, style);
    }

    /**
     * 取当前系统日期，并按指定格式或者是默认格式返回
     *
     * @param style
     * @return
     */
    public static String getNowByString(String style) {
        if (null == style || "".equals(style)) {
            style = "yyyy-MM-dd HH:mm:ss";
        }
        return format(new Date(), style);
    }

    /**
     * 将String类型 日期格式的数据转化成指定 格式的String
     * 如yyyy-MM-dd HH:mm:ss 转化成yyyyMMdd
     *
     * @param dateString
     * @param style
     * @return
     * @throws ParseException
     */
    public static String dateStringToString(String dateString, String style) throws ParseException {
        Date newDate = parseDateTime(dateString);
        return format(newDate, "yyyyMMdd");
    }
}
