package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustXwqyMapper;
import com.tgb.service.cust.CustXwqyService;
@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustXwqyServiceImpl implements CustXwqyService {
	@Resource
	private CustXwqyMapper custXwqyMapper;

	@Override
	@SystemServiceLog(description = "删除工作经营情况-小微企业")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custXwqyMapper.deleteByPk(map);
	}

	@Override
	@SystemServiceLog(description = "删除工作经营情况-小微企业")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custXwqyMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "查询工作经营情况-小微企业")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custXwqyMapper.query(map);
	}

	@Override
	@SystemServiceLog(description = "新增工作经营情况-小微企业")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custXwqyMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改工作经营情况-小微企业")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custXwqyMapper.update(map);
	}

}
