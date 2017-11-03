package com.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




public class CustGradeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// TODO Auto-generated method stub
			 request.setCharacterEncoding("UTF-8"); 
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			//String n=new String(request.getParameter("n").getBytes("ISO-8859-1"),"UTF-8");
			//String id=request.getParameter("id");
			//ts.info("n="+n);
			//ts.info("id="+id);
			
			String CARDNUM =request.getAttribute("ID_NO")+"";
			String CNAME =request.getAttribute("CUST_NAME")+"";
			
			SendMsgCustGrap custGrap=new SendMsgCustGrap();
			
			
			try {
				//custGrap.sendMsg(CARDNUM+"&"+CNAME);
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.getWriter().write("评估成功");
			
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// TODO Auto-generated method stub
		}
}
