package com.bocop.xfjr.fragment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.boc.jx.baseUtil.view.annotation.CheckNet;
import com.boc.jx.baseUtil.view.annotation.Duplicate;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.XfjrIndexActivity;
import com.bocop.xfjr.base.BaseCheckProcessFragment;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.observer.StepObserver;
import com.bocop.xfjr.observer.StepSubject;
import com.bocop.xfjr.util.BitmapUtils;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.util.dialog.XfjrDialog;
import com.bocop.xfjr.util.file.FileUtil;
import com.bocop.xfjr.util.image.ImageLoad;
import com.bocop.xfjr.view.RoundImageView;
import com.bocop.yfx.utils.ToastUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 跳转到补充资料的时候需要给XfjrMain.businessStatus赋值
 * 
 * description： 补充资料
 */
public class FurtherInfoFragment extends BaseCheckProcessFragment implements View.OnClickListener, StepSubject {

	// 相册
	private static final int PICKTURE_CODE = 0x0001;
	// 相机
	private static final int CAMERA_CODE = 0x0002;
	// 裁剪
	private static final int ZOOM_CODE = 0x0003;
	private StepObserver mObserver;
	@ViewInject(R.id.btnLeft)
	private Button btnLeft;
	@ViewInject(R.id.btnRight)
	private Button btnRight;
	@ViewInject(R.id.tvTip)
	private TextView tvTip; // 两种情况
	@ViewInject(R.id.tvPhoto1)
	private TextView tvPhoto1; // 分期申请书（财力证明）
	@ViewInject(R.id.ivPhoto1)
	private RoundImageView ivPhoto1;
	@ViewInject(R.id.ivPhoto2)
	private RoundImageView ivPhoto2;
	@ViewInject(R.id.ivDelete1)
	private ImageView ivDelete1;
	@ViewInject(R.id.ivDelete2)
	private ImageView ivDelete2;
	@ViewInject(R.id.fltPhoto2)
	private View fltPhoto2; // 第二张照片
	private Dialog mScDialog;// 选择照片弹窗
	private String photoNo = "first"; // 照片序号
	private String path;
	private File photoFile1; // 照片路径
	private File photoFile2; // 照片路径
	// private String status = "1"; // 转人工、待放款

	@Override
	protected void initView() {
		super.initView();
		if (XfjrMain.businessStatus.equals("3")) { // 待放款
			tvTip.setText("您的终审已通过，请补充分期申请书。");
			tvPhoto1.setText("分期申请书");
			fltPhoto2.setVisibility(View.INVISIBLE);
		} else if (XfjrMain.businessStatus.equals("1")) { // 转人工
			tvPhoto1.setText("财力证明");
			fltPhoto2.setVisibility(View.VISIBLE);
			tvTip.setText("您的申请不符合预审批标准，请补充相关资料，转入人工决策。");
		}
		// setVisibility(tvReset, View.VISIBLE);
		// setVisibility(tvSave, View.VISIBLE);
	}

	@Override
	public void onResetClick(View view) {
		super.onResetClick(view);
		photoFile1 = null;
		photoFile2 = null;
		ivDelete1.setVisibility(View.GONE);
		ivDelete2.setVisibility(View.GONE);
		ivPhoto1.setImageResource(R.drawable.xfjr_pretrial_photo);
		ivPhoto2.setImageResource(R.drawable.xfjr_pretrial_photo);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return view = inflater.inflate(R.layout.xfjr_fragment_further_info, container, false);
	}

	@Override
	protected void initData() {
		super.initData();
		FileUtil.createSaveImagePath(getActivity());
		setRightBtnText("提交");
//		String eSDPath = Environment.getExternalStorageDirectory().getPath();
//		String pkg = getActivity().getPackageName();
		path = Environment.getExternalStorageDirectory() + File.separator + "image" + File.separator
				+ "temp";
		mScDialog = new XfjrDialog.Builder(getActivity()).setContentView(R.layout.xfjr_dialog_sc_picture)
				.setWidthAndHeight(-1, -2).setOnClickListener(R.id.picture, this).setOnClickListener(R.id.camera, this)
				.setOnClickListener(R.id.cancle, this).formBottom(true).create();
	}

	/**
	 * 提交
	 * 
	 * @param view
	 */
	@OnClick(R.id.btnRight)
	@CheckNet
	@Duplicate("FurtherInfoFragment.java")
	private void clickNext(View view) {
		Map<String, Object> params = new HashMap<>();
		if (mObserver != null) {
			if (XfjrMain.businessStatus.equals("3")) { // 预审通过
				if (photoFile1 == null) {
					ToastUtils.show(getActivity(), "请上传分期申请书", 0);
				} else {
					params.put("file3", photoFile1);
					commit(params);
				}
			} else if (XfjrMain.businessStatus.equals("1")) {
				if (photoFile1 == null) {
					ToastUtils.show(getActivity(), "请上传财力证明", 0);
				} else if (photoFile2 == null) {
					ToastUtils.show(getActivity(), "请上传收入证明", 0);
				} else {
					params.put("file1", photoFile1);
					params.put("file2", photoFile2);
					commit(params);
				}
			}
		}
	}

	/**
	 * 补充资料提交
	 * 
	 * @param params
	 */
	private void commit(Map<String, Object> params) {
		if (XfjrMain.isNet) {
			HttpRequest.addData(getActivity(), params, new IHttpCallback<String>() {

				@Override
				public void onSuccess(String url, String result) {
					//ToastUtils.show(getActivity(), "提交成功", 0);
					FileUtil.delAllImages(getActivity()); // 删除所有图片缓存
					Intent intent = new Intent(getActivity(), XfjrIndexActivity.class);
					getActivity().startActivity(intent);
					getActivity().finish();
				}

				@Override
				public void onFinal(String url) {
				}

				@Override
				public void onError(String url, Throwable e) {
					UrlConfig.showErrorTips(getActivity(), e, true);
					//ToastUtils.show(getActivity(), "提交不成功", 0);
//					String code = e.getMessage();
//					if (code != null) {
//						LogUtils.e("提交不成功：" + e.getMessage());
//						if (code.equals(UrlConfig.dynamicUrlExceptionCode)) {
//							LogUtils.e("dynamic url exception:don't find this APP_ID");
//							XFJRUtil.autoLogin();
//						}
//					}
				}
			});
		} else {
			ToastUtils.show(getActivity(), "提交成功", 0);
//			Intent intent = new Intent(XFJRConstant.ACTION_UPDATE_DATA);
//			intent.putExtra(XFJRConstant.KEY_BUSINESS_STATUS, Integer.parseInt(XfjrMain.businessStatus));
//			intent.putExtra(XFJRConstant.ACTION_FLAG_INT, ((XfjrMainActivity) getActivity()).productBean.getFrom());
//			getActivity().sendBroadcast(intent);
			Intent intent = new Intent(getActivity(), XfjrIndexActivity.class);
			getActivity().startActivity(intent);
			getActivity().finish();
		}
	}

	@OnClick(R.id.ivDelete1)
	private void clickDeletePhoto1(View view) {
		photoFile1 = null;
		ivDelete1.setVisibility(View.GONE);
		ivPhoto1.setImageResource(R.drawable.xfjr_pretrial_photo);
	}

	@OnClick(R.id.ivDelete2)
	private void clickDeletePhoto2(View view) {
		photoFile2 = null;
		ivDelete2.setVisibility(View.GONE);
		ivPhoto2.setImageResource(R.drawable.xfjr_pretrial_photo);
	}

	@OnClick(R.id.fltChoosePhoto1)
	private void clickChoosePhoto1(View view) {
		if (ivDelete1.getVisibility() == View.GONE) {
			photoNo = "first";
			//mScDialog.show();
			temUri = getTempUri();
			startCamera();
		} else {
			FileUtil.preview(photoFile1.getPath(), getActivity());
			// ToastUtils.show(getActivity(), "预览图片", 0);
		}
	}

	@OnClick(R.id.fltChoosePhoto2)
	private void clickChoosePhoto2(View view) {
		if (ivDelete2.getVisibility() == View.GONE) {
			photoNo = "second";
//			mScDialog.show();
			temUri = getTempUri();
			startCamera();
		} else {
			FileUtil.preview(photoFile2.getPath(), getActivity());
			// ToastUtils.show(getActivity(), "预览图片", 0);
		}
	}

	/**
	 * 设置单个按钮的文字（隐藏左边按钮）
	 * 
	 * @param right
	 */
	private void setRightBtnText(String right) {
		btnLeft.setVisibility(View.GONE);
		btnRight.setText(right);
	}

	@Override
	public void register(StepObserver observer) {
		mObserver = observer;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.picture:
			temUri = getTempUri();
			startPickture();
			mScDialog.dismiss();
			break;
		case R.id.camera:
			temUri = getTempUri();
			startCamera();
			mScDialog.dismiss();
			break;
		case R.id.cancle:
			mScDialog.dismiss();
			break;
		}
	}

	/**
	 * 结果回调
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_CODE) {
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
			int degree = BitmapUtils.readPictureDegree(path);
			// 获取旋转后的图片
			Bitmap bitmap = BitmapUtils.rotaingImage(degree, path);
			onBitmapCallResult(bitmap, temUri);
			// 图片转文件
//			File f = BitmapUtils.bitmap2File(bitmap, path);
//			try {
//				uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
//						f.getAbsolutePath(), null, null));
//				startPhotoZoom(uri);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
			break;
		case ZOOM_CODE:
			try {
				Bitmap bmp = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(temUri));
				onBitmapCallResult(bmp, temUri);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	/**
	 * 最终图片结果
	 * 
	 * @param bitmap
	 */
	private void onBitmapCallResult(Bitmap bitmap, Uri uri) {
		if (photoNo == null) {
			return;
		}
		switch (photoNo) {
		case "first":
			// ivPhoto1.setImageBitmap(bitmap);
			ImageLoad.loadImage(getActivity(), uri.getPath(), ivPhoto1);
			//ImageLoad.loadImage(getActivity(), uri.toString(), ivPhoto1, new ImageOption.Builder().setRadius(8).build());
//			ivPhoto1.setImageBitmap(BitmapUtils.getCircleBitmap(bitmap));			
			ivDelete1.setVisibility(View.VISIBLE);
			photoFile1 = BitmapUtils.bitmap2File(bitmap, uri.getPath());
			break;
		case "second":
			// ivPhoto2.setImageBitmap(bitmap);
			ImageLoad.loadImage(getActivity(), uri.getPath(), ivPhoto2);
			//ImageLoad.loadImage(getActivity(), uri.toString(), ivPhoto2, new ImageOption.Builder().setRadius(8).build());
//			ivPhoto2.setImageBitmap(BitmapUtils.getCircleBitmap(bitmap));			
			ivDelete2.setVisibility(View.VISIBLE);
			photoFile2 = BitmapUtils.bitmap2File(bitmap, uri.getPath());
			break;
		}
	}

	/**
	 * 跳转相册
	 */
	@Deprecated
	private void startPickture() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent, PICKTURE_CODE);
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
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				startActivityForResult(intent, CAMERA_CODE);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 跳裁剪
	 */
	private void startPhotoZoom(Uri uri) {
		// int dp = 300;
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 5);// 输出是X方向的比例
		intent.putExtra("aspectY", 3);
		// intent.putExtra("outputX", dp);// 输出X方向的像素
		// intent.putExtra("outputY", dp);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, temUri);
		intent.putExtra("return-data", false);// 设置为不返回数据
		startActivityForResult(intent, ZOOM_CODE);
	}

	private Uri temUri;

	/**
	 * 裁剪后保存的uri
	 */
	private Uri getTempUri() {
		String externalStorageDirectory = Environment.getExternalStorageDirectory().getPath();
		String filePath = File.separator + getActivity().getPackageName() + File.separator + "image";
		String uriPath = "file://" + File.separator + externalStorageDirectory + filePath;
		String fileName = File.separator + XFJRUtil.getCurrentTime() + ".jpg";
		LogUtils.e(uriPath + fileName);
		return Uri.parse(uriPath + fileName);
	}
	
}