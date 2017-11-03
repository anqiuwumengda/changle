package hlc.util;

import hlc.base.db.DbAccess;
import hlc.util.PageTool;

public class DB2PageTool implements PageTool {

	public String getPageSql(String sql, int start, int limit) throws Exception{
		int startnum = (start-1)*limit+1;
		int rownum = start*limit;
		String tsql="";
		DbAccess db = null;
		try {
			db = new DbAccess();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(db.isOracle());
		if(db.isOracle()){
			tsql = "select * from(select t.*,rownum rn from("+sql+") t where rownum <= "+rownum+") where rn >= "+startnum;
		}else if(db.isDB2()){
			return "select * from (select abc.*,rownumber() over() as rn from( "
					+ sql + " ) abc ) page_table where rn between " + startnum + " and " + rownum;
		}else if (db.isMySQL()){ 
			tsql = "select mysqlnum.* from ("+sql+")mysqlnum  limit "+(startnum-1)+","+rownum+"";
		} else {
			throw new Exception("系统不支持该数据库");
		}
		System.out.println(tsql);
		return tsql;
	}

}
