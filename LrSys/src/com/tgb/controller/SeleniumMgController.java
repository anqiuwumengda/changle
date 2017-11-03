package com.tgb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.misc.BASE64Decoder;

import com.rmi.SeleniumInputClient;
import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;
import com.tgb.service.cust.SeleniumMgService;
import com.tgb.util.SeleniumUtil;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/selenium")
public class SeleniumMgController extends BaseController {
	Logger log=Logger.getLogger(SeleniumMgController.class); 
	@Autowired
	private SeleniumMgService seleniumMgService;
	@Autowired
	private DataSourceTransactionManager txManager;
	
	@ResponseBody
	@RequestMapping(value = "/queryDsh", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户-自动录入信息")
	public String queryDsh(@RequestBody Map<String,Object> map,HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> mapPeiOu = new HashMap<String,Object>();
		Map<String, Object> dicMap = new HashMap<String, Object>();
		try {
			Map<String, Object> resultList = seleniumMgService.query(map);//查询客户信息
			Map<String, Object> sumFamily = seleniumMgService.sumFamily(map);//查询客户家庭人口信息
			Map<String, Object> famMoney = seleniumMgService.famMoney(map);//查询客户家庭人口财务信息
			if(famMoney==null){
				famMoney= new HashMap<String,Object>();
			}
			if(sumFamily!=null &&!sumFamily.isEmpty()&&sumFamily.get("sumFamily")!=null){
				String sum = sumFamily.get("sumFamily").toString();
				resultList.put("famNum", sum);			
			}else{
				resultList.put("famNum", "1");				
			}
			//查询经理信贷系统密码
			//map.put("USER_ID", this.getSessionUser(request).getUser_id());
			map.put("ORG_CD", this.getSessionUser(request).getOrgCD());
			Map<String, Object> queryPass = seleniumMgService.queryPass(map);
			if(queryPass!=null &&!queryPass.isEmpty()&&queryPass.get("XD_PASS")!=null){
				String base64 = queryPass.get("XD_PASS").toString();
				BASE64Decoder encode2 = new BASE64Decoder();
				byte[]b=encode2.decodeBuffer(base64);
				String str = new String(b);
				resultList.put("password", str);	
				//将查询出来的值拼装成信贷系统识别的参数
				 resultList.put("userName",map.get("USER_ID"));
				//查询配偶信息
				if(!"".equals(resultList.get("JH_FLAG"))&&resultList.get("JH_FLAG")!=null){
					if(SeleniumUtil.isMarried(resultList.get("JH_FLAG").toString()).equals("已婚")){
						mapPeiOu = seleniumMgService.queryRela(map);//查询配偶信息
						if(mapPeiOu==null){
							mapPeiOu= new HashMap<String,Object>();
						}
					}				
				}
				String methodSign=map.get("methodSign").toString();
				SeleniumInputClient seleniumInputClient = new SeleniumInputClient();
				dicMap.put("info", seleniumInputClient.inputInfo(resultList,mapPeiOu, famMoney,methodSign));				
				System.out.println("这是自动录入的结果："+dicMap.get("info"));
				this.setSuccess(dicMap, page);
			}else{
				dicMap.put("info", "请先在此系统设置信贷密码");
				this.setSuccess(dicMap, page);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
}


