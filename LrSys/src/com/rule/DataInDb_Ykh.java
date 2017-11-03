package com.rule;

import hlc.base.db.DbAccess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.sdnx.st.dp.model.RuleResponseObject;
import com.tgb.util.DateTools;

public class DataInDb_Ykh {
	DbAccess db;

	public DbAccess getDbAccess() throws Exception {
		if (null == db) {
			db = new DbAccess();
		}
		return db;
	}
	public String getDate(String date) throws ParseException{
		date= DateTools.getCurrentSysData("yyyy-MM-dd");
		return date;
	}
	public void delete(String id_no) throws Exception {
		/*String sql = "delete from scsp where id_no='" + id_no + "' and DATE='"
				+ DateTools.getCurrentSysData("yyyy-MM-dd") + "'";*/
		String sql = "delete from scsp_ykh where id_no='" + id_no + "'";
		getDbAccess().executeUpdate(sql);
	}

	public void insert(Map<String, String> map,String date) throws Exception {
		
		String sql = "insert into scsp_ykh (ID_NO,ORG_NAME,ZYXM,CUST_ID,CUST_GRP_GL,CUST_GRP_GL_NAME,DATE,LOSE_DATE,STAT,OPERID) "
				+ "values('"
				+ map.get("ID_NO")
				+ "','"
				+ map.get("ORG_NAME")
				+ "','"
				+ map.get("ZYXM")
				+ "','"
				+ map.get("CUST_ID")
				+ "',"
				+ "'"
				+ map.get("CUST_GRP_GL")
				+ "','"
				+ map.get("CUST_GRP_GL_NAME")
				+ "','"
				+ getDate(date)
				+ "','"
				+ DateTools.getCurrentSysMonth(3, "yyyy-MM-dd")
				+ "',"
				+ "'1','" + map.get("CUST_GRP_GL") + "');";
		getDbAccess().executeUpdate(sql);
	}
	
	public void updateZr1(RuleResponseObject rro, String sendMsg, String id_no,
			String date) throws Exception {
		String sql = "update scsp_ykh set ZR1_SENDMSG='" + sendMsg
				+ "',ZR1_RESULT='" + rro.getResult() + "',ZR1_CODE='"
				+ rro.getCode() + "',ZR1_DESC='" + rro.getDesc()
				+ "' where ID_NO='" + id_no + "' and DATE='" + getDate(date) + "'";
		getDbAccess().executeUpdate(sql);
	}

	public void updateZr2(RuleResponseObject rro, String sendMsg, String id_no,
			String date) throws Exception {
		String sql = "update scsp_ykh set ZR2_SENDMSG='" + sendMsg
				+ "',ZR2_RESULT='" + rro.getResult() + "',ZR2_CODE='"
				+ rro.getCode() + "',ZR2_DESC='" + rro.getDesc()
				+ "' where ID_NO='" + id_no + "' and DATE='" + getDate(date) + "'";
		getDbAccess().executeUpdate(sql);
	}



	
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		 SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date date  = formatter.parse("20170104");
		System.out.println(formatter2.format(date));
		
		 
	}

}
