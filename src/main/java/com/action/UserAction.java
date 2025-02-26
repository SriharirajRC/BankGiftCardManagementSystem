package com.action;

import com.Model.Pagination;
import com.Model.User;
import com.Model.UserModel;
import com.Service.UserService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAction extends ActionSupport implements ModelDriven<User>, ServletRequestAware {

    private User user = new User();
    private Map<String, Object> response = new HashMap<>();
    private HttpServletRequest request;
    private String id;
    private int pageno;
    private int pagesize;
    private String sortby;
    private String orderby;

	//Create User
    public String create() {
        try {
            BufferedReader reader = request.getReader();
            ObjectMapper objectMapper = new ObjectMapper();
            user = objectMapper.readValue(reader, User.class);
            System.out.println("Received UserModel: " + user);

            if (user.getUserid() == null) {
                System.out.println("ERROR: usermodel is not populated!");
                response.put("error", "Invalid JSON request");
                return SUCCESS;
            }
            if(UserService.createUser(user)) {
                response.put("user", user);
            }
            else {
            	response.put("message","Not Created");
            }

            return SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", e.getMessage());
            return SUCCESS;
        }
    }

    // Get All users
    public String index() {
    	
    	if (pageno <= 0) pageno = 0;
        if (pagesize <= 0 || pagesize>=10) pagesize = 10;
        
        System.err.println("Pagination Info -> Page No: " + pageno + ", Page Size: " + pagesize);
        response.put("user",UserService.getAllUsers(pageno,pagesize,orderby,sortby));
        return SUCCESS;
    }
    
    // Get User by userid
    public String show() {
        User user = UserService.getUser(id);
    	if(user != null) {
            response.put("users", user);
    	}
    	else {
            response.put("message", "usernotfound");
    	}
        return SUCCESS;
    }
    
    // Delete User by userid
    public String destroy() {
    	String message;
    	if(UserService.deleteUser(id)) {
    		message = "removed";
    	}
    	else {
    		message = "notremoved";
    	}
		response.put("message", message);
		return SUCCESS;
    }    
    
    // Update User
    public String update() {
    	BufferedReader reader;
		try {
			reader = request.getReader();
			ObjectMapper objectMapper = new ObjectMapper();
	        user = objectMapper.readValue(reader, User.class);
	        

	        if(UserService.updateUser(id,user)) {
		        response.put("message ", "updated");
	        }
	        else {
		        response.put("message ", "notupdated");

	        }
	        
	        
	    	return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
            response.put("error", e.getMessage());
            return SUCCESS;
		}
        
    }
    
    public void setId(String id) {
		this.id = id;
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
    
    public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public void setSortby(String sortby) {
		this.sortby = sortby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}
    
    @Override
    public User getModel() {
        return user;
    }
    

}
