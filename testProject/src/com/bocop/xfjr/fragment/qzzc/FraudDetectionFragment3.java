package com.bocop.xfjr.fragment.qzzc;

import java.io.File;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.boc.jx.baseUtil.view.annotation.CheckNet;
import com.boc.jx.baseUtil.view.annotation.Duplicate;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;
import com.boc.jx.tools.NetworkUtil;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.XfjrIndexActivity;
import com.bocop.xfjr.activity.customer.XfjrMainActivity;
import com.bocop.xfjr.base.BaseCheckProcessFragment;
import com.bocop.xfjr.bean.ErrorBean;
import com.bocop.xfjr.bean.FaceCheckBean;
import com.bocop.xfjr.bean.add.ProductBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.observer.StepObserver;
import com.bocop.xfjr.observer.StepSubject;
import com.bocop.xfjr.util.BitmapUtils;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.util.file.FileUtil;
import com.bocop.yfx.utils.ToastUtils;
import com.google.gson.Gson;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.megvii.livenesslib.LivenessActivity;
import com.megvii.livenesslib.util.ConUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * description： 欺诈侦测第3个界面 人脸识别
 * <p/>
 * Created by TIAN FENG on 2017年8月28日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public class FraudDetectionFragment3 extends BaseCheckProcessFragment implements StepSubject {
	private static final int CHECK_KEY_CODE = 100;

	private StepObserver mObserver;
	@ViewInject(R.id.tvData)
	private TextView tvData;
	@ViewInject(R.id.btnRight)
	private Button btnRight;
	@ViewInject(R.id.layout1)
	private View layout1;// 布局1
	@ViewInject(R.id.layout2)
	private View layout2;// 布局2
	@ViewInject(R.id.tvResemble)
	private TextView tvResemble;// 相似度
	@ViewInject(R.id.tvCause)
	private TextView tvCause;// 原因
	@ViewInject(R.id.ivType)
	private ImageView ivType;// 成功失败的图片
	@ViewInject(R.id.line)
	private View line;// 分隔线条
	@ViewInject(R.id.aginCheck)
	private View aginCheck;// 再次检测
	@ViewInject(R.id.flGroup)
	private View flGroup;// 所有的跟布局llGroup
	@ViewInject(R.id.llGroup)
	private View llGroup;// 所有的跟布局
	@ViewInject(R.id.btnLeft)
	private View leftBtn;
	@ViewInject(R.id.btnRight)
	private TextView rightBtn;
	@ViewInject(R.id.tvAdress)
	private TextView tvAdress;
	// 强杀任务
	private boolean threadSwitch = true;
	
	private boolean isOk;

	// 相似度
	private int mSimilarityDegree;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.xfjr_fragment_fraud_detection3, container, false);
	}

	
	@OnClick(R.id.btnRight)
	@Duplicate("FraudDetectionFragment3")
	@CheckNet
	private void clickNext(View view) {
		if (!XfjrMain.isNet) {
			if (mObserver != null) {
				mObserver.pushBackStack();
				btnRight.setEnabled(false);
			}
			return;
		}
		if (mObserver != null) {
			if (rightBtn.getText().toString().equals(getString(R.string.next))) {
				//sendRequest(); // 原来是下一步提交数据给后台--->现在是成功了直接下一步
				if (mObserver != null) {
					FileUtil.delAllImages(getActivity()); // 删除所有图片缓存
					//((XfjrMainActivity) getActivity()).sendBR(0);
					mObserver.pushBackStack();
					btnRight.setEnabled(false);
				}
			} else if (rightBtn.getText().toString().equals(getString(R.string.over))) {
				HttpRequest.over(baseActivity, "03", new IHttpCallback<String>() {

					@Override
					public void onSuccess(String url, String result) {
						((XfjrMainActivity) getActivity()).sendBR(0);
						// 结束申请
//						getActivity().finish();
						Intent intent = new Intent(getActivity(),XfjrIndexActivity.class);
						getActivity().startActivity(intent);
					}

					@Override
					public void onError(String url, Throwable e) {
						UrlConfig.showErrorTips(getActivity(), e, true);
					}

					@Override
					public void onFinal(String url) {

					}
				});
			}

		}
	}

	/**
	 * 点击下一步提交网络数据
	 */
	private void sendRequest() {
		rightBtn.setEnabled(false);
//		if(file==null){
//			ToastUtils.show(getActivity(), "", 0);
//			return;
//		}
		HttpRequest.submitFaceInfo(getActivity(), mSimilarityDegree, file, new IHttpCallback<String>() {

			@Override
			public void onSuccess(String url, String result) {
				LogUtils.e("FraudDetectionFragment3--->"+result);
				onCheckSuccess();
				// 成功说明可以下一步，否则只能重新检测
//				if (mObserver != null) {
//					FileUtil.delAllImages(getActivity()); // 删除所有图片缓存
//					((XfjrMainActivity) getActivity()).sendBR(0);
//					mObserver.pushBackStack();
//				}
			}

			@Override
			public void onFinal(String url) {
				rightBtn.setEnabled(true);
			}

			@Override
			public void onError(String url, Throwable e) {
				LogUtils.e("FraudDetectionFragment3--->onError");
				String json = e.getMessage();
				LogUtils.e("error Json = " + json);
				ErrorBean b = new Gson().fromJson(json, ErrorBean.class);
				if (b.code == UrlConfig.FRDNotEnoughCode) {
					onCheckError();
					return;
				}
				UrlConfig.showErrorTips(getActivity(), e, false);
//				boolean isNetWork = e.getMessage().contains("无网络");
//				ToastUtils.show(getActivity(), isNetWork ? "当前无网络" : "服务器异常,请稍后重试。", 0);
			}
		});
	}

	@Override
	protected void initView() {
		// mSimilarityDegree = 95;
		// sendRequest();
		// 重置按钮不显示
		setVisibility(tvReset, View.GONE);
		leftBtn.setVisibility(View.GONE);
		rightBtn.setVisibility(View.GONE);
		// 检测成功后显示
		tvData.setText(getData());

		/**
		 * 将栈下的点击事件吃掉
		 */
		flGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		llGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		startLocation();
	}
	
	private LocationClient client;
	private BDLocationListener l;
	@SuppressWarnings("deprecation")
	private void startLocation() {
		client = new LocationClient(getActivity().getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		// 可选，是否需要地址信息，默认为不需要，即参数为false
		// 如果开发者需要获得当前点的地址信息，此处必须为true
		option.setIsNeedAddress(true);
        client.setLocOption(option);
		// 注册监听函数
        client.registerLocationListener(l = new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation location) {
				LogUtils.e("addr--->"+location.getAddrStr()+"----errorCode-->"+location.getLocType());
				// location.getLocType(),location.getAddrStr(),location.getCountry(),location.getProvince(),location.getCity(), location.getDistrict(), location.getStreet(), location
				if(TextUtils.isEmpty(location.getProvince())||TextUtils.isEmpty(location.getCity())){
					return ;
				}
				isOk = true;
				tvAdress.setText(location.getProvince() + location.getCity()
						+ (TextUtils.isEmpty(location.getDistrict()) ? "" : location.getDistrict())
						+ (TextUtils.isEmpty(location.getStreet()) ? "" : location.getStreet()));
				client.stop();
			}
		});
        client.start();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		client.stop();
	}

	@Override
	public void register(StepObserver observer) {
		mObserver = observer;
	}

	// 检测点击
	@OnClick({ R.id.btnCheck1, R.id.aginCheck })
	private void clickCheck(View view) {
		startCheckActivity();
	}

	/**
	 * 获取日期
	 */
	private String getData() {
		return XFJRUtil.getCurrentDate("yyyy/MM/dd HH:mm");
//		DateFormat dataFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//		long currentTime = System.currentTimeMillis();
//		return dataFormat.format(currentTime);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mIsCheck = false;
		if (!isOk) {
			startLocation();
		}
			
	}

	private boolean mIsCheck;// 是否正在检测

	/**
	 * 跳转人脸活体检测
	 */
	private void startCheckActivity() {
		if (!NetworkUtil.isNetworkAvailable(getActivity())) {
			ToastUtils.show(getActivity(), getString(R.string.no_open_network), 0);
			return;
		}
		if (mIsCheck) {
			return;
		}
		threadSwitch = true;
		mIsCheck = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				Manager manager = new Manager(getActivity());
				LivenessLicenseManager licenseManager = new LivenessLicenseManager(getActivity());
				manager.registerLicenseManager(licenseManager);
				manager.takeLicenseFromNetwork(ConUtil.getUUIDString(getActivity()));
				if (!threadSwitch) {
					return;
				}
				if (licenseManager.checkCachedLicense() > 0) {
					Looper.prepare();
					startActivityForResult(new Intent(getActivity(), LivenessActivity.class), CHECK_KEY_CODE);
					mIsCheck = false;
					Looper.loop();
				} else {
					Looper.prepare();
					ToastUtils.show(XfjrMain.mApp, getString(R.string.empower_error), 0);
					mIsCheck = false;
					Looper.loop();
				}
			}
		}).start();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CHECK_KEY_CODE && resultCode == Activity.RESULT_OK) {
			mIsCheck = false;
			// 拿到包装对象
			Bundle bundle = data.getExtras();
			// 拿到结果也就是是否成功
			String result = bundle.getString("result");
			// 这个看不懂
			String delta = bundle.getString("delta");
			LogUtils.e("result = " + result + ",delta = " + delta);
			// 成功传回来的string的id是这个id
			if (result.contains(getString(R.string.verify_success))) {
				// 返回所有的图片
				Map<String, byte[]> images = (Map<String, byte[]>) bundle.getSerializable("images");
				// 拿file
				for (String key : images.keySet()) {

					byte[] imageByte = images.get(key);
					if (key.equals("image_best")) {
						byte[] imageBestData = imageByte;// 这是最好的一张图片
						File bestFile = BitmapUtils.bytes2File(imageBestData,
								Environment.getExternalStorageDirectory().getPath() + File.separator
										+ getActivity().getPackageName() + File.separator + "image",
								XFJRUtil.getCurrentTime() + "temp.jpg");
						// 获取到file后做后续操作
						onSuccessBySdk(bestFile, delta);
						break;
					} else if (key.equals("image_env")) {
						byte[] imageEnvData = imageByte;// 这是一张全景图
					} else {
						// 其余为其他图片，根据需求自取
					}
				}
			} else if (result.contains(getString(R.string.user_check_time_out))) {// 超时失败
				tvResemble.setText(getString(R.string.user_check_error));
				tvCause.setText("失败原因：" + getString(R.string.user_check_time_out));
				onCheckError();
			} else if (result.contains(getString(R.string.liveness_detection_failed_not_video))) { // 活体检测连续性检测失败
				tvResemble.setText(getString(R.string.user_check_error));
				tvCause.setText("失败原因：" + getString(R.string.liveness_detection_failed_not_video));
				onCheckError();
			} else if (result.contains(getString(R.string.liveness_detection_failed_action_blend))) { // 活体检测动作错误
				tvResemble.setText(getString(R.string.user_check_error));
				tvCause.setText("失败原因：" + getString(R.string.liveness_detection_failed_action_blend));
				onCheckError();
			}

		}

	}

	/**
	 * 检测失败
	 */
	private void onCheckError() {
		rightBtn.setText(getString(R.string.over));
		rightBtn.setVisibility(View.VISIBLE);
		// 跳转完成之后显示隐藏
		layout1.setVisibility(View.GONE);
		layout2.setVisibility(View.VISIBLE);
	}

	/**
	 * 检测成功
	 */
	private void onCheckSuccess() {
		rightBtn.setText(getString(R.string.next));
		// 跳转完成之后显示隐藏
		layout1.setVisibility(View.GONE);
		layout2.setVisibility(View.VISIBLE);
	}

	
	private File file;
	/**
	 * 成功后文件操作 ficeid 有源对比
	 * 
	 * @param file
	 */
	private void onSuccessBySdk(File file, String delta) {
		this.file = file;
		HttpRequest.getIdUserInfoByNetWork(getActivity(), delta, file, productBean.getUserName(),
				productBean.getIdCard(), new IHttpCallback<FaceCheckBean>() {

					@Override
					public void onSuccess(String url, FaceCheckBean result) {
						netCheckSuccess(result);
					}

					@Override
					public void onFinal(String url) {
						rightBtn.setVisibility(View.VISIBLE);
						// 不管怎样，都要把结果告诉后台
						sendRequest();
					}

					@Override
					public void onError(String url, Throwable e) {
						LogUtils.e("279 " + e.getMessage());
						onCheckError();
						// 活体检测失败
						tvResemble.setText(getString(R.string.user_check_error));
						// 结束申请
						rightBtn.setText(getString(R.string.over));
						if (TextUtils.isEmpty( e.getMessage())) {
							return;
						}
						// 身份证号码有误
						if (e.getMessage().contains("NO_SUCH_ID_NUMBER")) {
							tvCause.setText("失败原因：身份证号码有误");
						} else if (e.getMessage().contains("ID_NUMBER_NAME_NOT_MATCH")) {
							tvCause.setText("失败原因：身份证号码与姓名不匹配");
						} else {

						}
					}
				});

	}

	private int degree = -1;

	public void getDegree() {
		// 获取activity的对象
		ProductBean productBean = XfjrMainActivity.productBean;
		// 拿到相似度
		String faceSimDegree = productBean.getQz().getFaceSimDegree();
		// double类型
		double simDegree = Double.parseDouble(faceSimDegree);
		// 转为int
		degree = (int) simDegree;
	}

	/**
	 * 网络检测后的结果
	 * 
	 * @param result
	 */
	private void netCheckSuccess(FaceCheckBean result) {
		if (degree == -1) {
			getDegree();
		}
		LogUtils.e("相似度 = " + result.getResult_faceid().getConfidence());
		if (result.getResult_faceid().getConfidence() >= degree) {// 活体检测成功
			// 成功的图片
			ivType.setImageResource(R.drawable.xfjr_check_success);
			// 成功文本
			tvResemble.setText("验证成功");
			// 失败原因 相似度
			mSimilarityDegree = (int) result.getResult_faceid().getConfidence();
			tvCause.setText("相似度" + mSimilarityDegree + "%");
			line.setVisibility(View.VISIBLE);
			aginCheck.setVisibility(View.GONE);
			//onCheckSuccess(); // 
		} else {// 活体检测失败
			// 失败的图片
			ivType.setImageResource(R.drawable.xfjr_check_faile);
			// 失败文本
			tvResemble.setText("相似度" + ((int)result.getResult_faceid().getConfidence()) + "%验证失败");
			// 失败原因
			tvCause.setText("失败原因：人脸活体检测相似度低于" + degree + "%。");
			line.setVisibility(View.VISIBLE);
			onCheckError();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtils.e("FraudDetectionFragment3--->onDestroy");
		threadSwitch = false;
		client.unRegisterLocationListener(l);
		client.stop();
		client = null;
	}
}