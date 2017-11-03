package com.tgb.service.dic.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgb.mapper.dic.LrdDicMapper;
import com.tgb.service.dic.LrdDicService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class LrdDicServiceImpl implements LrdDicService {
	
	@Resource
	private LrdDicMapper lrdDicMapper;

	@Override
	public List<Map<String, Object>> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return lrdDicMapper.query(map);
	}

	@Override
	public void save(Map<String, Object> map) {
		// TODO Auto-generated method stub
		lrdDicMapper.save(map);
	}

	@Override
	public boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return lrdDicMapper.update(map);
	}

	@Override
	public boolean delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return lrdDicMapper.delete(map);
	}

	@Override
	public boolean deleteByPk(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return lrdDicMapper.deleteByPk(map);
	}

	@Override
	public Map<String, String> queryMap(Map<String, String> map) {
		// TODO Auto-generated method stub
		return lrdDicMapper.queryMap(map);
	}



	
}
