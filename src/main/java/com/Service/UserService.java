package com.Service;

import java.util.List;

import com.Exceptions.InsufficientBalanceException;
import com.Model.Pagination;
import com.Model.TransactionLog;
import com.Model.User;
import com.Model.UserModel;
import com.Repository.UserRepository;
import com.Utils.PaginationFun4ty;

public class UserService {
	
	public static User authenticate(User user) {
		User existingUser = UserRepository.getUserByUserid(user.getUserid());
		if(existingUser == null) {
			return null;
		}
		if(existingUser.getUserid().equals(user.getUserid()) 
				&& existingUser.getPassword().equals(user.getPassword())) {
			return existingUser;
		}	
		return null;
				
	}

	public static boolean createUser(User usermodel) throws Exception{
		
		if(UserRepository.getUserByUserid(usermodel.getUserid()) != null) {
			throw new Exception("Userid Already Exists");
		}
		
		if(UserRepository.createUser(usermodel)) {
			return true;
		}
		return false;
	}

	public static Pagination getAllUsers(int pageno,int pagesize,String orderby,String sortby) {
		
		int totalelements = UserRepository.getCount();
		return PaginationFun4ty.getPage(
					pageno,
					pagesize,
					UserRepository.getAllUsers(pagesize, pageno * pagesize),
					totalelements);
			
	}

	public static User getUser(String userid) {
		
		return UserRepository.getUserByUserid(userid);
		
	}

	public static boolean deleteUser(String userid) {
		if(UserRepository.getUserByUserid(userid)==null) {
			return false;
		}
		return UserRepository.deleteUser(userid);
	}
	
	
	public static boolean updateUser(String userid,User updateUser) throws Exception{

        User user = UserRepository.getUserByUserid(userid);

        if (user == null) {
            return false;
        }

        if (updateUser.getName() != null) {
            user.setName(updateUser.getName());
        }
        if (updateUser.getEmail() != null) {
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getPassword() != null) {
        	UserRepository.markAsOldUser(userid);
            user.setPassword(updateUser.getPassword());
        }
        if (updateUser.getBonus() != null) {
        	if(UserRepository.getUserByUserid(userid).getBonus() < updateUser.getBonus())
        		throw new InsufficientBalanceException("Insufficient Bonus");
        	return UserRepository.convertBonus(userid,updateUser.getBonus()) && TransactionLogService.logTransaction(new TransactionLog(
        			3,userid,updateUser.getBonus(),"Bonus Converted"));
        }
 
        return UserRepository.updateUser(user);
    }
	
}
