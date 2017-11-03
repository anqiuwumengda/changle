package hlc.base.interceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PortalProxy implements Filter {
	public void destroy() {
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		String url =(String)((HttpServletRequest) request).getRequestURI();
		System.out.println(url);
		if(url!=null&&url.indexOf("eam")>-1&&url.indexOf("portal/test.action")==-1){
			if(url.startsWith("/hlc")){
				url = url.substring(4);
			}
			/*
			try{
				String proxyurl = "http://127.0.0.1:8080/dwz-ria/"+url;
				String curLine = null;
				String content = "";
				URL server = new URL(proxyurl);
				HttpURLConnection connection = (HttpURLConnection)server.openConnection();
				connection.connect();
				InputStream is = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				while((curLine = reader.readLine())!=null){
					content = content+curLine+"\r\n";
				}
				request.setAttribute("content", content);
				System.out.println(new String(content.getBytes("gbk"),"utf-8"));
				is.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			*/
			HttpServletResponse httpServeletResponse = (HttpServletResponse)response;
			httpServeletResponse.sendRedirect("/hlc/portal/test.action?rel=010102000000&url="+url);
			//((HttpServletRequest)request).getRequestDispatcher("/hlc/portal/main/proxy.jsp").forward(request, response);
			return;
		}else{
			chain.doFilter(request, response);
		}
		
		
	}
	
	public String execute(){
		
		return "success";
	}

}
