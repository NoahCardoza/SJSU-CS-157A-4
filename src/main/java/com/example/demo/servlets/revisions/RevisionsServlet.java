package com.example.demo.servlets.revisions;

import com.example.demo.Guard;
import com.example.demo.Util;
import com.example.demo.beans.Alert;
import com.example.demo.beans.entities.Revision;
import com.example.demo.beans.entities.User;
import com.example.demo.daos.RevisionDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
                case "revert" -> revert(request, response);
                case "get" -> get(request, response);
                default -> response.sendRedirect(request.getContextPath() + "/");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        Long revisionId = Util.parseLongOrNull(request.getParameter("id"));

        if (revisionId == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Optional<Revision> revision = RevisionDao.getInstance().get(revisionId);

        if (revision.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        RevisionDao.getInstance().getEditsForRevision(revision.get());
        RevisionDao.getInstance().getUserForRevision(revision.get());
        request.setAttribute("revision", revision.get());

        request.getRequestDispatcher("template/revisions/get.jsp").forward(request, response);
    }

    private void revert(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        if (!request.getMethod().equals("POST")) {
            response.getWriter().println("Invalid method");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User user = Guard.requireAuthenticationWithMessage(
                request,
                response,
                "You must be logged in to revert.",
                false
        );

        if (user == null) {
            return;
        }

        HttpSession session = request.getSession();

        if (!(user.isAdministrator() || user.isModerator())) {
            Guard.redirectToLogin(
                    request,
                    response,
                    new Alert("danger", "You must be an administrator or moderator to revert")
            );
            return;
        }

        Long revisionId = Util.parseLongOrNull(request.getParameter("id"));

        if (revisionId == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Optional<Revision> revision = RevisionDao.getInstance().get(revisionId);

        if (!revision.isPresent()) {
            return;
        }

        RevisionDao.getInstance().revert(revision.get());

        session.setAttribute("alert", new Alert("success", "Revert successful!"));
        response.sendRedirect(request.getContextPath() + "/revisions?f=list&type=" + revision.get().getTableName()+ "&id=" + revision.get().getPrimaryKey());
    }

    private void vote(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        if (!request.getMethod().equals("POST")) {
            response.getWriter().println("Invalid method");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User user = Guard.requireAuthenticationWithMessage(
                request,
                response,
                "You must be logged in to vote."
        );

        if (user == null) {
            return;
        }

        HttpSession session = request.getSession();

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

        String action = request.getParameter("action");

        if (action == null) {
            session.setAttribute("alert", new Alert("danger", "Invalid vote method"));
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        int vote = switch (action) {
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
        response.sendRedirect(request.getContextPath() + "/revisions?f=list&type=" + revision.get().getTableName() + "&id=" + revision.get().getPrimaryKey());
    }


    public void list(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String tableName = request.getParameter("type");
        Long primaryKey = Util.parseLongOrNull(request.getParameter("id"));

        if (tableName == null || primaryKey == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        List<Revision> revisions = RevisionDao.getInstance().getEntityRevisions(tableName, primaryKey, (Long) request.getAttribute("user_id"));

        for (Revision revision : revisions) {
            RevisionDao.getInstance().getUserForRevision(revision);
        }

        request.setAttribute(
                "revisions",
                revisions
        );

        request.getRequestDispatcher("template/revisions/list.jsp").forward(request, response);
    }
}