package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.ScheduleMapper;
import com.tgb.service.cust.ScheduleService;
@Service
@Transactional// 此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class ScheduleServiceImpl implements ScheduleService {
	@Resource
	private  ScheduleMapper  scheduleMapper;
	@Override
	@SystemServiceLog(description = "删除客户-日程计划")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return scheduleMapper.deleteByPk(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-日程计划")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return scheduleMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "查询客户-日程计划")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return scheduleMapper.query(map);
	}
	@Override
	@SystemServiceLog(description = "查询客户-日程计划")
	public List<Map<String, Object>> queryNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return scheduleMapper.queryNum(map);
	}
	@Override
	@SystemServiceLog(description = "查询客户-日程计划-按主键")
	public List<Map<String, Object>> queryByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return scheduleMapper.queryByPk(map);
	}
	@Override
	@SystemServiceLog(description = "新增客户-日程计划")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		scheduleMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-日程计划")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return scheduleMapper.update(map);
	}

}
