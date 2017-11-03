package com.tgb.mapper.propertymanage;

import java.util.List;
import java.util.Map;

public interface LrdUserBkMapper {

	boolean delete(Map<String,Object> map);
	boolean deleteByPk(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	Map<String,Object> queryMap(Map<String,Object> map);
	List<Map<String, Object>> querList(Map<String, Object> map);
	boolean deleteByUser(Map<String,Object> map);
	void saveRole(Map<String,Object> map);
	List<Map<String, Object>> queryUserRole(Map<String, Object> map2);
}
