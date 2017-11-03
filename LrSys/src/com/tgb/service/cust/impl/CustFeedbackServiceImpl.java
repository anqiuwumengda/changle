package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustFeedbackMapper;
import com.tgb.service.cust.CustFeedbackService;
@Service
@Transactional
// 此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustFeedbackServiceImpl implements CustFeedbackService {
	@Resource
	private CustFeedbackMapper custFeedbackMapper;
	@Override
	@SystemServiceLog(description = "删除客户-需求")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custFeedbackMapper.deleteByPk(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-需求")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custFeedbackMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "查询客户-需求")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custFeedbackMapper.query(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户-需求")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custFeedbackMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-需求")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custFeedbackMapper.update(map);
	}

	@Override
	public List<Map<String, Object>> queryList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custFeedbackMapper.queryList(map);
	}

}
