package com.example.demo.taglib;
import com.example.demo.CloudImg;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
public class ToCdnUrlTag extends SimpleTagSupport {
    private String url;
    CloudImg urlBuilder;

    public ToCdnUrlTag() {
        super();
        urlBuilder = new CloudImg();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWidth(int width) {
        urlBuilder.setWidth(width);
    }
    public void setHeight(int height) {
        urlBuilder.setHeight(height);
    }
    public void setFunc(String func) {
        urlBuilder.setFunc(func);
    }

    public void doTag() {
        try {
            getJspContext().getOut().write(urlBuilder.getCdnUrl(url));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
