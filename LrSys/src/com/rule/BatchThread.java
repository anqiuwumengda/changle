package com.rule;

import java.util.List;
import java.util.Map;

import com.tgb.controller.propertymanage.LrdCallRuleController;
import com.tgb.model.User;

public class BatchThread extends Thread{
	private List<Map<String, String>> list = null;
	private User user;
	private String batchDate;
	public BatchThread(List<Map<String, String>> list, User user,String batchDate){
		this.user = user;
		this.list = list;
		this.batchDate=batchDate;
	}
	@Override
	public void run(){
		int i = 0;
		for (Map<String, String> map2 : list) {
			i++;
			LrdCallRuleController lrc = new LrdCallRuleController();
			lrc.batch(map2, user, this.batchDate);
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
