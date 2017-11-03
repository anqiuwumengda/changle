package com.tgb.model;

import hlc.UserInfo;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用户
 * @author liang
 *
 */
public class User {
	private String user_id;
	private String userName;
	private String password;
	private String sex;
	private String age;
	private String idNO;//唯一识别码
	private String telNO;
	private String email;
	private String corpCD;//法人号
	private String jlFlag;//客户经理
	private String orgCD;//机构号
	private String 	deptCD;//部门号
	private ArrayList FUNC_CD = new ArrayList();//部门号
	private ArrayList ROLE_CD = new ArrayList();//部门号
	
	
	
	
	private Hashtable ht = new Hashtable();
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getIdNO() {
		return idNO;
	}
	public void setIdNO(String idNO) {
		this.idNO = idNO;
	}
	public String getTelNO() {
		return telNO;
	}
	public void setTelNO(String telNO) {
		this.telNO = telNO;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCorpCD() {
		return corpCD;
	}
	public void setCorpCD(String corpCD) {
		this.corpCD = corpCD;
	}
	public String getJlFlag() {
		return jlFlag;
	}
	public void setJlFlag(String jlFlag) {
		this.jlFlag = jlFlag;
	}
	public String getOrgCD() {
		return orgCD;
	}
	public void setOrgCD(String orgCD) {
		this.orgCD = orgCD;
	}
	public String getDeptCD() {
		return deptCD;
	}
	public void setDeptCD(String deptCD) {
		this.deptCD = deptCD;
	}
	public static UserInfo getInstance(HttpServletRequest request)throws Exception {
		HttpSession session = request.getSession(false);
		Object o = session.getAttribute("user");
		UserInfo user = (UserInfo) o;
		return user;
	}
	public Hashtable getHt() {
		return ht;
	}
	public void setHt(Hashtable ht) {
		this.ht = ht;
	}
	public ArrayList getFUNC_CD() {
		return FUNC_CD;
	}
	public void setFUNC_CD(ArrayList fUNC_CD) {
		FUNC_CD = fUNC_CD;
	}
	public ArrayList getROLE_CD() {
		return ROLE_CD;
	}
	public void setROLE_CD(ArrayList rOLE_CD) {
		ROLE_CD = rOLE_CD;
	}
	
}
