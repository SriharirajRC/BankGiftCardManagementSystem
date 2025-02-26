package com.Model;

import java.sql.Timestamp;

public class Giftcard {

    private String code;
    private String pin;
    private double amount;
    private String ownerofgiftcard;
    private Timestamp generatedtime;
    
    public Giftcard() {
    	
    }
    
    
	@Override
	public String toString() {
		return "Giftcard [code=" + code + ", pin=" + pin + ", amount=" + amount + ", ownerofgiftcard=" + ownerofgiftcard
				+ ", generatedtime=" + generatedtime + "]";
	}


	public Giftcard(String code, String pin, double amount, String ownerofgiftcard) {
		super();
		this.code = code;
		this.pin = pin;
		this.amount = amount;
		this.ownerofgiftcard = ownerofgiftcard;
	}


	public Giftcard(String code, String pin, double amount, String ownerofgiftcard, Timestamp generatedtime) {
		super();
		this.code = code;
		this.pin = pin;
		this.amount = amount;
		this.ownerofgiftcard = ownerofgiftcard;
		this.generatedtime = generatedtime;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getOwnerofgiftcard() {
		return ownerofgiftcard;
	}
	public void setOwnerofgiftcard(String ownerofgiftcard) {
		this.ownerofgiftcard = ownerofgiftcard;
	}
	public Timestamp getGeneratedtime() {
		return generatedtime;
	}
	public void setGeneratedtime(Timestamp generatedtime) {
		this.generatedtime = generatedtime;
	}
    
}
