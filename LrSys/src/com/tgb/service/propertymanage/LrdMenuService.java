package com.tgb.service.propertymanage;

import java.util.List;
import java.util.Map;

public interface LrdMenuService {
	
	List<Map<String, Object>> queryTree(Map<String, Object> map);
}
