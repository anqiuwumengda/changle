package hlc.base;

import hlc.base.db.DataSet;
import hlc.base.db.DbAccess;
import hlc.util.MultiPage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class DbAccessHelper {
	public static Logger bAccessHelper = Logger.getLogger(DbAccessHelper.class);
	public static Object[] executeQuery(String classname, String sql)  throws Exception{
		Object[] obj = null;
		DbAccess db = new DbAccess();
		DataSet ds = db.executeQuery(sql);
		obj = new Object[ds.size()];
		Class onwClass = Class.forName(classname);
		for (int i = 0; i < ds.size(); i++) {
			Object model = onwClass.newInstance();
			Field[] field = model.getClass().getDeclaredFields();
			for (int j = 0; j < field.length; j++) {
				String name = field[j].getName();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				String type = field[j].getGenericType().toString();

				if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
					// Method m = model.getClass().getMethod("get" + name);
					Method m = null;
					// String value = (String) m.invoke(model); //
					// 调用getter方法获取属性值
					// if (value == null) {
					m = model.getClass().getMethod("set" + name, String.class);
					if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
						////System.out.println("结果集中不存在:"+name.toUpperCase());
						continue;
					}
					m.invoke(model, ds.getString(i, name));
					// }
				}
				if (type.equals("int")) {
					// Method m = model.getClass().getMethod("get" + name);
					Method m = null;
					// Integer value = (Integer) m.invoke(model);
					// if (value == null) {
					m = model.getClass().getMethod("set" + name, int.class);
					if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
						//System.out.println("结果集中不存在:"+name.toUpperCase());
						continue;
					}
					m.invoke(model, ds.getInt(i, name));
					// }
				}
				if (type.equals("class java.lang.Boolean")) {
					// Method m = model.getClass().getMethod("get" + name);
					Method m = null;
					// Boolean value = (Boolean) m.invoke(model);
					// if (value == null) {
					m = model.getClass().getMethod("set" + name, Boolean.class);
					if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
						//System.out.println("结果集中不存在:"+name.toUpperCase());
						continue;
					}
					m.invoke(model, false);
					// }
				}
				if (type.equals("class java.util.Date")) {
					// Method m = model.getClass().getMethod("get" + name);
					Method m = null;
					// Date value = (Date) m.invoke(model);
					// if (value == null) {
					m = model.getClass().getMethod("set" + name, Date.class);
					if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
						//System.out.println("结果集中不存在:"+name.toUpperCase());
						continue;
					}
					m.invoke(model, new Date());
					// }
				}
				if (type.equals("double")) {
					Method m = null;
					m = model.getClass().getMethod("set" + name, double.class);
					if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
						//System.out.println("结果集中不存在:"+name.toUpperCase());
						continue;
					}
					m.invoke(model, ds.getDouble(i, name));
				}
				if (type.equals("long")) {
					Method m = null;
					m = model.getClass().getMethod("set" + name, long.class);
					if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
						//System.out.println("结果集中不存在:"+name.toUpperCase());
						continue;
					}
					m.invoke(model, ds.getLong(i, name));
				}
			}
			obj[i] = model;
			
		}

		return obj;
	}

	public static Object executeQueryOne(String classname, String sql)  throws Exception{
		Object[] obj = null;
		DbAccess db = new DbAccess();
		DataSet ds = db.executeQuery(sql);
		obj = new Object[ds.size()];
		Class onwClass = Class.forName(classname);
		if(ds.size()>0){
			for (int i = 0; i < ds.size(); i++) {
				Object model = onwClass.newInstance();
				Field[] field = model.getClass().getDeclaredFields();
				for (int j = 0; j < field.length; j++) {
					String name = field[j].getName();
					name = name.substring(0, 1).toUpperCase() + name.substring(1);
					String type = field[j].getGenericType().toString();

					if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
						// Method m = model.getClass().getMethod("get" + name);
						Method m = null;
						// String value = (String) m.invoke(model); //
						// 调用getter方法获取属性值
						// if (value == null) {
						m = model.getClass().getMethod("set" + name, String.class);
						if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
							////System.out.println("结果集中不存在:"+name.toUpperCase());
							continue;
						}
						m.invoke(model, ds.getString(i, name));
						// }
					}
					if (type.equals("int")) {
						// Method m = model.getClass().getMethod("get" + name);
						Method m = null;
						// Integer value = (Integer) m.invoke(model);
						// if (value == null) {
						m = model.getClass().getMethod("set" + name, int.class);
						if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
							//System.out.println("结果集中不存在:"+name.toUpperCase());
							continue;
						}
						m.invoke(model, ds.getInt(i, name));
						// }
					}
					if (type.equals("class java.lang.Boolean")) {
						// Method m = model.getClass().getMethod("get" + name);
						Method m = null;
						// Boolean value = (Boolean) m.invoke(model);
						// if (value == null) {
						m = model.getClass().getMethod("set" + name, Boolean.class);
						if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
							//System.out.println("结果集中不存在:"+name.toUpperCase());
							continue;
						}
						m.invoke(model, false);
						// }
					}
					if (type.equals("class java.util.Date")) {
						// Method m = model.getClass().getMethod("get" + name);
						Method m = null;
						// Date value = (Date) m.invoke(model);
						// if (value == null) {
						m = model.getClass().getMethod("set" + name, Date.class);
						if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
							//System.out.println("结果集中不存在:"+name.toUpperCase());
							continue;
						}
						m.invoke(model, new Date());
						// }
					}
					if (type.equals("double")) {
						Method m = null;
						m = model.getClass().getMethod("set" + name, double.class);
						if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
							//System.out.println("结果集中不存在:"+name.toUpperCase());
							continue;
						}
						m.invoke(model, ds.getDouble(i, name));
					}
					if (type.equals("long")) {
						Method m = null;
						m = model.getClass().getMethod("set" + name, long.class);
						if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
							//System.out.println("结果集中不存在:"+name.toUpperCase());
							continue;
						}
						m.invoke(model, ds.getLong(i, name));
					}
				}
				obj[i] = model;
				
			}
		}else{
			return null;
		}

		

		return obj[0];
	}
	
	public static Object[] executePageQuery(String classname, String sql, MultiPage cMultiPage)throws Exception{
		Object[] obj = null;
		DbAccess db = new DbAccess();
		DataSet ds = db.executePageQuery(sql,cMultiPage);
		obj = new Object[ds.size()];
		Class onwClass = Class.forName(classname);
		for (int i = 0; i < ds.size(); i++) {
			Object model = onwClass.newInstance();
			Field[] field = model.getClass().getDeclaredFields();
			for (int j = 0; j < field.length; j++) {
				String name = field[j].getName();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				String type = field[j].getGenericType().toString();

				if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
					// Method m = model.getClass().getMethod("get" + name);
					Method m = null;
					// String value = (String) m.invoke(model); //
					// 调用getter方法获取属性值
					// if (value == null) {
					m = model.getClass().getMethod("set" + name, String.class);
					if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
						//System.out.println("结果集中不存在:"+name.toUpperCase());
						continue;
					}
					m.invoke(model, ds.getString(i, name));
					// }
				}
				if (type.equals("int")) {
					// Method m = model.getClass().getMethod("get" + name);
					Method m = null;
					// Integer value = (Integer) m.invoke(model);
					// if (value == null) {
					m = model.getClass().getMethod("set" + name, int.class);
					if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
						//System.out.println("结果集中不存在:"+name.toUpperCase());
						continue;
					}
					m.invoke(model, ds.getInt(i, name));
					// }
				}
				if (type.equals("class java.lang.Boolean")) {
					// Method m = model.getClass().getMethod("get" + name);
					Method m = null;
					// Boolean value = (Boolean) m.invoke(model);
					// if (value == null) {
					m = model.getClass().getMethod("set" + name, Boolean.class);
					if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
						//System.out.println("结果集中不存在:"+name.toUpperCase());
						continue;
					}
					m.invoke(model, false);
					// }
				}
				if (type.equals("class java.util.Date")) {
					// Method m = model.getClass().getMethod("get" + name);
					Method m = null;
					// Date value = (Date) m.invoke(model);
					// if (value == null) {
					m = model.getClass().getMethod("set" + name, Date.class);
					if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
						//System.out.println("结果集中不存在:"+name.toUpperCase());
						continue;
					}
					m.invoke(model, new Date());
					// }
				}
				if (type.equals("double")) {
					Method m = null;
					m = model.getClass().getMethod("set" + name, double.class);
					if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
						//System.out.println("结果集中不存在:"+name.toUpperCase());
						continue;
					}
					m.invoke(model, ds.getDouble(i, name));
				}
				if (type.equals("long")) {
					Method m = null;
					m = model.getClass().getMethod("set" + name, long.class);
					if(!ds.getHtNameNum().containsKey(name.toUpperCase())){
						//System.out.println("结果集中不存在:"+name.toUpperCase());
						continue;
					}
					m.invoke(model, ds.getLong(i, name));
				}
			}
			obj[i] = model;
			
		}

		return obj;
	}
	
	public  List<Map<String,String>> queryForList(ResultSet rs) throws Exception{
		
		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
		List<String> list =getListColunm(rsmd,numCols);
		setValues(rs,listMap,list,numCols);
		
		return listMap;
	}
	public  Map<String,String> queryForMap(ResultSet rs) throws Exception{
		Map<String,String> Map = new HashMap<String,String>();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
		List<String> list =getListColunm(rsmd,numCols);
		setValuesMap(rs,Map,list,numCols);
		
		return Map;
	}
	public List<String> getListColunm(ResultSetMetaData rsmd,int numCols ) throws SQLException{
		List<String> list = new ArrayList();
		
		for(int i = 0; i < numCols; i++)
        {
			list.add(rsmd.getColumnName(i + 1));
        }
		return list;
	}
	
	public void setValues(ResultSet rs,List<Map<String,String>> listMap,List<String> list,int numCols) throws Exception{
		Map<String,String> map =null;
		while(rs.next())
        {
			map  = new HashMap<String,String>();
            for(int i=0;i<numCols;i++)
            {
           	 byte[] b = rs.getBytes(i+1);
           	 if(null != b ){
           		map.put(list.get(i), new String(b,"utf-8"));
           	 }else{
           		map.put(list.get(i), "");
           	 }
            }
            listMap.add(map);
       }
	}
	public void setValuesMap(ResultSet rs,Map<String,String> map,List<String> list,int numCols) throws Exception{
		while(rs.next())
        {
			//map  = new HashMap<String,String>();
            for(int i=0;i<numCols;i++)
            {
           	 byte[] b = rs.getBytes(i+1);
           	 if(null != b ){
           		map.put(list.get(i), new String(b,"utf-8"));
           	 }else{
           		map.put(list.get(i), "");
           	 }
            }
       }
	}
}
