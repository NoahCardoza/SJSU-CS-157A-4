package com.example.demo;

public class Util {
    static public Integer parseIntOrNull(String s) {
        if (s == null) {
            return null;
        }
        if (s.isEmpty()) {
            return null;
        }
        return Integer.parseInt(s);
    }

    static public Long parseLongOrNull(String s) {
        if (s == null) {
            return null;
        }
        if (s.isEmpty()) {
            return null;
        }
        return Long.parseLong(s);
    }

    static public Double parseDoubleOrNull(String s) {
        if (s == null) {
            return null;
        }
        if (s.isEmpty()) {
            return null;
        }
        return Double.parseDouble(s);
    }
}
