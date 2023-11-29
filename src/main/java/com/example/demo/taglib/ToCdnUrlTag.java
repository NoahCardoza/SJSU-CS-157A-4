package com.example.demo.taglib;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
public class ToCdnUrlTag extends SimpleTagSupport {
    private String url;
    private int size = 0;

    public void setUrl(String url) {
        this.url = url;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public String getUrl() {
        return url;
    }
    public int getSize() {
        return size;
    }
    public String getCdnUrl() {
        return com.example.demo.CloudImg.getCdnUrl(url, size);
    }
    public void doTag() {
        try {
            getJspContext().getOut().write(getCdnUrl());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
