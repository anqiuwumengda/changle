package com.tgb.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.tgb.model.User;

public class SystemInterceptor extends HandlerInterceptorAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//System.out.println("过滤器");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		if (request.getHeader("x-requested-with") != null
				&& request.getHeader("x-requested-with").equalsIgnoreCase(
						"XMLHttpRequest")) { // 如果是ajax请求响应头会有x-requested-with
			//PrintWriter out = response.getWriter();
			//out.print("loseSession");// session失效
			//out.flush();
			
			//*****************************************************************************
			// 后台session控制
			String[] noFilters = new String[] { "login", "logout" };
			String uri = request.getRequestURI();

			if (uri.indexOf("LrSys") != -1) {
				boolean beFilter = true;
				for (String s : noFilters) {
					if (uri.indexOf(s) != -1) {
						beFilter = false;
						break;
					}
				}
				if (beFilter) {
					HttpSession session = request.getSession();
					User user = (User) session.getAttribute("user");
					if (null == user) {

						// 未登录
						PrintWriter out = response.getWriter();
						StringBuilder builder = new StringBuilder();
						out.print("{\"code\":\"88\",\"errMsg\":\"请重新登陆 \",\"reLoad\":\"window.top.location.href='/LrSys/login.html'\"}");
						/*builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
						builder.append("");
						builder.append("window.top.location.href=\"");
						builder.append("");
						builder.append("/LrService/login.html\";</script>");*/

						// PrintWriter out2 = response.getWriter();

						//out.print(builder.toString());
						out.flush();
						// out.close();

						return false;
					} else {
						// 添加日志

					}
				}
			}else{
				return false;
			}
			
		} else {
			// 非ajax请求时，session失效的处理
		}
		// ************************


		/*
		 * Map paramsMap = request.getParameterMap();
		 * 
		 * for (Iterator<Map.Entry> it = paramsMap.entrySet().iterator(); it
		 * .hasNext();) { Map.Entry entry = it.next(); Object[] values =
		 * (Object[]) entry.getValue(); for (Object obj : values) { if
		 * (!DataUtil.isValueSuccessed(obj)) { throw new
		 * RuntimeException("有非法字符：" + obj); } } }
		 */

		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}
}
