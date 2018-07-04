package com.bocop.xms.fragment;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.activity.XmsMainActivity;
import com.bocop.xms.adapter.MyMessageAdapter;
import com.bocop.xms.view.PullToRefreshBase.OnRefreshListener;
import com.bocop.xms.view.PullToRefreshListView;
import com.bocop.xms.xml.CspXmlXmsCom;
import com.bocop.xms.xml.message.MessageBean;
import com.bocop.xms.xml.message.MessageListResp;
import com.bocop.xms.xml.message.MessageListXmlBean;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 我的消息
 * 
 * @author ftl
 * 
 */
public class MyMessageFragment extends BaseFragment {
	@ViewInject(R.id.lvContent)
	public PullToRefreshListView mPullRefreshListView;
	@ViewInject(R.id.rlContent)
	public RelativeLayout rlContent;
	@ViewInject(R.id.ivContent)
	public ImageView ivContent;
	@ViewInject(R.id.tvContent)
	public TextView tvContent;
	private ListView lvContent;
	private MyMessageAdapter msgAdapter;
	private int currentPageNo=0;
	private String pageSize="10";
	private ArrayList<MessageBean> msgDatas=new ArrayList<MessageBean>();
	private SharedPreferences sp;
	private Editor editor;
	private String currentRole;
	
	public static final String SELECT_ROLE = "select_role";
	public static final String FIRST_SELECT = "first_select";
	public static final String CURRENT_ROLE = "current_role";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(R.layout.xms_fragment_my_message);
		return view;
	}
	
	@Override
	protected void initData() {
		super.initData();
		activity = (XmsMainActivity ) getActivity();
//		msgAdapter = new MyMessageAdapter(activity, this, msgDatas);
		mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
		lvContent = mPullRefreshListView.getRefreshableView();
		lvContent.setAdapter(msgAdapter);
		
		//刷新按钮点击事件
//		activity.getIvRefresh().setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				requestMessage(1, true);
//				lvContent.setSelection(0);
//			}
//		});
		requestMessage(1, true);
		
		sp = activity.getSharedPreferences(SELECT_ROLE, Context.MODE_PRIVATE);
		editor = sp.edit();

		boolean flag = sp.getBoolean(FIRST_SELECT, true);
		currentRole = sp.getString(CURRENT_ROLE, "man");
		if (flag) {
			showRoleDialog();
			editor.putBoolean(FIRST_SELECT, false);
			editor.commit();
		}
	}
	
	public void showRoleDialog() {
		View view = View.inflate(activity, R.layout.xms_dialog_select_role, null);
		Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
		final ImageView ivMan = (ImageView) view.findViewById(R.id.ivMan);
		final ImageView ivWoman = (ImageView) view.findViewById(R.id.ivWoman);
		final AlertDialog dialog = DialogUtil.showWithView(activity, view);
		currentRole = sp.getString(CURRENT_ROLE, "man");
		if ("man".equals(currentRole)) {
			ivMan.setImageResource(R.drawable.xms_img_man_selected);
			ivWoman.setImageResource(R.drawable.xms_img_woman_normal);
		} else {
			ivMan.setImageResource(R.drawable.xms_img_man_normal);
			ivWoman.setImageResource(R.drawable.xms_img_woman_selected);
		}
		ivMan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentRole = "man";
				ivMan.setImageResource(R.drawable.xms_img_man_selected);
				ivWoman.setImageResource(R.drawable.xms_img_woman_normal);
			}
		});
		ivWoman.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentRole = "woman";
				ivMan.setImageResource(R.drawable.xms_img_man_normal);
				ivWoman.setImageResource(R.drawable.xms_img_woman_selected);
			}
		});
		btnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (msgAdapter != null) {
					msgAdapter.notifyDataSetChanged();
				}
				editor.putString(CURRENT_ROLE, currentRole);
				editor.commit();
			}
		});
	}
	
	OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
		public void onRefresh() {
			int state=mPullRefreshListView.getRefreshType();
			requestMessage(state, false);
		}
	};

	private XmsMainActivity activity;
	@Override
	protected void initView() {
		super.initView();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	public PullToRefreshListView getmPullToRefreshListView(){
		return mPullRefreshListView;
	}
	
	/**
	 * 请求消息
	 */
	private void requestMessage(final int pullState, boolean isShowDialog) {
		try {
			// 生成CSP XML报文
			String txCode = "MS002005";
			CspXmlXmsCom cspXmlXmsCom = new CspXmlXmsCom(LoginUtil.getUserId(baseActivity), txCode);
//			CspXmlXmsCom cspXmlXmsCom = new CspXmlXmsCom("developer", txCode);
			if(pullState==1){//下拉刷新
				currentPageNo = 0;
			}			
			cspXmlXmsCom.setPageNo(currentPageNo+"");
			cspXmlXmsCom.setPageSize(pageSize);
			String strXml = cspXmlXmsCom.getCspXml();
			Log.i("tag", "getMSgXml");
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			Log.i("tag", "Mcis");
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(baseActivity);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
//			cspUtil.setTest(true);
			cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {
				@Override
				public void onSuccess(String responStr) {
					if(responStr!=null){
						MessageListXmlBean msgListXmlBean = MessageListResp.readStringXml(responStr);
						if(msgListXmlBean==null) {
							mPullRefreshListView.setVisibility(View.GONE);
							ivContent.setImageResource(R.drawable.xms_img_data_error);
							tvContent.setText("数据异常");
							rlContent.setVisibility(View.VISIBLE);
							return;
						}
						if(!msgListXmlBean.getErrorcode().equals("00")){
							if (msgListXmlBean.getErrorcode().equals("01")) {
								mPullRefreshListView.onRefreshComplete();
								if (currentPageNo == 0) {
									mPullRefreshListView.setVisibility(View.GONE);
									ivContent.setImageResource(R.drawable.xms_img_no_data);
									tvContent.setText("暂无数据");
									rlContent.setVisibility(View.VISIBLE);
								} else {
									Toast.makeText(baseActivity, msgListXmlBean.getErrormsg(), Toast.LENGTH_SHORT).show();
								}
							} else {
								mPullRefreshListView.setVisibility(View.GONE);
								ivContent.setImageResource(R.drawable.xms_img_data_error);
								tvContent.setText("数据异常");
								rlContent.setVisibility(View.VISIBLE);
							}
						} else {
							mPullRefreshListView.setVisibility(View.VISIBLE);
							rlContent.setVisibility(View.GONE);
							currentPageNo++;
							Log.i("tag", "initCarDates");
						
							
							List<MessageBean> mList = msgListXmlBean.getMessageList();
//							MessageBean messageBean = mList.get(0);
//							if (messageBean != null){
//								if (XmsMainActivity.msgLastId == null) {
//									//存储最新消息id
//									XmsMainActivity.msgLastId = messageBean.getMessageId();
//								} else {
//									XmsMainActivity xmsMainActivity = (XmsMainActivity) activity;
//									if (!XmsMainActivity.msgLastId.equals(messageBean.getMessageId())) {
//										xmsMainActivity.displayDot();
//										//存储最新消息id
//										XmsMainActivity.msgLastId = messageBean.getMessageId();
//									} else {
//										xmsMainActivity.hiddenDot();
//									}
//								}
//							}
							if(pullState == 1){//下拉刷新
								msgDatas.clear();
							}
							msgDatas.addAll(mList);
							msgAdapter.notifyDataSetChanged();
							mPullRefreshListView.onRefreshComplete();
						}
					}
				}

				@Override
				public void onFinish() {

				}
				@Override
				public void onFailure(String responStr) {
					Toast.makeText(baseActivity, responStr, Toast.LENGTH_SHORT).show();
				}
			}, isShowDialog);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public Date strToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public String getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}
}
