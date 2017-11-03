package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.mapper.cust.CustNzNeedMapper;
import com.tgb.service.cust.CustNzNeedService;

@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustNzNeedServiceImpl implements CustNzNeedService {
	@Resource
	private CustNzNeedMapper custNzNeedMapper;
	@Override
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custNzNeedMapper.query(map);
	}

	@Override
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custNzNeedMapper.save(map);
	}

	@Override
	public void delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custNzNeedMapper.delete(map);
	}

}
