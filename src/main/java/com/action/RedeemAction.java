package com.action;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.Exceptions.InsufficientBalanceException;
import com.Model.Giftcard;
import com.Repository.GiftcardRepository;
import com.Service.GiftcardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class RedeemAction extends ActionSupport implements ModelDriven<Giftcard>, ServletRequestAware{
	private Giftcard giftcard = new Giftcard();	
	private Map<String, Object> response = new HashMap<String, Object>();
    private HttpServletRequest request;         
    private String id;
    
	public void setId(String id) {
		this.id = id;
	}

	//  Redeem
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
	       	if(GiftcardService.redeemGiftcard(giftcard)) {
	   			response.put("message ", "redeemed");
	   	    }
     		else {
     			response.put("message ", "notredeemed");
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
