package com.rule;

import hlc.base.db.DbAccess;

import java.util.Map;

import com.tgb.util.DateTools;

public class CommonUtil {
	public static synchronized String getSeq() throws Exception{
		String sql = "select CORP_CD,CHPARAKEY,MTN_DATE,CHPARAVALUE from lrd_syspara where CHPARAKEY='SEQNO'";
		DbAccess db = new DbAccess();
		String currData= DateTools.getCurrentSysData("yyyyMMdd");
		Map<String,String> map= db.queryForMap(sql);
		String mtn_date = map.get("MTN_DATE");
		String corp_cd=  map.get("CORP_CD"); 
		String key = map.get("CHPARAKEY");
		int seq= Integer.parseInt(map.get("CHPARAVALUE"));
		if(currData.equals(mtn_date)){
			seq++;
			String sql2 = "update lrd_syspara set CHPARAVALUE='"+seq+"' where CORP_CD='"+corp_cd+"' and CHPARAKEY='"+key+"'";
			db.executeUpdate(sql2);
		}else{
			seq=1;
			String sql2 = "update lrd_syspara set CHPARAVALUE='"+seq+"',MTN_DATE='"+currData+"' where CORP_CD='"+corp_cd+"' and CHPARAKEY='"+key+"'";
			db.executeUpdate(sql2);
		}
		
		return String.format("%5d", seq).replace(" ", "0");
	}
	public static void main (String[] args){
		
	}
}
