package com.tgb.controller.reportmanager;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.ReportManagerContoller.ReportManagerService;
import com.tgb.util.DateTools;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/ReportManager")
public class ReportManagerContoller extends BaseController {
	Logger log=Logger.getLogger(ReportManagerContoller.class); 
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private ReportManagerService reportService;
	
	private PrintWriter pw = null;
	
	
	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "报表查询功能")
	public String query(HttpServletRequest request,@RequestBody Map<String,Object> map) {
		log.info("接收的JSON:"+map.toString());
		try {
			isPage(map);
			List<Map<String, Object>> list = reportService.query(map);
			if (null != page) {
				this.page.removeContext();
			}
			this.setSuccess(list, page);
			
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "报表新增功能")
	public String save(HttpServletRequest request,@RequestBody Map<String,Object> map) {
		TransactionStatus status = this.getTransactionStatus(txManager);
		log.info("接收的JSON:"+map.toString());
		try {
			
			reportService.save(map);
			txManager.commit(status);
			
			this.setSuccess(null, page);
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "报表修改功能")
	public String update(HttpServletRequest request,@RequestBody Map<String,Object> map) {
		TransactionStatus status = this.getTransactionStatus(txManager);
		log.info("接收的JSON:"+map.toString());
		try {
		
			reportService.update(map);
			txManager.commit(status);
			this.setSuccess(null, page);
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "报表删除功能")
	public String delete(HttpServletRequest request,@RequestBody Map<String,Object> map){
		TransactionStatus status = this.getTransactionStatus(txManager);
		log.info("接收的JSON:"+map.toString());
		try {
		
			reportService.delete(map);
			txManager.commit(status);
			this.setSuccess(null, page);
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	@RequestMapping("/upload")
	public String upload2(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		String path = "";
		String uploadPath = "reportFiles";//从系统参数中获取
		String fileName = "";
		try {
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				// 转换成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// 取得request中的所有文件名
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					// 记录上传过程起始时的时间，用来计算上传时间
					int pre = (int) System.currentTimeMillis();
					// 取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						// 取得当前上传文件的文件名称
						String myFileName = file.getOriginalFilename();
						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (myFileName.trim() != "") {
							System.out.println(myFileName);
							// 重命名上传后的文件名
							fileName = DateTools
									.getCurrentSysData("yyyyMMddHHmmsss")
									+ (int) ((Math.random() * 9 + 1) * 10000)
									+ file.getOriginalFilename();
							/*fileName=reportName+ file.getOriginalFilename();*/
							// 定义上传路径
							
							path=request.getSession().getServletContext().getRealPath(uploadPath)+"\\"+fileName;
							String url = this.getClass().getResource("/")
									.toString();
							System.out.println(url);
							//path = "D:/" + fileName;
							File localFile = new File(path);
							if (!localFile.getParentFile().exists()) {  
				                // 目标文件所在目录不存在  
				                if (!localFile.getParentFile().mkdirs()) {  
				                    // 复制文件失败：创建目标文件所在目录失败  
				                	pw = response.getWriter();
				        			pw.write("{'code':'99','msg':'创建目标文件所在目录失败 '}");
				                }  
				            }  
							file.transferTo(localFile);
						}
					}
					// 记录上传该文件后的时间
					int finaltime = (int) System.currentTimeMillis();
					System.out.println(finaltime - pre);
				}

			}
			pw = response.getWriter();
			pw.write("{\"code\":\"00\",\"msg\":\"" + uploadPath+"\\\\"+fileName+ "\",\"FILENAME\":\""+fileName+"\",\"path\":\""+uploadPath+"/"+fileName+"\"}");
		} catch (Exception e) {
			pw = response.getWriter();
			pw.write("{'code':'99','msg':'" + e.getMessage() + "'}");
			e.printStackTrace();
			System.out.println("上传出错");
		}

		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/ReportToRole", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "报表权限功能")
	public String ReportToRole(HttpServletRequest request,@RequestBody Map<String,Object> map){
		TransactionStatus status = this.getTransactionStatus(txManager);
		log.info("接收的JSON:"+map.toString());
		try {
			
			reportService.deleteBBRole(map);
			List<String> BBIDS= (List<String>) map.get("BBIDS");
			for (String BBID : BBIDS) {
				map.put("BBID", BBID);
				reportService.reportToRole(map);
			}
			txManager.commit(status);
			this.setSuccess(null, page);
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/ReportToView", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "报表查看")
	public String ReportToView(HttpServletRequest request){
		//TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			User user = (User) request.getSession().getAttribute("user");
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("ROLE_ID", user.getROLE_CD());
			
			isPage(map);
			List<Map<String, Object>> list=reportService.reportToView(map);
			if (null != page) {
				this.page.removeContext();
			}
			
			this.setSuccess(list, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	
}


