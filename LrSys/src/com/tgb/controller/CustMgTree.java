package com.tgb.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.cust.CustBaseService;
import com.tgb.service.propertymanage.LrdOrgService;
import com.tgb.service.propertymanage.LrdRoleService;
import com.tgb.service.propertymanage.LrdUserService;
import com.tgb.service.tree.CustMgTreeService;
import com.tgb.util.DicReplace;
import com.tgb.util.StringUtils;
/**
 * 
 * @author hp
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/custmgtree")
public class CustMgTree extends BaseController {
	Logger log=Logger.getLogger(CustApplyController.class); 
	
	
	@Autowired
	private LrdRoleService lrdRoleService;
	@Autowired
	private LrdOrgService lrdOrgService;
	@Autowired
	private LrdUserService lrdUserService;
	@Autowired
	private CustMgTreeService custMgTreeService;
	
	@Autowired
	private DataSourceTransactionManager txManager;
	
	@ResponseBody
	@RequestMapping(value = "/queryTree", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户-待审核信息")
	public String queryTree(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		log.info("接收的JSON:"+map.toString());
		try {
			User user = (User) request.getSession().getAttribute("user");
			map.put("USER_ID", user.getUser_id());
			List<Map<String, Object>> userRole = lrdRoleService.queryUserRole(map);//获取用户权限
			Map<String, Object> maxRole=new HashMap<String, Object>();
			for (Map<String, Object> map2 : userRole) {
				if(map2.get("ROLE_NAME").equals("系统管理员")||map2.get("ROLE_NAME").equals("总行用户")){
					maxRole.put("ROLE_NAME", "系统管理员");
				}
				if(map2.get("ROLE_NAME").equals("支行行长")){
					if("系统管理员".equals(maxRole.get("ROLE_NAME"))){
						break;
					}
					maxRole.put("ROLE_NAME", "支行行长");
				}
				if(map2.get("ROLE_NAME").equals("客户经理")){
					if("系统管理员".equals(maxRole.get("ROLE_NAME"))||"支行经理".equals(maxRole.get("ROLE_NAME"))){
						break;
					}
					maxRole.put("ROLE_NAME", "客户经理");
				}
				
				
			}
			List<Map<String,Object>> result =new ArrayList<Map<String,Object>>();
			if("系统管理员".equals(maxRole.get("ROLE_NAME"))){
				//机构
				
					List<Map<String,Object>> queryMenu = custMgTreeService.queryAllMenu(map);
					result.addAll(queryMenu);
				for (Map<String, Object> map3 : queryMenu) {
					map3.put("ZH_ID", map3.get("id"));
					List<Map<String, Object>> listJL = custMgTreeService.queryJL(map3);//经理列表个个机构
					for (Map<String, Object> map2 : listJL) {
						map2.put("pId", map3.get("id"));
						map2.put("JL_ID", map2.get("id"));
						map2.put("ZH_ID", map3.get("id"));
						result.add(map2);
					}
					//map3.put("listJL", listJL);
					for (Map<String, Object> map4 : listJL) {
						List<Map<String,Object>> area = custMgTreeService.queryAreaByCustJl(map4);
						for (Map<String, Object> map5 : area) {
							map5.put("pId", map4.get("id"));
							map5.put("C_ID", map5.get("id"));
							map5.put("JL_ID", map4.get("id"));
							map5.put("ZH_ID", map3.get("id"));
							result.add(map5);
						}
						//map4.put("area", area);
					}	
				}
			}
			if("支行行长".equals(maxRole.get("ROLE_NAME"))){
				Map<String, Object> queryUserOrg = lrdOrgService.queryUserOrg(map);
				map.put("ORG_PCD", queryUserOrg.get("ORG_CD")+"");
				List<Map<String,Object>> list = lrdOrgService.queryMenu(map);
				list.add(queryUserOrg);
			
				for (Map<String, Object> map3 : list) {
					map3.put("id", map3.get("ORG_CD"));
					List<Map<String, Object>> listJL = custMgTreeService.queryJL(map3);//经理列表个个机构
					for (Map<String, Object> map2 : listJL) {
						map2.put("pId", map3.get("id"));
						map2.put("JL_ID", map2.get("id"));
						map2.put("ZH_ID", map3.get("id"));
						result.add(map2);
					}
					//map3.put("listJL", listJL);
					for (Map<String, Object> map4 : listJL) {
						List<Map<String,Object>> area = custMgTreeService.queryAreaByCustJl(map4);
						for (Map<String, Object> map5 : area) {
							map5.put("pId", map4.get("id"));
							map5.put("C_ID", map5.get("id"));
							map5.put("JL_ID", map4.get("id"));
							map5.put("ZH_ID", map3.get("id"));
							result.add(map5);
						}
						//map4.put("area", area);
					}	
				}
				
				//result.add(queryUserOrg);
			}
			if("客户经理".equals(maxRole.get("ROLE_NAME"))){
				Map<String, Object> queryUserOrg = lrdOrgService.queryUserOrg(map);
				map.put("id", user.getUser_id());
				List<Map<String,Object>> area = custMgTreeService.queryAreaByCustJl(map);
				for (Map<String, Object> map5 : area) {
					map5.put("pId", queryUserOrg.get("id"));
					map5.put("C_ID", map5.get("id"));
					result.add(map5);
				}
				//queryUserOrg.put("area", area);
				//result.add(queryUserOrg);
			}
			this.setSuccess(result, page);

		} catch (Exception e) {
			e.printStackTrace();
			this.setErr(page, e.getLocalizedMessage());
		}
		return this.js.toString();
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/queryList", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户信息")
	public String queryList(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("查询客户信息接收的JSON:"+map.toString());
		try {
			User user = (User) request.getSession().getAttribute("user");
			map.put("USER_ID", user.getUser_id());
			List<Map<String, Object>> userRole = lrdRoleService.queryUserRole(map);//获取用户权限
			/*Map<String, Object> maxRole=new HashMap<String, Object>();
			for (Map<String, Object> map2 : userRole) {
				if(map2.get("ROLE_NAME").equals("系统管理员")||map2.get("ROLE_NAME").equals("总行用户")){
					maxRole.put("ROLE_NAME", "系统管理员");
				}
				if(map2.get("ROLE_NAME").equals("支行行长")){
					if("系统管理员".equals(maxRole.get("ROLE_NAME"))){
						break;
					}
					maxRole.put("ROLE_NAME", "支行行长");
				}
				if(map2.get("ROLE_NAME").equals("客户经理")){
					if("系统管理员".equals(maxRole.get("ROLE_NAME"))||"支行经理".equals(maxRole.get("ROLE_NAME"))){
						break;
					}
					maxRole.put("ROLE_NAME", "客户经理");
				}
			}*/
			
			
			List<Map<String,Object>> roleList = user.getROLE_CD();//权限列表
			String fw="";
			
			for (Map<String, Object> map2 : roleList) {
				fw=map2.get("ROLE_FW")+"";
			}
			
			
			//寻找最大范围
			if (fw.contentEquals("01")) {//可查看所有---不对数据进行处理
				
				
			}else if (fw.contentEquals("02")) {//可查看本级及下级
				
				map.put("ZH_ID",user.getOrgCD());
				
			}else {//查询本级的
				map.put("ZH_ID",user.getOrgCD());
				map.put("JL_ID",user.getUser_id());
				
			}
			/*List<Map<String,Object>> result =new ArrayList<Map<String,Object>>();
			Map<String, Object> userOrg = lrdOrgService.queryUserOrg(map);
			if("支行行长".equals(maxRole.get("ROLE_NAME"))){
				
			}
			if("客户经理".equals(maxRole.get("ROLE_NAME"))){
				
			}*/
			
			//去除评级级别为0情况
			if(map.get("PJ_JB") != null && map.get("PJ_JB").equals("0.0")){
				log.info("请注意，不存在评级结果为0的情况");
				map.remove("PJ_JB");
			}
			//将授信区间数值转化为decimal
			if(map.get("ED_LMT_START") != null && !map.get("ED_LMT_START").equals("")){
				String strStart = (String) map.get("ED_LMT_START");
				BigDecimal bdStart = new BigDecimal(strStart);
				bdStart = bdStart.multiply(new BigDecimal(10000));
				map.put("ED_LMT_START", bdStart);
			}
			if(map.get("ED_LMT_END") != null && !map.get("ED_LMT_END").equals("")){
				String strEnd = (String) map.get("ED_LMT_END");
				BigDecimal bdEnd = new BigDecimal(strEnd);
				bdEnd = bdEnd.multiply(new BigDecimal(10000));
				map.put("ED_LMT_END", bdEnd);
			}
			//判断是否涉及拜访
			if ((map.get("BF_CRT_DATE_START") != null && !map.get("BF_CRT_DATE_START").equals(""))
					|| (map.get("BF_CRT_DATE_END") != null && !map.get("BF_CRT_DATE_END").equals(""))
					|| (map.get("BF_COUNT") != null && !map.get("BF_COUNT").equals(""))
					|| (map.get("ISHF") != null && !map.get("ISHF").equals(""))
					|| (map.get("HF_DATE_START") != null && !map.get("HF_DATE_START").equals(""))
					|| (map.get("HF_DATE_END") != null && !map.get("HF_DATE_END").equals(""))
					|| (map.get("HF_RESULT") != null && !map.get("HF_RESULT").equals(""))) {
				if(map.get("BF_COUNT").equals("0")){
					// 当拜访次数为0时
					// 此时需要查询出在cust_feedback中没有state="bf"的记录以及在cust_feedback中
					// 没有关联的记录
					// 0表示未拜访
					map.put("ISBF", "0");
				}else{
					map.put("BF", "true");

					if ((map.get("BF_CRT_DATE_END") == null || map.get("BF_CRT_DATE_END").equals(""))
							&& (map.get("BF_COUNT") != null && !map.get("BF_COUNT").equals(""))) {
						// 默认日期小于等于当前日期，为了拜访次数查询结果
						map.put("BF_CRT_DATE_END", getCurrentDate());
					}
					
					// 回访
					map.put("HF_COUNT", "0");
				}
				
			}
			/*// 当拜访次数为0时
			if (map.get("BF_COUNT") != null && map.get("BF_COUNT").equals("0")) {
				// 此时需要查询出在cust_feedback中没有state="bf"的记录以及在cust_feedback中
				// 没有关联的记录
				// 0表示未拜访
				map.put("ISBF", "0");
			}*/
			
			
			isPage(map);
			List<Map<String, Object>> resultList =custMgTreeService.queryList(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			if (null != page) {
				this.page.removeContext();
			}
			dicMap.put("CUST_TYPE", "CUST_TYPE");
			dicMap.put("JL_TYPE", "JL_TYPE");
			dicMap.put("CUST_GRP", "CUST_GRP1");
			// dicMap.put("CUST_GRP2", "CUST_GRP2");
			dicMap.put("AREA_CD", "AREA_CD1");
			dicMap.put("CREATE_TYPE", "ZTBJ");
			//字典项对应的数值
			DicReplace.replaceDic(resultList, dicMap);
			//二级
			DicReplace.replaceChildDic(resultList, "CUST_GRP", "CUST_GRP2");
			DicReplace.replaceChildDic(resultList, "AREA_CD", "AREA_CD2");
			this.setSuccess(resultList, page);
		} catch (Exception e) {
			e.printStackTrace();
			this.setErr(page, e.getLocalizedMessage());
		}
		return this.js.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/querybylike", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "模糊查询客户基本信息")
	public String query(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {

		log.info("接收的JOSN：" + map.toString());
		Object telNo = null;
		Object idNO = null;
		Object custName = null;
		String num = "\\d*";
		String reg = "^((13[0-9])|(15[0-9])|(18[0,5-9]))\\d*$";
		isPage(map);
		Object obj = map.get("keyword");

		try {
			User user = (User) request.getSession().getAttribute("user");
			map.put("USER_ID", user.getUser_id());
			if (((String) obj).matches(reg)) {
				telNo = String.valueOf(obj);
			} else if (((String) obj).matches(num)) {
				idNO = String.valueOf(obj);
			} else {
				custName = obj.toString();
			}
			map.put("CUST_GRP_JL", user.getUser_id());
			map.put("TEL_NO", telNo);
			map.put("ID_NO", idNO);
			map.put("CUST_NAME", custName);
			List<Map<String, Object>> resultList = custMgTreeService
					.queryByLike(map);

			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("CUST_TYPE", "CUST_TYPE");
			dicMap.put("JL_TYPE", "JL_TYPE");
			DicReplace.replaceDic(resultList, dicMap);
			for(Map<String, Object> tmpMap:resultList){
				String custGrpJl = tmpMap.get("CUST_GRP_JL").toString();
				if(!user.getUser_id().equals(custGrpJl)){
					//替换电话中间四位为*
					tmpMap.put("OTHER", "TRUE");
					String custType = tmpMap.get("CUST_TYPE").toString();
					if("XWQY".equals(custType)){
						String telNo1 = tmpMap.get("LXR_TEL").toString();
						tmpMap.put("LXR_TEL", StringUtils.userNameReplaceWithStar(telNo1));
						
					}else{
						String telNo1 = tmpMap.get("TEL_NO").toString();
						tmpMap.put("TEL_NO", StringUtils.userNameReplaceWithStar(telNo1));
					}
					
				}
			}
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/queryTreebyJl", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "通过客户经理查询村")
	public String queryTreebyJl(HttpServletRequest request) {
		log.info("接收的JOSN：");
		try {
			
			User user=(User) request.getSession().getAttribute("user");
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("id", user.getUser_id());
			List<Map<String,Object>> resultList = custMgTreeService.queryAreaByCustJl(map);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/filterQuery", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "按需求查询客户信息")
	public String filterQuery(@RequestBody Map<String, Object> map,HttpServletRequest request){
		log.info("按需求查询客户信息接收的JSON:"+map.toString());
		try {
			User user = (User) request.getSession().getAttribute("user");
			map.put("USER_ID", user.getUser_id());
			List<Map<String, Object>> userRole = lrdRoleService.queryUserRole(map);//获取用户权限
			List<Map<String,Object>> roleList = user.getROLE_CD();//权限列表
			String fw="";
			
			for (Map<String, Object> map2 : roleList) {
				fw=map2.get("ROLE_FW")+"";
			}
			
			
			//寻找最大范围
			if (fw.contentEquals("01")) {//可查看所有---不对数据进行处理
				
				
			}else if (fw.contentEquals("02")) {//可查看本级及下级
				
				map.put("ZH_ID",user.getOrgCD());
				
			}else {//查询本级的
				map.put("ZH_ID",user.getOrgCD());
				map.put("JL_ID",user.getUser_id());
				
			}
			//默认用“录入”类信息，当涉及农资用户时，用“拜访”类信息
			map.put("TYPE", "LR");
			//筛选条件
			String selectItem = (String) map.get("selectItem");
			if(selectItem.equals("XQED")){ 
				map.put("XQED", "1");
			}else if(selectItem.equals("SCYH_DESC")){
				map.put("SCYH_DESC", "1");
			}else if(selectItem.equals("SH_DESC")){
				map.put("SH_DESC", "1");
			}else if(selectItem.equals("NZYH_DESC")){
				map.put("NZYH_DESC", "1");
				map.put("TYPE", "BF");
			}else if(selectItem.equals("BANK_CD")){
				map.put("BANK_CD", "1");
			}else if(selectItem.equals("DJ_DESC")){
				map.put("DJ_DESC", "1");
			}else if(selectItem.equals("CK_DESC")){
				map.put("CK_DESC", "1");
			}else if(selectItem.equals("LC_DESC")){ 
				map.put("LC_DESC", "1");
			}else if(selectItem.equals("DZ_DESC")){
				map.put("DZ_DESC", "1");
			}else if(selectItem.equals("POS_DESC")){
				map.put("POS_DESC", "1");
			}else if(selectItem.equals("WH_DESC")){
				map.put("WH_DESC", "1");
			}else if(selectItem.equals("DFGZ_DESC")){
				map.put("DFGZ_DESC", "1");
			}
			isPage(map);
			List<Map<String, Object>> resultList =custMgTreeService.filterQuery(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			if (null != page) {
				this.page.removeContext();
			}
			dicMap.put("CUST_TYPE", "CUST_TYPE");
			dicMap.put("JL_TYPE", "JL_TYPE");
			dicMap.put("CUST_GRP", "CUST_GRP1");
			// dicMap.put("CUST_GRP2", "CUST_GRP2");
			dicMap.put("AREA_CD", "AREA_CD1");
			dicMap.put("CREATE_TYPE", "ZTBJ");
			//字典项对应的数值
			DicReplace.replaceDic(resultList, dicMap);
			DicReplace.replaceChildDic(resultList, "CUST_GRP", "CUST_GRP2");
			DicReplace.replaceChildDic(resultList, "AREA_CD", "AREA_CD2");
			this.setSuccess(resultList, page);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.setErr(page, e.getLocalizedMessage());
		}
		return this.js.toString();
	}
			
	public String getCurrentDate(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		//System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		return df.format(new Date());
	}
	
}


