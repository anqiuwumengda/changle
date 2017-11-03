package com.rule;

import hlc.base.db.DbAccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.http.HnData;
import com.sdnx.st.dp.StCalculator;
import com.sdnx.st.dp.StCalculatorInterface;
import com.sdnx.st.dp.model.RuleRequestObject;
import com.sdnx.st.dp.model.RuleResponseObject;
import com.sdnx.st.dp.model.RuleRequestObject.InterInfo;
import com.tgb.controller.propertymanage.LrdCallRuleController;
import com.tgb.util.DateTools;
/**
 * 准入
 * @author javacai
 *
 */
public class PackageZr1Data {
	private Map<String,Object> sendMapMsg;
	Logger log = Logger.getLogger(PackageZr1Data.class);
	/**
	 * 
	 * @param paraMap    客户编号  客户经理所在网点      当前操作员  身份证号   姓名    
	 * @return
	 * @throws Exception 
	 */
	private  Map<String,Object> getData(Map<String,String> paraMap) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ID", DateTools.getCurrentSysData("yyyyMMdd")+paraMap.get("CUST_ID")+CommonUtil.getSeq());//受理编号 日期+客户编号+4位序列
		map.put("DEPTCODE", paraMap.get("CUST_ORG"));//申请机构代码 网点代码 客户经理所在网点
		map.put("INSTCITYCODE", "02000A");//地市机构编号 907
		map.put("FRCODE", "90706");//法人机构编码
		map.put("CURRENTDATE", DateTools.getCurrentSysData("yyyy-MM-dd"));//会计日期	当前日期
		map.put("CALLSTEP", "1");//规则调取阶段   1
		map.put("CHECKOBJ", "1");//检查对象
		map.put("CUSTID", paraMap.get("CUST_ID"));//客户编号
		map.put("CNAME", paraMap.get("CUST_NAME"));//客户名称
		map.put("OPERATOR", paraMap.get("USER_ID"));//操作员编码
		map.put("APPBUSITYPE", "@EMPTY@");//贷款产品 空置 ： @EMPTY@
		map.put("REQLMT", "@EMPTY@");//申请额度 @EMPTY@
		map.put("IFNEWCUST", "@EMPTY@");//是否新增客户
		map.put("LIMT", "@EMPTY@");//申请期限
		map.put("AGE", paraMap.get("USER_AGE"));//年龄
		String jh_flag = paraMap.get("JH_FLAG");
		//婚姻状况     婚姻状况 乐融贷 0-未婚；1-已婚；2-丧偶；3-离婚
		if("1".equals(jh_flag)){
			map.put("MARRIGE", "0");
		}else if("2".equals(jh_flag)){
			map.put("MARRIGE", "2");
		}else if("3".equals(jh_flag)){
			map.put("MARRIGE", "3");
		}else if("4".equals(jh_flag)){
			map.put("MARRIGE", "2");
		}
		map.put("FAMASSET", paraMap.get("ZZC"));//家庭总资产
		map.put("FAMDEBT", paraMap.get("ZFZ"));//家庭总负债
		map.put("UPBUSITYPE", "@EMPTY@");//上层业务编号
		map.put("SEX", paraMap.get("SEX")=="01"?"1":"2");//性别
		
		return map;
	}
	
	public RuleResponseObject zr1(String cust_id,String user_id,String org_id) throws Exception{
		DbAccess db = new DbAccess();
		String sql = "select cb.CUST_ID,cb.TEL_NO,cb.CUST_NAME,cb.SEX,cb.ID_NO,cb.USER_AGE,cb.JH_FLAG,IFNULL(fr.SR_1Y,0) SR_1Y," +
				"IFNULL(fr.ZC_1Y,0) ZC_1Y,IFNULL(fr.ZZC,0) ZZC,IFNULL(fr.ZFZ,0) ZFZ " +
				"from cust_base cb left join fin_rpt fr on cb.cust_id=fr.CUST_ID where cb.cust_id='"+cust_id+"'";
		Map<String, String> custMap  = db.queryForMap(sql);
		RuleResponseObject rr=null;
		StCalculatorInterface sc = StCalculator.getInstance();
		//for (Map<String, String> custMap : custList) {
			custMap.put("CUST_ORG", org_id);
			custMap.put("USER_ID", user_id);
			Map<String, Object> paraMap = getData(custMap);
			setSendMapMsg(paraMap);
			RuleRequestObject rro = new RuleRequestObject();
			rro.setCallStep("1");
			InterInfo ii = new InterInfo();
			ii.setInterCode("ZD00002");
			rro.setInterInfo(ii);
			for(String key :paraMap.keySet()){
				rro.put(key, paraMap.get(key));
			}
			//log.info("请求参数:"+paraMap.toString());
			rr= sc.calculateRule(rro);
			log.info("返回编码:"+rr.getCode()+";返回结果:"+rr.getResult()+";DESC:"+rr.getDesc());
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

	/**
	 * @param args  客户编号  客户经理所在网点      当前操作员  身份证号   姓名     
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		PackageZr1Data pzr1 = new PackageZr1Data();
		DbAccess db = new DbAccess();
		List<Map<String,String>> list = db.queryForList("select cust_id from cust_base");
		for(Map<String,String> map :list){
			pzr1.zr1(map.get("cust_id"), "admin", "020008");
		}
	}

}
