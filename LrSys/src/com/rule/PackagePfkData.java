package com.rule;

import hlc.base.db.DbAccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.http.HnData;
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
public class PackagePfkData {
	private Map<String,Object> sendMapMsg;
	Logger log = Logger.getLogger(PackagePfkData.class);

	/**
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getData(Map<String, String> paraMap)
			throws Exception {
		 HnData hn = new HnData();
		 JSONObject pjJsonList= hn.getCustPj(paraMap.get("ID_NO").toString(),paraMap.get("CUST_NAME").toString());
		 if(null == pjJsonList){
			 throw new Exception("获取行内接口数据异常");
		 }
		 String ztzyxm=paraMap.get("JTZYXM")+"";
		DbAccess db = new DbAccess();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID",
				DateTools.getCurrentSysData("yyyyMMdd")
						+ paraMap.get("CUST_ID") + CommonUtil.getSeq());// 受理编号
																		// 日期+客户编号+4位序列
		map.put("DEPTCODE", paraMap.get("CUST_ORG"));// 申请机构代码 网点代码
		map.put("INSTCITYCODE", "02000A");// 地市编号 907固定
		map.put("APPBUSITYPE", "default");// 贷款产品 @EMPTY@
		map.put("FRCODE", "90706");// 法人机构编号 固定:90706
		map.put("OPERATOR", paraMap.get("USER_ID"));// 当前操作员
		map.put("CURRENTDATE", DateTools.getCurrentSysData("yyyy-MM-dd"));// 会计日期
		map.put("CUSTID", paraMap.get("CUST_ID"));// 客户编号
		map.put("CNAME", paraMap.get("CUST_NAME"));// 客户名称
		map.put("CARDTYPE", "0");// 证件类型 身份证 ？确定代码
		map.put("PERCARDNUM", paraMap.get("ID_NO"));// 证件号码 身份证
		String cust_type = paraMap.get("CUST_TYPE");
		// 客户类型 乐融贷客户类型
		if("NCJM".equals(cust_type)){
			map.put("CUSTTYPE", "01");
		}else if("GTGS".equals(cust_type)){
			map.put("CUSTTYPE", "02");
		}else if("CZJM".equals(cust_type)){
			map.put("CUSTTYPE", "03");
		}else{
			map.put("CUSTTYPE", "04");
		}
		
		map.put("CUSTPROPERTY", "@EMPTY@");// 客户性质 @EMPTY@
		map.put("INDUSTRY", "@EMPTY@");// 行业分类 @EMPTY@
		String edu_level = paraMap.get("EDU_LEVEL");
		// 最高学历 乐融贷-------------------------
		//1、研究生；2-大学本科；3-大学专科或专科学校；4-中等专业或技校；5-高中；6-初中；7-小学；8-文盲
		if("01".equals(edu_level)){
			map.put("EDUCATIONLEVEL", "8");
		}else if("02".equals(edu_level)){
			map.put("EDUCATIONLEVEL", "7");
		}else if("03".equals(edu_level)){
			map.put("EDUCATIONLEVEL", "6");
		}else if("04".equals(edu_level)){
			map.put("EDUCATIONLEVEL", "5");
		}else if("05".equals(edu_level)){
			map.put("EDUCATIONLEVEL", "4");
		}else if("06".equals(edu_level)){
			map.put("EDUCATIONLEVEL", "3");
		}else if("07".equals(edu_level)){
			map.put("EDUCATIONLEVEL", "2");
		}else if("08".equals(edu_level)){
			map.put("EDUCATIONLEVEL", "1");
		}
		
		map.put("AGE", paraMap.get("USER_AGE"));// 年龄 乐融贷
		map.put("SEX", paraMap.get("SEX").equals("01")?"1":"2");// 性别 乐融贷 （1-男，2-女）
		// 婚姻状况 乐融贷 0-未婚；1-已婚；2-丧偶；3-离婚
		String jh_flag = paraMap.get("JH_FLAG");
		if("1".equals(jh_flag)){
			map.put("MARRIGE", "0");
		}else if("2".equals(jh_flag)){
			map.put("MARRIGE", "1");
		}else if("3".equals(jh_flag)){
			map.put("MARRIGE", "3");
		}else if("4".equals(jh_flag)){
			map.put("MARRIGE", "2"); 
		}
		
		map.put("IFHAVECHILD", paraMap.get("CHILD_FLAG").equals("1")?"T":"F");// 有无子女 乐融贷1 T有 0F无 
		String jh_date = paraMap.get("JH_DATE");
		if(null!=jh_date && !"".equals(jh_date))
			map.put("MARRIGEDATE", jh_date.replace("/", "-"));// 结婚（离婚）证登记日期 乐融贷
		else
			map.put("MARRIGEDATE", "@EMPTY@");// 结婚（离婚）证登记日期 乐融贷
		String vocation = paraMap.get("VOCATION");
		// 职业细分 客户基本信息职业
		if("01".equals(vocation)){
			map.put("VOCATIONMIN", "0");
		}else if("02".equals(vocation)){
			map.put("VOCATIONMIN", "1");
		}else if("03".equals(vocation)){
			map.put("VOCATIONMIN", "2");
		}else if("04".equals(vocation)){
			map.put("VOCATIONMIN", "3");
		}else if("05".equals(vocation)){
			map.put("VOCATIONMIN", "4");
		}else if("06".equals(vocation)){
			map.put("VOCATIONMIN", "5");
		}else if("07".equals(vocation)){
			map.put("VOCATIONMIN", "6");
		}else if("08".equals(vocation)){
			map.put("VOCATIONMIN", "7");
		}else if("09".equals(vocation)){
			map.put("VOCATIONMIN", "8");
		}else if("10".equals(vocation)){
			map.put("VOCATIONMIN", "9");
		}else if("11".equals(vocation)){
			map.put("VOCATIONMIN", "10");
		}else if("12".equals(vocation)){
			map.put("VOCATIONMIN", "11");
		}else if("13".equals(vocation)){
			map.put("VOCATIONMIN", "12");
		}else{
			map.put("VOCATIONMIN", "13");
		}
		//**************// 本单位工作起始年限 计算工作几年
		String cust_id = paraMap.get("CUST_ID").toString();
		Map<String,String> map1 = db.queryForMap("select WORK_DATE,YLBXAMT from cust_zg where cust_id='"+cust_id+"'");
		String temp = map1.get("WORK_DATE");
		if(null!=temp && !"".equals(temp) && temp.length()>=4){
			String work_date = map1.get("WORK_DATE").substring(0,4);//参加工作年份
			int i = Integer.parseInt(DateTools.getCurrentSysData("yyyy"))-Integer.parseInt(work_date);
			map.put("EMPLOYERTIME", String.valueOf(i));
		}else{
			map.put("EMPLOYERTIME", "0");
		}
		
		//***************
		String ylbxamt = map1.get("YLBXAMT");
		if(null!=ylbxamt && !"".equals(ylbxamt))
			map.put("SECUREMONEY", Double.parseDouble(ylbxamt));// 医疗保险上年度合计缴纳金额 ？@EMPTY@元
		else
			map.put("SECUREMONEY", "0");
		map.put("CUSTWORKINFO", paraMap.get("JK_JT_STATUS").equals("01")?"2":"1");// 借款人及家庭成员工作状况
																// 乐融贷:家庭信息：客户及家庭成员工作情况------------------------
		//*************
		// 是否在城区购买商住商用房 固定资产，商品住房数量+商铺数量》0
		Map<String,String> map2 = db.queryForMap("select count(*) num from cust_base cb " +
				"left join rpt_dtl rd on cb.cust_id=rd.cust_id " +
				"where rd.SUBJ_CD='A0005' and (rd.SUBJ_TYPE='03' or rd.SUBJ_TYPE='04') and cb.cust_id='"+cust_id+"'");
		if(Integer.parseInt(map2.get("num"))>0){
			map.put("IFBUYHOUSE", "T");//是--------------------
		}else{
			map.put("IFBUYHOUSE", "F");//否------------------------
		}
		
		//************
		// 申请金额
													// 固定值：农户200000，个体：300000，城镇300000
		String tmpEd = GetEd.getEd(cust_id);
		if("NCJM".equals(paraMap.get("CUST_TYPE")) ){
			if(null!=tmpEd){
				map.put("APPREQLMT", tmpEd);
			}else{
				map.put("APPREQLMT", "100000");
			}
		}else if("CZJM".equals(paraMap.get("CUST_TYPE"))){
			if(null!=tmpEd){
				map.put("APPREQLMT", tmpEd);
			}else{
				map.put("APPREQLMT", "200000");
			}
		}else if("GTGS".equals(paraMap.get("CUST_TYPE"))){
			if(null!=tmpEd){
				map.put("APPREQLMT", tmpEd);
			}else{
				map.put("APPREQLMT", "200000");
			}
		}else {
			
		}
		Map<String,String> map3 = db.queryForMap("select (IFNULL(SR_1Y,0)+IFNULL(SR_2Y,0)+IFNULL(SR_3Y,0))/3 val3sr," +
				"(IFNULL(ZC_1Y,0)+IFNULL(ZC_2Y,0)+IFNULL(ZC_3Y,0))/3 val3zc,IFNULL(SR_1Y,0) SR_1Y,IFNULL(ZC_1Y,0) ZC_1Y,IFNULL(SR_2Y,0) SR_2Y,IFNULL(ZC_2Y,0) ZC_2Y,IFNULL(ZFZ,0) ZFZ,IFNULL(ZZC,0) ZZC " +
				"from fin_rpt where CUST_ID='"+cust_id+"';");
		map.put("FAMANNUINCOME",  Double.parseDouble(map3.get("val3sr")==null?"0":map3.get("val3sr"))*10000);// 近三年家庭年均收入 计算
		map.put("FAMANNUINOUT",  Double.parseDouble(map3.get("val3zc")==null?"0":map3.get("val3zc"))*10000);// 近三年家庭年均支出 计算
		map.put("LASTFAMINCOME",  Double.parseDouble(map3.get("SR_1Y")==null?"0":map3.get("SR_1Y"))*10000);// 上年度家庭收入 1年前
		map.put("LASTFAMINOUT",  Double.parseDouble(map3.get("ZC_1Y")==null?"0":map3.get("ZC_1Y"))*10000);// 上年度家庭支出 1年前
		map.put("FAMINCOMEBEFORE", Double.parseDouble( map3.get("SR_2Y")==null?"0":map3.get("SR_2Y"))*10000);// 前年家庭收入 2年前
		map.put("FAMINCOMEBEFOUT",  Double.parseDouble(map3.get("ZC_2Y")==null?"0":map3.get("ZC_2Y"))*10000);// 前年家庭支出 2年前
		map.put("FAMDEBT",  Double.parseDouble(map3.get("ZFZ")==null?"0":map3.get("ZFZ"))*10000);// 家庭总负债 计算
		map.put("FAMASSET",  Double.parseDouble(map3.get("ZZC")==null?"0":map3.get("ZZC"))*10000);// 家庭总资产 计算
		
		map.put("BXNUM", paraMap.get("BX_CNT"));// 有人身意外伤害保险或工伤保险的员工数量 乐融贷
		
		map.put("EMPLOYEENUM", paraMap.get("WORKERS"));// 职工人数（雇员人数） 乐融贷
		//**************************************************************************
		map.put("IFBUYGARAGE", "@EMPTY@");// 是否购买车库 @EMPTY@
		map.put("FIRSTPAYRATE", "@EMPTY@");// 首付比例 @EMPTY@
		
		
		if ("01".equals(ztzyxm)) {//种植蔬菜
			Map<String,String> map4 = db.queryForMap("select ifnull(sum(ZZ_MS),0) ZZ_MS from cust_zz where cust_id='"+cust_id+"' and ZZ_MODEL='02' and ZW_TYPE in('01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','24')"); //作物小于27为蔬菜
			map.put("JJXMBUILDAREA", map4.get("ZZ_MS")==null?"0":map4.get("ZZ_MS"));// 大棚面积 种植亩数
		}else{//种植其他
			Map<String,String> map4 = db.queryForMap("select ifnull(sum(ZZ_MS),0) ZZ_MS from cust_zz where cust_id='"+cust_id+"' and ZZ_MODEL='02' and ZW_TYPE not in('01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','24')");//作物大于27为蔬菜
			map.put("JJXMBUILDAREA", map4.get("ZZ_MS")==null?"0":map4.get("ZZ_MS"));// 大棚面积 种植亩数
		}
		
		
		
		//根据主营项目 去判断年限
		
		
		
		if ("01".equals(ztzyxm)||"02".equals(ztzyxm)) {//种植
			String zzsql="SELECT MIN(z.biz_date) as biz_date  FROM `cust_zz` z where cust_id='"+paraMap.get("CUST_ID")+"';";
			Map<String, String> date = db.queryForMap(zzsql);
			String nx = date.get("biz_date");
			if("".equals(nx)||nx==null||"0".equals(nx)){
				map.put("JJXMLMT", "0");
			}else{
				int jynx=0;
				try {
					jynx=DateTools.getCurrentYear()-Integer.valueOf(nx);
				} catch (Exception e) {}
				map.put("JJXMLMT", jynx+"");
			}	
		}else if("03".equals(ztzyxm)||"04".equals(ztzyxm)) {//养殖
			String  yzsql="SELECT MIN(z.biz_date) as biz_date  FROM `cust_yz` z where cust_id='"+paraMap.get("CUST_ID")+"';";
			Map<String, String> date = db.queryForMap(yzsql);
			String nx = date.get("biz_date");
			if("".equals(nx)||nx==null||"0".equals(nx)){
				map.put("JJXMLMT", "0");
			}else{
				int jynx=0;
				try {
					jynx=DateTools.getCurrentYear()-Integer.valueOf(nx);
				} catch (Exception e) {}
				map.put("JJXMLMT", jynx+"");
			}

		}else if("05".equals(ztzyxm)){//职工
			
			String  yzsql="SELECT MIN(z.work_date)  as work_date FROM `cust_zg` z where cust_id='"+paraMap.get("CUST_ID")+"';";
			Map<String, String> date = db.queryForMap(yzsql);
			String nx = date.get("work_date");
			if("".equals(nx)||nx==null||"0".equals(nx)){
				map.put("JJXMLMT", "0");
			}else{
				int jynx=0;
				try {
					jynx=DateTools.getCurrentYear()-Integer.valueOf(nx);
				} catch (Exception e) {}
				map.put("JJXMLMT", jynx+"");
			}
			
		}else if ("11".equals(ztzyxm)) {//打工
			String  yzsql="SELECT MAX(DG_YEARS) DG_YEARS FROM `cust_dg` where cust_id='"+paraMap.get("CUST_ID")+"';";
			Map<String, String> date = db.queryForMap(yzsql);
			String nx = date.get("DG_YEARS");
			if("".equals(nx)||nx==null||"0".equals(nx)){
				map.put("JJXMLMT", "0");
			}else{
				int jynx=0;
				try {
					jynx=DateTools.getCurrentYear()-Integer.valueOf(nx);
				} catch (Exception e) {}
				map.put("JJXMLMT", jynx+"");
			}
			
			
		}else{//经商
			String  yzsql="SELECT MIN(z.biz_date) BIZ_DATE FROM `cust_js` z where cust_id='"+paraMap.get("CUST_ID")+"';";
			Map<String, String> date = db.queryForMap(yzsql);
			String nx = date.get("BIZ_DATE");
			if("".equals(nx) || nx==null || "0".equals(nx)){
				map.put("JJXMLMT", "0");
			}else{
				int jynx=0;
				try {
					jynx= DateTools.getCurrentYear()-Integer.valueOf(nx);
				} catch (Exception e) {}
				map.put("JJXMLMT", jynx+"");
			}
		}
		 
		
		//map.put("JJXMLMT", paraMap.get("BIZ_YEARS")==null?"0":paraMap.get("BIZ_YEARS"));// 种植/养殖/经营年限 乐融贷 计算--------------------------------
		
		Map<String,String> map5 = db.queryForMap("select ifnull(sum(SUBJ_VAL),0) JYXMGDZC from rpt_dtl rd " +
				"where rd.SUBJ_CD='A0005' and (rd.SUBJ_TYPE='05' or rd.SUBJ_TYPE='07' or rd.SUBJ_TYPE='08') and cust_id='"+cust_id+"'");
		map.put("JJXMPPE",  Double.parseDouble(map5.get("JYXMGDZC"))*10000);// 经营项目固定资产 乐融贷
													// 固定资产：经营场所金额+机器设备金额+营运车金额
		Map<String,String> map6 = db.queryForMap("select ifnull(sum(DTL_NUM),0) DTL_NUM from rpt_dtl rd " +
				"where rd.SUBJ_CD='A0005' and rd.SUBJ_TYPE='08' and cust_id='"+cust_id+"'");
		map.put("CARNUM",  Double.parseDouble(map6.get("DTL_NUM")==null?"0":map6.get("DTL_NUM"))*10000);// 营运车辆数量 乐融贷 固定资产：营运车数量
		
		
		
		//经营场所面积----只有经商有经营场所面积
		
		
		if ("01".equals(ztzyxm)||"02".equals(ztzyxm)) {//种植面积
			
			if ("01".equals(ztzyxm)) {
				String  yzsql="SELECT sum(zz_ms)  ZZ_MS FROM `cust_zz` where cust_id='"+paraMap.get("CUST_ID")+"' and ZW_TYPE in('01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','24');";
				Map<String, String> date = db.queryForMap(yzsql);
				
				map.put("FIELDAREAZY", (date.get("ZZ_MS")==null||"".equals(date.get("ZZ_MS"))) ?"@EMPTY@":date.get("ZZ_MS"));// 种植场地面积（自用） ？---------------
				map.put("FIELDAREAZL", "@EMPTY@");// 种植场地面积（租赁） ？------------------------
			}else{
				String  yzsql="SELECT sum(zz_ms)  ZZ_MS FROM `cust_zz` where cust_id='"+paraMap.get("CUST_ID")+"' and ZW_TYPE not in('01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','24');";
				Map<String, String> date = db.queryForMap(yzsql);
				
				map.put("FIELDAREAZY", (date.get("ZZ_MS")==null||"".equals(date.get("ZZ_MS"))) ?"@EMPTY@":date.get("ZZ_MS"));// 种植场地面积（自用） ？---------------
				map.put("FIELDAREAZL", "@EMPTY@");// 种植场地面积（租赁） ？------------------------
			}
			
			
			
			
		}else if("03".equals(ztzyxm)||"04".equals(ztzyxm)){//养殖面积
			map.put("FIELDAREAZY", "@EMPTY@");// 养殖场地面积（自用） ？---------------
			map.put("FIELDAREAZL", "@EMPTY@");// 养殖场地面积（租赁） ？------------------------
			
		}else {//经商
			String  yzsql="SELECT OWN_AREA,RENT_AREA FROM `cust_js` z where cust_id='"+paraMap.get("CUST_ID")+"';";
			Map<String, String> date = db.queryForMap(yzsql);
			
			map.put("FIELDAREAZY", (date.get("OWN_AREA")==null||"".equals(date.get("OWN_AREA"))) ?"@EMPTY@":date.get("OWN_AREA"));// 经商经营场地面积（自用） ？---------------
			map.put("FIELDAREAZL", (date.get("RENT_AREA")==null||"".equals(date.get("RENT_AREA")))?"@EMPTY@":date.get("RENT_AREA"));// 经商经营场地面积（租赁） ？------------------------
		}

		
		
		map.put("HOUSENUM", "@EMPTY@");// 拟购房屋套数 @EMPTY@
		map.put("HOUSETYPE", "@EMPTY@");// 拟购房屋类别 @EMPTY@
		Map<String,String> map7 = db.queryForMap("select sum(ifnull(DTL_NUM,0)) ZYFCSL from rpt_dtl rd " +
				"where rd.SUBJ_CD='A0005' and (rd.SUBJ_TYPE='03' or rd.SUBJ_TYPE='04' or rd.SUBJ_TYPE='02') and cust_id='"+cust_id+"'");
		map.put("ZYHOUSENUM", map7.get("ZYFCSL")==""?"0":map7.get("ZYFCSL"));// 自有房产数量（住宅、商铺）
														// 固定资产，商品住房数量+商铺数量+宅基地住房数量
		Map<String,String> map8 = db.queryForMap("select ifnull(INCOME_Y,0) SNDSR,(ifnull(INCOME_Y,0)-ifnull(PROFIT_Y,0)) SNDZC  from CUST_JS where cust_id='"+cust_id+"'");
		String sndsr = map8.get("SNDSR");
		if(null!=sndsr && !"".equals(sndsr))
			map.put("LASTYSR",  Double.parseDouble(sndsr)*10000);// 上年度经营收入 经商年收入----------------------------------------------
		else
			map.put("LASTYSR",  "0");
		String sndzc = map8.get("SNDZC");
		if(null!=sndzc && !"".equals(sndzc))
			map.put("LASTYZC",  Double.parseDouble(sndzc)*10000);// 上年度经营支出 年收入-年利润-----------------------------------------------
		else
			map.put("LASTYZC",  "0");
		
		Map<String,String> map9 = db.queryForMap("select max(ifnull(JTHYFZ,0)) JTHYFZ from fin_rpt where cust_id='"+cust_id+"'");
		String jthyfz = map9.get("JTHYFZ");
		if(null!=jthyfz && !"".equals(jthyfz)){
			map.put("DBSUMBALAMT1",  Double.parseDouble(jthyfz)*10000);// 对外担保本金余额（家庭）
			// 财务信息：家庭或有负债
		}else{
			map.put("DBSUMBALAMT1",  "0");
		}
		
		map.put("DBSUMBALAMT2", "@EMPTY@");// 对外担保本金余额（征信） @EMPTY@
		map.put("JOINWENTURE", "@EMPTY@");// 与下游客户的平均合作年限? 小微：@EMPTY@
		map.put("IFGDRFFGZ", "@EMPTY@");// 是否在固定日发放工资? 小微：@EMPTY@
		//Map<String,String> map10 = db.queryForMap("select * from  ");
		String tmp1 = paraMap.get("INCOME_Y");
		if(null!= tmp1 && !"".equals(tmp1))
			map.put("LASTTHREEYSR",  Double.parseDouble(tmp1)*10000);// 近三年经营收入? 经商年收入---------------------------
		else
			map.put("LASTTHREEYSR",  "0");
		String tmp2 = paraMap.get("INCOME_ZC");
		if(null!=tmp2 && !"".equals(tmp2))
			map.put("LASTTHREEYZC",  Double.parseDouble(tmp2)*10000);// 近三年经营支出?------------------------------
		else
			map.put("LASTTHREEYZC",  "0");
		
			// 经商年收入-经商年利润
		map.put("ACCRECEIVABLE", "0");// 所经营企业应收账款 ?
		map.put("OPERATEDEBT", "0");// 所经营企业负债? ?
		map.put("JJXMCL", "0");// 存货数量 ?
		map.put("CONTROLLERCHANGE", "");// 近三年实际控制人是否变更 ---------------
		map.put("BUSITYPE", "@EMPTY@");// 业务品种 @EMPTY@
		map.put("BHLOANBAL", "0");// 我行贷款余额 0
		map.put("THLOANBAL", "0");// 他行贷款余额 0
		map.put("DEBITCARDUSEDAMT", "0");// 贷记卡已用额度 0
		map.put("ZDEBITCARDUSEDAMT", "0");// 准贷记卡透支余额 0
		
		// 申请时点所在周期 上升期月份（9,10,11,12,1,2）   1
		// 下降期月份（3,4,5,6,7,8）CODE待确认                   f 2
		String currM= DateTools.getCurrentSysData("MM");
		if("09,10,11,12,01,02".contains(currM)){
			map.put("APPDATECYCLE", "1");
		}else{
			map.put("APPDATECYCLE", "2");
		}
		
		map.put("APPDATELOANNUM", "0");// 申请时点未结清账户数 接口获取
		map.put("IFEXISTCREDIT", "T");// 是否有征信报告
										// 征信信息表中status='2'时附F,status='3'时附T;固定T
		map.put("USEUAMT", "0");// 有效授信额度 0
		map.put("SIXMOVERDUENUM", "0");// 最近6个月本息的逾期次数（征信） 0
		map.put("ONEYOVERDUENUM", "0");// 最近12个月本息的逾期次数（征信） 0
		map.put("TOWYOVERDUENUM", "0");// 最近24个月本息的逾期次数（征信） 0
		map.put("THREEYOVERDUENUM", "0");// 最近36个月本息的逾期次数（征信） 0
		map.put("SIXMOVERDUETERM", "0");// 最近6个月本息最大逾期期数（征信） 0
		map.put("TOWYOVERDUETERM", "0");// 最近24个月本息最大逾期期数（征信） 0
		map.put("SIXMCHECKNUM", "0");// 最近6个月征信报告查询次数 0
		map.put("ONEYCHECKNUM", "0");// 最近12个月征信报告查询次数 0
		map.put("TOWYCHECKNUM", "0");// 最近24个月征信报告查询次数 0
		map.put("THREEYCHECKNUM", "0");// 最近36个月征信报告查询次数 0
		map.put("MINOVERDUETDATE", "-1");// 最近一次本息逾期距离现在的月数(征信） -1
		map.put("FIRSTACCMONTH", "0");// 最早贷款账户距今月数（征信） 0
		map.put("ONEYKHNUM", "0");// 最近12个月贷记卡开户个数（征信） 0
		map.put("CCARDNUM", "0");// 信用卡数量（不含销户） 0
		
		map.put("BHMINRATEOVERDUETDATE", pjJsonList.get("BHMINRATEOVERDUETDATE"));// 最近一次利息逾期距离现在的月数(本行）接口获取
		map.put("BHMINOVERDUETDATE", pjJsonList.get("BHMINOVERDUETDATE"));// 最近一次本金逾期距离现在的月数(本行） 接口获取
		
		map.put("BHSIXMOVERDUENUM", pjJsonList.get("BHSIXMOVERDUENUM").equals("@EMPTY@")?"0":pjJsonList.get("BHSIXMOVERDUENUM"));// 最近6个月本息的逾期次数（本行） 接口获取
		map.put("BHONEYOVERDUENUM", pjJsonList.get("BHONEYOVERDUENUM").equals("@EMPTY@")?"0":pjJsonList.get("BHONEYOVERDUENUM"));// 最近12个月本息的逾期次数（本行） 接口获取
		map.put("BHTOWYOVERDUENUM", pjJsonList.get("BHTWOYOVERDUENUM").equals("@EMPTY@")?"0":pjJsonList.get("BHTWOYOVERDUENUM"));// 最近24个月本息的逾期次数（本行） 接口获取
		map.put("BHTHREEYOVERDUENUM", pjJsonList.get("BHTHREEYOVERDUENUM").equals("@EMPTY@")?"0":pjJsonList.get("BHTHREEYOVERDUENUM"));// 最近36个月本息的逾期次数（本行） 接口获取
		map.put("BHSIXMOVERDUETERM", pjJsonList.get("BHSIXMOVERDUETERM").equals("@EMPTY@")?"0":pjJsonList.get("BHSIXMOVERDUETERM"));// 最近6个月本息最大逾期期数（本行） 接口获取
		map.put("BHTOWYOVERDUETERM", pjJsonList.get("BHTWOYOVERDUETERM").equals("@EMPTY@")?"0":pjJsonList.get("BHTWOYOVERDUETERM"));// 最近24个月本息最大逾期期数（本行） 接口获取
		map.put("ONEYSDAILY", pjJsonList.get("ONEYSDAILY").equals("@EMPTY@")?"0":pjJsonList.get("ONEYSDAILY"));// 近12个月（存款+理财）日均额 接口获取
		map.put("SIXMSDAILY", pjJsonList.get("SIXMSDAILY").equals("@EMPTY@")?"0":pjJsonList.get("SIXMSDAILY"));// 近6个月（存款与理财）日均额 接口获取
		map.put("NINEMDAILY", pjJsonList.get("NINEMDAILY").equals("@EMPTY@")?"0":pjJsonList.get("NINEMDAILY"));// 近9个月（存款与理财）日均额 接口获取
		map.put("SIXMDAILY", pjJsonList.get("SIXMDAILY").equals("@EMPTY@")?"0":pjJsonList.get("SIXMDAILY"));// 近6个月日均存款金额 接口获取
		map.put("ONEYDAILY", pjJsonList.get("ONEYDAILY").equals("@EMPTY@")?"0":pjJsonList.get("ONEYDAILY"));// 近12个月日均存款金额 接口获取
		map.put("ONEYSPOSNUM", pjJsonList.get("ONEYSPOSNUM").equals("@EMPTY@")?"0":pjJsonList.get("ONEYSPOSNUM"));// 最近12个月ATM与POS交易次数之和（夜间） 接口获取
		map.put("ONEYPOSNUM", pjJsonList.get("ONEYPOSNUM").equals("@EMPTY@")?"0":pjJsonList.get("ONEYPOSNUM"));// 最近12个月POS交易次数（夜间） 接口获取
		String tmp22 = pjJsonList.get("APPOPENNUM").toString();
		if("@EMPTY@".equals(tmp22)){
			map.put("APPOPENNUM","0" );// 申请时点前其他敞口个数 接口获取
		}else{
			map.put("APPOPENNUM",tmp22);// 申请时点前其他敞口个数 接口获取
		}
		//map.put("APPOPENNUM", );// 贷款申请时点敞口数 接口获取
		String tmp11 = pjJsonList.get("APPOTHEROPENNUM").toString();
		if("@EMPTY@".equals(tmp11)){
			map.put("APPOTHEROPENNUM","0" );// 申请时点前其他敞口个数 接口获取
		}else{
			map.put("APPOTHEROPENNUM",tmp11.split("-").length );// 申请时点前其他敞口个数 接口获取
		}
		
		map.put("FIRSTLOANDAY", pjJsonList.get("FIRSTLOANDAY").equals("@EMPTY@")?"0":pjJsonList.get("FIRSTLOANDAY"));// 与银行交易时长 接口获取
		map.put("BHONEYRATEOVERDUETERM", pjJsonList.get("BHONEYRATEOVERDUETERM").equals("@EMPTY@")?"0":pjJsonList.get("BHONEYRATEOVERDUETERM"));// 最近12个月利息最大逾期期数（本行） 接口获取
		map.put("ONEYOVERDUETERM", "0");// 最近12个月本息最大逾期期数（征信） 0
		map.put("CREDITCARDFLAG", pjJsonList.get("CREDITCARDFLAG"));// 有无行内银行卡 接口获取
		map.put("DEPOSITINFOFLAG", pjJsonList.get("DEPOSITINFOFLAG"));// 有无行内存款信息 接口获取
		map.put("IFFIXEDWORK", "@EMPTY@");// 是否有固定工作 @EMPTY@
		String jtzyxm = paraMap.get("JTZYXM");
		// 评级模型类型? 打分卡类型 ----------------------------
		if("01".equals(jtzyxm)){
			map.put("RRMODEL", "DFK003");//种植蔬菜
		}else if("02".equals(jtzyxm)){
			map.put("RRMODEL", "DFK004");//种植其他
		}else if("03".equals(jtzyxm)){
			map.put("RRMODEL", "DFK005");//养殖猪
		}else if("04".equals(jtzyxm)){
			map.put("RRMODEL", "DFK006");//养殖其他
		}else if("05".equals(jtzyxm)){
			map.put("RRMODEL", "DFK001");//职工
		}else if("06".equals(jtzyxm)){
			map.put("RRMODEL", "DFK009");//制造业
		}else if("07".equals(jtzyxm)){
			map.put("RRMODEL", "DFK008");//运输业
		}else if("08".equals(jtzyxm)){
			map.put("RRMODEL", "DFK007");//批发零售业
		}else if("09".equals(jtzyxm)){
			map.put("RRMODEL", "DFK010");//餐饮及住宿业
		}else if("10".equals(jtzyxm)){
			map.put("RRMODEL", "DFK011");//其他行业
		}else{
			map.put("RRMODEL", "DFK011");//其他行业
		}
		

		return map;
	}

	public RuleResponseObject pfk(String cust_id,String user_id,String org_id) throws Exception {
		DbAccess db = new DbAccess();
		String sql ="select cb.CUST_ID,cb.CUST_NAME,cb.ID_NO,cb.CUST_TYPE,cb.EDU_LEVEL," +
				"cb.USER_AGE,cb.SEX,cb.JH_FLAG,cb.CHILD_FLAG,cb.JH_DATE,cb.VOCATION,cb.JK_JT_STATUS,ifnull(cb.BX_CNT,0) BX_CNT,ifnull(cb.WORKERS,0) WORKERS,cb.JTZYXM" +
				" from cust_base cb where cb.cust_id='"+cust_id+"'";
		Map<String,String> map = db.queryForMap(sql);
		StCalculatorInterface sc = StCalculator.getInstance();
		RuleResponseObject rr=null;
		//for(Map<String,String> map :list){
			map.put("CUST_ORG", org_id);
			map.put("USER_ID", user_id);
			Map<String,Object> paramap = getData(map);
			setSendMapMsg(paramap);
			RuleRequestObject rro = new RuleRequestObject();
			rro.setCallStep("3");
			InterInfo ii = new InterInfo();
			ii.setInterCode("RR001");
			rro.setInterInfo(ii);
			for(String key :paramap.keySet()){
				rro.put(key, paramap.get(key));
			}
			//log.info("请求参数:"+paramap.toString());
			rr= sc.calculateRule(rro);
			log.info("返回编码:"+rr.getCode()+";DESC:"+rr.getDesc()+";评级级别:"+rr.getCseqLmt()+";评级模型类型:"+rr.getRrModel()+";评分卡最高额度:"+rr.getRateHighestaMount()+"");
			map.clear();
			paramap.clear();
			map=null;
			paramap=null;
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
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String ss= "2010";
		System.out.println(DateTools.getCurrentYear()-Integer.valueOf("2010"));
	}

}
