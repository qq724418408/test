package com.bocop.kht.utils.fileupload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.httptools.http.decortor.DialogDecorter;
import com.boc.jx.tools.LogUtils;
import com.boc.jx.tools.NetworkUtil;
import com.bocop.kht.bean.CameraBean;
import com.bocop.kht.config.LifeBaseConfig;
import com.bocop.kht.constants.LifeConstants;
import com.bocop.kht.utils.LifeUtil;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.bean.FaceCheckBean;
import com.bocop.xfjr.bean.FaceIdCardBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.util.BitmapUtils;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.yfx.utils.ToastUtils;
import com.megvii.idcardlib.IDCardScanActivity;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.megvii.livenesslib.LivenessActivity;
import com.megvii.livenesslib.util.ConUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaxinqing
 *         android:configChanges="keyboardHidden|orientation|screenSize"//
 */
public class ImageUpload {

	private static final String TAG = "ImageUpload: ";

	public ImageUpload() {

	}

	private String filePath;
	private CameraBean cameraBean;
	// private double qua;
	private long fileSize;
	private int parsew;
	private int parseh;
	private int compressHeight;
	private int compressWidth;
	private int thumbHeight;
	private int thumbWidth;
	private Activity activity;
	private ImageUploadCallBack callBack;
	private String imaType;
	private String sourceType;
	private String source;
	private Uri fileUri;
	private String timeStamp;
	private static String imageFile = "";
	private static int imageFileCount = 0;

	public void start(Activity activity, String jsMsg, String saveFilePath, boolean isCompressed,
			ImageUploadCallBack callBack) {

		if (activity == null || TextUtils.isEmpty(jsMsg) || TextUtils.isEmpty(saveFilePath) || callBack == null) {
			throw new NullPointerException("All the parameters cannot be empty");
		}
		if (!checkJson(jsMsg)) {
			callBack.errorMsg(ResultCode.OTHER_ERROR, "json error");
			return;
		}
		this.filePath = saveFilePath;
		try {
			cameraBean = JSON.parseObject(jsMsg, CameraBean.class);
		} catch (Exception e) {
			callBack.errorMsg(ResultCode.OTHER_ERROR, e.toString());
		}
		this.activity = activity;
		this.callBack = callBack;
		choosestartup(isCompressed);
	}

	public void stop(Context context, int requestCode, int resultCode, Intent data, boolean isCompressed) {
		if (resultCode == Activity.RESULT_OK) {
			String picturePath = LifeUtil.getCallBackFilePath(context, requestCode, resultCode, data, fileUri);

			if (!TextUtils.isEmpty(picturePath) && requestCode == LifeConstants.REQUEST_CAMERA
					|| requestCode == LifeConstants.REQUEST_PHOTO
							| requestCode == LifeBaseConfig.CONTENT_REQUEST_CODE) {

				switch (requestCode) {
				case LifeConstants.REQUEST_CAMERA:
					source = "c";
					compressPhoto(picturePath, isCompressed);
					break;

				case LifeConstants.REQUEST_PHOTO:
					source = "p";
					if (!TextUtils.isEmpty(picturePath)) {
						compressPhoto(picturePath, isCompressed);
					}
					break;
				case LifeBaseConfig.CONTENT_REQUEST_CODE:
					source = "p";
					if (!TextUtils.isEmpty(picturePath)) {
						compressPhoto(picturePath, isCompressed);
					}
					break;
				}
			}
		}
	}

	public CameraBean getCameraBean() {
		return cameraBean;
	}

	private void compressPhoto(String picturePath, boolean isCompressed) {
		new compressPicture().execute(picturePath);
	}

	private void choosestartup(boolean isCompressed) {
		if (cameraBean == null) {
			callBack.errorMsg(ResultCode.OTHER_ERROR, "json format error");
			return;
		}
		if (!checkType(cameraBean.getType())) {
			callBack.errorMsg(ResultCode.OTHER_ERROR, "拍照类型错误");
			return;
		}
		if (!TextUtils.isEmpty(cameraBean.getSourceType())) {
			sourceType = cameraBean.getSourceType();
			switch (sourceType) {
			case "p":
				if (checkData(isCompressed)) {
					startPicture();
				}
				break;
			case "c":
				if (checkData(isCompressed)) {
					startCamera();
				}
				break;
			default:
				callBack.errorMsg(ResultCode.IMAGE_SOURCE_TYPE_ERROR, "Source Type Error");
				break;
			}
		} else {
			if (checkData(isCompressed)) {
				getChooseImgMethod();
			}
		}
	}

	private void startPicture() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		activity.startActivityForResult(intent, LifeConstants.REQUEST_PHOTO);
	}

	private boolean mIsCheck = false;// 是否正在检测
	private AsyncTask aTask;
	private boolean isIdCard;
	public String name;
	public String idCard;
	private String delta;
	private String confidence;

	public void startCamera() {
		if (!NetworkUtil.isNetworkAvailable(activity)) {
			Toast.makeText(activity, "当前无网络~", Toast.LENGTH_SHORT).show();
			return;
		}
		if (mIsCheck) {
			return;
		}
		mIsCheck = true;
		String sdState = Environment.getExternalStorageState();
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {
			// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// fileUri = Uri.fromFile(getOutputMediaFile(1));
			// intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			// activity.startActivityForResult(intent, LifeConstants.REQUEST_CAMERA);
			switch (cameraBean.getType()) {
			case "1":
				isIdCard = true;
				isScanFront(0);
				break;
			case "2":
				isIdCard = true;
				isScanFront(1);
				break;
			case "3":
				isIdCard = false;
				if (TextUtils.isEmpty(name) || TextUtils.isEmpty(idCard)) {
					callBack.errorMsg(ResultCode.FACE_DEFEAT, "请先进行身份证正面识别");
					mIsCheck = false;
				} else {
					startCheckActivity();
				}
				break;
			}

		} else {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(activity, "内存卡异常", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	public void isScanFront(final int side) {
		aTask = new AsyncTask<Void, Void, Long>() {

			@Override
			protected Long doInBackground(Void... params) {
				Manager manager = new Manager(activity);
				IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(activity);
				manager.registerLicenseManager(idCardLicenseManager);
				String uuid = ConUtil.getUUIDString(activity);
				manager.takeLicenseFromNetwork(uuid);
				String contextStr = manager.getContext(uuid);
				return idCardLicenseManager.checkCachedLicense();
			}

			@Override
			protected void onPostExecute(Long result) {
				if (result > 0) {
					Intent intent = new Intent(activity, IDCardScanActivity.class);
					intent.putExtra("side", side);// 0正面
					intent.putExtra("isvertical", true);
					activity.startActivityForResult(intent, LifeConstants.REQUEST_CAMERA);
				} else {// 授权失败
					callBack.errorMsg(ResultCode.FACE_DEFEAT, "授权失败");
					mIsCheck = false;
				}
				super.onPostExecute(result);
			}

		}.execute();

	}

	/**
	 * 成功结果处理
	 * 
	 * @param requestCode
	 * @param data
	 */
	public void onResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			source = "c";
			switch (requestCode) {
			case LifeConstants.REQUEST_CAMERA:// 返回身份证扫描
				byte[] imageByte = data.getByteArrayExtra("idcardImg");
				String eSDPath = Environment.getExternalStorageDirectory().getPath();
				String pkg = activity.getPackageName();
				final File idCardFile = BitmapUtils.bytes2File(imageByte,
						eSDPath + File.separator + pkg + File.separator + "image",
						XFJRUtil.getCurrentTime() + "idCard.jpg");
				compressPhoto(idCardFile.getAbsolutePath(), true);
				break;
			case LifeConstants.CHECK_KEY_CODE:
				onActivityResult(data);
				break;
			}
		} else {
			callBack.errorMsg(ResultCode.FACE_DEFEAT, "图片获取失败");
			mIsCheck = false;
		}
	}

	public String formatBirthDay(String year, int month, int day) {
		return year + String.format("%02d", month) + String.format("%02d", day);
	}

	/**
	 * 拿到身份证结果后发送网络请求
	 */
	private void sendRequestIdCard(final File idCardFile, final String fileName, final String imgSize,
			final String extraInfo) {
		// 正面需要请求出姓名和身份证号
		HttpRequest.getIdCardByNetWork(activity, idCardFile, new IHttpCallback<FaceIdCardBean>() {
			@Override
			public void onSuccess(String url, FaceIdCardBean result) {
				LogUtils.e("onSuccess = " + result);
				if ("front".equals(result.getSide())) {
					name = result.getName();
					idCard = result.getId_card_number();
				}
				callBack.onFinish(idCardFile, fileName, imgSize, extraInfo, result);
			}

			@Override
			public void onError(String url, Throwable e) {
				callBack.errorMsg(ResultCode.FACE_DEFEAT, "识别身份证信息失败");
			}

			@Override
			public void onFinal(String url) {
				mIsCheck = false;
			}
		});

	}

	private void showFileChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
		} else {
			intent.setAction(Intent.ACTION_GET_CONTENT);
		}

		try {
			activity.startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的TIFF文件"),
					LifeBaseConfig.CONTENT_REQUEST_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(activity, "未找到相关应用市场", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	private void getChooseImgMethod() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				String[] chooserString = { "相机", "从相册中新增照片" };
				showSelectDialog(activity, "", chooserString, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							startCamera();
							break;
						case 1:
							startPicture();
							break;
						case 2:
							showFileChooser();
							break;
						}
					}
				});
			}
		});

	}

	private AlertDialog showSelectDialog(Context context, String title, String[] items,
			OnClickListener onClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setItems(items, onClickListener);
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		return builder.show();
	}

	private boolean checkData(boolean isCompressed) {
		imaType = cameraBean.getImageFormat();
		if (!TextUtils.isEmpty(imaType)) {
			switch (imaType.toLowerCase()) {
			case "jpg":
				break;
			case "png":
				break;
			case "default":
				break;
			default:
				callBack.errorMsg(ResultCode.IMAGE_FORMAT_ERROR, "Image Format Error");
				return false;
			}
		}
		compressHeight = Integer.parseInt(cameraBean.getCompressHeight());
		compressWidth = Integer.parseInt(cameraBean.getCompressWidth());
		thumbHeight = Integer.parseInt(cameraBean.getThumbnailHeight());
		thumbWidth = Integer.parseInt(cameraBean.getThumbnailWidth());
		String imageFile = cameraBean.getImageFile();
		String size = cameraBean.getFileSize();
		try {
			if (TextUtils.isEmpty(size)) {
				fileSize = 6 * 1024 * 1024L;
			} else {
				fileSize = (long) (Double.parseDouble(size) * 1024 * 1024);
				if (fileSize <= 0L) {
					callBack.errorMsg(ResultCode.OTHER_ERROR, "Image Size Error");
					return false;
				}
			}
			if (!TextUtils.isEmpty(imageFile)) {
				String regular = "[a-zA-Z_0-9]+";
				Pattern p = Pattern.compile(regular);
				Matcher m = p.matcher(imageFile);
				if (!m.matches()) {
					callBack.errorMsg(ResultCode.IMAGE_NAME_ERROR, "Image Name Error");
					return false;
				}
			}

			if (compressWidth != 0 && compressHeight != 0) {
				if (compressWidth > 1920) {
					callBack.errorMsg(ResultCode.IMAGE_WIDTH_ERROR, "Image Width Error");
					return false;
				}
				if (compressHeight > 1440) {
					callBack.errorMsg(ResultCode.IMAGE_HEIGHT_ERROR, "Image Height Error");
					return false;
				}
				if (compressWidth < 0) {
					callBack.errorMsg(ResultCode.IMAGE_WIDTH_ERROR, "Image Width Error");
					return false;
				}
				if (compressHeight < 0) {
					callBack.errorMsg(ResultCode.IMAGE_HEIGHT_ERROR, "Image Height Error");
					return false;
				}
			} else if (thumbWidth != 0 && thumbHeight != 0) {
				if (thumbWidth > 1920) {
					callBack.errorMsg(ResultCode.IMAGE_WIDTH_ERROR, "Image Width Error");
					return false;
				}
				if (thumbHeight > 1440) {
					callBack.errorMsg(ResultCode.IMAGE_HEIGHT_ERROR, "Image Height Error");
					return false;
				}
				if (thumbWidth < 0) {
					callBack.errorMsg(ResultCode.IMAGE_WIDTH_ERROR, "Image Width Error");
					return false;
				}
				if (thumbHeight < 0) {
					callBack.errorMsg(ResultCode.IMAGE_HEIGHT_ERROR, "Image Height Error");
					return false;
				}
			} else {
				parsew = 0;
				parseh = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			callBack.errorMsg(ResultCode.OTHER_ERROR, "Unknown Error");
			return false;

		}
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	private File getOutputMediaFile(int type) {
		// "/A_Test_Camera/"
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + filePath);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		File mediaFile;
		if (type == 1) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	private boolean checkJson(String value) {
		try {
			new JSONObject(value);
		} catch (JSONException e) {
			return false;
		}
		return true;
	}

	String fileName = "";

	class compressPicture extends AsyncTask<String, Void, File> {

		@Override
		protected File doInBackground(String... params) {
			callBack.onStart(cameraBean);
			File file = new File(params[0]);
			timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			if (imageFile.equals(timeStamp)) {
				imageFileCount++;
			} else {
				imageFileCount = 0;
			}
			timeStamp = timeStamp
					+ (String.valueOf(imageFileCount).length() > 2 ? imageFileCount : ("0" + imageFileCount));
			imageFile = timeStamp;
			String imageFormat = "";
			if (!(file.getName().toLowerCase().endsWith(".gif") || file.getName().toLowerCase().endsWith(".tif")
					|| file.getName().toLowerCase().endsWith(".tiff"))) {
				imageFormat = cameraBean.getImageFormat();
			}
			if (!TextUtils.isEmpty(imageFile) && !"default".equalsIgnoreCase(imageFormat)
					&& !TextUtils.isEmpty(imageFormat)) {
				fileName = imageFile + "." + imageFormat;
			} else if (TextUtils.isEmpty(imageFile)
					&& ("default".equalsIgnoreCase(imageFormat) || TextUtils.isEmpty(imageFormat))) {
				fileName = file.getName();
				int lastIndexOf = fileName.lastIndexOf(".");
				imaType = fileName.substring(lastIndexOf + 1, fileName.length());
			} else if (TextUtils.isEmpty(imageFile) && !"default".equalsIgnoreCase(imageFormat)
					&& !TextUtils.isEmpty(imageFormat)) {
				fileName = file.getName();
				int lastIndexOf = fileName.lastIndexOf(".");
				String name = fileName.substring(0, lastIndexOf + 1);
				fileName = name + imageFormat;
			} else if (!TextUtils.isEmpty(imageFile)
					&& ("default".equalsIgnoreCase(imageFormat) || TextUtils.isEmpty(imageFormat))) {
				fileName = file.getName();
				int lastIndexOf = fileName.lastIndexOf(".");
				String substring = fileName.substring(lastIndexOf, fileName.length());
				fileName = imageFile + substring;
			}
			try {

				final Object result = ImgUtils.compressImage(file, fileName, compressHeight, compressWidth, fileSize,
						imaType, source);

				if (result instanceof String) {
					final String resultStr = (String) result;
					if (ResultCode.IMAGE_OUT_OF_MEMORY.equals(resultStr)) {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								callBack.errorMsg(ResultCode.IMAGE_TOO_LARGE, "The image is too large");
							}
						});
					} else {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								callBack.errorMsg(ResultCode.IMAGE_FORMAT_ERROR, resultStr);
							}
						});
					}
					return null;
				} else if (result instanceof File) {
					if (((File) result).length() > fileSize) {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								callBack.errorMsg(ResultCode.IMAGE_TOO_LARGE, "Can not compress to the given size");
							}
						});
						return null;
					}
					return (File) result;
				}
			} catch (Exception e) {
				LogUtils.d(TAG + e.getMessage());
				e.printStackTrace();
			}
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					callBack.errorMsg(ResultCode.IMAGE_FORMAT_ERROR, "Image compress error");
				}

			});
			return null;
		}

		@Override
		protected void onPostExecute(File result) {
			if (result != null) {
				LogUtils.d(TAG + "FilePath -> " + result.getAbsolutePath());
				try {
					byte[] bytes = getBytesFromFile(result);
					float a = (float) (bytes.length / (1024.0 * 1024.0));
					if (isIdCard) {
						sendRequestIdCard(result, fileName, String.format("%.2f", a), cameraBean.getArgs());
					} else {
						FaceIdCardBean bean = new FaceIdCardBean();
						bean.setConfidence(confidence);
						callBack.onFinish(result, fileName, String.format("%.2f", a), cameraBean.getArgs(), bean);
						mIsCheck = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					callBack.errorMsg(ResultCode.OTHER_ERROR, "Compress Error");
					mIsCheck = false;
				}
			} else {
				mIsCheck = false;
			}
		}

	}

	public interface ImageUploadCallBack {
		void onStart(CameraBean bean);

		void onFinish(File file, String fileName, String imgSize, String extraInfo, FaceIdCardBean result);// 调用stop
																											// 方法时才会触发该方法

		void errorMsg(String state, String errorMsg);

	}

	/**
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static byte[] getBytesFromFile(File file) throws Exception {
		if (file != null && !(file.length() > Integer.MAX_VALUE)) {
			try {
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[(int) file.length()];
				int offest = 0;
				int numRead = 0;
				while (offest < bytes.length && (numRead = is.read(bytes, offest, bytes.length - offest)) >= 0) {
					offest += numRead;
				}
				is.close();
				return bytes;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		throw new Exception("File is null or file is to large");
	}

	private boolean checkType(String type) {
		type = type.trim();
		return "1".equals(type) || "2".equals(type) || "3".equals(type);
	}

	/**
	 * 跳转人脸活体检测
	 */
	private void startCheckActivity() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Manager manager = new Manager(activity);
				LivenessLicenseManager licenseManager = new LivenessLicenseManager(activity);
				manager.registerLicenseManager(licenseManager);
				manager.takeLicenseFromNetwork(ConUtil.getUUIDString(activity));
				if (licenseManager.checkCachedLicense() > 0) {
					Looper.prepare();
					activity.startActivityForResult(new Intent(activity, LivenessActivity.class),
							LifeConstants.CHECK_KEY_CODE);
					Looper.loop();
				} else {
					Looper.prepare();
					callBack.errorMsg(ResultCode.FACE_DEFEAT, "授权失败");
					mIsCheck = false;
					Looper.loop();
				}
			}
		}).start();
	}

	public void onActivityResult(Intent data) {
		// 拿到包装对象
		Bundle bundle = data.getExtras();
		// 拿到结果也就是是否成功
		String result = bundle.getString("result");
		// 这个看不懂
		delta = bundle.getString("delta");
		LogUtils.e("result = " + result + ",delta = " + delta);
		// 成功传回来的string的id是这个id
		if (result.contains("验证成功")) {
			// 返回所有的图片
			Map<String, byte[]> images = (Map<String, byte[]>) bundle.getSerializable("images");
			// 拿file
			for (String key : images.keySet()) {
				byte[] imageByte = images.get(key);
				if (key.equals("image_best")) {
					byte[] imageBestData = imageByte;// 这是最好的一张图片
					File bestFile = BitmapUtils.bytes2File(imageBestData,
							Environment.getExternalStorageDirectory().getPath() + File.separator
									+ activity.getPackageName() + File.separator + "image",
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
		} else if (result.contains("检测超时失败")) {// 超时失败
			callBack.errorMsg(ResultCode.FACE_DEFEAT, "活体检测超时失败");
			mIsCheck = false;
		} else if (result.contains("活体检测连续性检测失败")) { // 活体检测连续性检测失败
			callBack.errorMsg(ResultCode.FACE_DEFEAT, "活体检测连续性检测失败");
			mIsCheck = false;
		} else if (result.contains("活体检测动作错误")) { // 活体检测动作错误
			callBack.errorMsg(ResultCode.FACE_DEFEAT, "活体检测动作错误");
			mIsCheck = false;
		} else {
			callBack.errorMsg(ResultCode.FACE_DEFEAT, "活体检测失败");
			mIsCheck = false;
		}
	}

	/**
	 * 成功后文件操作 ficeid 有源对比
	 * 
	 * @param file
	 */
	private void onSuccessBySdk(final File file, final String fileName) {
		HttpRequest.getIdUserInfoByNetWork(activity, delta, file, name, idCard, new IHttpCallback<FaceCheckBean>() {

			@Override
			public void onSuccess(String url, FaceCheckBean result) {
				confidence = String.valueOf(result.getResult_faceid().getConfidence());
				compressPhoto(file.getAbsolutePath(), true);
			}

			@Override
			public void onFinal(String url) {

			}

			@Override
			public void onError(String url, Throwable e) {
				mIsCheck = false;
				// 活体检测失败
				if (TextUtils.isEmpty(e.getMessage())) {
					callBack.errorMsg(ResultCode.FACE_DEFEAT, "活体检测失败");
				} else {
					// 身份证号码有误
					if (e.getMessage().contains("NO_SUCH_ID_NUMBER")) {
						callBack.errorMsg(ResultCode.FACE_DEFEAT, "身份证号码有误");
					} else if (e.getMessage().contains("ID_NUMBER_NAME_NOT_MATCH")) {
						callBack.errorMsg(ResultCode.FACE_DEFEAT, "身份证号码与姓名不匹配");
					} else {
						callBack.errorMsg(ResultCode.FACE_DEFEAT, "活体检测失败");
					}
				}

			}
		});

	}
}
