package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Ted on 2018/6/23.
 */
public class DateUtil {
    private static final Logger log          = LoggerFactory.getLogger(DateUtil.class);
    public static        String YEAR_FORMAT  = "yyyy";
    public static        String MONTH_FORMAT = "yyyy-MM";
    public static        String DATE_FORMAT  = "yyyy-MM-dd";
    public static        String HOUR_FORMAT  = "HH";
    public static        String TIME_FORMAT  = "yyyy-MM-dd HH:mm:ss";

    public DateUtil() {
    }

    public static long getTodayLong() {
        String todayStr = dateToString(new Date(), DATE_FORMAT);
        return convert2Long(todayStr, DATE_FORMAT);
    }

    public static String getCurrentTime() {
        return dateToString(new Date(), TIME_FORMAT);
    }

    public static String getCurrentTime(Long currentTimeMillis) {
        return convert2String(currentTimeMillis, TIME_FORMAT);
    }

    public static String dateToString(Date date, String dateFormat) {
        String str = "";
        if (date == null) {
            return str;
        } else {
            if (StringUtils.isEmpty(dateFormat)) {
                dateFormat = DATE_FORMAT;
            }

            SimpleDateFormat sf = new SimpleDateFormat(dateFormat);

            try {
                str = sf.format(date);
            } catch (Exception var5) {
                log.error("时间转换错误！", var5);
            }

            return str;
        }
    }

    public static String format(String date, String dateFormat) {
        if (StringUtils.isEmpty(date)) {
            return null;
        } else {
            if (StringUtils.isEmpty(dateFormat)) {
                dateFormat = DATE_FORMAT;
            }

            SimpleDateFormat sf = new SimpleDateFormat(dateFormat);

            try {
                return sf.format(sf.parse(date));
            } catch (ParseException var4) {
                log.error("时间转换错误！", var4);
                return null;
            }
        }
    }

    public static String format(Date date, String dateFormat) {
        if (date == null) {
            return null;
        } else {
            if (StringUtils.isEmpty(dateFormat)) {
                dateFormat = DATE_FORMAT;
            }

            SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
            return sf.format(date);
        }
    }

    public static long convert2Long(String date, String format) {
        if (!StringUtils.isEmpty(date) && !StringUtils.isEmpty(format)) {
            SimpleDateFormat sf = new SimpleDateFormat(format);

            try {
                return sf.parse(date).getTime();
            } catch (ParseException var4) {
                log.error("时间转换错误！", var4);
                return 0L;
            }
        } else {
            return 0L;
        }
    }

    public static String convert2String(long time, String format) {
        if (time > 0L && !StringUtils.isEmpty(format)) {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            Date date = new Date(time);
            return sf.format(date);
        } else {
            return null;
        }
    }

    public static String convert2String(Date date, String format) {
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            return sf.format(date);
        }
    }

    public static Date parseStringToDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        } else {
            SimpleDateFormat sf = new SimpleDateFormat();

            try {
                return sf.parse(date);
            } catch (ParseException var3) {
                log.error("时间转换错误！", var3);
                return null;
            }
        }
    }

    public static Date parseStringToDate(String date, String format) {
        if (StringUtils.isEmpty(date)) {
            return null;
        } else {
            SimpleDateFormat sf = new SimpleDateFormat(format);

            try {
                return sf.parse(date);
            } catch (ParseException var4) {
                log.error("时间转换错误！", var4);
                return null;
            }
        }
    }

    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(6, calendar.get(6) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    public static String getPastDate(Date date, int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(6, calendar.get(6) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    public static String getPastDate(String dateStr, int past) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(dateStr));
        calendar.set(6, calendar.get(6) - past);
        Date today = calendar.getTime();
        String result = format.format(today);
        return result;
    }

    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(6, calendar.get(6) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    public static String getFetureDate(Date date, int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(6, calendar.get(6) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    public static Long getPastYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, calendar.get(1) - year);
        return Long.valueOf(calendar.getTime().getTime());
    }

    public static void main(String[] args) {
        String year = getYear(new Date());
        System.out.println(year);
        System.out.println(getMonth(new Date()));
    }

    public static String getFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int firstDay = cal.getActualMinimum(5);
        cal.set(5, firstDay);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth;
    }

    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        int firstDay = cal.getActualMinimum(5);
        cal.set(5, firstDay);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth;
    }

    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        int lastDay = cal.getActualMaximum(5);
        cal.set(5, lastDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    public static String getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int lastDay = cal.getActualMaximum(5);
        cal.set(5, lastDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(6);
        int day2 = cal2.get(6);
        int year1 = cal1.get(1);
        int year2 = cal2.get(1);
        if (year1 == year2) {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        } else {
            int timeDistance = 0;

            for (int i = year1; i < year2; ++i) {
                if ((i % 4 != 0 || i % 100 == 0) && i % 400 != 0) {
                    timeDistance += 365;
                } else {
                    timeDistance += 366;
                }
            }

            return timeDistance + (day2 - day1);
        }
    }

    public static int differentDaysByMillisecond(Date date1, Date date2) {
        Date tempDate1 = new Date(date1.getTime());
        Date tempDate2 = new Date(date2.getTime());
        tempDate1.setMinutes(0);
        tempDate1.setHours(0);
        tempDate1.setSeconds(0);
        System.out.println(tempDate1);
        tempDate2.setMinutes(0);
        tempDate2.setHours(0);
        tempDate2.setSeconds(0);
        System.out.println(tempDate2);
        int days = (int) ((tempDate2.getTime() - tempDate1.getTime()) / 86400000L);
        return days;
    }

    public static String getMonthStartDay() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(5, 1);
        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(simpleFormate.format(calendar.getTime()));
        return simpleFormate.format(calendar.getTime());
    }

    public static String getMonthEndDay() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
        calendar.set(5, 1);
        calendar.roll(5, -1);
        System.out.println(simpleFormate.format(calendar.getTime()));
        return simpleFormate.format(calendar.getTime());
    }

    public static String getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return "" + calendar.get(1);
    }

    public static String getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(2) + 1;
        return month > 9 ? "" + month : "0" + month;
    }
}
