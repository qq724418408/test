package com.bocop.qzt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.adapter.QztProgressAdapter;
import com.bocop.jxplatform.bean.QztDocBean;
import com.bocop.jxplatform.bean.QztDocListBean;
import com.bocop.jxplatform.bean.QztOrderBean;
import com.bocop.jxplatform.bean.QztProgressBean;
import com.bocop.jxplatform.bean.QztProgressListBean;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.QztRequestWithJsonAndBody;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.qzt.view.CustomNodeListView;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
@ContentView(R.layout.qzt_activity_progress)
public class QztProgressActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn;
	
	@ViewInject(R.id.qztcustomlv)
	private CustomNodeListView listview;
	
	QztProgressListBean qztProgressListBean;
	List<QztProgressBean> listQztProgress;
	
//	List<QztProgressBean> datas;
	String strQztOrderNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.qzt_activity_progress);
		
		initView();
		tv_titleName.setText("签证进度");
	}
	
	private void initView(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			strQztOrderNum = bundle.getString("orderNum");
			Log.i("tag22", strQztOrderNum);
			requestBocopForQztProgress(strQztOrderNum);
		}
	}
	
	
	private void requestBocopForQztProgress(String orderNum){
		Gson gson = new Gson();
		Map<String,String> map = new HashMap<String,String>();
		map.put("order_num",orderNum);
		final String strGson = gson.toJson(map);
		QztRequestWithJsonAndBody qztRequestWithJsonAndBody = new QztRequestWithJsonAndBody(this);
		qztRequestWithJsonAndBody.postOpboc(strGson, BocSdkConfig.qztProgressUrl, new com.bocop.jxplatform.util.QztRequestWithJsonAndBody.CallBackBoc(){
			@Override
			public void onSuccess(String responStr) {
				Log.i("tag22", responStr);
				String respon = "{" + "\"" + "body" + "\"" + ":" + responStr + "}";
				Log.i("tag33", respon);
				try {
					qztProgressListBean = JsonUtils.getObject(respon, QztProgressListBean.class);
					listQztProgress = qztProgressListBean.getBody();
					QztProgressAdapter adapter = new QztProgressAdapter(listQztProgress, QztProgressActivity.this);
					listview.setAdapter(adapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
//				qztDocListBean = JsonUtils.getObject("\"" + "body" + "\"" + ":" +responStr, QztDocListBean.class);
//				listQztDoc = qztDocListBean.getBody();
//				List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
////				  List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//				try {
////					qztOrderListBean = JsonUtils.getObject(responStr, QztOrderListBean.class);
//					mapList = JsonUtils.getListMap(responStr);
////					qztOrderListBean.getHead()
////					qztOrderList = qztOrderListBean.getBody();
//					qztOrderList.clear();
//					for(int i=0;i<mapList.size();i++){
//						QztOrderBean qztOrderBean = new QztOrderBean();
//						qztOrderBean.setOrder_num(mapList.get(i).get("order_num").toString());
//						qztOrderBean.setOrder_status(mapList.get(i).get("order_status").toString());
//						qztOrderList.add(qztOrderBean);
//					}
//					Log.i("tag22", "list comp");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onFailure(String responStr) {
				Toast.makeText(QztProgressActivity.this, responStr, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
