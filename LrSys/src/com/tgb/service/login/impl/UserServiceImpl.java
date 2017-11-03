package com.tgb.service.login.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.mapper.login.lrdUserMapper;
import com.tgb.model.User;
import com.tgb.service.login.UserService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class UserServiceImpl implements UserService {
	
	@Resource
	private lrdUserMapper mapper;

	@Override
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.delete(map);
	}

	@Override
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.deleteByPk(map);
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
	public boolean updateXinPass(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.updateXinPass(map);
	}

	@Override
	public Map<String, Object> queryMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.queryMap(map);
	}

	@Override
	public ArrayList queryFunc(Map<String, Object> userId) {
		// TODO Auto-generated method stub
		return mapper.queryFunc(userId);
	}

	@Override
	public ArrayList queryRole(Map<String, Object> userId) {
		// TODO Auto-generated method stub
		return mapper.queryRole(userId);
	}

	@Override
	public int queryCount(Map<String, Object> map) {
		
		return mapper.queryCount(map);
	}
	

}
