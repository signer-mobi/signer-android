package com.signer.db;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.google.bitcoin.core.Utils;
import com.signer.signer.SignerApplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



public class CoindbService {
	private DBOpenHelper dbOpenHelper;
	
	public CoindbService(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}


	public void save(CoinClass coin){		
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into cbd (cointype, coinNameen, coinnamecn, digits, digits_after_dot, confirmation_interval, min_fee, max_tx_amount, pos, tx_header, max_tx_size,  address_header, multisig_address_header,"+
				" max_amount, halflife, block_reward, pow_algorithm, ecdsa_curve, start_date, updatetime ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[]{coin.coinType, coin.coinName[0], coin.coinName[1], coin.digits, coin.digits_after_dot, coin.confirmation_interval, coin.min_fee.toString(), coin.max_tx_amount.toString(),coin.pos?1:0,
				coin.tx_header, coin.max_tx_size, coin.address_header,coin.multisig_address_header,coin.max_amount.toString(),coin.halflife,coin.block_reward,coin.pow_algorithm,coin.ecdsa_curve,coin.start_date,coin.updatetime});
		
	}
	
	public void update(CoinClass coin){
		//delete(coin.cointype);
		//save(coin);		
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update cbd set coinnameen = ?, coinnamecn = ?, digits = ?, digits_after_dot = ?, confirmation_interval = ?, min_fee = ?, max_tx_amount = ?, pos = ?,  tx_header = ? , max_tx_size = ? ,address_header = ?, multisig_address_header = ?,"+
				" max_amount = ?, halflife = ?, block_reward = ?, pow_algorithm = ?, ecdsa_curve = ?, start_date = ?, updatetime = ?  where cointype=?", 
				new Object[]{coin.coinName[0], coin.coinName[1], coin.digits, coin.digits_after_dot, coin.confirmation_interval, coin.min_fee.toString(), coin.max_tx_amount.toString(),coin.pos?1:0,
						coin.tx_header, coin.max_tx_size,coin.address_header,coin.multisig_address_header,coin.max_amount.toString(),coin.halflife,coin.block_reward,coin.pow_algorithm,coin.ecdsa_curve,coin.start_date,coin.updatetime, coin.coinType});				
	}
	
	public void delete(String coinType){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		//delete item in cbd
		db.execSQL("delete from cbd where cointype=?", new Object[]{coinType});
		//delete receive and send tables
		dbOpenHelper.clear(db, coinType);
	}
	
	
	public CoinClass find(String coinType){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from cbd where cointype=?", new String[]{coinType});
		if(cursor.moveToFirst()){
			CoinClass coin=new CoinClass();
			coin.id=cursor.getInt(cursor.getColumnIndex("id"));
			coin.coinType=coinType;
			coin.coinName[0]=cursor.getString(cursor.getColumnIndex("coinnameen"));
			coin.coinName[1]=cursor.getString(cursor.getColumnIndex("coinnamecn")); 	 
			coin.digits=cursor.getInt(cursor.getColumnIndex("digits"));
			coin.digits_after_dot=cursor.getInt(cursor.getColumnIndex("digits_after_dot"));
			coin.confirmation_interval=cursor.getInt(cursor.getColumnIndex("confirmation_interval"));
			coin.min_fee=new BigInteger(cursor.getString(cursor.getColumnIndex("min_fee"))); 
			coin.max_tx_amount=new BigInteger(cursor.getString(cursor.getColumnIndex("max_tx_amount"))); 
			coin.pos=(cursor.getInt(cursor.getColumnIndex("digits"))==1)?true:false;			
			coin.tx_header=SignerApplication.byteArrayToInt(Utils.hexStringToBytes(cursor.getString(cursor.getColumnIndex("tx_header"))),0);
			coin.max_tx_size=cursor.getInt(cursor.getColumnIndex("max_tx_size"));			
			coin.address_header=(byte)cursor.getInt(cursor.getColumnIndex("address_header"));
			coin.multisig_address_header=(byte)cursor.getInt(cursor.getColumnIndex("multisig_address_header"));
			coin.max_amount=new BigInteger(cursor.getString(cursor.getColumnIndex("max_amount"))); 
			coin.halflife=cursor.getInt(cursor.getColumnIndex("halflife"));
			coin.block_reward=new BigInteger(cursor.getString(cursor.getColumnIndex("block_reward"))); 
			coin.pow_algorithm=cursor.getString(cursor.getColumnIndex("pow_algorithm"));
			coin.ecdsa_curve=cursor.getString(cursor.getColumnIndex("ecdsa_curve"));
			coin.start_date=cursor.getLong(cursor.getColumnIndex("start_date"));
			coin.updatetime=cursor.getLong(cursor.getColumnIndex("updatetime"));				
			return coin;
		}
		return null;
	}
	
	public void clearAllCoins(){
		List<CoinClass> coinList=getScrollData(0,256);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		for (CoinClass coin:coinList){
			dbOpenHelper.clear(db, coin.coinType);
			dbOpenHelper.createCoinTable(db, coin.coinType);
		}
	}
	
	public List<CoinClass> getScrollData(Integer offset, Integer maxResult){
		List<CoinClass> coinList = new ArrayList<CoinClass>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from cbd order by id limit ?,?",
				new String[]{offset.toString(), maxResult.toString()});
		while(cursor.moveToNext()){
			CoinClass coin=new CoinClass();
			coin.id=cursor.getInt(cursor.getColumnIndex("id"));
			coin.coinType=cursor.getString(cursor.getColumnIndex("cointype"));;
			coin.coinName[0]=cursor.getString(cursor.getColumnIndex("coinnameen"));
			coin.coinName[1]=cursor.getString(cursor.getColumnIndex("coinnamecn")); 	 
			coin.digits=cursor.getInt(cursor.getColumnIndex("digits"));
			coin.digits_after_dot=cursor.getInt(cursor.getColumnIndex("digits_after_dot"));
			coin.confirmation_interval=cursor.getInt(cursor.getColumnIndex("confirmation_interval"));
			coin.min_fee=new BigInteger(cursor.getString(cursor.getColumnIndex("min_fee"))); 
			coin.max_tx_amount=new BigInteger(cursor.getString(cursor.getColumnIndex("max_tx_amount"))); 
			coin.pos=(cursor.getInt(cursor.getColumnIndex("digits"))==1)?true:false;
			coin.tx_header=SignerApplication.byteArrayToInt(Utils.hexStringToBytes(cursor.getString(cursor.getColumnIndex("tx_header"))),0);;
			coin.max_tx_size=cursor.getInt(cursor.getColumnIndex("max_tx_size"));			
			coin.address_header=(byte)cursor.getInt(cursor.getColumnIndex("address_header"));
			coin.multisig_address_header=(byte)cursor.getInt(cursor.getColumnIndex("multisig_address_header"));
			coin.max_amount=new BigInteger(cursor.getString(cursor.getColumnIndex("max_amount"))); 
			coin.halflife=cursor.getInt(cursor.getColumnIndex("halflife"));
			coin.block_reward=new BigInteger(cursor.getString(cursor.getColumnIndex("block_reward"))); 
			coin.pow_algorithm=cursor.getString(cursor.getColumnIndex("pow_algorithm"));
			coin.ecdsa_curve=cursor.getString(cursor.getColumnIndex("ecdsa_curve"));
			coin.start_date=cursor.getLong(cursor.getColumnIndex("start_date"));
			coin.updatetime=cursor.getLong(cursor.getColumnIndex("updatetime"));		
			coinList.add(coin);			
		}
		cursor.close();
		return coinList;
	}
		
	
	public long getCount() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from cbd", null);
		cursor.moveToFirst();
		return cursor.getLong(0);
	}

}
