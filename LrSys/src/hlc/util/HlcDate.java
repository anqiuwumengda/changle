package hlc.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

public class HlcDate {
	//定义格式化的日期的实例
	  /** 临时格式化日期类 */
	  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	  /**  临时日历类 */
	  private Calendar cld = Calendar.getInstance();

	  /**  临时日期类 */
	  private Date dt = null;

	  /**  临时开始日期类 */
	  private Date dtBegin = null;

	  /**  临时结束日期类 */
	  private Date dtEnd = null;

	  /**  临时字符串变量 */
	  private String s = "";

	  /**
	   * 构造器
	   */
	  public HlcDate() {
	  }

	  /**
	   * 判断日期的合法性
	   * @param sDate 日期
	   *
	   * @throws Exception
	   */
	  public void checkDate(String sDate)
	      throws Exception
	  {
	    if (sDate == null)
	      throw new Exception("日期为空");

	    if (sDate.length() != 8)
	      throw new Exception("日期必须为8位");

	  }

	  /**
	   * 获得给定日期的，n几天后的日期
	   * @param sDate 原日期
	   * @param n 天数
	   * @return 计算后得的日期
	   * @throws Exception
	   */
	  public String getNextDate(String sDate, int n)
	      throws Exception
	  {
	    //把日期的字符串转化成日期的实例
	    dt = sdf.parse(sDate, new ParsePosition(0));
	    cld.setTime(dt);
	    //计算新的Calendar
	    cld.add(Calendar.DATE, n);
	    return sdf.format(cld.getTime());
	  }


	  /**
	   * 取得两个日期之间的所有日期（包括本身）---自然日期
	   * add by ck 2002-12-9 10:50
	   * @param sBeginDate 开始日期
	   * @param sEndDate   结束日期
	   * @return           两个日期之间的所有日期（包括本身）
	   * @throws Exception
	   */
	  public String[] getNaturalBetweenDates(String sBeginDate, String sEndDate)
	      throws Exception
	  {
	    String aReturn[] = null;

	    if (sEndDate.compareTo(sBeginDate) < 0)
	      throw new Exception("结束日期不能小于开始日期。");

	    int iBetween = 0;

	    try
	    {
	      //取出两个日期相差的天数
	      dtBegin = sdf.parse(sBeginDate, new ParsePosition(0));
	      dtEnd = sdf.parse(sEndDate, new ParsePosition(0));
	      iBetween = (new Long( (dtEnd.getTime() - dtBegin.getTime()) / 1000 / 60 / 60 / 24 + 1)).intValue();

	      aReturn = new String[iBetween];

	      aReturn[0] = sBeginDate;
	      for (int i = 1; i < iBetween; i++)
	      {
	        aReturn[i] = getNextDate(sBeginDate, i);
	      }
	    }
	    catch (Exception e)
	    {
	      throw new Exception("IRPDate.java:getNaturalBetweenDates方法出错：" + e);
	    }

	    return aReturn;

	  }

	 

	  /**
	   * 返回当前时间　HHmmss 24小时制
	   * @return 当前时间　HHmmss
	   * @throws Exception
	   */
	  public String getCurrentTime()
	      throws Exception
	  {
	    SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
	    s = sdf.format(Calendar.getInstance().getTime());
	    return s;
	  }

	  /**
	   * 返回当前日期     yyyyMMdd
	   * @return 当前日期 yyyyMMdd
	   * @throws Exception
	   */
	  public String getCurrentDate()
	      throws Exception
	  {
	    //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    s = sdf.format(Calendar.getInstance().getTime());
	    return s;
	  }

	  /**
	   * 返回昨天日期     yyyyMMdd
	   * @throws Exception
	   */
	  public static String getPreDate()
	      throws Exception
	  {
	    //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    Date d = new Date();
	    d = new Date(d.getTime() - 24 * 60 * 60 * 1000);
	    String ss = new SimpleDateFormat("yyyyMMdd").format(d);
	    return ss;
	  }


	  /**
	   * Advances this day by n days.
	   * 返回今天以后的几天是哪天
	   * @param n the number of days by which to change this day
	   * @return String date.
	   * @throws Exception
	   */
	  public String getNextDate(int n)
	      throws Exception
	  {
	    dt = sdf.parse(getCurrentDate(), new ParsePosition(0));
	    cld.setTime(dt);
	    cld.add(Calendar.DATE, n);
	    s = sdf.format(cld.getTime());
	    return s;
	  }

	  /**
	   * 获取从calendar的日期到January 1, 1970, 0:00:00 GMT的milliseconds
	   * @param calendar Calendar
	   * @return 长整数milliseconds
	   */
	  private long getTimeMillis(Calendar calendar)
	  {
	    String sCalendar = calendar.toString();
	    String sTimeInMillis = sCalendar.substring(sCalendar.indexOf("=") + 1, sCalendar.indexOf(","));
	    long lTimeInMillis = Long.parseLong(sTimeInMillis);
	    ////System.out.println();
	    return lTimeInMillis;
	  }

	  /**
	   * 获得两个日期之间的天数（整形）
	   * @param sBiginDate 开始日期
	   * @param sEndDate 结束日期
	   * @return 两个日期之间的天数（整形）
	   * @throws Exception
	   */
	  public int daysBetween(String sBiginDate, String sEndDate)
	      throws Exception
	  {
	    try
	    {
	      dt = sdf.parse(sBiginDate, new ParsePosition(0));
	      cld.setTime(dt);
	      long lTime1 = getTimeMillis(cld);

	      dt = sdf.parse(sEndDate, new ParsePosition(0));
	      cld.setTime(dt);
	      long lTime2 = getTimeMillis(cld);

	      return (int) ( (lTime2 - lTime1) / 1000 / 60 / 60 / 24);
	    }
	    catch (Exception e)
	    {
	      throw e;
	    }
	  }
	  
	  /**
	   * 获得本月月末日
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getMonthEndDate(String sDate)
	      throws Exception
	  {
		  String tmpD = sDate.substring(0,6);
		  return this.getNextDate(tmpD+"01",-1);

	  }
	  /**
	   * 判断是否此日期为月末
	   * @param sDate 日期
	   * @throws Exception
	   * @return boolean
	   */
	  private boolean checkMonthEndDate(String sDate)
	      throws Exception
	  {
	    return getMonthEndDate(sDate).equals(sDate);

	  }

	 
	  /**
	   * 判断是否为年初
	   * @param sDate 日期
	   * @return 是否为年初
	   * @throws Exception
	   */
	  public boolean isYearEarlier(String sDate)
	      throws Exception
	  {
	    checkDate(sDate);

	    s = sDate.substring(4, 6);
	    return s.equals("01") && checkMonthEarlierDate(sDate);

//	    return sDate.substring(4).equals("0101") ;
	  }


	  /**
	   * 判断是否为季末
	   * @param sDate 日期
	   * @return 是否为季末
	   * @throws Exception
	   */
	  public boolean isQuarterEnd(String sDate)
	      throws Exception
	  {
	    checkDate(sDate);

	    s = sDate.substring(4, 6);
	    return (s.equals("03") || s.equals("06") || s.equals("09") || s.equals("12"))
	        &&
	        checkMonthEndDate(sDate);

//	    s = sDate.substring(4) ;
//	    return s.equals("0331") || s.equals("0630") || s.equals("0930") || s.equals("1231");
	  }


	  /**
	   * 获得上月月末日
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getPreMonthEndDate(String sDate)
	      throws Exception
	  {
		  checkDate(sDate);
		  if(sDate.substring(4).equals("1331"))
			  sDate = sDate.substring(0, 4) + "1231";
		  String tmpD = sDate.substring(0,6);
		  return this.getNextDate(tmpD+"01",-1);
	  }

	  
	  /**
	   * 获得上月月末日_13期
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getPreMonthEndDate_13(String sDate)
	      throws Exception
	  {	
		  if(sDate.substring(4).equals("1331"))
			  s=sDate.substring(0,4)+"1130";
		  else{
			  s=getPreMonthEndDate(sDate);
			  if(s.substring(4).equals("1231"))
				  s=s.substring(0,4)+"1331";
		  }
		  return s;
	  }
	  
	  /**
	   * 获得上季季末日
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getPreQuarterEndDate(String sDate)
	      throws Exception
	  {
	    checkDate(sDate);
	    if(sDate.substring(4).equals("1331"))
	    	sDate = sDate.substring(0, 4) + "1231";
	    s = sDate.substring(4, 6);

	    if (s.equals("01") || s.equals("02") || s.equals("03"))
	    {
	      //返回上年年底日
	      return getPreYearEndDate(sDate);
	    }
	    else if (s.equals("04") || s.equals("05") || s.equals("06"))
	    {
	      return sDate.substring(0, 4) + "0331";
//	      return sDate.substring(0,4) + "0331" ;
	    }
	    else if (s.equals("07") || s.equals("08") || s.equals("09"))
	    {
	      return sDate.substring(0, 4) + "0630";
//	      return sDate.substring(0,4) + "0630" ;
	    }
	    else if (s.equals("10") || s.equals("11") || s.equals("12"))
	    {
	      return sDate.substring(0, 4 )+ "0930";
//	      return sDate.substring(0,4) + "0930" ;
	    }
	    else
	    {
	      throw new Exception("不存在的月份：" + s);
	    }

	  }

	  /**
	   * 获得上季季末日_13期
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getPreQuarterEndDate_13(String sDate)
	      throws Exception
	  {
		  if(sDate.substring(4).equals("1331"))
			  s=sDate.substring(0,4)+"0930";
		  else{
			  s=getPreQuarterEndDate(sDate);
			  if(s.substring(4).equals("1231"))
				  s=s.substring(0,4)+"1331";
		  }
		  return s;
	  }
	  
	  /**
	   * 获得上年同期日
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getPreYearByDate(String sDate)
	      throws Exception
	  {
	    checkDate(sDate);
	    if(sDate.substring(4).equals("1331")){
	    	 int a=Integer.parseInt(sDate.substring(0,4))-1;
			 s=a+"1331";
			 return s;
	    }
	    dt = sdf.parse(sDate, new ParsePosition(0));
	    cld.setTime(dt);
	    //计算新的Calendar
	    cld.add(Calendar.YEAR, -1);
	    return sdf.format(cld.getTime());
	  }
	  
	  
	  /**
	   * 获得上月同期日
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getPreMonthByDate(String sDate)
	      throws Exception
	  {
	    checkDate(sDate);
	    dt = sdf.parse(sDate, new ParsePosition(0));
	    cld.setTime(dt);
	    //计算新的Calendar
	    cld.add(Calendar.MONTH, -1);
	    return sdf.format(cld.getTime());
	  }
	  
	  /**
	   * 获得上年年末日
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getPreYearEndDate(String sDate)
	      throws Exception
	  {
	    checkDate(sDate);
	    if(sDate.substring(4).equals("1331"))
	    	sDate = sDate.substring(0, 4) + "1231";
	    s = sDate.substring(0, 4) + "0101";
	    s = getNextDate(s, -1);
	    return s;
	  }
	  
	  /**
	   * 获得上年年末日_13期
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getPreYearEndDate_13(String sDate)
	      throws Exception
	  {
		  if(sDate.substring(4).equals("1331")){
			  int a=Integer.parseInt(sDate.substring(0,4))-1;
			  s=a+"1331";
		  }else{
			  s=getPreYearEndDate(sDate);
			  if(s.substring(4).equals("1231"))
				  s=s.substring(0,4)+"1331";
		  }
		  return s;
	  }
	  
	  /**
	   * 获得上半年年末日
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getPreHalfYearEndDate(String sDate)
	      throws Exception
	  {
	    checkDate(sDate);
	    if(sDate.substring(4).equals("1331"))
	    	sDate = sDate.substring(0, 4) + "1231";
	    String temp = sDate.substring(4,6);
	    if(Integer.parseInt(temp)<=6){
	    	return this.getPreYearEndDate(sDate);
	    }else{
	    	 s = sDate.substring(0, 4) + "0630";
	    	 return s;
	    }
	   
	  }

	  /**
	   * 获得上半年年末日_13期
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getPreHalfYearEndDate_13(String sDate)
	      throws Exception
	  {
		  if(sDate.substring(4).equals("1331"))
			  s=sDate.substring(0,4)+"0630";
		  else{
			  s=getPreHalfYearEndDate(sDate);
			  if(s.substring(4).equals("1231"))
				  s=s.substring(0,4)+"1331";
		  }
		  return s;
	  }
	  
	  /**
	   * 获得本年年初日
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getYearEarlierDate(String sDate)
	      throws Exception
	  {
	    checkDate(sDate);

	    return getMonthEarlierDate(sDate.substring(0, 4) + "0101");

//	    return sDate.substring(0,4) + "0101" ;
	  }


	  /**
	   * 获得本月月末日--自然日期
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getNaturalMonthEndDate(String sDate)
	      throws Exception
	  {
	    s = sDate.substring(4, 6);
	    if (s.equals("12"))
	      return sDate.substring(0, 4) + "1231";
	    else
	    {
	      //取出下月月份
	      s = String.valueOf(Integer.parseInt(s) + 1);
	      s = "0" + s;
	      s = s.substring(s.length() - 2);
	      //取出下月月初 --- 8位日期
	      s = sDate.substring(0, 4) + s + "01";
	      //获得本月月末
	      return getNextDate(s, -1);

	    }
	  }

	  /**
	   * 获得本月月初日
	   * @param sDate 日期
	   * @return String
	   * @throws Exception
	   */
	  public String getMonthEarlierDate(String sDate)
	      throws Exception
	  {
		  return sDate.substring(0, 6) + "01";
	  }

	  /**
	    获取格式化日期
	    @param tmpDate 传入的日期字符串可以为8位年月日，也可以为""
	    @param tmpFormat 分隔符号，可以是"-"、"/"等。
	    @return 若传入的日期字符串为""，按传入的格式返回当前日期，若为8位日期，按传入的格式返回该日期
	    @exception Exception
	   */
	  public static String getFormatDate(String tmpDate, String tmpFormat)
	      throws Exception
	  {
	    if (! (tmpDate.length() == 0 || tmpDate.length() == 8))
	      throw new Exception("日期必须为8位，格式为: yyyymmdd；或为\"\"，表示取当前日期!");

	    if (tmpDate.length() == 0)
	    {
	      //获取日期，生成格式为 2001-03-09
	      Calendar calNow = Calendar.getInstance();

	      String yy = "", mm = "", dd = "";
	      int y = calNow.get(calNow.YEAR);
	      int m = calNow.get(calNow.MONTH) + 1;
	      int d = calNow.get(calNow.DAY_OF_MONTH);

	      yy = String.valueOf(y);
	      mm = String.valueOf(100 + m).substring(1);
	      dd = String.valueOf(100 + d).substring(1);
	      return yy + tmpFormat + mm + tmpFormat + dd;
	    }
	    else
	    {
	      return tmpDate.substring(0, 4) + tmpFormat + tmpDate.substring(4, 6) + tmpFormat + tmpDate.substring(6, 8);
	    }

	  }

	  /**
	    获取格式化日期YYYYDDMM
	    @param tmpDate 传入的日期字符串
	    @param tmpFormat 分隔符号，可以是"-"、"/"等。
	    @return 若传入的日期字符串为""，按传入的格式返回当前日期，若为8位日期，按传入的格式返回该日期
	    @exception Exception
	   */
	  public static String getFormatYYYYMMDDDate(String tmpDate)
	      throws Exception
	  {
	     System.out.println("tmpDate="+tmpDate);
	    if (! (tmpDate.length() == 0 || tmpDate.length() == 10))
	      throw new Exception("日期必须为10位，格式为: yyyy/mm/dd；或为\"\"，表示取当前日期!");

	    if (tmpDate.length() == 0)
	    {
	      //获取日期，生成格式为 20010309
	      Calendar calNow = Calendar.getInstance();

	      String yy = "", mm = "", dd = "";
	      int y = calNow.get(calNow.YEAR);
	      int m = calNow.get(calNow.MONTH) + 1;
	      int d = calNow.get(calNow.DAY_OF_MONTH);

	      yy = String.valueOf(y);
	      mm = String.valueOf(100 + m).substring(1);
	      dd = String.valueOf(100 + d).substring(1);
	      return yy + mm + dd;
	    }
	    else
	    {
	      return tmpDate.substring(0, 4) +  tmpDate.substring(5, 7) +  tmpDate.substring(8, 10);
	    }

	  }


	  public static String getChinaDate(String tmpDate)
	      throws Exception
	  {
	    if (tmpDate == null) tmpDate = "";
	    if (tmpDate.length() < 8)return tmpDate;
	    String rDate = "";
	    rDate = tmpDate.substring(0, 4) + "年";
	    rDate = rDate + tmpDate.substring(4, 6) + "月";
	    rDate = rDate + tmpDate.substring(6, 8) + "日";

	    return rDate;
	  }

	  /**
	    获取格式化时间
	    @param tmpTime 传入的时间字符串可以为8位时间，也可以为""
	    @param tmpFormat 分隔符号，可以是":"、"."等。
	    @return 若传入的时间字符串为""，按传入的格式返回当前时间，若为8位时间，按传入的格式返回该时间
	    @exception Exception
	   */
	  public static String getFormatTime(String tmpTime, String tmpFormat)
	      throws Exception
	  {
	    if (! (tmpTime.length() == 0 || tmpTime.length() == 6))
	      throw new Exception("时间必须为6位，格式为: hhmmss；或为\"\"，表示取当前时间!");

	    if (tmpTime.length() == 0)
	    {
	      //获取日期和时间，生成格式为 2001-03-09  09:08:07
	      Calendar calNow = Calendar.getInstance();

	      String hh = "", min = "", ss = "";
	      int h = calNow.get(calNow.HOUR_OF_DAY);
	      int mi = calNow.get(calNow.MINUTE);
	      int s = calNow.get(calNow.SECOND);
	      hh = String.valueOf(100 + h).substring(1);
	      min = String.valueOf(100 + mi).substring(1);
	      ss = String.valueOf(100 + s).substring(1);

	      return hh + tmpFormat + min + tmpFormat + ss;
	    }
	    else
	    {
	      return tmpTime.substring(0, 2) + tmpFormat + tmpTime.substring(2, 4) + tmpFormat + tmpTime.substring(4, 6);
	    }

	  }

	  /**
	   * 比较给定与当前系统日期的差值
	   * @param sDate 日期
	   * @return 相差的天数
	   * @throws Exception
	   */
	  public int compareCurrentDate(String sDate)
	      throws Exception
	  {
	    checkDate(sDate);

	    return daysBetween(getCurrentDate(), sDate);
	  }



	  /**
	   * 判断是否此日期为月初
	   * @param sDate 日期
	   * @throws Exception
	   * @return boolean
	   */
	  private boolean checkMonthEarlierDate(String sDate)
	      throws Exception
	  {
	    return getMonthEarlierDate(sDate).equals(sDate);

	  }


	  /**
	   * 判断一个数组是否为空
	   * @param aSource 日期
	   * @throws Exception
	   */
	  public void checkArray(String[] aSource)
	      throws Exception
	  {
	    if (aSource == null || aSource.length == 0)
	      throw new Exception("参数数组不能为空");
	  }


	  /**
	   * 生成中文日期：该方法根据传人的8位有效日期生成该日期的中文名称
	   * @param sDate 8位有效日期
	   * @return String
	   * @throws Exception
	   */
	  public String makeCnDate(String sDate)
	      throws Exception
	  {
	    if (sDate.length() == 4)
	    {
	      return sDate + "年";
	    }
	    else if (sDate.length() == 6)
	    {
	      if (sDate.substring(4, 5).equals("Q") || sDate.substring(4, 5).equals("q"))
	        return sDate.substring(0, 4) + "年" + sDate.substring(5, 6) + "季度";
	      else
	        return sDate.substring(0, 4) + "年" + sDate.substring(4, 6) + "月";
	    }
	    else if (sDate.length() == 8)
	    {
	      return sDate.substring(0, 4) + "年" + sDate.substring(4, 6) + "月" + sDate.substring(6, 8) + "日";
	    }
	    else
	    {
	      throw new Exception("日期格式不对，不能生成中文日期");
	    }
	  } //end of makeCnDate()



	  /**
	   * 是否周末
	   * @param chDate String
	   * @return boolean
	   */
	  public static boolean isWeekEnd(String chDate)
	  {
	    Date dn = new java.text.SimpleDateFormat("yyyyMMdd").parse(chDate, new ParsePosition(0));
	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddWE", new java.text.DateFormatSymbols(java.util.Locale.US));
	    String s = sdf.format(dn);
	    int n = 0;
	    String week = s.substring(9, 12).toLowerCase();
	    return week.equals("fri");
	  }

	  /**
	   * 是否月末
	   * @param chDate String
	   * @return boolean
	   */
	  public static boolean isMonthEnd(String chDate)
	  {
	    Date dn = new java.text.SimpleDateFormat("yyyyMMdd").parse(chDate, new ParsePosition(0));
	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd", new java.text.DateFormatSymbols(java.util.Locale.US));
	    String s2 = sdf.format(new Date(dn.getTime() + (long) (24 * 60 * 60 * 1000)));
	    return s2.substring(6, 8).equals("01");
	  }

	  /**
	   * 是否旬末
	   * @param chDate String
	   * @return boolean
	   */
	  public static  boolean isTenDaysEnd(String chDate)
	  {
	    String day = chDate.substring(6, 8);
	    return day.equals("10")
	        || day.equals("20")
	        || isMonthEnd(chDate);
	  }

	  /**
	   * 是否季末
	   * @param chDate String
	   * @return boolean
	   */
	  public static boolean isSeasonEnd(String chDate)
	  {
	    String month = chDate.substring(4, 6);
	    return (month.equals("03")
	            || month.equals("06")
	            || month.equals("09")
	            || month.equals("12"))
	        && isMonthEnd(chDate);
	  }

	  /**
	   * 是否年末
	   * @param chDate String
	   * @return boolean
	   */
	  public static boolean isYearEnd(String chDate)
	  {
	    return chDate.substring(4, 8).equals("1231");
	  }

	  /**
	   * 是否半年末
	   * @param chDate String
	   * @return boolean
	   */
	  public static boolean isHalfYearEnd(String chDate)
	  {
	    return chDate.substring(4, 8).equals("0630");
	  }


	  /**
	   * 生成季日期：该方法根据传人的8位有效日期生成该日期的季日期
	   * @param sDate 8位有效日期
	   * @return String
	   * @throws Exception
	   */
	  public String makeQuarterDate(String sDate)
	      throws Exception
	  {
	    if (sDate.length() != 8)
	      throw new Exception("日期格式不对，不能生成季日期");

	    if (sDate.substring(4, 6).equals("01") || sDate.substring(4, 6).equals("02") || sDate.substring(4, 6).equals("03"))
	      return sDate.substring(0, 4) + "Q1";
	    else if (sDate.substring(4, 6).equals("04") || sDate.substring(4, 6).equals("05") || sDate.substring(4, 6).equals("06"))
	      return sDate.substring(0, 4) + "Q2";
	    else if (sDate.substring(4, 6).equals("07") || sDate.substring(4, 6).equals("08") || sDate.substring(4, 6).equals("09"))
	      return sDate.substring(0, 4) + "Q3";
	    else if (sDate.substring(4, 6).equals("10") || sDate.substring(4, 6).equals("11") || sDate.substring(4, 6).equals("12"))
	      return sDate.substring(0, 4) + "Q4";
	    else
	      throw new Exception("日期格式不对，不能生成季日期");

	  } //end of makeQuarterDate()

}
