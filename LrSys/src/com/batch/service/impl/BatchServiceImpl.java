package com.batch.service.impl;

import hlc.base.db.DbAccess;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.batch.constants.FilePath;
import com.batch.mapper.BatchMapper;
import com.batch.service.BatchService;

@Service
public class BatchServiceImpl implements BatchService {	

	@Autowired
	private BatchMapper batchMapper;

	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

	@Override
	public ResponseEntity<byte[]> exportSql() throws Exception {
		String dateString = format.format(new Date());
		String filePath = FilePath.EXPORT_SQL_PATH + "/" + dateString;
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		exportAmount(filePath);
		exportAdmit(filePath);
		exportRelation(filePath);
		fileToZip(filePath, filePath, "lydToWeixinSql-" + dateString);
		
		File f = new File(filePath + "/lydToWeixinSql-" + dateString + ".zip");
		HttpHeaders headers = new HttpHeaders();
		// String fileName=new
		// String("poi.rar".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
		headers.setContentDispositionFormData("attachment", "lydToWeixinSql-" + dateString + ".zip");
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(f),
				headers, HttpStatus.CREATED);
	}

	/**
	 * 额度数据sql生成
	 * @param filePath
	 * @throws Exception
	 */
	private void exportAmount(String filePath) throws Exception {
		List<Map<String, Object>> amountList = batchMapper.queryAmount();
		if (amountList != null && !amountList.isEmpty()) {
			File file = new File(filePath + "/amount.sql");
			if(!file.exists()){
				file.createNewFile();
			}
			//FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter (new FileOutputStream (filePath + "/amount.sql"), "UTF-8"));
			String truncateSql = "truncate table cust_amount_limit;\n";
			writer.write(truncateSql);
			for (Map<String, Object> amount : amountList) {
				String sql = "insert into cust_amount_limit(cust_id_number,cust_name,total_amount,credit_amount,family_amount,crt_time) values (";
				// 客户身份证号码
				if (amount.get("cust_id_number") == null)
					continue;
				else
					sql += "'" + amount.get("cust_id_number") + "',";
				// 客户姓名
				if (amount.get("cust_name") == null)
					continue;
				else
					sql += "'" + amount.get("cust_name") + "',";
				// 总额度
				if (amount.get("total_amount") == null)
					continue;
				else
					sql += amount.get("total_amount") + ",";
				// 信用额度
				if (amount.get("credit_amount") == null)
					continue;
				else
					sql += amount.get("credit_amount") + ",";
				// 亲情额度
				if (amount.get("family_amount") == null)
					continue;
				else
					sql += amount.get("family_amount") + ",";
				// 生成时间
				if (amount.get("crt_time") == null)
					continue;
				else
					sql += "'" + amount.get("crt_time") + "',";

				sql = sql.substring(0, sql.length() - 1) + ");\n";
				writer.write(sql);
				writer.flush();
			}
			
			//写入固定数据
			String basePath=BatchServiceImpl.class.getResource("/").toString();
			List<String> sqlList = loadSql(basePath+"data/amount.sql");
            for (String sql : sqlList) {
            	writer.write(sql+"\n");
				writer.flush();
            }
			
			writer.close();
			amountList.clear();
		}
		else{
			throw new Exception("数据处理错误，请联系管理人员");
		}
	}
	
	/**
	 * 准入数据sql生成
	 * @param filePath
	 * @throws Exception
	 */
	private void exportAdmit(String filePath) throws Exception {
		// 准入数据
		List<Map<String, Object>> admitList = batchMapper.queryAdmit();
		if (admitList != null && !admitList.isEmpty()) {
			File file = new File(filePath + "/admit.sql");
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(filePath + "/admit.sql", true);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter (new FileOutputStream (filePath + "/admit.sql"), "UTF-8"));
			String truncateSql = "truncate table cust_admit_result;\n";
			writer.write(truncateSql);
			for (Map<String, Object> admit : admitList) {
				String sql = "insert into cust_admit_result(cust_id_number,cust_name,admit_result,reason,crt_time) values (";
				String result = "";
				String reason = "";
				if (admit.get("ZR1_RESULT") == null)
					continue;
				else {
					// 判断准入1
					if ("3".equals(admit.get("ZR1_RESULT"))) {
						result = "3";
						if (admit.get("ZR1_DESC") != null)
							reason += admit.get("ZR1_DESC") + ";";
					} else if ("1".equals(admit.get("ZR1_RESULT"))) {
						result = "1";
						if (admit.get("ZR1_DESC") != null)
							reason = String.valueOf(admit.get("ZR1_DESC"));
					} else {
						if (admit.get("ZR2_RESULT") == null)
							continue;
						// 判断准入2
						if ("3".equals(admit.get("ZR2_RESULT"))) {
							result = "3";
							if (admit.get("ZR2_DESC") != null)
								reason += admit.get("ZR2_DESC") + ";";
						} else if ("1".equals(admit.get("ZR2_RESULT"))) {
							result = "1";
							if (admit.get("ZR2_DESC") != null)
								reason = String.valueOf(admit.get("ZR2_DESC"));
						} else {
							if (admit.get("CS_RESULT") == null)
								continue;
							// 判断初审
							if ("3".equals(admit.get("CS_RESULT"))) {
								result = "3";
								if (admit.get("CS_DESC") != null)
									reason += admit.get("CS_DESC") + ";";
							} else if ("1".equals(admit.get("CS_RESULT"))) {
								result = "1";
								if (admit.get("CS_DESC") != null)
									reason = String.valueOf(admit.get("CS_DESC"));
							} else
								result = "T";
						}
					}
				}
				// 客户身份证号码
				if (admit.get("cust_id_number") == null)
					continue;
				else
					sql += "'" + admit.get("cust_id_number") + "',";
				// 客户姓名
				if (admit.get("cust_name") == null)
					continue;
				else
					sql += "'" + admit.get("cust_name") + "',";
				// 准入结果
				if ("".equals(result))
					continue;
				else
					sql += "'" + result + "',";
				// 原因
					sql += "'" + reason + "',";
				// 生成时间
				if (admit.get("crt_time") == null)
					continue;
				else
					sql += "'" + admit.get("crt_time") + "',";

				sql = sql.substring(0, sql.length() - 1) + ");\n";
				writer.write(sql);
				writer.flush();
			}
			//写入固定数据
			String basePath=BatchServiceImpl.class.getResource("/").toString();
			List<String> sqlList = loadSql(basePath+"data/admit.sql");
            for (String sql : sqlList) {
            	writer.write(sql+"\n");
				writer.flush();
            }
			
			
			writer.close();
			admitList.clear();
		}
		else{
			throw new Exception("数据处理错误，请联系管理人员");
		}
	}

	/**
	 * 关系数据sql生成
	 * @param filePath
	 * @throws Exception
	 */
	private void exportRelation(String filePath) throws Exception {
		// 客户及客户经理对照
		List<Map<String, Object>> relationList = batchMapper.queryRelation();
		if (relationList != null && !relationList.isEmpty()) {
			File file = new File(filePath + "/relation.sql");
			if(!file.exists()){
				file.createNewFile();
			}
			DbAccess db = new DbAccess();
			String sql1 = "select lu.USER_ID,lu.USER_NAME,lu.ORG_CD,lur.ROLE_ID From lrd_user lu LEFT JOIN lrd_userrole lur on lu.USER_ID=lur.USER_ID where lur.ROLE_ID='0004';";
			List<Map<String,String>> list = db.queryForList(sql1);
			
			FileWriter fileWriter = new FileWriter(filePath + "/relation.sql", true);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter (new FileOutputStream (filePath + "/relation.sql"), "UTF-8"));
			String truncateSql = "truncate table cust_to_manager;\n";
			writer.write(truncateSql);
			
			for (Map<String, Object> relation : relationList) {
				String sql = "insert into cust_to_manager(cust_id_number,cust_name,cust_mobile,manager_login_id,manager_name,internal_login_id,internal_name) values (";
				// 客户身份证号码
				if (relation.get("cust_id_number") == null)
					continue;
				else
					sql += "'" + relation.get("cust_id_number") + "',";
				// 客户姓名
				if (relation.get("cust_name") == null)
					continue;
				else
					sql += "'" + relation.get("cust_name") + "',";
				// 客户手机号
				Object telno = relation.get("cust_mobile");
				if(null == telno)
					telno="";
				sql += "'" + telno + "',";
				/*if (relation.get("cust_mobile") == null)
					continue;
				else
					sql += "'" + relation.get("cust_mobile") + "',";*/
				// 客户经理登录ID
				if (relation.get("manager_login_id") == null)
					continue;
				else
					sql += "'" + relation.get("manager_login_id") + "',";
				// 客户经理姓名
				if (relation.get("manager_name") == null)
					continue;
				else
					sql += "'" + relation.get("manager_name") + "',";
				//Map<String,String> paraMap = new HashMap<String,String>();
				//paraMap.put("USER_ID", relation.get("manager_login_id").toString());
			
				String internal_login_id  = "";
				String internal_name = "";
				Object orgcd =relation.get("org_cd");
				if(null!=orgcd){
					for(Map<String,String> map :list){
						String tmp= map.get("ORG_CD");
						if(orgcd.toString().equals(tmp)){
							internal_login_id=map.get("USER_ID");
								internal_name=map.get("USER_NAME");
						}
						
					}
				}
				
				
				sql += "'" + internal_login_id+ "',";
				sql += "'" + internal_name+ "',";
				
				sql = sql.substring(0, sql.length() - 1) + ");\n";
				writer.write(sql);
				writer.flush();
			}
			writer.close();
			relationList.clear();
		}
		else{
			throw new Exception("数据处理错误，请联系管理人员");
		}
	}
	
	/**
	 * 压缩文件到zip
	 * @param sourceFilePath
	 * @param zipFilePath
	 * @param fileName
	 * @return
	 */
	private boolean fileToZip(String sourceFilePath,String zipFilePath,String fileName){  
        boolean flag = false;  
        File sourceFile = new File(sourceFilePath);  
        FileInputStream fis = null;  
        BufferedInputStream bis = null;  
        FileOutputStream fos = null;  
        ZipOutputStream zos = null;  
          
        if(sourceFile.exists() == false){  
            System.out.println("待压缩的文件目录："+sourceFilePath+"不存在.");  
        }else{  
            try {  
                File zipFile = new File(zipFilePath + "/" + fileName +".zip");  
                if(zipFile.exists()){  
                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName +".zip" +"打包文件.");  
                }else{  
                    File[] sourceFiles = sourceFile.listFiles();  
                    if(null == sourceFiles || sourceFiles.length<1){  
                        System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");  
                    }else{  
                        fos = new FileOutputStream(zipFile);  
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));  
                        byte[] bufs = new byte[1024*10];  
                        for(int i=0;i<sourceFiles.length;i++){  
                            //创建ZIP实体，并添加进压缩包  
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());  
                            zos.putNextEntry(zipEntry);  
                            //读取待压缩的文件并写进压缩包里  
                            fis = new FileInputStream(sourceFiles[i]);  
                            bis = new BufferedInputStream(fis, 1024*10);  
                            int read = 0;  
                            while((read=bis.read(bufs, 0, 1024*10)) != -1){  
                                zos.write(bufs,0,read);  
                            }  
                        }  
                        flag = true;  
                    }  
                }  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } catch (IOException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } finally{  
                //关闭流  
                try {  
                    if(null != bis) bis.close();  
                    if(null != zos) zos.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    throw new RuntimeException(e);  
                }  
            }  
        }  
        return flag;  
    }

	@Override
	public boolean exportSqltoPC() throws Exception {

		try {
			String filePath = FilePath.EXPORT_SQL_PATH_PC;
			File file = new File(filePath);
			
			if (!file.exists()) {
				file.mkdirs();
			}
			exportAmount(filePath);
			exportAdmit(filePath);
			exportRelation(filePath);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
		
		
		
		
		//fileToZip(filePath, filePath, "lydToWeixinSql-" + dateString);
		
		
		//File f = new File(filePath + "/lydToWeixinSql-" + dateString + ".zip");
		//HttpHeaders headers = new HttpHeaders();
		// String fileName=new
		// String("poi.rar".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
		//headers.setContentDispositionFormData("attachment", "lydToWeixinSql-" + dateString + ".zip");
		//headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		//return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(f),
				//headers, HttpStatus.CREATED);
	}  
	
	
	
	
	
	     
	    /**
	     * 读取 SQL 文件，获取 SQL 语句 
	     * @param sqlFile SQL 脚本文件
	     * @return List<sql> 返回所有 SQL 语句的 List
	     * @throws Exception
	     */
	    private static List<String> loadSql(String sqlFile) throws Exception {
	        //List<String> sqlList = new ArrayList<String>();
	        try {
	           /* InputStream sqlFileIn = new FileInputStream(sqlFile.substring(sqlFile.indexOf("/"), sqlFile.length()));
	            StringBuffer sqlSb = new StringBuffer();
	            byte[] buff = new byte[1024];
	            int byteRead = 0;
	            while ((byteRead = sqlFileIn.read(buff)) != -1) {
	                sqlSb.append(new String(buff, 0, byteRead));
	            }
	            
	            // Windows 下换行是 \r\n, Linux 下是 \n
	            String[] sqlArr = sqlSb.toString()
	                    .split("(\\s*\\r\\n)|(\\s*\\n)");
	            for (int i = 0; i < sqlArr.length; i++) {
	                String sql = sqlArr[i].replaceAll("--.*", "").trim();
	                if (!sql.equals("")) {
	                    sqlList.add(sql);
	                }
	            }*/
	            
	            //88888888888888888888888
	            List<String> lines=new ArrayList<String>();  
	            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(sqlFile.substring(sqlFile.indexOf("/"), sqlFile.length())),"UTF-8"));  
	            String line = null;  
	            while ((line = br.readLine()) != null) {  
	                  lines.add(line);  
	            }  
	            br.close();  
	            
	            return lines;
	        } catch (Exception ex) {
	            throw new Exception(ex.getMessage());
	        }
	    }
	 
	
	
	
	    public static void main(String[] args) {
	        try {
	        	System.out.println(BatchServiceImpl.class.getResource("/"));
	        	BatchServiceImpl bimpl=new BatchServiceImpl();
	        	bimpl.exportSqltoPC();
	        	
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	     
	
	
	

}
