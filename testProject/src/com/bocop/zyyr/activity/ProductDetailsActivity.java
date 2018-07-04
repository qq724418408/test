package com.bocop.zyyr.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.view.LoadingView;
import com.boc.jx.view.LoadingView.OnRetryListener;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.FormsUtil;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.utils.ToastUtils;
import com.bocop.zyyr.bean.Period;
import com.bocop.zyyr.bean.ProductDetails;
import com.bocop.zyyr.bean.ProductDetailsResponse;
import com.bocop.zyyr.bean.Status;
import com.bocop.zyyr.bean.StatusResponse;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.thoughtworks.xstream.io.StreamException;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 产品详情
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.zyyr_activity_pro_details)
public class ProductDetailsActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.tvProName)
	private TextView tvProName;
	@ViewInject(R.id.tvRate)
	private TextView tvRate;
	@ViewInject(R.id.tvProIntro)
	private TextView tvProIntro;
	/** 收益率 */
	@ViewInject(R.id.tvProceed)
	private TextView tvProceed;
	@ViewInject(R.id.llProceed)
	private LinearLayout llProceed;
	@ViewInject(R.id.tvProceedLine)
	private TextView tvProceedLine;

	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;
	@ViewInject(R.id.scrollView)
	private ScrollView scrollView;
	@ViewInject(R.id.ivProduct)
	private ImageView ivProduct;
	@ViewInject(R.id.btnApply)
	private Button btnApply;

	private String proId;
	private ProductDetails details;
	private List<String> periodList = new ArrayList<>();
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tvTitle.setText("产品详情");
		initDate();
		requestProDetails();
	}

	private void initDate() {
		if (null != getIntent().getStringExtra("PRO_ID")) {
			proId = getIntent().getStringExtra("PRO_ID");
			int resId = "0000000090".equals(proId) ? R.drawable.zyyr_img_ydd : R.drawable.zyyr_img_yxd; 
			bitmap = readBitMap(this, resId);
			LayoutParams lp = ivProduct.getLayoutParams();
			lp.width = FormsUtil.SCREEN_WIDTH;
			lp.height = FormsUtil.SCREEN_WIDTH * bitmap.getHeight() / bitmap.getWidth();
			ivProduct.setLayoutParams(lp);
			ivProduct.setImageBitmap(bitmap);
		}

	}

	private void initView() {
		tvProName.setText(details.getProName().replaceAll(" ", ""));
		tvRate.setText(Double.parseDouble(details.getRate().replaceAll(" ", "")) * 100 + "%");
		tvProIntro.setText(details.getDetails().replaceAll(" ", ""));

		if (!BocSdkConfig.isTest) {
			if (!LoginUtil.isLog(this)) {
				btnApply.setBackgroundDrawable(getResources().getDrawable(R.drawable.yfx_shape_circle_stroke_greymid));
				btnApply.setClickable(false);
			}
		}
	}

	@OnClick({ R.id.btnApply })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnApply:
			requestAuthenStatus();
			break;
		}
	}
	
	/**
	* 以最省内存的方式读取本地资源的图片
	* 
	* @param context
	* @param resId
	* @return
	*/
	public static Bitmap readBitMap(Context context, int resId) {
	     BitmapFactory.Options opt = new BitmapFactory.Options();
	     opt.inPreferredConfig = Bitmap.Config.RGB_565;
	     opt.inPurgeable = true;
	     opt.inInputShareable = true;
	     // 获取资源图片
	     InputStream is = context.getResources().openRawResource(resId);
	     return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 请求产品详情
	 */
	private void requestProDetails() {
		RequestBody formBody = new FormEncodingBuilder().add("pdtId", proId).build();
		CspUtil cspUtil = new CspUtil(this);
		cspUtil.postCspNoLogin(BocSdkConfig.ZYYR_PDT_DETAILS, formBody, true, new CallBack() {
			@Override
			public void onSuccess(String responStr) {
				onGetDataSuccess(responStr);
			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(String responStr) {
				loadingView.setVisibility(View.VISIBLE);
				btnApply.setVisibility(View.GONE);
				scrollView.setVisibility(View.GONE);

				loadingView.setmOnRetryListener(new OnRetryListener() {

					@Override
					public void retry() {
						requestProDetails();
					}
				});
			}
		});
	}

	/**
	 * 是否认证了资料
	 */
	private void requestAuthenStatus() {
		try {
			RequestBody formBody = new FormEncodingBuilder().add("userId", LoginUtil.getUserId(this)).build();
			CspUtil cspUtil = new CspUtil(this);

			cspUtil.postCspNoLogin(BocSdkConfig.ZYYR_CHCEK_USERINFO, formBody, true, new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					try {
						StatusResponse statusResponse = XStreamUtils.getFromXML(responStr, StatusResponse.class);
						ConstHead constHead = statusResponse.getConstHead();
						if (null != constHead && "00".equals(constHead.getErrCode())) {
							Status status = statusResponse.getStatusExtern().getStatus();
							if ("0".equals(status.getStatus())) {
								DialogUtil.showWithTwoBtn(ProductDetailsActivity.this, "资料认证未通过，请先认证资料", "确定", "取消",
										new OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {
												callMe(AuthentInfoActivity.class);
											}
										}, new OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {
												dialog.dismiss();
											}
										});
							} else {
								String phone = status.getPhone();
								Intent intent = new Intent(ProductDetailsActivity.this, ApplyLoanActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("DETAILS", details);
								bundle.putStringArrayList("PERIOD", (ArrayList<String>) periodList);
								bundle.putString("PHONE", phone);
								intent.putExtras(bundle);
								startActivity(intent);
							}
						}
					} catch (StreamException e) {
						ToastUtils.showError(ProductDetailsActivity.this, "后台数据异常", Toast.LENGTH_SHORT);
					}
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFailure(String responStr) {
					CspUtil.onFailure(ProductDetailsActivity.this, responStr);
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 请求产品详情成功后
	 * 
	 * @param responStr
	 */
	private void onGetDataSuccess(String responStr) {
		loadingView.setVisibility(View.GONE);
		btnApply.setVisibility(View.VISIBLE);
		scrollView.setVisibility(View.VISIBLE);
		try {
			ProductDetailsResponse detailsResponse = XStreamUtils.getFromXML(responStr, ProductDetailsResponse.class);
			ConstHead constHead = detailsResponse.getConstHead();
			if (null != constHead && "00".equals(constHead.getErrCode())) {
				details = detailsResponse.getDetailsExtern().getDetails();
				List<Period> pList = new ArrayList<>();
				pList.addAll(detailsResponse.getDetailsExtern().getPeriodList());
				periodList.clear();
				for (int i = 0; i < pList.size(); i++) {
					periodList.add(pList.get(i).getPeriod());
				}
				initView();
			} else {
				loadingView.setVisibility(View.VISIBLE);
				btnApply.setVisibility(View.GONE);
				scrollView.setVisibility(View.GONE);

				loadingView.setmOnRetryListener(new OnRetryListener() {

					@Override
					public void retry() {
						requestProDetails();
					}
				});
			}
		} catch (StreamException e) {
			loadingView.setVisibility(View.VISIBLE);
			btnApply.setVisibility(View.GONE);
			scrollView.setVisibility(View.GONE);

			loadingView.setmOnRetryListener(new OnRetryListener() {

				@Override
				public void retry() {
					requestProDetails();
				}
			});
			ToastUtils.showError(this, "后台数据异常", Toast.LENGTH_SHORT);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(bitmap != null && !bitmap.isRecycled()){ 
			System.out.println("bitmap recycle...");
	        // 回收并且置为null
			bitmap.recycle(); 
	        bitmap = null; 
		} 
		System.gc();
	}
}
