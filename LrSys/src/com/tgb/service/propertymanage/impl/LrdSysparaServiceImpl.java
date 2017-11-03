package com.tgb.service.propertymanage.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.mapper.propertymanage.LrdSysparaMapper;
import com.tgb.service.propertymanage.LrdSysparaService;

@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class LrdSysparaServiceImpl implements LrdSysparaService {

	@Resource
	public LrdSysparaMapper mapper;
	@Override
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.update(map);
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
	public Map<String, Object> queryMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.queryMap(map);
	}
	
}
