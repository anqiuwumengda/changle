package com.tgb.mapper.cust;

import java.util.List;
import java.util.Map;

public interface CustNzNeedMapper {
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String,Object> map);
	void delete(Map<String,Object> map);
}
