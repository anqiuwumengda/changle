package com.tgb.controller.propertymanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.http.HnData;
import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.CustFeedbackController;
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.propertymanage.CustExApService;
import com.tgb.service.propertymanage.LrdOrgService;
import com.tgb.util.DateTools;
import com.tgb.util.DicReplace;

import hlc.base.db.DbAccess;
/**
 * 
 * @author hp
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/SCSP")
public class CustExApController  extends BaseController {
	Logger log=Logger.getLogger(CustFeedbackController.class); 

	@Autowired
	private CustExApService custExApService;	
	@Autowired
	private LrdOrgService lrdOrgService;
	
	@Autowired
	private DataSourceTransactionManager txManager;
	
	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询单个审批表信息")
	public String query(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {

			Map<String,Object> list = custExApService.queryByID(map);
			this.setSuccess(list, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	
	

	@ResponseBody
	@RequestMapping(value = "/queryList", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "审查审批查询列表")
	public String queryList(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
		
			User user=(User) request.getSession().getAttribute("user");
			String CUST_GRP_JL = user.getUser_id();
			
			
			/*String role= user.getROLE_CD().toString();
			 
			String maxRole="";
			 
			 
			
				if (role.contains("0000")) {//系统管理员  
					maxRole="0000";
				}else if (role.contains("0003")) {//客户经理
						maxRole="0003";
				}else if (role.contains("0001")) {//支行行长
						maxRole="0001";
				}
			
			
			if ("0001".equals(maxRole)) {//客户经理
				map.put("CUST_GRP_JL", CUST_GRP_JL);
			}else if("0003".equals(maxRole)){//支行经理------将本行的所有客户查询出来
				map.put("ORG_CD", user.getOrgCD()+"");
			}
			*/
			List<Map<String,Object>> roleList = user.getROLE_CD();//权限列表
			String fw="";
			
			for (Map<String, Object> map2 : roleList) {
				fw=map2.get("ROLE_FW")+"";
			}
			
			
			//寻找最大范围
			if (fw.contentEquals("01")) {//可查看所有---部队数据进行处理
				
				
			}else if (fw.contentEquals("02")) {//可查看本级及下级
				
				map.put("ORG_CD", user.getOrgCD()+"");
				
			}else {//查询本级的
				map.put("CUST_GRP_JL", CUST_GRP_JL);
				
			}
			
			
			isPage(map);
			List<Map<String,Object>> list = custExApService.queryList(map);
			if (null!=page) {
				this.page.removeContext();
			}
			Map<String, String> dicMap=new HashedMap();
			dicMap.put("ZYXM", "JTZYXM");
			DicReplace.replaceDic(list, dicMap);
			
			this.setSuccess(list, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "审查审批删除")
	public String delete(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
		
			custExApService.save(map);
			
			
			txManager.commit(status);
			this.setSuccess(null, page);

		} catch (Exception e) {
			
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/ScBxX", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "审查表信息")
	public String ScBxX(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			DbAccess db = new DbAccess();
			String CUST_ID=map.get("CUST_ID")+"";
			//1基本信息表
			String sql = "select s.CUST_ID,s.ORG_NAME,s.CUST_GRP_GL_NAME,s.DATE,s.LOSE_DATE,s.zyxm,s.pj_jb,b.CUST_NAME,b.USER_AGE,b.ID_TYPE,"+
							"b.ID_NO,b.EDU_LEVEL,b.JH_FLAG,b.JH_DATE,b.CHILD_FLAG,b.WORKERS,b.BX_CNT,ifnull(b.JK_JT_STATUS,0) WORK_INFO,ifnull(f.GDZC,0) GDZC,ifnull(f.ZZC,0) ZZC,ifnull(f.ZFZ,0) ZFZ,ifnull(f.ZC_1Y,0) ZC_1Y,ifnull(f.SR_1Y,0) SR_1Y,"+
							"ifnull((f.SR_1Y+f.SR_2Y+f.SR_3Y)/3,0) as SNPJSR,ifnull((f.ZC_1Y+f.ZC_2Y+f.ZC_3Y)/3,0) as SNPJZC,if(s.pj_jb>=8,0,s.ED_LMT) ED_LMT,ifnull(s.CREALIMIT,0) CREALIMIT,"+
							"ifnull(s.CREATERATE,0) CREATERATE ,ifnull(s.QQLIMIT,0)  QQLIMIT ,ifnull(s.QQRATE,0) QQRATE ,b.sex ,REPLACE(pj.BHSIXMOVERDUENUM,'@EMPTY@','') BHSIXMOVERDUENUM ,REPLACE(pj.BHTWOYOVERDUETERM,'@EMPTY@','') BHTWOYOVERDUETERM,REPLACE(pj.ONEYSDAILY,'@EMPTY@','') ONEYSDAILY FROM scsp s "+
							"LEFT JOIN cust_base b on b.CUST_ID=s.CUST_ID "+
							"LEFT JOIN cust_rela r ON r.CUST_ID=s.CUST_ID "+
							"LEFT JOIN fin_rpt f on f.CUST_ID=s.CUST_ID "+
							"LEFT JOIN hn_cust_pj pj on pj.CARDNUM=s.ID_NO "+
							"where s.cust_id='"+CUST_ID+"' LIMIT 1;";
			Map<String, String> result = db.queryForMap(sql);
			Double pjjb=0.0;
			try {
				pjjb = Double.parseDouble(result.get("PJ_JB"));
			} catch (Exception e) {
			}
			
			if(pjjb>=8){
				result.put("ED_LMT", "0");
			}
			String zyxm = result.get("ZYXM");
			if("01".equals(zyxm)||"02".equals(zyxm)){//种植情况
				/*String zzsql="SELECT max((SELECT DATE_FORMAT(SYSDATE(),'%Y')-z.BIZ_DATE FROM DUAL)) as nx ,SUM(z.ZZ_MS) ms,"+
						 "(CASE when sum(r.DTL_NUM)>0 then '是' when sum(r.DTL_NUM)=0 then '否' END)sp "+
						 "from cust_zz z LEFT JOIN `rpt_dtl` r on r.CUST_ID=z.CUST_ID "+
						 "where (r.SUBJ_TYPE='03' or r.SUBJ_TYPE='08') and z.CUST_ID='"+CUST_ID+"';";*/
				
				Map<String, String> zz=new HashedMap();
				if ("01".equals(zyxm)) {//种植蔬菜  作物小于27 为蔬菜
					String zzsql="SELECT MAX((SELECT DATE_FORMAT(SYSDATE(),'%Y')-z.BIZ_DATE FROM DUAL))  nx ,SUM(z.ZZ_MS) ms "+
								"from cust_zz z where z.ZW_TYPE in('01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','24') and z.CUST_ID='"+CUST_ID+"';";
					zz = db.queryForMap(zzsql);
				}else if ("02".equals(zyxm)) {//种植其他
					String zzsql="SELECT MAX((SELECT DATE_FORMAT(SYSDATE(),'%Y')-z.BIZ_DATE FROM DUAL))  nx ,SUM(z.ZZ_MS) ms "+
							"from cust_zz z where z.ZW_TYPE not in('01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','24') and z.CUST_ID='"+CUST_ID+"';";
					zz = db.queryForMap(zzsql);
					
					String spf="SELECT COUNT(*) sp from rpt_dtl  where (SUBJ_TYPE='03' or SUBJ_TYPE='08') and CUST_ID='"+CUST_ID+"';";

					Map<String, String> spmap = db.queryForMap(spf);
					
					if ("0".equals(spmap.get("sp"))) {
						zz.put("sp", "否");
					}else {
						zz.put("sp", "是");
					}
				}
				
				
				
				result.putAll(zz);
			}else if("03".equals(zyxm)||"04".equals(zyxm)){//养殖
				
				String yzsql="SELECT y.CL_SJ, (SELECT DATE_FORMAT(SYSDATE(),'%Y')-y.BIZ_DATE FROM DUAL) nx FROM `cust_yz` y "+
							"where y.cust_id='"+CUST_ID+"';";
				Map<String, String> yz = db.queryForMap(yzsql);
				result.putAll(yz);

			}else if("05".equals(zyxm)){//职工
				String zgsql="SELECT DUTY,(SELECT DATE_FORMAT(SYSDATE(),'%Y')-WORK_DATE FROM DUAL) nx,YLBXAMT from cust_zg "+
						"where cust_id='"+CUST_ID+"';";
				Map<String, String> zg = db.queryForMap(zgsql);
				result.putAll(zg);
			}else if ("11".equals(zyxm)) {//打工
				String dgsql="SELECT  ifnull(DG_YEARS,0) nx from cust_dg "+
						"where cust_id='"+CUST_ID+"';";
				Map<String, String> dg = db.queryForMap(dgsql);
				result.putAll(dg);
			}else{//经商
				if ("09".equals(zyxm)) {//餐饮及住宿
					//经营场所面积总 员工总数量     上年度经营收入     上年度经营支出
					String zgsql="SELECT (RENT_AREA+OWN_AREA) zmj,INCOME_Y,(INCOME_Y-PROFIT_Y) zc,BIZ_YEARS nx from cust_js "+
							"where cust_id='"+CUST_ID+"';";
					Map<String, String> dg = db.queryForMap(zgsql);
					result.putAll(dg);
				}else if ("10".equals(zyxm)) {//其他行业
					//自有房产数量（住宅和商铺）  存货
					String zgsql="SELECT ifnull(SUM(r.DTL_NUM),0) dtl_num,ifnull(f.CK,0) CK from rpt_dtl r LEFT JOIN fin_rpt  f "
							+ "on r.CUST_ID=f.CUST_ID where r.SUBJ_CD='A0005' and (r.DTL_TYPE='03' or r.DTL_TYPE='04') "+
					" and r.cust_id='"+CUST_ID+"';";
					Map<String, String> dg = db.queryForMap(zgsql);
					result.putAll(dg);
				}else if ("06".equals(zyxm)){//制造业
					//经营场所 建筑面积（自有） 员工数量   有人身意外上海员工数量
					String zgsql="SELECT SUM(OWN_AREA) OWN_AREA,INCOME_Y,(INCOME_Y-PROFIT_Y) zc FROM cust_js "+
							"where cust_id='"+CUST_ID+"';";
					Map<String, String> dg = db.queryForMap(zgsql);
					result.putAll(dg);
				}else if ("07".equals(zyxm)) {//运输
					//运营车辆数量          上年度经营收入        上年度经营支出
					String zgsql="SELECT SUM(DTL_NUM) DTL_NUM,j.INCOME_Y,(j.INCOME_Y-j.PROFIT_Y) zc from rpt_dtl r "+
									"LEFT JOIN cust_js j on r.CUST_ID=j.CUST_ID "+
									"where r.SUBJ_CD='A0005' and (r.SUBJ_TYPE='08' or r.SUBJ_TYPE='09') "
									+ "and r.cust_id='"+CUST_ID+"';";
					Map<String, String> dg = db.queryForMap(zgsql);
					result.putAll(dg);
				}else if ("08".equals(zyxm)) {//批发零售
					//上年度经营项目收入        上年度经营项目支出
					String zgsql="SELECT INCOME_Y,(INCOME_Y-PROFIT_Y) zc from cust_js "+
							"where cust_id='"+CUST_ID+"';";
					Map<String, String> dg = db.queryForMap(zgsql);
					result.putAll(dg);
				} 
				
				//年限
				String nxsql="SELECT ifnull(max(BIZ_DATE),0) nx from cust_js "+
						"where cust_id='"+CUST_ID+"';";
				Map<String, String>  tmp=db.queryForMap(nxsql);
				Map<String, String>  nx= new HashMap<String,String>();
				nx.put("nx", String.valueOf(DateTools.getCurrentYear()-Integer.valueOf(tmp.get("nx"))));
				result.putAll(nx);
				
			}
			//字典查询
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			Map<String, Object> resultmap=new HashedMap();
			Set<Entry<String,String>> entrySet = result.entrySet();
			for (Entry<String, String> entry : entrySet) {
				resultmap.put(entry.getKey(), entry.getValue());
			}
			
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("EDU_LEVEL","EDU_LEVEL");
			dicMap.put("ID_TYPE", "ID_TYPE");
			dicMap.put("JH_FLAG", "JH_FLAG");
			dicMap.put("WORK_INFO", "JK_JT_STATUS");
			dicMap.put("SEX", "SEX");
			dicMap.put("DUTY", "ZGZW");
			list.add(resultmap);
			DicReplace.replaceDic(list, dicMap);
			this.setSuccess(list, page);
			} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/queryHn", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询行内数据")
	public String queryHn(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		//TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			HnData hn = new HnData();
			DbAccess db =new DbAccess();
			Map<String,String> m1 = db.queryForMap("select CUST_NAME,CUST_ID from cust_base where id_no='"+map.get("ID_NO").toString()+"'");
			JSONObject pjJsonList= hn.getCustPj(map.get("ID_NO").toString(),m1.get("CUST_NAME"));
			//List<Map<String,Object>> queryHnByYf = custExApService.queryHnByYf(map);
			Map<String, Object> result=new HashedMap();
			//for (Map<String, Object> map2 : queryHnByYf) {
				result.put("ID_NO",map.get("ID_NO"));
				result.put("CUST_ID",m1.get("CUST_ID") );
				result.put("CUST_NAME",m1.get("CUST_NAME") );
				
				if ("6".equals(map.get("YF")+"")) {
					result.put("BHVERDUENUM","@EMPTY@".equals(pjJsonList.get("BHSIXMOVERDUENUM"))?"暂无":pjJsonList.get("BHSIXMOVERDUENUM"));
					result.put("BHVERDUETERM","@EMPTY@".equals(pjJsonList.get("SIXMSDAILY"))?"暂无":pjJsonList.get("SIXMSDAILY"));
					
				}else if ("12".equals(map.get("YF")+"")) {
					result.put("BHVERDUENUM","@EMPTY@".equals(pjJsonList.get("BHONEYOVERDUENUM"))?"暂无":pjJsonList.get("BHONEYOVERDUENUM"));
					result.put("BHVERDUETERM","@EMPTY@".equals(pjJsonList.get("ONEYSDAILY"))?"暂无":pjJsonList.get("ONEYSDAILY"));		
				}else if ("24".equals(map.get("YF")+"")) {
					result.put("BHVERDUENUM","@EMPTY@".equals(pjJsonList.get("BHTWOYOVERDUENUM"))?"暂无":pjJsonList.get("BHTWOYOVERDUENUM"));
					result.put("BHVERDUETERM","@EMPTY@".equals(pjJsonList.get("BHTWOYOVERDUETERM"))?"暂无":pjJsonList.get("BHTWOYOVERDUETERM"));
				}
			//}
			this.setSuccess(result, page);
			
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/queryBatch", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "批量日志列表")
	public String queryBatch(HttpServletRequest request) {
		
		Map<String, Object> map=new HashedMap();
		log.info("接收的JSON:"+map.toString());
		try {
			isPage(map);
			List<Map<String,Object>> result=custExApService.queryBatch(map);
			if (null!=page) {
				this.page.removeContext();
			}
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("BATCH_TYPE", "BATCH_TYPE");
			DicReplace.replaceDic(result, dicMap);
			this.setSuccess(result, page);
			
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/Dcb", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "调查表")
	public String Dcb(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			DbAccess db = new DbAccess();
			String CUST_ID=map.get("CUST_ID")+"";
			String sql="SELECT o.ORG_NAME ,u.USER_NAME,b.CUST_ID,b.CUST_NAME,b.CUST_TYPE,b.USER_AGE,b.HEALTHY,b.ADDRESS,b.ADDRESS XADDRESS,b.TEL_NO, "+
			"ifnull(f.GDZC,0) GDZC,ifnull(f.ZZC,0) ZZC,ifnull(f.ZFZ,0) ZFZ,ifnull(SR_1Y-f.ZC_1Y,0) CSR_1Y,ifnull(f.SR_1Y,0) SR_1Y, "+
			"ifnull(f.SR_2Y-f.ZC_2Y,0) CSR_2Y,ifnull(f.SR_2Y,0) SR_2Y,ifnull(f.SR_3Y-f.ZC_3Y,0) CSR_3Y,ifnull(f.SR_3Y,0) SR_3Y, "+
			"ifnull((f.SR_1Y+f.SR_2Y+f.SR_3Y)/3,0) as SNPJSR,(ifnull((f.SR_1Y+f.SR_2Y+f.SR_3Y)/3,0)-ifnull((f.ZC_1Y+f.ZC_2Y+f.ZC_3Y)/3,0)) as SNPJCSR,(COUNT(r.ID_NO)+1) JTRK,  "+
			"IF(b.JH_FLAG=2,'夫妻','自己') ZYLDL,(f.dqdk+f.cqdk) BKFZ,ifnull(f.QTFZ,0) QTFZ,bk.XQED,zg.WORK_NAME,zg.DUTY,IF(b.CUST_TYPE='GTGS',ADDRESS,'') JYDZ, "+
			"js.OWN_AREA,js.RENT_AREA "+
			"FROM cust_base b "+
			"LEFT JOIN LRD_USER u ON u.USER_ID=b.CUST_GRP_JL "+
			"LEFT JOIN lrd_org o on o.ORG_CD=u.ORG_CD "+
			"LEFT JOIN fin_rpt f on f.CUST_ID=b.CUST_ID "+
			"LEFT JOIN cust_rela r on r.CUST_ID=b.CUST_ID "+
			"LEFT JOIN cust_feedback bk on  bk.CUST_ID=b.CUST_ID "+
			"LEFT JOIN cust_zg zg on zg.CUST_ID=b.CUST_ID "+
			"LEFT JOIN cust_js js on js.CUST_ID=b.CUST_ID "+
			"WHERE B.CUST_ID='"+CUST_ID+"' ";
			Map<String, String> forMap = db.queryForMap(sql);
			
			String MX="SELECT l.DIC_NAME,DTL_NUM,r.SUBJ_VAL from rpt_dtl r, lrd_dic l "+
						"WHERE DIC_PARENTID='GDZC' AND DIC_ID=SUBJ_TYPE and SUBJ_CD='A0005' and r.CUST_ID='"+CUST_ID+"'";
			List<Map<String, String>> mxlist = db.queryForList(MX);
			
			List<String> dgzcmx=new ArrayList<String>();
			for (Map<String, String> map2 : mxlist) {
				String name=map2.get("DIC_NAME")+"";
				String val=map2.get("SUBJ_VAL")+"";
				String num=map2.get("DTL_NUM")+"";
				
				String str="资产种类:"+name+",数量:"+num+",价值(万):"+val;
				dgzcmx.add(str);
				
			}
			
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			Map<String, Object> resultmap=new HashedMap();
			Set<Entry<String,String>> entrySet = forMap.entrySet();
			for (Entry<String, String> entry : entrySet) {
				resultmap.put(entry.getKey(), entry.getValue());
			}
			resultmap.put("DGZCMX", dgzcmx);
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("CUST_TYPE","Cust_Type");
			dicMap.put("HEALTHY","HEALTHY");
			list.add(resultmap);
			DicReplace.replaceDic(list, dicMap);
			this.setSuccess(list, page);
			} catch (Exception e) {
			this.setErr(page, e.getMessage());
		
			}
			return this.js.toString();
	}
}
