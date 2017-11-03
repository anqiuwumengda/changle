package com.http;

import net.sf.json.JSONObject;

public class HnData {

	public JSONObject getCustZr(String idno, String name) throws Exception {
		// DbAccess db =new DbAccess();
		JSONObject jsob = null;// 返回json
		StringBuffer rsb = new StringBuffer();
		HttpRequest hr = new HttpRequest();
		rsb.append(hr.sendGet("http://67.64.1.82:55555/InBankWebService/getData/admit", "CARDNUM=" + idno + "&CNAME="
				+ name));
	/*	String resultJson = HttpRequest.sendGet("http://67.64.1.82:55555/InBankWebService/getData/admit", "CARDNUM=" + idno + "&CNAME="
				+ name);*/
		jsob = JSONObject.fromObject(rsb.toString());
		SendMsgCustGrap smcg = new SendMsgCustGrap();
		smcg.saveZrMsg(jsob,idno,name);
		//String resSuccess = jsob.getString("CODE");
		if("00000".equals(jsob.getString("CODE"))){
			return jsob;
		}else{
			return null;
		}
		
	}
	public JSONObject getCustPj(String idno, String name) throws Exception {
		// DbAccess db =new DbAccess();
		JSONObject jsob = null;// 返回json
		StringBuffer rsb = new StringBuffer();
		HttpRequest hr = new HttpRequest();
		rsb.append(hr.sendGet("http://67.64.1.82:55555/InBankWebService/getData/rate", "CARDNUM=" + idno + "&CNAME="
				+ name));
		/*String resultJson = HttpRequest.sendGet("http://67.64.1.82:55555/InBankWebService/getData/rate", "CARDNUM=" + idno + "&CNAME="
				+ name);*/
		jsob = JSONObject.fromObject(rsb.toString());
		SendMsgCustGrap smcg = new SendMsgCustGrap();
		smcg.savePjMsg(jsob,idno,name);
		//String resSuccess = jsob.getString("CODE");
		if("00000".equals(jsob.getString("CODE"))){
			return jsob;
		}else{
			return null;
		}
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		HnData hd = new HnData();
		hd.getCustZr("370725196301130973", "刘万光");
	}

}
