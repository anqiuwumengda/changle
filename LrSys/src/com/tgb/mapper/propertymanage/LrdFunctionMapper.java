package com.tgb.mapper.propertymanage;

import java.util.List;
import java.util.Map;

public interface LrdFunctionMapper {

	boolean delete(Map<String,Object> map);
	boolean deleteByPk(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	Map<String,Object> queryMap(Map<String,Object> map);
	
	List<Map<String,Object>> queryByPK(Map<String,Object> map);
	List<Map<String, Object>> queryTree(Map<String, Object> map);
}
