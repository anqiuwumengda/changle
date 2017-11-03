package com.tgb.service.cust;

import java.util.Map;


public interface SeleniumMgService {
	Map<String,Object> query(Map<String,Object> map);
	Map<String,Object> queryRela(Map<String,Object> map);
	Map<String,Object> queryPass(Map<String,Object> map);
	Map<String,Object> sumFamily(Map<String,Object> map);
	Map<String,Object> famMoney(Map<String,Object> map);
	
}
