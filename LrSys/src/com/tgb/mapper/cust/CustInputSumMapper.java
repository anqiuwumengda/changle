package com.tgb.mapper.cust;

import java.util.List;
import java.util.Map;


public interface CustInputSumMapper {
	List<Map<String,Object>> query(Map<String,Object> map);

}