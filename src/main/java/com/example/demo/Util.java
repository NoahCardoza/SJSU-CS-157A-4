package com.example.demo;

import com.example.demo.beans.entities.Location;
import com.example.demo.daos.LocationDao;
import com.example.demo.servlets.search.SearchServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    static public String nullify(String s) {
        if (s == null) {
            return null;
        }

        if (s.isEmpty()) {
            return null;
        }

        if (s.equals("null")) {
            return null;
        }

        return s;
    }

    static public Integer parseIntOrNull(String s) {
        s = nullify(s);

        if (s == null) {
            return null;
        }

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static public Long parseLongOrNull(String s) {
        s = nullify(s);

        if (s == null) {
            return null;
        }

        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static public Long nullIfZero(Long l) {
        if (l == null || l == 0) {
            return null;
        }

        return l;
    }

    static public Double parseDoubleOrNull(String s) {
        s = nullify(s);

        if (s == null) {
            return null;
        }

        return Double.parseDouble(s);
    }
    static public HashMap<String, String> getGetParameters(HttpServletRequest request) {
        HashMap<String, String> getParams = new HashMap<>();

        String queryString = request.getQueryString();

        if (queryString != null) {
            String[] queryParams = queryString.split("&");
            for (String queryParam : queryParams) {
                String[] paramParts = queryParam.split("=");
                if (paramParts.length == 2) {
                    getParams.put(paramParts[0], paramParts[1]);
                }
            }
        }

        return getParams;
    }

    static public HashMap<String, String> getPostParameters(HttpServletRequest request) {
        HashMap<String, String> getParams = getGetParameters(request);

        HashMap<String, String> postParams = new HashMap<>();

        for (String param : request.getParameterMap().keySet()) {
            if (!getParams.containsKey(param)) {
                postParams.put(param, request.getParameter(param));
            }
        }

        return postParams;
    }

    public static Integer parseIntOrDefault(String s, int i) {
        Integer parsed = parseIntOrNull(s);

        if (parsed == null) {
            return i;
        }

        return parsed;
    }

    public static String getPathWithQueryString(HttpServletRequest request) {
        String queryString = request.getQueryString();

        if (queryString != null) {
            return request.getRequestURI() + "?" + queryString;
        }

        return request.getRequestURI();
    }

    public static String captureTemplateOutput(HttpServletRequest request, HttpServletResponse response, String template) throws ServletException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(baos);
        final PrintWriter writer = new PrintWriter(ps);

        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) response) {
            @Override public PrintWriter getWriter() {
                return writer;
            }
        };

        request.getRequestDispatcher("/template/locations/ajaxForm.jsp").forward(request, wrapper);

        return baos.toString();
    }

    public static Long parseLongOrDefault(String id, Long defaultValue) {
        Long parsed = parseLongOrNull(id);

        if (parsed == null) {
            return defaultValue;
        }

        return parsed;
    }
}
