package com.tgb.service.propertymanage;

import java.util.List;
import java.util.Map;

public interface LrdRuleService {

	boolean delete(Map<String,Object> map);
	List<Map<String,Object>> queryList(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	Map<String, Object> queryById(Map<String, Object> map);
}
