package com.example.demo.filters;

import com.example.demo.Guard;
import com.example.demo.beans.Alert;
import com.example.demo.beans.entities.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdministratorSessionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        User user = Guard.requireAuthenticationWithMessage(
                (HttpServletRequest) request,
                (HttpServletResponse) response,
                "You must be logged in to access this page."
        );

        if (user == null) {
            return;
        }

        if (!user.isAdministrator()) {
            Guard.redirectToLogin(
                    (HttpServletRequest)request,
                    (HttpServletResponse)response,
                    new Alert("danger", "You must be an administrator to access this page.")
            );
            return;
        }

        chain.doFilter(request, response);
    }
}
