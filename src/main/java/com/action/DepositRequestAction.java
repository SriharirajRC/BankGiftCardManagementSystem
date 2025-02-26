package com.action;

import java.io.BufferedReader;

	import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.Model.DepositRequest;
import com.Service.DepositRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class DepositRequestAction extends ActionSupport implements ModelDriven<DepositRequest>, ServletRequestAware {

	private DepositRequest depositrequest = new DepositRequest();	
	private Map<String, Object> response = new HashMap<String, Object>();
    private HttpServletRequest request;
	private String id;
	private String username;
    private int pageno;
    private int pagesize;
    private String filter;
	private String orderby;
    
    //create Request
    public String create() {
        System.out.println("Inside Create");
        try {
            BufferedReader reader = request.getReader();
            ObjectMapper objectMapper = new ObjectMapper();
            depositrequest = objectMapper.readValue(reader, DepositRequest.class);
            System.out.println("Received Model: " + depositrequest.toString());

            if (depositrequest == null) {
                System.out.println("ERROR: Request Model is not populated!");
                response.put("error", "Invalid JSON request");
                return ERROR;
            }
            if(DepositRequestService.createDepsoitrequest(depositrequest)) {
            	response.put("message", "Request Submited Successfully");
            }
            else {
            	response.put("message","Not Submited");
            }

            return SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Error parsing JSON");
            return SUCCESS;
        }
    }

    // Get All Requests
    public String index() {
        
    	if (pageno <= 0) pageno = 0;
        if (pagesize <= 0 || pagesize>=10) pagesize = 10;
        if (filter == null || filter.isEmpty()) {
        	filter = "pending";
        }
        if (orderby == null || orderby.isEmpty()) {
        	orderby = "desc";
        }
        System.err.println("Pagination Info -> Page No: " + pageno + ", Page Size: " + pagesize +"_"+filter +"_"+orderby);
        
        if (username == null || username.isEmpty()) {
        	try{
            	response.put("user",DepositRequestService.getAllDepositRequests(pageno,pagesize,filter,orderby));
            }
            catch(Exception e) {
            	response.put("error", e.getMessage());
            }
        } else {
        	try{
            	response.put("user",DepositRequestService.getAllDepositRequests(username,pageno,pagesize,filter,orderby));
            }
            catch(Exception e) {
            	response.put("error", e.getMessage());
            }
        }
        
        return SUCCESS;
    }   
    
//  Update User
	  public String update() {
			try {
				BufferedReader reader = request.getReader();
	          ObjectMapper objectMapper = new ObjectMapper();
	          depositrequest = objectMapper.readValue(reader, DepositRequest.class);
	          System.out.println("Received Model: " + depositrequest.toString());
		        
	      if(DepositRequestService.updateRequestStatus(Integer.valueOf(id),depositrequest.getStatus())) {
		        	System.out.println("Received UserModel: " + depositrequest);
			        response.put("message ", "updated");
		        }
		        else {
			        response.put("message ", "notupdated");
	
		        }
		        
		    	return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
	          response.put("error", "Error parsing JSON");
	          return SUCCESS;
			}
	      
	  }

   
    public Map<String, Object> getResponse() {
		return response;
	}

	public void setResponse(Map<String, Object> response) {
		this.response = response;
	}

	public DepositRequest getDepositrequest() {
		return depositrequest;
	}

	

	public void setId(String id) {
		this.id = id;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}


	@Override
	public DepositRequest getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	

}
