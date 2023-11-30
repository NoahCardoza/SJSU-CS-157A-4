package com.example.demo.filters;

import com.example.demo.Database;
import com.example.demo.Guard;
import com.example.demo.Util;
import com.example.demo.beans.Alert;
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
        var session = req.getSession(false);

        request.setAttribute("pathWithQueryString", Util.getPathWithQueryString(req));

        if (session != null) {
            // If there is an alert in the session, add it to the request and remove it from the session
            if (session.getAttribute("alert") != null) {
                request.setAttribute("alert", session.getAttribute("alert"));
                session.removeAttribute("alert");
            }

            Long userId = (Long)session.getAttribute("user_id");
            if (userId != null) {
                try {
                    Optional<User> user = UserDao.getInstance().get(userId);
                    if (user.isPresent()) {
                        if (user.get().isBanned()) {
                            session.invalidate();
                            Guard.redirectToLogin(
                                    (HttpServletRequest) request,
                                    (HttpServletResponse) response,
                                    new Alert("danger", "Your account has been banned.")
                            );
                            return;
                        }

                        request.setAttribute("user", user.get());
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
