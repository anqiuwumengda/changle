package com.tgb.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserReplace {
	

	/**
	 * 
	 * @param list 结果集
	 * @param map key 字段名,value 字典id
	 */
	
	public static void replaceDic(List<Map<String, Object>> list,String key ){
			for(Map<String, Object> map:list){
				
				SelectUserService.getUserService().queryMap(map);
			}
		
	}
	
}
