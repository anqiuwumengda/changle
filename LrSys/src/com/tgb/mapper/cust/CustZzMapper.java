package com.tgb.mapper.cust;

import java.util.List;
import java.util.Map;


public interface CustZzMapper {
	boolean deleteByPk(Map<String,Object> map);
	boolean delete(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	//按种植作物查找筛选
	List<Map<String,Object>> filterQuery(Map<String,Object> map);
}