package com.tgb.controller.propertymanage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.propertymanage.LrdOrgService;
import com.tgb.service.propertymanage.LrdRoleService;
import com.tgb.service.propertymanage.LrdUserService;
import com.tgb.util.DateTools;
import com.tgb.util.MD5;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/lrduser")
public class LrdUserController extends BaseController{

	
	Logger log = Logger.getLogger(LrdUserController.class);
	@Autowired
	private LrdUserService lrdUserService; 
	@Autowired
	private LrdOrgService lrdOrgService;
	@Autowired
	private LrdRoleService lrdRoleService;
	
	@Autowired
	private DataSourceTransactionManager txManager;

	
	@ResponseBody
	@RequestMapping(value = "/querylist", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询操作员")
	public String queryTree(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			
			Object userId = map.get("USER_ID");
			Object orgcd = map.get("ORG_CD");
			Map<String,Object> resultmap=new HashMap<String, Object>();
			
			//根据登录用权限查询机构菜单
			User user = (User) request.getSession().getAttribute("user");

			map.put("USER_ID", user.getUser_id());
			Map<String, Object> userOrg = lrdOrgService.queryUserOrg(map);
			String orgCD = userOrg.get("ORG_CD").toString();
			List<Map<String,Object>> userRole = lrdRoleService.queryUserRole(map);
			for (Map<String, Object> map2 : userRole) {
				String roleFw = map2.get("ROLE_FW").toString();
				//本级
				if(roleFw.equals("00")){
					if(map.get("ROLE_FW")=="01"||map.get("ROLE_FW")=="02")continue;
					map.put("ROLE_FW","00");
				}else if(roleFw.equals("02")){ //本级及下级
					map.put("ROLE_FW","02");
				}else if(roleFw.equals("01")){//本级及所有下级
					if(map.get("ROLE_FW")=="02")continue;
					map.put("ROLE_FW","01");
				}
			}
			if(null!=orgcd && !"".equals(orgcd)){
				
			}else{
				map.put("ORG_CD", orgCD);
			}
			
			List<Map<String, Object>> resulOrg = lrdOrgService.queryMenu(map);
			//查询当前登录机构拥有的角色
			//List<String> ordIds=new ArrayList<String>();
			String orgIds="";//如果是支行，获取所有下属机构org_cd
			for (Map<String, Object> map2 : resulOrg) {
				String orgId = map2.get("id").toString();
				orgIds=orgIds+""+orgId+",";
			}
			if(orgcd!=null&&orgcd!=""){
				orgIds=orgIds+""+orgcd+",";
			}
			orgIds=orgIds.substring(0,orgIds.length()-1);
			map.remove("ORG_CD");
			map.put("ordIds", "("+orgIds+")");
			List<Map<String, Object>> resultRole = lrdRoleService.queryList(map);
			//
			map.remove("USER_ID");
			if(userId!=null)map.put("USER_ID", userId.toString());
			
			isPage(map);
			List<Map<String, Object>> resultList = lrdUserService.queryList(map);//支行下客户经理汇总
			if (null != page) {
				this.page.removeContext();
			}
			for (Map<String, Object> map2 : resultList) {
				List<Map<String, Object>> userRoles = lrdUserService.queryUserRole(map2);
				if(map2.get("ORG_NAME")==null){
					map2.put("ORG_NAME", "");
				}
				String name="";
				for (Map<String, Object> map3 : userRoles) {
					String value = map3.get("ROLE_NAME").toString();
					name=name+value+",";
				}
				if(name.length()>0){
					name=name.substring(0,name.length()-1);
				}
				map2.put("name",name);
			}
			resultmap.put("ORG", resulOrg);
			resultmap.put("ROLE", resultRole);
			resultmap.put("result", resultList);
			
			
			this.setSuccess(resultmap, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}


	
	@ResponseBody
	@RequestMapping(value = "/updatePwd", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "修改密码")
	public String updatePwd(@RequestBody Map<String,Object> map, HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		
		try {
			String oldpwd = MD5.getMD5(map.get("OLD_PASSWORD").toString());
			String newpwd = MD5.getMD5(map.get("NEW_PASSWORD").toString());
			map.put("CORP_CD", "907");
			Map<String, Object> resultMap = lrdUserService.queryMap(map);

			if (null != resultMap && !resultMap.isEmpty()) {
					String resPwd = resultMap.get("PASSWORD").toString();
					// 验证密码
					if (oldpwd.equals(resPwd)) {
						// 密码正确
						map.put("PW_MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
						map.put("PASSWORD", newpwd);
						lrdUserService.update(map);
						this.setSuccess(resultMap, null);
					} else {
						this.setErr(null, "密码错误");
					}
				} 
	
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();

	}
	
	@ResponseBody
	@RequestMapping(value = "/resetPwd", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "重置密码")
	public String resetPwd(@RequestBody Map<String,Object> map, HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			map.put("CORP_CD", "907");
			String newpwd = MD5.getMD5("11111111");
			map.put("PW_MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
			map.put("PASSWORD", newpwd);
			map.put("PW_MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
			map.put("PASSWORD", newpwd);
			boolean update = lrdUserService.update(map);
			
			this.setSuccess(update, null);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();

	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除操作员")
	public String delete(@RequestBody Map<String,Object> map, HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			
			boolean delete = lrdUserService.delete(map);
			this.setSuccess(delete, null);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryinsert", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "添加操作员--查询")
	public String queryinsert( HttpServletRequest request) {
		log.info("接收的JSON:" );
		try {
			Map<String,Object> map=new HashMap<String, Object>();
			Map<String,Object> resultmap=new HashMap<String, Object>();
			//查询当前登录用户的拥有机构列表
			User user = (User) request.getSession().getAttribute("user");
			map.put("USER_ID", user.getUser_id());
			Map<String, Object> userOrg = lrdOrgService.queryUserOrg(map);
			String orgCD = userOrg.get("ORG_CD").toString();
			List<Map<String,Object>> userRole = lrdRoleService.queryUserRole(map);
			for (Map<String, Object> map2 : userRole) {
				String roleFw = map2.get("ROLE_FW").toString();
				//本级
				if(roleFw.equals("00")){
					if(map.get("ROLE_FW")=="01"||map.get("ROLE_FW")=="02")continue;
					map.put("ROLE_FW","00");
				}else if(roleFw.equals("02")){ //本级及下级
					map.put("ROLE_FW","02");
				}else if(roleFw.equals("01")){//本级及所有下级
					if(map.get("ROLE_FW")=="02")continue;
					map.put("ROLE_FW","01");
				}
			}
			map.put("ORG_CD", orgCD);
			List<Map<String, Object>> resultList = lrdOrgService.queryMenu(map);
			//查询当前登录机构拥有的角色
			String orgIds="";
			for (Map<String, Object> map2 : resultList) {
				String orgId = map2.get("id").toString();
				orgIds=orgIds+""+orgId+",";
			}

			orgIds=orgIds.substring(0,orgIds.length()-1);
			map.remove("ORG_CD");
			map.put("ordIds", "("+orgIds+")");
			List<Map<String, Object>> resultRole = lrdRoleService.queryList(map);
			resultmap.put("ORG", resultList);
			resultmap.put("ROLE", resultRole);
			
			
			this.setSuccess(resultmap, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
	}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/insert", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "添加操作员--添加")
	public String insert(@RequestBody Map<String,Object> map, HttpServletRequest request) {
		TransactionStatus status = this.getTransactionStatus(txManager);
		log.info("接收的JSON:" + map.toString());
		try {
			SimpleDateFormat format=new SimpleDateFormat("yyMMdd");
			map.put("CRT_DATE", format.format(new Date()));
			map.put("MTN_DATE", format.format(new Date()));
			String newpwd = MD5.getMD5("11111111");
			map.put("PASSWORD", newpwd);
			map.put("PW_MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
			lrdUserService.save(map);
			
			//先删除再插入
			boolean delete = lrdUserService.deleteByUser(map);
			String str = map.get("role_id").toString();
			List<String> result = Arrays.asList(StringUtils.split(str,","));  
			for(int i=0;i<result.size();i++){
				map.put("ROLEID", result.get(i));
				lrdUserService.saveRole(map);
			}
			txManager.commit(status);
			
			this.setSuccess(null, page);
		} catch (Exception e) {
			txManager.rollback(status);
			System.err.println(e.getCause().toString()+"9999999999999999");
			
			if(e.getCause() instanceof MySQLIntegrityConstraintViolationException){
				this.setErr(page, "用户编号已存在！");
			}else{
				this.setErr(page, e.getMessage());
			}	
		}
		return this.js.toString();
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/queryupdate", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "修改操作员--查询")
	public String queryUpdate(@RequestBody Map<String,Object> map, HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			String USER_ID = map.get("USER_ID").toString();
			Map<String,Object> resultmap=new HashMap<String, Object>();
			//查询当前登录用户的拥有机构列表
			User user = (User) request.getSession().getAttribute("user");
			map.put("USER_ID", user.getUser_id());
			Map<String, Object> userOrg = lrdOrgService.queryUserOrg(map);
			String orgCD = userOrg.get("ORG_CD").toString();
			List<Map<String,Object>> userRole = lrdRoleService.queryUserRole(map);
			for (Map<String, Object> map2 : userRole) {
				String roleFw = map2.get("ROLE_FW").toString();
				//本级
				if(roleFw.equals("00")){
					if(map.get("ROLE_FW")=="01"||map.get("ROLE_FW")=="02")continue;
					map.put("ROLE_FW","00");
				}else if(roleFw.equals("02")){ //本级及下级
					map.put("ROLE_FW","02");
				}else if(roleFw.equals("01")){//本级及所有下级
					if(map.get("ROLE_FW")=="02")continue;
					map.put("ROLE_FW","01");
				}
			}
			map.put("ORG_CD", orgCD);
			List<Map<String, Object>> resulOrg = lrdOrgService.queryMenu(map);
			//查询当前登录机构拥有的角色
			String orgIds="";
			for (Map<String, Object> map2 : resulOrg) {
				String orgId = map2.get("id").toString();
				orgIds=orgIds+""+orgId+",";
			}

			orgIds=orgIds.substring(0,orgIds.length()-1);
			map.remove("ORG_CD");
			map.put("ordIds", "("+orgIds+")");
			List<Map<String, Object>> resultRole = lrdRoleService.queryList(map);
			resultmap.put("ORG", resulOrg);
			resultmap.put("ROLE", resultRole);
			
			//返回机构，角色
			//Map<String, Object> userOrg = lrdOrgService.queryUserOrg(map);
			//List<Map<String, Object>> userRole = lrdRoleService.queryUserRole(map);
			
			
			map.put("USER_ID", USER_ID);
			
			List<Map<String,Object>> resultList = lrdUserService.query(map);
			List<Map<String,Object>> userRoleQuery = lrdRoleService.queryUserRole(map);
			Map<String, Object> userOrgQuery = lrdOrgService.queryUserOrg(map);
			resultmap.put("result", resultList);
			resultmap.put("userOrg", userOrgQuery);
			resultmap.put("userRole", userRoleQuery);
			
			this.setSuccess(resultmap, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "修改操作员--修改")
	public String update(@RequestBody Map<String,Object> map, HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			SimpleDateFormat format=new SimpleDateFormat("yyMMdd");
			map.put("MTN_DATE", format.format(new Date()));
			boolean update = lrdUserService.update(map);
			//先删除再插入
			boolean delete = lrdUserService.deleteByUser(map);
			
			String str = map.get("role_id").toString();
			List<String> result = Arrays.asList(StringUtils.split(str,","));  
			for(int i=0;i<result.size();i++){
				map.put("ROLEID", result.get(i));
				lrdUserService.saveRole(map);
			}
			txManager.commit(status);
			this.setSuccess(update, page);
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	public static void main(String[] args) {
		String md5 = MD5.getMD5("123456");
		System.out.println(md5);
		System.err.println("-----");
		System.out.println("e10adc3949ba59abbe56e057f20f883e");
	}
}
