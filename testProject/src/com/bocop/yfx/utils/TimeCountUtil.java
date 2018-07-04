package com.bocop.yfx.utils;

import java.util.Timer;
import java.util.TimerTask;

import com.bocop.jxplatform.R;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class TimeCountUtil {
	TextView btGetCheckcode;
	int time;

	public TimeCountUtil(TextView btGetCheckcode, int time) {
		this.btGetCheckcode = btGetCheckcode;
		this.time = time;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			btGetCheckcode.setText(msg.arg1 + "秒");
			btGetCheckcode.setClickable(false);
			countTime();

		};
	};
	private Timer timer;

	public void countTime() {
		timer = new Timer();
		if (time == 60)
			btGetCheckcode.setText(time + "秒");
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				time--;
				Message message = mHandler.obtainMessage();
				message.arg1 = time;
				mHandler.sendMessage(message);

			}
		};
		timer.schedule(timerTask, 1000);
		if (time == 0) {
			timer.cancel();
			btGetCheckcode.setText("重新发送");
			btGetCheckcode.setClickable(true);
			btGetCheckcode
					.setBackgroundResource(R.drawable.yfx_shape_get_check_code);
		}
	}
}
