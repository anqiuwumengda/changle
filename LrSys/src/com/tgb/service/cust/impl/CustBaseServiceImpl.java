package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustBaseMapper;
import com.tgb.service.cust.CustBaseService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustBaseServiceImpl implements CustBaseService {
	
	@Resource
	private CustBaseMapper custBaseMapper;



	@Override
	@SystemServiceLog(description = "查询客户")    
	public List<Map<String,Object>> queryList(Map<String,Object> map) {
		// TODO Auto-generated method stub
		return custBaseMapper.queryList(map);
	}

	@Override
	@SystemServiceLog(description = "验证客户")
	public Map<String, Object> isExistCust(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custBaseMapper.isExistCust(map);
	}

	@Override
	@SystemServiceLog(description = "新增客户")
	public void saveCust(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custBaseMapper.saveCust(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户")
	public boolean updateCustBase(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custBaseMapper.updateCustBase(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户")
	public boolean updCustBaseMtnData(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custBaseMapper.updCustBaseMtnData(map);
	}

	@Override
	@SystemServiceLog(description = "模糊查询客户")
	public List<Map<String, Object>> queryByLike(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custBaseMapper.queryByLike(map);
	}

	@Override
	@SystemServiceLog(description = "查询客户区域临时表")
	public Map<String, Object> queryAreaTmp(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custBaseMapper.queryAreaTmp(map);
	}

	@Override
	@SystemServiceLog(description = "保存客户区域临时表")
	public void saveAreaTmp(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custBaseMapper.saveAreaTmp(map);
	}

	@Override
	@SystemServiceLog(description = "修改客户区域临时表")
	public void updateAreaTmp(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custBaseMapper.updateAreaTmp(map);
	}

	@Override
	@SystemServiceLog(description = "删除客户区域临时表")
	public void deleteByPkAreaTmp(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custBaseMapper.deleteByPkAreaTmp(map);
	}

	@Override
	public void deleteAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		custBaseMapper.deleteAll(map);
	}


	

}
