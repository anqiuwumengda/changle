package com.tgb.mapper.propertymanage;

import java.util.List;
import java.util.Map;

public interface LrdSysLogMapper {
	boolean delete(Map<String,Object> map);
	boolean deleteByPk(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	boolean save(Map<String,Object> map);
}
