package com.tgb.controller.propertymanage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.propertymanage.CustYjService;
import com.tgb.service.propertymanage.LrdOrgService;
import com.tgb.service.propertymanage.LrdRoleService;
import com.tgb.service.tree.CustMgTreeService;
import com.tgb.util.DateTools;
import com.tgb.util.DicReplace;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/custyj")
public class CustYjController extends BaseController {
	Logger log = Logger.getLogger(CustYjController.class);

	@Autowired
	private CustYjService custYjService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private CustMgTreeService custMgTreeService;
	@Autowired
	private LrdOrgService lrdOrgService;
	@Autowired
	private LrdRoleService lrdRoleService;

	@ResponseBody
	@RequestMapping(value = "/queryYtj", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "已提交查询")
	public String queryYtj(HttpServletRequest request) {
		log.info("接收的JSON:");
		try {
			// 登录人的机构
			Map<String, Object> map = new HashMap<String, Object>();
			User user = (User) request.getSession().getAttribute("user");
			String userId = user.getUser_id();
			map.put("USER_ID", userId);
			map.put("STAT", "1");
			List<Map<String, Object>> resultList = custYjService.query(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("CUST_TYPE", "CUST_TYPE");
			dicMap.put("JL_TYPE", "JL_TYPE");
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/queryDjs", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "待接收查询")
	public String queryDjs(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("接收的JSON:" + map.toString());
		try {
			// 登录人的机构
			User user = (User) request.getSession().getAttribute("user");
			String userId = user.getUser_id();
			map.put("USER_ID_SQ", userId);
			map.put("STAT", "0");
			List<Map<String, Object>> resultList = custYjService.query(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("CUST_TYPE", "CUST_TYPE");
			dicMap.put("JL_TYPE", "JL_TYPE");
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/queryYjs", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "已接收查询")
	public String queryYjs(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("接收的JSON:" + map.toString());
		try {
			// 登录人的机构
			User user = (User) request.getSession().getAttribute("user");
			String userId = user.getUser_id();
			map.put("USER_ID_SQ", userId);
			map.put("STAT", "1");
			List<Map<String, Object>> resultList = custYjService.query(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("CUST_TYPE", "CUST_TYPE");
			dicMap.put("JL_TYPE", "JL_TYPE");
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/updStat", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "接收客户-修改状态")
	public String updStat(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {//0待接收，1以接收，2以拒绝
			//map.put("STAT", "1");
			User user = (User) request.getSession().getAttribute("user");
			String userId = user.getUser_id();
			map.put("USER_ID_SQ", userId);
			custYjService.update(map);
			if ("1".equals(map.get("STAT")+"")) {//接收修改客户经理
				map.put("USER_ID", userId);
				custYjService.updateCustBaseGrpJl(map);
			}
			
			txManager.commit(status);
			this.setSuccess(null, page);
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/custYj", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "客户移交")
	public String custYj(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			User user = (User) request.getSession().getAttribute("user");
			map.put("USER_ID", user.getUser_id());
			SimpleDateFormat fomat = new SimpleDateFormat();
			map.put("CRT_DATE", DateTools.getCurrentSysData("yyyy-MM-dd"));
			map.put("STAT", "2");
			List<Map<String, Object>> result2 = custYjService.queryByStat(map);
			if (result2.size() > 0) {
				String org = "";
				String jl = "";
				for (Map<String, Object> map2 : result2) {
					org = map2.get("ORG_NAME") + "";
					jl = map2.get("USER_NAME")+"";
				}
				throw new Exception("今日以移交  "+org+"，客户经理："+jl+",但被拒绝，今日不允许再次移交！");
			}
			
			map.put("STAT", "1");
			List<Map<String, Object>> result1 = custYjService.queryByStat(map);
			if (result1.size() > 0) {
				String org = "";
				String jl = "";
				for (Map<String, Object> map2 : result1) {
					org = map2.get("ORG_NAME") + "";
					jl = map2.get("USER_NAME")+"";
				}
				throw new Exception("今日以移交  "+org+"，客户经理："+jl+"。今日不允许再次移交！");
			}

			map.put("STAT", "0");
			map.remove("CRT_DATE");
			// 查询是否被移交还没有被处理
			List<Map<String, Object>> result = custYjService.queryByStatNO(map);
			if (result.size() > 0) {
				String org = "";
				String jl = "";
				for (Map<String, Object> map2 : result) {
					org = map2.get("ORG_NAME") + "";
					jl = map2.get("USER_NAME")+"";
				}
				throw new Exception("该客户以移交。请联系  "+org+"，客户经理："+jl+"，进行接收/拒绝处理！");
			}
			map.put("CRT_DATE", DateTools.getCurrentSysData("yyyy-MM-dd"));
			custYjService.save(map);
			txManager.commit(status);
			this.setSuccess(null, page);
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/custYjTree", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "客户移交树")
	public String custYjTree(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("接收的JSON:" + map.toString());
		try {
			User user = (User) request.getSession().getAttribute("user");
			String userId = user.getUser_id();
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> queryMenu = custMgTreeService.queryAllMenu(map);
			result.addAll(queryMenu);
			for (Map<String, Object> map3 : queryMenu) {
				List<Map<String, Object>> listJL = custMgTreeService.queryJL(map3);
				for (Map<String, Object> map2 : listJL) {
					map2.put("pId", map3.get("id"));
					if (!(map2.get("id").toString()).equals(userId)) {
						result.add(map2);
					}

				}
			}
			this.setSuccess(result, page);
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/custYjTree2", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.POST })
	@SystemControllerLog(description = "批量移交树")
	public String custYjTree2(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("接收的JSON:" + map.toString());
		try {
			User user = (User) request.getSession().getAttribute("user");
			map.put("USER_ID", user.getUser_id());
			List<Map<String, Object>> userRole = lrdRoleService.queryUserRole(map);// 获取用户权限
			Map<String, Object> maxRole = new HashMap<String, Object>();
			for (Map<String, Object> map2 : userRole) {
				if (map2.get("ROLE_NAME").equals("系统管理员") || map2.get("ROLE_NAME").equals("总行用户")) {
					maxRole.put("ROLE_NAME", "系统管理员");
				}
				if (map2.get("ROLE_NAME").equals("支行行长")) {
					if ("系统管理员".equals(maxRole.get("ROLE_NAME"))) {
						break;
					}
					maxRole.put("ROLE_NAME", "支行行长");
				}
				if (map2.get("ROLE_NAME").equals("客户经理")) {
					if ("系统管理员".equals(maxRole.get("ROLE_NAME")) || "支行经理".equals(maxRole.get("ROLE_NAME"))) {
						break;
					}
					maxRole.put("ROLE_NAME", "客户经理");
				}

			}
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			if ("系统管理员".equals(maxRole.get("ROLE_NAME"))) {
				// 机构

				List<Map<String, Object>> queryMenu = custMgTreeService.queryAllMenu(map);
				result.addAll(queryMenu);
				for (Map<String, Object> map3 : queryMenu) {
					map3.put("ZH_ID", map3.get("id"));
					List<Map<String, Object>> listJL = custMgTreeService.queryJL(map3);// 经理列表个个机构
					for (Map<String, Object> map2 : listJL) {
						map2.put("pId", map3.get("id"));
						map2.put("JL_ID", map2.get("id"));
						map2.put("ZH_ID", map3.get("id"));
						result.add(map2);
					}
				}
			}
			if ("支行行长".equals(maxRole.get("ROLE_NAME"))) {
				Map<String, Object> queryUserOrg = lrdOrgService.queryUserOrg(map);
				map.put("ORG_PCD", queryUserOrg.get("ORG_CD") + "");
				List<Map<String, Object>> list = lrdOrgService.queryMenu(map);
				list.add(queryUserOrg);

				for (Map<String, Object> map3 : list) {
					map3.put("id", map3.get("ORG_CD"));
					List<Map<String, Object>> listJL = custMgTreeService.queryJL(map3);// 经理列表个个机构
					for (Map<String, Object> map2 : listJL) {
						map2.put("pId", map3.get("id"));
						map2.put("JL_ID", map2.get("id"));
						map2.put("ZH_ID", map3.get("id"));
						result.add(map2);
					}
				}

			}
			this.setSuccess(result, page);
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/queryCByJl", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询移交村庄--通过客户经理")
	public String queryCByJl(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			List<Map<String, Object>> result = custYjService.queryAreaByCustJl(map);
			this.setSuccess(result, page);
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/custYjByAllC", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.POST })
	@SystemControllerLog(description = "村庄中的村移交给其他客户经理")
	public String custYjByAllC(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			// 客户变移交
			List<Map<String, String>> ALL = (List<Map<String, String>>) map.get("ZT");

			for (Map<String, String> map2 : ALL) {
				String AREA_CD = map2.get("z_id");
				String AREA_CD2 = map2.get("c_id");
				map.put("AREA_CD", AREA_CD);
				map.put("AREA_CD2", AREA_CD2);
				custYjService.custYjByAllC(map);
			}
			// 经理分布村表修改
			txManager.commit(status);
			this.setSuccess(null, page);
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/queryAllJl", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询素所有的客户经理")
	public String queryAllJl(HttpServletRequest request) {
		log.info("接收的JSON:");
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> queryMenu = custMgTreeService.queryAllMenu(map);
			result.addAll(queryMenu);
			for (Map<String, Object> map3 : queryMenu) {
				map3.put("ZH_ID", map3.get("id"));
				List<Map<String, Object>> listJL = custMgTreeService.queryJL(map3);// 经理列表个个机构
				for (Map<String, Object> map2 : listJL) {
					map2.put("pId", map3.get("id"));
					map2.put("JL_ID", map2.get("id"));
					map2.put("ZH_ID", map3.get("id"));
					result.add(map2);
				}
			}

			this.setSuccess(result, page);
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}

}
