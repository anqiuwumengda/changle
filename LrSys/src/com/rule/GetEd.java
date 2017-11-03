package com.rule;

import hlc.base.db.DbAccess;

import java.util.Map;

public class GetEd {
	
	public static String getEd(String cust_id) throws Exception{
		String ed="";
		DbAccess db = new DbAccess();
		Map<String,String> map = db.queryForMap("select XQED from CUST_FEEDBACK where cust_id='"+cust_id+"'");
		String tmp = map.get("XQED");
		if(null!=tmp && !"".equals(tmp) && Double.parseDouble(tmp)>0){
			ed=String.valueOf(Double.parseDouble(tmp)*10000);
			return ed;
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(GetEd.getEd("20170117161300718583"));
	}

}
 