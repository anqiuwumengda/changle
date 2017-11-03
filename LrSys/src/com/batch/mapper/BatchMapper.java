package com.batch.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface BatchMapper {

	List<Map<String, Object>> queryAmount();

	List<Map<String, Object>> queryAdmit();

	List<Map<String, Object>> queryRelation();
	
	Map<String, String> queryInternal(Map<String,String> map );
}
