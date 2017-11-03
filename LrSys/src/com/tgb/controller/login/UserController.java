package com.tgb.controller.login;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.CustApplyController;
import com.tgb.controller.base.BaseController;
import com.tgb.service.login.UserService;
import com.tgb.util.DateTools;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	Logger log=Logger.getLogger(UserController.class); 
	@Autowired
	private UserService userService;

	@ResponseBody
	@RequestMapping(value = "/updatePwd", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "维护客户-授权信息")
	public String save(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		map.put("USER_ID", this.getSessionUser(request).getUser_id());
		try {
			// User user = this.getSessionUser(request);
			Map<String, Object> resultMap = userService.queryMap(map);
			
			if (null != resultMap && resultMap.size() > 0) {
				map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				map.put("PW_MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				userService.update(map);
			}
			
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
}
