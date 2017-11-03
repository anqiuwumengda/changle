package com.tgb.mapper.cust;

import java.util.Map;


public interface SeleniumMgMapper {
	Map<String,Object> query(Map<String,Object> map);
	Map<String,Object> queryRela(Map<String,Object> map);
	Map<String,Object> queryPass(Map<String,Object> map);
	Map<String,Object> sumFamily(Map<String,Object> map);
	Map<String,Object> famMoney(Map<String,Object> map);
	
}