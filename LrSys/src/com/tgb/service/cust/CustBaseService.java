package com.tgb.service.cust;

import java.util.List;
import java.util.Map;


public interface CustBaseService {
	List<Map<String,Object>> queryByLike(Map<String,Object> map);
	List<Map<String,Object>> queryList(Map<String,Object> map);
	Map<String,Object> isExistCust(Map<String,Object> map);
	void saveCust(Map<String,Object> map);
	boolean updateCustBase(Map<String,Object> map);
	boolean updCustBaseMtnData(Map<String,Object> map);
	
	Map<String,Object> queryAreaTmp(Map<String,Object> map);
	void saveAreaTmp(Map<String,Object> map);
	void updateAreaTmp(Map<String,Object> map);
	void deleteByPkAreaTmp(Map<String,Object> map);
	
	void deleteAll(Map<String,Object> map);
}
