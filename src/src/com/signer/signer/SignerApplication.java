package com.signer.signer;



import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.lang.reflect.Method;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


import org.bitcoinj.wallet.Protos;
import org.bitcoinj.wallet.Protos.ScryptParameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.KeyParameter;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.Base58;
import com.google.bitcoin.core.BitcoinSerializer;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.TransactionInput;
import com.google.bitcoin.core.TransactionOutPoint;
import com.google.bitcoin.core.TransactionOutput;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.core.VerificationException;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.core.ECKey.ECDSASignature;
import com.google.bitcoin.core.Transaction.SigHash;
import com.google.bitcoin.crypto.EncryptedPrivateKey;
import com.google.bitcoin.crypto.TransactionSignature;

import com.google.bitcoin.crypto.KeyCrypterScrypt;
import com.google.bitcoin.params.MainNetParams;
import com.google.bitcoin.script.Script;
import com.google.bitcoin.script.ScriptBuilder;
import com.google.bitcoin.script.ScriptChunk;
import com.google.bitcoin.store.UnreadableWalletException;
import com.google.bitcoin.store.WalletProtobufSerializer;

import com.google.protobuf.ByteString;
import com.lambdaworks.codec.Base64;
import com.signer.db.CoinClass;
import com.signer.db.CoindbService;
import com.signer.db.DBOpenHelper;
import com.signer.db.Receive;
import com.signer.db.ReceiveService;
import com.signer.db.Send;
import com.signer.db.SendService;
import com.signer.utils.CoinSerializer;
import com.signer.utils.FileUtil;
import com.signer.utils.SignRequest;




import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Environment;

import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;




public class SignerApplication extends Application{	
	
	private  File walletFile;
	private ECKey PvtKey;
	private Wallet wallet;
	public boolean walletloaded=false;
	public boolean registered=false;	
	public int pagelength=1024;	
	public byte[] syncID=new byte[4];
	public String keyString=null;
	public byte[] walletHash160;
	public String cdbContent;
	public String currentCoinType="BTC";
	public String[] coinTypes;
	public CoindbService cs=new CoindbService(this);
	public boolean syncAll=false;
	public boolean accountstatus;
	public String tempstring;
	public String SDPATH= Environment.getExternalStorageDirectory().toString();
	public String backupPath= Environment.getExternalStorageDirectory()+"/signer/backup";
	public String coinDataPath= Environment.getExternalStorageDirectory()+"/signer/cbd";
	public List<String> accountList = new ArrayList<String>(); 
	SharedPreferences pres;
	public static final NetworkParameters NETWORK_PARAMETERS = MainNetParams.get();
	private static final String FILENAME_NETWORK_SUFFIX = NETWORK_PARAMETERS.getId().equals(NetworkParameters.ID_MAINNET) ? "" : "-testnet";
	public static final String WALLET_FILENAME_PROTOBUF = "hardbitwallet" + FILENAME_NETWORK_SUFFIX;
	
	//private static final Logger log = LoggerFactory.getLogger(ECKey.class);

    /** The parameters of the secp256k1 curve that Bitcoin uses. */
    public static final ECDomainParameters CURVE;
    /**
     * Equal to CURVE.getN().shiftRight(1), used for canonicalising the S value of a signature. If you aren't
     * sure what this is about, you can ignore it.
     */
    public static final BigInteger HALF_CURVE_ORDER;

    private static final SecureRandom secureRandom;
    //private static final long serialVersionUID = -728224901792295832L;
	static {        
        X9ECParameters params = SECNamedCurves.getByName("secp256k1");
        CURVE = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
        HALF_CURVE_ORDER = params.getN().shiftRight(1);
        secureRandom = new SecureRandom();
    }
	
	public void onCreate()
	{
		super.onCreate();		
		pres = SignerApplication.this.getSharedPreferences("Signer", 0);
		pagelength=pres.getInt("pagelength", 1024);
		accountstatus=pres.getBoolean("accountstatus", false);
		loadAccounts();		
		if (!pres.getBoolean("iniCoinData", false))
				iniCoinData();
		loadCoinData();		
		walletFile=getFileStreamPath(pres.getString("BTCaddress","")+".dat");
		if(openwallet())
		{
			walletloaded=true;		
		}
		else if (accountstatus){//wallet failed
			salvageWallet();			
		}	
			
	}
	public void loadAccounts(){
		accountList = new ArrayList<String>(); 
		  File file=getFilesDir();
		  String path=file.getAbsolutePath();		                        
		          String[] filelist = file.list();		          
		          for (int i = 0; i < filelist.length; i++) {
		        	  Log.println(3, "signer", "filelist:"+filelist[i]);
		                  File readfile = new File(path + "\\" + filelist[i]);
		                  if (!readfile.isDirectory()) {
		                	  accountList.add(filelist[i].substring(0, filelist[i].length()-4));                                      
		                  } 
		          }

		  
	}
	public boolean salvageWallet(){
		ScryptParameters sp=new ScryptParameters(false);
		sp.n_=pres.getLong("N",0);
		sp.p_=pres.getInt("P",0);
		sp.r_=pres.getInt("R",0);
		try {
			sp.salt_=ByteString.copyFrom(pres.getString("salt","").getBytes("ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {		}		
		KeyCrypterScrypt keyCrypter=new KeyCrypterScrypt(sp.salt_);		
		String inivector=pres.getString("inivector","");
		String privatekey=pres.getString("privatekey","");
		if (inivector.equals("")|privatekey.equals("")){
			SharedPreferences.Editor ed=pres.edit();
			ed.putBoolean("accountstatus", false);   
			ed.commit();
			return false;
		}			
		EncryptedPrivateKey encryptKey=new EncryptedPrivateKey(Utils.hexStringToBytes(inivector),Utils.hexStringToBytes(privatekey));
		PvtKey=new ECKey(encryptKey,Utils.hexStringToBytes(pres.getString("pubkey", "")),keyCrypter);
		wallet= new Wallet(NETWORK_PARAMETERS);
		wallet.setKeyCrypter(keyCrypter);
		wallet.addKey(PvtKey);
		Log.println(3, "signer", "app.salvagewallet salt:"+keyCrypter.getScryptParameters().getSalt().toStringUtf8());
		walletFile =getFileStreamPath(PvtKey.toAddress(NETWORK_PARAMETERS).toString()+".dat");
		try{				
	       	wallet.saveToFile(walletFile);   
	       	Log.println(3, "signer", "app.salvagewallet save walletfile OK");    
	       	walletloaded=true;
		}
	    catch(IOException e){	
	       	Log.println(3, "signer", "app.salvagewallet save wallet file failure");	 
	       	SharedPreferences.Editor ed=pres.edit();
			ed.putBoolean("accountstatus", false);   
			ed.commit();			
	       	return false;
	    }
        return true;       
	}
	public long getFee(String coinType){		
		SharedPreferences pres1 =SignerApplication.this.getSharedPreferences(coinType, 0);		 
   		return pres1.getLong("fee", 10000);
	}
	public void setFee(String coinType,long newfee){
		SharedPreferences pres1 =SignerApplication.this.getSharedPreferences(coinType, 0);
		SharedPreferences.Editor ed = pres1.edit(); 
   		ed.putLong("fee", newfee);
   		ed.commit(); 		
	}
	public void setqrlength(int qrlength){
		SharedPreferences.Editor ed = pres.edit(); 
   		ed.putInt("pagelength",qrlength);
   		ed.commit();
   		pagelength=qrlength;   		
	}
	public void loadCoinData(){
		//read cointypes
		String coinTypesSet=pres.getString("coinTypes", null);
		if(coinTypesSet==null){//if no pres cointype list record
			List<CoinClass> coinList =cs.getScrollData(0, 256);
			coinTypes=new String[coinList.size()];
			for (int i=0;i<coinList.size();i++){
				coinTypes[i]=coinList.get(i).coinType;
				coinTypesSet+=coinTypes[i];
			}
			SharedPreferences.Editor ed = pres.edit();
			ed.putString("coinTypes",coinTypesSet);
			ed.commit();
		}else{
			coinTypes=new String[(int)(coinTypesSet.length()/3)];
			for (int i=0;i<coinTypesSet.length()/3;i++){
				coinTypes[i]=coinTypesSet.substring(i*3,i*3+3);
			//	Log.println(3, "signer", "coinTypes:"+i+coinTypes[i]);
			}
		}
		//read currentcointype
		currentCoinType=pres.getString("currentCoinType", "BTC");
		if(cs.find(currentCoinType)==null)
			setCurrentCoinType("BTC");
	}
	public void setCurrentCoinType(String coinType){
		if (currentCoinType.equals(coinType))
			return;
		currentCoinType=coinType;		
		pres.edit().putString("currentCoinType", coinType);
	}
	public void changeCoinSeq(String coinType,String action){
		SharedPreferences.Editor ed = pres.edit();
		if (action.equals("top")){
			String[] tempstr=new String[coinTypes.length];
			 String coinTypesSet=coinType;
			 tempstr[0]=coinType;
	   		int j=0;
	   		for (int i=0;i<coinTypes.length;i++){
	   			if (coinTypes[i].equals(coinType)){
	   				j=1;
	   				continue;
	   			}
	   			tempstr[i+1-j]=coinTypes[i];
	   			coinTypesSet+=coinTypes[i];
	   		}
	   		coinTypes=tempstr;
	   		ed.putString("coinTypes",coinTypesSet);
			ed.commit();
			return;
		}
		if (action.equals("bottom")){
			String[] tempstr=new String[coinTypes.length];
			 String coinTypesSet=new String();			 
	   		int j=0;
	   		for (int i=0;i<coinTypes.length;i++){
	   			if (coinTypes[i].equals(coinType)){
	   				j=1;
	   				continue;
	   			}
	   			tempstr[i-j]=coinTypes[i];
	   			coinTypesSet+=coinTypes[i];
	   		}
	   		tempstr[coinTypes.length-1]=coinType;
	   		coinTypesSet+=coinType;
	   		coinTypes=tempstr;
	   		ed.putString("coinTypes",coinTypesSet);
			ed.commit();
			return;
		}
		if (action.equals("up")){			
			 String coinTypesSet=pres.getString("coinTypes", "");			 
			 int i;
			 for (i=0;i<coinTypes.length;i++){
				if (coinTypes[i].equals(coinType))
				break;
				//Log.println(3, "signer", "hww.changcoinseq coinTypes:"+i+coinTypes[i]);
			}
			 if (i>0){
				 //switch the cointype string with the previous one
				 String tempstr=coinTypes[i];
				 coinTypes[i]=coinTypes[i-1];
				 coinTypes[i-1]=tempstr;
				 int offset=i*3;
				 coinTypesSet=coinTypesSet.substring(0,offset-3)+coinTypesSet.substring(offset,offset+3)+
						 coinTypesSet.substring(offset-3,offset)+coinTypesSet.substring(offset+3,coinTypesSet.length());
			 }	   		
			return;
		}
		if (action.equals("down")){
			String coinTypesSet=pres.getString("coinTypes", "");			 
			 int i;
			 for (i=0;i<coinTypes.length;i++){
				if (coinTypes[i].equals(coinType))
				break;
				//Log.println(3, "signer", "hww.changcoinseq coinTypes:"+i+coinTypes[i]);
			}
			 if (i<coinTypes.length-1){
				 //switch the cointype string with the previous one
				 String tempstr=coinTypes[i];
				 coinTypes[i]=coinTypes[i+1];
				 coinTypes[i+1]=tempstr;
				 int offset=i*3;
				 coinTypesSet=coinTypesSet.substring(0,offset)+coinTypesSet.substring(offset+3,offset+6)+
						 coinTypesSet.substring(offset,offset+3);
				 if (i<coinTypes.length-2)
					 coinTypesSet+=coinTypesSet.substring(offset+6,coinTypesSet.length());
			 }	   		
			return;
		}
	}
	public void iniCoinData(){
		//create cdb
		DBOpenHelper dbOpenHelper = new DBOpenHelper(SignerApplication.this);
		//SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
       	//CoindbService cs=new CoindbService(this);		
		readCbdAsset("coinlist");
		SharedPreferences.Editor ed = pres.edit();
		coinTypes=new String[1];
		//parse coinlist
		try{
			JSONTokener jsonParser = new JSONTokener(cdbContent);   		
			JSONObject person = (JSONObject) jsonParser.nextValue();
			String coinstr = person.getString("coinlist");
			long existingTime=parseTime(person.getString("updatetime"));
			ed.putLong("existingcbdtime", existingTime);
			JSONArray arr = new JSONArray(coinstr);
			coinTypes=new String[arr.length()];
			String coinTypesSet=new String();
			for (int i=0; i<arr.length();i++){
				JSONObject temp = (JSONObject) arr.get(i);
				coinTypes[i]=temp.getString("cointype");
				coinTypesSet+=coinTypes[i];
			}
			//save cointypes to pres
			ed.putString("coinTypes",coinTypesSet);
			ed.commit();		
		}		
		catch (Exception e){
			Log.println(3, "signer","app.inicoindata coinlist file fail"+e);
			return;
		}
		// read coin types basic data
		copyImgFile("ALL");
		for (int i=0; i<coinTypes.length;i++){			
			copyImgFile(coinTypes[i]);
			readCbdAsset(coinTypes[i]);
			CoinClass coinClass=new CoinClass(cdbContent);
			cs.save(coinClass);	
			//set minfee in pres
			setFee(coinTypes[i],coinClass.min_fee.longValue());
			//create send and receive tables for cointype
			dbOpenHelper.createCoinTable(dbOpenHelper.getWritableDatabase(),coinTypes[i]);			
		}
		ed.putBoolean("iniCoinData",true);
		ed.commit();		
	}
	

	public static long parseTime(String timeString){
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd" );
		try {
			Date date = sdf.parse(timeString);
			return date.getTime();
		} catch (ParseException e) {
			Log.println(3, "signer", "app.parsetime:wrong TIME format");
			return 0;			
		}		
	}
	private void readCbdAsset(String fileNameHeader){
		 AssetManager am = getResources().getAssets();
		 String fileName="cbd/"+fileNameHeader+".cbd";
	      try  
	      {  
	          InputStream is = am.open(fileName); 
	          byte [] buffer = new byte[is.available()]; 
	          is.read(buffer);  
	          cdbContent=new String(buffer,"GBK");
	          is.close();  
	      }  
	      catch (IOException e)  
	      {  
	    	  Log.println(3, "signer","app.readcbdasset:"+e.toString());  
	      }  
	}
	private void copyImgFile(String fileNameHeader){
		 AssetManager am = getResources().getAssets();
		 String fileName="cbd/"+fileNameHeader+".png";
		 String fileName2=fileNameHeader+".png";
		 Log.println(3, "signer","copyImgFile destination:"+fileName2);		 
	      try  
	      {  
	    	  FileUtil fileUtil=new FileUtil();
//	    	  fileUtil.createSDDir(SDPATH+"signer");
//	    	  fileUtil.createSDDir(SDPATH+"signer/cbd");
	          InputStream is = am.open(fileName); 
	          fileUtil.write2SDFromInput(coinDataPath, fileName2, is);
//	          byte [] buffer = new byte[is.available()]; 
//	          is.read(buffer); 
//	          is.close();  
//	          FileOutputStream fout = new FileOutputStream(fileName2);
//	          fout.write(buffer); 
//	           fout.close(); 
	      }  
	      catch (IOException e)  
	      {  
	    	  Log.println(3, "signer","copyImgFile:"+e.toString());  
	      }  
	}
	public Bitmap getCoinIconBmp(String coinType)  
	  {  
	      Bitmap image = null;
	      try  
	      {  
	 		 String fileName=coinDataPath+"/"+coinType+".png";
	 		File file = new File(fileName);
	 		FileInputStream fis = new FileInputStream(file);  
	        image = BitmapFactory.decodeStream(fis);  
	        Log.println(3, "signer","app.getCoinIconBmp success");  
	      }  
	      catch (IOException e)  
	      {  
	    	  Log.println(3, "signer","app.getCoinIconBmp:"+e.toString());  
	      }	  
	      return image;	  
	  }  
	public String[] getBackupQr(){
		 InputStream input;
		 byte[] walletbyte;
		try {
			input = new FileInputStream(walletFile);					
			walletbyte = new byte[input.available()];
			input.read(walletbyte);
			walletHash160=Utils.sha256hash160(walletbyte);
			input.close();
		} catch (Exception e1) {
			Log.println(3, "signer","app.getBackupqr:"+e1.toString()); 
			return null;
		}
		int pages=(int)Math.ceil((double)(walletbyte.length+5)/(double)(pagelength-9));
		Log.println(3, "signer","backupQrpages:"+pages);
		Log.println(3, "signer","walletbytes::"+walletbyte.length);
		String[] accountString=new String[pages];
		int length;
		for (int i=0;i<pages;i++){
			if (i<pages-1){
				length=pagelength;	
			}else{
				length=(walletbyte.length+5)%(pagelength-9)+9;
			}
			byte[] currentPage=new byte[length];
			currentPage[0]=(byte)0xba;
			currentPage[1]=(byte)pages;
			currentPage[2]=(byte)(i+1);			
			currentPage[3]=(byte)((int)(length-5)>>8&(byte)0xff);
			currentPage[4]=(byte)((int)(length-5)&(byte)0xff);
			if (i==0){
				currentPage[5]=0x48;//H
				currentPage[6]=0x42;//B
				currentPage[7]=0x31;//1
				currentPage[8]=(byte)((int)(walletbyte.length)>>8&(byte)0xff);
				currentPage[9]=(byte)((int)(walletbyte.length)&(byte)0xff);					
				System.arraycopy(walletbyte,0,currentPage,10,length-9-5);
			}else{
				System.arraycopy(walletbyte,i*(pagelength-9)-5,currentPage,5,length-9);
			}
			currentPage=addCheckSum(currentPage);
			try {
				accountString[i]=new String(currentPage,"ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				Log.println(3, "signer","getbackupqr toQrerror:"+e);
			}
			Log.println(3,"signer","backupstr"+i+":"+Utils.bytesToHexString(currentPage));
		}		
		return accountString;
	}
	
	public static byte[] intToByteArray(int i) {   
		  byte[] result = new byte[4];   
		  result[0] = (byte)((i >> 24) & 0xFF);
		  result[1] = (byte)((i >> 16) & 0xFF);
		  result[2] = (byte)((i >> 8) & 0xFF); 
		  result[3] = (byte)(i & 0xFF);
		  return result;
	}
	public static int byteArrayToInt(byte[] b, int offset) {
	       int value= 0;
	       for (int i = 0; i < 4; i++) {
	           int shift= (4 - 1 - i) * 8;
	           value +=(b[i + offset] & 0x000000FF) << shift;
	       }
	       return value;
	 }
	public static int getInt(byte[] data,int offset, int length){
		byte[]tempint=new byte[4];
		for (int i=0;i<length;i++){
			tempint[(4-length)+i]=data[offset+i];
		}		
	    return byteArrayToInt(tempint,0);
	}
	
	
	public static byte[] longToByteArray(long number) {
	long temp = number;
	byte[] b = new byte[8];
	for (int i = b.length - 1; i > -1; i--)	{
	b[i] = Long.valueOf(temp & 0xff).byteValue();
	temp = temp >> 8;
	}
	return b;
	}	
	
	public byte[] integratearray(byte[] data1,byte[] data2){
		byte[] data3 = new byte[data1.length+data2.length];
		  System.arraycopy(data1,0,data3,0,data1.length);
		  System.arraycopy(data2,0,data3,data1.length,data2.length);
		  return data3;
	}
	
	
	
	
	public boolean createAccount(String password,String type){
		 wallet = new Wallet(NETWORK_PARAMETERS);
		 // create keypair
		 if (type.equals("manual")){
			 ManualCreateKey keyGen=new ManualCreateKey();
			 ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters(CURVE, secureRandom);
		     keyGen.init(keygenParams);
			 PvtKey=new ECKey(keyGen.generatePriv(keyString));
			 Log.println(3, "signer", Utils.bytesToHexString(PvtKey.getPubKey()));
		 }
		 else{		 
			 PvtKey=new ECKey();
		 }
		 wallet.addKey(PvtKey);	 
		 // encrypt by password
		 wallet.encrypt(password);	
		 // save wallet file	
		 walletFile =getFileStreamPath(PvtKey.toAddress(NETWORK_PARAMETERS).toString()+".dat");
		try{			
	       	wallet.saveToFile(walletFile);   
	       	Log.println(3, "signer", "save walletfile OK:"+walletFile.getName());    
	       	walletloaded=true;
		}
	    catch(IOException e){	
	       	Log.println(3, "signer", "save wallet file failure");	 
	       	return false;
	    }
			// save to pres
		if(saveKeysToPres()){
			DBOpenHelper dbOpenHelper = new DBOpenHelper(SignerApplication.this);   		
	   		dbOpenHelper.getWritableDatabase();	
	   		loadAccounts();
			return true;
		}
        return false;
	}
	public boolean changepwd(String password){
		Wallet wallet1 = new Wallet(NETWORK_PARAMETERS);
		 wallet1.addKey(PvtKey.decrypt(PvtKey.keyCrypter, PvtKey.keyCrypter.deriveKey(tempstring)));
		 tempstring="";
		 wallet1.encrypt(password);		 
		 //set backup status to unbackuped
		SharedPreferences pres = getSharedPreferences("Signer", 0);
		pres.edit().putString("backupstatus", "").commit();
		 // save wallet file
		walletFile =getFileStreamPath(PvtKey.toAddress(NETWORK_PARAMETERS).toString()+".dat");
		try{				
	       	wallet1.saveToFile(walletFile);   
	       	Log.println(3, "signer", "app.changpwd save walletfile OK");    
	       	walletloaded=true;
		}
	    catch(IOException e){	
	       	Log.println(3, "signer", "app.changpwd save wallet file failure");	 
	       	return false;
	    }
		wallet=wallet1;
			// save to pres,also includes set pvtkey to encrypted
		if(saveKeysToPres())			
			return true;		
       return false;
	}
	
	public boolean saveKeysToPres(){
		for (final ECKey key : wallet.getKeys()){				
		if (key.getEncryptedPrivateKey()!= null){
   			PvtKey=key; 
   			walletloaded=true;   			
   			SharedPreferences.Editor ed = pres.edit();   			
  			ed.putString("pubkey",Utils.bytesToHexString(PvtKey.getPubKey()));  			
   			ed.putString("BTCaddress",PvtKey.toAddress(NETWORK_PARAMETERS).toString());   			
            ed.putString("privatekey",Utils.bytesToHexString(PvtKey.getEncryptedPrivateKey().getEncryptedBytes()));
           ed.putString("inivector",Utils.bytesToHexString(PvtKey.getEncryptedPrivateKey().getInitialisationVector()));
  	        ed.putInt("EncryptionType",wallet.getEncryptionType().getNumber());
            ScryptParameters sp= wallet.getKeyCrypter().getscryptParameters();
   	         try {
 	        	 ed.putString("salt",sp.getSalt().toString("ISO-8859-1"));
 		      	 ed.putLong("N",sp.getN());//2
   		       	 ed.putInt("P", sp.getP());//3
   		       	 ed.putInt("R", sp.getR());//1
   		     } catch (UnsupportedEncodingException e) {		
   		    	 return false;
   		         }   		     
   		     ed.putBoolean("accountstatus", true); 
   		     ed.commit();   		   
    		 return true;        							
    	  }
		}
		return false;
	}
	public boolean importWallet(byte[]wallettosave){		
			// try to read the wallet file to see if it is correct
			ByteArrayInputStream is = new ByteArrayInputStream(wallettosave);
			Wallet wallet1;
			try {	
				wallet1=new Wallet(NETWORK_PARAMETERS);
				WalletProtobufSerializer wpf=new WalletProtobufSerializer();
				wallet1 = wpf.readWallet(is);
				if (wallet1.getKeys().isEmpty())
				{
					Log.println(3, "signer", "importwallet fail :no keys");
					return false;
				}
			} catch (UnreadableWalletException e1) {
				Log.println(3, "signer", "importwallet fail :wrong format"+e1.toString());
				return false;
			}			
			if (PvtKey!=null&&PvtKey.equals(wallet1.getKeys().get(0))){// the backup is the same as existing one				
				// do nothing
				return true;
			}
			walletFile =getFileStreamPath(wallet1.getKeys().get(0).toAddress(NETWORK_PARAMETERS).toString()+".dat");					
			try{				
		       	wallet1.saveToFile(walletFile);		       	
		       	Log.println(3, "signer", "save walletfile OK");
		       	wallet=wallet1;
		       	if (!saveKeysToPres())// read key out of wallet
		       		return false;		
		       	// clear the db, and set backup status to true		       	
				cs.clearAllCoins();		       	
		       	SharedPreferences.Editor ed = pres.edit(); 
		       	ed.putString("backupstatus","Backuped");  
		       	ed.commit();       	
		       	loadAccounts();
		       	Log.println(3, "signer", "importwallet success");
		       	return true;		       	
			}
			catch(IOException e){	
		       	Log.println(3, "signer", "save wallet file failure");	       	
		    }
			
		return false;
	}	
	
	public byte[] getAddressBytes(String coinType){	
		if(coinType.equals("ALL"))
			coinType="BTC";
		byte[] addressBytes=new byte[25];
		if (walletloaded){
			Log.println(3, "signer","app.getaddressbytes cointype:"+coinType);
			addressBytes[0]=cs.find(coinType).address_header;
			System.arraycopy(PvtKey.getPubKeyHash(), 0, addressBytes, 1,20);		    
		    byte[] check = Utils.doubleDigest(addressBytes, 0, 21);
		    System.arraycopy(check, 0, addressBytes, 21, 4);
		    return addressBytes;			
		}
		return new byte[25];
	}	
	public String getAddressString(String coinType){
		return Base58.encode(getAddressBytes(coinType));		
	}
	public byte[] getPubKeyHash(){
		return PvtKey.getPubKeyHash();
	}
	public boolean verifyTargetAddress(String coinType,String address){

		byte tmp[];
		try {
			tmp = Base58.decode(address);
		} catch (AddressFormatException e) {
			Log.println(3, "signer","app.verifytargetaddress address format error");
			return false;
		}
		 if (tmp.length !=25)
		 {
			 Log.println(3, "signer","app.verifytargetaddress address length error");
			 return false;
		 }
		if (tmp[0]!=cs.find(coinType).address_header&&tmp[0]!=cs.find(coinType).multisig_address_header){
			Log.println(3, "signer","app.verifytargetaddress address header error");
			return false;
		}
		byte[] bytes = Base58.copyOfRange(tmp, 0, tmp.length - 4);
		byte[] checksum =  Base58.copyOfRange(tmp, tmp.length - 4, tmp.length);		        
		tmp = Utils.doubleDigest(bytes);
		byte[] hash =  Base58.copyOfRange(tmp, 0, 4);
		if (!Arrays.equals(checksum, hash))	{
			Log.println(3, "signer","app.verifytargetaddress address hash error");
		    return false;
		}
		return true;
	}

	
	
	public Boolean verifypassword(String password){
		if (walletloaded){
			if(wallet.checkPassword(password)){
				//Log.println(3, "signer", "checkpasswordok");
				return true;
			}
			Log.println(3, "signer", "checkpasswordwrong");
			return false;
		}
		Log.println(3, "signer", "checkpassworderror:wallet not loaded");
		return false;
	}
	public Boolean switchAccount(String address){
		Log.println(3, "signer", "app.switchaccount address:"+address);
		walletFile=getFileStreamPath(address+".dat");
		if(!openwallet()){
			Log.println(3, "signer", "app.switchaccount openwallet fail");
			return false;
		}
		if (!saveKeysToPres())// read key out of wallet{
		{
			Log.println(3, "signer", "app.switchaccount save to pres fail");		
	       		return false;
		}
		return true;
	}
	private Boolean openwallet()
	{
				
		if (walletFile.exists())
		{
			FileInputStream walletStream = null;
			try
			{
				walletStream = new FileInputStream(walletFile);
				wallet = new WalletProtobufSerializer().readWallet(walletStream);
				Log.println(3, "signer","readwalletfileok");
				if (wallet.getKeys().isEmpty())
				{
					return false;
				}
				PvtKey=wallet.getKeys().get(0);
				Log.println(3, "signer","getkeysok");
				walletloaded=true;
				return true;
			
			}
			catch (final FileNotFoundException x)			
			{
				Log.println(3, "signer","app.openwallet wallet file not found");
				return false;
			}
			catch (final UnreadableWalletException x)
			{
				Log.println(3, "signer","app.openwallet wallet unreadable");
				return false;
			}
			finally
			{
				if (walletStream != null)
				{
					try
					{
						walletStream.close();
					}
					catch (final IOException x)
					{
					}
				}				
			}			
		}		
					return false;
			
	}	
	public static PackageInfo packageInfoFromContext(final Context context)
{
	try
	{
		return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
	}
	catch (final NameNotFoundException x)
	{
		throw new RuntimeException(x);
	}
}
	
	
	
	public NetworkParameters getCoinParams(String coinType){
		NetworkParameters params=MainNetParams.get();
		CoinClass cc=cs.find(coinType);		
		params.addressHeader=(int)cc.address_header;
		params.p2shHeader=(int)cc.multisig_address_header;
		params.acceptableAddressCodes = new int[] { params.addressHeader, params.p2shHeader };
		params.packetMagic=cc.tx_header;
		params.MAX_MONEY=cc.max_tx_amount;
		return params;
	}
	
	



	public String[] packQr(SignRequest sr,String pubkey, String[] sigs){
		if (sr.command.equals("signHashes")){
			//SignResult feedback=new SignResult("signResult",sr.address,sr.time,sigs);
			String resultStr="";
			resultStr+="{\"cmd\":\"";
			resultStr+="signResult";
			resultStr+="\",\"addr\":\"";
			resultStr+=sr.address;
			resultStr+="\",\"time\":\"";
			resultStr+=sr.time;
			resultStr+="\",\"pubkey\":\"";
			resultStr+=pubkey;
			resultStr+="\",\"sigs\":[";
			for(int i=0;i<sigs.length;i++){
				resultStr+="\"";
				resultStr+=sigs[i];
				resultStr+="\"";
				if (i<sigs.length-1){
					resultStr+=",";
				}
			}
			resultStr+="]}";
			Log.println(3,"signer","result json string:"+resultStr);
			int pages=(int)Math.ceil((double)resultStr.length()/(double)(pagelength-9));
			String[] qrs=new String[pages];
			String tp=String.valueOf(pages);
			if (tp.length()==1){
				tp="0"+tp;
			}
			for (int i=0;i<pages;i++){
				String currentPage="Sign";
				String cp=String.valueOf(i+1);
				if (cp.length()==1){
					cp="0"+cp;
				}
				currentPage+=cp;
				currentPage+="/";
				currentPage+=tp;
				if(i<pages-1){
					currentPage+=resultStr.substring(i*(pagelength-9),(i+1)*(pagelength-9));							
				}else{
					currentPage+=resultStr.substring(i*(pagelength-9));
				}
				qrs[i]=currentPage;
			}
			return qrs;
		}
		return null;		
	}
	public static byte[] addCheckSum(byte[] currentPage){
		Log.println(3,"gbol","currentpage length:"+currentPage.length);
		byte[] trimedbytes=new byte[currentPage.length-4];
		System.arraycopy(currentPage,0,trimedbytes,0,trimedbytes.length);
		byte[] hash=Utils.sha256hash160(trimedbytes);
		System.arraycopy(hash,0,currentPage,currentPage.length-4,4);		
		return currentPage;
	}
	
	
	


	public Bitmap getImgFromAssetsFile(String fileName)  
	  {  
	      Bitmap image = null;  
	      AssetManager am = getResources().getAssets();  
	      try  
	      {  
	          InputStream is = am.open(fileName);  
	          image = BitmapFactory.decodeStream(is);  
	          is.close();  
	      }  
	      catch (IOException e)  
	      {  
	    	  Log.println(3, "signer",e.toString());  
	      } 
	  
	      return image;  
	  
	  }  
	public String getCoinImgFileName(String coinType){
		String fileName="";
		if (coinType.equals("ALL"))
			return "cbd/"+coinType+".png";
		for (int i=0;i<coinTypes.length;i++){
			if (coinTypes[i].equals(coinType)){
				if (i<6)
					fileName="cbd/"+coinType+".png";
				else
					fileName= "";
				break;
			}
		}
		return fileName;
			
	}

	public String[] doSignRequest(String fullRequest){
		SignRequest sr;
		try {
			sr = new SignRequest(fullRequest);
		} catch (JSONException e) {
			return null;
		}		
			String[] sigs=signHashes(sr.hashes);
			String pubkey=String.valueOf(Base64.encode(PvtKey.getPubKey()));
			String[] signFeedback=packQr(sr,pubkey,sigs);
		return 	signFeedback;		
	}
	public String[] signHashes(String[] hashes){
		KeyParameter aeskey	=PvtKey.keyCrypter.deriveKey(tempstring);//get aes key from password
		ECKey decryptedkey=PvtKey.decrypt(PvtKey.keyCrypter,aeskey);
		if (!PvtKey.toAddress(NETWORK_PARAMETERS).equals(decryptedkey.toAddress(NETWORK_PARAMETERS))){
			Log.println(3,"signer","app.sighHashes wrong password");		
			return null;		
		}
		String[] sigs=new String[hashes.length];		 
		for (int i=0;i<hashes.length;i++){
			Sha256Hash hash=new Sha256Hash(Base64.decode(hashes[i].toCharArray()));
			Log.println(3,"signer","app.sighHashes hash:"+Utils.bytesToHexString(Base64.decode(hashes[i].toCharArray())));
			ECDSASignature sig=decryptedkey.sign(hash,aeskey);			
			
//			if (!ECKey.verify(hash.getBytes(), sig.encodeToDER(),PvtKey.getPubKey())){
//				Log.println(3,"signer","app.sighHashes verify failed");
//				return null;
//			}
			char[] encodedSig=Base64.encode(sig.encodeToDER());
			Log.println(3,"signer","sig:"+Utils.bytesToHexString(sig.encodeToDER()));
			Log.println(3,"signer","sig:"+String.valueOf(encodedSig));
			sigs[i]=String.valueOf(encodedSig);
		}
		tempstring="";
		return sigs;
	}
}
