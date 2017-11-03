package com.tgb.util;

public class IDCardVerifUtil {

	
	//身份证号验证
	public static final boolean IDCardVef(String idCard){
		idCard=getIDCard(idCard);
        Boolean flag=isIdCard(idCard);
		return flag;
	}
	
	
	 // 若是15位，则转换成18位；否则直接返回ID  
    public static final String getIDCard(String idCard) {  
        // 若是15位，则转换成18位；否则直接返回ID  
        if (15 == idCard.length()) {  
            int[] W = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };  
            String[] A = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };  
            int i, j, s = 0;  
            String newCard;  
            newCard = idCard;  
            newCard = newCard.substring(0, 6) + "19" + newCard.substring(6, idCard.length());  
            for (i = 0; i < newCard.length(); i++) {  
                j = Integer.parseInt(newCard.substring(i, i + 1)) * W[i];  
                s = s + j;  
            }  
            s = s % 11;  
            newCard = newCard + A[s];  
            return newCard;  
        } else {  
            return idCard;  
        }  
    } 
  //判断18位身份证是否真实  
    public static boolean isIdCard(String arrIdCard) {  
        int sigma = 0;  
        Integer[] a = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};  
        String[] w = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};  
        for (int i=0; i<17; i++) {  
            int ai = Integer.parseInt(arrIdCard.substring(i,i+1));  
            int wi = a[i];  
            sigma += ai * wi;  
        }  
        int number = sigma % 11;  
        String check_number = w[number];  
        if (!arrIdCard.substring(17).equals(check_number)) {  
            return false;  
        } else {  
            return true;  
        }  
    }  
}
