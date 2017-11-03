package com.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface  CustInfoAuto extends Remote  {

    public String inputInfoDetail(Map<String, Object> resultList,Map<String, Object> mapPeiOu,Map<String, Object> famMoney,String method) throws RemoteException;    

    
}
