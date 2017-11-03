package com.tgb.service.propertymanage;

import java.util.List;
import java.util.Map;

public interface LrdUserService {

	boolean deleteByPk(Map<String,Object> map);
	boolean delete(Map<String,Object> map);
	boolean deleteByUser(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String,Object> map);
	void saveRole(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	
	List<Map<String, Object>> queryList(Map<String, Object> map);
	Map<String, Object> queryMap(Map<String, Object> map);
	List<Map<String, Object>> queryUserRole(Map<String, Object> map2);
	
	
}
