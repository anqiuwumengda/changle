package com.tgb.util;


public class SeleniumUtil {
	public static String custType(String info){
		String returnStr="";
		if("CZJM".equals(info)){
			returnStr="城镇居民";			
		}else if("GTGS".equals(info)){
			returnStr="个体工商户";
		}else if("NCJM".equals(info)){
			returnStr="农户";
		}
		return returnStr;
	}
	public static String sex(String info){
		String sex="男";//为了给信贷系统提供默认值
		if("01".equals(info)){
			sex="男";			
		}else if("02".equals(info)){
			sex="女";
		}
		return sex;
	}
	public static String cardType(String info){
		String cardType="身份证";
		if("01".equals(info)){
			cardType="身份证";			
		}else if("02".equals(info)){
			cardType="军官证";
		}else if("03".equals(info)){
			cardType="护照";
		}
		return cardType;
	}
	public static String isMarried(String info){
		String MarryState="";
		if("1".equals(info)){
			MarryState="未婚";
		}else if("2".equals(info)){
			MarryState="已婚";
		}else if("3".equals(info)){
			MarryState="离婚";
		}else if("4".equals(info)){
			MarryState="丧偶";
		}
		return MarryState;
	}
	
}
