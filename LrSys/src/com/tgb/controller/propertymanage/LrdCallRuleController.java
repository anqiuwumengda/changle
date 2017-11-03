package com.tgb.controller.propertymanage;

import hlc.base.db.DbAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rule.BatchThread;
import com.rule.DataInDb;
import com.rule.PackageCsData;
import com.rule.PackageEdcsData;
import com.rule.PackagePfkData;
import com.rule.PackageZr1Data;
import com.rule.PackageZr2Data;
import com.sdnx.st.dp.model.RuleResponseObject;
import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.util.DateTools;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/edjs")
public class LrdCallRuleController extends BaseController implements Runnable {
	Logger log = Logger.getLogger(LrdCallRuleController.class);
	private List<Map<String, String>> list = null;
	private Map<String, String> resMap = new HashMap<String, String>();
	private String batch_date;
	private List<BatchThread>  threadlist = new ArrayList<BatchThread>();
	public LrdCallRuleController() {

	}

	public LrdCallRuleController(List<Map<String, String>> list, User user) {
		this.user = user;
		this.list = list;
	}

	public void batch(Map<String, String> map, User user, String date) {
		String cust_id = map.get("CUST_ID");
		// String date = ;
		if (null != cust_id && !"".equals(cust_id)) {
			try {
				DataInDb dib = new DataInDb();
				Map<String, String> dibMap = new HashMap<String, String>();
				DbAccess db = new DbAccess();
				// String scspSQL = "select * from scsp where cust_id='" +
				// cust_id
				// + "'";
				// Map<String, String> mapScsp = db.queryForMap(scspSQL);
				// if(null==mapScsp || mapScsp.isEmpty()){
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
					dib.insert(dibMap, date);
					dibMap = null;
					PackageZr1Data pzr1 = new PackageZr1Data();
					// 准入1
					RuleResponseObject zr1rro = pzr1.zr1(cust_id, user
							.getUser_id(), wd_id == null ? "@EMPTY@" : wd_id);// 准入1
					// 返回00000并且T进行下一步
					if ("00000".equals(zr1rro.getCode())
							&& "T".equals(zr1rro.getResult())) {
						dib.updateZr1(zr1rro, pzr1.getSendMapMsg().toString(),
								id_no, date);
						// 准入2

						PackageZr2Data pzr2 = new PackageZr2Data();
						RuleResponseObject zr2rro = pzr2.zr2(cust_id, user
								.getUser_id(), wd_id == null ? "@EMPTY@"
								: wd_id);// 准入2
						if (("00000".equals(zr2rro.getCode()) && "T"
								.equals(zr2rro.getResult()))
								|| zr2rro.getDesc().contains("数据源")) {
							dib.updateZr2(zr2rro, pzr2.getSendMapMsg()
									.toString(), id_no, date);
							// 评分卡
							PackagePfkData pfk = new PackagePfkData();
							RuleResponseObject pfkrro = pfk.pfk(cust_id, user
									.getUser_id(), wd_id == null ? "@EMPTY@"
									: wd_id);// 评级
							if ("00000".equals(pfkrro.getCode())) {
								dib.updatePfk(pfkrro, pfk.getSendMapMsg()
										.toString(), id_no, date);
								// 额度
								PackageEdcsData ped = new PackageEdcsData();
								ped.setNPSCORE(String.valueOf(pfkrro
										.getCseqLmt()));
								RuleResponseObject pedRro = ped.edcs(cust_id,
										user.getUser_id(),
										wd_id == null ? "@EMPTY@" : wd_id);// 额度
								if ("00000".equals(pedRro.getCode())) {
									dib.updateEd(pedRro, ped.getSendMapMsg()
											.toString(), id_no, date);
									// 初审
									PackageCsData pcd = new PackageCsData();
									pcd.setNPSCORE(String.valueOf(pfkrro
											.getCseqLmt()));
									pcd.setCSEQLMT(String.valueOf(pedRro
											.getCseqLmt()));
									pcd.setRRMODEL(pfkrro.getRrModel());
									RuleResponseObject pcdRro = pcd.cs(cust_id,
											user.getUser_id(),
											wd_id == null ? "@EMPTY@" : wd_id);
									if ("00000".equals(pcdRro.getCode())) {
										dib.updateCs(pcdRro, pcd
												.getSendMapMsg().toString(),
												id_no, date);
										dib.updEnd(pfkrro.getCseqLmt(),
												cust_id, id_no,
												pedRro.getCseqLmt(), date);
										resMap.put("ED_LMT", pedRro
												.getCseqLmt().toString());
										resMap.put("PJ_JB", pfkrro.getCseqLmt()
												.toString());
										pzr1 = null;
										zr1rro = null;
										pzr2 = null;
										zr2rro = null;
										pfk = null;
										pfkrro = null;
										ped = null;
										pedRro = null;
										pcd = null;
										pcdRro = null;

										this.setSuccess(null, page);
										// System.gc();
									} else {
										dib.updateCs(pcdRro, pcd
												.getSendMapMsg().toString(),
												id_no, date);
										this.setErr(page, pcdRro.getDesc());
									}
								} else {
									dib.updateEd(pedRro, ped.getSendMapMsg()
											.toString(), id_no, date);
									this.setErr(page, pedRro.getDesc());
								}
							} else {
								dib.updatePfk(pfkrro, pfk.getSendMapMsg()
										.toString(), id_no, date);
								this.setErr(page, pfkrro.getDesc());
							}
						} else {
							dib.updateZr2(zr2rro, pzr2.getSendMapMsg()
									.toString(), id_no, date);
							this.setErr(page, zr2rro.getDesc());
						}
					} else {
						// 返回错误入库
						dib.updateZr1(zr1rro, pzr1.getSendMapMsg().toString(),
								id_no, date);
						this.setErr(page, zr1rro.getDesc());
					}
				} else {
					this.setErr(page, "错误信息：身份证号为空");
				}
				// }

				dib = null;
			} catch (Exception e) {
				e.printStackTrace();
				this.setErr(page, e.getMessage());
			}
		} else {
			this.setErr(page, "错误信息：客户编号为空");
		}
	}

	/*
	 * public String single(User user) throws Exception { // Map<String, String>
	 * map = new HashMap<String, String>(); DbAccess db = new DbAccess();
	 * List<Map<String, String>> list = db .queryForList(
	 * "select CUST_ID from cust_base where id_no='370725196307120696'");
	 * batch(list.get(0), user); return "success"; }
	 */
	@ResponseBody
	@RequestMapping(value = "/callbatchrule", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "批量审查审批批量")
	public String callrule(@RequestBody Map<String, String> map,
			HttpServletRequest request) throws Exception {
		// Map<String, String> map = new HashMap<String, String>();
		// log.info("接收的JSON:" + map.toString());
		DbAccess db = new DbAccess();
		batch_date= map.get("BATCH_DATE");
		String sql11 = "select * from batch_log where stat='0'";
		List<Map<String, String>> t = db.queryForList(sql11);
		if (null == t || t.size() == 0) {
			try {
				User user = this.getSessionUser(request);
				// single(user);

				String delSql = "delete from batch_log where BATCH_DATE='"
						+ map.get("BATCH_DATE") + "'";
				db.executeUpdate(delSql);
				db.executeUpdate("insert into batch_log values('01','"
						+ DateTools.getCurrentSysData("yyyyMMdd HH:mm:ss")
						+ "','','0','" + map.get("BATCH_DATE") + "')");

				List<Map<String, String>> list = db
						.queryForList("select CUST_ID from cust_base where mtn_date='"
								+ map.get("BATCH_DATE")
								+ "' and (CREATE_TYPE='2' or CREATE_TYPE='4')");
				// List<Map<String,String>> list =
				// db.queryForList("select CUST_ID from cust_base where id_no='370725196607150029'");
				int ii = 0;
				if(null!=list && list.size()>200){
					int sum = list.size();
					int threadNum = 20;
					int nuitNum = sum % threadNum;
					int j = sum / threadNum;
					for (int i = 0; i < threadNum; i++) {
						
						/*Thread th= new Thread(new LrdCallRuleController(list.subList(i * j, (i + 1)
								* j - 1), user));*/
						/*LrdCallRuleController lt = new LrdCallRuleController(list.subList(i * j, (i + 1)
								* j - 1), user);*/
						BatchThread bt =new BatchThread(list.subList(i * j, (i + 1)
								* j - 1),user,this.batch_date);
						bt.start();
						threadlist.add(bt);
						
					}
					if(j>0){
						/*Thread th =  new Thread(new LrdCallRuleController(list.subList(threadNum * j, threadNum * j
								+ nuitNum), user));*/
						BatchThread bt =new BatchThread(list.subList(threadNum * j, threadNum * j
								+ nuitNum),user,this.batch_date);
						
						bt.start();
						threadlist.add(bt);
					}
					if(threadlist.size()>0){
						for(int i=1;i>0;){
							boolean isA=false;
							for(BatchThread tt :threadlist){
								if(tt.isAlive()){
									isA=true;
									continue;
								}
							}
							if(!isA){
								db.executeUpdate("update batch_log set STAT='2',END_TIME='"
										+ DateTools.getCurrentSysData("yyyyMMdd HH:mm:ss")
										+ "' where BATCH_DATE='" + map.get("BATCH_DATE") + "'");
								break;
							}
						}
					}
				}else{
					BatchThread bt =new BatchThread(list,user,this.batch_date);
					
					bt.start();
					threadlist.add(bt);
					for(int i=1;i>0;){
						boolean isA=false;
						for(BatchThread tt :threadlist){
							if(tt.isAlive()){
								isA=true;
								continue;
							}
						}
						if(!isA){
							db.executeUpdate("update batch_log set STAT='2',END_TIME='"
									+ DateTools.getCurrentSysData("yyyyMMdd HH:mm:ss")
									+ "' where BATCH_DATE='" + map.get("BATCH_DATE") + "'");
							break;
						}
					}
					/*for (Map<String, String> map2 : list) {
						batch(map2, user, map.get("BATCH_DATE"));
						ii++;
					}*/
					/*db.executeUpdate("update batch_log set STAT='2',END_TIME='"
							+ DateTools.getCurrentSysData("yyyyMMdd HH:mm:ss")
							+ "' where BATCH_DATE='" + map.get("BATCH_DATE") + "'");*/
				}
				
				/*db.executeUpdate("update batch_log set STAT='2',END_TIME='"
						+ DateTools.getCurrentSysData("yyyyMMdd HH:mm:ss")
						+ "' where BATCH_DATE='" + map.get("BATCH_DATE") + "'");*/
				Map<String, String> resmap = new HashMap<String, String>();
				this.setSuccess(resmap.put("NUM", String.valueOf(ii)), page);
			} catch (Exception e) {
				db.executeUpdate("update batch_log set STAT='1',END_TIME='"
						+ DateTools.getCurrentSysData("yyyyMMdd HH:mm:ss")
						+ "' where BATCH_DATE='" + map.get("BATCH_DATE") + "'");
				this.setErr(page, e.getMessage());
			}
		} else {
			this.setErr(page, "批量正在运行请稍后");
		}

		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/callruleAll", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "批量审查审批-全量")
	public String callruleAll(HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		log.info("接收的JSON:" + map.toString());
		User user = this.getSessionUser(request); //
		DbAccess db = new DbAccess();
		List<Map<String, String>> list = db
				.queryForList("select CUST_ID from cust_base ");
		// List<Map<String,String>> list = db.queryForList(
		// "select CUST_ID from cust_base where id_no='370725196607150029'");
		int sum = list.size();
		int threadNum = 20;
		int nuitNum = sum % threadNum;
		int j = sum / threadNum;
		for (int i = 0; i < threadNum; i++) {
			new Thread(new LrdCallRuleController(list.subList(i * j, (i + 1)
					* j - 1), user)).start();
		}
		if(j>0){
		new Thread(new LrdCallRuleController(list.subList(threadNum * j, threadNum * j
				+ nuitNum), user)).start();
		}
		this.setSuccess(null, page);
		return this.js.toString();
	}

	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value = "/callrule_add", produces =
	 * "application/json;charset=UTF-8", method = { RequestMethod.POST })
	 * 
	 * @SystemControllerLog(description = "批量审查审批-增量") public String
	 * callrule_add(@RequestBody Map<String,String> map,HttpServletRequest
	 * request) throws Exception { //Map<String, String> map = new
	 * HashMap<String, String>(); log.info("接收的JSON:" + map.toString()); User
	 * user = this.getSessionUser(request); // single(user); DbAccess db = new
	 * DbAccess(); List<Map<String,String>> list = db.queryForList(
	 * "select CUST_ID from cust_base where  CREATE_TYPE='2' and isdel<>'1'");
	 * //List<Map<String,String>> list = db.queryForList(
	 * "select CUST_ID from cust_base where id_no='370725196607150029'"); int
	 * sum = list.size(); int threadNum = 30; int nuitNum = sum%threadNum; int
	 * j=sum/threadNum; for(int i=0;i<threadNum;i++){ new Thread(new
	 * LrdCallRuleController(list.subList(i*j, (i+1)*j-1), user)).start(); } new
	 * Thread(new LrdCallRuleController(list.subList(10*j, 10*j+nuitNum),
	 * user)).start();
	 * 
	 * this.setSuccess(null, page); return this.js.toString(); }
	 */

	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value = "/batchcallrule", produces =
	 * "application/json;charset=UTF-8", method = { RequestMethod.POST })
	 * 
	 * @SystemControllerLog(description = "查询系统") public String
	 * batchcallrule(@RequestBody Map<String,String> map,HttpServletRequest
	 * request) throws Exception { //Map<String, String> map = new
	 * HashMap<String, String>(); log.info("接收的JSON:" + map.toString()); User
	 * user = this.getSessionUser(request); DbAccess db = new DbAccess();
	 * List<Map<String,String>> list =
	 * db.queryForList("select CUST_ID from cust_base"); for(Map<String,String>
	 * map2 :list){ batch(map2,user); }
	 * 
	 * return this.js.toString(); }
	 */

	@Override
	public void run() {
		int i = 0;
		for (Map<String, String> map2 : list) {
			i++;
			batchAll(map2, user, this.batch_date);
			//try {
				if (i == 10) {
					//Thread.sleep(3000);
				}
			//} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
			if (i == 100) {

				//System.runFinalization();
				//System.gc();
				i = 0;
			}

		}
		/*DbAccess db=null;
		try {
			db = new DbAccess();
			db.executeUpdate("update batch_log set STAT='2',END_TIME='"
					+ DateTools.getCurrentSysData("yyyyMMdd HH:mm:ss")
					+ "' where BATCH_DATE='" + this.batch_date+ "'");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

	@ResponseBody
	@RequestMapping(value = "/callrule_unit", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "审查审批-单笔")
	public String callrule_unit(@RequestBody Map<String, String> map,
			HttpServletRequest request) throws Exception {
		// Map<String, String> map = new HashMap<String, String>();
		log.info("接收的JSON:" + map.toString());
		User user = this.getSessionUser(request);
		String cust_id = map.get("CUST_ID");
		// single(user);
		if (null != cust_id && !"".equals(cust_id)) {
			batch(map, user, DateTools.getCurrentSysData("yyyyMMdd"));
			// this.setSuccess(this.resMap, page);
		} else {
			this.setErr(page, "客户编号为空");
		}

		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/clearLog", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "清楚规则日志")
	public String clearLog(HttpServletRequest request) throws Exception {
		// Map<String, String> map = new HashMap<String, String>();
		try {
			String sql1 = "truncate table  st_rule_log";
			String sql2 = "truncate table  hn_cust_pj";
			String sql3 = "truncate table   hn_cust_zr";
			String sql4 = "truncate table  st_log_inputdata";
			String sql5 = "truncate table  st_rate_result";
			String sql6 = "truncate table  st_rateresult_data";
			DbAccess db = new DbAccess();
			db.executeUpdate(sql1);
			db.executeUpdate(sql2);
			db.executeUpdate(sql3);
			db.executeUpdate(sql4);
			db.executeUpdate(sql5);
			db.executeUpdate(sql6);
			this.setSuccess(null, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		// single(user);

		return this.js.toString();
	}
	
	
	public void batchAll(Map<String, String> map, User user, String date) {
		String cust_id = map.get("CUST_ID");
		// String date = ;
		if (null != cust_id && !"".equals(cust_id)) {
			try {
				DataInDb dib = new DataInDb();
				Map<String, String> dibMap = new HashMap<String, String>();
				DbAccess db = new DbAccess();
				 String scspSQL = "select * from scsp where cust_id='" +
				 cust_id
				 + "'";
				 Map<String, String> mapScsp = db.queryForMap(scspSQL);
				 if(null==mapScsp || mapScsp.isEmpty()){
				String wd_id = db.queryForMap(
						"select wd_id from lrd_org where org_cd ='"
								+ user.getOrgCD() + "'").get("wd_id");
				Map<String, String> map1 = db
						.queryForMap("select JTZYXM,ID_NO,CUST_GRP_JL,CREATE_TYPE from cust_base where cust_id ='"
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
				String create_type=map1.get("CREATE_TYPE");
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
					dib.insert(dibMap, date);
					dibMap = null;
					PackageZr1Data pzr1 = new PackageZr1Data();
					// 准入1
					RuleResponseObject zr1rro = pzr1.zr1(cust_id, user
							.getUser_id(), wd_id == null ? "@EMPTY@" : wd_id);// 准入1
					// 返回00000并且T进行下一步
					if ("00000".equals(zr1rro.getCode())
							&& "T".equals(zr1rro.getResult())) {
						dib.updateZr1(zr1rro, pzr1.getSendMapMsg().toString(),
								id_no, date);
						// 准入2

						PackageZr2Data pzr2 = new PackageZr2Data();
						RuleResponseObject zr2rro = pzr2.zr2(cust_id, user
								.getUser_id(), wd_id == null ? "@EMPTY@"
								: wd_id);// 准入2
						if (("00000".equals(zr2rro.getCode()) && "T"
								.equals(zr2rro.getResult()))
								|| zr2rro.getDesc().contains("数据源")) {
							dib.updateZr2(zr2rro, pzr2.getSendMapMsg()
									.toString(), id_no, date);
							//******************************信息完整继续执行
							if(null!=create_type && ("2".equals(create_type) || "4".equals(create_type))){
								// 评分卡
								PackagePfkData pfk = new PackagePfkData();
								RuleResponseObject pfkrro = pfk.pfk(cust_id, user
										.getUser_id(), wd_id == null ? "@EMPTY@"
										: wd_id);// 评级
								if ("00000".equals(pfkrro.getCode())) {
									dib.updatePfk(pfkrro, pfk.getSendMapMsg()
											.toString(), id_no, date);
									// 额度
									PackageEdcsData ped = new PackageEdcsData();
									ped.setNPSCORE(String.valueOf(pfkrro
											.getCseqLmt()));
									RuleResponseObject pedRro = ped.edcs(cust_id,
											user.getUser_id(),
											wd_id == null ? "@EMPTY@" : wd_id);// 额度
									if ("00000".equals(pedRro.getCode())) {
										dib.updateEd(pedRro, ped.getSendMapMsg()
												.toString(), id_no, date);
										// 初审
										PackageCsData pcd = new PackageCsData();
										pcd.setNPSCORE(String.valueOf(pfkrro
												.getCseqLmt()));
										pcd.setCSEQLMT(String.valueOf(pedRro
												.getCseqLmt()));
										pcd.setRRMODEL(pfkrro.getRrModel());
										RuleResponseObject pcdRro = pcd.cs(cust_id,
												user.getUser_id(),
												wd_id == null ? "@EMPTY@" : wd_id);
										if ("00000".equals(pcdRro.getCode())) {
											dib.updateCs(pcdRro, pcd
													.getSendMapMsg().toString(),
													id_no, date);
											dib.updEnd(pfkrro.getCseqLmt(),
													cust_id, id_no,
													pedRro.getCseqLmt(), date);
											resMap.put("ED_LMT", pedRro
													.getCseqLmt().toString());
											resMap.put("PJ_JB", pfkrro.getCseqLmt()
													.toString());
											pzr1 = null;
											zr1rro = null;
											pzr2 = null;
											zr2rro = null;
											pfk = null;
											pfkrro = null;
											ped = null;
											pedRro = null;
											pcd = null;
											pcdRro = null;

											this.setSuccess(null, page);
											// System.gc();
										} else {
											dib.updateCs(pcdRro, pcd
													.getSendMapMsg().toString(),
													id_no, date);
											this.setErr(page, pcdRro.getDesc());
										}
									} else {
										dib.updateEd(pedRro, ped.getSendMapMsg()
												.toString(), id_no, date);
										this.setErr(page, pedRro.getDesc());
									}
								} else {
									dib.updatePfk(pfkrro, pfk.getSendMapMsg()
											.toString(), id_no, date);
									this.setErr(page, pfkrro.getDesc());
								}
							}
							
						} else {
							dib.updateZr2(zr2rro, pzr2.getSendMapMsg()
									.toString(), id_no, date);
							this.setErr(page, zr2rro.getDesc());
						}
					} else {
						// 返回错误入库
						dib.updateZr1(zr1rro, pzr1.getSendMapMsg().toString(),
								id_no, date);
						this.setErr(page, zr1rro.getDesc());
					}
				} else {
					this.setErr(page, "错误信息：身份证号为空");
				}
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