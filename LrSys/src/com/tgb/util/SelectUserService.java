package com.tgb.util;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tgb.service.dic.LrdDicService;
import com.tgb.service.login.UserService;

@Service
public class SelectUserService {
	private static UserService userService;

	public static UserService getUserService() {
		return userService;
	}
	@Resource
	public void setUserService(UserService userService) {
		SelectUserService.userService = userService;
	}

}
