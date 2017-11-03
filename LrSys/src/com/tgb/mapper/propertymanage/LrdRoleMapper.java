package com.tgb.mapper.propertymanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface LrdRoleMapper {

	boolean delete(Map<String,Object> map);
	boolean deleteByPk(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	Map<String,Object> queryMap(Map<String,Object> map);
	List<Map<String, Object>> queryUserRole(Map<String, Object> map);

	
	List<Map<String, Object>> queryList(Map<String, Object> map);
	List<Map<String, Object>> queryFunc(Map<String, Object> map);
	void updateFunc(Map<String, Object> map);
	void deleteFunc(HashMap<String, Object> hashMap);
}
