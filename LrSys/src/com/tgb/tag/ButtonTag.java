package com.tgb.tag;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.tgb.model.User;
import com.tgb.page.PageContext;
import com.tgb.util.SelectUserService;

@SuppressWarnings("serial")
public class ButtonTag extends TagSupport {
	
	private String iClass;//<i>标签class值
	private String buttonClass;//按钮class值
	private String buttonName;//按钮名称，例如：删除
	private String buttonId;//按钮id:<Button id="">
	private String funcCd;//功能编号，用于判断用户权限

	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			StringBuffer sbf = new StringBuffer();
			//判断用户所属角色是否在该功能id对应的角色列表里，是的话输出该按钮，不是不输出
			User user = (User) pageContext.getSession().getAttribute("user");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("USER_ID", user.getUser_id());
			map.put("funcCd", funcCd);
		    int count  = SelectUserService.getUserService().queryCount(map);
		    //count=1;
			if(count>0){
				sbf.append("<button id=\"" + buttonId + "\" class=\""+buttonClass+"\"><i class=\""+iClass+"\"></i>" +buttonName+ "</button>");
			}
			System.out.println(sbf.toString());
			out.print(sbf.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Tag.EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		return Tag.EVAL_PAGE;

	}

	public String getiClass() {
		return iClass;
	}

	public void setiClass(String iClass) {
		this.iClass = iClass;
	}

	public String getButtonClass() {
		return buttonClass;
	}

	public void setButtonClass(String buttonClass) {
		this.buttonClass = buttonClass;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	public String getFuncCd() {
		return funcCd;
	}

	public void setFuncCd(String funcCd) {
		this.funcCd = funcCd;
	}

	
}
