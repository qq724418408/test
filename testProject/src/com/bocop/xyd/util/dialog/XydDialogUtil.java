package com.bocop.xyd.util.dialog;

import java.util.List;

import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.adapter.recycleradapter.CommonRecyclerAdapter;
import com.bocop.xfjr.adapter.recycleradapter.RecyclerViewHolder;
import com.bocop.xfjr.bean.SystemBasicInfo.CityListBean;
import com.bocop.xfjr.bean.SystemBasicInfo.HouseTypeBean;
import com.bocop.xfjr.bean.SystemBasicInfo.JobLevelListBean;
import com.bocop.xfjr.bean.SystemBasicInfo.PayMethodsListBean;
import com.bocop.xfjr.bean.SystemBasicInfo.PeriodListBean;
import com.bocop.xfjr.bean.add.ChannelBean;
import com.bocop.xfjr.bean.add.MerchantBean;
import com.bocop.xfjr.bean.add.ProductBean;
import com.bocop.xfjr.bean.pretrial.CustomType;
import com.bocop.xfjr.util.FullyLinearLayoutManager;
import com.bocop.xfjr.util.ScreenUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class XydDialogUtil {

	public static XydDialog inputPasswordDialog(Context context, final PasswordDialogClick dialogClick) {
		final XydDialog dialog = new XydDialog.Builder(context)
				.setContentView(R.layout.xyd_dialog_paying)
				.setWidthAndHeight(com.bocop.xyd.util.ScreenUtils.dip2px(context, 300), ViewGroup.LayoutParams.WRAP_CONTENT)
				.setCancelable(false)
				.create();
		TextView tvOk = dialog.getView(R.id.tvOk);
		TextView tvCancel = dialog.getView(R.id.tvCancel);
		tvOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogClick.onOkClick(v, dialog);
			}
		});
		tvCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogClick.onCancelClick(v, dialog);
			}
		});
		return dialog;
	}
	
	public static void comfirmDialog(Context context, final ComfirmDialogClick dialogClick) {
		final XydDialog dialog = new XydDialog.Builder(context)
				.setContentView(R.layout.xyd_dialog_pay_sucess)
				.setWidthAndHeight(com.bocop.xyd.util.ScreenUtils.dip2px(context, 300), ViewGroup.LayoutParams.WRAP_CONTENT)
				.create();
		TextView tvOk = dialog.getView(R.id.tvOk);
		tvOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogClick.onOkClick(v, dialog);
			}
		});
		dialog.show();;
	}
	public interface  ComfirmDialogClick{
		void onOkClick(View v,Dialog dialog);
	}
	
	public interface  PasswordDialogClick{
		void onOkClick(View v,Dialog dialog);
		void onCancelClick(View v,Dialog dialog);
	}
	
}
