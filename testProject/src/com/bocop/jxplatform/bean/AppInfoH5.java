package com.bocop.jxplatform.bean;

/**
 * Created by bocop on 15/12/24.
 */
public class AppInfoH5 {

    private String clientId; // 应用id
    private String client_secret; // 应用id随机数
    private String asrUrl; // App服务器地址
    private String sapUrl; // Sap服务器地址
    private String hideBackArrow; // 返回箭头 是否需要箭头：默认true（隐藏），false（显示），返回提供回调exitlogin
    private String regUrl; // 注册地址
    private String themePath; // 主题
    private String appVersion; // App版本
    private String responseType;
    private String hasHeaderBar;
    private String accessToken;
    private String userId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getAsrUrl() {
        return asrUrl;
    }

    public void setAsrUrl(String asrUrl) {
        this.asrUrl = asrUrl;
    }

    public String getSapUrl() {
        return sapUrl;
    }

    public void setSapUrl(String sapUrl) {
        this.sapUrl = sapUrl;
    }

    public String getHideBackArrow() {
        return hideBackArrow;
    }

    public void setHideBackArrow(String hideBackArrow) {
        this.hideBackArrow = hideBackArrow;
    }

    public String getRegUrl() {
        return regUrl;
    }

    public void setRegUrl(String regUrl) {
        this.regUrl = regUrl;
    }

    public String getThemePath() {
        return themePath;
    }

    public void setThemePath(String themePath) {
        this.themePath = themePath;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getHasHeaderBar() {
        return hasHeaderBar;
    }

    public void setHasHeaderBar(String hasHeaderBar) {
        this.hasHeaderBar = hasHeaderBar;
    }

    public String getAccesssToken() {
        return accessToken;
    }

    public void setAccesssToken(String accesssToken) {
        this.accessToken = accesssToken;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId= userId;
    }
}