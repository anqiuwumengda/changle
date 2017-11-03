package com.rule;

import hlc.base.db.DbAccess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.sdnx.st.dp.model.RuleResponseObject;
import com.tgb.util.DateTools;

public class DataInDb {
	DbAccess db;

	public DbAccess getDbAccess() throws Exception {
		if (null == db) {
			db = new DbAccess();
		}
		return db;
	}
	public String getDate(String date) throws ParseException{
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date date1  = formatter.parse(date);
		date = formatter2.format(date1);
		return date;
	}
	public void delete(String id_no) throws Exception {
		/*String sql = "delete from scsp where id_no='" + id_no + "' and DATE='"
				+ DateTools.getCurrentSysData("yyyy-MM-dd") + "'";*/
		String sql = "delete from scsp where id_no='" + id_no + "'";
		getDbAccess().executeUpdate(sql);
	}

	public void insert(Map<String, String> map,String date) throws Exception {
		
		String sql = "insert into scsp (ID_NO,ORG_NAME,ZYXM,CUST_ID,CUST_GRP_GL,CUST_GRP_GL_NAME,DATE,LOSE_DATE,STAT,OPERID) "
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
		String sql = "update scsp set ZR1_SENDMSG='" + sendMsg
				+ "',ZR1_RESULT='" + rro.getResult() + "',ZR1_CODE='"
				+ rro.getCode() + "',ZR1_DESC='" + rro.getDesc()
				+ "' where ID_NO='" + id_no + "' and DATE='" + getDate(date) + "'";
		getDbAccess().executeUpdate(sql);
	}

	public void updateZr2(RuleResponseObject rro, String sendMsg, String id_no,
			String date) throws Exception {
		String sql = "update scsp set ZR2_SENDMSG='" + sendMsg
				+ "',ZR2_RESULT='" + rro.getResult() + "',ZR2_CODE='"
				+ rro.getCode() + "',ZR2_DESC='" + rro.getDesc()
				+ "' where ID_NO='" + id_no + "' and DATE='" + getDate(date) + "'";
		getDbAccess().executeUpdate(sql);
	}

	public void updatePfk(RuleResponseObject rro, String sendMsg, String id_no,
			String date) throws Exception {
		String sql = "update scsp set PJ_SENDMSG='" + sendMsg + "',PJ_JB='"
				+ rro.getCseqLmt() + "',PJ_MODEL_TYPE='" + rro.getRrModel()
				+ "',PJ_LMT='" + rro.getRateHighestaMount() + "',PJ_CODE='"
				+ rro.getCode() + "',PJ_DESC='" + rro.getDesc()
				+ "' where ID_NO='" + id_no + "' and DATE='" + getDate(date) + "'";
		getDbAccess().executeUpdate(sql);
	}

	public void updateEd(RuleResponseObject rro, String sendMsg, String id_no,
			String date) throws Exception {
		String sql = "update scsp set ED_SENDMSG='" + sendMsg + "',ED_LMT='"
				+ rro.getCseqLmt() + "',ED_CODE='" + rro.getCode()
				+ "',ED_DESC='" + rro.getDesc() + "' where ID_NO='" + id_no
				+ "' and DATE='" + getDate(date) + "'";
		getDbAccess().executeUpdate(sql);
	}

	public void updateCs(RuleResponseObject rro, String sendMsg, String id_no,
			String date) throws Exception {
		String sql = "update scsp set CS_SENDMSG='" + sendMsg + "',CS_RESULT='"
				+ rro.getResult() + "',CS_CODE='" + rro.getCode()
				+ "',CS_DESC='" + rro.getDesc() + "' where ID_NO='" + id_no
				+ "' and DATE='" + getDate(date) + "'";
		getDbAccess().executeUpdate(sql);
	}

	public void updateEdLv(RuleResponseObject rro, String sendMsg,
			String id_no, String date) throws Exception {
		String sql = "update scsp set CREALIMIT='1',CREATERATE='1',QQLIMIT='1',QQRATE='1' where ID_NO='"
				+ id_no + "' and DATE='" + getDate(date) + "'";
		getDbAccess().executeUpdate(sql);
	}

	public void updEnd(Double jb, String cust_id, String id_no, Double lmt,
			String date) throws Exception {
		if(jb<=7){
			String sql1 = "select CUST_TYPE,VOCATION from cust_base where cust_id='"
				+ cust_id + "'";
		String sql2 = "select * from ed_end where pj='" + jb.intValue() + "'";
		Map<String, String> map1 = getDbAccess().queryForMap(sql1);
		Map<String, String> map2 = getDbAccess().queryForMap(sql2);
		String cust_type = map1.get("CUST_TYPE");
		String vocation = map1.get("VOCATION");
		String xylmt = "";
		String xyrate = "";
		String jtlmt = "";
		String jtrate = "";

		if ("01,02,03,04,05,06".contains(vocation)) {// 事业
			int ckXyLmt = Integer.parseInt(map2.get("ED_XY_QT"));
			int ckJtLmt = Integer.parseInt(map2.get("ED_JT_QT"));
			xyrate = map2.get("LV_XY_QT");
			jtrate = map2.get("LV_JT_QT");
			if (lmt/10000 >= ckXyLmt) {
				xylmt = String.valueOf(ckXyLmt);
			} else {
				xylmt = String.valueOf(lmt/10000);
			}
			if (lmt/10000 >= ckJtLmt) {
				jtlmt = String.valueOf(ckJtLmt);
			} else {
				jtlmt = String.valueOf(lmt/10000);
			}

		} else if ("12".equals(vocation)) {// 个体
			int ckXyLmt = Integer.parseInt(map2.get("ED_XY_GT"));
			int ckJtLmt = Integer.parseInt(map2.get("ED_JT_GT"));
			xyrate = map2.get("LV_XY_GT");
			jtrate = map2.get("LV_JT_GT");
			if (lmt/10000 >= ckXyLmt) {
				xylmt = String.valueOf(ckXyLmt);
			} else {
				xylmt = String.valueOf(lmt/10000);
			}
			if (lmt/10000 >= ckJtLmt) {
				jtlmt = String.valueOf(ckJtLmt);
			} else {
				jtlmt = String.valueOf(lmt/10000);
			}
		} else {// 农户
			int ckXyLmt = Integer.parseInt(map2.get("ED_XY_NH"));
			int ckJtLmt = Integer.parseInt(map2.get("ED_JT_NH"));
			xyrate = map2.get("LV_XY_NH");
			jtrate = map2.get("LV_JT_NH");
			if (lmt/10000 >= ckXyLmt) {
				xylmt = String.valueOf(ckXyLmt);
			} else {
				xylmt = String.valueOf(lmt/10000);
			}
			if (lmt/10000 >= ckJtLmt) {
				jtlmt = String.valueOf(ckJtLmt);
			} else {
				jtlmt = String.valueOf(lmt/10000);
			}
		}

		String sql = "update scsp set CREALIMIT='" + xylmt + "',CREATERATE='"
				+ xyrate + "',QQLIMIT='" + jtlmt + "',QQRATE='" + jtrate
				+ "' where id_no='" + id_no + "' and date='" + getDate(date) + "'";
		getDbAccess().executeUpdate(sql);
		}
		
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
