package com.tgb.service.propertymanage.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.mapper.propertymanage.CustYjMapper;
import com.tgb.service.propertymanage.CustYjService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustYjServiceImpl implements CustYjService {
	
	@Resource
	private CustYjMapper mapper;

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
	public boolean updateCustBaseGrpJl(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.updateCustBaseGrpJl(map);
	}

	@Override
	public boolean custYjByAllC(Map<String, Object> map) {
		return mapper.custYjByAllC(map);
	}

	@Override
	public List<Map<String, Object>> queryAreaByCustJl(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.queryAreaByCustJl(map);
	}

	@Override
	public List<Map<String, Object>> queryByStat(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.queryByStat(map);
	}

	@Override
	public List<Map<String, Object>> queryByStatNO(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.queryByStatNO(map);
	}

	
}
