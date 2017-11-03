package com.tgb.service.propertymanage;

import java.util.List;
import java.util.Map;

public interface CustExApService {
	
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String, Object> map);
	boolean update(Map<String,Object> map);
	boolean delete(Map<String,Object> map);
	List<Map<String, Object>> queryList(Map<String, Object> map);
	List<Map<String, Object>> queryHnByYf(Map<String, Object> map);
	List<Map<String, Object>> queryBatch(Map<String, Object> map);
	Map<String, Object> queryByID(Map<String, Object> map);


}
