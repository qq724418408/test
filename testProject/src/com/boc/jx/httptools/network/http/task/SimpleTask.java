package com.boc.jx.httptools.network.http.task;

import com.boc.jx.httptools.network.http.build.HttpBuild;

/**
 * Created by XinQingXia on 2017/8/13.
 */

public class SimpleTask {
    private HttpBuild build;

    public SimpleTask(HttpBuild build) {
        this.build = build;
    }

    public void send() {
        build.getExecutors().postAsync(build);
    }
}
