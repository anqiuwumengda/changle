package com.tgb.util;

import java.io.File;

import junit.framework.Assert;

import com.tgb.task.BatchTask;

public class FileUtil {

	/*
	 * 清空文件夹
	 */
	public static void deleteAllToNew(String path) {   
   	 File file  = null;  
         
        try{  
          
        file = new File(path);  
          
        file.createNewFile();              
          
        FileUtil.deleteAll(file);
        
          
        }catch (Exception e) {  
            // TODO: handle exception  
        }     
        Assert.assertFalse(file.exists());     
   	
      //  String filePath = "D:/sql";
			File f = new File(path);
		
			if (!file.exists()) {
				f.mkdirs();
		}
   }
    
   public static void deleteAll(File file){  
   	   
   	   if(file.isFile() || file.list().length ==0)  
   	   {  
   	   file.delete();       
   	   }else{      
   	     File[] files = file.listFiles();  
   	     for (int i = 0; i < files.length; i++) {  
   	    deleteAll(files[i]);  
   	    files[i].delete();      
   	   }  
   	       if(file.exists())         //如果文件本身就是目录 ，就要删除目录  
   	       file.delete();  
   	   }  
  } 
}
