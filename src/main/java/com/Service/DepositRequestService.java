package com.Service;

import java.util.List;

import com.Model.DepositRequest;
import com.Model.Pagination;
import com.Model.TransactionLog;
import com.Repository.DepositRequestRepository;
import com.Utils.PaginationFun4ty;

public class DepositRequestService {

	// All user request 
	public static Pagination getAllDepositRequests(int pageno, int pagesize, String filter, String sortby) throws Exception{
		
		return PaginationFun4ty.getPage(
				pageno, 
				pagesize, 
				DepositRequestRepository.getAllRequest(pagesize , pagesize * pageno, filter,sortby), 
				DepositRequestRepository.countofrequest(filter));
		
	}
	
	//Specific User request
	public static Pagination getAllDepositRequests(String userid, int pageno, int pagesize, String filter, String sortby) throws Exception{
		
		return PaginationFun4ty.getPage(
				pageno, 
				pagesize, 
				DepositRequestRepository.getAllRequest(userid, pagesize , pagesize * pageno, filter,sortby), 
				DepositRequestRepository.countofrequest(userid,filter));
		
	}

	public static boolean updateRequestStatus(int id, String status) {
    	DepositRequest request = DepositRequestRepository.getRequestById(id);

		if(status.equals("approve")
				&& TransactionLogService.logTransaction(new TransactionLog(1,request.getUserid(),request.getRequestedamount(),"Approved"))) {
			return DepositRequestRepository.acceptRequestStatus(id,request);
		}
		else
			return DepositRequestRepository.rejectRequest(id);
	}

	public static boolean createDepsoitrequest(DepositRequest depositrequest) {
		return DepositRequestRepository.createRequest(depositrequest);
	}

	
	
}
