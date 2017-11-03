package com.rule;

import hlc.base.db.DbAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sdnx.st.constants.interfaceConstants.LMT001;
import com.sdnx.st.constants.interfaceConstants.ZD00007;
import com.sdnx.st.dp.StCalculator;
import com.sdnx.st.dp.StCalculatorInterface;
import com.sdnx.st.dp.model.RuleRequestObject;
import com.sdnx.st.dp.model.RuleResponseObject;
import com.sdnx.st.dp.model.RuleRequestObject.InterInfo;
import com.tgb.util.DateTools;

/**
 * 额度测算
 * 
 * @author javacai
 * 
 */
public class PackageEdcsData {
	private Map<String,Object> sendMapMsg;
	private String NPSCORE;
	
	Logger log = Logger.getLogger(PackageEdcsData.class);
	/**
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getData(Map<String, String> paraMap)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID",
				DateTools.getCurrentSysData("yyyyMMdd")
						+ paraMap.get("CUST_ID") + CommonUtil.getSeq());// 受理编号// 日期+客户编号+4位序列
		//map.put("DEPTCODE","@EMPTY@");													
		map.put("DEPTCODE", paraMap.get("CUST_ORG"));// 申请机构代码 客户经理所在网点??????
		map.put("INSTCITYCODE", "02000A");// 地市编号 907  ---02000A
		map.put("APPBUSITYPE", "default");// 贷款产品 @EMPTY@
		map.put("FRCODE", "90706");// 法人机构编号 90706
		map.put("OPERATOR", paraMap.get("USER_ID"));// 当前操作员 登录用户ID:乐融贷操作员
		map.put("CURRENTDATE", DateTools.getCurrentSysData("yyyy-MM-dd"));// 会计日期
																			// 当前日期
		map.put("CALLSTEP", "2");// 规则调取阶段 2
		map.put("REQLMT", "2147483647");// 申请额度 @EMPTY@ 
		map.put("TOTALCAPITALREQ", "2147483647");// 总资金需求额度 @EMPTY@
		map.put("SELFCAPITALRATE", "@EMPTY@");// 自有资金投入 @EMPTY@
		
		String sr_1y = paraMap.get("SR_1Y");
		if(null!=sr_1y && !"".equals(sr_1y))  
			map.put("FAMANNUINCOME", Double.parseDouble(sr_1y)*10000);// 家庭年收入1年前 乐融贷
		else
			map.put("FAMANNUINCOME","0");
		String zc_1y = paraMap.get("ZC_1Y");
		if(null!=zc_1y && !"".equals(zc_1y))  
			map.put("FAMANNUINOUT", Double.parseDouble(zc_1y)*10000);// 家庭年支出1年前 乐融贷
		else
			map.put("FAMANNUINOUT","0");
		
		String ZZC = paraMap.get("ZZC");
		if(null!=ZZC && !"".equals(ZZC))
			map.put("FAMASSET", Double.parseDouble(ZZC)*10000);// 家庭总资产 乐融贷
		else
			map.put("FAMASSET", "0");
		
		String ZFZ = paraMap.get("ZFZ");
		if(null!=ZFZ && !"".equals(ZFZ))
			map.put("FAMDEBT", Double.parseDouble(ZFZ)*10000);// 家庭总负债 乐融贷
		else
			map.put("FAMDEBT", "0");
		
		
		map.put("CPREQLMT", "@EMPTY@");// 初评小组推荐额度（理事会推荐额度、担保公司额度） @EMPTY@
		map.put("LIMIT", "36");// 贷款期限 ?
		map.put("GUARLIST", "@EMPTY@");// 担保人净资产 @EMPTY@
		map.put("USEUAMT", "0");// 有效授信余额 ?
		map.put("MONEY", "0");// 抵质押金额（合计） ?
		map.put("UNIONLIST", "@EMPTY@");// 联合组织其他成员家庭净资产 @EMPTY@
		map.put("IFUSEDHOUSE", "@EMPTY@");// 是否二手房 @EMPTY@
		map.put("HOUSETYPE", "@EMPTY@");// 拟购房产类别 @EMPTY@
		map.put("IFFCZ", "@EMPTY@");// 是否取得产权证 @EMPTY@
		map.put("HOUSEBUILDAREA", "@EMPTY@");// 拟购房屋建筑面积 @EMPTY@
		map.put("HOUSENUM", "@EMPTY@");// 拟购房屋套数 @EMPTY@
		map.put("FZMONEY", "@EMPTY@");// 其他银行负债（合计） @EMPTY@
		map.put("BUSITYPE", "@EMPTY@");// 业务品种 @EMPTY@
		map.put("NPSCORE", (Double.valueOf(getNPSCORE())).intValue());// 内部评级结果 ? 评分卡结果
		map.put("MAINASSURE", "C101");// 担保方式 保证 确定代码C101
		map.put("RATEHIGHESTAMOUNT", "@EMPTY@");// 评分卡最高额度固定 @EMPTY@
		map.put("FIRSTPAYRATE", "@EMPTY@");// 首付比例 @EMPTY@
		return map;
	}
	
	public RuleResponseObject edcs(String cust_id,String user_id,String org_id) throws Exception{
		// PackageEdcsData ped = new PackageEdcsData();
		DbAccess db = new DbAccess();
		String sql = "select cb.CUST_ID,cb.TEL_NO,cb.CUST_NAME,cb.ID_NO," +
				"IFNULL(fr.SR_1Y,0) SR_1Y,IFNULL(fr.ZC_1Y,0) ZC_1Y,IFNULL(fr.ZZC,0) " +
				"ZZC,IFNULL(fr.ZFZ,0) ZFZ from cust_base cb left join fin_rpt fr on " +
				"cb.cust_id=fr.CUST_ID where cb.cust_id='"+cust_id+"'";
		Map<String, String> custMap = db.queryForMap(sql);
		int i=0;
		RuleResponseObject rr = null;
		StCalculatorInterface sc = StCalculator.getInstance();
		//for (Map<String, String> custMap : custList) {
			custMap.put("CUST_ORG", org_id);
			custMap.put("USER_ID", user_id);
			Map<String, Object> paraMap = getData(custMap);
			setSendMapMsg(paraMap);
			RuleRequestObject rro = new RuleRequestObject();
			rro.setCallStep("4");
			InterInfo ii = new InterInfo();
			ii.setInterCode("LMT001");
			rro.setInterInfo(ii);
			for(String key :paraMap.keySet()){
				rro.put(key, paraMap.get(key));
			}
			//log.info("请求参数:"+paraMap.toString());
			rr= sc.calculateRule(rro);
			i++;
			log.info("身份证号:"+custMap.get("ID_NO")+";返回编码:"+rr.getCode()+";DESC:"+rr.getDesc()+";额度:"+rr.getCseqLmt()+";个数:"+i);
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

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		

	}

}
