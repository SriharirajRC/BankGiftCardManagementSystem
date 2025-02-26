package com.Servlet;
import com.opensymphony.xwork2.ActionSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

public class ForwardToServletAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public String execute() throws Exception {
    	System.out.println("INSIDE FOREWARD");
        request.getRequestDispatcher("/customServlet").forward(request, response);
        return null; // Return null to stop further processing
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }
}
