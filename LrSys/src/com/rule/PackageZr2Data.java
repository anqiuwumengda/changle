package com.rule;

import hlc.base.db.DbAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.http.HnData;
import com.sdnx.st.dp.StCalculator;
import com.sdnx.st.dp.StCalculatorInterface;
import com.sdnx.st.dp.model.RuleRequestObject;
import com.sdnx.st.dp.model.RuleRequestObject.InterInfo;
import com.sdnx.st.dp.model.RuleResponseObject;
import com.tgb.util.DateTools;

/**
 * 准入
 * 
 * @author javacai
 * 
 */
public class PackageZr2Data {
	private Map<String, Object> sendMapMsg;
	Logger log = Logger.getLogger(PackageZr2Data.class);

	/**
	 * 
	 * @param paraMap
	 *            客户编号 客户经理所在网点 当前操作员 身份证号 姓名
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getData(Map<String, String> paraMap)
			throws Exception {
		// 获取准入接口数据
		HnData hn = new HnData();
		JSONObject zrJsonList = hn.getCustZr(paraMap.get("ID_NO").toString(),
				paraMap.get("CUST_NAME").toString());
		if (null == zrJsonList) {
			throw new Exception("获取行内接口数据异常");
		}
		JSONObject pjJsonList = hn.getCustPj(paraMap.get("ID_NO").toString(),
				paraMap.get("CUST_NAME").toString());
		if (null == pjJsonList) {
			throw new Exception("获取行内接口数据异常");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID",
				DateTools.getCurrentSysData("yyyyMMdd")
						+ paraMap.get("CUST_ID") + CommonUtil.getSeq());// 受理编号
																		// 日期+客户编号+4位序列
		map.put("DEPTCODE", paraMap.get("CUST_ORG"));// 申请机构代码 网点代码 客户经理所在网点
		map.put("INSTCITYCODE", "02000A");// 地市机构编号 907
		map.put("APPBUSITYPE", "@EMPTY@");// 贷款产品 空置 ： @EMPTY@
		map.put("UPBUSITYPE", "@EMPTY@");// 上层业务品种 @EMPTY@
		map.put("REQLMT", "@EMPTY@");// 申请额度 @EMPTY@
		map.put("IFNEWCUST", "@EMPTY@");// 是否新增客户 @EMPTY@
		map.put("RELALOAN", zrJsonList.get("RELALOAN"));// 相关企业是否存在用信余额贷款余额
		// 准入接口
		// map.put("RELALOAN", "@EMPTY@");
		map.put("FRCODE", "90706");// 法人机构编号 90706
		map.put("OPERATOR", paraMap.get("USER_ID"));// 当前操作员 登录用户ID:乐融贷操作员
		map.put("CURRENTDATE", DateTools.getCurrentSysData("yyyy-MM-dd"));// 会计日期
																			// 当前日期
		map.put("CALLSTEP", "2");// 规则调取阶段 1

		// 外部失信或客户违法违纪、不良嗜好信息名单、黑名单 客户准入接口LIST
		Object obj = zrJsonList.get("CUSTLIST");
		if (!"@EMPTY@".equals(obj)) {
			DbAccess db = new DbAccess();
			String sql = "select ifnull(max(ZZC),0) ZZC,ifnull(max(ZFZ),0) ZFZ from fin_rpt where cust_id='"
					+ paraMap.get("CUST_ID") + "'";
			Map<String, String> tmpMap = db.queryForMap(sql);
			List<Map<String, Object>> list = (List<Map<String, Object>>) obj;
			for (Map<String, Object> tmp : list) {
				tmp.put("CUSTID", tmp.get("CUSTID") == null ? "@EMPTY@" : tmp.get("CUSTID"));
				tmp.put("INSXLIST", "@EMPTY@");
				tmp.put("DBSUMBALAMT", "0");
				tmp.put("IFFREEZE", pjJsonList.get("IFFREEZE"));
				tmp.put("CNAME", paraMap.get("CUST_NAME").toString());
				tmp.put("FAMASSET",
						tmpMap.get("ZZC") == "" ? "0" : tmpMap.get("ZZC"));
				tmp.put("FAMDEBT",
						tmpMap.get("ZFZ") == "" ? "0" : tmpMap.get("ZFZ"));
			}
			map.put("CUSTLIST", list);
		} else {
			map.put("CUSTLIST", obj == null ? "@EMPTY@" : obj);
		}

		// map.put("CUSTLIST", "@EMPTY@");
		/**
		 * 1、客户有贷款余额的贷款信息(机构、贷款产品) 2、表内未结清贷款信息 3、表内未结清贷款五级分类状态 客户准入接口 LIST
		 */
		map.put("BNLIST", zrJsonList.get("BNLIST") == null ? "@EMPTY@" : zrJsonList.get("BNLIST"));
		if(!"@EMPTY@".equals(map.get("BNLIST"))){
			List<Map<String,Object>> bwList = (List<Map<String, Object>>) map.get("BNLIST");
			if(bwList != null && !bwList.isEmpty()){
				for(Map<String,Object> bw : bwList){
					if(bw.get("CUSTID") == null)
						bw.put("CUSTID", "@EMPTY@");
					
					Object balAmt = bw.get("BALAMT");
					Object debtinterest = bw.get("DEBTINTEREST");
					Object delayamtdays = bw.get("DELAYAMTDAYS");
					Object delayinterestdays = bw.get("DELAYINTERESTDAYS");
					
					if(null!=balAmt && !"@EMPTY@".equals(balAmt)){
						Double d =(Double)balAmt;
						bw.put("BALAMT", String.valueOf(d.intValue()));
					}
					if(null!=debtinterest && !"@EMPTY@".equals(debtinterest)){
						Double d =(Double)debtinterest;
						bw.put("DEBTINTEREST", String.valueOf(d.intValue()));
					}
					if(null!=delayamtdays && !"@EMPTY@".equals(delayamtdays)){
						Double d =(Double)debtinterest;
						bw.put("DELAYAMTDAYS", String.valueOf(d.intValue()));
					}
					if(null!=delayinterestdays && !"@EMPTY@".equals(delayinterestdays)){
						//Double d =(Double)delayinterestdays;
						bw.put("DELAYINTERESTDAYS", String.valueOf(delayinterestdays));
					}
				}
			}
		}
		// map.put("BNLIST", "@EMPTY@");
		/**
		 * 表外未结清贷款信息 表外已结清贷款信息 客户准入接口 LIST
		 */
		map.put("BWLIST", zrJsonList.get("BWLIST") == null ? "@EMPTY@" : zrJsonList.get("BWLIST"));
		if(!"@EMPTY@".equals(map.get("BWLIST"))){
			List<Map<String,Object>> bwList = (List<Map<String, Object>>) map.get("BWLIST");
			if(bwList != null && !bwList.isEmpty()){
				for(Map<String,Object> bw : bwList){
					if(bw.get("CLOSEDATE") == null || "".equals(bw.get("CLOSEDATE")) || " ".equals(bw.get("CLOSEDATE")))
						bw.put("CLOSEDATE", "@EMPTY@");
				}
			}
		}
		// map.put("BWLIST", "@EMPTY@");

		// map.put("EXPLIST", zrJsonList.get("EXPLIST"));//展期(根据发起展期授权的日期进行判断)
		// 客户准入接口 LIST
		map.put("EXPLIST", zrJsonList.get("EXPLIST") == null ? "@EMPTY@" : zrJsonList.get("EXPLIST"));
		if(!"@EMPTY@".equals(map.get("EXPLIST"))){
			List<Map<String,Object>> bwList = (List<Map<String, Object>>) map.get("EXPLIST");
			if(bwList != null && !bwList.isEmpty()){
				for(Map<String,Object> bw : bwList){
					if(bw.get("CUSTID") == null)
						bw.put("CUSTID", "@EMPTY@");
					if(bw.get("BUSICODE") == null || "".equals(bw.get("BUSICODE")))
						bw.put("BUSICODE", "@EMPTY@");
				}
			}
		}
		/**
		 * 客户对外担保表内未结清贷款 客户对外担保表外未结清贷款 客户准入接口 LIST
		 */
		map.put("BNWLIST_DB", zrJsonList.get("BNWLIST_DB") == null ? "@EMPTY@" : zrJsonList.get("BNWLIST_DB"));
		if(!"@EMPTY@".equals(map.get("BNWLIST_DB"))){
			List<Map<String,Object>> bwList = (List<Map<String, Object>>) map.get("BNWLIST_DB");
			if(bwList != null && !bwList.isEmpty()){
				for(Map<String,Object> bw : bwList){
					if(bw.get("CUSTID") == null)
						bw.put("CUSTID", "@EMPTY@");
					if(bw.get("ISOUTTABLELOAN") == null || "".equals(bw.get("ISOUTTABLELOAN")))
						bw.put("ISOUTTABLELOAN", "@EMPTY@");
				}
			}
		}
		// map.put("BNWLIST_DB", "@EMPTY@");
		// ZXLIST 征信信息 空置 ： @EMPTY@ list内容为空还是map内的value为空
		List<Map<String, Object>> zxlList = new ArrayList<Map<String, Object>>();
		Map<String, Object> tmp = new HashMap<String, Object>();
		tmp.put("TOWYOVERDUETERM", pjJsonList.get("BHTOWYOVERDUETERM") == null ? "@EMPTY@" : pjJsonList.get("BHTOWYOVERDUETERM"));// 最近24个月本息最大逾期期数
		tmp.put("ONEYOVERDUENUM", pjJsonList.get("BHONEYOVERDUENUM"));// 最近12个月本息逾期次数
		tmp.put("CHECKOBJ", "1");// 检查对象
		tmp.put("CUSTID", "@EMPTY@");// 客户编号
		tmp.put("CNAME", "@EMPTY@");// 客户名称
		tmp.put("CARDTYPE", "@EMPTY@");// 证件类型
		tmp.put("CARDNUM", "@EMPTY@");// 证件号码
		tmp.put("THWROSTCLASS", "@EMPTY@");// 当前他行贷款最差分类形态
		tmp.put("DBWROSTCLASS", "@EMPTY@");// 对外担保贷款最差分类形态
		tmp.put("FRNUM", "@EMPTY@");// 贷款法人机构数
		tmp.put("NORMALCARDNUM", "@EMPTY@");// 未销户信用卡发卡机构数
		tmp.put("USERATE", "@EMPTY@");// 未销户贷记卡与准贷记卡最近6个月平均使用率
		tmp.put("SIXMCHECKNUM", "@EMPTY@");// 最近6个月征信查询次数
		tmp.put("ONEYCHECKNUM", "@EMPTY@");// 最近12个月征信查询次数
		tmp.put("TOWYCHECKNUM", "@EMPTY@");// 最近24个月征信查询次数
		tmp.put("THREEYCHECKNUM", "@EMPTY@");// 最近36个月征信查询次数
		tmp.put("SIXMOVERDUETERM", "@EMPTY@");// 最近6个月本息最大逾期期数
		tmp.put("ONEYOVERDUETERM", "@EMPTY@");// 最近12个月本息最大逾期期数
		tmp.put("SIXMOVERDUENUM", "@EMPTY@");// 最近6个月本息逾期次数
		tmp.put("TOWYOVERDUENUM", "@EMPTY@");// 最近24个月本息逾期次数
		tmp.put("THREEYOVERDUENUM", "@EMPTY@");// 最近36个月本息逾期次数
		tmp.put("ONEYKHNUM", "@EMPTY@");// 最近12个月贷记卡开户个数
		tmp.put("MINOVERDUETDATE", "@EMPTY@");// 最近一次本息逾期距今月份数
		tmp.put("DBSUMBALAMT", "@EMPTY@");// 对外担保本金余额
		tmp.put("THREEYHIGHESTOVDTIMES", "@EMPTY@");// 连续最高逾期次数
		tmp.put("ONEYTOTALOVDTIMES", "@EMPTY@");// 累计逾期次数
		tmp.put("OVERDUETERM", "@EMPTY@");// 当前逾期期数
		tmp.put("STATUS", "@EMPTY@");// 查询状态
		zxlList.add(tmp);
		map.put("ZXLIST", zxlList);// 无征信信息 直接设置LIST为@EMPTY@
		return map;
	}

	public RuleResponseObject zr2(String cust_id, String user_id, String org_id)
			throws Exception {
		StCalculatorInterface sc = StCalculator.getInstance();
		RuleResponseObject rr = null;
		Map<String, String> custMap = new HashMap<String, String>();
		custMap.put("CUST_ORG", org_id);
		custMap.put("USER_ID", user_id);
		custMap.put("CUST_ID", cust_id);
		DbAccess db = new DbAccess();
		Map<String, String> tmp = db
				.queryForMap("select ID_NO,CUST_NAME from cust_base where cust_id='"
						+ cust_id + "'");
		custMap.put("ID_NO", tmp.get("ID_NO"));
		custMap.put("CUST_NAME", tmp.get("CUST_NAME"));
		Map<String, Object> paraMap = getData(custMap);
		setSendMapMsg(paraMap);
		RuleRequestObject rro = new RuleRequestObject();
		rro.setCallStep("2");
		InterInfo ii = new InterInfo();
		ii.setInterCode("ZD00010");
		rro.setInterInfo(ii);
		for (String key : paraMap.keySet()) {
			rro.put(key, paraMap.get(key));
		}
		//log.info("请求参数:" + paraMap.toString());
		rr = sc.calculateRule(rro);
		log.info("返回编码:" + rr.getCode()
				+ ";返回结果:" + rr.getResult() + ";DESC:" + rr.getDesc());
		custMap.clear();
		tmp.clear();
		paraMap.clear();
		custMap=null;
		tmp=null;
		paraMap=null;
		return rr;
	}

	public Map<String, Object> getSendMapMsg() {
		return sendMapMsg;
	}

	public void setSendMapMsg(Map<String, Object> sendMapMsg) {
		this.sendMapMsg = sendMapMsg;
	}

	/**
	 * @param args
	 *            客户编号 客户经理所在网点 当前操作员 身份证号 姓名
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CUST_ID", "123");
		map.put("CUST_ORG", "123");
		map.put("USER_ID", "123");
		map.put("ID_NO", "123");
		map.put("CUST_NAME", "123");
		map.put("RELALOAN", "123");
	}

}
