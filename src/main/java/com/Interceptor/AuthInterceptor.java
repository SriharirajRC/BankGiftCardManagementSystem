package com.Interceptor;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import com.Utils.Jwtutils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import io.jsonwebtoken.Claims;

public class AuthInterceptor extends AbstractInterceptor {
	
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        String actionName = invocation.getProxy().getActionName();
        
        System.err.println("--- " + actionName + " intercepted...");

        if (isExcludedAction(actionName,request)) {
            System.err.println("--- " + actionName + " - Excluded");
            return invocation.invoke();
        }

        String token = getTokenFromCookies(request);

        if (token != null && Jwtutils.validateToken(token)) {
            Claims claims = Jwtutils.extractAllClaims(token);
            if (claims != null) {
            	String userRole = claims.get("role", String.class);
                String userId = claims.get("userid", String.class);

                request.setAttribute("role", userRole);
                request.setAttribute("useriId", userId); 
                return invocation.invoke();
            }
        }

        System.out.println("--- " + actionName + " - Authentication failed");
        return "unauthorized";
    }

    private boolean isExcludedAction(String actionName, HttpServletRequest request) {
        return "login".equals(actionName) || "auth".equals(actionName) || "".equals(actionName);
    }

    private String getTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
