package com.http;

import hlc.base.db.DbAccess;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class SendMsgCustGrap {

	//private static final String CUSTGRAPURL = "http://ss0.lawxp.com/n1/Search.aspx";
	public static Logger sendMsgSf = Logger.getLogger(SendMsgCustGrap.class);

	// private static final String[] keyArr = new
	// String[]{"success","message","totalnumber","pg","pz","startime","endtime","allmsglist"};

	public   void savePjMsg(JSONObject jsob, String id_no, String name){
		try{
		DbAccess db = new DbAccess();
		String flag = jsob.getString("CODE");// 返回成功的标记信息

		if ("00000".equals(flag)) {// 00000成功
			String delSql = "delete from hn_cust_pj where CARDNUM='" + id_no
					+ "'";
			db.executeUpdate(delSql);
			String sql = "insert into hn_cust_pj (CNAME,CARDNUM,CARDTYPE,BHMINRATEOVERDUETDATE,"
					+ "BHMINOVERDUETDATE,BHSIXMOVERDUENUM,BHONEYOVERDUENUM,BHTWOYOVERDUENUM,"
					+ "BHTHREEYOVERDUENUM,BHSIXMOVERDUETERM,BHTWOYOVERDUETERM,ONEYSDAILY,SIXMSDAILY,"
					+ "NINEMDAILY,SIXMDAILY,ONEYDAILY,ONEYSPOSNUM,ONEYPOSNUM,APPOPENNUM,APPOTHEROPENNUM,"
					+ "FIRSTLOANDAY,BHONEYRATEOVERDUETERM,DEPOSITINFOFLAG,CREDITCARDFLAG,DLAYFLAG,IFFREEZE) values"
					+ "('"
					+ name
					+ "',"
					+ "'"
					+ id_no
					+ "',"
					+ "'身份证',"
					+ "'"
					+ jsob.getString("BHMINRATEOVERDUETDATE")
					+ "',"
					+ "'"
					+ jsob.getString("BHMINOVERDUETDATE")
					+ "',"
					+ "'"
					+ jsob.getString("BHONEYOVERDUENUM")
					+ "',"
					+ "'"
					+ jsob.getString("BHONEYOVERDUENUM")
					+ "',"
					+ "'"
					+ jsob.getString("BHTWOYOVERDUENUM")
					+ "',"
					+ "'"
					+ jsob.getString("BHTHREEYOVERDUENUM")
					+ "',"
					+ "'"
					+ jsob.getString("BHSIXMOVERDUETERM")
					+ "',"
					+ "'"
					+ jsob.getString("BHTWOYOVERDUETERM")
					+ "',"
					+ "'"
					+ jsob.getString("ONEYSDAILY")
					+ "',"
					+ "'"
					+ jsob.getString("SIXMSDAILY")
					+ "',"
					+ "'"
					+ jsob.getString("NINEMDAILY")
					+ "',"
					+ "'"
					+ jsob.getString("SIXMDAILY")
					+ "',"
					+ "'"
					+ jsob.getString("ONEYDAILY")
					+ "',"
					+ "'"
					+ jsob.getString("ONEYSPOSNUM")
					+ "',"
					+ "'"
					+ jsob.getString("ONEYPOSNUM")
					+ "',"
					+ "'"
					+ jsob.getString("APPOPENNUM")
					+ "',"
					+ "'"
					+ jsob.getString("APPOTHEROPENNUM")
					+ "',"
					+ "'"
					+ jsob.getString("FIRSTLOANDAY")
					+ "',"
					+ "'"
					+ jsob.getString("BHONEYRATEOVERDUETERM")
					+ "',"
					+ "'"
					+ jsob.getString("DEPOSITINFOFLAG")
					+ "',"
					+ "'"
					+ jsob.getString("CREDITCARDFLAG")
					+ "','"
					+ jsob.getString("DLAYFLAG")
					+ "',"
					+ "'"
					+ jsob.getString("IFFREEZE") + "')";
			sendMsgSf.info(sql);
			db.executeUpdate(sql);
		}
		}catch(Exception e){
			e.printStackTrace();
			sendMsgSf.error(e.getMessage());
		}
	}

	public  void saveZrMsg(JSONObject jsob, String id_no, String name)
			 {
		try{
		DbAccess db = new DbAccess();
		String flag = jsob.getString("CODE");// 返回成功的标记信息

		if ("00000".equals(flag)) {// 00000成功
			String delSql = "delete from hn_cust_zr where CARDNUM='" + id_no
					+ "'";
			db.executeUpdate(delSql);
			String CUSTLIST =jsob.get("CUSTLIST") == null ? "" : jsob.get("CUSTLIST")
					.toString().replace("'", "");
			String BNLIST =jsob.get("BNLIST") == null ? "" : jsob
					.get("BNLIST").toString().replace("'", "");
			String BWLIST=jsob.get("BWLIST") == null ? "" : jsob
					.get("BWLIST").toString().replace("'", "");
			String EXPLIST=jsob.get("EXPLIST") == null ? "" : jsob
					.get("EXPLIST").toString().replace("'", "");
			String BNWLIST=jsob.get("BNWLIST") == null ? "" : jsob
					.get("BNWLIST").toString().replace("'", "");
			String sql = "insert into hn_cust_zr (CARDNUM,CNAME,RESCODE,RESMSG,CUSTLIST,BNLIST,BWLIST,EXPLIST,BNWLIST) "
					+ "values('"
					+ id_no
					+ "','"
					+ name
					+ "','"
					+ jsob.get("RESCODE")
					+ "','"
					+ jsob.get("RESMSG")
					+ "','"
					+ CUSTLIST
					+ "','" + BNLIST
					+ "','" + BWLIST
					+ "','" + EXPLIST
					+ "','" + BNWLIST
					+ "') ";
			sendMsgSf.info(sql);
			db.executeUpdate(sql);
		}
		}catch(Exception e){
			e.printStackTrace();
			sendMsgSf.error(e.getMessage());
		}
	}

	/**
	 * a 请求入口
	 * 
	 * @param n
	 * @param id
	 * @param stype
	 * @throws Exception
	 */
	/*public boolean sendMsg(String string) throws Exception {
		DbAccess db = new DbAccess();
		JSONObject jsob = null;// 返回json
		String resultJson = HttpRequest.sendGet(CUSTGRAPURL, string);// 发送请求获取Json
		jsob = JSONObject.fromObject(resultJson);
		String flag = jsob.getString("code");// 返回成功的标记信息
		// Iterator keys = jsob.keys();
		sendMsgSf.info("请求url：" + CUSTGRAPURL + "?" + string);
		sendMsgSf.info("返回数据：" + resultJson);
		if ("00000".equals(flag)) {// 00000成功
			db.executeUpdate("delete from hfw_sf_base where CARDNUM='"
					+ jsob.getString("CARDNUM") + "'");
			String sql = "insert into hfw_sf_base (CNAME,CARDNUM,CARDTYPE,BHMINRATEOVERDUETDATE,"
					+ "BHMINOVERDUETDATE,BHSIXMOVERDUENUM,BHONEYOVERDUENUM,BHTWOYOVERDUENUM,"
					+ "BHTHREEYOVERDUENUM,BHSIXMOVERDUETERM,BHTWOYOVERDUETERM,ONEYSDAILY,SIXMSDAILY,"
					+ "NINEMDAILY,SIXMDAILY,ONEYDAILY,ONEYSPOSNUM,ONEYPOSNUM,APPOPENNUM,APPOTHEROPENNUM,"
					+ "FIRSTLOANDAY,BHONEYRATEOVERDUETERM,DEPOSITINFOFLAG,CREDITCARDFLAG,DLAYFLAG,IFFREEZE) values"
					+ "('"
					+ jsob.getString("CNAME")
					+ "',"
					+ "'"
					+ jsob.getString("CARDNUM")
					+ "',"
					+ "'"
					+ jsob.getString("CARDTYPE")
					+ "',"
					+ "'"
					+ jsob.getString("BHMINRATEOVERDUETDATE")
					+ "',"
					+ "'"
					+ jsob.getString("BHMINOVERDUETDATE")
					+ "',"
					+ "'"
					+ jsob.getString("BHONEYOVERDUENUM")
					+ "',"
					+ "'"
					+ jsob.getString("BHONEYOVERDUENUM")
					+ "',"
					+ "'"
					+ jsob.getString("BHTWOYOVERDUENUM")
					+ "',"
					+ "'"
					+ jsob.getString("BHTHREEYOVERDUENUM")
					+ "',"
					+ "'"
					+ jsob.getString("BHSIXMOVERDUETERM")
					+ "',"
					+ "'"
					+ jsob.getString("BHTWOYOVERDUETERM")
					+ "',"
					+ "'"
					+ jsob.getString("ONEYSDAILY")
					+ "',"
					+ "'"
					+ jsob.getString("SIXMSDAILY")
					+ "',"
					+ "'"
					+ jsob.getString("NINEMDAILY")
					+ "',"
					+ "'"
					+ jsob.getString("SIXMDAILY")
					+ "',"
					+ "'"
					+ jsob.getString("ONEYDAILY")
					+ "',"
					+ "'"
					+ jsob.getString("ONEYSPOSNUM")
					+ "',"
					+ "'"
					+ jsob.getString("ONEYPOSNUM")
					+ "',"
					+ "'"
					+ jsob.getString("APP;OPENNUM")
					+ "',"
					+ "'"
					+ jsob.getString("APPOTHEROPENNUM")
					+ "',"
					+ "'"
					+ jsob.getString("FIRSTLOANDAY")
					+ "',"
					+ "'"
					+ jsob.getString("BHONEYRATEOVERDUETERM")
					+ "',"
					+ "'"
					+ jsob.getString("DEPOSITINFOFLAG")
					+ "',"
					+ "'"
					+ jsob.getString("CREDITCARDFLAG")
					+ "',"
					+ "'"
					+ jsob.getString("IFFREEZE") + "')";
			sendMsgSf.info(sql);
			db.executeUpdate(sql);
			return true;
		}
		if (flag.startsWith("e")) {
			sendMsgSf.info("查询返回状态: e;" + jsob.toString());
			new Exception(jsob.toString());

		}

		return false;
	}*/

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

	}
}
