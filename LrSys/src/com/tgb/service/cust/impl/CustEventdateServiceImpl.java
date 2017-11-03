package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustEventdateMapper;
import com.tgb.service.cust.CustEventdateService;
@Service
@Transactional
// 此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustEventdateServiceImpl implements CustEventdateService {
	@Resource
	private CustEventdateMapper  custEventdateMapper;
	@Override
	@SystemServiceLog(description = "刪除客户-事件提醒")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custEventdateMapper.deleteByPk(map);
	}
	@Override
	@SystemServiceLog(description = "刪除客户-事件提醒")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custEventdateMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户-事件提醒")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custEventdateMapper.save(map);
	}
	@Override
	@SystemServiceLog(description = "修改客户-事件提醒")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custEventdateMapper.update(map);
	}
	@Override
	@SystemServiceLog(description = "查询客户-事件提醒")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custEventdateMapper.query(map);
	}

}
