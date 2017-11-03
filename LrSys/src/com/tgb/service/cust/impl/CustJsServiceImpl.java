package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustJsMapper;
import com.tgb.service.cust.CustJsService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustJsServiceImpl implements CustJsService {
	
	@Resource
	private CustJsMapper custJsMapper;

	@Override
	@SystemServiceLog(description = "查询客户-经商")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custJsMapper.query(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户-经商")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custJsMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-经商")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custJsMapper.update(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-经商")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custJsMapper.delete(map);
	}

	@Override
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custJsMapper.deleteByPk(map);
	}



	
}
