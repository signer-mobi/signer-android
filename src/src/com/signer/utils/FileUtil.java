package com.signer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;
import android.util.Log;

public class FileUtil{
 private String SDPATH;
 
 public String getSDPATH(){
  return SDPATH;
 }
 
 public FileUtil(){
  SDPATH= Environment.getExternalStorageDirectory()+"/";
 }
 
 public File createSDFile(String fileName) throws IOException{
  File file = new File(fileName);
  file.createNewFile();
  return file;
 }
 public File createSDDir(String dirName) {
  File dir = new File(dirName);
  dir.mkdir();
  return dir;
 }
 public boolean isFileExist(String fileName){
  File file = new File(fileName);
  return file.exists();
 }
 public boolean write2SDFromInput(String path,String fileName,InputStream input){
  File file = null;
  OutputStream output = null;
  try{
	  createSDDir(SDPATH+"signer");
	  createSDDir(SDPATH+"signer/cbd");	  
	  Log.println(3, "signer","path+filename:"+path+"/"+fileName);
	  file = new File(path+"/"+fileName);   
   output = new FileOutputStream(file); 
  // byte buffer[] = new byte[4*1024];
   byte buffer[]=new byte[input.available()];
   while((input.read(buffer))!=-1){
    output.write(buffer);
    Log.println(3, "signer","save file OK:"+fileName);
   }
   output.flush();   
  }catch(Exception e){
	  Log.println(3, "alan","save file error:"+e);
	  return false;
 
  }finally{
   try{
    output.close();
   }
   catch(Exception e){
    e.printStackTrace();
   }
  }
  return true;
  //return file;
 }
 public boolean writeFileSdcard(String fileName,byte[] bytes){ 

     try{ 

      //FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);

     FileOutputStream fout = new FileOutputStream(fileName);

      //byte [] bytes = message.getBytes(); 

      fout.write(bytes); 
       fout.close(); 
       return true;

      } 

     catch(Exception e){ 
    	 Log.println(3, "alan","save file error:"+e);
      return false;
     } 

 }
 public String readFileSdcard(String fileName){

     String res=""; 

     try{ 

      FileInputStream fin = new FileInputStream(fileName); 

      int length = fin.available(); 

      byte [] buffer = new byte[length]; 

      fin.read(buffer);     

      res = EncodingUtils.getString(buffer, "UTF-8"); 

      fin.close();     

     } 

     catch(Exception e){ 

      e.printStackTrace(); 

     } 

     return res; 

}
}

