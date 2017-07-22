package atlas.util;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * 日期&时间工具类
 * @author Ricky Fung
 */
public class DateUtils {

    public static final String STANDARD_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**日期加减法**/
    public static DateTime plusYears(Date date, int years){
        return new DateTime(date).plusYears(years);
    }
    public static DateTime minusYears(Date date, int years){
        return new DateTime(date).minusYears(years);
    }

    public static DateTime plusMonths(Date date, int months){
        return new DateTime(date).plusMonths(months);
    }
    public static DateTime minusMonths(Date date, int months){
        return new DateTime(date).minusMonths(months);
    }

    public static DateTime plusWeeks(Date date, int weeks){
        return new DateTime(date).plusWeeks(weeks);
    }
    public static DateTime minusWeeks(Date date, int weeks){
        return new DateTime(date).minusWeeks(weeks);
    }

    public static DateTime plusDays(Date date, int days){
        return new DateTime(date).plusDays(days);
    }
    public static DateTime minusDays(Date date, int days){
        return new DateTime(date).minusDays(days);
    }

    public static DateTime plusHours(Date date, int hours){
        return new DateTime(date).plusHours(hours);
    }
    public static DateTime minusHours(Date date, int hours){
        return new DateTime(date).minusHours(hours);
    }

    public static DateTime plusMinutes(Date date, int minutes){
        return new DateTime(date).plusMinutes(minutes);
    }
    public static DateTime minusMinutes(Date date, int minutes){
        return new DateTime(date).minusMinutes(minutes);
    }

    public static DateTime plusSeconds(Date date, int seconds){
        return new DateTime(date).plusSeconds(seconds);
    }
    public static DateTime minusSeconds(Date date, int seconds){
        return new DateTime(date).minusSeconds(seconds);
    }


    /**计算时间差*/
    public static int yearsBetween(Date d1, Date d2){
        return Years.yearsBetween(new DateTime(d1), new DateTime(d2)).getYears();
    }
    public static int monthsBetween(Date d1, Date d2){
        return Months.monthsBetween(new DateTime(d1), new DateTime(d2)).getMonths();
    }
    public static int weeksBetween(Date d1, Date d2){
        return Weeks.weeksBetween(new DateTime(d1), new DateTime(d2)).getWeeks();
    }
    public static int daysBetween(Date d1, Date d2){
        return Days.daysBetween(new DateTime(d1), new DateTime(d2)).getDays();
    }
    public static int hoursBetween(Date d1, Date d2){
        return Hours.hoursBetween(new DateTime(d1), new DateTime(d2)).getHours();
    }
    public static int minutesBetween(Date d1, Date d2){
        return Minutes.minutesBetween(new DateTime(d1), new DateTime(d2)).getMinutes();
    }
    public static int secondsBetween(Date d1, Date d2){
        return Seconds.secondsBetween(new DateTime(d1), new DateTime(d2)).getSeconds();
    }

    /**格式化日期*/
    public static String format(Date date) {
        return format(date, STANDARD_DATE_PATTERN);
    }
    public static String format(Date date, String format) {
        return new DateTime(date).toString(format);
    }

    public static String format(DateTime date) {
        return format(date, STANDARD_DATE_PATTERN);
    }
    public static String format(DateTime date, String format) {
        return date.toString(format);
    }

    /**解析日期&时间*/
    public static DateTime parseDateTime(String date, String pattern) {
    	DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
        return DateTime.parse(date, dateTimeFormatter);
    }
    public static Date parseDate(String date, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
        return DateTime.parse(date, dateTimeFormatter).toDate();
    }
}
