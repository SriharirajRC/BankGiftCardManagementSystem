package com.Service;

import com.Exceptions.InsufficientBalanceException;
import com.Model.Giftcard;
import com.Model.Pagination;
import com.Model.TransactionLog;
import com.Repository.GiftcardRepository;
import com.Repository.UserRepository;
import com.Utils.PaginationFun4ty;

public class GiftcardService {

	public static Pagination getAllGiftcard(int pageno, int pagesize, String orderby){
		return PaginationFun4ty.getPage(
				pageno, 
				pagesize, 
				GiftcardRepository.getAllGiftcard(pagesize,pagesize*pageno,orderby),
				GiftcardRepository.countofgiftcard());
	}

	public static String generateGiftcard(Giftcard giftcard) {
		String code = GiftcardRepository.generateGiftcard(giftcard);
		if(code!= null && TransactionLogService.logTransaction(new TransactionLog(2,giftcard.getOwnerofgiftcard(),
				giftcard.getAmount(),"Giftcard Created"))) {
			return code;
		}
		return null;
	}
	
	//specific user
	public static Pagination getAllGiftcard(String userid, int pageno, int pagesize, String orderby){
		return PaginationFun4ty.getPage(
				pageno, 
				pagesize, 
				GiftcardRepository.getAllGiftcard(userid,pagesize,pagesize*pageno,orderby),
				GiftcardRepository.countofgiftcard(userid));
	}

	public static boolean rechargeGiftcard(Giftcard giftcard) throws InsufficientBalanceException {
		if(giftcard.getAmount() > UserRepository.getUserByUserid(giftcard.getOwnerofgiftcard()).getAvailablebalance()) {
            throw new InsufficientBalanceException("Insufficient balance to create gift card.");
		}
		
		if(GiftcardRepository.rechargeGiftcard(giftcard) &&
				TransactionLogService.logTransaction(new TransactionLog(2,giftcard.getOwnerofgiftcard(),giftcard.getAmount(),"Giftcard Recharged"))) {
			return true;
		}
		return false;

	}

	public static boolean redeemGiftcard(Giftcard giftcard) throws Exception{
		
		if(giftcard.getAmount() > GiftcardRepository.getGiftcardByCode(giftcard.getCode()).getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance in gift card.");
		}
		
		if(!GiftcardRepository.getGiftcardByCode(giftcard.getCode()).getPin().equals(giftcard.getPin())) {
            throw new Exception("Invalid Pin");
		}
		giftcard.setOwnerofgiftcard(GiftcardRepository.getGiftcardByCode(giftcard.getCode()).getOwnerofgiftcard());
		
		if(GiftcardRepository.redeemGiftcard(giftcard) &&
				TransactionLogService.logTransaction(new TransactionLog(2,giftcard.getOwnerofgiftcard(),giftcard.getAmount(),"Giftcard Redeemed"))) {
			return true;
		}
		return false;

	}

}
