package com.Model;

public class UserModel {
	private String userid;
    private String name;
    private String email;
    
    public UserModel() {
    	
    }
    
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@Override
	public String toString() {
		return "UserModel [userid=" + userid + ", name=" + name + ", email=" + email + "]";
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
    
}
