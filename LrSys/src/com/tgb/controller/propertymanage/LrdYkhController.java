package com.tgb.controller.propertymanage;

import hlc.base.db.DbAccess;

import java.util.ArrayList;
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

import com.rule.BatchThread;
import com.rule.BatchThread_Ykh;
import com.rule.DataInDb_Ykh;
import com.rule.PackageZr1Data;
import com.rule.PackageZr2Data;
import com.sdnx.st.dp.model.RuleResponseObject;
import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.propertymanage.LrdYkhService;
import com.tgb.util.ControllerUtil;
import com.tgb.util.DateTools;
import com.tgb.util.DicReplace;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/lrdykh")
public class LrdYkhController extends BaseController {
	private List<BatchThread_Ykh> threadlist = new ArrayList<BatchThread_Ykh>();

	Logger log = Logger.getLogger(LrdYkhController.class);
	@Autowired
	private LrdYkhService lrdYkhService;

	@Autowired
	private DataSourceTransactionManager txManager;

	@ResponseBody
	@RequestMapping(value = "/querygzdw", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	public String queryTree() {
		// log.info("接收的JSON:" + map.toString());
		try {
			List<String> list = lrdYkhService.querygzdwname();
			System.out.println(list);
			this.js.put("name", list);
			this.js.put("code", ControllerUtil.SUCCESSCODE);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	// 职工单位检索
		@ResponseBody
		@RequestMapping(value = "/queryZgdwByWorkName", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
		@SystemControllerLog(description = "职工单位检索")
		public String queryZgdwByWorkName(@RequestBody Map<String, String> map,
				HttpServletRequest request) {
			// log.info("接收的JSON:" + map.toString());
			try {
				isPage(map);
				List<Map<String, Object>> list = lrdYkhService.queryZgdwByWorkName(map);
				Map<String, String> dicMap = new HashMap<String, String>();
				dicMap.put("CREATE_TYPE", "ZTBJ");
				DicReplace.replaceDic(list, dicMap);
				this.setSuccess(list, page);
			} catch (Exception e) {
				this.setErr(page, e.getMessage());
			}
			return this.js.toString();
		}
	// 职工单位首页列表
	@ResponseBody
	@RequestMapping(value = "/querygzdwlist", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	public String queryGzdwList(@RequestBody Map<String, String> map,
			HttpServletRequest request) {
		// log.info("接收的JSON:" + map.toString());
		try {
			this.js.clear();
			isPage(map);
			List<Map<String, Object>> list = lrdYkhService.queryZgdwList(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("CREATE_TYPE", "ZTBJ");
			DicReplace.replaceDic(list, dicMap);
			this.setSuccess(list, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	// 调用行内接口，只调用准入1、准入2
	@ResponseBody
	@RequestMapping(value = "/callbatchrule", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "批量审查审批批量")
	public String callrule(
			HttpServletRequest request) throws Exception {
		DbAccess db = new DbAccess();
		try {
			User user = this.getSessionUser(request);

			List<Map<String, String>> list = db
					.queryForList("select CUST_ID from cust_base where CREATE_TYPE='8' ");

			int ii = 0;
				//else {
				BatchThread_Ykh bt = new BatchThread_Ykh(list, user);

				bt.start();
				threadlist.add(bt);
				for (int i = 1; i > 0;) {
					boolean isA = false;
					for (BatchThread_Ykh tt : threadlist) {
						if (tt.isAlive()) {
							isA = true;
							continue;
						}
					}
					if (!isA) {
						
						Map<String, String> resmap = new HashMap<String, String>();
						this.setSuccess(resmap.put("NUM", String.valueOf(ii)), page);
						break;
					}
				}
				//this.setSuccess(null, page);
		} catch (Exception e) {

			this.setErr(page, e.getMessage());
		}

		return this.js.toString();
	}

	public void batchYkh(Map<String, String> map, User user) {
		String cust_id = map.get("CUST_ID");
		if (null != cust_id && !"".equals(cust_id)) {
			try {
				DataInDb_Ykh dib = new DataInDb_Ykh();
				Map<String, String> dibMap = new HashMap<String, String>();
				DbAccess db = new DbAccess();
				String wd_id = db.queryForMap(
						"select wd_id from lrd_org where org_cd ='"
								+ user.getOrgCD() + "'").get("wd_id");
				Map<String, String> map1 = db
						.queryForMap("select JTZYXM,ID_NO,CUST_GRP_JL from cust_base where cust_id ='"
								+ cust_id + "'");
				String custGrpJL = map1.get("CUST_GRP_JL");
				String custGrpJlOrg_cd = db.queryForMap(
						"select ORG_CD from lrd_user where user_id='"
								+ custGrpJL + "'").get("ORG_CD");
				String org_name = db.queryForMap(
						"select ORG_NAME from lrd_org where org_cd = '"
								+ custGrpJlOrg_cd + "'").get("ORG_NAME");
				String zyxm = map1.get("JTZYXM");
				String id_no = map1.get("ID_NO");
				if (null != id_no && !"".equals(id_no)) {
					dibMap.put("CUST_ID", cust_id);
					dibMap.put("ORG_NAME", org_name);
					dibMap.put("ZYXM", zyxm);
					dibMap.put("ID_NO", id_no);
					dibMap.put("CUST_GRP_GL", map1.get("CUST_GRP_JL"));
					dibMap.put(
							"CUST_GRP_GL_NAME",
							db.queryForMap(
									"select USER_NAME from lrd_user where user_id='"
											+ map1.get("CUST_GRP_JL") + "'")
									.get("USER_NAME"));
					dibMap.put("OPERID", user.getUser_id());
					// 先删除历史记录
					dib.delete(id_no);
					dib.insert(dibMap,
							DateTools.getCurrentSysData("yyyy-MM-dd"));
					dibMap = null;
					PackageZr1Data pzr1 = new PackageZr1Data();
					// 准入1
					RuleResponseObject zr1rro = pzr1.zr1(cust_id, user
							.getUser_id(), wd_id == null ? "@EMPTY@" : wd_id);// 准入1
					// 返回00000并且T进行下一步
					if ("00000".equals(zr1rro.getCode())
							&& "T".equals(zr1rro.getResult())) {
						dib.updateZr1(zr1rro, pzr1.getSendMapMsg().toString(),
								id_no,
								DateTools.getCurrentSysData("yyyy-MM-dd"));
						// 准入2

						PackageZr2Data pzr2 = new PackageZr2Data();
						RuleResponseObject zr2rro = pzr2.zr2(cust_id, user
								.getUser_id(), wd_id == null ? "@EMPTY@"
								: wd_id);// 准入2
						if (("00000".equals(zr2rro.getCode()) && "T"
								.equals(zr2rro.getResult()))
								|| zr2rro.getDesc().contains("数据源")) {
							dib.updateZr2(zr2rro, pzr2.getSendMapMsg()
									.toString(), id_no, DateTools
									.getCurrentSysData("yyyy-MM-dd"));

						} else {
							dib.updateZr2(zr2rro, pzr2.getSendMapMsg()
									.toString(), id_no, DateTools
									.getCurrentSysData("yyyy-MM-dd"));
							this.setErr(page, zr2rro.getDesc());
						}
					} else {
						// 返回错误入库
						dib.updateZr1(zr1rro, pzr1.getSendMapMsg().toString(),
								id_no,
								DateTools.getCurrentSysData("yyyy-MM-dd"));
						this.setErr(page, zr1rro.getDesc());
					}
				} else {
					this.setErr(page, "错误信息：身份证号为空");
				}

				dib = null;
			} catch (Exception e) {
				e.printStackTrace();
				this.setErr(page, e.getMessage());
			}
		} else {
			this.setErr(page, "错误信息：客户编号为空");
		}
	}

}
