package com.Interceptor;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;


public class RBAInterceptor extends AbstractInterceptor{
	
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
        String actionName = invocation.getProxy().getActionName();
        
        System.err.println("--- " + actionName + " intercepted by RBA...");
        
        if (isExcludedAction(actionName)) {
            System.err.println("--- " + actionName + " - Excluded");
            return invocation.invoke();
        }
        String userRole = (String) request.getAttribute("role");
        String loggedInUserId = (String) request.getAttribute("userid");
        String method = request.getMethod();

        
        if (userRole == null) {
            System.out.println("--- " + actionName + " - No role found, unauthorized");
            return "unauthorized";
        }
        
        if (isAuthorized(actionName, userRole)) {
        	System.out.println("--- " + actionName + " - Authorized as " + userRole);
            return invocation.invoke();
        } else {
            System.out.println("--- " + actionName + " - Unauthorized for role: " + userRole);
            return "unauthorized";
        }
        

    }
        
    private boolean isAuthorized(String actionName, String role) {
        List<String> managerActions = Arrays.asList("createuser", "viewusers", "viewallrequests", "viewallgiftcards", "viewalltransactions", "removeuser");
        List<String> userActions = Arrays.asList("requestmoney", "viewmyrequests", "generategiftcard", "rechargegiftcards", "convertbonus", "viewmytransactions", "viewmygiftcards", "changepassword");

        if ("manager".equals(role) && userActions.contains(actionName)) {
            return false;
        } else if ("user".equals(role) && managerActions.contains(actionName)) {
            return false;
        }
        return true;
    }
    
    private boolean isExcludedAction(String actionName) {
        return "login".equals(actionName) || "auth".equals(actionName) || "".equals(actionName);
    }
    
//    private boolean isUserAllowed(String method, String loggedInUserId, String requestedUserId) {
//        List<String> userAllowedActions = Arrays.asList("get", "put");
//
//        return userAllowedActions.contains(method) && loggedInUserId.equals(requestedUserId);
//    }

}




