package com.tgb.mapper.cust;

import java.util.List;
import java.util.Map;

public interface ScheduleMapper {
	boolean deleteByPk(Map<String, Object> map);
	boolean delete(Map<String, Object> map);
	List<Map<String, Object>> query(Map<String, Object> map);
	List<Map<String, Object>> queryNum(Map<String, Object> map);
	List<Map<String, Object>> queryByPk(Map<String, Object> map);
	void save(Map<String, Object> map);
	boolean update(Map<String, Object> map);
}
