package com.bocop.xfjr.activity;

import java.io.IOException;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.base.XfjrBaseActivity;
import com.bocop.xfjr.view.photoview.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class XfjrPreViewActivity extends XfjrBaseActivity {

	public static void startActivity(Context ctx, String imagePath) {
		Intent intent = new Intent(ctx, XfjrPreViewActivity.class);
		intent.putExtra("imagePath", imagePath);
		ctx.startActivity(intent);
	}

	private PhotoView photoView;
	private String filePath;
	private Dialog dialog;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		hideNavigationBar();
		setContentView(R.layout.xfjr_activity_pre_view);

		photoView = (PhotoView) findViewById(R.id.imageView);
		filePath = getIntent().getStringExtra("imagePath");
		
		
		// 按返回键删除文件
		dialog = ProgressDialog.show(this, "请稍等", "正在加载图片");
		
		
		
		Glide.with(this).load(filePath)
		.bitmapTransform(new BitmapTransformation (this){

			@Override
			public String getId() {
				// TODO Auto-generated method stub
				return "";
			}

			@Override
			protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
				Matrix matrix = new Matrix();
				
				float scan = 1;
				if(toTransform.getWidth()>toTransform.getHeight()){
					scan = photoView.getWidth()*1f/toTransform.getWidth();
				}			
				matrix.setScale(scan, scan);
				return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
			}
			
		})
		.listener(new RequestListener<String, GlideDrawable>() {
			@Override
			public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
				dialog.dismiss();
				Toast.makeText(XfjrPreViewActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
				finish();
				return false;
			}

			@Override
			public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {				
				dialog.dismiss();
				return false;
			}
		}).into(photoView);
		photoView.enable();
//		photoView.enableRotate();
		photoView.performClick();
	}

	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			Log.d(TAG, "cannot read exif" + ex);
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}
		return degree;
	}

	public void back(View v) {
		finish();
	}
	
	private void hideNavigationBar() {  
        int systemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();  
  
        // Navigation bar hiding:  Backwards compatible to ICS.  
        if (Build.VERSION.SDK_INT >= 14) {  
            systemUiVisibility ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;  
        }  
  
        // 全屏展示  
        if (Build.VERSION.SDK_INT >= 16) {  
            systemUiVisibility ^= View.SYSTEM_UI_FLAG_FULLSCREEN;  
        }  
  
        if (Build.VERSION.SDK_INT >= 18) {  
            systemUiVisibility ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;  
        }  
  
        getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);  
    }

}
