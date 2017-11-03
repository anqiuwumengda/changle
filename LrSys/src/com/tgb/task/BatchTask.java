package com.tgb.task;

import hlc.base.db.DbAccess;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.batch.constants.FilePath;
import com.batch.service.BatchService;
import com.rule.BatchThread;
import com.tgb.controller.propertymanage.LrdCallRuleController;
import com.tgb.model.User;
import com.tgb.service.login.UserService;
import com.tgb.util.DateTools;
import com.tgb.util.FileUtil;

/**
 * 批量定时任务
 * @author lxs
 *
 */

@Component("taskJob")
public class BatchTask {
	
	@Autowired
	private BatchService batchService;
	@Autowired
	private UserService userService;
	Logger log = Logger.getLogger(LrdCallRuleController.class);
	private List<Map<String, String>> list = null;
	private String batch_date;
	private List<BatchThread>  threadlist = new ArrayList<BatchThread>();
	private User user;
	
	
	/**
	 * 跑批定时任务   （每日1：00：00执行跑批任务）
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void batchJob() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dateString = format.format(new Date());
		System.out.println("定时任务开始");
		try {
			//1、将前一天的变量客户进行跑批
			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1); //得到前一天
			Date date = calendar.getTime();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			batch_date=df.format(date);
			Map<String, Object> map=new HashMap<String, Object>();
			String userId="admin";
			map.put("USER_ID", userId);
			Map<String, Object> resultMap = userService.queryMap(map);
			user = new User();
			user.setCorpCD(resultMap.get("CORP_CD").toString());
			user.setUser_id(userId);
			user.setUserName(resultMap.get("USER_NAME").toString());
			//补充user对象，最终填充session信息
			user.setSex(resultMap.get("SEX")+"");
			user.setIdNO(resultMap.get("ID_NO")+"");
			user.setTelNO(resultMap.get("TEL_NO")+"");
			user.setEmail(resultMap.get("EMAIL")+"");
			user.setJlFlag(resultMap.get("JL_FLAG")+"");
			user.setOrgCD(resultMap.get("ORG_CD")+"");
			user.setDeptCD(resultMap.get("DEPT_CD")+"");
			//查询用户的角色和功能id信息
			user.setFUNC_CD(userService.queryFunc(resultMap));
			user.setROLE_CD(userService.queryRole(resultMap));
			
			
			//if(true){
			if(BatchTask.callrule(batch_date, threadlist, user)){
				//2、微信平台所需sql存入电脑固定位置
				String path=FilePath.EXPORT_SQL_PATH_PC;
				
				FileUtil.deleteAllToNew(path);//清空目录
				
				batchService.exportSqltoPC();

				//3、将产生的sql文件以ftp的方式
				
				//Ftp fu = new Ftp();
				
		        /*// 使用默认的端口号、用户名、密码以及根目录连接FTP服务器
				
				String ip="192.168.242.128"; 						//ip
			    int port=22; 						//服务器端口
			    String userName="root"; 				//用户名
			    String password="lixuesong520"; 				//密码
			    String path="/home/sqldata"; 					//服务器路径

		        fu.connectServer(ip, port, userName,password, path);
		        
		        String filePath = FilePath.EXPORT_SQL_PATH + "/" + dateString;
		        String localfile = filePath+"/lydToWeixinSql-" + dateString + ".zip";
		        String remotefile = path+"/lydToWeixinSql-" + dateString + ".zip";
		        //上传
		        fu.upload(localfile, remotefile);
		        fu.closeConnect(); */
		       
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	
	
	public static boolean callrule(String batch_date,List<BatchThread>  threadlist,User user) throws Exception {
		DbAccess db = new DbAccess();
		String sql11 = "select * from batch_log where stat='0'";
		List<Map<String, String>> t = db.queryForList(sql11);
		if (null == t || t.size() == 0) {
			try {
				String delSql = "delete from batch_log where BATCH_DATE='"
						+ batch_date + "'";
				db.executeUpdate(delSql);
				db.executeUpdate("insert into batch_log values('01','"
						+ DateTools.getCurrentSysData("yyyyMMdd HH:mm:ss")
						+ "','','0','" + batch_date + "')");

				List<Map<String, String>> list = db
						.queryForList("select CUST_ID from cust_base where mtn_date='"
								+ batch_date
								+ "' and (CREATE_TYPE='2' or CREATE_TYPE='4')");
				int ii = 0;
				if(null!=list && list.size()>200){
					int sum = list.size();
					int threadNum = 20;
					int nuitNum = sum % threadNum;
					int j = sum / threadNum;
					for (int i = 0; i < threadNum; i++) {
						BatchThread bt =new BatchThread(list.subList(i * j, (i + 1)
								* j - 1),user,batch_date);
						bt.start();
						threadlist.add(bt);
					}
					if(j>0){
						BatchThread bt =new BatchThread(list.subList(threadNum * j, threadNum * j
								+ nuitNum),user,batch_date);
						
						bt.start();
						threadlist.add(bt);
					}
					if(threadlist.size()>0){
						for(int i=1;i>0;){
							boolean isA=false;
							for(BatchThread tt :threadlist){
								if(tt.isAlive()){
									isA=true;
									continue;
								}
							}
							if(!isA){
								db.executeUpdate("update batch_log set STAT='2',END_TIME='"
										+ DateTools.getCurrentSysData("yyyyMMdd HH:mm:ss")
										+ "' where BATCH_DATE='" + batch_date + "'");
								break;
							}
						}
					}
				}else{
					BatchThread bt =new BatchThread(list,user,batch_date);
					
					bt.start();
					threadlist.add(bt);
					for(int i=1;i>0;){
						boolean isA=false;
						for(BatchThread tt :threadlist){
							if(tt.isAlive()){
								isA=true;
								continue;
							}
						}
						if(!isA){
							db.executeUpdate("update batch_log set STAT='2',END_TIME='"
									+ DateTools.getCurrentSysData("yyyyMMdd HH:mm:ss")
									+ "' where BATCH_DATE='" + batch_date + "'");
							break;
						}
					}
				}
				
				
				return true;
			} catch (Exception e) {
				db.executeUpdate("update batch_log set STAT='1',END_TIME='"
						+ DateTools.getCurrentSysData("yyyyMMdd HH:mm:ss")
						+ "' where BATCH_DATE='" + batch_date + "'");
				return false;
			}
		}
		return false; 
	}

	/*public static void main(String[] args) {
		Ftp fu = new Ftp();
		
        // 使用默认的端口号、用户名、密码以及根目录连接FTP服务器
		
		String ip="192.168.242.128"; 						//ip
	    int port=21; 						//服务器端口
	    String userName="root"; 				//用户名
	    String password="lixuesong520"; 				//密码
	    String path="/home/sqldata"; 					//服务器路径

        fu.connectServer(ip, port, userName,password, path);
        
        //String filePath = FilePath.EXPORT_SQL_PATH + "/" + dateString;
        String localfile = "D:/sql.zip";
        String remotefile = path+"/sql.zip";
        //上传
        fu.upload(localfile, remotefile);
        fu.closeConnect(); 
		System.out.println("完成");
	}*/
	

   
   
    
	
}
