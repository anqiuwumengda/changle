package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustYzMapper;
import com.tgb.service.cust.CustYzService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustYzServiceImpl implements CustYzService {
	
	@Resource
	private CustYzMapper custYzMapper;

	@Override
	@SystemServiceLog(description = "查询客户-养殖")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custYzMapper.query(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户-养殖")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custYzMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-养殖")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custYzMapper.update(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-养殖")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custYzMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-养殖")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custYzMapper.deleteByPk(map);
	}



	
}
