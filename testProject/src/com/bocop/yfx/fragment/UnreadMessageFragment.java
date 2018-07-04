package com.bocop.yfx.fragment;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.base.BaseAdapter;
import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.view.LoadingView;
import com.boc.jx.view.LoadingView.OnRetryListener;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.tools.ViewHolder;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.bean.Message;
import com.bocop.yfx.bean.MessageResponse;
import com.bocop.yfx.xml.CspXmlYfxCom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * 未读消息
 * 
 * @author lh
 * 
 */
public class UnreadMessageFragment extends BaseFragment {

	@ViewInject(R.id.lvMessage)
	private ListView lvMessage;
	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;

	private List<Message> list = new ArrayList<>();
	private BaseAdapter<Message> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(R.layout.yfx_fragment_unread_message);
		return view;
	}

	@Override
	protected void initData() {
		super.initData();
		requestMessage();
	}

	private void setAdapter() {
		lvMessage.setAdapter(adapter = new BaseAdapter<Message>(baseActivity, list, R.layout.yfx_item_message) {

			@Override
			public void viewHandler(int position, final Message t, View convertView) {
				TextView tvMessage = ViewHolder.get(convertView, R.id.tvMessage);
				LinearLayout llMessage = ViewHolder.get(convertView, R.id.llMessage);
				if (null != t) {
					tvMessage.setText(t.getMsg());
				}

				llMessage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if ("0".equals(t.getMsgType())) {

						} else if ("1".equals(t.getMsgType())) {

						} else if ("2".equals(t.getMsgType())) {

						}
					}
				});
			}
		});
	}

	/**
	 * 请求消息列表
	 */
	private void requestMessage() {
		try {
			CspXmlYfxCom cspXmlYfxCom = new CspXmlYfxCom("WL001013",
					LoginUtil.getUserId(baseActivity));
			String strXml = cspXmlYfxCom.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			byte[] byteMessage = {};
			byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(baseActivity);
			cspUtil.setFLAG_YFX_CSP(true);
			cspUtil.setTxCode("001013");
			cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					onGetMsgSuccess(responStr);
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFailure(String responStr) {
					onGetListFailure(responStr);
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 获取消息列表成功后
	 * 
	 * @param responStr
	 */
	private void onGetMsgSuccess(String responStr) {
		MessageResponse msgResponse = XStreamUtils.getFromXML(responStr, MessageResponse.class);
		ConstHead constHead = msgResponse.getConstHead();
		if (constHead != null) {
			if ("00".equals(constHead.getErrCode())) {
				lvMessage.setVisibility(View.VISIBLE);
				loadingView.setVisibility(View.GONE);
				list = msgResponse.getList().getList();
				setAdapter();
			} else if ("50".equals(constHead.getErrCode())) {
				DialogUtil.showWithToMain(baseActivity, constHead.getErrMsg());
			} else {
				onGetListFailure(constHead.getErrMsg());
			}
		}
	}
	
	private void onGetListFailure(String err_msg){
		CspUtil.onFailure(baseActivity, err_msg);
		lvMessage.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);
		loadingView.setmOnRetryListener(new OnRetryListener() {

			@Override
			public void retry() {
				requestMessage();
			}
		});
	}
}
