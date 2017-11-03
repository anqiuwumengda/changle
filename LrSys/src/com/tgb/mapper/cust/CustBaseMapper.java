package com.tgb.mapper.cust;

import java.util.List;
import java.util.Map;

import com.tgb.model.User;
import com.tgb.page.Page;

public interface CustBaseMapper {

/*	void save(User user);
	void save(Map<String,String> map);
	boolean update(User user);
	boolean delete(int id);
	User findById(int id);
	List<User> findAll();
	List<Map<String,String>> findAllJson();
	List<Map<String,String>> findByIdMap(Map<String,String> map);*/
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
	
	boolean deleteAll(Map<String,Object> map);
}
