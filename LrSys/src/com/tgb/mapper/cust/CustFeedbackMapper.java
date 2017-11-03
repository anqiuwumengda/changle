package com.tgb.mapper.cust;

import java.util.List;
import java.util.Map;

public interface CustFeedbackMapper {
	boolean deleteByPk(Map<String, Object> map);
	boolean delete(Map<String, Object> map);
	List<Map<String, Object>> query(Map<String, Object> map);
	void save(Map<String, Object> map);
	boolean update(Map<String, Object> map);
	//联合查询不同状态的记录
	List<Map<String, Object>> queryList(Map<String, Object> map);
}
