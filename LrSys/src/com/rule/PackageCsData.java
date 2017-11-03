package com.rule;

import hlc.base.db.DbAccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sdnx.st.dp.StCalculator;
import com.sdnx.st.dp.StCalculatorInterface;
import com.sdnx.st.dp.model.RuleRequestObject;
import com.sdnx.st.dp.model.RuleResponseObject;
import com.sdnx.st.dp.model.RuleRequestObject.InterInfo;
import com.tgb.util.DateTools;
/**
 * 准入
 * @author javacai
 *
 */
public class PackageCsData {
	private Map<String,Object> sendMapMsg;
	private String NPSCORE;
	private String CSEQLMT;
	private String RRMODEL;
	Logger log = Logger.getLogger(PackageCsData.class);
	/**
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> getData(Map<String,String> paraMap) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ID",DateTools.getCurrentSysData("yyyyMMdd")+paraMap.get("CUST_ID")+CommonUtil.getSeq());//受理编号
		map.put("DEPTCODE",paraMap.get("CUST_ORG"));//申请机构代码				网点代码
		map.put("INSTCITYCODE","02000A");//地市机构编号			02000A固定
		map.put("FRCODE","90706");//法人机构编号根据DEPTCODE获取法人机构编号	固定:90706
		map.put("OPERATOR",paraMap.get("USER_ID"));//当前操作员登录用户ID
		map.put("CURRENTDATE",DateTools.getCurrentSysData("yyyy-MM-dd"));//会计日期SELECTVALUEFROMPMSYSPARAMwherecode='0001'
		map.put("CUSTID",paraMap.get("CUST_ID"));//客户编号		乐融贷系统客户编号
		map.put("CNAME",paraMap.get("CUST_NAME"));//客户名称		
		map.put("IFNEWCUST","@EMPTY@");//是否新增客户			客户本次申请授信距离上次客户结清贷款日期不超过一年
		map.put("NPSCORE",getNPSCORE());//评级结果		评级结果
		map.put("CSEQLMT",getCSEQLMT());//初审额度			评级结果
		map.put("CHECKOBJ","1");//检查对象此处默认'1'即可
		map.put("CALLSTEP","5");//规则调取阶段5
		map.put("RRMODEL",getRRMODEL());//评级模型类型打分卡类型
		map.put("KEYTYPE","@EMPTY@");//押品类型多种押品，用逗号分隔空置：@EMPTY@
		map.put("INDUSTRY","@EMPTY@");//所属行业空置：@EMPTY@
		map.put("MAINASSURE","C101");//担保方式		C101	保证
		
		return map;
	}
	public RuleResponseObject cs(String cust_id,String user_id,String org_id) throws Exception{
		DbAccess db = new DbAccess();
		String sql = "select CUST_ID,CUST_NAME from cust_base where cust_id='"+cust_id+"'";
		Map<String, String> custMap = db.queryForMap(sql);
		RuleResponseObject rr=null;
		StCalculatorInterface sc = StCalculator.getInstance();
		//for (Map<String, String> custMap : custList) {
			custMap.put("CUST_ORG", org_id);
			custMap.put("USER_ID", user_id);
			Map<String, Object> paraMap = getData(custMap);
			setSendMapMsg(paraMap);
			RuleRequestObject rro = new RuleRequestObject();
			rro.setCallStep("5");
			InterInfo ii = new InterInfo();
			ii.setInterCode("ZD00007");
			rro.setInterInfo(ii);
			for(String key :paraMap.keySet()){
				rro.put(key, paraMap.get(key));
			}
			//log.info("请求参数:"+paraMap.toString());
			rr= sc.calculateRule(rro);
			log.info("返回编码:"+rr.getCode()+";DESC:"+rr.getDesc());
			custMap.clear();
			paraMap.clear();
			custMap=null;
			paraMap=null;
		//}
			return rr;
	}
	
	public Map<String, Object> getSendMapMsg() {
		return sendMapMsg;
	}
	public void setSendMapMsg(Map<String, Object> sendMapMsg) {
		this.sendMapMsg = sendMapMsg;
	}
	
	public String getNPSCORE() {
		return NPSCORE;
	}
	public void setNPSCORE(String nPSCORE) {
		NPSCORE = nPSCORE;
	}
	public String getCSEQLMT() {
		return CSEQLMT;
	}
	public void setCSEQLMT(String cSEQLMT) {
		CSEQLMT = cSEQLMT;
	}
	public String getRRMODEL() {
		return RRMODEL;
	}
	public void setRRMODEL(String rRMODEL) {
		RRMODEL = rRMODEL;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
