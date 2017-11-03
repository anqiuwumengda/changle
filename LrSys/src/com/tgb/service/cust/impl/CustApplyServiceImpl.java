package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustApplyMapper;
import com.tgb.service.cust.CustApplyService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustApplyServiceImpl implements CustApplyService {
	
	@Resource
	private CustApplyMapper custApplyMapper;

	@Override
	@SystemServiceLog(description = "查询授权客户")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custApplyMapper.query(map);
	}
	@Override
	@SystemServiceLog(description = "查询授权客户")
	public List<Map<String, Object>> queryOne(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custApplyMapper.queryOne(map);
	}
	@Override
	@SystemServiceLog(description = "新增授权客户")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custApplyMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改授权客户")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custApplyMapper.update(map);
	}

	@Override
	@SystemServiceLog(description = "删除授权客户")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custApplyMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "删除授权客户")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custApplyMapper.deleteByPk(map);
	}
	@Override
	public void deleteGq(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custApplyMapper.deleteGq(map);
	}



	
}
