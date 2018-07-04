package com.boc.jx.baseUtil.net;

import com.boc.jx.baseUtil.logger.Logger;
import org.apache.http.Header;

/**
 * Created by hwt on 2/2/15.
 * 网络请求响应解析。
 */
public abstract class Notify {

    protected abstract void parseResponse(ResponseObserver observer, String requestUrl, Header[] headers, String responseStr);

    protected abstract void parseFailed(ResponseObserver observer, String requestUrl, Header[] headers, Throwable throwable);

    /**
     * 解析常见异常
     *
     * @param throwable
     * @return
     */
    protected RetCode parseFailedInfo(Throwable throwable) {
        if (throwable != null) {
            String message = throwable.getMessage();
            if (message != null) {
                if (message.contains("Timeout") || message.contains("timeout")
                        || message.contains("timed out")) {
                    return RetCode.timeOut;
                } else if (message.contains("refused") || message.contains("unreachable") ||
                        message.contains("SocketException")) {
                    return RetCode.netError;
                } else {
                    Logger.d(message, throwable);
                }
            }
        }
        return RetCode.unKnow;
    }
}
