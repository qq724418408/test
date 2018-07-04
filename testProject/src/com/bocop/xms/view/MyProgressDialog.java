package com.bocop.xms.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bocop.jxplatform.R;


public class MyProgressDialog extends android.app.Dialog {

	String message;
	TextView messageTextView;
	String title;
	TextView titleTextView;

	public MyProgressDialog(Context context, String title, String message) {
		super(context, android.R.style.Theme_Translucent);
		this.message = message;
		this.title = title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_progress_dialog);

		this.titleTextView = (TextView) findViewById(R.id.tvTitle);
		setTitle(title);

		this.messageTextView = (TextView) findViewById(R.id.tvMessage);
		setMessage(message);
	}

	// GETERS & SETTERS

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		messageTextView.setText(message);
	}

	public TextView getMessageTextView() {
		return messageTextView;
	}

	public void setMessageTextView(TextView messageTextView) {
		this.messageTextView = messageTextView;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if (TextUtils.isEmpty(title))
			titleTextView.setVisibility(View.GONE);
		else {
			titleTextView.setVisibility(View.VISIBLE);
			titleTextView.setText(title);
		}
	}

	public TextView getTitleTextView() {
		return titleTextView;
	}

	public void setTitleTextView(TextView titleTextView) {
		this.titleTextView = titleTextView;
	}

}
