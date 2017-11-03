package com.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

public class SeleniumInputClient {
	 public String inputInfo(Map<String, Object> resultList,Map<String, Object> mapPeiOu,Map<String, Object> famMoney,String method){
		 String meg ="";
		 try { 
			 CustInfoAuto custInfoAuto =(CustInfoAuto) Naming.lookup("rmi://127.0.0.1:8888/InputInfo"); 
			 meg = custInfoAuto.inputInfoDetail(resultList,mapPeiOu,famMoney,method);
        } catch (NotBoundException e) {
        	meg="对象绑定异常！";        	
            e.printStackTrace(); 
        } catch (MalformedURLException e) { 
        	meg="发生URL畸形异常！";
            e.printStackTrace(); 
        } catch (RemoteException e) {
        	meg="创建远程对象发生异常！";
            e.printStackTrace();   
        } 
		 return meg;
	 }
	    
}
