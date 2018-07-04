package com.bocop.jxplatform.wxapi;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;


import com.bocop.jxplatform.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        wxAPI = WXAPIFactory.createWXAPI(this,Constant.APP_ID,true);
//        wxAPI.registerApp(Constant.APP_ID);
       WeChatOpenID.mWxApi.handleIntent(getIntent(), this);
        
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        WeChatOpenID.mWxApi.handleIntent(getIntent(),this);
    }
    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
    	 	Toast.makeText(this, "微信发送请求到第三方应用时", Toast.LENGTH_SHORT).show();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
    		Toast.makeText(this, "消息。。。。", Toast.LENGTH_SHORT).show();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == resp.getType()) {
               	//分享失败
                }else {
                	//登录失败
                	 Toast.makeText(this, "用户已取消登录", Toast.LENGTH_SHORT).show();
                }
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        
                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                        getOpenId(code);
                        break;
                    case RETURN_MSG_TYPE_SHARE:
                    		//微信分享成功
                        //finish();
                        break;
                }
                break;
                
        }
    }

	private void getOpenId(String  code) {
		// TODO Auto-generated method stub
		OkHttpClient client = new OkHttpClient();
		Request.Builder builder = new Request.Builder().url(WeChatOpenID.getAccessTokenUrl(code));
		client.newCall(builder.build()).enqueue(new Callback() {
			
			@Override
			public void onResponse(Response response) throws IOException {
				// TODO Auto-generated method stub
				if(response.isSuccessful()) {
					String result = response.body().string();
//					Log.d("", result);
					 JSONObject jsonObject = null;
					  String openid="";
		                try {
		                    jsonObject = new JSONObject(result);
		                    openid = jsonObject.getString("openid").toString().trim();
		                    String access_token = jsonObject.getString("access_token").toString().trim();
		                } catch (JSONException e) {
		                    e.printStackTrace();
		                }
		               final String xxxx = openid;
		                runOnUiThread(new Runnable() {
							public void run() {
								new AlertDialog.Builder(WXEntryActivity.this)
                	   				.setTitle("微信授权登录").setMessage("openid===>>>"+xxxx).show();
							}
						});
				}				
			}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				// TODO Auto-generated method stub
				runOnUiThread( new Runnable() {
					public void run() {
						new AlertDialog.Builder(WXEntryActivity.this)
						.setTitle("微信授权登录").setMessage("获取失败").show();
					}
				});
				
			}
		});
	}
}

