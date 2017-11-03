package com.tgb.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.tgb.util.DateTools;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/file")
public class UploadController {
	private PrintWriter pw = null;

/*	@RequestMapping("/upload")
	public String addUser(@RequestParam("file") CommonsMultipartFile[] files,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String path = "";
		for (int i = 0; i < files.length; i++) {
			System.out.println("fileName---------->"
					+ files[i].getOriginalFilename());

			if (!files[i].isEmpty()) {
				int pre = (int) System.currentTimeMillis();
				try {
					// 拿到输出流，同时重命名上传的文件
					path = "D:/"
							+ DateTools.getCurrentSysData("yyyyMMddHHmmsss")
							+ (int) ((Math.random() * 9 + 1) * 10000)
							+ files[i].getOriginalFilename();
					FileOutputStream os = new FileOutputStream(path);
					// 拿到上传文件的输入流
					InputStream in = files[i].getInputStream();

					// 以写字节的方式写文件
					int b = 0;
					while ((b = in.read()) != -1) {
						os.write(b);
					}
					os.flush();
					os.close();
					in.close();
					int finaltime = (int) System.currentTimeMillis();
					System.out.println(finaltime - pre);
					pw = response.getWriter();
					pw.write("{'code':'00','msg':'" + path + "'}");
				} catch (Exception e) {
					pw = response.getWriter();
					pw.write("{'code':'99','msg':'" + e.getMessage() + "'}");
					e.printStackTrace();
					System.out.println("上传出错");

				}
			}
		}

		return null;
	}*/

	@RequestMapping("/upload2")
	public String upload2(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		String path = "";
		String uploadPath = "upload";//从系统参数中获取
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

	@RequestMapping("/toUpload")
	public String toUpload() {

		return "/upload";
	}

}
