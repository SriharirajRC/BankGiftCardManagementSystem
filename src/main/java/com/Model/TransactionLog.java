package com.Model;

import java.sql.Timestamp;

public class TransactionLog {

	private int id;
	private String userid;
	private double amount;
	private String eventname;
	private Timestamp eventdate;
	private String details;
	
	public TransactionLog() {
		
	}
	
	
	
	public TransactionLog(int id, String userid, double amount, String details) {
		super();
		this.id = id;
		this.userid = userid;
		this.amount = amount;
		this.details = details;
	}



	public TransactionLog(int id, String userid, double amount, String eventname, Timestamp eventdate, String details) {
		super();
		this.id = id;
		this.userid = userid;
		this.amount = amount;
		this.eventname = eventname;
		this.eventdate = eventdate;
		this.details = details;
	}
	public String getEventname() {
		return eventname;
	}
	public void setEventname(String eventname) {
		this.eventname = eventname;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public Timestamp getEventdate() {
		return eventdate;
	}
	public void setEventdate(Timestamp eventdate) {
		this.eventdate = eventdate;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}



	@Override
	public String toString() {
		return "TransactionLog [id=" + id + ", userid=" + userid + ", amount=" + amount + ", eventname=" + eventname
				+ ", eventdate=" + eventdate + ", details=" + details + "]";
	}
	
	
}
