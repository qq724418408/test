package com.boc.jx.httptools.network.base;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.jx.tools.LogUtils;

public class ContentResponse extends RootRsp {
	private DynamicUri dynamicUri;
    private String rtnCode;
    private String rtnMsg;
    private List<Function> functions;
    private String accessToken;
    private String publicKey;
    private String content;

    public DynamicUri getDynamicUri() {
		return dynamicUri;
	}

	public void setDynamicUri(DynamicUri dynamicUri) {
		this.dynamicUri = dynamicUri;
	}

	public String getPublicKey() {
    	JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(content);
			publicKey =jsonObject.optString("publicKey");
			LogUtils.e("publicKey=="+publicKey);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getAccessToken() {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(content);
			accessToken =jsonObject.optString("accessToken");
			LogUtils.e("accessToken=="+accessToken);
		} catch (JSONException e) {
			e.printStackTrace();
		}
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
