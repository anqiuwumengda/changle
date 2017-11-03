package com.tgb.service.propertymanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface LrdRoleService {
	boolean deleteByPk(Map<String,Object> map);
	boolean delete(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	List<Map<String, Object>> queryList(Map<String, Object> map);
	List<Map<String, Object>> queryFunc(Map<String, Object> map);
	List<Map<String, Object>> queryUserRole(Map<String, Object> map);
	void updateFunc(Map<String, Object> hashMap);
	void deleteFunc(HashMap<String, Object> hashMap);
	Map<String, Object> queryMap(Map<String, Object> map);
	void insertFunc(HashMap<String, Object> hashMap);
	
}
