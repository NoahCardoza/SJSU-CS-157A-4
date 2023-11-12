package com.example.demo.servlets.revisions;

import com.example.demo.Util;
import com.example.demo.Validation;
import com.example.demo.beans.Alert;
import com.example.demo.beans.entities.AmenityWithImage;
import com.example.demo.beans.entities.Location;
import com.example.demo.beans.entities.Revision;
import com.example.demo.beans.entities.User;
import com.example.demo.beans.forms.LocationForm;
import com.example.demo.daos.AmenityDao;
import com.example.demo.daos.LocationDao;
import com.example.demo.daos.RevisionDao;
import com.example.demo.daos.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "Revisions", value = "/revisions")
public class RevisionsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)  {
        doRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)  {
        doRequest(request, response);
    }

    public void doRequest(HttpServletRequest request, HttpServletResponse response) {
        String function = request.getParameter("f");

        if (function == null) {
            function = "index";
        }

        try {
            switch (function) {
                case "list" -> list(request, response);
                case "vote" -> vote(request, response);
                case "get" ->
                        request.getRequestDispatcher("/template/locations/delete-location.jsp").forward(request, response);
                default -> response.sendRedirect(request.getContextPath() + "/");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void vote(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        if (!request.getMethod().equals("POST")) {
            response.getWriter().println("Invalid method");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        HttpSession session = request.getSession(true);

        Long revisionId = Util.parseLongOrNull(request.getParameter("id"));

        if (revisionId == null) {
            session.setAttribute("alert", new Alert("danger", "Invalid revision id"));
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Optional<Revision> revision = RevisionDao.getInstance().get(revisionId);

        if (revision.isEmpty()) {
            session.setAttribute("alert", new Alert("danger", "Invalid revision id" ));
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        User user = (User) request.getAttribute("user");

        if (user == null) {
            session.setAttribute("alert", new Alert("danger", "You must be logged in to vote"));
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String method = request.getParameter("method");

        int vote = switch (method) {
            case "up" -> 1;
            case "down" -> -1;
            default -> 0;
        };

        if (vote == 0) {
            session.setAttribute("alert", new Alert("danger", "Invalid vote method"));
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        RevisionDao.getInstance().vote(revisionId, user.getId(), vote);


        session.setAttribute("alert", new Alert("success", "Vote successful!"));
        response.sendRedirect(request.getContextPath() + "/revisions/list?type=" + revision.get().getTableName() + "&id=" + revision.get().getPrimaryKey());
    }


    public void list(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String tableName = request.getParameter("type");
        Long primaryKey = Util.parseLongOrNull(request.getParameter("id"));

        if (tableName == null || primaryKey == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        List<Revision> revisions = RevisionDao.getInstance().getRevisionEdits(tableName, primaryKey);

        request.setAttribute(
                "revisions",
                revisions
        );

        request.getRequestDispatcher("template/revisions/list.jsp").forward(request, response);
    }
}