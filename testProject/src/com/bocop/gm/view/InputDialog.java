package com.bocop.gm.view;

import com.bocop.jxplatform.R;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class InputDialog extends Dialog {

	EditText etURL;
	Button btnOK;
	Button btnCancel;

	private OnOKClickListener okClickListener;

	
	public InputDialog(Context context,OnOKClickListener onOKClickListener){
		super(context);
		this.okClickListener = onOKClickListener;
		initDialog();
	}

	private void initDialog() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.gm_layout_dialog_input);
		etURL = (EditText) findViewById(R.id.etUrl);
		btnOK = (Button) findViewById(R.id.btnOK);
		btnCancel = (Button) findViewById(R.id.btnCancel);

		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(etURL.getText().toString())) {
					okClickListener.onOKClick(etURL.getText().toString());
				}
			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InputDialog.this.dismiss();
			}
		});
		this.show();
	}

	public interface OnOKClickListener {
		void onOKClick(String url);
	}
	
}
