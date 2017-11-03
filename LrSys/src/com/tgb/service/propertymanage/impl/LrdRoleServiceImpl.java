package com.tgb.service.propertymanage.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.mapper.propertymanage.LrdRoleMapper;
import com.tgb.service.propertymanage.LrdRoleService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class LrdRoleServiceImpl implements LrdRoleService {
	
	@Resource
	private LrdRoleMapper mapper;

	@Override
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.deleteByPk(map);
	}

	@Override
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.delete(map);
	}

	@Override
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.query(map);
	}

	@Override
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		mapper.save(map);
	}

	@Override
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.update(map);
	}

	@Override
	public List<Map<String, Object>> queryList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.queryList(map);
	}

	@Override
	public List<Map<String, Object>> queryFunc(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.queryFunc(map);
	}


	@Override
	public void updateFunc(Map<String, Object> map) {
		// TODO Auto-generated method stub
		mapper.updateFunc(map);
	}

	@Override
	public void deleteFunc(HashMap<String, Object> hashMap) {
		// TODO Auto-generated method stub
		mapper.deleteFunc(hashMap);
	}

	@Override
	public List<Map<String, Object>> queryUserRole(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.queryUserRole(map);
	}

	@Override
	public Map<String, Object> queryMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.queryMap(map);
	}

	@Override
	public void insertFunc(HashMap<String, Object> hashMap) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
}
