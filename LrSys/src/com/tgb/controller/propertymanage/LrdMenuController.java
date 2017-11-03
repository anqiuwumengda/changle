package com.tgb.controller.propertymanage;

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
import com.tgb.service.propertymanage.LrdMenuService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/lrdmenu")
public class LrdMenuController extends BaseController {
	Logger log = Logger.getLogger(LrdMenuController.class);
	@Autowired
	private LrdMenuService lrdMenuService;
	@Autowired
	private DataSourceTransactionManager txManager;

	@ResponseBody
	@RequestMapping(value = "/querymenu", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询左侧菜单列表")
	public String queryTree(@RequestBody Map<String,String> map2,HttpServletRequest request) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			User user = this.getSessionUser(request);
			map.put("user_id", user.getUser_id());
			List<Map<String, Object>> resultList = lrdMenuService.queryTree(map);
			String ip = map2.get("ip");
			if(null!=ip && !"".equals(ip)){
				for(Map<String, Object> tmp:resultList){
					String url = tmp.get("FUNC_URL").toString();
					if(url.contains(".jsp")){
						tmp.put("FUNC_URL", ip+url.substring(url.indexOf("./")+2, url.length()));
					}
				}
			}
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	

}
