
package com.weiweisc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

    public static String currentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
    
    // yyyy-MM-dd HH:mm:ss 
    public static long datestrToLong(String str) {
        return datestrToLong(str,"yyyy-MM-dd HH:mm:ss");
    }

    // yyyy-MM-dd HH:mm:ss 
    public static long datestrToLong(String str, String dateformat) {

        SimpleDateFormat format = new SimpleDateFormat(dateformat);
        if (str == null)
            return 0;
        try {
            return format.parse(str).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }


    public static Date parseDate(String dateStr) {
        Date result = null;
        if (dateStr != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                
                result = format.parse(dateStr);
            } catch (ParseException e) {
                System.err.println(e);
                result = null;
            }
        }
        return result;
    }

  
    public static Date parseDateTime(String dateStr) {
        return new Date(datestrToLong(dateStr, "yyyy-MM-dd HH:mm:ss"));
    }
    
    public static Date parseDateTimes(String dateStr) {
        return new Date(datestrToLong(dateStr, "yyyy-MM-dd"));
    }
   
    
    public static String formatDate(long time) {
        return formatDate(time, "yyyy-MM-dd HH:mm");
    }
    
    public static String formatDate(long time, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatDate(calendar.getTime(), format);
    }

    public static String formatDate(Date date, String format) {
        try {
            SimpleDateFormat formater = new SimpleDateFormat(format);
            return formater.format(date);
        } catch (Exception e) {
            return "";
        }
    }
    
    public static String formatDate(Date date) {
       return formatDate(date,"yyyy-MM-dd HH:mm:ss");
    }

    // 将 时间戳（秒级别）转换成 yyyy-MM-dd HH:mm:ss
    public static String convertDate(int time) {
        Calendar calendar = Calendar.getInstance();
        long l = 1000;
        calendar.setTimeInMillis(time * l);
        return formatDate(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前 秒
     *
     * @return
     */
    public static Integer getTime() {
        return (int) Math.round(System.currentTimeMillis() / 1000.0);
    }

    public static List<Calendar> findCalendar(Calendar begin, Calendar end, List<Integer> weeks) {
        List<Calendar> result = new ArrayList<Calendar>();
        if (begin != null && end != null && weeks != null) {

            Long beginTimeInMillis = begin.getTimeInMillis();

            Long endTimeInMillis = end.getTimeInMillis();

            Long oneDay = 1000 * 60 * 60 * 24L;

            Long time = beginTimeInMillis;

            while (time <= endTimeInMillis) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);

                int i = calendar.get(Calendar.DAY_OF_WEEK);

                if (weeks.contains(i)) {
                    result.add(calendar);
                }
                time += oneDay;
            }
        }

        return result;
    }


    public static List<Calendar> findCalendar(Date beginDate, Date endDate, Integer intervalMinutes) {
        List<Calendar> result = new ArrayList<Calendar>();

        if (beginDate == null || endDate == null || intervalMinutes == null) {
            return result;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        ;

        Long begin = calendar.getTimeInMillis();

        calendar.setTime(endDate);

        Long end = calendar.getTimeInMillis();

        Long interval = 1000L * 60 * intervalMinutes;

        Long time = begin;

        while (time < end) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            result.add(c);
            time += interval;
        }

        return result;
    }

    public static Integer convertAgeByBirthday(Date birthday) {
        Integer age = null;
        if (birthday != null) {
            Calendar today = Calendar.getInstance();

            Calendar birth = Calendar.getInstance();

            birth.setTime(birthday);

            age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
            if (age < 0) {
                age = null;
            }
        }
        return age;
    }

    public static Date getBirtydayByAge(Integer age) {
        Date result = null;
        if (age == null || age <= 0) {
            result = new Date();
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - age);

            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            result = calendar.getTime();
        }
        return result;
    }

    public static Boolean isToday(Date date) {
        Boolean result = null;
        if (date != null) {
            String str = formatDate(date, "yyyy-MM-dd");
            String today = formatDate(new Date(), "yyyy-MM-dd");
            result = str.equals(today);
        }

        return result;
    }

    /**
     * 获取两个日期之间间隔的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getBetweenDays(Date date1, Date date2) {
        Long time1 = date1.getTime();
        Long time2 = date2.getTime();
        Long timeDiff = time1 - time2;
        Long days = Math.abs(timeDiff / (1000 * 3600 * 24));
        return Integer.valueOf(String.valueOf(days));
    }

    /**
     * 获取多少天以前或多少天以后的日期(时间为00:00:00)
     *
     * @param referenceDate 以这个日期为基准
     * @param days          N天以后正整数, N天以前负整数
     * @return
     */
    public static Date getNDaysDate(Date referenceDate, int days) {
        Calendar calendar = Calendar.getInstance();
        Date date = DateUtils.parseDate(DateUtils.formatDate(referenceDate, "yyyy-MM-dd"));
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date daysAgeDate = calendar.getTime();
        return daysAgeDate;
    }
   
    
    /**
     * 获取以"参考日期"为准, 该天在本周的第一天日期(第一天为周日)
     *
     * @param referenceDate 参考日期
     * @return
     */
    public static Date getBeginWeekDate(Date referenceDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(referenceDate);
        return getNDaysDate(referenceDate, -(calendar.get(Calendar.DAY_OF_WEEK) - 1));
    }
    
    /**
     * 获取当年的第一天
     * @param year
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }
     
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }
    /**
     * 通过标识获取日期   1 当天  2 本周   3 本月  4 今年
     * @param year 年份
     * @return Date
     */
    public static Date getDateByTime(Integer time){
    	if(time==null) time=1;
    	 Date beginDate=null;
	     if(time==1){
	    	  beginDate = getNDaysDate(new Date(), 0);
	     }else if(time==2){
	    	  beginDate = getBeginWeekDate(new Date());
	     }else if(time==3){
	    	 beginDate = getBeginMonthDate(new Date());
	     }else{
	    	 beginDate = getCurrYearFirst(); 
	     }
    	return beginDate;
    }

    /**
     * 获取以"参考日期"为准, 该天在本周的第一天日期(第一天为周日)
     *
     * @param referenceDate 参考日期
     * @return
     */
    public static Date getBeginMonthDate(Date referenceDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(referenceDate);
        return getNDaysDate(referenceDate, -(calendar.get(Calendar.DAY_OF_MONTH) - 1));
    }

    public static void main(String[] args) {
    	
    	Date tm=new Date();
    	
    	Calendar calendar = Calendar.getInstance(); 
    	 
    	
    	calendar.setTime(new Date());
 
    	int year = calendar.get(Calendar.YEAR); //获取年;
    	int month = calendar.get(Calendar.MONTH); //获取月;
    	int date = calendar.get(Calendar.DATE); //获取天;
    	int hour = calendar.get(Calendar.HOUR_OF_DAY); //获取小时;
    	int minute = calendar.get(Calendar.MINUTE); //获取分钟;
    	int second = calendar.get(Calendar.SECOND); //获取秒钟;
    	 
    	
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	
    	System.out.println(df.format(calendar.getTime()));
    	System.out.println(df.format(tm.getTime()+24*3600*1000));
    	
    	System.out.println(year);
    	System.out.println(month);
    	System.out.println(date);
    	System.out.println(hour);
    	System.out.println(minute);
    	System.out.println(second);
    	
//        System.out.println(formatDate(parseDate("2016-02-50 12:12:12"), "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(formatDate(getBeginMonthDate(parseDate("2016-02-50 12:12:12")), "yyyy-MM-dd HH:mm:ss"));

        //String dateStr = DateUtils.convertDate((int)(System.currentTimeMillis()/1000l));
        //System.out.println(DateUtils.parseDate(dateStr));
        // System.out.println(convertDate(getTime()));
        //System.out.println(getTime());

		/*
        Date birthday = parseDateTime("2014-05-09 08:30:00");
		System.out.println(convertAgeByBirthday(birthday));
		System.exit(-1);
		
		List<Calendar> list = findCalendar(8, 10, 20);
		Date beginDate = parseDateTime("2015-05-09 08:30:00");
		
		
		Date endDate = parseDateTime("2015-05-09 10:20:00");
		list = findCalendar(beginDate, endDate, 20);
		//System.out.println(list.size());
		for(Calendar c:list){
			System.out.println(formatDate(c.getTime(), "yyyy-MM-dd HH:mm:ss"));
		}
		*/

        //Date date = parseDateTime("2015-06-14 08:30:00");
        //System.out.println(isToday(date));

//        Date date = getBirtydayByAge(23);
//
//        System.out.println(formatDate(date, "yyyy-MM-dd HH:mm:ss"));
//
//        System.out.println(convertAgeByBirthday(date));
    	  System.exit(0);
    }

}
