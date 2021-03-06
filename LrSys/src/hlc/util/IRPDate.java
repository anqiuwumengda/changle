package hlc.util;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

//import hlc.pub.util.DbAccess;
//import hlc.pub.util.ResultData;
//import hlc.pub.util.Exception;

public class IRPDate
    implements Serializable
{

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

  //数据库连接
//   private DbAccess db = null ;
//   private ResultData rd = null ;


  /**
   * 构造器
   */
  public IRPDate()
  {
  }

  public static void main(String args[])
  {

    //Date now = new Date();
    //String a[] = null;
    IRPDate ad = new IRPDate();

    try
    {
    	System.out.println(ad.getPreQuarterEndDate("20071231"));

    }
    catch (Exception e)
    {
      e.printStackTrace();
      //System.out.println(e);
    }
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
   * 获得给定日期的前一个会计工作日
   * @param sDate 日期
   * @return String
   * @throws Exception
   */
  public String getKJPrevDate(String sDate)
      throws Exception
  {
    throw new java.lang.UnsupportedOperationException(
        "Method getKJPrevDate() not yet implemented.");
//    try
//    {
//      checkDB();
//
//      //判断此日期是否为合法日期
//      s = "SELECT MAX(chdt) FROM " + getHolidayAlias()
//        + " WHERE "//load_date=(SELECT MAX(load_date) FROM " + getHolidayAlias() + ")"
//        //+ " AND
//        + "chdt < '" + sDate + "' AND inFlag = 0 " ;
//      //System.out.println(s);
//      rd = db.executeQuery(s) ;
//      if (rd.size() != 1)
//        throw new Exception("此日期为非法日期");
//      else
//        return rd.getString(0,0) ;
//    }
//    catch(Exception ae)
//    {
//      throw new Exception("IRPDate.java查询数据库出错：" + ae.getMsg()) ;
//    }

  }

  /**
   * 获得给定日期的下一个会计工作日
   * @param sDate 日期
   * @return String
   * @throws Exception
   */
  public String getKJNextDate(String sDate)
      throws Exception
  {
    throw new java.lang.UnsupportedOperationException(
        "Method getKJNextDate() not yet implemented.");
//    try
//    {
//      checkDB();
//
//      //判断此日期是否为合法日期
//      s = "SELECT MIN(chdt) FROM " + getHolidayAlias()
//        + " WHERE " //load_date=(SELECT MAX(load_date) FROM " + getHolidayAlias() + ")"
//        //+ " AND
//        + "chdt > '" + sDate + "' AND inFlag = 0 " ;
//      //System.out.println(s);
//      rd = db.executeQuery(s) ;
//      if (rd.size() != 1)
//        throw new Exception("此日期为非法日期");
//      else
//        return rd.getString(0,0) ;
//    }
//    catch(Exception ae)
//    {
//      throw new Exception("IRPDate.java查询数据库出错：" + ae.getMsg()) ;
//    }

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
   * 取得两个日期之间的所有日期（包括本身）---会计日期
   *
   * @param sBeginDate 开始日期
   * @param sEndDate   结束日期
   * @return           两个日期之间的所有日期（包括本身）
   * @throws Exception
   */
  public String[] getBetweenDates(String sBeginDate, String sEndDate)
      throws Exception
  {
    throw new java.lang.UnsupportedOperationException(
        "Method getBetweenDate() not yet implemented.");
//    String aReturn[] = null ;
//
//    if(sEndDate.compareTo(sBeginDate) < 0)
//      throw new Exception("结束日期不能小于开始日期。");
//
//    try
//    {
//      checkDB() ;
//
//      s = "SELECT chdt FROM " + getHolidayAlias()
//        + " WHERE " //load_date=(SELECT MAX(load_date) FROM " + getHolidayAlias() + ")"
//        //+ " AND
//        + "chdt BETWEEN '" + sBeginDate + "' and '" + sEndDate + "'"
//        + " AND inFlag = 0 " ;
//      //System.out.println(s);
//      rd = db.executeQuery(s) ;
//      if (rd.size()==0)
//        throw new Exception("假日表中没有符合条件的日期") ;
//      else
//      {
//        aReturn = new String[rd.size()] ;
//        for (int i=0;i<rd.size();i++)
//          aReturn[i] = rd.getString(i,0) ;
//      }
//      return aReturn ;
//    }
//    catch(Exception ae)
//    {
//      throw new Exception("IRPDate.java查询数据库出错：" + ae.getMsg()) ;
//    }
  }

  /**
   * 返回当前时间　HHmmss 24小时制
   * @return 当前时间　HHmmss
   * @throws Exception
   */
  public String getTimes()
		  throws Exception
		  {
	  SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
	  s = sdf.format(Calendar.getInstance().getTime());
	  return s;
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

//    return sDate.substring(4).equals("0101") ;
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

//    s = sDate.substring(4) ;
//    return s.equals("0331") || s.equals("0630") || s.equals("0930") || s.equals("1231");
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
//    if (sDate.substring(4, 6).equals("01"))
//      s = getPreYearEndDate(sDate);
//    else
//    {
//      s = String.valueOf(Integer.parseInt(sDate.substring(4, 6)) - 1 + 100).substring(1);
//      s = getMonthEndDate(sDate.substring(0, 4) + s + "01");
//    }
//    return s;
    return getNextDate(sDate.substring(0,6) + "01" ,-1) ;
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
    s = sDate.substring(4, 6);

    if (s.equals("01") || s.equals("02") || s.equals("03"))
    {
      //返回上年年底日
      return getPreYearEndDate(sDate);
    }
    else if (s.equals("04") || s.equals("05") || s.equals("06"))
    {
      return sDate.substring(0, 4) + "0301";
//      return sDate.substring(0,4) + "0331" ;
    }
    else if (s.equals("07") || s.equals("08") || s.equals("09"))
    {
      return sDate.substring(0, 4) + "0601";
//      return sDate.substring(0,4) + "0630" ;
    }
    else if (s.equals("10") || s.equals("11") || s.equals("12"))
    {
      return sDate.substring(0, 4 )+ "0901";
//      return sDate.substring(0,4) + "0930" ;
    }
    else
    {
      throw new Exception("不存在的月份：" + s);
    }

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
    s = sDate.substring(0, 4) + "0101";
    s = getNextDate(s, -1);
//    return getMonthEndDate(s);
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

//    return sDate.substring(0,4) + "0101" ;
  }

  /**
   * 获得本月月末日--会计日期
   * @param sDate 日期
   * @return String
   * @throws Exception
   */
  public String getMonthEndDate(String sDate)
      throws Exception
  {
    throw new java.lang.UnsupportedOperationException(
        "Method getMonthEndDate() not yet implemented.");
//    try
//    {
//      checkDB() ;
//
//      s = "SELECT MAX(chdt) FROM " + getHolidayAlias()
//        + " WHERE "//load_date=(SELECT MAX(load_date) FROM " + getHolidayAlias() + ")"
//        //+ " AND
//        + "SUBSTR(chdt,1,6) = '" + sDate.substring(0,6) + "' AND inFlag = 0 " ;
//      //System.out.println(s);
//      rd = db.executeQuery(s) ;
//      if (rd.size()==0)
//        throw new Exception("假日表中没有符合条件的日期") ;
//      else
//      {
//        return rd.getString(0,0) ;
//      }
//    }
//    catch(Exception ae)
//    {
//      throw new Exception("IRPDate.java查询数据库出错：" + ae.getMsg()) ;
//    }

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
//	  throw new java.lang.UnsupportedOperationException(
//        "Method getMonthEarlierDate() not yet implemented.");
//    try
//    {
//      checkDB() ;
//
//      s = "SELECT MIN(chdt) FROM " + getHolidayAlias()
//        + " WHERE " //load_date=(SELECT MAX(load_date) FROM " + getHolidayAlias() + ")"
//        //+ " AND
//        + "SUBSTR(chdt,1,6) = '" + sDate.substring(0,6) + "' AND inFlag = 0 " ;
//      //System.out.println(s);
//      rd = db.executeQuery(s) ;
//      if (rd.size()==0)
//        throw new Exception("假日表中没有符合条件的日期") ;
//      else
//      {
//        return rd.getString(0,0) ;
//      }
//    }
//    catch(Exception ae)
//    {
//      throw new Exception("IRPDate.java查询数据库出错：" + ae.getMsg()) ;
//    }
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
   * 检查数据库连接，若不存在连接则新建一个
   *
   * 从连接ODS改为连接联合体tamis_fd
   * alter by ck 2003-3-17 20:16
   *
   * @throws Exception
   */
  private void checkDB()
      throws Exception
  {
//      if (db == null)
//         db = new DbAccess() ;
    //db = new DbAccess("tamis_fd",true,"IRPDate.java") ;

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
   * 判断一个日期是否为假日
   * @param sDate 日期
   * @return boolean
   * @throws Exception
   */
  public boolean isHoliday(String sDate)
      throws Exception
  {
    throw new java.lang.UnsupportedOperationException(
        "Method isHoliday() not yet implemented.");

//    try
//    {
//      boolean bReturn = true ;
//
//      checkDB() ;
//
//      //判断此日期是否为合法日期
//      s = "SELECT inFlag FROM " + getHolidayAlias()
//        + " WHERE " //load_date=(SELECT MAX(load_date) FROM " + getHolidayAlias() + ")"
//        //+ " AND
//        + "chdt = '" + sDate + "'" ;
//      //System.out.println(s);
//      rd = db.executeQuery(s) ;
//      if (rd.size() != 1)
//        throw new Exception("此日期为非法日期");
//      else
//      {
//        if (rd.getString(0,0).equals("1"))
//          bReturn = true ;
//        else
//          bReturn = false ;
//      }
//      return bReturn ;
//    }
//    catch(Exception ae)
//    {
//      throw new Exception("IRPDate.java查询数据库出错：" + ae.getMsg()) ;
//    }

  }

  /**
   * 找出给定日期区间内的所有假日，并存入Hashtable中
   * add by ck 2002-12-10 11:00
   * @param sBeginDate 开始日期
   * @param sEndDate   结束日期
   * @return Hashtable
   * @throws Exception
   */
  public Hashtable getHolidays(String sBeginDate, String sEndDate)
      throws Exception
  {
    try
    {
      if (sEndDate.compareTo(sBeginDate) < 0)
        throw new Exception("结束日期不能小于开始日期。");

      Hashtable ht = new Hashtable();

      checkDB();

      //判断此日期是否为合法日期
      s = "SELECT chdt FROM " + getHolidayAlias()
          + " WHERE " //load_date=(SELECT MAX(load_date) FROM " + getHolidayAlias() + ")"
          //+ " AND
          + "chdt between '" + sBeginDate + "' AND '" + sEndDate + "'"
          + " AND inFlag = 1 ";
//      //System.out.println(s);
//      rd = db.executeQuery(s) ;
//      for (int i=0;i<rd.size();i++)
//      {
//        ht.put(rd.getString(i,0),"1") ;
//      }
      return ht;
    }
    catch (Exception e)
    {
      throw new Exception("IRPDate.java出错：" + e.getMessage());
    }
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
   * 获得本日期所在的季度的季末日期
   * @param sDate 日期
   * @return String
   * @throws Exception
   */
  public String getQuarterEndDate(String sDate)
      throws Exception
  {
    checkDate(sDate);
    s = sDate.substring(4, 6);

    if (s.equals("01") || s.equals("02") || s.equals("03"))
    {
      s = sDate.substring(0, 4) + "0301";
    }
    else if (s.equals("04") || s.equals("05") || s.equals("06"))
    {
      s = sDate.substring(0, 4) + "0601";
    }
    else if (s.equals("07") || s.equals("08") || s.equals("09"))
    {
      s = sDate.substring(0, 4) + "0901";
    }
    else if (s.equals("10") || s.equals("11") || s.equals("12"))
    {
      s = sDate.substring(0, 4) + "1201";
    }
    else
    {
      throw new Exception("不存在的月份：" + s);
    }
    //返回季末日期
    return getMonthEndDate(s);

  }

  /**
   * 获得等于或早于给定日期的合法的就近会计工作日
   * 若给定日期为会计工作日，则直接返回此日期，
   * 若不是，则找到早于给定日期的就近的会计工作日
   * @param sDate 日期
   * @return String
   * @throws Exception
   */
  public String getKJDateEarly(String sDate)
      throws Exception
  {
//    checkDate(sDate) ;

    if (!isHoliday(sDate))
      return sDate;
    else
      return getKJPrevDate(sDate);
  }

  /**
   * 获得等于或晚于给定日期的合法的就近会计工作日
   * 若给定日期为会计工作日，则直接返回此日期，
   * 若不是，则找到晚于给定日期的就近的会计工作日
   * @param sDate 日期
   * @return String
   * @throws Exception
   */
  public String getKJDateLate(String sDate)
      throws Exception
  {
    if (!isHoliday(sDate))
      return sDate;
    else
      return getKJNextDate(sDate);
  }

  /**
   * 获取最新的 ODS 分库名称
   * add by ck 2003-3-17 19:46
   *
   * @return 最新的 ODS 分库名称
   * @throws Exception
   */
  private String getLastODSName()
      throws Exception
  {
//    try
//    {
//      //获取所有的ods库
//      hlc.sa.para.EnSysPara cEnSysPara;
//      String   sOds = "" ;
//      String   sReturn = "" ;
//      String[] aOds ;
//      String[]  aAllODSNames = new String[0] ;
//      String[]  aAllODSDates = new String[0] ;
//      Function f = new Function() ;
//
//      //建立一个临时数据库连接，取出系统参数的值
//      DbAccess db2 = new DbAccess(true,"IRPDate.java") ;
//      cEnSysPara = new hlc.sa.para.EnSysPara(db2);
//      sOds = cEnSysPara.getValueByCode("ods_db");
//
    //恢复数据库连接
//      db = new DbAccess("tamis_fd",true,"IRPDate.java") ;
//
//      String[] aTmp ;
//      s = "" ;
//      aOds = f.strToArray(sOds,";") ;
//      aAllODSNames = new String[aOds.length];
//      aAllODSDates = new String[aOds.length];
//      for(int i=0;i<aOds.length;i++)
//      {
//        aTmp = f.strToArray(aOds[i],":");
//        aAllODSNames[i] = aTmp[0];
//        aAllODSDates[i] = aTmp[1];
//        if (aTmp[1].compareTo(s) > 0)
//        {
//          s = aTmp[1] ;
//          sReturn = aTmp[0] ;
//        }
//
//      }
//
//      return sReturn ;
//    }
//    catch(Exception ae)
//    {
//      throw new Exception("IRPDate.java取系统参数时出错: " + ae.getMsgCode() + "-" + ae.getMsg()) ;
//    }
//    catch(Exception e)
//    {
//      throw new Exception("IRPDate.java取系统参数时出错： " + e ) ;
//    }
    throw new java.lang.UnsupportedOperationException(
        "Method getLastODSName() not yet implemented.");

  }

  /**
   * 根据ODS分库名称，按照规则生成表在联合体中的别名，并返回
   *
   * add by ck 2003-3-17 20:16
   *
   * @return 表在联合体中的别名
   * @throws Exception
   */
  private String getHolidayAlias()
      throws Exception
  {
    String sReturn = "";

    //sReturn = "DB2INST1." + getLastODSName() + "_OYW_CIM_HOLIDAY" ;
    sReturn = "HOLIDAY";

    return sReturn;

  }

  /***************************** add by wsq 2002.11.12 ******************************/

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

  /**********************************add by wsq end ******************************/


  /**
   * 测试数据(2002年ods库)
   */
  /*
   20020104	20020201	20020301	20020401	20020508	20020603	20020701	20020801	20020902	20021008	20021101	20021202
   20020107	20020204	20020304	20020402	20020509	20020604	20020702	20020802	20020903	20021009	20021104	20021203
   20020108	20020205	20020305	20020403	20020510	20020605	20020703	20020805	20020904	20021010	20021105	20021204
   20020109	20020206	20020306	20020404	20020513	20020606	20020704	20020806	20020905	20021011	20021106	20021205
   20020110	20020207	20020307	20020405	20020514	20020607	20020705	20020807	20020906	20021014	20021107	20021206
   20020111	20020208	20020308	20020408	20020515	20020610	20020708	20020808	20020909	20021015	20021108	20021209
   20020114	20020209	20020311	20020409	20020516	20020611	20020709	20020809	20020910	20021016	20021111	20021210
   20020115	20020210	20020312	20020410	20020517	20020612	20020710	20020812	20020911	20021017	20021112	20021211
   20020116	20020211	20020313	20020411	20020520	20020613	20020711	20020813	20020912	20021018	20021113	20021212
   20020117	20020219	20020314	20020412	20020521	20020614	20020712	20020814	20020913	20021021	20021114	20021213
   20020118	20020220	20020315	20020415	20020522	20020617	20020715	20020815	20020916	20021022	20021115	20021216
   20020121	20020221	20020318	20020416	20020523	20020618	20020716	20020816	20020917	20021023	20021118	20021217
   20020122	20020222	20020319	20020417	20020524	20020619	20020717	20020819	20020918	20021024	20021119	20021218
   20020123	20020225	20020320	20020418	20020527	20020620	20020718	20020820	20020919	20021025	20021120	20021219
   20020124	20020226	20020321	20020419	20020528	20020621	20020719	20020821	20020920	20021028	20021121	20021220
   20020125	20020227	20020322	20020422	20020529	20020624	20020722	20020822	20020923	20021029	20021122	20021223
   20020128	20020228	20020325	20020423	20020530	20020625	20020723	20020823	20020924	20021030	20021125	20021224
   20020129		        20020326	20020424	20020531	20020626	20020724	20020826	20020925	20021031	20021126	20021225
   20020130		        20020327	20020425		        20020627	20020725	20020827	20020926		        20021127	20021226
   20020131		        20020328	20020426		        20020628	20020726	20020828	20020927		        20021128	20021227
                      20020329	20020427			                20020729	20020829	20020928		        20021129	20021230
                                20020428                      20020730	20020830	20020929			                20021231
                                20020429                      20020731		        20020930
                                20020430

   */
  // 得到 至给定日期止 的 当月天数
  public int getDaysOfMonth(String date){
	  String days = date.substring(6, 8);
	  int d = Integer.parseInt(days);
	  return d;
  }
  // 得到 至给定日期止 的 当季天数
  public int getDaysOfQuarter(String date){
	  String month = date.substring(4, 6);
	  int d = Integer.parseInt(date.substring(6, 8));
	  if("01".equals(month)){
		  return d;
	  }else if("02".equals(month)){
		  return 31+d;
	  }else if("03".equals(month)){
		  if(isRunYear(date)){
			  return 59+d;
		  }else{
			  return 60+d;
		  }
	  }else if("04".equals(month)){
		  return d;
	  }else if("05".equals(month)){
		  return 30+d;
	  }else if("06".equals(month)){
		  return 61+d;
	  }else if("07".equals(month)){
		  return d;
	  }else if("08".equals(month)){
		  return 31+d;
	  }else if("09".equals(month)){
		  return 62+d;
	  }else if("10".equals(month)){
		  return d;
	  }else if("11".equals(month)){
		  return 31+d;
	  }else{
		  return 61+d;
	  }
  }
  public int getDaysOfYear(String date) throws Exception{
	  String start = date.substring(0, 4)+"0101";
	  return daysBetween(start,date)+1;
  }
  public boolean isRunYear(String date){
	  String year1 = date.substring(1, 4);
	  int year = Integer.parseInt(year1);
	  if((year%4==0&&year%100!=0)||(year%400==0)){
		  return true;
	  }
	  return false;
  }
}
