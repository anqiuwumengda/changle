package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.RptDtlMapper;
import com.tgb.service.cust.RptDtlService;

@Service
@Transactional
// 此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class RptDtlServiceImpl implements RptDtlService {
	@Resource
	private RptDtlMapper rptDtlMapper;
	@Override
	@SystemServiceLog(description = "删除客户-财报明细")
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return rptDtlMapper.deleteByPk(map);
	}
	@Override
	@SystemServiceLog(description = "删除客户-财报明细")
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return rptDtlMapper.delete(map);
	}

	@Override
	@SystemServiceLog(description = "查询客户-财报明细")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return rptDtlMapper.query(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户-财报明细")
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		rptDtlMapper.save(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户-财报明细")
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return rptDtlMapper.update(map);
	}
	@Override
	public Map<String, Object> queryQtSR1Y(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return rptDtlMapper.queryQtSR1Y(map);
	}
	@Override
	public Map<String, Object> queryXVQYSR1Y(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return rptDtlMapper.queryXVQYSR1Y(map);
	}
	

}
