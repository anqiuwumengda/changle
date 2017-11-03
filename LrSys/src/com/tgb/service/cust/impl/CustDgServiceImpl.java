package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustDgMapper;
import com.tgb.service.cust.CustDgService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustDgServiceImpl implements CustDgService {
	
	@Resource
	private CustDgMapper custDgMapper;

	@Override
	@SystemServiceLog(description = "查询客户-打工")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custDgMapper.query(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户-打工")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custDgMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-打工")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custDgMapper.update(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-打工")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custDgMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-打工")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custDgMapper.deleteByPk(map);
	}



	
}
