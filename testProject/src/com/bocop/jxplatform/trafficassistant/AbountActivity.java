package com.bocop.jxplatform.trafficassistant;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.util.MyUtils;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.file.SaveImageToSD;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Update;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

@ContentView(R.layout.activity_about)
public class AbountActivity extends BaseActivity {
	
	Bitmap bitmap;// 二维码中间图片
	private int iv_halfWidth = 20;// 显示中间图片的宽度的一半
	Bitmap mBitmap;// 二维码图片;
	
	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;

	// 关于页面
	@ViewInject(R.id.llt_about)
	private LinearLayout llAbout;
	@ViewInject(R.id.tv_version)
	private TextView tvVersion;

	@ViewInject(R.id.btnUpdate)
	private Button btnUpdate;
	
	@ViewInject(R.id.iv_qrcode)
	private ImageView ivQrcode;
	
	String url = "http://a.app.qq.com/o/simple.jsp?pkgname=com.bocop.jxplatform#opened";
	String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		Log.i("tag", "onResume于");
		super.onResume();
		Log.i("tag", "initData");
		initData();
	}

	private void initData() {
		tv_titleName.setText("关于");
		Log.i("tag", "关于");
		tvVersion.setText("版本号 " + 	 MyUtils.getVersionName(this));
		if(BocSdkConfig.NEED_UPDATE){
			btnUpdate.setVisibility(View.VISIBLE);
			btnUpdate.setText("有新版本（" + BocSdkConfig.NEW_APP_VERSION + "），点击下载");
		}else{
			btnUpdate.setVisibility(View.GONE);
		}
		if(LoginUtil.isLog(AbountActivity.this)){
			userId = LoginUtil.getUserId(AbountActivity.this);
			url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd5944788c1a9ce15&redirect_uri=http://jxboc.uni-infinity.com/yhtsharecount/servlet/DealRestResourceForWeixin/" + userId + "/&response_type=code&scope=snsapi_userinfo&state=a123#wechat_redirect";
			createQrcode(url);
		}else{
			createQrcode(url);
		}
		
	}

	@OnClick(R.id.btnUpdate)
	public void btnUpdate(View v) {
		Update update = new Update(this);
		update.showUpdate();;
	}
	
	
	private void createQrcode(String userId){
		
		
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.bocjx);

		// 缩放图片，用到矩阵去做
		Matrix matrix = new Matrix();
		float sx = (float) 2 * iv_halfWidth / bitmap.getWidth();
		float sy = (float) 2 * iv_halfWidth / bitmap.getHeight();
		matrix.setScale(sx, sy);
		// 生成缩放后的图片
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, false);

//		String s = "http://open.boc.cn/appstore/#/app/appDetail/38201";
		try {
			mBitmap = createBitmap(new String(url.getBytes(), "ISO-8859-1"));
			ivQrcode.setImageBitmap(mBitmap);
			SaveImageToSD.saveBitmap(mBitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据字符串生成二维码
	 * 
	 * @param s
	 * @return
	 * @throws WriterException
	 */
	private Bitmap createBitmap(String str) throws WriterException {
		// 生成而为矩阵，编码是指定大小，不要生成了图片在进行缩放，这样会导致模糊识别失败，就是扫描失败了。
		BitMatrix mBitMatrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, 300, 300);// BarcodeFormat.QR_CODE-编码格式
		// 二维矩阵的宽高
		int w = mBitMatrix.getWidth();
		int h = mBitMatrix.getHeight();

		// 头像的宽度
		int halfw = w / 2;
		int halfh = h / 2;
		// 准备画二维码，把二维矩阵转换为一维数组，一直横着画
		int[] pixels = new int[w * h];// 数组长度就是矩阵的面积值
		for (int y = 0; y < h; y++) {
			int outputOffset = y * w;
			for (int x = 0; x < w; x++) {
				// 画一个普通的二维码
				// if (mBitMatrix.get(x, y)) {// 表示二维矩阵有值，对应画一个黑点
				// pixels[outputOffset + x] = 0xff000000;
				// } else {
				// pixels[outputOffset + x] = 0xffffffff;
				// }

				// 画一个有图片的二维码图片
				if (x > (halfw - iv_halfWidth) && x < (halfw + iv_halfWidth)
						&& y > (halfh - iv_halfWidth)
						&& y < (halfh + iv_halfWidth)) {// 中间图片的区域
					pixels[outputOffset + x] = bitmap.getPixel(x - halfw
							+ iv_halfWidth, y - halfh + iv_halfWidth);// 这里画图之后会很明显的显示出来
				} else {
					if (mBitMatrix.get(x, y)) {// 表示二维矩阵有值，对应画一个黑点
						pixels[outputOffset + x] = 0xff000000;
					} else {
						pixels[outputOffset + x] = 0xffffffff;
					}
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个300*300bitmap
		bitmap.setPixels(pixels, 0, w, 0, 0, w, h);// 像素点、起始点、宽度、其起始像素、宽、高
		return bitmap;

	}
}
