package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustZzMapper;
import com.tgb.service.cust.CustZzService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustZzServiceImpl implements CustZzService {
	
	@Resource
	private CustZzMapper custZzMapper;

	@Override
	@SystemServiceLog(description = "查询客户-种植")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custZzMapper.query(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户-种植")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custZzMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-种植")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custZzMapper.update(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-种植")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custZzMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-种植")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custZzMapper.deleteByPk(map);
	}

	@Override
	public List<Map<String, Object>> filterQuery(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custZzMapper.filterQuery(map);
	}



	
}
