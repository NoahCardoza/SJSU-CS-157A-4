package com.example.demo.filters;

import com.example.demo.beans.entities.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdministratorSessionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        User user = (User)request.getAttribute("user");

        if (user == null || !user.isAdministrator()) {
            ((HttpServletResponse)response).sendRedirect("/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
