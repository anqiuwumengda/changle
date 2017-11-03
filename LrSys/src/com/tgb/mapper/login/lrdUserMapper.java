package com.tgb.mapper.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface lrdUserMapper {
	boolean delete(Map<String,Object> map);
	boolean deleteByPk(Map<String,Object> map);
	List<Map<String,Object>> query(Map<String,Object> map);
	void save(Map<String,Object> map);
	boolean update(Map<String,Object> map);
	boolean updateXinPass(Map<String,Object> map);
	Map<String,Object> queryMap(Map<String,Object> map);
	ArrayList queryFunc(Map<String,Object> userId);
	ArrayList queryRole(Map<String,Object> userId);
	int queryCount(Map<String, Object> map);
}
