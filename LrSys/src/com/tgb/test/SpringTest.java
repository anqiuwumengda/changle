package com.tgb.test;

import hlc.base.db.DbAccess;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tgb.util.DateTools;

public class SpringTest {

	 public static void main(String[] args) {
		 try {
			DbAccess db =new DbAccess(false);
			String id = "";
			for(int i=1;i<=100;i++){
				id = DateTools.getCurrentSysData("yyyyMMddHHmmsss")
				+ (int) ((Math.random() * 9 + 1) * 10000);
				String sql="INSERT INTO `cust_base` VALUES ('"+id+"', '蔡氏家族"+i+"', null, null, null, '01', '', '01', 'CZJM', '01', '01', '13122223333', '', null, null, null, '', null, '01', '61794', 'JMXQ', 'JMXQ001', '370725002000', '370725002005', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '20160922', '20160922', '1', null, null, null, null);";
				db.executeUpdate(sql);
			}
			
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		/* ApplicationContext ctx = new ClassPathXmlApplicationContext("config/spring-common.xml");
		 Object userMapper = ctx.getBean("userMapper");
		 System.out.println(userMapper);*/
	 }
}
