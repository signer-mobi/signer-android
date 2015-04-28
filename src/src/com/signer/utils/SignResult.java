package com.signer.utils;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
public class SignResult {
	public String command="";
	public String address="";
	public String time="";	
	public String[] sigs ;
	public SignResult(String command,String address,String time,String[] sigs) {
		this.command=command;
		this.address=address;
		this.time=time;	
		this.sigs=sigs ;
	}
}
