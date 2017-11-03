package com.tgb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/download")
public class DownloadController {
	@RequestMapping("/app2")
	public ResponseEntity<byte[]> download() throws IOException {
		String name = "/乐融贷.apk";
		String path = Thread.currentThread().getContextClassLoader()
		.getResource("").getPath()
		+ "config";
		File file = new File(path+"/"+name);
		HttpHeaders headers = new HttpHeaders();
		// String fileName=new
		// String("poi.rar".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
		headers.setContentDispositionFormData("attachment", "lrdAndroid.apk");
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
				headers, HttpStatus.CREATED);
	}

	@RequestMapping("/app")
	public String download( HttpServletRequest request,
			HttpServletResponse response) {
		String fileName = "lrdAndroid.apk";
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.android.package-archive");
		response.setHeader("Content-Disposition", "attachment;fileName="
				+ fileName);
		try {
			String path = Thread.currentThread().getContextClassLoader()
					.getResource("").getPath()
					+ "config";// 这个download目录为啥建立在classes下的
			InputStream inputStream = new FileInputStream(new File(path
					 +"/"+ fileName));

			OutputStream os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}

			// 这里主要关闭。
			os.close();

			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 返回值要注意，要不然就出现下面这句错误！
		// java+getOutputStream() has already been called for this response
		return null;
	}
}
