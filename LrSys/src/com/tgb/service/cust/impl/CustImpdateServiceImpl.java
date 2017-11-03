package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustImpdateMapper;
import com.tgb.service.cust.CustImpdateService;
@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustImpdateServiceImpl implements CustImpdateService {
	@Resource
	private CustImpdateMapper custImpdateMapper;
	@Override
	@SystemServiceLog(description = "刪除客户-纪念日提醒")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custImpdateMapper.deleteByPk(map);
	}
	@Override
	@SystemServiceLog(description = "刪除客户-纪念日提醒")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custImpdateMapper.delete(map);
	}
	@Override
	@SystemServiceLog(description = "查询客户-纪念日提醒")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custImpdateMapper.query(map);
	}
	@Override
	@SystemServiceLog(description = "新增客户-纪念日提醒")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custImpdateMapper.save(map);
	}
	@Override
	@SystemServiceLog(description = "修改客户-纪念日提醒")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custImpdateMapper.update(map);
	}
	@Override
	@SystemServiceLog(description = "查询客户-首页客户提醒")
	public List<Map<String, Object>> queryTx(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custImpdateMapper.queryTx(map);
	}
}
