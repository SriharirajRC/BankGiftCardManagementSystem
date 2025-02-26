package com.Servlet;

import java.io.IOException;
import javax.servlet.*;

public class ServletBypassFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        request.getRequestDispatcher("/customServlet").forward(request, response);
    }

    @Override
    public void destroy() {}
}
