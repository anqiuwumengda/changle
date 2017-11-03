package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustDocMapper;
import com.tgb.service.cust.CustDocService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustDocServiceImpl implements CustDocService {
	
	@Resource
	private CustDocMapper custDocMapper;

	@Override
	@SystemServiceLog(description = "查询客户-档案")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custDocMapper.query(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户-档案")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custDocMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-档案")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custDocMapper.update(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-档案")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custDocMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-养殖")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custDocMapper.deleteByPk(map);
	}

	@Override
	public void updatePhotoDesc(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custDocMapper.updatePhotoDesc(map);
	}



	
}
