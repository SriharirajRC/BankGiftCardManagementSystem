package com.Model;

import java.sql.Timestamp;

public class User {

	private String userid;
    private Long accountnumber;
    private String name;
    private String email;
    private String password;
    private Double availablebalance;
    private Double giftcardbalance;
    private Timestamp createdat;
    private Timestamp lastupdated;
    private Double bonus;
    private boolean isnewuser;
    private String role;
    
	public User(String userid, Long accountnumber, String name, String email, String password, Double availablebalance,
			Double giftcardbalance, Timestamp createdat, Timestamp lastupdated, Double bonus, boolean isnewuser,
			String role) {
		super();
		this.userid = userid;
		this.accountnumber = accountnumber;
		this.name = name;
		this.email = email;
		this.password = password;
		this.availablebalance = availablebalance;
		this.giftcardbalance = giftcardbalance;
		this.createdat = createdat;
		this.lastupdated = lastupdated;
		this.bonus = bonus;
		this.isnewuser = isnewuser;
		this.role = role;
	}
	public User() {
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Long getAccountnumber() {
		return accountnumber;
	}
	public void setAccountnumber(Long accountnumber) {
		this.accountnumber = accountnumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Double getAvailablebalance() {
		return availablebalance;
	}
	public void setAvailablebalance(Double availablebalance) {
		this.availablebalance = availablebalance;
	}
	public Double getGiftcardbalance() {
		return giftcardbalance;
	}
	public void setGiftcardbalance(Double giftcardbalance) {
		this.giftcardbalance = giftcardbalance;
	}
	public Timestamp getCreatedat() {
		return createdat;
	}
	public void setCreatedat(Timestamp createdat) {
		this.createdat = createdat;
	}
	public Timestamp getLastupdated() {
		return lastupdated;
	}
	public void setLastupdated(Timestamp lastupdated) {
		this.lastupdated = lastupdated;
	}
	public Double getBonus() {
		return bonus;
	}
	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
	public boolean isIsnewuser() {
		return isnewuser;
	}
	public void setIsnewuser(boolean isnewuser) {
		this.isnewuser = isnewuser;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "User [userid=" + userid + ", accountnumber=" + accountnumber + ", name=" + name + ", email=" + email
				+ ", password=" + password + ", availablebalance=" + availablebalance + ", giftcardbalance="
				+ giftcardbalance + ", createdat=" + createdat + ", lastupdated=" + lastupdated + ", bonus=" + bonus
				+ ", isnewuser=" + isnewuser + ", role=" + role + "]";
	}
	
}
