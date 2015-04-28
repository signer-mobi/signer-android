package com.signer.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



public class ReceiveService {
	private DBOpenHelper dbOpenHelper;
	
	public ReceiveService(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}


	public void save(String coinType,Receive receive){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into "+coinType.toLowerCase()+"receive (timestamp, transactionid,trx_index, no, value, receivestatus, spent, spent_transaction_id, spent_no, spentstatus ) values(?,?,?,?,?,?,?,?,?,?)",
				new Object[]{receive.getTimestamp(), receive.getTransactionid(), receive.getTx_index(),receive.getNo(), receive.getValue(), receive.getReceivestatus(), receive.isSpent(),
								receive.getSpent_transaction_id(), receive.getSpent_no(), receive.getSpentstatus()});
		
	}
	
	public void update(String coinType,Receive receive){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update "+coinType.toLowerCase()+"receive set timestamp = ?, transactionid = ?, trx_index=?,no = ?, value = ?, receivestatus = ?, spent = ?, spent_transaction_id = ?, spent_no = ?, spentstatus= ?   where id=?", 
				new Object[]{receive.getTimestamp(), receive.getTransactionid(), receive.getTx_index(),receive.getNo(), receive.getValue(), receive.getReceivestatus(), receive.isSpent(),
				receive.getSpent_transaction_id(), receive.getSpent_no(), receive.getSpentstatus(), receive.getId()});
	}
	
	public void delete(String coinType,Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from "+coinType.toLowerCase()+"receive where id=?", new Object[]{id.toString()});
	}
	
	public Receive find(String coinType,Integer id){		
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+coinType.toLowerCase()+"receive where id=?", new String[]{id.toString()});
		if(cursor.moveToFirst()){
			
			    int id2 = cursor.getInt(cursor.getColumnIndex("id"));
				
				long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
				
				String transactionid = cursor.getString(cursor.getColumnIndex("transactionid"));
				 int trx_index = cursor.getInt(cursor.getColumnIndex("trx_index"));
				 int no = cursor.getInt(cursor.getColumnIndex("no"));
				 long value = cursor.getLong(cursor.getColumnIndex("value"));
				 int receivestatus = cursor.getInt(cursor.getColumnIndex("receivestatus"));
				 int spent = cursor.getInt(cursor.getColumnIndex("spent"));
				 String spent_transaction_id = cursor.getString(cursor.getColumnIndex("spent_transaction_id"));
				 int spent_no = cursor.getInt(cursor.getColumnIndex("spent_no"));
				 int spentstatus = cursor.getInt(cursor.getColumnIndex("spentstatus"));
			
			
			
			
			Receive receive = new Receive(id, timestamp, transactionid,trx_index, no, value, receivestatus, spent, spent_transaction_id, spent_no, spentstatus);
			
			return receive;
		}
		return null;
	}
	public Receive findById(String coinType,String trxid){
		
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+coinType.toLowerCase()+"receive where transactionid=?", new String[]{trxid});
		if(cursor.moveToFirst()){
			
			    int id = cursor.getInt(cursor.getColumnIndex("id"));
				
				long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
				
				String transactionid = cursor.getString(cursor.getColumnIndex("transactionid"));
				 int trx_index = cursor.getInt(cursor.getColumnIndex("trx_index"));
				 int no = cursor.getInt(cursor.getColumnIndex("no"));
				 long value = cursor.getLong(cursor.getColumnIndex("value"));
				 int receivestatus = cursor.getInt(cursor.getColumnIndex("receivestatus"));
				 int spent = cursor.getInt(cursor.getColumnIndex("spent"));
				 String spent_transaction_id = cursor.getString(cursor.getColumnIndex("spent_transaction_id"));
				 int spent_no = cursor.getInt(cursor.getColumnIndex("spent_no"));
				 int spentstatus = cursor.getInt(cursor.getColumnIndex("spentstatus"));
			
			
			
			
			Receive receive = new Receive(id, timestamp, transactionid,trx_index, no, value, receivestatus, spent, spent_transaction_id, spent_no, spentstatus);
			
			return receive;
		}
		return null;
	}
	public Receive findBySpentId(String coinType,String trxid){
		//���ֻ����ݽ��ж�ȡ������ʹ�ô˷���
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+coinType.toLowerCase()+"receive where spent_transaction_id=?", new String[]{trxid});
		if(cursor.moveToFirst()){			
			    int id = cursor.getInt(cursor.getColumnIndex("id"));				
				long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));				
				String transactionid = cursor.getString(cursor.getColumnIndex("transactionid"));
				 int trx_index = cursor.getInt(cursor.getColumnIndex("trx_index"));
				 int no = cursor.getInt(cursor.getColumnIndex("no"));
				 long value = cursor.getLong(cursor.getColumnIndex("value"));
				 int receivestatus = cursor.getInt(cursor.getColumnIndex("receivestatus"));
				 int spent = cursor.getInt(cursor.getColumnIndex("spent"));
				 String spent_transaction_id = cursor.getString(cursor.getColumnIndex("spent_transaction_id"));
				 int spent_no = cursor.getInt(cursor.getColumnIndex("spent_no"));
				 int spentstatus = cursor.getInt(cursor.getColumnIndex("spentstatus"));			
			
			
			
			Receive receive = new Receive(id, timestamp, transactionid,trx_index, no, value, receivestatus, spent, spent_transaction_id, spent_no, spentstatus);
			
			return receive;
		}
		return null;
	}
	public Receive findByIndex(String coinType,int trxIndex){
		//���ֻ����ݽ��ж�ȡ������ʹ�ô˷���
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+coinType.toLowerCase()+"receive where trx_index=?", new String[]{String.valueOf(trxIndex)});
		if(cursor.moveToFirst()){
			
			    int id = cursor.getInt(cursor.getColumnIndex("id"));
				
				long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
				
				String transactionid = cursor.getString(cursor.getColumnIndex("transactionid"));
				 int trx_index = cursor.getInt(cursor.getColumnIndex("trx_index"));
				 int no = cursor.getInt(cursor.getColumnIndex("no"));
				 long value = cursor.getLong(cursor.getColumnIndex("value"));
				 int receivestatus = cursor.getInt(cursor.getColumnIndex("receivestatus"));
				 int spent = cursor.getInt(cursor.getColumnIndex("spent"));
				 String spent_transaction_id = cursor.getString(cursor.getColumnIndex("spent_transaction_id"));
				 int spent_no = cursor.getInt(cursor.getColumnIndex("spent_no"));
				 int spentstatus = cursor.getInt(cursor.getColumnIndex("spentstatus"));
			
			
			
			
			Receive receive = new Receive(id, timestamp, transactionid,trx_index, no, value, receivestatus, spent, spent_transaction_id, spent_no, spentstatus);
			
			return receive;
		}
		return null;
	}
	
	public List<Receive> getScrollData(String coinType,Integer offset, Integer maxResult){
		List<Receive> receives = new ArrayList<Receive>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+coinType.toLowerCase()+"receive order by timestamp desc limit ?,?",
				new String[]{offset.toString(), maxResult.toString()});
		while(cursor.moveToNext()){
			 int id = cursor.getInt(cursor.getColumnIndex("id"));
				
				 long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
				 String transactionid = cursor.getString(cursor.getColumnIndex("transactionid"));
				 int trx_index = cursor.getInt(cursor.getColumnIndex("trx_index"));
				 int no = cursor.getInt(cursor.getColumnIndex("no"));
				 long value = cursor.getLong(cursor.getColumnIndex("value"));
				 int receivestatus = cursor.getInt(cursor.getColumnIndex("receivestatus"));
				 int spent = cursor.getInt(cursor.getColumnIndex("spent"));
				 String spent_transaction_id = cursor.getString(cursor.getColumnIndex("spent_transaction_id"));
				 int spent_no = cursor.getInt(cursor.getColumnIndex("spent_no"));
				 int spentstatus = cursor.getInt(cursor.getColumnIndex("spentstatus"));
				 Receive receive = new Receive(id, timestamp, transactionid,trx_index, no, value, receivestatus, spent, spent_transaction_id, spent_no, spentstatus);
			receives.add(receive);
			
		}
		cursor.close();
		return receives;
	}
		
	public List<Receive> getUnspentChecks(String coinType,Integer offset, Integer maxResult){
		List<Receive> receives = new ArrayList<Receive>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+coinType.toLowerCase()+"receive where spent=0 order by value desc limit ?,?",
				new String[]{offset.toString(), maxResult.toString()});
		//cursor.moveToFirst();
		//cursor.moveToPrevious();
		while(cursor.moveToNext()){
			 int id = cursor.getInt(cursor.getColumnIndex("id"));
				
				 long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
				 String transactionid = cursor.getString(cursor.getColumnIndex("transactionid"));
				 int trx_index = cursor.getInt(cursor.getColumnIndex("trx_index"));
				 int no = cursor.getInt(cursor.getColumnIndex("no"));
				 long value = cursor.getLong(cursor.getColumnIndex("value"));
				 int receivestatus = cursor.getInt(cursor.getColumnIndex("receivestatus"));
				 int spent = cursor.getInt(cursor.getColumnIndex("spent"));
				 String spent_transaction_id = cursor.getString(cursor.getColumnIndex("spent_transaction_id"));
				 int spent_no = cursor.getInt(cursor.getColumnIndex("spent_no"));
				 int spentstatus = cursor.getInt(cursor.getColumnIndex("spentstatus"));
				 Receive receive = new Receive(id, timestamp, transactionid,trx_index, no, value, receivestatus, spent, spent_transaction_id, spent_no, spentstatus);
			receives.add(receive);
			
		}
		Log.println(3,"HB01m","unspentchecks:"+receives.size());
		cursor.close();
		return receives;
	}
	public List<Receive> getCandidateChecks(String coinType,Integer offset, Integer maxResult){
		List<Receive> receives = new ArrayList<Receive>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+coinType.toLowerCase()+"receive where spent=0 and receivestatus=1 order by value desc limit ?,?",
				new String[]{offset.toString(), maxResult.toString()});
		//cursor.moveToFirst();
		//cursor.moveToPrevious();
		while(cursor.moveToNext()){
			 int id = cursor.getInt(cursor.getColumnIndex("id"));
				
				 long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
				 String transactionid = cursor.getString(cursor.getColumnIndex("transactionid"));
				 int trx_index = cursor.getInt(cursor.getColumnIndex("trx_index"));
				 int no = cursor.getInt(cursor.getColumnIndex("no"));
				 long value = cursor.getLong(cursor.getColumnIndex("value"));
				 int receivestatus = cursor.getInt(cursor.getColumnIndex("receivestatus"));
				 int spent = cursor.getInt(cursor.getColumnIndex("spent"));
				 String spent_transaction_id = cursor.getString(cursor.getColumnIndex("spent_transaction_id"));
				 int spent_no = cursor.getInt(cursor.getColumnIndex("spent_no"));
				 int spentstatus = cursor.getInt(cursor.getColumnIndex("spentstatus"));
				 Receive receive = new Receive(id, timestamp, transactionid,trx_index, no, value, receivestatus, spent, spent_transaction_id, spent_no, spentstatus);
			receives.add(receive);
			
		}
		cursor.close();
		return receives;
	}
	public List<Receive> getPendingSpentChecks(String coinType,Integer offset, Integer maxResult){
		List<Receive> receives = new ArrayList<Receive>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+coinType.toLowerCase()+"receive where spent=1 and spentstatus=0 limit ?,?",
				new String[]{offset.toString(), maxResult.toString()});
		while(cursor.moveToNext()){
			 int id = cursor.getInt(cursor.getColumnIndex("id"));
				
				 long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
				 String transactionid = cursor.getString(cursor.getColumnIndex("transactionid"));
				 int trx_index = cursor.getInt(cursor.getColumnIndex("trx_index"));
				 int no = cursor.getInt(cursor.getColumnIndex("no"));
				 long value = cursor.getLong(cursor.getColumnIndex("value"));
				 int receivestatus = cursor.getInt(cursor.getColumnIndex("receivestatus"));
				 int spent = cursor.getInt(cursor.getColumnIndex("spent"));
				 String spent_transaction_id = cursor.getString(cursor.getColumnIndex("spent_transaction_id"));
				 int spent_no = cursor.getInt(cursor.getColumnIndex("spent_no"));
				 int spentstatus = cursor.getInt(cursor.getColumnIndex("spentstatus"));
				 Receive receive = new Receive(id, timestamp, transactionid,trx_index, no, value, receivestatus, spent, spent_transaction_id, spent_no, spentstatus);
			receives.add(receive);
			
		}
		cursor.close();
		return receives;
	}
	public List<Receive> getPendingChecks(String coinType,Integer offset, Integer maxResult){
		List<Receive> receives = new ArrayList<Receive>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+coinType.toLowerCase()+"receive where receivestatus=0 limit ?,?",
				new String[]{offset.toString(), maxResult.toString()});
		while(cursor.moveToNext()){
			 int id = cursor.getInt(cursor.getColumnIndex("id"));
				
				 long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
				 String transactionid = cursor.getString(cursor.getColumnIndex("transactionid"));
				 int trx_index = cursor.getInt(cursor.getColumnIndex("trx_index"));
				 int no = cursor.getInt(cursor.getColumnIndex("no"));
				 long value = cursor.getLong(cursor.getColumnIndex("value"));
				 int receivestatus = cursor.getInt(cursor.getColumnIndex("receivestatus"));
				 int spent = cursor.getInt(cursor.getColumnIndex("spent"));
				 String spent_transaction_id = cursor.getString(cursor.getColumnIndex("spent_transaction_id"));
				 int spent_no = cursor.getInt(cursor.getColumnIndex("spent_no"));
				 int spentstatus = cursor.getInt(cursor.getColumnIndex("spentstatus"));
				 Receive receive = new Receive(id, timestamp, transactionid,trx_index, no, value, receivestatus, spent, spent_transaction_id, spent_no, spentstatus);
			receives.add(receive);
			
		}
		cursor.close();
		return receives;
	}
	public long getCount(String coinType) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from "+coinType.toLowerCase()+"receive", null);
		cursor.moveToFirst();
		return cursor.getLong(0);
	}
}
