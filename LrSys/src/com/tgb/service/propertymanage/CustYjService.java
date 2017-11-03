package com.tgb.service.propertymanage;

import java.util.List;
import java.util.Map;

public interface CustYjService {
	
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String, Object> map);
	boolean update(Map<String,Object> map);	
	boolean updateCustBaseGrpJl(Map<String,Object> map);
	boolean custYjByAllC(Map<String, Object> map);
	
	List<Map<String,Object>> queryAreaByCustJl(Map<String,Object> map);
	List<Map<String, Object>> queryByStat(Map<String, Object> map);
	List<Map<String, Object>> queryByStatNO(Map<String, Object> map);
}
