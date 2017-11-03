package com.tgb.service.propertymanage;

import java.util.List;
import java.util.Map;

public interface LrdYkhService {

	
	List<String> querygzdwname();

	List<Map<String,Object>> queryZgdwList(Map<String,String> map);
	List<Map<String,Object>> queryZgdwByWorkName(Map<String,String> map);
}
