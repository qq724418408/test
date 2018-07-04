package com.boc.jx.httptools.network.base;

/**
 * Created by XinQingXia on 2017/8/16.
 */

public class DynamicContent extends RootRsp {
    protected String message; //
    protected String result; //
    //	protected String VIEW_ID; //
    protected String DYNAMIC_TOKEN; //

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDYNAMIC_TOKEN() {
        return DYNAMIC_TOKEN;
    }

    public void setDYNAMIC_TOKEN(String DYNAMIC_TOKEN) {
        this.DYNAMIC_TOKEN = DYNAMIC_TOKEN;
    }
}
