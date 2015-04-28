package com.signer.db;

public class Receive {
	 int id;
	
	long timestamp;
	
	String trxid;
	int trx_index;
	 int no;
	 long value;
	 int receivestatus;
	 int spent;
	 String spentTrxId;
	 int spent_no;
	 int spentstatus;
	 public Receive(int id, long timestamp, String transactionid, int trx_index,int no,
			long value, int receivestatus, int spent,
			String spent_transaction_id, int spent_no, int spentstatus) {
			super();
			this.id = id;
			this.timestamp = timestamp;
			this.trxid = transactionid;
			this.trx_index=trx_index;
			this.no = no;
			this.value = value;
			this.receivestatus = receivestatus;
			this.spent = spent;
			this.spentTrxId = spent_transaction_id;
			this.spent_no = spent_no;
			this.spentstatus = spentstatus;
		}
	
	 public Receive() {
		 this.id =0;
			this.timestamp = 0;
			this.trxid = "";
			this.trx_index=0;
			this.no =0;
			this.value =0;
			this.receivestatus = 0;
			this.spent = 0;
			this.spentTrxId = "";
			this.spent_no = 0;
			this.spentstatus = 0;
	}

	public int getId() {
			return id;
		}
	public void setId(int id) {
		this.id = id;
	}
	 public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getTransactionid() {
		return trxid;
	}
	public void setTransactionid(String transactionid) {
		this.trxid = transactionid;
	}
	public int getTx_index() {
		return trx_index;
	}
	public void setTx_index(int trx_index) {
		this.trx_index = trx_index;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	public int getReceivestatus() {
		return receivestatus;
	}
	public void setReceivestatus(int receivestatus) {
		this.receivestatus = receivestatus;
	}
	public int isSpent() {
		return spent;
	}
	public void setSpent(int spent) {
		this.spent = spent;
	}
	public String getSpent_transaction_id() {
		return spentTrxId;
	}
	public void setSpent_transaction_id(String spent_transaction_id) {
		this.spentTrxId = spent_transaction_id;
	}
	public int getSpent_no() {
		return spent_no;
	}
	public void setSpent_no(int spent_no) {
		this.spent_no = spent_no;
	}
	public int getSpentstatus() {
		return spentstatus;
	}
	public void setSpentstatus(int spentstatus) {
		this.spentstatus = spentstatus;
	}
	
	

}
