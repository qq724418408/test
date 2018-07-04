package com.bocop.jxplatform.bean;

/**
 * Created by bocop on 15/12/30.
 */
public class AppLoginInfo {
    private String clientId; // 应用id
    private String userId; // 应用id随机数
    private String accessToken; // App服务器地址
    private String hideBackArrow; // 返回箭头 是否需要箭头：默认true（隐藏），false（显示），返回提供回调exitlogin

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getHideBackArrow() {
        return hideBackArrow;
    }

    public void setHideBackArrow(String hideBackArrow) {
        this.hideBackArrow = hideBackArrow;
    }
}
