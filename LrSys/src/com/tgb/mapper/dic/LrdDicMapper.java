package com.tgb.mapper.dic;

import java.util.List;
import java.util.Map;


public interface LrdDicMapper {
	boolean deleteByPk(Map<String,Object> map);
	boolean delete(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	Map<String,String> queryMap(Map<String,String> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
}