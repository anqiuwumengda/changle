package com.tgb.service.propertymanage;

import java.util.List;
import java.util.Map;

public interface LrdOrgService {
	boolean deleteByPk(Map<String,Object> map);
	boolean delete(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	List<Map<String,Object>> queryMenu(Map<String, Object> map);
	Map<String,Object> queryUserOrg(Map<String, Object> map);
	void deletepc(Map<String, Object> map);
	void insertpc(Map<String, Object> map);
}
