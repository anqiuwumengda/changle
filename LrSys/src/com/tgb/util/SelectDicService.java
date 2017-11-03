package com.tgb.util;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tgb.service.dic.LrdDicService;

@Service
public class SelectDicService {
private static LrdDicService lrdDicService;
	
	@Resource
	public  void setLrdDicService(LrdDicService lrdDicService) {
		SelectDicService.lrdDicService = lrdDicService;
	}
	
	public static LrdDicService getLrdDicService() {
		return lrdDicService;
	}
}
