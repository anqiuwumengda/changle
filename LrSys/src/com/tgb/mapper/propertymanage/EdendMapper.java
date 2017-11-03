package com.tgb.mapper.propertymanage;

import java.util.List;
import java.util.Map;

public interface EdendMapper {

	List<Map<String,Object>> queryList(Map<String,Object> map);
	Map<String,Object> query(Map<String,Object> map);
	boolean update(Map<String,Object> map);
}
