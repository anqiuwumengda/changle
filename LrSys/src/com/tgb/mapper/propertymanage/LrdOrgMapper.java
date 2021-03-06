package com.tgb.mapper.propertymanage;

import java.util.List;
import java.util.Map;

public interface LrdOrgMapper {

	boolean delete(Map<String,Object> map);
	boolean deleteByPk(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	Map<String,Object> queryMap(Map<String,Object> map);
	Map<String,Object> queryUserOrg(Map<String, Object> map);

	List<Map<String, Object>> queryMenu(Map<String, Object> map);
	void deletepc(Map<String, Object> map);
	void insertpc(Map<String, Object> map);
}
