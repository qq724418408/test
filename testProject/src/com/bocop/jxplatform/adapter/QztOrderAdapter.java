package com.bocop.jxplatform.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.boc.jx.tools.CommonAdapter;
import com.boc.jx.tools.ViewHolder;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.PerFunctionBean;
import com.bocop.jxplatform.bean.QztOrderBean;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.QztRequestWithJsonAndBody;
import com.bocop.jxplatform.util.QztRequestWithJsonAndHead;
import com.bocop.qzt.QztOrderActivity;
import com.bocop.qzt.QztPayActivity;
import com.bocop.qzt.QztProgressActivity;
import com.bocop.qzt.QztOrderActivity.OnItemClickListenerImpl;
import com.google.gson.Gson;

/**
 * @author luoyang
 * @version 创建时间：2016-4-27 上午9:47:52 类说明
 */

public class QztOrderAdapter extends CommonAdapter<QztOrderBean> {

	Context mcontext;

	public QztOrderAdapter(Context context, List<QztOrderBean> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		mcontext = context;
	}

	@Override
	public void convert(ViewHolder helper, final QztOrderBean item) {
		String strState;
		if (item.getOrder_status().equals("3")) {
			strState = "已缴费";
		} else {
			strState = "未缴费";
		}
		helper.setImageResource(R.id.icon, R.drawable.icon_card);
		helper.setText(R.id.qzt_order_num, item.getOrder_num());
		helper.setText(R.id.qzt_order_state,
				strState + ":" + item.getOrder_amt() + "元");
		// if(item.getOrder_num().length() > 10){
		// String startData = item.getOrder_num().substring(0, 8);
		// Log.i("tag22", startData);
		// SimpleDateFormat format = new SimpleDateFormat("yyyyMMDD");
		// String nowData = format.format(new Date(System.currentTimeMillis()));
		// public Date = System.currentTimeMillis();
		// }
		// helper.getConvertView().set
		helper.getView(R.id.bt_qztorderpay).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						String strQztOrderNum = item.getOrder_num();
						String strQztOrderState = item.getOrder_status();
						String strAmt = item.getOrder_amt();
						Intent intent;
						Bundle bundle = new Bundle();
						bundle.putString("orderNum", strQztOrderNum);
						bundle.putString("amt", strAmt);
						bundle.putString("orderState", strQztOrderState);
						if (strQztOrderState.equals("3")) {
							intent = new Intent(mcontext,
									QztProgressActivity.class);
						} else {
							intent = new Intent(mcontext, QztPayActivity.class);
						}
						intent.putExtras(bundle);
						mcontext.startActivity(intent);

					}
				});

		helper.getView(R.id.bt_qztorderdel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						delQztOrder(item.getOrder_num());
					}
				});
	}

	private void delQztOrder(String orderNum) {

		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", LoginUtil.getUserId(mcontext));
		map.put("order_num", orderNum);
		final String strGson = gson.toJson(map);
		QztRequestWithJsonAndHead qztRequestWithJsonAndHead = new QztRequestWithJsonAndHead(
				mcontext);
		qztRequestWithJsonAndHead
				.postOpboc(
						strGson,
						BocSdkConfig.qztOrderDelUrl,
						new com.bocop.jxplatform.util.QztRequestWithJsonAndHead.CallBackBoc() {
							@Override
							public void onSuccess(String responStr) {
								Log.i("tag22", responStr);
								Toast.makeText(mContext, "删除订单成功", Toast.LENGTH_LONG).show();
								((QztOrderActivity)mContext).requestBocopForQztOrder();
//								Intent intent = new Intent(mContext,QztOrderActivity.class);
//								mcontext.startActivity(intent);
							}

							@Override
							public void onStart() {
								// TODO Auto-generated method stub
							}

							@Override
							public void onFailure(String responStr) {
								Toast.makeText(mcontext,
										responStr, Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFinish() {
							}
						});
	}
	
	
	public interface IOrderDelete {
		void onSucess();

		void onError();

		void onException();

	}

}
