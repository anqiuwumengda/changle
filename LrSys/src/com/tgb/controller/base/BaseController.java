package com.tgb.controller.base;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import net.sf.json.JSONObject;

import com.tgb.controller.CustZzController;
import com.tgb.model.User;
import com.tgb.page.PageContext;
import com.tgb.util.ControllerUtil;
import com.tgb.util.DateTools;

public class BaseController {
	public JSONObject js = new JSONObject();
	public PageContext page = null;
	public User user;
	Logger log=Logger.getLogger(BaseController.class); 
	public void setSuccess(Object relust, PageContext page) {
		this.js.put("code", ControllerUtil.SUCCESSCODE);
		if(relust instanceof List){
			List<Map<String,Object>> list = (List<Map<String,Object>>)relust;
			for(Map<String,Object> map : list){
				for(String key :map.keySet()){
					if(null == map.get(key)){
						map.put(key, "");
					}
				}
			}
			this.js.put("result", list);
		}else if(relust instanceof Map){
			Map<String,Object> map = (Map<String,Object>)relust;
				for(String key :map.keySet()){
					if(null == map.get(key)){
						map.put(key, "");
					}
				}
			this.js.put("result", map);
		}
		
		
		if (null != page) {
			this.js.put("page", page);
			this.page.removeContext();
		}
	}

	public void setErr( PageContext page, String errMsg) {
		log.info(errMsg);
		this.js.put("code", ControllerUtil.ERRCODE);
		this.js.put("errMsg", errMsg);
		if (null != page) {
			this.page.removeContext();
		}
	}
	public void setErrCode( String code ,PageContext page, String errMsg) {
		log.info(errMsg);
		this.js.put("code", code);
		this.js.put("errMsg", errMsg);
		if (null != page) {
			this.page.removeContext();
		}
	}
	public PageContext isPage(Map map) {
		// PageContext page = null;
		if (null != map && !map.isEmpty()) {
			Object obj = map.get("isPagination");
			if (null != obj && !"".equals(obj) && "true".equals(obj.toString())) {
				page = PageContext.getContext();
				page.setPagination(true);
				Object currPage = map.get("currentPage");
				Object pageSize = map.get("pageSize");
				Object totalPages = map.get("totalPages");
				if (null != totalPages && !"".equals(totalPages.toString())) {
					page.setCurrentPage(Integer.parseInt(totalPages.toString()));
					map.remove("totalPages");
				}
				if (null != currPage && !"".equals(currPage.toString())) {
					page.setCurrentPage(Integer.parseInt(currPage.toString()));
					map.remove("currentPage");
				}
				if (null != pageSize && !"".equals(pageSize.toString())) {
					page.setPageSize(Integer.parseInt(pageSize.toString()));
					map.remove("pageSize");
				}
				map.put("page", page);
				map.remove("isPagination");
				return page;
			} else
				return page;
		} else {
			return page;
		}
	}
	/**
	 * 设置map用户信息，通用;例如创建人，修改人，创建日期，修改日期
	 */
	public void setBaseInfo(Map map,HttpServletRequest request){
		map.put("createUser", "");
		map.put("createDate", DateTools.getCurrentSysData("yyyy-MM-dd hh:mm:ss"));
		map.put("updateUser", "");
		map.put("updateDate", DateTools.getCurrentSysData("yyyy-MM-dd hh:mm:ss"));
	
	}
	public TransactionStatus getTransactionStatus(DataSourceTransactionManager txManager){
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = txManager.getTransaction(def); 
		return status;
	}
	public User getSessionUser(HttpServletRequest request){
		return (User)request.getSession().getAttribute("user");
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
