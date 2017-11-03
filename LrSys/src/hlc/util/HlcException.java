package hlc.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class HlcException extends Exception
{

    private final String ERRCODE_TEXT = "9901";
    public static final int LANGUAGE_CHINESE = 1;
    public static final int LANGUAGE_ENGLISH = 0;
    private String errCode;
    private String index;
    private String invoker;
    private String jspFileName;
    private static HashMap map = null;
    private static HashMap busMap = null;
    private String message;
    private static int messageLanguage = 1;
    private String para[];

    public HlcException(String index)
        throws Exception
    {
        this.index = "01";
        para = new String[0];
        invoker = "java.lang.Object";
        message = "message undefined";
        errCode = "9901";
        jspFileName = "";
        this.index = index;
    }

    public HlcException(String index, String para[])
        throws Exception
    {
        this.index = "01";
        this.para = new String[0];
        invoker = "java.lang.Object";
        message = "message undefined";
        errCode = "9901";
        jspFileName = "";
        this.index = index;
        this.para = para;
    }

    public HlcException(String index, String para)
        throws Exception
    {
        this.index = "01";
        this.para = new String[0];
        invoker = "java.lang.Object";
        message = "message undefined";
        errCode = "9901";
        jspFileName = "";
        this.index = index;
        String aPara[] = {
            para
        };
        this.para = aPara;
    }

    public static void clearSetting()
    {
        if(map != null)
            map.clear();
    }

    public String getErrCode()
    {
        return errCode;
    }

    public String getMessage()
    {
    	try{
        initMessage();
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
        return "[" + errCode + "]  " + message;
    }

    public static int getMessageLanguage()
    {
        return messageLanguage;
    }

    private void initClass()
        throws Exception
    {
        InputStream is;
        InputStream busIs;
        map = new HashMap();
        busMap = new HashMap();
        is = getClass().getResourceAsStream("HlcException.xml");
        busIs = getClass().getResourceAsStream("/Exception.xml");
        
        try
        {
            DOMParser dp = new DOMParser();
            dp.parse(new InputSource(is));
            Document doc = dp.getDocument();
            NodeList nl = doc.getElementsByTagName("class");
            for(int i = 0; i < nl.getLength(); i++)
            {
                Element element = (Element)nl.item(i);
                String className = element.getAttribute("name");
                String code = element.getAttribute("code");
                NodeList nlMessage = element.getElementsByTagName("message");
                for(int j = 0; j < nlMessage.getLength(); j++)
                {
                    Element eleMessage = (Element)nlMessage.item(j);
                    String key = eleMessage.getAttribute("key");
                    String enValue = eleMessage.getAttribute("enValue");
                    String cnValue = eleMessage.getAttribute("cnValue");
                    String aEn[] = {
                        code + key, enValue
                    };
                    String aCn[] = {
                        code + key, cnValue
                    };
                    map.put(className + "." + key + "en", aEn);
                    map.put(className + "." + key + "cn", aCn);
                }

            }
            dp = null;
            doc = null;
            nl = null;
            if(busIs!=null){
            	 dp = new DOMParser();
                 dp.parse(new InputSource(busIs));
                 doc = dp.getDocument();
                 nl = doc.getElementsByTagName("class");
                 for(int i = 0; i < nl.getLength(); i++)
                 {
                     Element element = (Element)nl.item(i);
                     String className = element.getAttribute("name");
                     String code = element.getAttribute("code");
                     NodeList nlMessage = element.getElementsByTagName("message");
                     for(int j = 0; j < nlMessage.getLength(); j++)
                     {
                         Element eleMessage = (Element)nlMessage.item(j);
                         String key = eleMessage.getAttribute("key");
                         String enValue = eleMessage.getAttribute("enValue");
                         String cnValue = eleMessage.getAttribute("cnValue");
                         String aEn[] = {
                             code + key, enValue
                         };
                         String aCn[] = {
                             code + key, cnValue
                         };
                         busMap.put(className + "." + key + "en", aEn);
                         busMap.put(className + "." + key + "cn", aCn);
                     }

                 }
            }
           
        }catch(Exception ex){
        	throw ex;
        }
        finally
        {
            is.close();
        }
        return;
    }

    public void initMessage()
        throws UnsupportedOperationException,Exception
    {
        String newMsg;
        StringTokenizer st;
        String lastMsg;
        String tmpStr;
        int paraIndex;
        try
        {
            if(map == null)
                initClass();
            if(!invoker.equals("java.lang.Object"))
                return;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new UnsupportedOperationException(ex.getMessage());
        }
        StackTraceElement ste[] = getStackTrace();
        if(ste.length > 0)
            invoker = ste[0].getClassName();
        if(invoker.indexOf("hlc.") != 0)
            invoker = jspFileName;
        readMessage();
        newMsg = "";
        st = new StringTokenizer(message, "%", true);
        lastMsg = "";
        while (st.hasMoreTokens())
        {
          tmpStr = st.nextToken();
          if (!tmpStr.equals("%") && !lastMsg.equals("%"))
            newMsg += tmpStr;
          if (lastMsg.equals("%"))
          {
            paraIndex = 1;
            try
            {
              paraIndex = Integer.parseInt(tmpStr.substring(0, 1));
            }
            catch (Exception ex)
            {
              throw new Exception("message text error, can't get parmeter index.");
            }
            newMsg += this.para[paraIndex - 1] + tmpStr.substring(1);
          }
          lastMsg = tmpStr;
        }
    
        message = newMsg;
        return;
    }

    private void readMessage()
    {
    	String tmp[];
    	try{
           if(getMessageLanguage() == 0){
               tmp = (String[])map.get(invoker + "." + index + "en");
               if(tmp==null||tmp.length==0){
               	tmp = (String[])busMap.get(invoker + "." + index + "en");
               }
           }
           else{
        	   tmp = (String[])map.get(invoker + "." + index + "cn");
        	   
           	 if(tmp==null||tmp.length==0){
                	tmp = (String[])busMap.get(invoker + "." + index + "cn");
                }
           }
               
           errCode = tmp[0];
           message = tmp[1];
       }catch(Exception ex){
    	   if(errCode.equals("9901")){
    		   if(messageLanguage==0){
    			   message = "can't get the message code of invoker '" + invoker + "'.";
    		   }else{
    			   message =  "读取错误消息时: 无法取到类" + this.invoker + "对应的消息代号!";
    		   }
    	   }else{
    		   if(messageLanguage==0){
    			   message = "can't get the message of message code '" + errCode + "'.";
    		   }else{
    			   message = "读取错误消息时: 无法取到消息代号(" + this.errCode + ")对应的消息!";
    		   }
    	   }
              
              
       }
        return;
    }

    public void setJspFileName(String fileName)
    {
        jspFileName = fileName;
    }

    public static void setMessageLanguage(int MessageLanguage)
        throws Exception
    {
        if(MessageLanguage != 0 && MessageLanguage != 1)
        {
            throw new Exception("language set error, allowed {0,1}.");
        } else
        {
            messageLanguage = MessageLanguage;
            return;
        }
    }

    public String toString()
    {
    	try{
        initMessage();
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return "ErrCode=[" + errCode + "]\r\nErrMessage=[" + message + "]\r\n";
    }

}
