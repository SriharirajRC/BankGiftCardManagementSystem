package com.Service;

import com.Model.Pagination;
import com.Model.TransactionLog;
import com.Repository.TransactionLogRepository;
import com.Utils.PaginationFun4ty;

public class TransactionLogService {

	public static Pagination getAllTransaction(String username ,int pageno, int pagesize, String sortby){
		
		return PaginationFun4ty.getPage(
				pageno, 
				pagesize, 
				TransactionLogRepository.getAllTransaction(username, pagesize , pagesize * pageno,sortby),
				TransactionLogRepository.countoftransaction(username));
	}
	
	public static boolean logTransaction(TransactionLog transaction) {
		
		return TransactionLogRepository.logthetransaction(transaction);
		
		
	}
}
