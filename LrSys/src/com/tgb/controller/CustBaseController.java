package com.tgb.controller;

import hlc.base.db.DbAccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
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
import com.tgb.service.cust.CustBaseService;
import com.tgb.service.cust.CustFeedbackService;
import com.tgb.service.cust.CustJsService;
import com.tgb.service.propertymanage.LrdRoleService;
import com.tgb.util.DateTools;
import com.tgb.util.DicReplace;
import com.tgb.util.StringUtils;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/custbase")
public class CustBaseController extends BaseController {

	@Autowired
	private CustBaseService custBaseService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private LrdRoleService lrdRoleService;
	@Autowired
	private CustJsService custJsservice;
	@Autowired
	private  CustFeedbackService  custFeedbackService;
	Logger log = Logger.getLogger(CustBaseController.class);

	@ResponseBody
	@RequestMapping(value = "/queryList", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户信息")
	public String queryList(@RequestBody Map<String, Object> map,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("接收的JOSN：" + map.toString());
		// response.setContentType("text/html;charset=utf-8");
		// response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			User user = this.getSessionUser(request);
			// map.put("cust_grp_jl", user.getId());
			Object view = map.get("VIEW");
			if (null != view && "1".equals(view.toString())) {

			} else {
				map.put("CUST_GRP_JL", user.getUser_id());
			}
			map.put("USER_ID", user.getUser_id());
			List<Map<String, Object>> userRole = lrdRoleService
					.queryUserRole(map);// 获取用户权限
			for (Map<String, Object> map2 : userRole) {
				if (map2.get("ROLE_ID").toString().equals("0000")) {
					map.put("USER_ROLE", "0000");
				}
			}
			
			isPage(map);
			
			if ("".equals(map.get("CUST_ID"))||map.get("CUST_ID")==null){//CUST_ID为空去查询列表
				List<Map<String, Object>> resultList = custBaseService
						.queryList(map);
			}else{
				map.remove("CUST_GRP_JL");
				map.remove("USER_ID");
				List<Map<String, Object>> resultList = custBaseService
						.queryList(map);
				
			}
			
			List<Map<String, Object>> resultList = custBaseService
					.queryList(map);
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
			DicReplace.replaceDic(resultList, dicMap);
			DicReplace.replaceChildDic(resultList, "CUST_GRP", "CUST_GRP2");
			DicReplace.replaceChildDic(resultList, "AREA_CD", "AREA_CD2");
			/*
			 * if(null!=resultList && resultList.size()>0){ for(Map<String,
			 * Object> tmp :resultList){ String
			 * custid=tmp.get("CUST_ID").toString(); tmp.put("CUST_NAME1",
			 * tmp.get("CUST_TYPE")+String.format("%08d",
			 * Integer.parseInt(custid))); } }
			 */
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		// "totalPages":15,"totalRows":144
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/isExistCust", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "验证客户是否存在")
	public String isExistCust(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JOSN：" + map.toString());
		User user = this.getSessionUser(request);
		try {
			Map<String, Object> telMap = new HashMap<String, Object>();
			telMap.put("TEL_NO", map.get("TEL_NO") + "");
			List<Map<String, Object>> queryList = custBaseService
					.queryList(telMap);
			if (queryList.size() > 0) {
				throw new Exception("电话号码已存在!");
			}
			Map<String, Object> resultMap = null;
			resultMap = custBaseService.isExistCust(map);
			if (null != resultMap && !resultMap.isEmpty()) {
				Object CUST_GRP_JL = resultMap.get("CUST_GRP_JL");
				if (null != CUST_GRP_JL
						&& user.getUser_id().equals(CUST_GRP_JL.toString())) {
					resultMap.put("isExist", "2");// 存在 属于当前客户经理
				} else {
					resultMap.put("isExist", "3");// 存在不属于当前客户经理
				}
			} else {
				resultMap = new HashMap<String, Object>();
				resultMap.put("isExist", "1");// 不存在
			}
			this.setSuccess(resultMap, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/saveCust", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "保存客户基本信息")
	public String saveCust(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JOSN：" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			// User user = this.getSessionUser(request);
			User user = this.getSessionUser(request);
			map.put("CUST_GRP_JL", user.getUser_id());
			// map.put("JK_JT_STATUS", "1");
			map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
			map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
			map.put("CUST_JL_ID", user.getUser_id());
			map.put("CUST_ID", DateTools.getCurrentSysData("yyyyMMddHHmmsss")
					+ (int) ((Math.random() * 9 + 1) * 10000));
			custBaseService.saveCust(map);
			custBaseService.deleteByPkAreaTmp(map);
			custBaseService.saveAreaTmp(map);
			/*//wjk修改，在客户需求表生成一条记录
			saveFeedBack(map);*/
			this.setSuccess(map, page);
			txManager.commit(status);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
			txManager.rollback(status);
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/updateCustBase", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "修改客户基本信息")
	public String updateCustBase(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		TransactionStatus status = this.getTransactionStatus(txManager);
		log.info("接收的JOSN：" + map.toString());
		try {
			String creatType = map.get("CREATE_TYPE")+"";
			if("2".equals(creatType)||"4".equals(creatType)){
			CustBaseController.YzCustWz(map.get("CUST_ID")+"");
			}
			// User user = this.getSessionUser(request);
			User user = this.getSessionUser(request);
			map.put("CUST_GRP_JL", user.getUser_id());
			map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
			Object idType = map.get("ID_TYPE");
			Object idNo = map.get("ID_NO");
			Object userName = map.get("CUST_NAME");
			Object telNo = map.get("TEL_NO");
			Object credCode = map.get("CRED_CODE");
			Object custType = map.get("CUST_TYPE");
			
			
			/*if (map.get("ADDRESS")!=null&&"".equals(map.get("ADDRESS"))){//基本信息家庭地址修改  联动 经营地址修改
			
				custJsservice.update(map);
				
			}*/
			
			
			
			if (null != custType && "XWQY".equals(custType)) {// 小微企業
				if (null != credCode && !"".equals(credCode.toString())) {
					Map<String, Object> idNoMap = new HashMap<String, Object>();
					idNoMap.put("CRED_CODE", credCode.toString());
					List<Map<String, Object>> resultList = custBaseService
							.queryList(idNoMap);
					if (null != resultList && resultList.size() == 1) {
						Map<String, Object> tmp = resultList.get(0);
						if (map.get("CUST_ID").toString()
								.equals(tmp.get("CUST_ID").toString())) {
							map.remove("CUST_GRP_JL");
							custBaseService.updateCustBase(map);
							this.setSuccess(null, page);
						} else {
							this.setErr(null, "统一社会信用代码重复");
						}

					} else if (null != resultList && resultList.size() > 1) {
						this.setErr(null, "统一社会信用代码重复");
					}

					else {
						// map.put("credCode", resultList.get(1));
						map.remove("CUST_GRP_JL");
						custBaseService.updateCustBase(map);
						this.setSuccess(null, page);
					}

				} else {
					map.remove("CUST_GRP_JL");
					custBaseService.updateCustBase(map);
					this.setSuccess(null, page);
				}

			} else if (null != custType
					&& "CZJM,NCJM,GTGS".contains(custType.toString())) {
				if (null != idType && "01".equals(idType)) {
					if (null != idNo && !"".equals(idNo.toString())) {
						Map<String, Object> idNoMap = new HashMap<String, Object>();
						idNoMap.put("ID_NO", idNo.toString());
						List<Map<String, Object>> resultList = custBaseService
								.queryList(idNoMap);
						if (null != resultList && resultList.size() == 1) {
							Map<String, Object> tmp = resultList.get(0);
							if (map.get("CUST_ID").toString()
									.equals(tmp.get("CUST_ID").toString())) {
								map.remove("CUST_GRP_JL");
								custBaseService.updateCustBase(map);
								this.setSuccess(null, page);
							} else {
								this.setErr(null, "身份证号重复");
							}

						} else if (null != resultList && resultList.size() > 1) {
							this.setErr(null, "身份证号重复");
						} else {
							map.remove("CUST_GRP_JL");
							custBaseService.updateCustBase(map);
							this.setSuccess(null, page);
						}
						// map.put("ID_NO", resultList.get(1));
					} else {
						if (!userName.toString().isEmpty()
								&& !telNo.toString().isEmpty()) {
							Map<String, Object> umap = new HashMap<String, Object>();
							umap.put("CUST_NAME", userName.toString());
							umap.put("TEL_NO", telNo.toString());
							List<Map<String, Object>> resultList = custBaseService
									.queryList(umap);
							if (null != resultList && resultList.size() == 1) {
								Map<String, Object> tmp = resultList.get(0);
								if (map.get("CUST_ID").toString()
										.equals(tmp.get("CUST_ID").toString())) {
									map.remove("CUST_GRP_JL");
									custBaseService.updateCustBase(map);
									this.setSuccess(null, page);
								} else {
									this.setErr(null, "姓名电话重复");
								}

							} else if (null != resultList
									&& resultList.size() > 1) {
								this.setErr(null, "姓名电话重复");
							} else {
								map.remove("CUST_GRP_JL");
								custBaseService.updateCustBase(map);
								this.setSuccess(null, page);
							}
							// map.put("USER_NAME", resultList.get(1));
							// map.put("TEL_NO", resultList.get(1));

						} else {
							this.setErr(null, "姓名/电话不能为空");
							// custBaseService.updateCustBase(map);
						}
					}
				} else {
					if (!userName.toString().isEmpty()
							&& !telNo.toString().isEmpty()) {
						Map<String, Object> umap = new HashMap<String, Object>();
						umap.put("CUST_NAME", userName.toString());
						umap.put("TEL_NO", telNo.toString());
						List<Map<String, Object>> resultList = custBaseService
								.queryList(umap);
						if (null != resultList && resultList.size() > 1) {
							this.setErr(null, "姓名电话重复");
						} else {
							map.remove("CUST_GRP_JL");
							custBaseService.updateCustBase(map);
							this.setSuccess(null, page);
						}
						// map.put("USER_NAME", resultList.get(1));
						// map.put("TEL_NO", resultList.get(1));

					} else {
						this.setErr(null, "姓名/电话不能为空");
						// custBaseService.updateCustBase(map);
					}
				}
			}

			else {
				// 其他关联信息
				map.remove("CUST_GRP_JL");
				custBaseService.updateCustBase(map);
				this.setSuccess(null, page);
			}

			txManager.commit(status);
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/querybylike", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "模糊查询客户基本信息")
	public String query(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		User user = this.getSessionUser(request);
		log.info("接收的JOSN：" + map.toString());
		Object telNo = null;
		Object idNO = null;
		Object custName = null;
		String num = "\\d*";
		String reg = "^((13[0-9])|(15[0-9])|(18[0,5-9]))\\d*$";
		isPage(map);
		Object obj = map.get("keyword");

		try {
			if (null != obj && !"".equals(obj.toString())) { 
				
				/*
				 * if (((String) obj).matches(reg)) { telNo =
				 * String.valueOf(obj); } else if (((String) obj).matches(num))
				 * { idNO = String.valueOf(obj); } else { custName =
				 * obj.toString(); } User user = this.getSessionUser(request);
				 * map.put("CUST_GRP_JL", user.getUser_id()); map.put("TEL_NO",
				 * telNo); map.put("ID_NO", idNO); map.put("CUST_NAME",
				 * custName);
				 */
				List<Map<String, Object>> resultList = custBaseService
						.queryByLike(map);

				Map<String, String> dicMap = new HashMap<String, String>();
				dicMap.put("CUST_TYPE", "CUST_TYPE");
				dicMap.put("JL_TYPE", "JL_TYPE");
				dicMap.put("CREATE_TYPE", "ZTBJ");
				DicReplace.replaceDic(resultList, dicMap);
				
				
				List<Map<String,Object>> roleList = user.getROLE_CD();//权限列表
				String fw="";
				
				for (Map<String, Object> map2 : roleList) {
					fw=map2.get("ROLE_FW")+"";
				}
				
				
				//寻找最大范围
				if (fw.contentEquals("01")) {//可查看所有---不对数据进行处理
					
					
				}else if (fw.contentEquals("02")) {//可查看本级及下级
					
					
					for (Map<String, Object> tmpMap : resultList) {
									
						String orgCd = tmpMap.get("ORG_CD").toString();
						if (!user.getOrgCD().equals(orgCd)) {
							// 替换电话中间四位为*
							tmpMap.put("OTHER", "TRUE");
							String custType = tmpMap.get("CUST_TYPE").toString();
							if ("XWQY".equals(custType)) {
								Object tmpObj = tmpMap.get("LXR_TEL");
								if (null != tmpObj && !"".equals(obj.toString())) {
									tmpMap.put("LXR_TEL", StringUtils
											.userNameReplaceWithStar(tmpObj
													.toString()));
								}

							} else {
								Object tmpObj = tmpMap.get("TEL_NO");
								if (null != tmpObj && !"".equals(obj.toString())) {
									tmpMap.put("TEL_NO", StringUtils
											.userNameReplaceWithStar(tmpObj
													.toString()));
								}
							}

						}
					}
					
					
					
				}else {//查询本级的
					for (Map<String, Object> tmpMap : resultList) {
						String custGrpJl = tmpMap.get("CUST_GRP_JL").toString();
						if (!user.getUser_id().equals(custGrpJl)) {
							// 替换电话中间四位为*
							tmpMap.put("OTHER", "TRUE");
							String custType = tmpMap.get("CUST_TYPE").toString();
							if ("XWQY".equals(custType)) {
								Object tmpObj = tmpMap.get("LXR_TEL");
								if (null != tmpObj && !"".equals(obj.toString())) {
									tmpMap.put("LXR_TEL", StringUtils
											.userNameReplaceWithStar(tmpObj
													.toString()));
								}

							} else {
								Object tmpObj = tmpMap.get("TEL_NO");
								if (null != tmpObj && !"".equals(obj.toString())) {
									tmpMap.put("TEL_NO", StringUtils
											.userNameReplaceWithStar(tmpObj
													.toString()));
								}
							}

						}
					}
				}
				
				
				
				
				/*for (Map<String, Object> tmpMap : resultList) {
					String custGrpJl = tmpMap.get("CUST_GRP_JL").toString();
					if (!user.getUser_id().equals(custGrpJl)) {
						// 替换电话中间四位为*
						tmpMap.put("OTHER", "TRUE");
						String custType = tmpMap.get("CUST_TYPE").toString();
						if ("XWQY".equals(custType)) {
							Object tmpObj = tmpMap.get("LXR_TEL");
							if (null != tmpObj && !"".equals(obj.toString())) {
								tmpMap.put("LXR_TEL", StringUtils
										.userNameReplaceWithStar(tmpObj
												.toString()));
							}

						} else {
							Object tmpObj = tmpMap.get("TEL_NO");
							if (null != tmpObj && !"".equals(obj.toString())) {
								tmpMap.put("TEL_NO", StringUtils
										.userNameReplaceWithStar(tmpObj
												.toString()));
							}
						}

					}
				}*/
				this.setSuccess(resultList, page);
			}
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/queryAreaTmp", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户区域临时信息")
	public String queryAreaTmp(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JOSN：" + map.toString());
		try {
			User user = this.getSessionUser(request);
			map.put("CUST_GRP_JL", user.getUser_id());
			map.put("CUST_JL_ID", user.getUser_id());
			Map<String, Object> resMap = custBaseService.queryAreaTmp(map);

			this.setSuccess(resMap, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}

		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/saveAreaTmp", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "保存客户区域临时信息")
	public String saveAreaTmp(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JOSN：" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		// 先删除在新增
		try {
			custBaseService.deleteByPkAreaTmp(map);
			custBaseService.saveAreaTmp(map);
			txManager.commit(status);
			this.setSuccess(null, page);
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}

		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/deleteAll", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除客户信息")
	public String deleteAll(@RequestBody Map<String, Object> map,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("接收的JOSN：" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			map.put("ISDEL", "1");
			custBaseService.updateCustBase(map);// 删除客户的所有信息
			//删除客户需求表记录
			custFeedbackService.delete(map);
			txManager.commit(status);
			this.setSuccess(null, page);
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}

		return this.js.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/YzTelNo", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "验证手机号是否存在")
	public String YzTelNo(@RequestBody Map<String, Object> map,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("接收的JOSN：" + map.toString());
		try {
			Map<String, Object> telMap = new HashMap<String, Object>();
			telMap.put("TEL_NO", map.get("TEL_NO") + "");
			String CUST_ID=map.get("CUST_ID")+"";
			List<Map<String, Object>> queryList = custBaseService
					.queryList(telMap);
			for (Map<String, Object> map2 : queryList) {
				if (!CUST_ID.equals((map2.get("CUST_ID")+""))) {
					throw new Exception("电话号码已存在!");
				}
			}
			//if (queryList.size() > 1) {
				//throw new Exception("电话号码已存在!");
			//}
			this.setSuccess(null, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}

		return this.js.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/YzSFZH", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "验证身份证号存在")
	public String YzSFZH(@RequestBody Map<String, Object> map,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("接收的JOSN：" + map.toString());
		try {
			Map<String, Object> telMap = new HashMap<String, Object>();
			if (!"".equals(map.get("ID_NO") + "")) {
				telMap.put("ID_NO", map.get("ID_NO") + "");
				List<Map<String, Object>> queryList = custBaseService
						.queryList(telMap);
				if (queryList.size() > 1) {
					//if (!"".equals(map.get("ID_NO") + "")) {
						throw new Exception("证件号已存在");
					//}
				}	
			}
			this.setSuccess(null, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}

		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/YzSFZHSave", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "验证身份证号存在")
	public String YzSFZHSave(@RequestBody Map<String, Object> map,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("接收的JOSN：" + map.toString());
		try {
			Map<String, Object> telMap = new HashMap<String, Object>();
			if (!"".equals(map.get("ID_NO") + "")) {
				telMap.put("ID_NO", map.get("ID_NO") + "");
				List<Map<String, Object>> queryList = custBaseService
						.queryList(telMap);
				if (queryList.size() > 0) {
					//if (!"".equals(map.get("ID_NO") + "")) {
						throw new Exception("证件号已存在");
					//}
				}	
			}
			this.setSuccess(null, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}

		return this.js.toString();
	}
	
	
	
	//验证客户信息完整度"
	public static void YzCustWz(String CUST_ID)throws Exception {
			DbAccess db = new DbAccess();
			String sql1 = "select"+
			"(case  when cust_base.CUST_TYPE is NULL then '客户类型,' when  cust_base.CUST_TYPE='' then '客户类型,' end )SF1,"+
			"(case  when cust_base.CUST_NAME is NULL then '客户姓名,' when cust_base.CUST_NAME='' then '客户姓名,' end )SF2,"+
			"(case  when cust_base.USER_AGE is NULL then '客户年龄,' when cust_base.USER_AGE='' then '客户年龄,' end )SF3,"+
			"(case  when cust_base.SEX is NULL then '客户性别,' when cust_base.SEX='' then '客户性别,' end )SF4,"+
			"(case  when cust_base.TEL_NO is NULL then '客户电话,' when cust_base.TEL_NO='' then '客户电话,' end )SF5,"+
			"(case  when cust_base.ID_TYPE is NULL then '证件类型,' when cust_base.ID_TYPE='' then '证件类型,' end )SF6,"+
			"(case  when cust_base.ID_NO is NULL then '证件号码,' when cust_base.ID_NO='' then '证件号码,' end )SF7,"+
			"(case  when cust_base.EDU_LEVEL is NULL then '学历,' when cust_base.EDU_LEVEL='' then '学历,' end )SF8,"+
			"(case  when cust_base.HEALTHY is NULL then '健康状况,' when cust_base.HEALTHY='' then '健康状况,' end )SF9,"+
			"(case  when cust_base.AREA_CD is NULL then '所属区域,' when cust_base.AREA_CD='' then '所属区域,' end )SF10 "+
			"from cust_base  where cust_id='"+CUST_ID+"';";
			//客戶基本信息
			Map<String, String> map1 = db.queryForMap(sql1);
			Set<Entry<String,String>> entrySet = map1.entrySet();
			String value="";
			for (Entry<String, String> entry : entrySet) {
				value=value+entry.getValue();
			}
			if(value.length()>0){
				value=value.substring(0,value.length()-1);
				throw new  Exception("基本信息"+value+"为空值，请核实完善。   基本信息请状态选择信息不完整进行保存");
			}
			
			//单独判断个体工商用户信息
			String sql22 = "select CUST_TYPE from cust_base  where cust_id='"+CUST_ID+"';";
			Map<String, String> map22 = db.queryForMap(sql22);
			String custType = map22.get("CUST_TYPE")+"";
			if (custType.equals("GTGS")) {
				//单独判断个体工商用户信息
				String sql23 ="select"+
						"(case  when cust_base.DP_NAME is NULL then '店铺名称,' when cust_base.DP_NAME='' then '店铺名称,' end )SF9,"+
						"(case  when cust_base.LICENCE_NO is NULL then '营业执照号,' when cust_base.LICENCE_NO='' then '营业执照号,' end )SF10 ,"+
						"(case  when cust_base.LICENCE_NAME is NULL then '营业执照名称,' when cust_base.LICENCE_NAME='' then '营业执照名称,' end )SF11 "+
						"from cust_base  where cust_id='"+CUST_ID+"';";
				Map<String, String> map23 = db.queryForMap(sql23);
			
					Set<Entry<String,String>> entrySet3 = map23.entrySet();
					String value3="";
					for (Entry<String, String> entry : entrySet3) {
						value3=value3+entry.getValue();
					}
					
					if(value3.length()>0||map23.size()==0){
						value3=value3.substring(0,value3.length()-1);
						throw new Exception("主营项目经商中，"+value3+"为空值，请核实完善。");
					}
		
			}
			
			//家庭关系有无子女校验
			String sql24 = "select CHILD_FLAG from cust_base  where cust_id='"+CUST_ID+"';";
			Map<String, String> map24 = db.queryForMap(sql24);			
			if ((map24.get("CHILD_FLAG")+"").equals("1")) {//有子女教研是否添加子女
				
				String sql25 = "SELECT COUNT(*) num from cust_rela where RELA_TYPE='03' and cust_id='"+CUST_ID+"';";
				Map<String, String> map25 = db.queryForMap(sql25);			
				int num=0;
				try {
					num=Integer.valueOf(map25.get("num")+"");
				} catch (Exception e) {}
				if (num==0) {
					throw new Exception("家庭情况中，标记有子女且未添加子女信息，请核实完善。");
				}
				
				
			}
			
			
			
			//判断“主营项目”是否选择。
			String sql2 = "select JTZYXM from cust_base  where cust_id='"+CUST_ID+"';";
			Map<String, String> map2 = db.queryForMap(sql2);			
			Set<Entry<String,String>> entrySet2 = map2.entrySet();
			String value2="";
			for (Entry<String, String> entry : entrySet2) {
				value2=value2+entry.getValue();
			}
			if(value2.length()==0){
				//value=value.substring(value.length()-1,value.length());
				throw new Exception("主营项目字段为空值，请核实完善。");
			}
			
			if("01".equals(value2)||"02".equals(value2)){//种植情况
				String sql3="SELECT"+
				" (case  when cust_zz.zw_type is NULL then '作物种类,' when cust_zz.zw_type='' then '作物种类,' end )SF1,"+
				"(case  when cust_zz.zz_ms is NULL then '种植亩数,' when cust_zz.zz_ms='' then '种植亩数,' end )SF2,"+
				"(case  when cust_zz.ZZ_MODEL is NULL then '种植模式,' when cust_zz.ZZ_MODEL='' then '种植模式,' end )SF3,"+
				"(case  when cust_zz.BIZ_DATE is NULL then '经营开始年份,' when cust_zz.BIZ_DATE='' then '经营开始年份,' end )SF4,"+
				"(case  when cust_zz.INCOME_Y is NULL then '年收入,' when cust_zz.INCOME_Y='' then '年收入,' end )SF5 "+
				"from cust_zz where cust_id='"+CUST_ID+"';";
				
				List<Map<String,String>> list = db.queryForList(sql3);
				
				if(list.size()==0){
					
					throw new Exception("主营项目种植未填写，请核实完善。");
				}
				for (Map<String, String> map3 : list) {
					Set<Entry<String,String>> entrySet3 = map3.entrySet();
					String value3="";
					for (Entry<String, String> entry : entrySet3) {
						value3=value3+entry.getValue();
					}
					
					
					if(value3.length()>0||map3.size()==0){
						value3=value3.substring(0,value3.length()-1);
						throw new Exception("主营项目种植中，"+value3+"为空值，请核实完善。");
					}
				}
				
			}else if("03".equals(value2)||"04".equals(value2)){//养殖
				
				String sql3 ="SELECT"+
				"(case  when yz_type is NULL then '养殖种类,' when yz_type='' then '养殖种类,' end )SF1,"+
				"(case  when biz_date is NULL then '开始年份,' when biz_date='' then '开始年份,' end )SF2,"+
				"(case  when income_y is NULL then '年收入,' when income_y='' then '年收入,' end )SF3 "+
				"from cust_yz where cust_id='"+CUST_ID+"';";
				
				 List<Map<String,String>> list = db.queryForList(sql3);
				 
				 if (list.size()==0) {
						throw new Exception("主营项目养殖中未填写，请核实完善。");
					}
				 
				 for (Map<String, String> map3 : list) {
					 Set<Entry<String,String>> entrySet3 = map3.entrySet();
						String value3="";
						for (Entry<String, String> entry : entrySet3) {
							value3=value3+entry.getValue();
						}
						
						
						if(value3.length()>0){
							value3=value3.substring(0,value3.length()-1);
							throw new Exception("主营项目养殖中，"+value3+"为空值，请核实完善。");
						}
				}
				
			}else if("05".equals(value2)){//职工
				
				
				
				String sql3 ="SELECT"+
				"(case  when work_date is NULL then '工作开始年份,' when work_date='' then '工作开始年份,' end )SF1,"+
				"(case  when YLBXAMT is NULL then '医疗保险上年度合计缴纳金额,' when YLBXAMT<0 then '医疗保险上年度合计缴纳金额,' end )SF2,"+
				"(case  when YEARAMT is NULL then '年收入,' when YEARAMT='' then '年收入,' end )SF3 "+
				"from cust_zg where cust_id='"+CUST_ID+"';";
				
				Map<String, String> map3 = db.queryForMap(sql3);
				Set<Entry<String,String>> entrySet3 = map3.entrySet();
				String value3="";
				for (Entry<String, String> entry : entrySet3) {
					value3=value3+entry.getValue();
				}
				if (map3.size()==0) {
					throw new Exception("主营项目职工未填写，请核实完善。");
				}
				
				if(value3.length()>0){
					value3=value3.substring(0,value3.length()-1);
					throw new Exception("主营项目职工中，"+value3+"为空值，请核实完善。");
				}
				
				
			}else if ("11".equals(value2)) {//打工
				
				String sql11 = "SELECT"+
				"(case  when DG_YEARS is NULL then '打工年限,' when DG_YEARS<0 then '打工年限,' end )SF2,"+
				"(case  when INCOME_Y is NULL then '年收入,' when INCOME_Y<0 then '年收入,' end )SF3 "+
				"from cust_dg where cust_id='"+CUST_ID+"';";
				
				Map<String, String> map31 = db.queryForMap(sql11);
				Set<Entry<String,String>> entrySet31 = map31.entrySet();
				String value31="";
				for (Entry<String, String> entry : entrySet31) {
					value31=value31+entry.getValue();
				}
				if (map31.size()==0) {
					throw new Exception("主营项目打工未填写，请核实完善。");
				}
				
				if(value31.length()>0){
					value31=value31.substring(0,value31.length()-1);
					throw new Exception("主营项目打工中，"+value31+"为空值，请核实完善。");
				}
				
				
			}else{//经商
				
				
				String sql11 = "select"+
				"(case  when cust_base.workers is NULL then '员工总数量,' when cust_base.workers<0 then '员工总数量,' end )SF9,"+
				"(case  when cust_base.BX_CNT is NULL then '有医疗保险数量,' when cust_base.BX_CNT<0 then '有医疗保险数量,' end )SF10 "+
				"from cust_base  where cust_id='"+CUST_ID+"';";
				
				
				Map<String, String> map31 = db.queryForMap(sql11);
				Set<Entry<String,String>> entrySet31 = map31.entrySet();
				String value31="";
				for (Entry<String, String> entry : entrySet31) {
					value31=value31+entry.getValue();
				}
				if (map31.size()==0) {
					throw new Exception("主营项目职工未填写，请核实完善。");
				}
				
				if(value31.length()>0){
					value31=value31.substring(0,value31.length()-1);
					throw new Exception("主营项目职工中，"+value31+"为空值，请核实完善。");
				}
				
				
				String sql3 ="SELECT"+
				"(case  when biz_hy is NULL then '经营行业,' when biz_hy='' then '经营行业' end )SF3,"+
				"(case  when biz_date is NULL then '经营开始年份,' when biz_date='' then '经营开始年份,' end )SF4,"+
				"(case  when (rent_area  is NULL OR OWN_AREA IS NULL) then '经营场所建筑面积,' when (rent_area<0 OR OWN_AREA<0)  then '经营场所建筑面积,' end )SF5,"+
				"(case  when profit_y is NULL then '年利润,' when profit_y='' then '年利润,' end )SF6,"+
				"(case  when income_y is NULL then '年收入,' when income_y='' then '年收入,' end )SF7 "+
				"from cust_js where cust_id='"+CUST_ID+"';";
				
				List<Map<String,String>> list = db.queryForList(sql3);
				 
				
				if (list.size()==0) {
					throw new Exception("主营项目经商未填写，请核实完善。");
				}
				
				for (Map<String, String> map3 : list) {
					Set<Entry<String,String>> entrySet3 = map3.entrySet();
					String value3="";
					for (Entry<String, String> entry : entrySet3) {
						value3=value3+entry.getValue();
					}
					
					if(value3.length()>0){
						value3=value3.substring(0,value3.length()-1);
						throw new Exception("主营项目经商中，"+value3+"为空值，请核实完善。");
					}
				}
				
			}
			
			//婚姻状况
			String sql3 = "SELECT"+
					"(case  when JH_FLAG is NULL then '婚姻状况,' when JH_FLAG='' then '婚姻状况,' end )SF1,"+
					
					"(case  when JK_JT_STATUS is NULL then '客户及家庭成员工作情况,' when JK_JT_STATUS='' then '客户及家庭成员工作情况,' end )SF4 "+
					"from cust_base where cust_id='"+CUST_ID+"';";
			
			Map<String, String> map3 = db.queryForMap(sql3);
			Set<Entry<String,String>> entrySet3 = map3.entrySet();
			String value3="";
			for (Entry<String, String> entry : entrySet3) {
				value3=value3+entry.getValue();
			}
			if(value3.length()>0){
				value3=value3.substring(0,value3.length()-1);
				throw new Exception("家庭信息中客户本人的婚姻状况，"+value3+"为空值，请核实完善");
			}
		
			
			//查询婚姻状况
			String sql4 = "SELECT JH_FLAG from cust_base where cust_id='"+CUST_ID+"';";
			
			Map<String, String> map4 = db.queryForMap(sql4);
			Set<Entry<String,String>> entrySet4 = map4.entrySet();
			String value4="";
			for (Entry<String, String> entry : entrySet4) {
				value4=value4+entry.getValue();
			}
			//已婚
			if (value4=="02") {
				//判断是否有结婚证日期
				String sql15 = "SELECT"+
						"(case  when JH_DATE is NULL then '结婚证登记日期,' when JH_DATE='' then '结婚证登记日期,' end )SF2,"+
						"(case  when CHILD_FLAG is NULL then '有无子女,' when CHILD_FLAG='' then '有无子女,' end )SF3 "+
						"from cust_base where cust_id='"+CUST_ID+"';";
				Map<String, String> map15 = db.queryForMap(sql15);
				Set<Entry<String,String>> entrySet15 = map15.entrySet();
				String value15="";
				for (Entry<String, String> entry : entrySet15) {
					value15=value15+entry.getValue();
				}
				if(value15.length()>0){
					throw new Exception("家庭信息中客户本人的"+value15+"为空值，请核实完善");
				}
				
				//判断配偶
				String sql5="SELECT RELA_TYPE from cust_rela where cust_id='"+CUST_ID+"' and RELA_TYPE=='01';";
				Map<String, String> map5 = db.queryForMap(sql5);
				Set<Entry<String,String>> entrySet5 = map5.entrySet();
				String value5="";
				for (Entry<String, String> entry : entrySet5) {
					value5=value5+entry.getValue();
				}
				//没有配偶
				if(!value5.equals("01")){
					throw new Exception("家庭信息中客户已婚但无配偶信息，请核实完善。");
				}
			}
			//判断用户年龄
			String sql5 = "SELECT USER_AGE from cust_base where cust_id='"+CUST_ID+"';";
			Map<String, String> map5 = db.queryForMap(sql5);
			Set<Entry<String,String>> entrySet5 = map5.entrySet();
			String value5="";
			for (Entry<String, String> entry : entrySet5) {
				value5=value5+entry.getValue();
			}
			
			//年龄大于30
			if(Integer.valueOf(value5)>30){
				//判断财务信息中固定资产是否大于0。
				String sql6="SELECT GDZC from fin_rpt where cust_id='"+CUST_ID+"';";
				 List<Map<String, String>> list = db.queryForList(sql6);
				 double gd=0.0;
				 for (Map<String, String> map : list) {
					 String gded=map.get("GDZC")+"";
					 Double zc=0.0;
					 
					 try {
						zc = Double.valueOf(gded);
					} catch (Exception e) {}
					gd=gd+zc;
				}
				 
				 if (gd==0) {
					 throw new Exception("财务信息中固定资产为空值，请核实完善。");
				}

			}

			//三年的收入
			String sql7 = "SELECT"+ 
			"(case  when SR_1Y is NULL then '一年前总收入,' when SR_1Y='' then '一年前总收入,' when SR_1Y=0 then '一年前总收入,' end )SF1,"+
			"(case  when SR_2Y is NULL then '二年前总收入,' when SR_2Y='' then '二年前总收入,' when SR_2Y=0 then '二年前总收入,' end )SF2,"+
			"(case  when SR_3Y is NULL then '三年前总收入,' when SR_3Y='' then '三年前总收入,' when SR_3Y=0 then '三年前总收入,' end )SF3,"+
			"(case  when ZC_1Y is NULL then '一年前总支出,' when ZC_1Y='' then '一年前总支出,' when ZC_1Y=0 then '一年前总支出,' end )SF4,"+
			"(case  when ZC_2Y is NULL then '二年前总支出,' when ZC_2Y='' then '二年前总支出,' when ZC_2Y=0 then '二年前总支出,' end )SF5,"+
			"(case  when ZC_3Y is NULL then '三年前总支出,' when ZC_3Y='' then '三年前总支出,' when ZC_3Y=0 then '三年前总支出,' end )SF6 "+
			"from fin_rpt where cust_id='"+CUST_ID+"' ORDER BY DATE_ID DESC;";
			
			List<Map<String,String>> list = db.queryForList(sql7);
			
			for (Map<String, String> map7 : list) {
				/*String SR_1Y=map7.get("SF1")+"";
				String SR_2Y=map7.get("SF2")+"";
				String SR_3Y=map7.get("SF3")+"";
				String ZC_1Y=map7.get("SF4")+"";
				String ZC_2Y=map7.get("SF5")+"";
				String ZC_3Y=map7.get("SF6")+"";*/
				
				Set<Entry<String,String>> entrySet7 = map7.entrySet();
				String value7="";
				for (Entry<String, String> entry : entrySet7) {
					value7=value7+entry.getValue();
				}
				
				if (value7.length()==0) {
					return;
				}
				
				if(value7.length()>0){
					value7=value7.substring(0,value7.length()-1);
					throw new Exception("财务信息中"+value7+"字段为空值，请核实完善。");
				}			
				
			}		
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteReal", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除数据库客户信息客户信息")
	public String delete(@RequestBody Map<String, Object> map,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("接收的JOSN：" + map.toString());
		try {
			custBaseService.deleteAll(map);//删除客户的所有信息
			this.setSuccess(null, page);
			
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	public void saveFeedBack(Map<String,Object> map){
		map.put("STATE", "LR");
		custFeedbackService.save(map);
	}

}
