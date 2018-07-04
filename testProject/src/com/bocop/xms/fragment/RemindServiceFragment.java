package com.bocop.xms.fragment;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.CalendarUtils;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.activity.RemindManageActivity;
import com.bocop.xms.activity.XmsMainActivity;
import com.bocop.xms.adapter.DateAdapter;
import com.bocop.xms.service.AlarmService;
import com.bocop.xms.service.AlarmServiceManager;
import com.bocop.xms.utils.SharedPreferencesUtils;
import com.bocop.xms.xml.CspXmlXmsCom;
import com.bocop.xms.xml.remind.EventBean;
import com.bocop.xms.xml.remind.EventComResp;
import com.bocop.xms.xml.remind.EventComXmlBean;
import com.bocop.xms.xml.remind.EventListResp;
import com.bocop.xms.xml.remind.EventListXmlBean;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 
 * 提醒服务
 * 
 * @author ftl
 * 
 */
public class RemindServiceFragment extends BaseFragment {

	@ViewInject(R.id.lvContent)
	private ListView lvContent;
	//private static String TAG = "ZzL";
	/*private GridView gridView = null;
	private GestureDetector gestureDetector = null;*/
	private DateAdapter dateAdapter;
	//private SpecialCalendar sc = null;
	//private boolean isLeapyear = false; // 是否为闰年
	private int selectPostion = 0;
	private String dayNumbers[] = new String[7];
	//private List<String> timeList=new ArrayList<String>();
	private List<EventBean> itemList;// 数据
	private SlideAdapter adapter;
	private int currentPosition;
	private XmsMainActivity activity;
	private String showDate;//显示的时期
	private Map<Integer, Boolean> isSelected=new HashMap<Integer, Boolean>();
 	private List beSelectedData = new ArrayList();
 	private SharedPreferencesUtils spf;
 	private List<EventBean> list;//手机存储数据
 	private String showDateFormat;
 	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(R.layout.xms_fragment_remind_service);
		activity = (XmsMainActivity) getActivity();
		return view;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initData() {
		super.initData();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		showDate = sdf.format(date);
		showDateFormat=sdf1.format(date);
		//初始化事件测试数据
		itemList = new ArrayList<EventBean>();
		adapter = new SlideAdapter();
		lvContent.setAdapter(adapter);
		
		spf = new SharedPreferencesUtils(baseActivity, AlarmService.ALARM_SER);
		list = (List<EventBean>) spf.getObject(LoginUtil.getUserId(baseActivity), EventBean.class);
	}
	
	@Override
	protected void initView() {
		super.initView();
		requestRemindList();
	}
	

	@OnClick(R.id.rlAdd)
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.rlAdd:
			 Intent intent = new Intent();
			 intent.setClass(baseActivity, RemindManageActivity.class);
			 Bundle bundle = new Bundle();
			 bundle.putString(RemindManageActivity.DATE,showDate);
			 bundle.putString(RemindManageActivity.OPRATE, "1");
			 bundle.putString(RemindManageActivity.DAY_OF_WEEK, CalendarUtils.getWeekDay(showDateFormat));
			 intent.putExtras(bundle);
			 baseActivity.startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
	}
	
	class SlideAdapter extends android.widget.BaseAdapter {

       
    	public SlideAdapter(){
    		//initList();
    	}
		@Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.xms_item_remind, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final EventBean item = itemList.get(position);
            if (item != null) {
            	holder.tvRemindTime.setText(item.getRemindtime());
            	//holder.tvEndTime.setText("结束时间："+item.getEndtime());
            	//holder.tvRecycleType.setText(item.getType());
            	holder.tvContent.setText(item.getContent());
            }
            holder.ivDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					for (Integer p : isSelected.keySet()) {
						isSelected.put(p, false);
					}
					holder.llOperate.setVisibility(View.GONE);
					requestDeleteEvent(itemList.get(position).getEventId());
				}
			});
            holder.ivEdit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//dayOfWeek(selectPostion);
					for (Integer p : isSelected.keySet()) {
						isSelected.put(p, false);
					}
					holder.llOperate.setVisibility(View.GONE);
					Intent intent = new Intent();
					intent.setClass(baseActivity, RemindManageActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString(RemindManageActivity.DATE,showDate);
			    	bundle.putString(RemindManageActivity.OPRATE, "2");
			    	bundle.putString(RemindManageActivity.DAY_OF_WEEK,  CalendarUtils.getWeekDay(showDateFormat));
			    	bundle.putSerializable(RemindManageActivity.EVENT, itemList.get(position));
			    	intent.putExtras(bundle);
			    	baseActivity.startActivityForResult(intent, 2);
				}
			});
            holder.rlRemind.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					currentPosition = position;
					if(isSelected.size()!=0){
						boolean flag = !isSelected.get(position);
						// 先将所有的置为FALSE
						for (Integer p : isSelected.keySet()) {
							isSelected.put(p, false);
						}
						// 再将当前选择实际状态
						isSelected.put(position, flag);
						SlideAdapter.this.notifyDataSetChanged();
						beSelectedData.clear();
						if (flag)
							beSelectedData.add(itemList.get(position));
					}
				
				}
			});
            if(isSelected.size()!=0){
            	 if(isSelected.get(position)){
                 	holder.llOperate.setVisibility(View.VISIBLE);
                 }else{
                 	holder.llOperate.setVisibility(View.GONE);
                 }
            }
           
            return convertView;
        }
        
	
    }

    private static class ViewHolder {
    	public TextView tvRemindTime;
        public TextView tvContent;
        public TextView tvRecycleType;
        public TextView tvEndTime;
        public LinearLayout llOperate;
        public RelativeLayout rlRemind;
        public ImageView ivDelete;
        public ImageView ivEdit;
        ViewHolder(View view) {
        	tvRemindTime = (TextView) view.findViewById(R.id.tvRemindTime);
        	tvContent = (TextView) view.findViewById(R.id.tvContent);
        	llOperate=(LinearLayout)view.findViewById(R.id.llOperate);
        	rlRemind=(RelativeLayout)view.findViewById(R.id.rlRemind);
        	ivDelete=(ImageView)view.findViewById(R.id.ivDel);
        	ivEdit=(ImageView)view.findViewById(R.id.ivEdit);
        	tvEndTime=(TextView)view.findViewById(R.id.tvEndTime);
        	tvRecycleType=(TextView)view.findViewById(R.id.tvRecycleType);
        	
        }
    }
    
    /**
     * 请求提醒日 
     * @param date 2016-12
     */
   /* private void requestRemindDate() {
    	Log.i("RemindServiceFragment", "requestRemindDate...");
    	
    	int year=dateAdapter.getCurrentYear(0);
    	int month=dateAdapter.getCurrentMonth(0);
    	int day=Integer.parseInt(dayNumbers[0]);
        String date = year + "/" + 
        			(month<10?"0"+month:month)+"/"+(day<10?"0"+day:day);
    	System.out.println("-------------》》》"+date);
    	try {
			// 生成CSP XML报文
			CspXmlXmsCom cspXmlXmsCom = new CspXmlXmsCom(LoginUtil.getUserId(baseActivity), "MS002006");
			cspXmlXmsCom.setDate(date);
			String strXml = cspXmlXmsCom.getCspXml();
			Log.i("tag", "getCspXml");
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
					EventDateXmlBean eventDateXmlBean = EventDateResp.readStringXml(responStr);
					String code = eventDateXmlBean.getErrorcode();
					if ("00".equals(code) || "01".equals(code)) {
						timeList.clear();
						if ("00".equals(code)) {
							timeList.addAll(eventDateXmlBean.getTimeList());
						}
						dateAdapter.notifyDataSetChanged();
					}
				}

				@Override
				public void onFinish() {
				}
				@Override
				public void onFailure(String responStr) {
					Toast.makeText(baseActivity, responStr, Toast.LENGTH_SHORT).show();
				}
			}, false);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    */
    
    /**
     * 请求提醒列表
     * 
     */
    private void requestRemindList() {
    	Log.i("RemindServiceFragment", "requestRemindList...");
    	try {
			// 生成CSP XML报文
			CspXmlXmsCom cspXmlXmsCom = new CspXmlXmsCom(LoginUtil.getUserId(baseActivity), "MS002001");
			cspXmlXmsCom.setDate("");
			String strXml = cspXmlXmsCom.getCspXml();
			Log.i("tag", "getCspXml");
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
					EventListXmlBean eventListXmlBean = EventListResp.readStringXml(responStr);
					if (!"00".equals(eventListXmlBean.getErrorcode())) {
						if ("01".equals(eventListXmlBean.getErrorcode())) {
							itemList.clear();
							adapter.notifyDataSetChanged();
						} else {
							Toast.makeText(baseActivity, eventListXmlBean.getErrormsg(), Toast.LENGTH_SHORT).show();
						}
					} else {
						final List<EventBean> eventList = eventListXmlBean.getEventList();
						if (eventList != null && eventList.size() > 0) {
							/*String sDate=dateAdapter.getCurrentYear(selectPostion)+"年"+dateAdapter.getCurrentMonth(selectPostion)+"月"+dayNumbers[selectPostion]+"日";
							String cDate=year_c+"年"+month_c+"月"+day_c+"日";
							if(cDate.equals(sDate)){
							    activity.setEventList(eventList);
							}*/
							itemList.clear();
							itemList.addAll(eventList);
							for (int i = 0; i < itemList.size(); i++) {
				    			isSelected.put(i, false);
				    		}
				    		// 清除已经选择的项
				    		if (beSelectedData.size() > 0) {
				    			beSelectedData.clear();
				    		}
							adapter.notifyDataSetChanged();
							new AsyncTask<String, Integer, String>(){

								@Override
								protected String doInBackground(String... params) {
									
									// 数据是否变化
									boolean isDataChange = false;
									for (int i = 0; i < eventList.size(); i++) {
										EventBean eventBean = eventList.get(i);
										if (list == null) {
											list = new ArrayList<EventBean>();
											list.addAll(eventList);
											isDataChange = true;
										} else {
											int count = 0;
											for (int j = 0; j < list.size(); j++) {
												EventBean mEventBean = list.get(j);
												//不同事件
												if (!eventBean.getEventId().equals(mEventBean.getEventId())) {
													count++;
												} 
											}
											//本地未存在该事件
											if (count == list.size()) {
												Log.i("RemindServiceFragment", "新增事件...");
												list.add(eventBean);
												isDataChange = true;
											} 
										}
									}
									if (isDataChange) {
										spf.setObject(LoginUtil.getUserId(baseActivity), list);
									}
									return null;
								}

								@Override
								protected void onPostExecute(String result) {
									super.onPostExecute(result);
									// 开始闹铃服务
									AlarmServiceManager.getInstance().startAlarmService(baseActivity);
							 }}.execute("");
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
			}, true);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 处理提醒时间
     * @param remindtime
     * @return
     */
    public String handleRemindTime(String time) {
    	String remindtime = dateAdapter.getCurrentYear(selectPostion) + "-" + 
				dateAdapter.getCurrentMonth(selectPostion) + "-" + dayNumbers[selectPostion];
    	if (time != null && time.length() > 4) {
    		remindtime = remindtime + " " + time.substring(0, 5);
    	}
    	Log.i("RemindServiceFragment", "处理过的时间： " + remindtime);
    	return remindtime;
    }
    
    /**
     * 处理事件对象
     * @param eventBean
     * @return
     */
    public EventBean handleEventBean(EventBean eventBean) {
    	EventBean mEventBean = new EventBean();
		mEventBean.setEventId(eventBean.getEventId());
		mEventBean.setContent(eventBean.getContent());
		mEventBean.setType(eventBean.getType());
		mEventBean.setRemindtime(handleRemindTime(eventBean.getRemindtime()));
		return mEventBean;
    } 
    
	/**
	 * 请求删除事件
	 * @param eventId
	 */
	private void requestDeleteEvent(final String eventId) {
		try {
			// 生成CSP XML报文
			CspXmlXmsCom cspXmlXmsCom = new CspXmlXmsCom(LoginUtil.getUserId(baseActivity), "MS002002");
			cspXmlXmsCom.setEventId(eventId != null ? eventId : "");
			String strXml = cspXmlXmsCom.getCspXml();
			Log.i("tag", "getCspXml");
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			//Log.i("tag", "Mcis");
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(baseActivity);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
//			cspUtil.setTest(true);
			cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					EventComXmlBean eventComXmlBean = EventComResp.readStringXml(responStr);
					if (!eventComXmlBean.getErrorcode().equals("00")) {
						Toast.makeText(baseActivity, eventComXmlBean.getErrormsg(), Toast.LENGTH_SHORT).show();
					} else{
						itemList.remove(currentPosition);
						adapter.notifyDataSetChanged();
						//activity.requestRemindList();
						//activity.setEventList(itemList);
						//requestRemindDate();
						if (list != null) {
							for (int i = 0; i < list.size(); i++) {
								EventBean eventBean = list.get(i);
								if (eventId.equals(eventBean.getEventId())) {
									list.remove(eventBean);
									spf.setObject(LoginUtil.getUserId(baseActivity), list);
									break;
								}
							}
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
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			//requestRemindDate();
			requestRemindList();
			//activity.requestRemindList();
		}
	}
	
}
