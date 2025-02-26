package com.Model;

import java.sql.Timestamp;

public class DepositRequest {

	private int id;
    private String userid;
    private double requestedamount;
    private Timestamp requesteddate;
    private String status;
    private Timestamp responsedate;
    
    public DepositRequest() {
    	
    }
    
	@Override
	public String toString() {
		return "DepositRequest [id=" + id + ", userid=" + userid + ", requestedamount=" + requestedamount
				+ ", requesteddate=" + requesteddate + ", status=" + status + ", responsedate=" + responsedate + "]";
	}

	public DepositRequest(int id, String userid, double requestedamount, Timestamp requesteddate, String status,
			Timestamp responsedate) {
		super();
		this.id = id;
		this.userid = userid;
		this.requestedamount = requestedamount;
		this.requesteddate = requesteddate;
		this.status = status;
		this.responsedate = responsedate;
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
	public double getRequestedamount() {
		return requestedamount;
	}
	public void setRequestedamount(double requestedamount) {
		this.requestedamount = requestedamount;
	}
	public Timestamp getRequesteddate() {
		return requesteddate;
	}
	public void setRequesteddate(Timestamp requesteddate) {
		this.requesteddate = requesteddate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getResponsedate() {
		return responsedate;
	}
	public void setResponsedate(Timestamp responsedate) {
		this.responsedate = responsedate;
	}
    
    
    
}
