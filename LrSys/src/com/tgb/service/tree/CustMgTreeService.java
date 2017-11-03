package com.tgb.service.tree;

import java.util.List;
import java.util.Map;


public interface CustMgTreeService {
	boolean deleteByPk(Map<String,Object> map);
	boolean delete(Map<String,Object> map);
	List<Map<String,Object>> queryJL(Map<String,Object> map);
	List<Map<String,Object>> queryOne(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	
	List<Map<String,Object>> queryAreaByCustJl(Map<String,Object> map);
	List<Map<String, Object>> queryOrgList(Map<String, Object> map);
	List<Map<String, Object>> queryAllMenu(Map<String, Object> map);
	List<Map<String, Object>> queryList(Map<String, Object> map);
	List<Map<String, Object>> queryByLike(Map<String, Object> map);
	//需求筛选
	List<Map<String, Object>> filterQuery(Map<String, Object> map);
}
