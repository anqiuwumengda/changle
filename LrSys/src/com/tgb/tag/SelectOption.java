package com.tgb.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.tgb.util.SelectDicService;

@SuppressWarnings("serial")
public class SelectOption extends TagSupport {

	private String divClass;
	private String selectClass;
	private String selectName;
	private String selectId;
	private String dicId;
	private String selected;

	public int doStartTag() throws JspException {
		try {
			System.out.println(111);
			// setParent(new ButtonAreaTag());

			JspWriter out = pageContext.getOut();
			StringBuffer sbf = new StringBuffer();

			sbf.append("<div class=\"" + divClass + "\">");
			sbf.append("<select class=\"" + selectClass + "\" name=\""
					+ selectName + "\" id=\"" + selectId + "\">");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("DIC_PARENTID", dicId);
			List<Map<String, Object>> list = SelectDicService.getLrdDicService()
					.query(map);
			sbf.append("<option value=\"\" selected>--请选择--</option>");
			for (Map<String, Object> tmp : list) {
				if (tmp.get("DIC_ID").toString().equals(selected)) {
					sbf.append("<option value=\"" + tmp.get("DIC_ID")
							+ "\" selected>" + tmp.get("DIC_NAME")
							+ "</option>");
				} else {
					sbf.append("<option value=\"" + tmp.get("DIC_ID") + "\" >"
							+ tmp.get("DIC_NAME") + "</option>");
				}
			}
			sbf.append("</select></div>");
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

	public String getDivClass() {
		return divClass;
	}

	public void setDivClass(String divClass) {
		this.divClass = divClass;
	}

	public String getSelectClass() {
		return selectClass;
	}

	public void setSelectClass(String selectClass) {
		this.selectClass = selectClass;
	}

	public String getSelectName() {
		return selectName;
	}

	public void setSelectName(String selectName) {
		this.selectName = selectName;
	}

	public String getSelectId() {
		return selectId;
	}

	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}

	public String getDicId() {
		return dicId;
	}

	public void setDicId(String dicId) {
		this.dicId = dicId;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}
	/*
	 * public static void main(String[] args){ LrdDicService lrdDicService =
	 * ApplicationContext.getBean(LrdDicService.class); }
	 */
}
