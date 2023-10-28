package com.example.demo.filters;

import com.example.demo.Util;
import com.example.demo.beans.entities.User;
import com.example.demo.daos.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class UserSessionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        var session = req.getSession();

        if (session != null) {
            Long userId = (Long)session.getAttribute("user_id");
            if (userId != null) {
                try {
                    Optional<User> user = UserDao.getInstance().get(userId);
                    user.ifPresent(value -> request.setAttribute("user", value));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
