package com.tgb.controller.login;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.login.UserService;
import com.tgb.service.propertymanage.LrdSysparaService;
import com.tgb.util.DateTools;
import com.tgb.util.MD5;

import sun.misc.BASE64Encoder;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/Login")
public class LoginController extends BaseController {
	private static Hashtable htAll = new Hashtable();
	@Autowired
	private UserService userService;
	@Autowired
	private LrdSysparaService lrdSysparaService;
	
	public void setCookie(HttpServletResponse response, String userId) {
		Cookie ck = new Cookie("USER_ID", userId);
		ck.setMaxAge(60 * 60 * 8);
		ck.setPath("/");
		ck.setDomain("/LrService/*");
		response.addCookie(ck);
	}

	@ResponseBody
	@RequestMapping(value = "/getUserInfo", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	public String login(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("USER_ID", this.getSessionUser(request).getUser_id());
			map.put("USER_NAME", this.getSessionUser(request).getUserName());
			this.setSuccess(map, null);
		} catch (Exception e) {
			this.setErr(null, "获取错误");
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUserMeg", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	public String getUserMeg(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("USER_ID", this.getSessionUser(request).getUser_id());
			map.put("USER_NAME", this.getSessionUser(request).getUserName());
			this.setSuccess(map, null);
		} catch (Exception e) {
			this.setErr(null, "获取错误");
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/updateXinPas", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	public String updateXinPas(@RequestBody Map<String, Object> map,HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> pass = new HashMap<String, Object>();
			//转换密码格式
			BASE64Encoder encode = new BASE64Encoder();
			String xinPass=map.get("xinPass").toString();
		    String base64xinPass = encode.encode(xinPass.getBytes());
			pass.put("xinPass",base64xinPass);
			pass.put("USER_ID", this.getSessionUser(request).getUser_id());
			pass.put("ORG_CD", this.getSessionUser(request).getOrgCD());
			userService.updateXinPass(pass);
			this.setSuccess(pass, null);
		} catch (Exception e) {
			this.setErr(null, "设置密码失败");
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/login", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	public String login(@RequestBody Map map, HttpServletRequest request,
			HttpServletResponse response) {
		// System.out.println(request.getSession().getAttribute("admin"));
		try {
			System.out.println("登录.........."+map.get("USER_ID"));
			String userId = map.get("USER_ID").toString();
			String pwd = MD5.getMD5(map.get("PASSWORD").toString());
			Map<String, Object> resultMap = userService.queryMap(map);
			

			if (null != resultMap && !resultMap.isEmpty()) {
				if (resultMap.get("USER_ID").equals(userId)) {
					String resPwd = resultMap.get("PASSWORD").toString();
					// 验证密码
					if (pwd.equals(resPwd)) {
						// 密码正确
						// setCookie(response, userId);
						//验证密码过期时间
						String pw_date="20170101";
						Object pw_dateObj = resultMap.get("PW_MTN_DATE");
						if(null!=pw_dateObj && !"".equals(pw_dateObj.toString())){
							pw_date=resultMap.get("PW_MTN_DATE").toString();
						}
						SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
						Date pwdate=null;
						try {
							pwdate = format.parse(pw_date);
						} catch (Exception e) {}
						Date date = new Date();
						long intervalMilli = date.getTime() - pwdate.getTime();
						int day=(int)(intervalMilli / (24 * 60 * 60 * 1000));
						//查询过期天数参数
						Map<String, Object> map1=new HashMap();
						map1.put("CHPARAKEY", "PW_OUT_TIME");
						map1.put("CORP_CD", resultMap.get("CORP_CD"));
						Map<String, Object> queryMap = lrdSysparaService.queryMap(map1);
						int daytime=45;//防止参数未填写带来的错误  给默认为45天
						
						try {daytime = Integer.valueOf(queryMap.get("CHPARAVALUE").toString());
						} catch (Exception e) {}
						if (day>=daytime) {
							//超出过期时间
							this.setErr(null, "77");
						}else{
							 this.js.remove("errMsg");
						}
						
						User user = new User();
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
						// 查询并获得一个session
						HttpSession session = request.getSession(false);
						if (session == null) {
							session = request.getSession(true);
							if (user.getHt().containsValue(session.getId())) {
								// 销毁刚创建的session
								session.invalidate();
								// 创建一个新的session
								session = request.getSession(true);
								Thread.sleep(100);
								request.getSession().setAttribute("user", user);
							} else {
								Thread.sleep(100);
								request.getSession().setAttribute("user", user);
							}

						} else {
							User user1 = (User) session.getAttribute("user");
							if (null == user1) {
								// 销毁刚创建的session
								session.invalidate();
								// 创建一个新的session
								session = request.getSession(true);
								Thread.sleep(100);
								request.getSession().setAttribute("user", user);
							} else {
								user.getHt().remove(user.getUser_id());
								session.invalidate();
								// 创建一个新的session
								session = request.getSession(true);
								Thread.sleep(100);
								request.getSession().setAttribute("user", user);
							}
						}

						user.getHt().put(userId, session.getId());
						// request.getRequestDispatcher("/index.html").forward(request,response);
						resultMap.remove("PASSWORD");
						System.out.println("登录成功.....");
						
						if ((daytime-day)<=10) {
							resultMap.put("PW_MSG","密码将在"+(daytime-day)+"天后过期");
						}
						
						this.setSuccess(resultMap, null);
						// return null;
					} else {
						this.setErr(null, "密码错误");
					}

				} else {
					// 密码错误
					this.setErr(null, "用户名错误");
				}
			} else {
				// 未查找到用户
				this.setErr(null, "未查找到用户");
			}

			/*
			 * User user = new User();
			 * user.setId(Integer.parseInt(map.get("user_id").toString()));
			 * request.getSession().setAttribute("user", user);
			 * this.setSuccess(user, page);
			 */

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();

	}

	@ResponseBody
	@RequestMapping(value = "/resetPwd", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	public String resetPwd(@RequestBody Map<String,Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		// System.out.println(request.getSession().getAttribute("admin"));
		try {
			String userId = this.getSessionUser(request).getUser_id();
			String oldpwd = MD5.getMD5(map.get("OLD_PASSWORD").toString());
			String newpwd = MD5.getMD5(map.get("NEW_PASSWORD").toString());
			map.put("CORP_CD", this.getSessionUser(request).getCorpCD());
			map.put("USER_ID", this.getSessionUser(request).getUser_id());
			Map<String, Object> resultMap = userService.queryMap(map);

			if (null != resultMap && !resultMap.isEmpty()) {
				if (resultMap.get("USER_ID").equals(userId)) {
					String resPwd = resultMap.get("PASSWORD").toString();
					// 验证密码
					if (oldpwd.equals(resPwd)) {
						// 密码正确
						// setCookie(response, userId);
						map.put("PASSWORD", newpwd);
						map.put("PW_MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
						userService.update(map);
						this.setSuccess(resultMap, null);
						// return null;
					} else {
						this.setErr(null, "密码错误");
					}

				} else {
					//
					this.setErr(null, "用户名错误");
				}
			} else {
				// 未查找到用户
				this.setErr(null, "未查找到用户");
			}

			/*
			 * User user = new User();
			 * user.setId(Integer.parseInt(map.get("user_id").toString()));
			 * request.getSession().setAttribute("user", user);
			 * this.setSuccess(user, page);
			 */

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();

	}

	@ResponseBody
	@RequestMapping(value = "/logout", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	public String logout(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		try {
			User user = this.getSessionUser(request);
			if (null != user) {
				user.getHt().remove(user.getUser_id());
				user = null;
				HttpSession session = request.getSession();
				session.invalidate();
			} else {
				HttpSession session = request.getSession();
				session.invalidate();
			}
			this.setSuccess(null, page);
			/*
			 * StringBuilder builder = new StringBuilder(); PrintWriter out =
			 * response.getWriter(); out.write("{'code':'99','msg':'请重新登陆 '}");
			 * builder
			 * .append("<script type=\"text/javascript\" charset=\"UTF-8\">");
			 * builder.append("");
			 * builder.append("window.top.location.href=\"");
			 * builder.append(""); builder.append(
			 * "http://127.0.0.1:9090/LrService/login.html\";</script>");
			 * out.print(builder.toString()); out.close();
			 */
			/*
			 * request.getRequestDispatcher("/LrService/login.html").forward(
			 * request, response);
			 */
			// response.sendRedirect("/LrService/login.html");
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}

		return null;
	}
}
