package com.example.demo.filters;

import jakarta.servlet.*;

import java.io.IOException;

public class CharacterEncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        if (servletRequest.getContentType() != null && servletRequest.getContentType().startsWith("text/html")) {
            servletRequest.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("text/html; charset=UTF-8");
        }
    }

    @Override
    public void destroy() {}
}
