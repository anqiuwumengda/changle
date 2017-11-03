package com.tgb.service.propertymanage.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.mapper.propertymanage.LrdUserBkMapper;
import com.tgb.service.propertymanage.LrdUserService;

@Service
@Transactional 
public class LrdUserServiceImpl implements LrdUserService {

	@Resource
	private LrdUserBkMapper mapper;
	
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
		return mapper.querList(map);
	}

	@Override
	public Map<String, Object> queryMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.queryMap(map);
	}

	@Override
	public boolean deleteByUser(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.deleteByUser(map);
	}

	@Override
	public void saveRole(Map<String, Object> map) {
		// TODO Auto-generated method stub
		mapper.saveRole(map);
	}

	@Override
	public List<Map<String, Object>> queryUserRole(Map<String, Object> map2) {
		// TODO Auto-generated method stub
		return mapper.queryUserRole(map2);
	}

}
