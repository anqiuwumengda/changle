package com.tgb.service.propertymanage.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.aop.SystemServiceLog;
import com.tgb.mapper.propertymanage.LrdYkhMapper;
import com.tgb.service.propertymanage.LrdYkhService;

@Service
@Transactional 
public class LrdYkhServiceImpl implements LrdYkhService {

	@Resource
	private LrdYkhMapper mapper;

	@Override
	public List<String> querygzdwname() {
		// TODO Auto-generated method stub
		return mapper.querygzdwname();
	}

	@Override
	public List<Map<String, Object>> queryZgdwList(Map<String,String> map) {
		// TODO Auto-generated method stub
		return mapper.queryZgdwList( map);
	}

	@Override
	@SystemServiceLog(description = "职工单位检索") 
	public List<Map<String, Object>> queryZgdwByWorkName(Map<String,String> map) {
		// TODO Auto-generated method stub
		return mapper.queryZgdwByWorkName(map);
	}
	
	
}
