
package hlc.base;

import java.io.Serializable;
import java.util.HashMap;

public class EnField
	implements Cloneable, Serializable
{

	public static final String TYPE_STRING = "String";
	public static final String TYPE_INT = "int";
	public static final String TYPE_DOUBLE = "double";
	public static final String TYPE_BOOLEAN = "boolean";
	public static final String TYPE_BYTE_ARRAY = "byte[]";
	public static final String TYPE_EN = "EnBase";
	public static final String BOUND_ALL = "all";
	public static final String BOUND_GE_0 = "ge0";
	public static final String BOUND_GT_0 = "gt0";
	public static final String BOUND_LE_0 = "le0";
	public static final String BOUND_LT_0 = "lt0";
	private String fieldName;
	private String enName;
	private String cnName;
	private String dataType;
	private String extendType;
	private int minLength;
	private int maxLength;
	private boolean pk;
	private boolean needSave;
	private boolean autoCheck;
	private boolean autoEvaluate;
	private String bound;
	private String ge;
	private String le;
	private HashMap aliasMap;
	private HashMap aliasIndexKeyMap;
	private boolean fromParent;

	public EnField()
	{
		fieldName = "";
		enName = "";
		cnName = "";
		dataType = "";
		extendType = "";
		minLength = 0;
		maxLength = 0x19000;
		pk = false;
		needSave = true;
		autoCheck = true;
		autoEvaluate = true;
		bound = "all";
		ge = "";
		le = "";
		aliasMap = null;
		aliasIndexKeyMap = null;
		fromParent = false;
	}

	public String getCnName()
	{
		return cnName;
	}

	public void setCnName(String cnName)
	{
		this.cnName = cnName;
	}

	public String getDataType()
	{
		return dataType;
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}

	public String getEnName()
	{
		return enName;
	}

	public void setEnName(String enName)
	{
		this.enName = enName;
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public int getMaxLength()
	{
		return maxLength;
	}

	public void setMaxLength(int maxLength)
	{
		this.maxLength = maxLength;
	}

	public int getMinLength()
	{
		return minLength;
	}

	public void setMinLength(int minLength)
	{
		this.minLength = minLength;
	}

	public boolean isPk()
	{
		return pk;
	}

	public void setPk(boolean pk)
	{
		this.pk = pk;
	}

	public boolean isAutoCheck()
	{
		return autoCheck;
	}

	public void setAutoCheck(boolean autoCheck)
	{
		this.autoCheck = autoCheck;
	}

	public boolean isNeedSave()
	{
		return needSave;
	}

	public void setNeedSave(boolean needSave)
	{
		this.needSave = needSave;
	}

	public String getExtendType()
	{
		return extendType;
	}

	public void setExtendType(String extendType)
	{
		this.extendType = extendType;
	}

	public String getBound()
	{
		return bound;
	}

	public void setBound(String bound)
	{
		this.bound = bound;
	}

	public String getGE()
	{
		return ge;
	}

	public void setGE(String ge)
	{
		this.ge = ge;
	}

	public String getLE()
	{
		return le;
	}

	public void setLE(String le)
	{
		this.le = le;
	}

	public boolean isNumber()
	{
		return !getDataType().equals("String");
	}

	public HashMap getAliasMap()
	{
		return aliasMap;
	}

	public void setAliasMap(HashMap aliasMap)
	{
		this.aliasMap = aliasMap;
	}

	public Object clone()
		throws CloneNotSupportedException
	{
		EnField obj = (EnField)super.clone();
		return obj;
	}

	public boolean isAutoEvaluate()
	{
		return autoEvaluate;
	}

	public void setAutoEvaluate(boolean autoEvaluate)
	{
		this.autoEvaluate = autoEvaluate;
	}

	public boolean isFromParent()
	{
		return fromParent;
	}

	public void setFromParent(boolean fromParent)
	{
		this.fromParent = fromParent;
	}

	public HashMap getAliasIndexKeyMap()
	{
		return aliasIndexKeyMap;
	}

	public void setAliasIndexKeyMap(HashMap aliasIndexKeyMap)
	{
		this.aliasIndexKeyMap = aliasIndexKeyMap;
	}
}
