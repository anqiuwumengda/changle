package com.tgb.service.cust;

import java.util.List;
import java.util.Map;


public interface FinTransService {
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	boolean deleteByPk(Map<String, Object> map);
	boolean delete(Map<String,Object> map);
}
