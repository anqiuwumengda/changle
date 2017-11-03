package com.tgb.service.cust.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.cust.CustApplyMapper;
import com.tgb.mapper.cust.CustInputSumMapper;
import com.tgb.service.cust.CustApplyService;
import com.tgb.service.cust.CustInputSumService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class CustInputSumServiceImpl implements CustInputSumService {
	
	@Resource
	private CustInputSumMapper custInputSumMapper;

	@Override
	@SystemServiceLog(description = "统计-客户经理月录入信息")
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return custInputSumMapper.query(map);
	}

	
}
