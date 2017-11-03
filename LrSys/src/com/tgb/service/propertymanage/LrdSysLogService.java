package com.tgb.service.propertymanage;

import java.util.List;
import java.util.Map;

public interface LrdSysLogService {
	boolean delete(Map<String,Object> map);
	boolean deleteByPk(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
}
