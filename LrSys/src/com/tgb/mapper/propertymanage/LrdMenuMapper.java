package com.tgb.mapper.propertymanage;

import java.util.List;
import java.util.Map;

public interface LrdMenuMapper {

	List<Map<String, Object>> queryTree(Map<String, Object> map);
}
