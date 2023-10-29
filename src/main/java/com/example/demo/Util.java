package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
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

        return Integer.parseInt(s);
    }

    static public Long parseLongOrNull(String s) {
        s = nullify(s);

        if (s == null) {
            return null;
        }

        return Long.parseLong(s);
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
}
