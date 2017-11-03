package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.FinTransMapper;
import com.tgb.service.cust.FinTransService;
@Service
@Transactional
// 此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class FinTransServiceImpl implements FinTransService {
	@Resource
	private FinTransMapper finTransMapper;
	@Override
	@SystemServiceLog(description = "查询客户-银行交易信息")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return finTransMapper.query(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户-银行交易信息")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		finTransMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-银行交易信息")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return finTransMapper.update(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-银行交易信息")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return finTransMapper.deleteByPk(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-银行交易信息")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return finTransMapper.delete(map);
	}

}
