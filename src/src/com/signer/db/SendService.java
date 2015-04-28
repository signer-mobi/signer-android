package com.signer.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



public class SendService {
	private DBOpenHelper dbOpenHelper;
	
	public SendService(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}


	public void save(String coinType,Send send){
		
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();		
		db.execSQL("insert into "+coinType.toLowerCase()+"send (timestamp, transactionid, target_address, value, change, fee, status ) values(?,?,?,?,?,?,?)",
				new Object[]{send.getTimestamp(), send.getTransactionid(),send.getTarget_address(), send.getValue(), send.getChange(),send.getFee(),
							 send.getStatus()});
	}
	
	public void update(String coinType,Send send){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update "+coinType.toLowerCase()+"send set timestamp = ?, transactionid = ?,target_address = ?, value = ?, change = ?, fee = ?, status = ? where id=?", 
				new Object[]{send.getTimestamp(), send.getTransactionid(),send.getTarget_address(), send.getValue(), send.getChange(),send.getFee(),
				 send.getStatus(), send.getId()});
	}
	
	public void delete(String coinType,Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from "+coinType.toLowerCase()+"send where id=?", new Object[]{id.toString()});
	}
	
	public Send find(String coinType,Integer id){
		//���ֻ����ݽ��ж�ȡ������ʹ�ô˷���
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+coinType.toLowerCase()+"send where id=?", new String[]{id.toString()});
		if(cursor.moveToFirst()){
			
			    int id2 = cursor.getInt(cursor.getColumnIndex("id"));
				
				long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));				
				 String target_address = cursor.getString(cursor.getColumnIndex("target_address"));
				String transactionid = cursor.getString(cursor.getColumnIndex("transactionid"));
				 long value = cursor.getLong(cursor.getColumnIndex("value"));
				 long change = cursor.getLong(cursor.getColumnIndex("change"));
				 long fee = cursor.getLong(cursor.getColumnIndex("fee"));
				 int status = cursor.getInt(cursor.getColumnIndex("status"));
						
			
			Send send = new Send(id, timestamp, transactionid, target_address, value, change, fee, status);
			
			return send;
		}
		return null;
	}
	public Send findbyID(String coinType,String trxID){
		//���ֻ����ݽ��ж�ȡ������ʹ�ô˷���
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+coinType.toLowerCase()+"send where transactionid=?", new String[]{trxID});
		if(cursor.moveToFirst()){
			
			    int id = cursor.getInt(cursor.getColumnIndex("id"));
				
				long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));				
				 String target_address = cursor.getString(cursor.getColumnIndex("target_address"));
				String transactionid = cursor.getString(cursor.getColumnIndex("transactionid"));
				 long value = cursor.getLong(cursor.getColumnIndex("value"));
				 long change = cursor.getLong(cursor.getColumnIndex("change"));
				 long fee = cursor.getLong(cursor.getColumnIndex("fee"));
				 int status = cursor.getInt(cursor.getColumnIndex("status"));
						
			
			Send send = new Send(id, timestamp, transactionid, target_address, value, change, fee, status);
			
			return send;
		}
		return null;
	}
	
	
	public List<Send> getScrollData(String coinType,Integer offset, Integer maxResult){
		List<Send> sends = new ArrayList<Send>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+coinType.toLowerCase()+"send order by timestamp desc limit ?,?",
				new String[]{offset.toString(), maxResult.toString()});
		while(cursor.moveToNext()){
			 int id = cursor.getInt(cursor.getColumnIndex("id"));
				
				long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
				 String target_address = cursor.getString(cursor.getColumnIndex("target_address"));
				String transactionid = cursor.getString(cursor.getColumnIndex("transactionid"));
				 long value = cursor.getLong(cursor.getColumnIndex("value"));
				 long change = cursor.getLong(cursor.getColumnIndex("change"));
				 long fee = cursor.getLong(cursor.getColumnIndex("fee"));
				 int status = cursor.getInt(cursor.getColumnIndex("status"));
				 Send send = new Send(id, timestamp, transactionid, target_address, value, change, fee, status);
			sends.add(send);
			
		}
		cursor.close();
		return sends;
	}
	public List<Send> getPending(String coinType,Integer offset, Integer maxResult){
		List<Send> sends = new ArrayList<Send>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+coinType.toLowerCase()+"send where status=0 order by timestamp desc limit ?,?",
				new String[]{offset.toString(), maxResult.toString()});
		while(cursor.moveToNext()){
			 int id = cursor.getInt(cursor.getColumnIndex("id"));
				
				long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
				 String target_address = cursor.getString(cursor.getColumnIndex("target_address"));
				String transactionid = cursor.getString(cursor.getColumnIndex("transactionid"));
				 long value = cursor.getLong(cursor.getColumnIndex("value"));
				 long change = cursor.getLong(cursor.getColumnIndex("change"));
				 long fee = cursor.getLong(cursor.getColumnIndex("fee"));
				 int status = cursor.getInt(cursor.getColumnIndex("status"));
				 Send send = new Send(id, timestamp, transactionid, target_address, value, change, fee, status);
			sends.add(send);
			
		}
		cursor.close();
		return sends;
	}
		
	public long getCount(String coinType) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from "+coinType.toLowerCase()+"send", null);
		cursor.moveToFirst();
		return cursor.getLong(0);
	}
}
