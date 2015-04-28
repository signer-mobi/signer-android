package com.signer.db;

public class Send {
	 int id;

	long timestamp;
	 String transactionid;
	 String target_address;
	 long value;
	 long change; 
	 long fee;
	 int status;
	 public Send(){
		 this.id=0;
		 this.timestamp = 0;
			this.transactionid ="";
			this.target_address = "";
			this.value =0;
			this.change =0;
			this.fee = 0;
			this.status = 0;
	 }
	 public Send(int id, long timestamp, String transactionid, String target_address,
			long value, long change, long fee, int status) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.transactionid = transactionid;
		this.target_address = target_address;
		this.value = value;
		this.change = change;
		this.fee = fee;
		this.status = status;
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
		return transactionid;
	}
	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}
	public String getTarget_address() {
		return target_address;
	}
	public void setTarget_address(String target_address) {
		this.target_address = target_address;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	public long getChange() {
		return change;
	}
	public void setChange(long change) {
		this.change = change;
	}
	public long getFee() {
		return fee;
	}
	public void setFee(long fee) {
		this.fee = fee;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
	
