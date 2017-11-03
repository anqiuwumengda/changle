package hlc.base;

import hlc.base.db.DataSet;
import hlc.base.db.DbAccess;
import hlc.util.Function;
import hlc.util.MultiPage;

import java.lang.reflect.Array;
import java.util.*;

import org.apache.log4j.Logger;


public class DAOBaseCqh {
	public static Logger daoBase = Logger.getLogger(DAOBaseCqh.class);
	public DAOBaseCqh() {

	}

	public static EnBase[] findAll(String className) throws Exception {
		String tableName = ((EnBase) Class.forName(className).newInstance())
				.getTableName();
		String sql = "select * from " + tableName;

		DbAccess db = new DbAccess();
		DataSet ds = db.executeQuery(sql);
		System.out.println(sql);
		EnBase aEnBase[] = resumeFromDataSet(ds, className);
		return aEnBase;

	}

	
	public static EnBase[] findByCondition(String className,
			String sqlWhereRegex, String sqlOrderByRegex) throws Exception {
		DbAccess db = new DbAccess();
		String tableName = ((EnBase) Class.forName(className).newInstance())
				.getTableName();
		String sql = "select * from " + tableName;
		if (!sqlWhereRegex.trim().equals(""))
			sql = sql + " where " + sqlWhereRegex;
		if (!sqlOrderByRegex.trim().equals(""))
			sql = sql + " order by " + sqlOrderByRegex;
		System.out.println(sql);
		DataSet ds = db.executeQuery(sql);
		EnBase aEnBase[] = resumeFromDataSet(ds, className);
		return aEnBase;
	}
	
	public static void findAllForPage(String className,MultiPage cMultiPage) throws Exception {
		String tableName = ((EnBase) Class.forName(className).newInstance())
				.getTableName();
		String sql = "select * from " + tableName;
		System.out.println(sql);
//		String sql = "select charts_no, charts_name, charts_type from  ";
		DbAccess db = new DbAccess();
		db.executePageQuery(sql, className, cMultiPage);
	}
	
	public static void findByConForPage(String className,String sqlWhereRegex, String sqlOrderByRegex,MultiPage cMultiPage) throws Exception {
		DbAccess db = new DbAccess();
		String tableName = ((EnBase) Class.forName(className).newInstance())
				.getTableName();
		String sql = "select * from " + tableName;
		if (!sqlWhereRegex.trim().equals(""))
			sql = sql + " where " + sqlWhereRegex;
		if (!sqlOrderByRegex.trim().equals(""))
			sql = sql + " order by " + sqlOrderByRegex;
		System.out.println(sql);
		db.executePageQuery(sql, className, cMultiPage);

	}
	
	public static void findByEnForPage(EnBase en,MultiPage cMultiPage) throws Exception {
		if(en==null){
			throw new Exception("the en is null");
		}
		findByEnForPage(en,null,cMultiPage);
	}
	public static void  findByEnForPage(String className,MultiPage cMultiPage) throws Exception {
		findByEnForPage(null,className,cMultiPage);
	}
	public static void findByEnForPage(EnBase en,String className,MultiPage cMultiPage) throws Exception {
		if(en==null)
			en =  ((EnBase) Class.forName(className).newInstance());
		DbAccess db = new DbAccess();
		String tableName = en.getTableName();
		String sql = "select * from " + tableName;
		EnField[] aEnField = en.getFieldArray();
		StringBuffer sqlWhereRegex = new StringBuffer();
		for(int i=0;i<aEnField.length;i++){
			Object obj = en.getValue( aEnField[i].getFieldName());
			if(obj!=null&&!obj.equals("")){
				if (aEnField[i].getDataType().equals("String"))
					sqlWhereRegex.append(" and " + aEnField[i].getFieldName() + "='" + en.getValue( aEnField[i].getFieldName()) + "'");
				else{
					if((Double.parseDouble(obj.toString()))!=-1){
						sqlWhereRegex.append(" and " + aEnField[i].getFieldName() + "=" + en.getValue( aEnField[i].getFieldName()));
					}
				}
					
			}
		}
		//if (!sqlOrderByRegex.trim().equals(""))
			//sql = sql + " order by " + sqlOrderByRegex;
		if(sqlWhereRegex.length()!=0){
			sql = sql+" where 1=1 "+sqlWhereRegex.toString();
		}
		if(className==null){
			className = en.getClass().getName();
		}
		System.out.println(sql);
		db.executePageQuery(sql, className, cMultiPage);
	}
	

	public static EnBase findByPK(DbAccess db, String className, String pkValue)
			throws  Exception {
		if (pkValue == null)
			throw new Exception("无查询主键");
		EnBase en = (EnBase)Class.forName(className).newInstance();
		String tableName = en.getTableName();
		String aValue[] = pkValue.split(",");
		String pkTmp = en.getPrimaryKey();
		String aPK[] = pkTmp.split(",");
		if (aValue.length != aPK.length)
			throw new Exception("主键不一致");
		StringBuffer pk = new StringBuffer();
		EnField aEnField[] = en.getFieldArray();
		int k = 0;
		for (int i = 0; i < aEnField.length; i++)
		{
			if (!aEnField[i].isPk())
				continue;
			Object objValue = aValue[k];
			if (aEnField[i].getDataType().equals("String"))
				pk.append(" and " + aEnField[i].getFieldName() + "='" + objValue + "'");
			else
				pk.append(" and " + aEnField[i].getFieldName() + "=" + objValue);
			k++;
		}

		if (pk.length() == 0)
			throw new Exception("主键为空");
		pk = pk.delete(0, 5);
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(tableName);
		sql.append(" where ");
		sql.append(pk);
		System.out.println(sql.toString());
		DataSet ds = db.executeQuery(sql.toString());
		
		if (ds.size() > 0)
			return resumeFromDataSet(ds, 0, className);
		else
			throw new Exception ("Object " + className + "{" + pk + "} not found.");
	
		//return null;
	}
	public static EnBase findByEn(EnBase en) throws Exception {
		if(en==null)
			throw new Exception("the en is null");
		return findByEn( en,null );
	}
	public static EnBase findByEn(EnBase en,String className ) throws Exception {
		if(en==null)
			en =  ((EnBase) Class.forName(className).newInstance());
		DbAccess db = new DbAccess();
		String tableName = en.getTableName();
		String sql = "select * from " + tableName;
		EnField[] aEnField = en.getFieldArray();
		StringBuffer sqlWhereRegex = new StringBuffer();
		for(int i=0;i<aEnField.length;i++){
			Object obj = en.getValue( aEnField[i].getFieldName());
			if(obj!=null&&!obj.toString().equals("-1")&&!obj.equals("")){
				if (aEnField[i].getDataType().equals("String"))
					sqlWhereRegex.append(" and " + aEnField[i].getFieldName() + "='" + en.getValue( aEnField[i].getFieldName()) + "'");
				else
					sqlWhereRegex.append(" and " + aEnField[i].getFieldName() + "=" + en.getValue( aEnField[i].getFieldName()));
			}
		}
		//if (!sqlOrderByRegex.trim().equals(""))
			//sql = sql + " order by " + sqlOrderByRegex;
		if(sqlWhereRegex.length()!=0){
			sql = sql+" where 1=1 "+sqlWhereRegex.toString();
		}
		System.out.println(sql);
		DataSet ds = db.executeQuery(sql);
		EnBase aEnBase[] = resumeFromDataSet(ds, en.getClass().getName());
		return aEnBase[0];
	}
	

	public static EnBase[] resumeFromDataSet(DataSet ds, String className)
			throws Exception {
		Object a = Array.newInstance(Class.forName(className), ds.size());
		for (int i = 0; i < Array.getLength(a); i++)
			Array.set(a, i, resumeFromDataSet(ds, i, className));

		return (EnBase[]) (EnBase[]) a;
	}

	public static EnBase resumeFromDataSet(DataSet ds, int rowIndex,
			String className) throws Exception {
		EnBase en = (EnBase) (EnBase) Class.forName(className).newInstance();
		setValueFromDataSet(ds, rowIndex, en);
		return en;
	}

	public static void setValueFromDataSet(DataSet ds, int rowIndex, EnBase en)
			throws Exception {
		EnField aEnField[] = en.getFieldArray();
		for (int i = 0; i < aEnField.length; i++) {
			if (aEnField[i].getDataType().equals("EnBase"))
				continue;
			String fieldName = aEnField[i].getFieldName();
			String columnName = ds.getColumnName(fieldName);
			if (columnName == null || columnName.equals(""))
				continue;
			Object objValue = ds.getObject(rowIndex, fieldName);
			if (objValue != null)
				en.setValue(fieldName, objValue);
		}

	}

	public static String arrayToString(Object array) throws Exception {
		if (array instanceof int[])
			return Function.arrayToString((int[]) (int[]) array, ",", "");
		if (array instanceof double[]) {
			double d[] = (double[]) (double[]) array;
			String a[] = new String[d.length];
			for (int i = 0; i < a.length; i++)
				a[i] = String.valueOf(d[i]);

			return Function.arrayToString(a, ",");
		}
		if (array instanceof String[])
			return Function.arrayToString((String[]) (String[]) array, ",");
		if (array instanceof AbstractCollection) {
			Object ac[] = ((AbstractCollection) array).toArray();
			String a[] = new String[ac.length];
			for (int i = 0; i < a.length; i++)
				a[i] = ac[i].toString();

			return Function.arrayToString(a, ",");
		} else {
			return "";
		}
	}

	public static void createTable(DbAccess db, EnBase en) throws Exception {
		String tableName = en.getTableName();
		EnField aEnField[] = en.getFieldArray();
		StringBuffer fields = new StringBuffer();
		StringBuffer pk = new StringBuffer();
		for (int i = 0; i < aEnField.length; i++) {
			if (!aEnField[i].isNeedSave())
				continue;
			if (aEnField[i].isPk())
				pk.append("," + aEnField[i].getFieldName());
			if (aEnField[i].getDataType().equals("String")) {
				fields.append("," + aEnField[i].getFieldName() + " char("
						+ aEnField[i].getMaxLength() + ")");
				continue;
			}
			if (aEnField[i].getDataType().equals("int")) {
				fields.append("," + aEnField[i].getFieldName() + " int");
				continue;
			}
			if (aEnField[i].getDataType().equals("double"))
				fields.append("," + aEnField[i].getFieldName() + " double");
		}

		fields.deleteCharAt(0);
		if (pk.length() > 0) {
			pk.deleteCharAt(0);
			pk.append(",primary key(");
			pk.append(pk);
			pk.append(")");
		}
		StringBuffer sql = new StringBuffer("create table ");
		sql.append(tableName);
		sql.append("(");
		sql.append(fields);
		sql.append(pk);
		sql.append(")");
		db.executeUpdate(sql.toString());
	}

	public static boolean exist(DbAccess db, EnBase en) throws Exception {
		StringBuffer pk = new StringBuffer();
		EnField aEnField[] = en.getFieldArray();
		int k = 0;
		for (int i = 0; i < aEnField.length; i++) {
			if (!aEnField[i].isPk())
				continue;
			Object objValue = en.getValue(aEnField[i].getFieldName());
			if (aEnField[i].getDataType().equals("String"))
				pk.append(" and " + aEnField[i].getFieldName() + "='"
						+ objValue + "'");
			else
				pk
						.append(" and " + aEnField[i].getFieldName() + "="
								+ objValue);
			k++;
		}

		if (pk.length() == 0)
			throw new Exception("主键不空");
		pk = pk.delete(0, 5);
		StringBuffer sql = new StringBuffer("select '1' from ");
		sql.append(en.getTableName());
		sql.append(" where ");
		sql.append(pk);
		DataSet ds = db.executeQuery(sql.toString());
		return !ds.isEmpty();
	}

	public static int insert(DbAccess db, EnBase en) throws Exception {
		String tableName = en.getTableName();
		EnField aEnField[] = en.getFieldArray();
		;
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(tableName);
		sql.append("(");
		StringBuffer fields = new StringBuffer();
		for (int i = 0; i < aEnField.length; i++) {
			Object objValue = en.getValue(aEnField[i].getFieldName());
			if (!(objValue instanceof EnBase)
					&& !(objValue instanceof EnBase[])
					&& aEnField[i].isNeedSave())
				fields.append("," + aEnField[i].getFieldName());
		}

		fields = fields.deleteCharAt(0);
		sql.append(fields);
		sql.append(")");
		sql.append(" values(");
		StringBuffer values = new StringBuffer();
		int x = 0;
		Hashtable mapValue = new Hashtable();
		for (int i = 0; i < aEnField.length; i++) {
			boolean isString = false;
			boolean isByteArray = false;
			Object objValue = en.getValue(aEnField[i].getFieldName());
			if (!aEnField[i].isNeedSave())
				continue;
			if (aEnField[i].getDataType().equals("String"))
				isString = true;
			else if (objValue instanceof EnBase)
				insert(db, (EnBase) objValue);
			else if (objValue instanceof EnBase[]) {
				for (int j = 0; j < ((EnBase[]) (EnBase[]) objValue).length; j++)
					insert(db, ((EnBase[]) (EnBase[]) objValue)[j]);
			} else if (objValue instanceof byte[])
				isByteArray = true;
			else if (aEnField[i].getDataType().indexOf("[]") > -1) {
				isString = true;
				objValue = arrayToString(objValue);
			} else if (objValue instanceof AbstractCollection) {
				isString = true;
				objValue = arrayToString(objValue);
			} else {
				values.append("," + objValue);
			}

			if (isString) {
				if(objValue!=null && !"".equals(objValue)){
					if(objValue.toString().substring(0,1).equals("$")){
						values.append("," + objValue.toString().substring(1) + "");
					}else{
						values.append(",'" + objValue + "'");
					}
				}else{
					objValue = "''";
					values.append("," + objValue + "");
				}
			}

		}

		values = values.deleteCharAt(0);
		sql.append(values);
		sql.append(")");
		System.out.println(sql);
		/*
		 * db.prepareSQL(sql.toString()); for (int i = 1; i <= x; i++) { Object
		 * tmp = mapValue.get(String.valueOf(i)); if (tmp instanceof byte[])
		 * db.setPrepareBytes(i,(byte[])tmp); else db.setPrepareString(i,
		 * (String)tmp); }
		 */
		return db.executeUpdate(sql.toString());
	}

	public static int delete(DbAccess db, String className, String pkValue)
			throws Exception {
		EnBase en = (EnBase) Class.forName(className).newInstance();
		String tableName = en.getTableName();
		EnField aEnField[] = en.getFieldArray();
		return delete(db, tableName, aEnField, null, pkValue);
	}

	public static int delete(DbAccess db, EnBase en) throws Exception {
		String tableName = en.getTableName();
		EnField aEnField[] = en.getFieldArray();
		return delete(db, tableName, aEnField, en, "");
	}

	private static int delete(DbAccess db, String tableName,
			EnField aEnField[], EnBase en, String pkValue) throws Exception {
		String aPKValue[] = pkValue.split(",");
		StringBuffer pk = new StringBuffer();
		int n = 0;
		for (int i = 0; i < aEnField.length; i++) {
			Object objValue = "";
			if (en != null)
				objValue = en.getValue(aEnField[i].getFieldName());
			if (aEnField[i].isPk()) {
				if (en == null)
					objValue = aPKValue[n];
				if (aEnField[i].getDataType().equals("String"))
					pk.append(" and " + aEnField[i].getFieldName() + "='"
							+ objValue + "'");
				else
					pk.append(" and " + aEnField[i].getFieldName() + "="
							+ objValue);
				n++;
			}
			if (objValue instanceof EnBase) {
				delete(db, (EnBase) objValue);
				continue;
			}
			if (!(objValue instanceof EnBase[])) {
				continue;
			}
			for (int j = 0; j < ((EnBase[]) (EnBase[]) objValue).length; j++)
				delete(db, ((EnBase[]) (EnBase[]) objValue)[j]);

		}

		if (pk.length() == 0) {
			throw new Exception("无删除主键");
		} else {
			pk = pk.delete(0, 5);
			StringBuffer sql = new StringBuffer("delete from ");
			sql.append(tableName);
			sql.append(" where ");
			sql.append(pk);
			System.out.println(sql.toString());
			return db.executeUpdate(sql.toString());
		}
	}

	public static int deleteByCondition(DbAccess db, String className,
			String sqlWhereRegex) throws Exception {
		EnBase en = (EnBase) Class.forName(className).newInstance();
		String tableName = en.getTableName();
		String sql = "delete from " + tableName;
		if (!sqlWhereRegex.trim().equals(""))
			sql = sql + " where " + sqlWhereRegex;

		System.out.println(sql);
		return db.executeUpdate(sql);
	}

	public static int update(DbAccess db, EnBase en) throws Exception {
		return update(db, en, new String[0]);
	}

	public static int update(DbAccess db, EnBase en, String updateField[])
			throws Exception {
		int cnt = 0;
		ArrayList al = new ArrayList();
		HashMap mapEnUpdateField = new HashMap();
		processUpdateField(en, updateField, al, mapEnUpdateField);
		EnBase cEnBase = en;
		String tableName = en.getTableName();
		// EnBase 1 = en;
		EnField aEnField[] = en.getFieldArray();
		StringBuffer fv = new StringBuffer();
		StringBuffer pk = new StringBuffer();
		int x = 0;
		Hashtable mapValue = new Hashtable();
		for (int i = 0; i < aEnField.length; i++) {
			boolean isString = false;
			boolean isByteArray = false;
			String fieldName = aEnField[i].getFieldName();
			Object objValue = en.getValue(fieldName);
			if (aEnField[i].isPk())
				if (aEnField[i].getDataType().equals("String"))
					pk.append(" and " + aEnField[i].getFieldName() + "='"
							+ objValue + "'");
				else
					pk.append(" and " + aEnField[i].getFieldName() + "="
							+ objValue);
			if (aEnField[i].isPk() || !aEnField[i].isNeedSave()
					|| al.size() > 0
					&& !al.contains(aEnField[i].getFieldName()))
				continue;
			if (aEnField[i].getDataType().equals("String"))
				isString = true;
			else if (objValue instanceof EnBase) {
				String a[] = getUpdateFields(mapEnUpdateField, aEnField[i]);
				cnt += update(db, (EnBase) objValue, a);
			} else if (objValue instanceof EnBase[]) {
				String a[] = getUpdateFields(mapEnUpdateField, aEnField[i]);
				for (int j = 0; j < ((EnBase[]) (EnBase[]) objValue).length; j++)
					cnt += update(db, ((EnBase[]) (EnBase[]) objValue)[j], a);

			} else if (objValue instanceof byte[])
				isByteArray = true;
			else if (aEnField[i].getDataType().indexOf("[]") > -1) {
				isString = true;
				objValue = arrayToString(objValue);
			} else if (objValue instanceof AbstractCollection) {
				isString = true;
				objValue = arrayToString(objValue);
			}  else {
				if(null != en.getValue(fieldName)){
					if(Double.parseDouble(en.getValue(fieldName).toString()) != 0.0){
						fv.append("," + fieldName + "=" + en.getValue(fieldName));
					}
					
				}
				
			}
			if (isString) {
				if(null != objValue){
					fv.append("," + fieldName + "='" + objValue + "'");
				}
				
			}
		}

		if (fv.length() > 0)
			fv = fv.deleteCharAt(0);
		if (pk.length() == 0)
			return 0;
		pk.delete(0, 5);
		StringBuffer sql = new StringBuffer("update ");
		sql.append(tableName);
		sql.append(" set ");
		sql.append(fv);
		sql.append(" where ");
		sql.append(pk);
		System.out.println(sql);
		/*
		 * if (fv.toString().trim().length() > 0) {
		 * db.prepareSQL(sql.toString()); for (int i = 1; i <= x; i++) { Object
		 * tmp = mapValue.get(String.valueOf(i)); if (tmp instanceof byte[])
		 * db.setPrepareBytes(i, (byte[])tmp); else db.setPrepareString(i,
		 * tmp.toString()); }
		 * 
		 * cnt = db.executePreparedUpdate(); }
		 */
		cnt = db.executeUpdate(sql.toString());
		return cnt;
	}

	private static void processUpdateField(EnBase en, String updateField[],
			ArrayList al, HashMap mapEnUpdateField) throws Exception {
		if (updateField.length > 0) {
			ArrayList alTmp = new ArrayList();
			for (int i = 0; i < en.getFieldArray().length; i++)
				alTmp.add(en.getFieldArray()[i].getFieldName());

			for (int i = 0; i < updateField.length; i++) {
				String enName = updateField[i];
				if (updateField[i].indexOf(".") > -1) {
					enName = updateField[i].substring(0, updateField[i]
							.indexOf("."));
					Object objTmp = mapEnUpdateField.get(enName);
					if (objTmp == null)
						objTmp = new String[0];
					String a[] = (String[]) (String[]) objTmp;
					String a1[] = new String[a.length + 1];
					System.arraycopy(a, 0, a1, 0, a.length);
					a1[a.length] = updateField[i].substring(updateField[i]
							.indexOf(".") + 1);
					mapEnUpdateField.put(enName, a1);
				}
				if (!alTmp.contains(enName)) {
					// String msg[] = {
					// enName, en.getClassName()
					// };
				}
				al.add(enName);
			}

		}
	}

	private static String[] getUpdateFields(HashMap mapEnUpdateField,
			EnField field) {
		String a[] = (String[]) (String[]) mapEnUpdateField.get(field
				.getFieldName());
		if (a == null)
			a = new String[0];
		return a;
	}
	
	public static String getUpdSql(Map<String,String> map,String tableName,String ...pkKey) throws Exception{
		StringBuffer sb = new StringBuffer();
		StringBuffer pk = new StringBuffer();
		StringBuffer set = new StringBuffer();
		sb.append("update ");
		sb.append(tableName);
		sb.append(" set ");
		
		if(pkKey != null && pkKey.length>0 && tableName != null && !"".equals(tableName)){
			for(String tmpPk : pkKey){
				if(map.containsKey(tmpPk)){
					pk.append(" "+tmpPk+"="+"'"+map.get(tmpPk)+"' and");
					map.remove(tmpPk);
				}else{
					throw new Exception("请求数据Map中找不到:"+tmpPk);
				}
			}
			for(String key :map.keySet()){
				set.append(" "+key+"='"+map.get(key)+"',");
			}
			sb.append(set.substring(0, set.length()-1));
			sb.append(" where ");
			sb.append(pk.substring(0, pk.length()-3));
		}else{
			throw new Exception("请输入主键条件或表明");
		}
		daoBase.info(sb.toString());
		return sb.toString();
	}
	public static String getAddSql(Map<String,String> map,String tableName) throws Exception{
		StringBuffer sb = new StringBuffer();
		StringBuffer key = new StringBuffer();
		StringBuffer value = new StringBuffer();
		sb.append("insert into ");
		sb.append(tableName+" ");
		if(map!= null && !map.isEmpty() && tableName !=null && !"".equals(tableName)){
			for(String tmp : map.keySet()){
				key.append(tmp+",");
				value.append("'"+map.get(tmp)+"',");
			}
			sb.append("(");
			sb.append(key.substring(0, key.length()-1));
			sb.append(") values(");
			sb.append(value.substring(0, value.length()-1)+")");
		}else{
			throw new Exception("请求数据或表名为空");
		}
		daoBase.info(sb.toString());
		return sb.toString();
	}
}
