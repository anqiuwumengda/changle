package com.tgb.service.ReportManagerContoller;

import java.util.List;
import java.util.Map;

public interface ReportManagerService {

	boolean deleteByPk(Map<String,Object> map);
	boolean delete(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	void reportToRole(Map<String, Object> map);
	void deleteBBRole(Map<String, Object> map);
	List<Map<String, Object>> reportToView(Map<String, Object> map);
	
}
