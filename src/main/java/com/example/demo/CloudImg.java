package com.example.demo;

public class CloudImg {
    private static final String CLOUD_IMG_CDN_ID = "ctosadjgda";
    private static final String CLOUD_IMG_BASE_URL = "https://" + CLOUD_IMG_CDN_ID +".cloudimg.io/";

    public static String getCdnUrl(String url) {
        return getCdnUrl(url, 0);
    }

    public static String getCdnUrl(String url, int size) {
        if (url == null || url.isEmpty() || url.isBlank()) {
            return "";
        }

        String path = url.substring(url.indexOf(':') + 3);

        int queryIndex = path.indexOf('?');
        if (queryIndex != -1) {
            path = path.substring(0, queryIndex);
        }

        if (size > 0) {
            path = path + "?width=" + size + "&height=" + size;
        }

        return CLOUD_IMG_BASE_URL + path;
    }
}
