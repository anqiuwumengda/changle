package com.tgb.service.propertymanage.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.mapper.propertymanage.LrdMenuMapper;
import com.tgb.service.propertymanage.LrdMenuService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class LrdMenuServiceImpl implements LrdMenuService {

	@Resource
	public LrdMenuMapper mapper;
	
	@Override
	public List<Map<String, Object>> queryTree(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.queryTree(map);
	}
	
	
}
