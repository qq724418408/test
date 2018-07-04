package com.bocop.xfjr.fragment.qzzc;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boc.jx.baseUtil.view.annotation.CheckNet;
import com.boc.jx.baseUtil.view.annotation.Duplicate;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.httptools.http.decortor.DialogDecorter;
import com.boc.jx.tools.LogUtils;
import com.boc.jx.tools.NetworkUtil;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.customer.XfjrMainActivity;
import com.bocop.xfjr.base.BaseCheckProcessFragment;
import com.bocop.xfjr.bean.FaceIdCardBean;
import com.bocop.xfjr.bean.add.ProductBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.helper.CheckHelper;
import com.bocop.xfjr.helper.CheckHelper.IClickListener;
import com.bocop.xfjr.observer.StepObserver;
import com.bocop.xfjr.observer.StepSubject;
import com.bocop.xfjr.util.BitmapUtils;
import com.bocop.xfjr.util.LimitInputTextWatcher;
import com.bocop.xfjr.util.PatternUtils;
import com.bocop.xfjr.util.PreferencesUtil;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.util.dialog.XfjrDialog;
import com.bocop.xfjr.util.file.FileUtil;
import com.bocop.xfjr.util.image.ImageLoad;
import com.bocop.yfx.utils.ToastUtils;
import com.megvii.idcardlib.IDCardScanActivity;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;
import com.megvii.livenesslib.util.ConUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * description： 欺诈侦测第一个界面 授权 授权页面，涉及到上传和身份证检测
 * <p/>
 * Created by TIAN FENG on 2017年8月28日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public class FraudDetectionFragment1 extends BaseCheckProcessFragment
		implements StepSubject, IHttpCallback<String>, View.OnClickListener, IClickListener {
	// 跳转身份证扫描
	private static final int INTO_IDCARDSCAN_PAGE = 100;
	// 相册
	private static final int PICKTURE_CODE = 1;
	// 相机
	private static final int CAMERA_CODE = 2;
	// 裁剪
	private static final int ZOOM_CODE = 3;

	// 点击按钮的类型
	private enum Type {
		WARRANT, MESSAGE, IDCARDPOSITIVE, IDCARDNEGATIVE
	}

	// 点击按钮的类型
	private Type mClickType;

	private StepObserver mObserver;
	@ViewInject(R.id.ivWarrant)
	private ImageView ivWarrant;// 征信授权书
	@ViewInject(R.id.ivClearnWarrant)
	private ImageView ivClearnWarrant;// 清空征信授权书
	@ViewInject(R.id.ivMessage)
	private ImageView ivMessage;// 信息授权书
	@ViewInject(R.id.ivClearnMessage)
	private ImageView ivClearnMessage;// 清空信息授权书
	@ViewInject(R.id.ivIDcardPositive)
	private ImageView ivIDcardPositive;// 身份证正面
	@ViewInject(R.id.ivClearnIDcardPositive)
	private ImageView ivClearnIDcardPositive;// 清空身份证正面
	@ViewInject(R.id.ivIDcardNegative)
	private ImageView ivIDcardNegative;// 身份证反面
	@ViewInject(R.id.ivClearnIDcardNegative)
	private ImageView ivClearnIDcardNegative;// 清空身份证反面
	@ViewInject(R.id.etName)
	private EditText etName;// 名称
	@ViewInject(R.id.btnClearnEtName)
	private ImageView btnClearnEtName;// 清空名称
	@ViewInject(R.id.etIdNumber)
	private EditText etIdNumber;// 身份证号码
	@ViewInject(R.id.btnClearnIdNumber)
	private ImageView btnClearnIdNumber;// 清空身份证号码
	@ViewInject(R.id.btnLeft)
	private View btnLeft;// 上一步
	@ViewInject(R.id.btnRight)
	private TextView btnRight;
	
	private String sex;
	private AsyncTask aTask;

	private Dialog mScDialog;// 选择照片弹窗
	// 选择后的四张图片
	private File mWarrantFile, mMessageFile, mIDcardPositiveFile, mIDcardNegativeFile;
	private String mFilePath = Environment.getExternalStorageDirectory() + File.separator + "image" + File.separator
			+ "temp";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// mFilePath = FileUtil.createSaveImagePath(getActivity());
		// FileUtil.createTemPath();
		return inflater.inflate(R.layout.xfjr_fragment_fraud_detection1, container, false);
	}

	/**
	 * 下一步
	 */
	@CheckNet
	@Duplicate("FraudDetectionFragment1")
	@OnClick(R.id.btnRight)
	private void clickNext(View view) {
		ProductBean product = ((XfjrMainActivity) getActivity()).productBean;
		// 判断是否有授权图片
		if (mWarrantFile == null) {
			ToastUtils.show(getActivity(), getString(R.string.no_warrant_img), 0);
			return;
		}
		// 判断是否有信息授权图片
		if (mMessageFile == null) {
			ToastUtils.show(getActivity(), getString(R.string.no_message_img), 0);
			return;
		}
		// 判断是否有身份证正面图
		if (mIDcardPositiveFile == null) {
			ToastUtils.show(getActivity(), getString(R.string.no_id_positive_img), 0);
			return;
		}
		// 判断是否有身份证反面图
		if (mIDcardNegativeFile == null) {
			ToastUtils.show(getActivity(), getString(R.string.no_id_negative_img), 0);
			return;
		}
		// 判断姓名是否为空
		if (TextUtils.isEmpty(etName.getText().toString())) {
			ToastUtils.show(getActivity(), getString(R.string.toase_no_name_no_id), 0);
			return;
		}
		// 判断申请人姓名与身份证是否匹配
		XfjrMainActivity activity = (XfjrMainActivity) getActivity();
		if (!etName.getText().toString().trim().equals(activity.productBean.getUserName())) {
			ToastUtils.show(getActivity(), getString(R.string.user_age_1), 0);
			return;
		}
		productBean.setUserName(etName.getText().toString());

		// 判断身份证是否正确
		if (!PatternUtils.is18ByteIdCardComplex(etIdNumber.getText().toString())) {
			ToastUtils.show(getActivity(), getString(R.string.error_id_card), 0);
			return;
		}
		
		// 赋值性别
		sex = XFJRUtil.identifySexCodeByIDCard(etIdNumber.getText().toString());
		
		productBean.setIdCard(etIdNumber.getText().toString());
		
		try {
			if(sex.equals("M")){
				if (XFJRUtil.calAgeByIDCard(etIdNumber.getText().toString()) < Integer.parseInt(product.getMinAge())) {
					ToastUtils.show(getActivity(), getString(R.string.user_age_2), 0);
					return;
				} else if (XFJRUtil.calUpAgeByIDCard(etIdNumber.getText().toString(),product.getPeriods()) >= Integer
						.parseInt(product.getMaxAge())) {
					ToastUtils.show(getActivity(), getString(R.string.user_age_3), 0);
					return;
				}		
			}else {
				if (XFJRUtil.calAgeByIDCard(etIdNumber.getText().toString()) < Integer.parseInt(product.getMinAge2())) {
					ToastUtils.show(getActivity(), getString(R.string.user_age_2), 0);
					return;
				} else if (XFJRUtil.calUpAgeByIDCard(etIdNumber.getText().toString(),product.getPeriods()) >= Integer
						.parseInt(product.getMaxAge2())) {
					ToastUtils.show(getActivity(), getString(R.string.user_age_3), 0);
					return;
				}
			}
			
				
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 都通过，保存身份证信息和姓名
		saveUserInfo();
		if (!XfjrMain.isNet) {
			if (mObserver != null) {
				mObserver.pushBackStack();
				btnRight.setEnabled(false);
			}
			return;
		} else {
			btnRight.setEnabled(false);
			// 发送网络请求
			sendNetWork();
		}

	}

	/**
	 * 保存身份证信息
	 */
	private void saveUserInfo() {
		PreferencesUtil.put(XFJRConstant.KEY_USER_NAME, etName.getText().toString());
		PreferencesUtil.put(XFJRConstant.KEY_USER_IDCARD, etIdNumber.getText().toString());
	}

	/**
	 * 上一步
	 */
	@OnClick(R.id.btnLeft)
	private void clickStep(View view) {
		if (mObserver != null) {
			mObserver.popBackStack();
		}
	}

	@Override
	public boolean onBackClick() {
		// TODO Auto-generated method stub
		return isPush ? false : super.onBackClick();
	}

	@Override
	public void register(StepObserver observer) {
		mObserver = observer;
	}

	@Override
	protected void initData() {
		// 回显逻辑 请求网络file显示,文本信息显示
	}

	// 发送网络请求
	private void sendNetWork() {
		Map<String, Object> params = new HashMap<>();
		params.put("file1", mWarrantFile);// 征信授权书
		params.put("file2", mMessageFile);// 信息授权书
		params.put("file3", mIDcardPositiveFile);// 身份证正面
		params.put("file4", mIDcardNegativeFile);// 身份证反面
		params.put("sex", sex);
		params.put("IDCard", etIdNumber.getText().toString());// 身份证
		params.put("customerName", etName.getText().toString());// 用户名
		// params.put("pictureTime", System.currentTimeMillis());// 当前时间毫秒值
		HttpRequest.sendWarrantMessage(getActivity(), params, this);
	}

	@Override
	protected void initView() {
		// 是否显示上一步按钮
		btnLeft.setVisibility(isPush ? View.VISIBLE : View.GONE);
		// 保存重置按钮显示
		btnRight.setText("验证");
		etName.addTextChangedListener(new LimitInputTextWatcher(etName));
		initEvent();
		// 图片选择的dialog
		mScDialog = new XfjrDialog.Builder(getActivity()).setContentView(R.layout.xfjr_dialog_sc_picture)
				.setWidthAndHeight(-1, -2).setOnClickListener(R.id.picture, this).setOnClickListener(R.id.camera, this)
				.setOnClickListener(R.id.cancle, this).formBottom(true).create();
	}

	private void initEvent() {
		// 文本输入框的监听
		CheckHelper.bindEditText(etName, btnClearnEtName);
		CheckHelper.bindEditText(etIdNumber, btnClearnIdNumber);
		// 图片清空监听
		CheckHelper.bindImage(ivWarrant, ivClearnWarrant, this);
		CheckHelper.bindImage(ivMessage, ivClearnMessage, this);
		CheckHelper.bindImage(ivIDcardNegative, ivClearnIDcardNegative, this);
		CheckHelper.bindImage(ivIDcardPositive, ivClearnIDcardPositive, this);
	}

	/**
	 * 征信授权书点击
	 */
	@OnClick(R.id.ivWarrant)
	private void ivWarrantClick(View view) {
		if (ivClearnWarrant.getVisibility() != View.VISIBLE) {
			if (XFJRUtil.cameraIsCanUse()) {
				mClickType = Type.WARRANT;
				ivWarrant.setEnabled(false);
				temUri = getTempUri();
				startCamera();
			} else {
				ToastUtils.show(getActivity(), getString(R.string.are_you_sure_camera_can_use), 0);
			}
		} else {
			FileUtil.preview(mWarrantFile.getPath(), getActivity());
		}
	}

	/**
	 * 信息授权书点击
	 */
	@OnClick(R.id.ivMessage)
	private void ivMessageClick(View view) {
		if (ivClearnMessage.getVisibility() != View.VISIBLE) {
			if (XFJRUtil.cameraIsCanUse()) {
				mClickType = Type.MESSAGE;
				ivMessage.setEnabled(false);
				temUri = getTempUri();
				startCamera();
			} else {
				ToastUtils.show(getActivity(), getString(R.string.are_you_sure_camera_can_use), 0);
			}
		} else {
			FileUtil.preview(mMessageFile.getPath(), getActivity());
		}
	}

	/**
	 * 省份证正面
	 */
	@OnClick(R.id.ivIDcardPositive)
	@CheckNet
	private void ivIDcardPositiveClick(View view) {
		if (mIDcardPositiveFile == null) {
			if (XFJRUtil.cameraIsCanUse()) {
				mClickType = Type.IDCARDPOSITIVE;
				ivIDcardPositive.setEnabled(false);
				startActivityByIdcard(0);
			} else {
				ToastUtils.show(getActivity(), getString(R.string.are_you_sure_camera_can_use), 0);
			}
		} else {
			FileUtil.preview(mIDcardPositiveFile.getPath(), getActivity());
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mIsCheck = false;
		ivIDcardNegative.setEnabled(true);
		ivIDcardPositive.setEnabled(true);
		ivMessage.setEnabled(true);
		ivWarrant.setEnabled(true);
	}

	/**
	 * 跳转idcard扫描
	 */
	private boolean mIsCheck;// 是否正在检测

	private void startActivityByIdcard(final int side) {
		if (!NetworkUtil.isNetworkAvailable(getActivity())) {
			Toast.makeText(getActivity(), getString(R.string.no_open_network), 0);
			return;
		}
		if (mIsCheck) {
			return;
		}
		mIsCheck = true;
		final Dialog dialog = new DialogDecorter().getDialog(getActivity(), "111");
		aTask = new AsyncTask<Void, Void, Long>() {

			@Override
			protected Long doInBackground(Void... params) {
				Manager manager = new Manager(getActivity());
				IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(getActivity());
				manager.registerLicenseManager(idCardLicenseManager);
				String uuid = ConUtil.getUUIDString(getActivity());
				manager.takeLicenseFromNetwork(uuid);
				String contextStr = manager.getContext(uuid);
				return idCardLicenseManager.checkCachedLicense();
			}

			@Override
			protected void onPostExecute(Long result) {
				if (result > 0) {
					Intent intent = new Intent(getActivity(), IDCardScanActivity.class);
					intent.putExtra("side", side);// 0正面
					intent.putExtra("isvertical", true);
					startActivityForResult(intent, INTO_IDCARDSCAN_PAGE);
					
				} else {// 授权失败
					ToastUtils.show(XfjrMain.mApp, getString(R.string.empower_error), 0);
					mIsCheck = false;
					ivIDcardPositive.setEnabled(true);
					ivIDcardNegative.setEnabled(true);
				}
				// return null;
				super.onPostExecute(result);
				dialog.dismiss();
			}

		}.execute();
	}

	/**
	 * 省份证反面
	 */
	@OnClick(R.id.ivIDcardNegative)
	@CheckNet
	private void ivIDcardNegativeClick(View view) {
		if (mIDcardNegativeFile == null) {
			if (XFJRUtil.cameraIsCanUse()) {
				mClickType = Type.IDCARDNEGATIVE;
				// mScDialog.show();
				ivIDcardNegative.setEnabled(false);
				startActivityByIdcard(1);
			} else {
				ToastUtils.show(getActivity(), getString(R.string.are_you_sure_camera_can_use), 0);
			}
		} else {
			FileUtil.preview(mIDcardNegativeFile.getPath(), getActivity());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.picture:
			// 跳相册
			temUri = getTempUri();
			startPickture();
			mScDialog.dismiss();
			break;
		case R.id.camera:
			// 跳相机
			temUri = getTempUri();
			startCamera();
			mScDialog.dismiss();
			break;
		case R.id.cancle:
			// 取消
			mScDialog.dismiss();
			break;
		}
	}

	/**
	 * 跳转相册
	 */
	private void startPickture() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
		getActivity().startActivityForResult(intent, PICKTURE_CODE);
	}

	/**
	 * 跳相机
	 */
	private void startCamera() {
		// 判断sdcard是否存在
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				// qwe为相片保存的文件夹名
				File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "image");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				// asd为保存的相片名
				File f = new File(dir, "temp");// localTempImgDir和localTempImageFileName是自己定义的名字
				Uri u = Uri.fromFile(f);
				
				try {// 尽可能调用系统相机  
		            String cameraPackageName = getCameraPhoneAppInfos(getActivity());  
		            if (cameraPackageName == null) {  
		                cameraPackageName = "com.android.camera";  
		            }  
		            final Intent intent_camera = getActivity().getPackageManager()  
		                    .getLaunchIntentForPackage(cameraPackageName);  
		            if (intent_camera != null) {  
		            	intent.setPackage(cameraPackageName);  
		            }  
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }  
				
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				getActivity().startActivityForResult(intent, CAMERA_CODE);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 对使用系统拍照的处理    
	public String getCameraPhoneAppInfos(Activity context) {  
	    try {  
	        String strCamera = "";  
	        List<PackageInfo> packages = context.getPackageManager()  
	                .getInstalledPackages(0);  
	        for (int i = 0; i < packages.size(); i++) {  
	            try {  
	                PackageInfo packageInfo = packages.get(i);  
	                String strLabel = packageInfo.applicationInfo.loadLabel(  
	                        context.getPackageManager()).toString();  
	                // 一般手机系统中拍照软件的名字  
	                if ("相机,照相机,照相,拍照,摄像,美图,Camera,camera".contains(strLabel)) {  
	                    strCamera = packageInfo.packageName;  
	                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {  
	                        break;  
	                    }  
	                }  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        if (strCamera != null) {  
	            return strCamera;  
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return null;  
	}  

	/**
	 * 跳裁剪
	 */
	private void startPhotoZoom(Uri uri) {
		int dp = 500;
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		// aspectX aspectY 是宽高的比例 ，注释后可以自由选择裁剪区域
		intent.putExtra("aspectX", 5);// 输出是X方向的比例
		intent.putExtra("aspectY", 3);
		// intent.putExtra("outputX", dp);// 输出X方向的像素
		// intent.putExtra("outputY", dp);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, temUri);
		intent.putExtra("return-data", false);// 设置为不返回数据
		getActivity().startActivityForResult(intent, ZOOM_CODE);
	}

	private Uri temUri;

	/**
	 * 裁剪后保存的uri
	 */
	private Uri getTempUri() {
		String uriPath = "file://" + File.separator + FileUtil.createSaveImagePath(getActivity());
		String fileName = "/" + XFJRUtil.getCurrentTime() + ".jpg";
		LogUtils.e(uriPath + fileName);
		String  path = uriPath + fileName;
		if(path.contains("////")){
			path.replace("////", "///");
		}
		return Uri.parse(path);
	}

	/**
	 * 结果回调
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {// 基类空指针异常
			super.onActivityResult(requestCode, resultCode, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resultCode == Activity.RESULT_OK) {
			onResult(requestCode, data);
		}
	}

	/**
	 * 成功结果处理
	 * 
	 * @param requestCode
	 * @param data
	 */
	private void onResult(int requestCode, Intent data) {
		Uri uri = null;
		try {
			uri = data.getData();
			LogUtils.e("uri343 = " + uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		switch (requestCode) {
		case PICKTURE_CODE:
			if (null != data) {// 为了取消选取不报空指针用
				startPhotoZoom(uri);
			}
			break;
		case CAMERA_CODE:
			// 获取图片的选转角度
			int degree = BitmapUtils.readPictureDegree(mFilePath);
			// 获取旋转后的图片
			Bitmap bitmap = BitmapUtils.rotaingImage(degree, mFilePath);
			onBitmapCallResult(bitmap, temUri);
			// 图片转文件
			// File f = BitmapUtils.bitmap2File(bitmap, mFilePath);
			// try {
			// uri =
			// Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
			// f.getAbsolutePath(), null, null));
			// startPhotoZoom(uri);
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
			// }
			break;
		case ZOOM_CODE:
			Bitmap bmp = BitmapUtils.getBitmapFormUri(getActivity(), temUri, 320, 640);
			onBitmapCallResult(bmp, temUri);
			break;

		case INTO_IDCARDSCAN_PAGE:// 返回身份证扫描
			sendRequestIdCard(data);
			mIsCheck = false;
			break;
		}
	}

	/**
	 * 拿到身份证结果后发送网络请求
	 */
	private void sendRequestIdCard(Intent data) {
		// 是否是正面
		int isIdPostive = data.getIntExtra("side", 0);
		byte[] imageByte = data.getByteArrayExtra("idcardImg");
		String eSDPath = Environment.getExternalStorageDirectory().getPath();
		String pkg = getActivity().getPackageName();
		File idCardFile = BitmapUtils.bytes2File(imageByte, eSDPath + File.separator + pkg + File.separator + "image",
				XFJRUtil.getCurrentTime() + "idCard.jpg");
		onBitmapCallResult(BitmapFactory.decodeFile(idCardFile.getAbsolutePath()), Uri.fromFile(idCardFile));
		// 反面不需要请求
		if (isIdPostive != 0) {
			return;
		}
		// 正面需要请求出姓名和身份证号
		HttpRequest.getIdCardByNetWork(getActivity(), idCardFile, new IHttpCallback<FaceIdCardBean>() {

			@Override
			public void onSuccess(String url, FaceIdCardBean result) {
				LogUtils.e("onSuccess = " + result);
				etName.setText(result.getName());
				etIdNumber.setText(result.getId_card_number());
				/**
				 * TODO 后续此行可删
				 */
				saveUserInfo();
			}

			@Override
			public void onError(String url, Throwable e) {
				LogUtils.e("onError = " + e.getMessage());
			}

			@Override
			public void onFinal(String url) {

			}
		});

	}

	/**
	 * 最终图片结果
	 * 
	 * @param bitmap
	 * @param uri
	 */
	private void onBitmapCallResult(Bitmap bitmap, Uri uri) {
		if (mClickType == null) {
			return;
		}
		switch (mClickType) {
		case WARRANT:// 授权
//			ivWarrant.setImageBitmap(BitmapUtils.getCircleBitmap(bitmap));
			 ImageLoad.loadImage(getActivity(), uri.getPath(), ivWarrant);
			// ImageOption.Builder().setRadius(8).build());
			ivClearnWarrant.setVisibility(View.VISIBLE);
			mWarrantFile = BitmapUtils.bitmap2File(bitmap, uri.getPath());
			break;
		case MESSAGE:// 信息
//			ivMessage.setImageBitmap(BitmapUtils.getCircleBitmap(bitmap));
			 ImageLoad.loadImage(getActivity(),uri.getPath(), ivMessage);
			// ImageOption.Builder().setRadius(8).build());
			ivClearnMessage.setVisibility(View.VISIBLE);
			mMessageFile = BitmapUtils.bitmap2File(bitmap, uri.getPath());
			break;
		case IDCARDPOSITIVE:// 身份证正面
//			ivIDcardPositive.setImageBitmap(BitmapUtils.getCircleBitmap(bitmap));
			 ImageLoad.loadImage(getActivity(),uri.getPath(),ivIDcardPositive);
			// ImageOption.Builder().setRadius(8).build());
			ivClearnIDcardPositive.setVisibility(View.VISIBLE);
			mIDcardPositiveFile = BitmapUtils.bitmap2File(bitmap, uri.getPath());
			break;
		case IDCARDNEGATIVE:// 身份证反面
//			ivIDcardNegative.setImageBitmap(BitmapUtils.getCircleBitmap(bitmap));
			ImageLoad.loadImage(getActivity(), uri.getPath(),ivIDcardNegative);
			// ImageLoad.loadImage(getActivity(), uri.toString(),
			// ivIDcardNegative, new
			// ImageOption.Builder().setRadius(8).build());
			ivClearnIDcardNegative.setVisibility(View.VISIBLE);
			mIDcardNegativeFile = BitmapUtils.bitmap2File(bitmap, uri.getPath());
			break;
		}
	}

	/**
	 * 执行成功
	 */
	@Override
	public void onSuccess(String url, String result) {
		LogUtils.e("result = " + result);
		// 提交授权信息
		// if (UrlConfig.WARRANT_SEND_URL.equals(url)) {
		if (mObserver != null) {
			FileUtil.delAllImages(getActivity()); // 删除所有图片缓存
			//((XfjrMainActivity) getActivity()).sendBR(0);
			// 跳转下一个页面
			mObserver.pushBackStack();
			btnRight.setEnabled(false);
			ivClearnWarrant.performClick();
			ivClearnMessage.performClick();
			ivClearnIDcardNegative.performClick();
			ivClearnIDcardPositive.performClick();
			// 图片清空监听
			System.gc();
		}
		// }
		// 还有回显接口逻辑处理
	}

	/**
	 * 执行失败
	 */
	@Override
	public void onError(String url, Throwable e) {
		LogUtils.e("error = " + e.getMessage());
		e.printStackTrace();
//		ToastUtils.show(getActivity(), "网络请求失败", 0);
		UrlConfig.showErrorTips(getActivity(), e, true);
		if (e.getMessage().contains(UrlConfig.dynamicUrlExceptionCode)) {
			XFJRUtil.autoLogin(getActivity());
		}
	}

	/**
	 * 一定进入
	 */
	@Override
	public void onFinal(String url) {
		btnRight.setEnabled(true);
	}

	/**
	 * 图片清空按钮点击事件回掉
	 */
	@Override
	public void click(int viewId) {
		switch (viewId) {
		case R.id.ivClearnWarrant:// 授权清空
			mWarrantFile = null;
			break;
		case R.id.ivClearnMessage:// 信息授权清空
			mMessageFile = null;
			break;
		case R.id.ivClearnIDcardNegative:// 身份证反面
			mIDcardNegativeFile = null;
			break;
		case R.id.ivClearnIDcardPositive:// 身份证正面
			mIDcardPositiveFile = null;
			break;
		}
	}

	/**
	 * 可以删除方便测试////////////////////////////////////////////////
	 */
	@Override
	protected boolean saveClick(View view) {
		String userName = PreferencesUtil.get(XFJRConstant.KEY_USER_NAME, "");
		etName.setText(userName);
		String userId = PreferencesUtil.get(XFJRConstant.KEY_USER_IDCARD, "");
		etIdNumber.setText(userId);
		return super.saveClick(view);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (aTask != null) {
			aTask.cancel(true);
		}
	}

	/**
	 * 空白处点击事件
	 * 
	 * @param view
	 */
	@OnClick(R.id.lltBlank)
	private void clickBlank(View view) {
		XFJRUtil.hideSoftInput(view);
	}
}