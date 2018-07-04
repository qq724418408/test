package com.boc.jx.httptools.network.http.cnotrol;



import java.util.List;
import java.util.Map;

import com.boc.jx.httptools.network.base.Function;


/**
 * Created by XinQingXia on 2017/8/15.
 */

public class DyanmicData {
    private Map<String, String> fParam;
    private List<Function> functions;//功能列表
    private boolean dynamicState;//动态流程开关
    private String entry;//key/url
    private String dynamicToken;//动态token
    private String heardToken;//heartoken

    public Map<String, String> getfParam() {
        return fParam;
    }

    public DyanmicData setfParam(Map<String, String> fParam) {
        this.fParam = fParam;
        return this;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public DyanmicData setFunctions(List<Function> functions) {
        this.functions = functions;
        return this;
    }

    public boolean isDynamicState() {
        return dynamicState;
    }

    public DyanmicData setDynamicState(boolean dynamicState) {
        this.dynamicState = dynamicState;
        return this;
    }

    public String getEntry() {
        return entry;
    }

    public DyanmicData setEntry(String entry) {
        this.entry = entry;
        return this;
    }

    public String getDynamicToken() {
        return dynamicToken;
    }

    public DyanmicData setDynamicToken(String dynamicToken) {
        this.dynamicToken = dynamicToken;
        return this;
    }

    public String getHeardToken() {
        if (heardToken == null)
            heardToken = "";
        return heardToken;
    }

    public DyanmicData setHeardToken(String heardToken) {
        this.heardToken = heardToken;
        return this;
    }
}
