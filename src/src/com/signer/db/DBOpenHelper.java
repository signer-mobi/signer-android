package com.signer.db;

import java.math.BigInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "signer.db"; //��ݿ����
	private static final int DATABASEVERSION = 2;//��ݿ�汾
	String createReceive = "receive (id integer primary key autoincrement, timestamp long, transactionid char(32), trx_index int, no integer, value long," +
			"receivestatus integer, spent integer, spent_transaction_id varchar(32), spent_no integer, spentstatus integer)";
	String createSend="send (id integer primary key autoincrement, timestamp long, transactionid varchar(32), target_address string, value long, " +
			" change long, fee long, status integer)";
	public DBOpenHelper(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE cbd (id integer primary key autoincrement, cointype char(3), coinnameen varchar(32), coinnamecn varchar(32), digits integer, digits_after_dot integer,"+
				" confirmation_interval integer, min_fee varchar(32), max_tx_amount varchar(32), pos integer, tx_header char(4), max_tx_size int, address_header int, multisig_address_header int,"+
				" max_amount varchar(32), halflife integer, block_reward varchar(32), pow_algorithm varchar(32), ecdsa_curve varchar(32),"+
				" start_date long, updatetime long)");
		/*
		db.execSQL("CREATE TABLE receive (id integer primary key autoincrement, timestamp long, transactionid varchar(32),  trx_index int,no integer, value long, " +
				"receivestatus boolean, spent boolean, spent_transaction_id varchar(50), spent_no integer, spentstatus boolean)");//ִ���и�ĵ�sql���
		db.execSQL("CREATE TABLE send (id integer primary key autoincrement, timestamp long, transactionid varchar(32), target_address string, value long, " +
				" change long, fee long, status integer)");//ִ���и�ĵ�sql���
		*/
		
			
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS person");
		onCreate(db);
	}
	/*delete the receive and send table of the coinType*/
	public void clear(SQLiteDatabase db,String coinType){
		db.execSQL("DROP TABLE IF EXISTS "+coinType.toLowerCase()+"receive");
		db.execSQL("DROP TABLE IF EXISTS "+coinType.toLowerCase()+"send");
		//onCreate(db);
	}
	public void createCoinTable(SQLiteDatabase db,String coinType){
		db.execSQL("CREATE TABLE "+coinType.toLowerCase()+createReceive);
		db.execSQL("CREATE TABLE "+coinType.toLowerCase()+createSend);
	}

}
