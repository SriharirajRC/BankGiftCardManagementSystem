package com.action;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.Model.User;
import com.Service.UserService;
import com.Utils.Jwtutils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class LoginAction extends ActionSupport implements ModelDriven<User>, ServletRequestAware{
	
	private User user;

	private Map<String, Object> response = new HashMap<>();
    private HttpServletRequest request;
	
	public String create() {
		HttpServletResponse httpresponse = ServletActionContext.getResponse();
		System.out.println("HI ---");
		try {
			BufferedReader reader = request.getReader();
            ObjectMapper objectMapper = new ObjectMapper();
            user = objectMapper.readValue(reader, User.class);

            User authUser = UserService.authenticate(user);
    		if(authUser!= null) {
    			
    			String token = Jwtutils.generateToken(authUser.getUserid(), authUser.getRole());
    			
    			Cookie jwtCookie = new Cookie("jwtToken", token);
    			jwtCookie.setMaxAge(30 * 60);
    			jwtCookie.setPath("/");
    			
    			httpresponse.addHeader("Set-Cookie", "jwtToken=" + token + "; Path=/; Domain=.localhost; Max-Age=1800; Secure; SameSite=None");
    	        
    	        Cookie useridCookie = new Cookie("userid", authUser.getUserid());
    	        useridCookie.setMaxAge(30 * 60);
    	        useridCookie.setPath("/");


    	        httpresponse.addCookie(useridCookie);
    	        
    	        if(authUser.isIsnewuser()) {
    				Cookie isnewuser = new Cookie("newuser", "true");
    				isnewuser.setMaxAge(30 * 60);
    				isnewuser.setPath("/");
    				isnewuser.setDomain("localhost");

        	        httpresponse.addCookie(isnewuser);
        	        return SUCCESS;
    			}
    	        
    	        Cookie roleCook = new Cookie("role", authUser.getRole());
    	        roleCook.setMaxAge(30 * 60);
    	        roleCook.setPath("/");
    	        
    	        httpresponse.addCookie(roleCook);
    	        response.put("message", "loggedIn");
    		}
    		else {
    			response.put("message", "Invalid Credentials");
    		}
    		return SUCCESS;
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		response.put("message", "Invalid Credentials");
		return SUCCESS;
		
	}
	
	public String logout() {
	    HttpServletResponse httpresponse = ServletActionContext.getResponse();
	    
	    Cookie jwtCookie = new Cookie("jwtToken", "");
	    jwtCookie.setHttpOnly(true);
	    jwtCookie.setMaxAge(0);
	    jwtCookie.setPath("/");
	    
	    Cookie roleCoolie = new Cookie("role", "");
	    jwtCookie.setHttpOnly(true);
	    jwtCookie.setMaxAge(0);
	    jwtCookie.setPath("/");

	    httpresponse.addCookie(jwtCookie);
	    httpresponse.addCookie(roleCoolie);
	    
	    response.put("message", "Logout successful");

	    return SUCCESS;
	}


	
    public Map<String, Object> getResponse() {
		return response;
	}

	public void setResponse(Map<String, Object> response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public User getModel() {
		// TODO Auto-generated method stub
		return user;
	}
	
}
