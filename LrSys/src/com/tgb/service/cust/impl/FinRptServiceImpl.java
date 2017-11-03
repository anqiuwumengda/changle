package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.FinRptMapper;
import com.tgb.service.cust.FinRptService;

@Service
@Transactional
// 此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class FinRptServiceImpl implements FinRptService {
	@Resource
	private FinRptMapper finRptMapper;
	@Override
	@SystemServiceLog(description = "删除客户-财务报表")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return finRptMapper.deleteByPk(map);
	}
	@Override
	@SystemServiceLog(description = "删除客户-财务报表")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return finRptMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "查询客户-财务报表")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return finRptMapper.query(map);
	}
	@Override
	@SystemServiceLog(description = "查询客户-财务报表-按日期，客户id去重")
	public List<Map<String, Object>> queryDistinct(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return finRptMapper.queryDistinct(map);
	}
	@Override
	@SystemServiceLog(description = "新增客户-财务报表")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		finRptMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-财务报表")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return finRptMapper.update(map);
	}

}
