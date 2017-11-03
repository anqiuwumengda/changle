/**
 * 
 */
package com.tgb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.cust.CustNzNeedService;
import com.tgb.service.propertymanage.LrdRoleService;

/**
 * @author wjk
 * @category 获取登录用户的信息
 */
@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/OperatorInfo")
public class GetOperatorInfoController extends BaseController{
	@Autowired
	private LrdRoleService lrdRoleService;
	@Autowired
	private CustNzNeedService custNzNeedService;
	@ResponseBody
	@RequestMapping(value = "/getUserInfo", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "获取当前登录用户信息")
	public Object GetUserInfo(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		map.put("USER_ID", user.getUser_id());
		List<Map<String,Object>> userRole = lrdRoleService.queryUserRole(map);//权限
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
		this.setSuccess(userRole, page);
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getNzNeedInfo", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "农资需求测试")
	public String getNzNeedInfo(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CUST_ID", "11111");
		map.put("CRT_DATE", "20171017");
		map.put("NZ_TYPE", "HF");
		map.put("NZ_NAME", "化肥");
		map.put("NUM", "10");
		map.put("MONTH", "5");
		custNzNeedService.save(map);
		return null;
	}
}
