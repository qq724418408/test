package com.bocop.xfjr.helper;

import com.bocop.jxplatform.R;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * description： 欺诈侦测工具
 * <p/>
 * Created by TIAN FENG on 2017年9月6日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class CheckHelper {
	
	/**
	 * edit清空，联动图片的显隐
	 */
	public static void bindEditText(final EditText editText,final ImageView imageView) {
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					imageView.setVisibility(View.VISIBLE);
		        } else {
		        	imageView.setVisibility(View.GONE);
		        }
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		imageView.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				editText.setText("");
			}
		});
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
		            // 此处为得到焦点时的处理内容
		            if (editText.getText().length() > 0) {
		            	imageView.setVisibility(View.VISIBLE);
		            } else {
		                // 此处为失去焦点时的处理内容
		            	imageView.setVisibility(View.GONE);
		            }
		        } else {
		            // 此处为失去焦点时的处理内容
		        	imageView.setVisibility(View.GONE);
		        }
			}
		});
	}
	
	/**
	 * edit清空，联动图片的显隐
	 */
	public static void bindImage(final ImageView contentImg,final ImageView removeImg,final IClickListener listener) {
		removeImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				contentImg.setImageResource(R.drawable.xfjr_pretrial_photo);
//				contentImg.setImageBitmap(BitmapFactory.decodeResource(contentImg.getResources(),R.drawable.xfjr_camera_group));
				removeImg.setVisibility(View.GONE);
				listener.click(v.getId());
			}
		});
	}
	
	public interface IClickListener {
		void click(int viewId);
	}
}
