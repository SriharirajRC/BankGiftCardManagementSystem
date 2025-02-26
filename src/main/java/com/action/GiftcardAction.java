package com.action;

import java.io.BufferedReader;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.Exceptions.InsufficientBalanceException;
import com.Exceptions.NotFound;
import com.Model.Giftcard;
import com.Model.TransactionLog;
import com.Repository.GiftcardRepository;
import com.Service.DepositRequestService;
import com.Service.GiftcardService;
import com.Service.TransactionLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class GiftcardAction extends ActionSupport implements ModelDriven<Giftcard>, ServletRequestAware {

	private Giftcard giftcard = new Giftcard();	
	private Map<String, Object> response = new HashMap<String, Object>();
    private HttpServletRequest request;
    private String username;
	private String id;
    private int pageno;
    private int pagesize;
    private String filter;
    private String orderby;
    
    
    // Get All Requests
    public String index() {
    	
    	if (pageno <= 0) pageno = 0;
        if (pagesize <= 0 || pagesize>=10) pagesize = 10;
        if (orderby == null || orderby.isEmpty()) {
        	orderby = "desc";
        }
        System.err.println("Pagination Info -> Page No: " + pageno + ", Page Size: " + pagesize +"_"+filter +"_"+orderby);
        if (username == null || username.isEmpty()) {
        	try{
            	response.put("message", "inside index");
            	response.put("giftcards",GiftcardService.getAllGiftcard(pageno,pagesize,orderby));
            }
            catch(Exception e) {
            	response.put("error", e.getMessage());
            }
            return SUCCESS;
        } else {
        	try{
            	response.put("message", "inside Specific");
            	response.put("giftcards",GiftcardService.getAllGiftcard(username, pageno,pagesize,orderby));
            }
            catch(Exception e) {
            	response.put("error", e.getMessage());
            }
            return SUCCESS;
        }
    }
         
    
    //create Request
    public String create() {
        System.err.println("Inside Create");
    	try {
            BufferedReader reader = request.getReader();
            ObjectMapper objectMapper = new ObjectMapper();
            giftcard = objectMapper.readValue(reader, Giftcard.class);
            System.out.println("Received Model: " + giftcard.toString());

            if (giftcard == null) {
                System.out.println("ERROR: Giftcard Model is not populated!");
                response.put("error", "Invalid JSON request");
                return ERROR;
            }
            String code = GiftcardService.generateGiftcard(giftcard);
            if(code != null) {
            	response.put("message", "Gift Card Created Successfully");
            	response.put("code",code);
            }
            else {
            	response.put("message","insufficentBalance");
            }

            return SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Error parsing JSON");
            return SUCCESS;
        }
    }

    
//    Update User
    public String update() {
	    try {
	    	BufferedReader reader = request.getReader();
	        ObjectMapper objectMapper = new ObjectMapper();
	        giftcard = objectMapper.readValue(reader, Giftcard.class);
	        System.out.println("Received Model: " + giftcard.toString());
	        if(GiftcardRepository.getGiftcardByCode(id) == null) {
		    	response.put("error", "Giftcard Not Found");
		        return SUCCESS;
	        }
	    	giftcard.setCode(id);
	        if(GiftcardService.rechargeGiftcard(giftcard)) {
	        	response.put("message", "Recharged");
    	    }
	   		else {
	   	  		response.put("message", "not recharged");
	   		 }      
	    }
	    catch(InsufficientBalanceException e) {
	    	response.put("error", e.getMessage());
	    	response.put("availamount", GiftcardRepository.getGiftcardByCode(giftcard.getCode()).getAmount());
	    }
	    catch (Exception e) {
			e.printStackTrace();
			response.put("error", e.getMessage());
			System.err.println(e.getMessage());
		}
        return SUCCESS;

    }
	      
   
    public Map<String, Object> getResponse() {
		return response;
	}

	public void setResponse(Map<String, Object> response) {
		this.response = response;
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

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	@Override
	public Giftcard getModel() {
		// TODO Auto-generated method stub
		return giftcard;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	

}
