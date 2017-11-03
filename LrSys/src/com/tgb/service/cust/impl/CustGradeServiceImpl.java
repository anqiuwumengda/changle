package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustGradeMapper;
import com.tgb.service.cust.CustGradeService;
@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustGradeServiceImpl implements CustGradeService {
	@Resource
	private CustGradeMapper custGradeMapper;
	@Override
	@SystemServiceLog(description = "删除客户-分类")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custGradeMapper.deleteByPk(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户-分类")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custGradeMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "查询客户-分类")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custGradeMapper.query(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户-分类")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custGradeMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-分类")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custGradeMapper.update(map);
	}

}
