package com.bocop.jxplatform.view.riders;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bocop.jxplatform.R;

public class ApplySuccesDialog extends Dialog {
	Context context;
	setOkListener listener;
	private TextView tvConfirm;
	private TextView tvPoint;
	private TextView tvPhone;

	public ApplySuccesDialog(Context context,String point,String phone) {
		super(context, R.style.DialogStyle_4);
		this.context = context;
		initView();
		tvPoint.setText(point);
		tvPhone.setText(phone);
	}

	private void initView() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		View view = View.inflate(context, R.layout.dialog_applysuccess, null);
		tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
		tvPoint = (TextView) view.findViewById(R.id.tv_point);
		tvPhone = (TextView) view.findViewById(R.id.tv_telephone);
		tvConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (listener != null) {
					listener.onOkClick();
				}
			}
		});
		setContentView(view);
		Window win = getWindow();
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.width = (int) (screenWidth * 0.8);
		lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// View viewRoot = getWindow().getDecorView();
		// viewRoot.findViewById(R.id.tv_confirm)
	}

	public interface setOkListener {
		void onOkClick();
	}

	// public abstract void onOkClickListener();
	public setOkListener getListener() {
		return listener;
	}

	public void setListener(setOkListener listener) {
		this.listener = listener;
	}
}
