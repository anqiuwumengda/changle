package com.tgb.service.propertymanage.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.mapper.propertymanage.LrdSysLogMapper;
import com.tgb.service.propertymanage.LrdSysLogService;
@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class LrdSysLogServiceImpl implements LrdSysLogService {
	@Resource
	public  LrdSysLogMapper mapper;
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

	

}
