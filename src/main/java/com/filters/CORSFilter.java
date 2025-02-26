package com.filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

public class CORSFilter implements Filter {
    public void init(FilterConfig filterConfig) {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5500"); // Allow all origins
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        
//        httpResponse.setHeader("Access-Control-Max-Age", "3600"); 

        chain.doFilter(request, response);
    }

    public void destroy() {}
}
