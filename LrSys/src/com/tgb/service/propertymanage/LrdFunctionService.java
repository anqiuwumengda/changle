package com.tgb.service.propertymanage;

import java.util.List;
import java.util.Map;

public interface LrdFunctionService {

	boolean deleteByPk(Map<String,Object> map);
	boolean delete(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	
	List<Map<String,Object>> queryByPK(Map<String,Object> map);
	
	List<Map<String, Object>> queryTree(Map<String, Object> map);
}
