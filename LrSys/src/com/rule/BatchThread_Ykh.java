package com.rule;

import java.util.List;
import java.util.Map;

import com.tgb.controller.propertymanage.LrdYkhController;
import com.tgb.model.User;

public class BatchThread_Ykh extends Thread{
	private List<Map<String, String>> list = null;
	private User user;
	public BatchThread_Ykh(List<Map<String, String>> list, User user){
		this.user = user;
		this.list = list;
	}
	@Override
	public void run(){
		int i = 0;
		for (Map<String, String> map2 : list) {
			i++;
			LrdYkhController lrc = new LrdYkhController();
			lrc.batchYkh(map2, user);
			//try {
				if (i == 10) {
					//Thread.sleep(3000);
				}
			
		}
	
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
