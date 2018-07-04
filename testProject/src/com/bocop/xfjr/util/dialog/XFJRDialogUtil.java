package com.bocop.xfjr.util.dialog;

import java.util.List;

import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.bean.SystemBasicInfo.ChildCityBean;
import com.bocop.xfjr.bean.SystemBasicInfo.CityListBean;
import com.bocop.xfjr.bean.SystemBasicInfo.HouseTypeBean;
import com.bocop.xfjr.bean.SystemBasicInfo.IndustryListBean;
import com.bocop.xfjr.bean.SystemBasicInfo.JobLevelListBean;
import com.bocop.xfjr.bean.SystemBasicInfo.PayMethodsListBean;
import com.bocop.xfjr.bean.SystemBasicInfo.PeriodListBean;
import com.bocop.xfjr.bean.add.ChannelBean;
import com.bocop.xfjr.bean.add.MerchantBean;
import com.bocop.xfjr.bean.add.ProductBean;
import com.bocop.xfjr.bean.pretrial.CustomType;
import com.bocop.xfjr.util.ScreenUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class XFJRDialogUtil {

	public static void showListDialog(List<String> list, String title, Context context,
			OnClickListener l) {
		String[] items = (String[]) list.toArray(new String[list.size()]);
		AlertDialog.Builder listDialog = new AlertDialog.Builder(context);
		listDialog.setTitle(title);
		listDialog.setItems(items, l);
		listDialog.show();
	}

	public static XfjrDialog confirmDialog(Context context, String msg,
			final DialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_confirm_no_title)
				.setWidthAndHeight(ScreenUtils.dip2px(context, 250),
						ViewGroup.LayoutParams.WRAP_CONTENT)
				.create();
		TextView tvMsg = dialog.getView(R.id.tvMsg);
		TextView tvOk = dialog.getView(R.id.tvOk);
		TextView tvCancel = dialog.getView(R.id.tvCancel);
		if (!TextUtils.isEmpty(msg)) {
			tvMsg.setText(msg);
		}
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
	
	/**
	 * 不可取消的加载对话框
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static XfjrDialog loadingDialog(Context context, String msg) {
		XfjrDialog dialog = new XfjrDialog
				.Builder(context)
				.setCancelable(false)
				.setContentView(R.layout.xfjr_loading_dialog)
				.setWidthAndHeight(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
				.create();
		TextView tvMsg = dialog.getView(R.id.loadingText);
		if (!TextUtils.isEmpty(msg)) {
			tvMsg.setText(msg);
		}
		return dialog;
	}

	/**
	 * 带标题的确认对话框
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @param dialogClick
	 * @return
	 */
	public static XfjrDialog confirmDialog(Context context, String title, String msg, final DialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_confirm)
				.setWidthAndHeight(ScreenUtils.dip2px(context, 250), LayoutParams.WRAP_CONTENT)
				.create();
		TextView tvTitle = dialog.getView(R.id.tvTitle);
		TextView tvMsg = dialog.getView(R.id.tvMsg);
		TextView tvOk = dialog.getView(R.id.tvOk);
		TextView tvCancel = dialog.getView(R.id.tvCancel);
		if (!TextUtils.isEmpty(title)) {
			tvTitle.setText(title);
		}
		if (!TextUtils.isEmpty(msg)) {
			tvMsg.setText(msg);
		}
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

	public static XfjrDialog tipsDialog(Context context, String msg, final DialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_confirm_no_title)
				.setWidthAndHeight(ScreenUtils.dip2px(context, 250),
						ViewGroup.LayoutParams.WRAP_CONTENT)
				.create();
		TextView tvMsg = dialog.getView(R.id.tvMsg);
		TextView tvOk = dialog.getView(R.id.tvOk);
		dialog.setCancelable(false);
		dialog.getView(R.id.tvCancel).setVisibility(View.GONE);
		if (!TextUtils.isEmpty(msg)) {
			tvMsg.setText(msg);
		}
		tvOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogClick.onOkClick(v, dialog);
			}
		});
		return dialog;
	}
	
	@SuppressLint("NewApi")
	public static void showMerchantListDialog(Context context, final String selected,
			final List<MerchantBean> list, final MerchantSelectDialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_select).setWidthAndHeight(-1, -1).create();
		ListView recyclerView = (ListView) dialog.getView(R.id.recyclerView);
		recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setTag(position);
				dialogClick.onClick(view, dialog, list.get(position));
			}
		});
		dialog.getView(R.id.lltDialog).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		recyclerView.setAdapter(new ArrayAdapter<>(context, R.layout.xfjr_dialog_list_select_item,
				R.id.ctvContent, list));
		dialog.show();
	}

	/**
	 * 
	 * @param context
	 * @param selected
	 * @param list
	 * @param dialogClick
	 */
	@SuppressLint("NewApi")
	public static void showChannelListDialog(Context context, final String selected,
			final List<ChannelBean> list, final ChannelSelectDialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_select).setWidthAndHeight(-1, -1).create();
		ListView recyclerView = (ListView) dialog.getView(R.id.recyclerView);
		recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setTag(position);
				dialogClick.onClick(view, dialog, list.get(position));
			}
		});
		dialog.getView(R.id.lltDialog).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		recyclerView.setAdapter(new ArrayAdapter<>(context, R.layout.xfjr_dialog_list_select_item,
				R.id.ctvContent, list));
		dialog.show();
	}

	@SuppressLint("NewApi")
	public static void showProductListDialog(Context context, final String selected,
			final List<ProductBean> list, final ProductSelectDialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_select).setWidthAndHeight(-1, -1).create();
		ListView recyclerView = (ListView) dialog.getView(R.id.recyclerView);
		recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setTag(position);
				dialogClick.onClick(view, dialog, list.get(position));
			}
		});
		dialog.getView(R.id.lltDialog).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		recyclerView.setAdapter(new ArrayAdapter<>(context, R.layout.xfjr_dialog_list_select_item,
				R.id.ctvContent, list));
		dialog.show();
	}

	@SuppressLint("NewApi")
	public static void showCityListListDialog(Context context, final String selected,
			final List<CityListBean> list, final CityListSelectDialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_select).setWidthAndHeight(-1, -1).create();
		ListView recyclerView = (ListView) dialog.getView(R.id.recyclerView);
		recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setTag(position);
				dialogClick.onClick(view, dialog, list.get(position));
			}
		});
		dialog.getView(R.id.lltDialog).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		recyclerView.setAdapter(new ArrayAdapter<>(context, R.layout.xfjr_dialog_list_select_item,
				R.id.ctvContent, list));
		dialog.show();
	}

	@SuppressLint("NewApi")
	public static void alertCustomTypeSelectDialog(Context context, final String selected,
			final List<CustomType> list, final CustomTypeSelectDialogClick dialogClick) {
		LogUtils.e("集合：" + list.toString());
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_select).setWidthAndHeight(-1, -1).create();
		ListView recyclerView = (ListView) dialog.getView(R.id.recyclerView);
		recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setTag(position);
				dialogClick.onClick(view, dialog, list.get(position));
			}
		});
		dialog.getView(R.id.lltDialog).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		recyclerView.setAdapter(new ArrayAdapter<>(context, R.layout.xfjr_dialog_list_select_item,
				R.id.ctvContent, list));
		dialog.show();
	}

	/**
	 * string选择对话框
	 * 
	 * @param context
	 * @param selected
	 * @param list
	 * @param dialogClick
	 */
	public static void alertStringSelectDialog(Context context, final String selected,
			final List<String> list, final StringSelectDialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_select)
				.setWidthAndHeight(-1, -1)
				.create();
		ListView recyclerView = (ListView) dialog.getView(R.id.recyclerView);
		recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setTag(position);
				dialogClick.onClick(view, dialog);
			}
		});
		dialog.getView(R.id.lltDialog).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		recyclerView.setAdapter(new ArrayAdapter<>(context, R.layout.xfjr_dialog_list_select_item,
				R.id.ctvContent, list));
		dialog.show();
	}

	/**
	 * 选择分期期数对话框
	 * 
	 * @param context
	 * @param selected
	 * @param list
	 * @param dialogClick
	 */
	@SuppressLint("NewApi")
	public static void showPeriodListDialog(Context context, final String selected,
			final List<PeriodListBean> list, final PeriodSelectDialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_select).setWidthAndHeight(-1, -1).create();
		ListView recyclerView = (ListView) dialog.getView(R.id.recyclerView);
		recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setTag(position);
				dialogClick.onClick(view, dialog, list.get(position));
			}
		});
		dialog.getView(R.id.lltDialog).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		recyclerView.setAdapter(new ArrayAdapter<>(context, R.layout.xfjr_dialog_list_select_item,
				R.id.ctvContent, list));
		dialog.show();
	}

	/**
	 * 选择房产类型对话框
	 * 
	 * @param context
	 * @param selected
	 * @param list
	 * @param dialogClick
	 */
	public static void showHouseTypeDialog(Context context, final String selected,
			final List<HouseTypeBean> list, final HouseTypeSelectDialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_select).setWidthAndHeight(-1, -1).create();
		ListView recyclerView = (ListView) dialog.getView(R.id.recyclerView);
		recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setTag(position);
				dialogClick.onClick(view, dialog, list.get(position));
			}
		});
		dialog.getView(R.id.lltDialog).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		recyclerView.setAdapter(new ArrayAdapter<>(context, R.layout.xfjr_dialog_list_select_item,
				R.id.ctvContent, list));
		dialog.show();
	}

	/**
	 * 选择职位等级对话框
	 * 
	 * @param context
	 * @param selected
	 * @param list
	 * @param dialogClick
	 */
	public static void showJobLevelListDialog(Context context, final String selected,
			final List<JobLevelListBean> list, final JobLevelListSelectDialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_select).setWidthAndHeight(-1, -1).create();
		ListView recyclerView = (ListView) dialog.getView(R.id.recyclerView);
		recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setTag(position);
				dialogClick.onClick(view, dialog, list.get(position));
			}
		});
		dialog.getView(R.id.lltDialog).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		recyclerView.setAdapter(new ArrayAdapter<>(context, R.layout.xfjr_dialog_list_select_item,
				R.id.ctvContent, list));
		dialog.show();
	}

	/**
	 * 选择支付方式对话框
	 * 
	 * @param context
	 * @param selected
	 * @param list
	 * @param dialogClick
	 */
	public static void showPayMethodsListDialog(Context context, final String selected,
			final List<PayMethodsListBean> list,
			final PayMethodsListSelectDialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_select).setWidthAndHeight(-1, -1).create();
		ListView recyclerView = (ListView) dialog.getView(R.id.recyclerView);
		recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setTag(position);
				dialogClick.onClick(view, dialog, list.get(position));
			}
		});
		dialog.getView(R.id.lltDialog).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		recyclerView.setAdapter(new ArrayAdapter<>(context, R.layout.xfjr_dialog_list_select_item,
				R.id.ctvContent, list));
		dialog.show();
	}

	/**
	 * 选择行业类别对话框
	 * 
	 * @param context
	 * @param selected
	 * @param list
	 * @param dialogClick
	 */
	public static void showIndustryListDialog(Context context, final String selected,
			final List<IndustryListBean> list, final IndustryListSelectDialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_select).setWidthAndHeight(-1, -1).create();
		final ListView recyclerView = (ListView) dialog.getView(R.id.recyclerView);
		recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setTag(position);
				dialogClick.onClick(view, dialog, list.get(position));
			}
		});
		dialog.getView(R.id.lltDialog).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		recyclerView.setAdapter(new ArrayAdapter<>(context, R.layout.xfjr_dialog_list_select_item,
				R.id.ctvContent, list));
		dialog.show();
	}
	
	public static void showChildCityListDialog(Context context, final String selected,
			final List<ChildCityBean> list, final ChildCityListSelectDialogClick dialogClick) {
		final XfjrDialog dialog = new XfjrDialog.Builder(context)
				.setContentView(R.layout.xfjr_dialog_select).setWidthAndHeight(-1, -1).create();
		final ListView recyclerView = (ListView) dialog.getView(R.id.recyclerView);
		recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setTag(position);
				dialogClick.onClick(view, dialog, list.get(position));
			}
		});
		dialog.getView(R.id.lltDialog).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
			
		});
		recyclerView.setAdapter(new ArrayAdapter<>(context, R.layout.xfjr_dialog_list_select_item,
				R.id.ctvContent, list));
		dialog.show();
	}

	// 种类dialog回调
	public interface CustomTypeSelectDialogClick {
		void onClick(View view, XfjrDialog dialog, CustomType type);
	}

	public interface StringSelectDialogClick {
		void onClick(View view, XfjrDialog dialog);
	}

	public interface DialogClick {
		void onOkClick(View view, XfjrDialog dialog);

		void onCancelClick(View view, XfjrDialog dialog);
	}

	public interface MerchantSelectDialogClick {
		void onClick(View view, XfjrDialog dialog, MerchantBean merchantBean);
	}

	public interface ChannelSelectDialogClick {
		void onClick(View view, XfjrDialog dialog, ChannelBean channelBean);
	}

	public interface ProductSelectDialogClick {
		void onClick(View view, XfjrDialog dialog, ProductBean productBean);
	}

	public interface PeriodSelectDialogClick {
		void onClick(View view, XfjrDialog dialog, PeriodListBean bean);
	}

	public interface CityListSelectDialogClick {
		void onClick(View view, XfjrDialog dialog, CityListBean bean);
	}
	
	public interface ChildCityListSelectDialogClick {
		void onClick(View view, XfjrDialog dialog, ChildCityBean bean);
	}

	/**
	 * 房产类型
	 * 
	 * @author bubbly
	 *
	 */
	public interface HouseTypeSelectDialogClick {
		void onClick(View view, XfjrDialog dialog, HouseTypeBean bean);
	}

	/**
	 * 职位等级
	 * 
	 * @author bubbly
	 *
	 */
	public interface JobLevelListSelectDialogClick {
		void onClick(View view, XfjrDialog dialog, JobLevelListBean bean);
	}

	/**
	 * 支付方式
	 * 
	 * @author bubbly
	 *
	 */
	public interface PayMethodsListSelectDialogClick {
		void onClick(View view, XfjrDialog dialog, PayMethodsListBean bean);
	}

	/**
	 * 行业类别
	 * 
	 * @author bubbly
	 *
	 */
	public interface IndustryListSelectDialogClick {
		void onClick(View view, XfjrDialog dialog, IndustryListBean bean);
	}
}