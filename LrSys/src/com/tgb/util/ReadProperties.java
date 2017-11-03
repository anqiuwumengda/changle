package com.tgb.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


	public class ReadProperties {
		public  String readProperty(String filename,String key) throws Exception{
			Properties prop = new Properties();
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename+".properties");
			try {
				prop.load(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception("读取"+filename+"信息出错");
			}finally{
				in.close();
			}

			return prop.getProperty(key);
		}
		/**
		 * @param args
		 * @throws Exception 
		 */
		public static void main(String[] args) throws Exception {
			// TODO Auto-generated method stub
			ReadProperties p = new ReadProperties();
			String prot = p.readProperty("global","biurl");
			System.out.println(prot);
			//System.out.println(s);
		}

}
