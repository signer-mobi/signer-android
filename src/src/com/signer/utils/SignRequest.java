package com.signer.utils;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;
public class SignRequest {
	public String command="";
	public String address="";
	public String time="";	
	public String[] hashes ;
	public SignRequest(String fullRequest) throws JSONException{
		Log.println(3,"signer","SignRequest:"+fullRequest);
	  JSONTokener jsonParser = new JSONTokener(fullRequest);
	  JSONObject jObject = (JSONObject) jsonParser.nextValue();	  
		command=jObject.getString("cmd");
		time=jObject.getString("time");
		address=jObject.getString("addr");		
		String hashesStr=jObject.getString("hashes");
		JSONArray arr = new JSONArray(hashesStr);
		hashes=new String[arr.length()];
		Log.println(3,"signer","SignRequest:"+hashesStr);
		for (int i=0;i<arr.length();i++){			   
			hashes[i] = arr.getString(i);			
		}
	}
}
