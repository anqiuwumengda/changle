package com.tgb.controller.propertymanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.tgb.service.propertymanage.StandardLrdDicService;
import com.tgb.util.DateTools;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/slrddic")
public class StandardLrdDicController extends BaseController {
	Logger log = Logger.getLogger(LrdFunctionController.class);
	@Autowired
	private StandardLrdDicService standardLrdDicService;
	@Autowired
	private DataSourceTransactionManager txManager;

	@ResponseBody
	@RequestMapping(value = "/queryPK", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "标准字典--列表查询")
	public String queryByPK(HttpServletRequest request) {
		// log.info("接收的JSON:" + map.toString());
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> resultList = standardLrdDicService
					.queryPK(map);

			this.setSuccess(resultList, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/queryTree", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "标准字典--列表查询")
	public String queryTree(HttpServletRequest request) {
		// log.info("接收的JSON:" + map.toString());
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> resultList = standardLrdDicService
					.queryTree(map);

			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/queryByPK", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "标准字典-法人信息查询")
	public String qurey(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			String id = map.get("pId").toString();
			map.put("DIC_PARENTID", id);
			List<Map<String, Object>> resultList = standardLrdDicService
					.queryByPK(map);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "标准字典-添加")
	public String save(HttpServletRequest request,
			@RequestBody List<Map<String, Object>> map) {
		log.info("接收的JSON:" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {

			for (Map<String, Object> map2 : map) {
				List<Map<String, Object>> resultList = standardLrdDicService
						.queryBefore(map2);
				if (resultList.size() > 0) {
					this.setErr(null, "数据已存在");
					return this.js.toString();
				} else {
					standardLrdDicService.save(map2);
				}
			}
			txManager.commit(status);
			this.setSuccess(null, page);

		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "标准字典--修改")
	public String update(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			boolean update = standardLrdDicService.update(map);
			this.setSuccess(update, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "标准字典--删除")
	public String delete(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		Map<String, Object> queryMap=new HashMap<String, Object>();
		try {
			List<Map<String, Object>> queryByPK = standardLrdDicService
					.queryBefore(queryMap);
			if (queryByPK.size() > 0) {
				for (int i = 0; i < queryByPK.size(); i++) {
					boolean delete = standardLrdDicService.delete(queryByPK.get(i));
					
				}
			}
			boolean delete = standardLrdDicService.delete(map);
			this.setSuccess(delete, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

}
