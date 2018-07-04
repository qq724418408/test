package com.bocop.qzt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.adapter.QztOrderAdapter;
import com.bocop.jxplatform.bean.CarListBean;
import com.bocop.jxplatform.bean.QztOrderBean;
import com.bocop.jxplatform.bean.QztOrderListBean;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.trafficassistant.MyPeccancyActivity;
import com.bocop.jxplatform.trafficassistant.TrafficAssistantMainActivity;
import com.bocop.jxplatform.trafficassistant.TrafficPayActivity;
import com.bocop.jxplatform.trafficassistant.TrafficQuickPayActivity;
import com.bocop.jxplatform.util.ActivityForResultCode;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.QztRequestWithJsonAll;
import com.bocop.jxplatform.util.QztRequestWithJsonAndBody;
import com.bocop.jxplatform.util.QztRequestWithJsonAndHead;
import com.bocop.jxplatform.view.BackButton;
import com.google.gson.Gson;

@ContentView(R.layout.qzt_activity_order)
public class QztOrderActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn;
	@ViewInject(R.id.lvqztorder)
	private ListView qztOrderLV;
	
	@ViewInject(R.id.ed_qzt_id)
	private EditText id;
	
	String strId;
	QztOrderAdapter qztOrderAdapter;
	List<QztOrderBean> qztOrderList;
	List<Map<String,Object>> mapList;
	
	QztOrderListBean qztOrderListBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tv_titleName.setText("订单");
	}
	
	@OnClick(R.id.bt_qztorder)
	public void onClick(View v){
		strId = id.getText().toString().trim();
		if(strId.length()<15){
			Toast.makeText(this, "请输入正确的身份证号", Toast.LENGTH_SHORT).show();
			return;
		}
		requestBocopForQztOrder();
	}
	
	public void requestBocopForQztOrder(){
		
		Gson gson = new Gson();
		Map<String,String> map = new HashMap<String,String>();
		map.put("user_id", LoginUtil.getUserId(QztOrderActivity.this));
		map.put("customerId", strId);
		final String strGson = gson.toJson(map);
		QztRequestWithJsonAndBody qztRequestWithJsonAndBody = new QztRequestWithJsonAndBody(this);
		qztRequestWithJsonAndBody.postOpboc(strGson, BocSdkConfig.qztOrderUrl, new com.bocop.jxplatform.util.QztRequestWithJsonAndBody.CallBackBoc(){
			@Override
			public void onSuccess(String responStr) {
				Log.i("tag22", responStr);
				mapList = new ArrayList<Map<String,Object>>();
				qztOrderList = new ArrayList<QztOrderBean>();
//				  List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				try {
//					qztOrderListBean = JsonUtils.getObject(responStr, QztOrderListBean.class);
					mapList = JsonUtils.getListMap(responStr);
//					qztOrderListBean.getHead()
//					qztOrderList = qztOrderListBean.getBody();
					for(int i=0;i<mapList.size();i++){
						QztOrderBean qztOrderBean = new QztOrderBean();
						qztOrderBean.setOrder_num(mapList.get(i).get("order_num").toString());
						qztOrderBean.setOrder_status(mapList.get(i).get("order_status").toString());
						qztOrderBean.setOrder_amt(mapList.get(i).get("order_amt").toString());
						qztOrderList.add(qztOrderBean);
					}
					Log.i("tag22", "list comp");
					qztOrderAdapter = new QztOrderAdapter(QztOrderActivity.this, qztOrderList, R.layout.qzt_order_listview_item);
					qztOrderLV.setAdapter(qztOrderAdapter);
					qztOrderLV.setOnItemClickListener(new OnItemClickListenerImpl());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onFailure(String responStr) {
				Toast.makeText(QztOrderActivity.this, responStr, Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	public class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			QztOrderBean bean = QztOrderActivity.this.qztOrderAdapter.getItem(position);
			String strQztOrderNum = bean.getOrder_num();
			String strQztOrderState = bean.getOrder_status();
			String strAmt = bean.getOrder_amt();
			Intent intent; 
			Bundle bundle = new Bundle();
			bundle.putString("orderNum", strQztOrderNum);
			bundle.putString("amt", strAmt);
			bundle.putString("orderState", strQztOrderState);
			if(strQztOrderState.equals("3")){
				intent = new Intent(QztOrderActivity.this, QztProgressActivity.class);
			}else{
				intent = new Intent(QztOrderActivity.this, QztPayActivity.class);
			}
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
	}
}
