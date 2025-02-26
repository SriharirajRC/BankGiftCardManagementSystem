package com.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.Model.TransactionLog;
import com.Service.DepositRequestService;
import com.Service.TransactionLogService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class TransactionLogAction extends ActionSupport implements ModelDriven<TransactionLog>, ServletRequestAware {

	private TransactionLog transactionLog = new TransactionLog();	
	private Map<String, Object> response = new HashMap<String, Object>();
    private HttpServletRequest request;
	private String id;
	private String username;
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
                	
        try{
           	response.put("message", "inside index");
           	response.put("transactions",TransactionLogService.getAllTransaction(username,pageno,pagesize,orderby));
        }
        catch(Exception e) {
           	response.put("error", e.getMessage());
        }
        
        return SUCCESS;
    }    
   
    public Map<String, Object> getResponse() {
		return response;
	}

	public void setResponse(Map<String, Object> response) {
		this.response = response;
	}
	

	public void setUsername(String username) {
		this.username = username;
	}

	public void setTransactionLog(TransactionLog transactionLog) {
		this.transactionLog = transactionLog;
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

	@Override
	public TransactionLog getModel() {
		// TODO Auto-generated method stub
		return transactionLog;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	

}
