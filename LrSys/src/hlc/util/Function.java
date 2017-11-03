package hlc.util;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Function
{

  /**
   * 构造函数，不需要，此类中所有方法均应该为静态方法。
   */
  public Function()
  {
  }

  /**
   * 将传入的字符串格式化：传入 a,b,c 格式化后 'a','b','c'
   * @param sSource String
   * @return String
   */
  public static String formatStringA(String sSource)
  {
    if (sSource == null)
      sSource = "''";
    else if (sSource.equals(""))
      sSource = "''";
    else
    {
      sSource = sSource.replaceAll(",", "','");
      sSource = "'" + sSource + "'";
    }

    return sSource;
  }

  /**
   * 判断参数是否为空，为空则返回一个长度为0的整形数组，否则返回其值
   * @param aSource 源字符串数组
   * @return 整型数组
   */
  public static int[] getIntArray(String[] aSource)
  {
    int iReturn[] = new int[0];
    if (aSource != null)
    {
      iReturn = new int[aSource.length];
      for (int i = 0; i < aSource.length; i++)
      {
        iReturn[i] = Integer.parseInt(aSource[i]);
      }
    }
    return iReturn;
  }

  /**
   * 将数组中的元素连成一个以逗号分隔的字符串
   * @param aSource 源数组
   * @return 字符串
   */
  public static String arrayToString(String[] aSource)
  {
    return arrayToString(aSource, ",");
  }

  /**
   * 将数组中的元素连成一个以给定字符分隔的字符串
   * @param aSource 源数组
   * @param sChar 分隔符
   * @return 字符串
   */
  public static String arrayToString(String[] aSource, String sChar)
  {
    String sReturn = "";
    for (int i = 0; i < aSource.length; i++)
    {
      if (i > 0)
        sReturn += sChar;
      sReturn += aSource[i];
    }
    return sReturn;
  }

  /**
   * 将两个字符串的所有元素连结为一个字符串数组
   * @param array1 源字符串数组1
   * @param array2 源字符串数组2
   * @return String[]
   */
  public static String[] arrayAppend(String[] array1, String[] array2)
  {
    return (String[]) (arrayAppend(array1, array2));
  }

  /**
   * 将两个对象数组中的所有元素连结为一个对象数组
   * @param array1 源字符串数组1
   * @param array2 源字符串数组2
   * @return Object[]
   */
  public static Object[] arrayAppend(Object[] array1, Object[] array2)
  {
    int iLen = 0;
    Object aReturn[];
    if (array1 == null)
      array1 = new Object[0];
    if (array2 == null)
      array2 = new Object[0];
      //获得第一个对象数组的元素个数
    iLen = array1.length;
    aReturn = new Object[iLen + array2.length];
    //将第一个对象数组的元素加到结果数组中
    for (int i = 0; i < iLen; i++)
      aReturn[i] = array1[i];
      //将第二个对象数组的元素加到结果数组中
    for (int i = 0; i < array2.length; i++)
      aReturn[iLen + i] = array2[i];
    return aReturn;
  }

  /**
   * 拆分以逗号分隔的字符串,并存入String数组中
   * @param sSource 源字符串
   * @return String[]
   */
  public static String[] stringToArray(String sSource)
  {
    return stringToArray(sSource, ",");
  }

  /**
   * 拆分以给定分隔符分隔的字符串,并存入字符串数组中
   * @param sSource 源字符串
   * @param sChar 分隔符
   * @return String[]
   */
  public static String[] stringToArray(String sSource, String sChar)
  {
    String aReturn[] = null;
    StringTokenizer st = null;
    st = new StringTokenizer(sSource, sChar);
    int i = 0;
    aReturn = new String[st.countTokens()];
    while (st.hasMoreTokens())
    {
      aReturn[i] = st.nextToken();
      i++;
    }
    return aReturn;
  }

  /**
   * 拆分以给定分隔符分隔的字符串,并存入整型数组中
   * @param sSource 源字符串
   * @param sChar 分隔符
   * @return int[]
   */
  public static int[] stringToArray(String sSource, char sChar)
  {
    return getIntArray(stringToArray(sSource, String.valueOf(sChar)));
  }

  /**
   * 将数组转换成字符串，转换后的字符串首尾不含分隔符，格式如下：a,b,c 。
   * @param a int[]
   * @param dot 分隔符，比如: ,
   * @param mark 引号，比如: '
   * @return 字符串
   */
  public static String arrayToString(int a[], String dot, String mark)
  {
    String strReturn = "";
    if (a.length == 0)
      strReturn = "";
    else if (a.length == 1)
      strReturn = mark + a[0] + mark;
    else
    {
      for (int i = 0; i < a.length - 1; i++)
        strReturn = strReturn + mark + String.valueOf(a[i]) + mark + dot;
      strReturn = strReturn + mark + String.valueOf(a[a.length - 1]) + mark;
    }
    return strReturn;
  }

  /**
   * 将数组转换成字符串，转换后的字符串首尾不含分隔符，格式如下：a,b,c 。
   * @param a String[]
   * @param dot 分隔符，比如: ,
   * @param mark 引号，比如: '
   * @return 字符串
   */
  public static String arrayToString(String a[], String dot, String mark)
  {
    String strReturn = "";
    if (a.length == 0)
      strReturn = "";
    else if (a.length == 1)
      strReturn = mark + a[0] + mark;
    else
    {
      for (int i = 0; i < a.length - 1; i++)
        strReturn = strReturn + mark + String.valueOf(a[i]) + mark + dot;
      strReturn = strReturn + mark + String.valueOf(a[a.length - 1]) + mark;
    }
    return strReturn;
  }

  /**
   * 删除磁盘上的文件
   * @param fileName 文件全路径
   * @return boolean
   */
  public static boolean deleteFile(String fileName)
  {
    File file = new File(fileName);
    return file.delete();
  }

  /**
   * 获取点分格式(123,456,789.88)的显示用数据
   * @param dlSrc 源数值
   * @param bitNum 小数位数
   * @return boolean
   * @throws Exception
   */
  public static String getDecimalAsString(double dlSrc, int bitNum)
  {
    String sSrc = String.valueOf(dlSrc);
    return getDecimalAsString(sSrc, bitNum);
  }

  /**
   * 获取点分格式(123,456,789.88)的显示用数据
   * @param sSrc 源数值
   * @param bitNum 小数位数
   * @return boolean
   */
  public static String getDecimalAsString(String sSrc, int bitNum)
  {
    String input = "";
    String restr = "";
    String head = "";
    int flag = 0;

    double dl = Double.parseDouble(sSrc);
    BigDecimal bd = new BigDecimal(dl);
    //对传入的数字四舍五入，小数点后位数
    bd = bd.setScale(bitNum, 5);
    input = String.valueOf(bd).trim();
    int i = input.indexOf('.');
    if (i == -1)
      i = input.length();

    if (input.substring(0, 1).equals("+") || input.substring(0, 1).equals("-"))
    {
      head = input.substring(0, 1);
      flag = 1;
    }

    String inputsub = input.substring(flag, i);
    if (inputsub.length() <= 3)
    {
      restr = input;
      return restr;
    }

    int j = inputsub.length();
    while (j >= 0)
    {
      if (j > 3)
      {
        restr = "," + inputsub.substring(j - 3, j) + restr;
      }
      else
      {
        restr = inputsub.substring(0, j) + restr;
      }
      j = j - 3;
    }

    return head + restr + input.substring(i, input.length());
  }

  /**
   * 判断参数是否为空，为空则返回0,不为空则返回其整型值
   * @param sSource 源字符串
   * @return 整型数
   */
  public static int getInt(String sSource)
  {
    int iReturn = 0;
    if (sSource != null && !sSource.equals(""))
      iReturn = Integer.parseInt(sSource);
    return iReturn;
  }

  /**
   * 判断参数是否为空，为空则返回""，否则返回其值
   * @param sSource 源字符串
   * @return 字符串
   */
  public static String getString(String sSource)
  {
    String sReturn = "";
    if (sSource != null) sReturn = sSource;
    return sReturn;
  }

  /**
   * 将给定的源字符串加1
   * 例如：“0001”　经本函数转换后返回为“0002”
   * @param sSource :源字符串
   * @return 返回字符串
   */
  public static String increaseOne(String sSource)
  {
    String sReturn = null;
    int iSize = 0;

    iSize = sSource.length();

    long l = (new Long(sSource)).longValue();
    l++;
    sReturn = String.valueOf(l);

    for (int i = sReturn.length(); i < iSize; i++)
    {
      sReturn = "0" + sReturn;
    }

    return sReturn;
  }

  /**
   * 将给定的整数转化成字符串，
   * 结果字符串的长度为给定长度，不足位数的左端补"0"
   * （此方法为林伟伟于 2002-10-30 所加）
   * @param val val
   * @param len len
   * @return String
   */
  public static String intToStr(int val, int len)
  {
    String sReturn = new String();

    sReturn = String.valueOf(val);

    if (sReturn.length() < len)
    {
      for (int i = len - sReturn.length(); i > 0; i--)
      {
        sReturn = "0" + sReturn;
      }
    }

    return sReturn;
  }

  /**
   * 将String转换成HTML文本<br>
   * 规则：<br>
   * 1、将其中的\r\n转换为网页中换行<br>
   * 2、将其中某段超过rowLen的文字拆成以每行rowLen字的多行;<br>
   * 3、将其中的空格替换成网页中的空格;<br>
   * @param str String 要转换的String
   * @param rowLen int 每行字数（英文字数[汉字*2]）
   * @return String 转换后的String
   */
  public static String stringToHTML(String str, int rowLen)
  {
    StringBuffer newStr = new StringBuffer();
    String aTmp[] = str.split("\r\n");
    for (int i = 0; i < aTmp.length; i++)
    {
      byte aByte[] = aTmp[i].getBytes();

      if (aByte.length <= rowLen)
      {
        newStr.append(aTmp[i] + "<br>");
        continue;
      }

      int sbLen = 0;
      StringBuffer sbTmp = new StringBuffer();
      for (int j = 0; j < aTmp[i].length(); j++)
      {
        String s1 = aTmp[i].substring(j, j + 1);
        int nowLen = s1.getBytes().length;
        sbLen = sbLen + nowLen;

        if (sbLen > rowLen)
        {
          sbTmp.append("<br>" + s1);
          sbLen = nowLen;
        }
        else if (sbLen == rowLen)
        {
          sbTmp.append(s1 + "<br>");
          sbLen = 0;
        }
        else
          sbTmp.append(s1);
      }
      String lastStr = sbTmp.substring(sbTmp.length() - 4);
      if (lastStr.equals("<br>"))
        newStr.append(sbTmp.toString());
      else
        newStr.append(sbTmp.toString() + "<br>");
    }

    return newStr.toString().replaceAll(" ", "&nbsp;");
  }

  /**
   * 将以逗号分隔的字符串里相同的内容去掉。此方法不记忆原字符串顺序。
   * @param str String
   * @param mark String 以此符号分隔
   * @return String
   * @author shantao
   */
  public static String noRepeat(String str, String mark)
  {
    StringTokenizer st = new StringTokenizer(str, mark);
    List lt = new ArrayList();
    StringBuffer sb = new StringBuffer();
    String tmp = null;
    while (st.hasMoreTokens())
    {
      tmp = st.nextToken();
      if (!lt.contains(tmp))
        lt.add(tmp);
    }
    if (!lt.isEmpty())
      sb.append(lt.get(0));
    for (int i = 1; i < lt.size(); i++)
      sb.append(mark + lt.get(i));
    return sb.toString();
  }

  public static String getFormatDate(String chDate, String dot)
  {
    if (chDate.length() == 8)
      return chDate.substring(0, 4) + dot + chDate.substring(4, 6) + dot + chDate.substring(6, 8);
    else
      return chDate;
  }

  public static String getFormatTime(String chTime, String dot)
  {
    if (chTime.length() == 6)
      return chTime.substring(0, 2) + dot + chTime.substring(2, 4) + dot + chTime.substring(4, 6);
    else
      return chTime;
  }

  public static void main(String[] args)
  {
    String[] a = new String[0];
//    a[0] = "a";
//    a[1] = "b";
//    a[2] = "c";
    String ss = Function.arrayToString(a, ",", "'");
    //System.out.println("[" + ss + "]");
  }
}
