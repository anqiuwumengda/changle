package hlc.base;

import hlc.base.annotation.Properties;
import hlc.base.annotation.Table;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;


public abstract class EnBase
	implements Cloneable, Serializable
{
	private String tableName;
	private String primaryKey="";
	private HashMap mapClassField = new HashMap();

	public EnBase()
	{
		initClass();
	}

	private void initClass()throws UnsupportedOperationException
	{
		Annotation[] tableName = this.getClass().getAnnotations();
		for(Annotation an : tableName){
			if(an instanceof Table){
				this.tableName = ((Table)an).name();
				break;
			}
		}
		
		Field[] properties = this.getClass().getDeclaredFields();
		for(Field property : properties){
			Annotation[] p = property.getAnnotations();
			for(int i=0;i<p.length;i++){
				//处理键信息
				if(p[i] instanceof Properties){
					EnField cEnField = new EnField();
					cEnField.setFieldName(property.getName());
					if(((Properties)p[i]).IsPk()==true){
						String pkname = property.getName();
						if(this.primaryKey.equals("")){
							this.primaryKey = pkname;
						}else{
							this.primaryKey = this.primaryKey+","+pkname;
						}
						cEnField.setPk(true);
					}
					String enName = ((Properties)p[i]).enName();
					if(enName!=null&&!enName.equals("")){
						cEnField.setEnName(enName);
					}
					
					String cnName = ((Properties)p[i]).cnName();
					if(cnName!=null&&!cnName.equals("")){
						cEnField.setCnName(cnName);
					}
					
					String dataType = ((Properties)p[i]).dataType();
					if(dataType!=null&&!dataType.equals("")){
						cEnField.setDataType(dataType);
					}
					
					boolean needSave = ((Properties)p[i]).needSave();
					cEnField.setNeedSave(needSave);
					
					this.mapClassField.put(property.getName(), cEnField);
				}
			}
		}
	}
	
	public String getTableName()
		throws Exception
	{
		return this.tableName;
	}
	
	public  String getPrimaryKey()
	{
		return this.primaryKey;
	}

	public EnField[] getFieldArray() {
		Object obj = mapClassField.get(getClass().getName());
		Set keySet = mapClassField.keySet();
		Iterator it = keySet.iterator();
		ArrayList al = new ArrayList();
		while(it.hasNext()){
			String name = (String)it.next();
			al.add(mapClassField.get(name));
		}
		
		EnField aEnField[] = new EnField[al.size()];
		System.arraycopy(((Object) (al.toArray())), 0, aEnField, 0, al.size());
		return aEnField;
	}
	public Object getValue(String fieldName)throws Exception{
		EnField field = (EnField)mapClassField.get(fieldName);;
		String get_is = "get";
		if (field.getDataType().equals("boolean"))
			get_is = "is";
		String methodName = get_is + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Object obj = getClass().getMethod(methodName, null).invoke(this, null);
		return obj;
	
	}
	public void setValue(String fieldName, Object fieldValue)throws Exception
	{
		String aTmp[] = fieldName.replace('.', ',').split(",");
		Object object = this;
		for (int i = 0; i < aTmp.length - 1; i++)
			object = getValue(aTmp[i]);
	
		setValue((EnBase)object, aTmp[aTmp.length - 1], fieldValue);
	}

	public EnField getEnField(String fieldName)throws Exception
	{
		String aTmp[] = fieldName.replace('.', ',').split(",");
		Object object = this;
		for (int i = 0; i < aTmp.length - 1; i++)
			object = getValue( aTmp[i]);
	
		if (!(object instanceof EnBase))
			throw new Exception("数据类型错误");
		EnField aEnField[] = ((EnBase)object).getFieldArray();
		for (int i = 0; i < aEnField.length; i++)
			if (aEnField[i].getFieldName().equals(aTmp[aTmp.length - 1]))
				return aEnField[i];
	
		throw new Exception("05");
	}
	
	private void setValue(EnBase obj, String fieldName, Object fieldValue)
		throws Exception
	{
		EnField field = obj.getEnField(fieldName);
		String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Class clsTmp;
		if (field.getDataType().equals("String"))
			clsTmp = java.lang.String.class;
		else
		if (field.getDataType().equals("int"))
		{
			clsTmp = Integer.TYPE;
			String value = fieldValue.toString();
			if (value.indexOf(",") > -1)
				value = value.replaceAll(",", "");
			fieldValue = new Integer(value);
		} else
		if (field.getDataType().equals("double"))
		{
			clsTmp = Double.TYPE;
			String value = fieldValue.toString();
			if (value.indexOf(",") > -1)
				value = value.replaceAll(",", "");
			fieldValue = new Double(value);
		} else
		if (field.getDataType().equals("boolean"))
		{
			clsTmp = Boolean.TYPE;
			if (fieldValue.toString().equals("true"))
				fieldValue = new Boolean(true);
			else
				fieldValue = new Boolean(false);
		} else
		if (field.getDataType().equals("int[]"))
		{
			clsTmp = int[].class;
			if (!(fieldValue instanceof String[]))
				fieldValue = fieldValue.toString().split(",");
			String a[] = (String[])(String[])fieldValue;
			int v[] = new int[a.length];
			for (int i = 0; i < a.length; i++)
				v[i] = Integer.parseInt(a[i]);
	
			fieldValue = v;
		} else
		if (field.getDataType().equals("double[]"))
		{
			clsTmp = double[].class;
			if (!(fieldValue instanceof String[]))
				fieldValue = fieldValue.toString().split(",");
			String a[] = (String[])(String[])fieldValue;
			double v[] = new double[a.length];
			for (int i = 0; i < a.length; i++)
				v[i] = Double.parseDouble(a[i]);
	
			fieldValue = v;
		} else
		if (field.getDataType().equals("byte[]"))
			clsTmp = byte[].class;
		else
		if (field.getDataType().equals("String[]"))
		{
			clsTmp = java.lang.String[].class;
			if (!(fieldValue instanceof String[]))
				fieldValue = fieldValue.toString().split(",");
			String a[] = (String[])(String[])fieldValue;
			if (a.length == 1 && a[0].indexOf(";") > -1)
				fieldValue = a[0].split(";");
		} else
		{
			clsTmp = fieldValue.getClass();
		}
		Class aPara[] = {
			clsTmp
		};
		Object aObj[] = {
			fieldValue
		};
		obj.getClass().getMethod(methodName, aPara).invoke(obj, aObj);
	}
}
