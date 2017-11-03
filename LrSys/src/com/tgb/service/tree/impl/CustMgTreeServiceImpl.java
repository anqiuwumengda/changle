package com.tgb.service.tree.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.mapper.tree.CustMgTreeMapper;
import com.tgb.service.tree.CustMgTreeService;



@Service
@Transactional 
public class CustMgTreeServiceImpl implements CustMgTreeService {

	@Autowired
	private CustMgTreeMapper custMgTreeMapper;
	
	@Override
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Map<String, Object>> queryJL(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custMgTreeMapper.queryJL(map);
	}

	@Override
	public List<Map<String, Object>> queryOne(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Map<String, Object>> queryAreaByCustJl(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custMgTreeMapper.queryAreaByCustJl(map);
	}

	@Override
	public List<Map<String, Object>> queryOrgList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custMgTreeMapper.queryOrgList(map);
	}

	@Override
	public List<Map<String, Object>> queryAllMenu(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custMgTreeMapper.queryAllMenu(map);
	}

	@Override
	public List<Map<String, Object>> queryList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custMgTreeMapper.queryList(map);
	}

	@Override
	public List<Map<String, Object>> queryByLike(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custMgTreeMapper.queryByLike(map);
	}

	@Override
	public List<Map<String, Object>> filterQuery(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custMgTreeMapper.filterQuery(map);
	}
	
	



	
}
