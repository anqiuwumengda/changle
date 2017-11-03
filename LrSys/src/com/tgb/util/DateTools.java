package com.tgb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class DateTools {
	/** 
	 * 默认日期格式 
	 */
	public static String DEFAULT_FORMAT = "yyyy-MM-dd";
	/**
	 * 完整的时分秒格式
	 * yyMMddHHmmss
	 */
	public static String FULLTIME_FORMAT = "yyyy-MM-dd HH-mm-ss";
	/**
	 * 获取当前系统时间
	 * @param format 日期格式
	 * @return String
	 */
	public static String getCurrentSysData(String format){
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		//calendar.add(Calendar.DATE, -1); // 得到前一天
		String currentData = new SimpleDateFormat(format)
				.format(calendar.getTime());
		return currentData;
	}
	/**
	 * 
	 * @param i 例如-1前一天
	 * @param format
	 * @return String
	 */
	public static String getCurrentSysData(int i,String format){
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		calendar.add(Calendar.DATE, i); 
		String currentData = new SimpleDateFormat(format)
				.format(calendar.getTime());
		return currentData;
	}
	public static String getCurrentSysMonth(int i,String format){
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		calendar.add(Calendar.MONTH, i); 
		String currentData = new SimpleDateFormat(format)
				.format(calendar.getTime());
		return currentData;
	}
	
	/** 
	 * 格式化日期 
	 * @param date 日期对象 
	 * @return String 日期字符串 
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat f = new SimpleDateFormat(DEFAULT_FORMAT);
		String sDate = f.format(date);
		return sDate;
	}
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat f = new SimpleDateFormat(pattern);
		String sDate = f.format(date);
		return sDate;
	}
	/** 
	 * 格式化日期 
	 * @param date 日期对象 
	 * @return String 日期字符串 
	 * @throws ParseException 
	 */
	public static Date parseDate(String strDate) throws ParseException {
		SimpleDateFormat f = new SimpleDateFormat(DEFAULT_FORMAT);
		Date date = f.parse(strDate);
		return date;
	}
	/** 
	 * 格式化日期 
	 * @param date 日期对象 
	 * @return String 日期字符串 
	 * @throws ParseException 
	 */
	public static Date parseDate(String strDate,String format) throws ParseException {
		SimpleDateFormat f = new SimpleDateFormat(format);
		Date date = f.parse(strDate);
		return date;
	}
	/**
	 * 获取指定日期对应的月份
	 * @return
	 * @author dingwm
	 * @throws ParseException 
	 * @date Nov 24, 2013
	 */
	public static int getCurrentMonthByDate(String dateStr)
	        throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(dateStr));
		return calendar.get(Calendar.MONTH) + 1;
	}
	public static int getCurrentYearByDate(String dateStr)
	        throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(dateStr));
		return calendar.get(Calendar.YEAR) ;
	}
	/**
	 * 获取当前年份
	 * @return
	 * @author dingwm
	 * @date Nov 24, 2013
	 */
	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}
	/**
	 * 获取当前月份
	 * @return
	 * @author dingwm
	 * @date Nov 24, 2013
	 */
	public static int getCurrentMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH) + 1;
	}


	/** 
	 * 获取某年第一天日期 
	 * @param year 年份 
	 * @return String 
	 */
	public static String getCurrYearFirstDay(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return formatDate(currYearFirst);
	}

	/** 
	 * 获取某年最后一天日期 
	 * @param year 年份 
	 * @return String 
	 */
	public static String getCurrYearLastDay(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();
		return formatDate(currYearLast);
	}
	/**
	 * 获取指定范围区间集合
	 * @param startMonth
	 * @param endMonth
	 * @return
	 * @author dingwm
	 * @date Nov 22, 2013
	 */
	public static List createMonths(int startMonth, int endMonth) {
		if (startMonth > endMonth) {
			return null;
		}
		List list = new ArrayList();
		for (int i = startMonth; i <= endMonth; i++) {
			list.add(i + "");
		}
		return list;
	}
	public static boolean compareDate(String date1,String date2) throws ParseException{
		Date date11 = DateTools.parseDate(date1);
		Date date22 = DateTools.parseDate(date2);
		if(date11.getTime()>date22.getTime()){
			return true;
		}
		return false;
		
	}
	public static boolean compareDate1(String date1,String date2) throws ParseException{
		Date date11 = DateTools.parseDate(date1);
		Date date22 = DateTools.parseDate(date2);
		if(date11.getTime()>=date22.getTime()){
			return true;
		}
		return false;
		
	}
	/**
	 * 与当前年月比较是否相等
	 * @param date1
	 * @return
	 * @throws ParseException
	 */
	public static boolean compareDate2(String date1) throws ParseException{
		Date date11 = DateTools.parseDate(date1,"yyyy-MM");
		Date date22 = DateTools.parseDate(DateTools.getCurrentSysData("yyyy-MM"),"yyyy-MM");
		if(date11.getTime()==date22.getTime()){
			return true;
		}
		return false;
		
	}
	public static HashMap getMinMaxDate(String dateForm,int min,int max){
		HashMap dateMap = new LinkedHashMap();
		String minDate = DateTools.getCurrentSysMonth(min, dateForm);
		String maxDate = DateTools.getCurrentSysMonth(max, dateForm);
		
		dateMap.put(minDate, minDate);
		for(int i=Integer.parseInt(minDate)+1;i<Integer.parseInt(maxDate);i++){
			dateMap.put(String.valueOf(i), String.valueOf(i));
		}
		dateMap.put(maxDate, maxDate);
		return dateMap;
	}
	public static List getMinMaxDateList(String dateForm,int min,int max){
		List lista = new ArrayList();
		String minDate = DateTools.getCurrentSysMonth(min, dateForm);
		String maxDate = DateTools.getCurrentSysMonth(max, dateForm);
		
		lista.add(minDate);
		for(int i=Integer.parseInt(minDate)+1;i<Integer.parseInt(maxDate);i++){
			lista.add( String.valueOf(i));
		}
		lista.add( maxDate);
		return lista;
	}
	public static String getStringRandom(int length) {  
        
        String val = "";  
        Random random = new Random();  
          
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
              
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
               // int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                int temp = 97;
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    }  
	public static void main(String[] args) throws ParseException{
		Date date11 = DateTools.parseDate("2017-01-01");
		Date date22 = DateTools.parseDate("2016-12-29");
		System.out.println((int)(date22.getTime()-date11.getTime())/(1000*3600*24));
		
		String s1 = "29/10/2015:17";
		Date d = DateTools.parseDate(s1, "dd/MM/yyyy:HH");
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH");
		System.out.println(f.format(d));
		String s2 = "";
		s2.substring(1, s2.length());
		boolean fa = DateTools.compareDate2(s2);
		String s = DateTools.getStringRandom(32);
		System.out.println(s);
	}
}
