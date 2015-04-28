package com.signer.db;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.bitcoin.core.Utils;
import com.signer.signer.SignerApplication;

import android.util.Log;

public class CoinClass {
	public int id;
	 public String coinType;
	 public String[] coinName;	 
	 public int digits;
	 public int digits_after_dot;
	 public int confirmation_interval;
	 public BigInteger min_fee;
	 public BigInteger max_tx_amount;
	 public Boolean pos;
	 public int tx_header;
	 public int max_tx_size;
	 public byte address_header;
	 public byte multisig_address_header;
	 public BigInteger max_amount;
	 public  int halflife;
	 public BigInteger block_reward;
	 public String pow_algorithm;
	 public String ecdsa_curve;
	 public long start_date;
	 public String[] nodes;
	 public long updatetime;
	
	 public CoinClass(String cdbContent) {			
			this.id = 0;
			JSONTokener jsonParser = new JSONTokener(cdbContent);   		
			JSONObject person;
			try {
				person = (JSONObject) jsonParser.nextValue();			
				coinType=person.getString("cointype");
				setCoinName(person.getString("coinname"));
				digits=(int)Long.parseLong(person.getString("digits"));
				digits_after_dot=(int)Long.parseLong(person.getString("digits_after_dot"));
				confirmation_interval=(int)Long.parseLong(person.getString("confirmation_interval"));
				 min_fee=new BigInteger(person.getString("min_fee"));
				 max_tx_amount=new BigInteger(person.getString("max_tx_amount"));
				 pos=person.getString("pos").equals("true")?true:false;
				 tx_header=SignerApplication.byteArrayToInt(Utils.hexStringToBytes(person.getString("tx_header")),0);
				 max_tx_size=(int)Long.parseLong(person.getString("max_tx_size"));
				 address_header=Utils.hexStringToBytes(person.getString("address_header"))[0];
				 multisig_address_header=Utils.hexStringToBytes(person.getString("multisig_address_header"))[0];
				 max_amount=new BigInteger(person.getString("max_amount"));
				 halflife=(int)Long.parseLong(person.getString("halflife"));
				 block_reward=new BigInteger(person.getString("block_reward"));
				 pow_algorithm=person.getString("pow_algorithm");
				 ecdsa_curve=person.getString("ecdsa_curve");
				 setStartDate(person.getString("start_date"));
				 setNodes(person.getString("nodes"));
				 setUpdateTime(person.getString("updatetime"));	
			} catch (JSONException e) {
				coinType="";
				Log.println(3, "hb01m",e.toString());
			}
	}
	 public CoinClass() {
		 this.id =0;		
		 coinName=new String[2];
	}

	 void setCoinName(String coinNameArray){
		 JSONArray arr;
		try {
			arr = new JSONArray(coinNameArray);		
			coinName=new String[arr.length()];
			for (int i=arr.length()-1;i>=0;i--){
				JSONObject temp = (JSONObject) arr.get(i);
				try {
					coinName[i]=new String(temp.getString("coinname").getBytes("ISO-8859-1"),"GBK");
				} catch (UnsupportedEncodingException e) {}
			}
		} catch (JSONException e) {		}
	 }
	 void setNodes(String nodesArray){
		 JSONArray arr;
		try {
			arr = new JSONArray(nodesArray);		
			nodes=new String[arr.length()];
			for (int i=arr.length()-1;i>=0;i--){
				JSONObject temp = (JSONObject) arr.get(i);
				nodes[i]=temp.getString("ip").length()>5?temp.getString("ip"):temp.getString("domain_name");
			}
		} catch (JSONException e) {		}
	 }
	void setStartDate(String timeString){
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd" );
		try {
			Date date = sdf.parse(timeString);
			start_date=date.getTime();
		} catch (ParseException e) {
			Log.println(3, "hb01m", "wrong startdate format");
		}
	}
	void setUpdateTime(String timeString){
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd" );
		try {
			Date date = sdf.parse(timeString);
			updatetime=date.getTime();
		} catch (ParseException e) {
			Log.println(3, "hb01m", "wrong updatetime format");
		}
	}
	
	
	
	

}
