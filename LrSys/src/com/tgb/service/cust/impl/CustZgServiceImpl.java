package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustZgMapper;
import com.tgb.service.cust.CustZgService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustZgServiceImpl implements CustZgService {
	
	@Resource
	private CustZgMapper custZgMapper;

	@Override
	@SystemServiceLog(description = "查询客户-职工")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custZgMapper.query(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户-职工")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custZgMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-职工")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custZgMapper.update(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-职工")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custZgMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-职工")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custZgMapper.deleteByPk(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户基本信息维护日期")
	public boolean updCustBaseMtnData(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custZgMapper.updCustBaseMtnData(map);
	}



	
}
