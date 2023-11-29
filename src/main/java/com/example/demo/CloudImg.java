package com.example.demo;

import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.StringJoiner;

public class CloudImg {
    private static final String CLOUD_IMG_CDN_ID = "ctosadjgda";
    private static final String CLOUD_IMG_BASE_URL = "https://" + CLOUD_IMG_CDN_ID +".cloudimg.io/";

    private Integer width;
    private Integer height;
    private String func;

    public CloudImg() {
        this.width = null;
        this.height = null;
        this.func = null;
    }

    public String getCdnUrl(String url) {
        if (url == null || url.isEmpty() || url.isBlank()) {
            return "";
        }

        String path = url.substring(url.indexOf(':') + 3);

        int queryIndex = path.indexOf('?');
        if (queryIndex != -1) {
            path = path.substring(0, queryIndex);
        }

        StringJoiner params = new StringJoiner("&");

        if (func != null) {
            params.add("func=" + func);
        }
        if (width != null) {
            params.add("width=" + width);
        }
        if (height != null) {
            params.add("height=" + height);
        }

        if (params.length() > 0) {
            path += "?" + params;
        }

        return CLOUD_IMG_BASE_URL + path;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }
}
