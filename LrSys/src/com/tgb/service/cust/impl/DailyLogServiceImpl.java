package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.DailyLogMapper;
import com.tgb.service.cust.DailyLogService;
@Service
@Transactional
// 此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class DailyLogServiceImpl implements DailyLogService {
	@Resource
	private DailyLogMapper  dailyLogMapper;
	@Override
	@SystemServiceLog(description = "刪除客户-工作日志")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dailyLogMapper.deleteByPk(map);
	}

	@Override
	@SystemServiceLog(description = "刪除客户-工作日志")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dailyLogMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "查询客户-工作日志")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dailyLogMapper.query(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户-工作日志")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		dailyLogMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-工作日志")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dailyLogMapper.update(map);
	}

}
