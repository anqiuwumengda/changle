package com.tgb.mapper.cust;

import java.util.List;
import java.util.Map;


public interface CustApplyMapper {
	boolean deleteByPk(Map<String,Object> map);
	boolean delete(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	List<Map<String,Object>> queryOne(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	void deleteGq(Map<String, Object> map);
}