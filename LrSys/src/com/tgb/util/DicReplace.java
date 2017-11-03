package com.tgb.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicReplace {
	

	/**
	 * 
	 * @param list 结果集
	 * @param map key 字段名,value 字典id
	 */
	
	public static void replaceDic(List<Map<String, Object>> list,
			Map<String, String> map) {
		if(null!=list && list.size()>0){
			for(Map<String,Object> rmap :list){
				for(String key :map.keySet()){
					String parent_id = map.get(key);
					Object dic_id = rmap.get(key);
					rmap.put(key+"C", "");
					if(null!=dic_id && !"".equals(dic_id.toString())){
						//查询
						Map<String,String> paraMap = new HashMap<String,String>();
						paraMap.put("DIC_PARENTID", parent_id);
						paraMap.put("DIC_ID", dic_id.toString());
						Map<String,String> rrMap = SelectDicService.getLrdDicService().queryMap(paraMap);
						
						if(null!=rrMap && !rrMap.isEmpty()){
							rmap.put(key+"C", rrMap.get("DIC_NAME"));
						}
						
					}
				}
			}
		}
		
	}
	public static void replaceChildDic(List<Map<String, Object>> list,String pIdColumn,String childColumn){
		if(null!=list && list.size()>0){
			for(Map<String,Object> rmap :list){
				
					rmap.put(childColumn+"C", "");
					Object parent_id = rmap.get(pIdColumn);
					Object dic_id = rmap.get(childColumn);
					if(null!=dic_id &&null!=parent_id&& !"".equals(dic_id.toString())&&!"".equals(parent_id.toString())){
						//查询
						Map<String,String> paraMap = new HashMap<String,String>();
						paraMap.put("DIC_PARENTID", parent_id.toString());
						paraMap.put("DIC_ID", dic_id.toString());
						Map<String,String> rrMap = SelectDicService.getLrdDicService().queryMap(paraMap);
						if(null!=rrMap && !rrMap.isEmpty()){
							rmap.put(childColumn+"C", rrMap.get("DIC_NAME"));
						}
							
				
						
					}
				}
			}
		
	}
}
