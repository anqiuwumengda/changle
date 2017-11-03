package com.tgb.aop;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tgb.mapper.propertymanage.LrdSysLogMapper;
import com.tgb.model.User;
import com.tgb.service.propertymanage.LrdOrgService;
import com.tgb.service.propertymanage.LrdSysLogService;

@Aspect
@Component
public class SystemLogAspect {
	// 注入Service用于把日志保存数据库
	/*
	 * @Resource private LogService logService;
	 */
	// 本地异常日志记录对象
	private static final Logger logger = Logger
			.getLogger(SystemLogAspect.class);

	// Service层切点
	@Pointcut("@annotation(com.tgb.aop.SystemServiceLog)")
	public void serviceAspect() {
	}

	// Controller层切点
	@Pointcut("@annotation(com.tgb.aop.SystemControllerLog)")
	public void controllerAspect() {
	}
	
	@Autowired
	private LrdOrgService lrdOrgService;
	
	@Autowired
	
	private LrdSysLogMapper lrdSysLogMapper;

	/**
	 * 前置通知 用于拦截Controller层记录用户的操作
	 * 
	 * @param joinPoint
	 *            切点
	 */
	@Before("controllerAspect()")
	public void doBefore(JoinPoint joinPoint) {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		// 读取session中的用户
		// User user = (User) session.getAttribute(WebConstants.CURRENT_USER);
		// 请求的IP
		String ip = request.getRemoteAddr();
		String params = "";
		if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
			for (int i = 0; i < joinPoint.getArgs().length; i++) {
				params += joinPoint.getArgs()[i] + ";";
			}
		}
		User user = (User) session.getAttribute("user");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("CORP_CD", user.getCorpCD());//法人号
		map.put("USER_ID", user.getUser_id());//用户id
		Map<String, Object> userOrg = lrdOrgService.queryUserOrg(map);
		map.put("ORG_CD", userOrg.get("ORG_CD").toString());//机构号
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		String OPER_DATE = format.format(new Date());
		map.put("OPER_DATE", OPER_DATE);
		
		try {
			map.put("OPER_NAME", getControllerMethodDescription(joinPoint)+"");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			map.put("OPER_NAME",e1.getMessage()+"");
			
		}//方法描述：操作名称
		map.put("OPER_DX", joinPoint.getTarget().getClass().getName() + "."
				+ joinPoint.getSignature().getName() + "()");//请求方法
		map.put("OPER_PARA", params+"");
		try {
			// *========控制台输出=========*//
			System.out.println("=====前置通知开始=====");
			System.out.println("请求方法:"
					+ (joinPoint.getTarget().getClass().getName() + "."
							+ joinPoint.getSignature().getName() + "()"));
			System.out.println("方法描述:"
					+ getControllerMethodDescription(joinPoint));
			System.out.println("请求参数:" + params);
			System.out.println("请求人:" + "****");
			System.out.println("请求IP:" + ip);
			// *========数据库日志=========*//
			/*
			 * Log log = SpringContextHolder.getBean("logxx");
			 * log.setDescription(getControllerMethodDescription(joinPoint));
			 * log.setMethod((joinPoint.getTarget().getClass().getName() + "." +
			 * joinPoint.getSignature().getName() + "()")); log.setType("0");
			 * log.setRequestIp(ip); log.setExceptionCode( null);
			 * log.setExceptionDetail( null); log.setParams( null);
			 * log.setCreateBy(user);
			 * log.setCreateDate(DateUtil.getCurrentDate()); //保存数据库
			 * logService.add(log);
			 */
			
			map.put("OPER_IG", "成功");
			lrdSysLogMapper.save(map);	
			System.out.println("=====前置通知结束=====");
		} catch (Exception e) {
			// 记录本地异常日志
			map.put("OPER_IG", "失败");
			lrdSysLogMapper.save(map);
			logger.error("==前置通知异常==");
			logger.error("异常信息:{" + e.getMessage() + "}");
		}
	}

	/**
	 * 异常通知 用于拦截service层记录异常日志
	 * 
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		// 读取session中的用户
		// User user = (User) session.getAttribute(WebConstants.CURRENT_USER);
		// 获取请求ip
		String ip = request.getRemoteAddr();
		// 获取用户请求方法的参数并序列化为JSON格式字符串
		String params = "";
		if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
			for (int i = 0; i < joinPoint.getArgs().length; i++) {
				params += joinPoint.getArgs()[i] + ";";
			}
		}
		
		User user = (User) session.getAttribute("user");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("CORP_CD", user.getCorpCD());//法人号
		map.put("USER_ID", user.getUser_id());//用户id
		Map<String, Object> userOrg = lrdOrgService.queryUserOrg(map);
		map.put("ORG_CD", userOrg.get("ORG_CD").toString());//机构号
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		String OPER_DATE = format.format(new Date());
		map.put("OPER_DATE", OPER_DATE);
		
		try {
			map.put("OPER_NAME", getControllerMethodDescription(joinPoint)+"");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			map.put("OPER_NAME",e1.getMessage()+"");
			
		}//方法描述：操作名称
		map.put("OPER_DX", joinPoint.getTarget().getClass().getName() + "."
				+ joinPoint.getSignature().getName() + "()");//请求方法
		map.put("OPER_PARA", params+"");
		
		try {
			/* ========控制台输出========= */
			System.out.println("=====异常通知开始=====");
			System.out.println("异常代码:" + e.getClass().getName());
			System.out.println("异常信息:" + e.getMessage());
			System.out.println("异常方法:"
					+ (joinPoint.getTarget().getClass().getName() + "."
							+ joinPoint.getSignature().getName() + "()"));
			System.out.println("方法描述:" + getServiceMthodDescription(joinPoint));
			System.out.println("请求人:" + "****");
			System.out.println("请求IP:" + ip);
			System.out.println("请求参数:" + params);
			/* ==========数据库日志========= */
			/*
			 * Log log = SpringContextHolder.getBean("logxx");
			 * log.setDescription(getServiceMthodDescription(joinPoint));
			 * log.setExceptionCode(e.getClass().getName()); log.setType("1");
			 * log.setExceptionDetail(e.getMessage());
			 * log.setMethod((joinPoint.getTarget().getClass().getName() + "." +
			 * joinPoint.getSignature().getName() + "()"));
			 * log.setParams(params); log.setCreateBy(user);
			 * log.setCreateDate(DateUtil.getCurrentDate());
			 * log.setRequestIp(ip); //保存数据库 logService.add(log);
			 */
			map.put("OPER_IG", "成功");
			lrdSysLogMapper.save(map);
			System.out.println("=====异常通知结束=====");
		} catch (Exception ex) {
			// 记录本地异常日志
			map.put("OPER_IG", "失败");
			lrdSysLogMapper.save(map);
			logger.error("==异常通知异常==");
			logger.error("异常信息:{" + ex.getMessage() + "}");
		}
		/* ==========记录本地异常日志========== */
		logger.error("异常方法:{" + joinPoint.getTarget().getClass().getName()
				+ joinPoint.getSignature().getName() + "}异常代码:{"
				+ e.getClass().getName() + "}异常信息:{" + e.getMessage() + "}参数:{"
				+ params + "}");

	}

	/**
	 * 获取注解中对方法的描述信息 用于service层注解
	 * 
	 * @param joinPoint
	 *            切点
	 * @return 方法描述
	 * @throws Exception
	 */
	public static String getServiceMthodDescription(JoinPoint joinPoint)
			throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		String description = "";
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					description = method.getAnnotation(SystemServiceLog.class)
							.description();
					break;
				}
			}
		}
		return description;
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 * 
	 * @param joinPoint
	 *            切点
	 * @return 方法描述
	 * @throws Exception
	 */
	public static String getControllerMethodDescription(JoinPoint joinPoint)
			throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		String description = "";
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					description = method.getAnnotation(
							SystemControllerLog.class).description();
					break;
				}
			}
		}
		return description;
	}
}
