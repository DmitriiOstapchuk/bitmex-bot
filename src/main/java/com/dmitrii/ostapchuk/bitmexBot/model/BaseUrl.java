package com.dmitrii.ostapchuk.bitmexBot.model;

public enum BaseUrl {
    TEST("https://testnet.bitmex.com");
    private String url;
    BaseUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
}
