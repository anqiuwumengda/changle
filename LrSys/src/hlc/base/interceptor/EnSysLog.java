package hlc.base.interceptor;

public class EnSysLog
{
  /**操作机构*/
  private String chOpOrg = "";

  /**操作机构名称*/
  private String chOpOrgAlias = "";

  /**项目代码*/
  private String chProjCode = "";

  /**项目名称*/
  private String chProjCodeAlias = "";

  /**操作人员*/
  private String chOpOperator = "";

  /**操作人员名称*/
  private String chOpOperatorAlias = "";

  /**操作名称*/
  private String chOpName = "";

  /**操作对象类型*/
  private String chOpObjType = "";

  /**操作对象*/
  private String chOpObj = "";

  /**操作对象ID*/
  private String chOpObjID = "";

  /**操作日期*/
  private String chOpDate = "";

  /**操作时间*/
  private String chOpTime = "";

  /**操作返回码*/
  private String chOpResult = "";
  
  
  private String Memo ="";

  /**
   * 构造方法
   */
  public EnSysLog()
  {
  }

  public String getChOpOperator()
  {
    return chOpOperator;
  }

  public void setChOpOperator(String chOpOperator)
  {
    this.chOpOperator = chOpOperator;
  }

  public void setChOpDate(String chOpDate)
  {
    this.chOpDate = chOpDate;
  }

  public String getChOpDate()
  {
    return chOpDate;
  }

  public String getChOpOrg()
  {
    return chOpOrg;
  }

  public void setChOpOrg(String chOpOrg)
  {
    this.chOpOrg = chOpOrg;
  }

  public void setChOpName(String chOpName)
  {
    this.chOpName = chOpName;
  }

  public String getChOpName()
  {
    return chOpName;
  }

  public String getChOpObj()
  {
    return chOpObj;
  }

  public void setChOpObj(String chOpObj)
  {
    this.chOpObj = chOpObj;
  }

  public void setChOpObjID(String chOpObjID)
  {
    this.chOpObjID = chOpObjID;
  }

  public String getChOpObjID()
  {
    return chOpObjID;
  }

  public String getChOpObjType()
  {
    return chOpObjType;
  }

  public void setChOpObjType(String chOpObjType)
  {
    this.chOpObjType = chOpObjType;
  }

  public void setChOpResult(String chOpResult)
  {
    this.chOpResult = chOpResult;
  }

  public String getChOpResult()
  {
    return chOpResult;
  }

  public String getChOpTime()
  {
    return chOpTime;
  }

  public void setChOpTime(String chOpTime)
  {
    this.chOpTime = chOpTime;
  }

  public void setChProjCode(String chProjCode)
  {
    this.chProjCode = chProjCode;
  }

  public String getChProjCode()
  {
    return chProjCode;
  }

  public String getChOpOperatorAlias()
  {
    return chOpOperatorAlias;
  }

  public void setChOpOperatorAlias(String chOpOperatorAlias)
  {
    this.chOpOperatorAlias = chOpOperatorAlias;
  }

  public void setChOpOrgAlias(String chOpOrgAlias)
  {
    this.chOpOrgAlias = chOpOrgAlias;
  }

  public String getChOpOrgAlias()
  {
    return chOpOrgAlias;
  }

  public String getChProjCodeAlias()
  {
    return chProjCodeAlias;
  }

  public void setChProjCodeAlias(String chProjCodeAlias)
  {
    this.chProjCodeAlias = chProjCodeAlias;
  }
  
  public String getMemo()
  {
	return Memo;
  }
  
  public void setMemo(String Memo)
  {
	this.Memo = Memo;
  }
}
