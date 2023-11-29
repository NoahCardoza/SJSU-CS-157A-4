package com.example.demo.jstl;

import com.example.demo.CloudImg;

import java.util.Arrays;
import java.util.List;

public class JstlFunctions {
    public static boolean contains(String[] list, String o) {
        return Arrays.asList(list).contains(o);
    }

    public static String toCdnUrl(String url) {
        return CloudImg.getCdnUrl(url);
    }
    public static String toCdnUrl(String url, int size) {
        return CloudImg.getCdnUrl(url, size);
    }
}
