package com.tgb.service.propertymanage.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.mapper.propertymanage.CustExApMapper;
import com.tgb.service.propertymanage.CustExApService;

/**
 * 
 * @author hp
 *
 */
@Service
@Transactional 
public class CustExApServiceImpl implements CustExApService{

	@Resource
	public CustExApMapper custExApMapper;
	
	@Override
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custExApMapper.query(map);
	}

	@Override
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custExApMapper.save(map);
	}

	@Override
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custExApMapper.update(map);
	}

	@Override
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		 return custExApMapper.delete(map);
	}

	@Override
	public List<Map<String, Object>> queryList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custExApMapper.queryList(map);
	}

	@Override
	public List<Map<String, Object>> queryHnByYf(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custExApMapper.queryHnByYf(map);
	}

	@Override
	public List<Map<String, Object>> queryBatch(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custExApMapper.queryBatch(map);
	}

	@Override
	public Map<String, Object> queryByID(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custExApMapper.queryByID(map);
	}

	

}
