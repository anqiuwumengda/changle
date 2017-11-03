package com.tgb.service.cust.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.SeleniumMgMapper;
import com.tgb.service.cust.SeleniumMgService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class SeleniumMgServiceImpl implements SeleniumMgService {
	
	@Resource
	private SeleniumMgMapper seleniumMgMapper;

	@Override
	@SystemServiceLog(description = "查询客户信息")
	public Map<String, Object> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return seleniumMgMapper.query(map);
	}
	@Override
	@SystemServiceLog(description = "查询客户关系信息")
	public Map<String, Object> queryRela(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return seleniumMgMapper.queryRela(map);
	}
	@Override
	public Map<String, Object> queryPass(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return seleniumMgMapper.queryPass(map);
	}
	@Override
	public Map<String, Object> sumFamily(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return seleniumMgMapper.sumFamily(map);
	}
	@Override
	public Map<String, Object> famMoney(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return seleniumMgMapper.famMoney(map);
	}
}