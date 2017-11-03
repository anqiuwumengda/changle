package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustRelaMapper;
import com.tgb.service.cust.CustRelaService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustRelaServiceImpl implements CustRelaService {
	
	@Resource
	private CustRelaMapper custRelaMapper;

	@Override
	@SystemServiceLog(description = "查询客户-关联关系")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custRelaMapper.query(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户-关联关系")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custRelaMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-关联关系")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custRelaMapper.update(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-关联关系")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custRelaMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-关联关系")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custRelaMapper.deleteByPk(map);
	}



	
}
