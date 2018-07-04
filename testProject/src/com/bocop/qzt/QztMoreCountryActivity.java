/**
 * 
 */
package com.bocop.qzt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.boc.jx.base.BaseActivity;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.QztCountry;
import com.bocop.jxplatform.bean.QztCountryDetail;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.QztRequest;
import com.bocop.jxplatform.util.ShuZu;
import com.bocop.qzt.adapter.CityListAdapter;
import com.bocop.qzt.view.BladeView;
import com.bocop.qzt.view.BladeView.OnItemClickListener;
import com.bocop.qzt.view.ContactSortModel;
import com.bocop.qzt.view.PinnedHeaderListView;
import com.bocop.qzt.view.PinyinComparator;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2017-5-12 下午2:57:10 
 * 类说明 
 */
/**
 * @author zhongye
 * 
 */
public class QztMoreCountryActivity extends BaseActivity {

	private static final int COPY_DB_SUCCESS = 10;
	private static final int COPY_DB_FAILED = 11;
	protected static final int QUERY_CITY_FINISH = 12;

	private MySectionIndexer mIndexer;

	private List<QztCountryDetail> countrysList = new ArrayList<QztCountryDetail>();
	private List<ContactSortModel> SourceDateList;

	private CityListAdapter mAdapter;
	private  String ALL_CHARACTER = "" ;
	protected static final String TAG = null;

//	private String[] sections = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
//			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
//			"W", "X", "Y", "Z" };
	private String[] sections;
	
	private int[] counts;
	private PinnedHeaderListView mListView;
	BladeView mLetterListView;

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case QUERY_CITY_FINISH:
				Log.i("tag", "QUERY_CITY_FINISH:" + String.valueOf(sections.length) + sections[2] );
				if (mAdapter == null) {
					Collections.sort(SourceDateList, new PinyinComparator());
					mIndexer = new MySectionIndexer(sections, counts);
					Log.i("tag", "MySectionIndexer");
					mAdapter = new CityListAdapter(SourceDateList, mIndexer,
							getApplicationContext());
					mListView.setAdapter(mAdapter);
					
					mLetterListView.refreshBladeView(sections);

					mListView.setOnScrollListener(mAdapter);

					// 設置頂部固定頭部
					mListView.setPinnedHeaderView(LayoutInflater.from(
							getApplicationContext()).inflate(
							R.layout.list_group_item, mListView, false));

				} else if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}

				break;

			case COPY_DB_SUCCESS:
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("tag", "savedInstanceState");
		super.onCreate(savedInstanceState);
		
		
		Log.i("tag", "onCreate");
		setContentView(R.layout.qzt_morecountry_activity);
		Log.i("tag", "onCreate");
		findView();
		Log.i("tag", "findView");
		requestBocopForcountry();

	}

	private void requestBocopForcountry() {
		QztRequest qztRequest = new QztRequest(QztMoreCountryActivity.this);

		qztRequest.postOpboc(BocSdkConfig.qztApplyurl,
				new com.bocop.jxplatform.util.QztRequest.CallBackBoc() {

					@Override
					public void onSuccess(String responStr) {
						Log.i("tag1", responStr);
						try {
							QztCountry qztCountry;
							qztCountry = JsonUtils.getObject(responStr,
									QztCountry.class);
							Log.i("tag", "body 调用完毕。");
							if (qztCountry != null) {
								countrysList = qztCountry.getCountrys();
								Log.i("tag", "body 调用完毕。countrysList");
								SourceDateList = filledData(countrysList);
								Log.i("tag", "body 调用完毕。SourceDateList");
								handler.sendEmptyMessage(QUERY_CITY_FINISH);
							} else {
								handler.sendEmptyMessage(QUERY_CITY_FINISH);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(String responStr) {
						CspUtil.onFailure(QztMoreCountryActivity.this,
								responStr);
					}
				});
	}

	private List<ContactSortModel> filledData(List<QztCountryDetail> list) {
		List<ContactSortModel> mSortList = new ArrayList<>();
		ArrayList<String> indexString = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			ContactSortModel sortModel = new ContactSortModel();
			sortModel.setName(list.get(i).getName());
			sortModel.setId(list.get(i).getId());
			String pinyin = PinyinUtils.getPingYin(list.get(i).getName());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
				if (!indexString.contains(sortString)) {
					indexString.add(sortString);
				}
			}
			mSortList.add(sortModel);
		}
		Collections.sort(indexString);
		sections = new String[indexString.size()];
		for(int i = 0;i < indexString.size();i++){
			sections[i] = indexString.get(i);
			ALL_CHARACTER = ALL_CHARACTER +  sections[i];
		}
		Log.i("tag", "sections" + ALL_CHARACTER);
		// 初始化每个字母有多少个item
		counts = new int[sections.length];
		Log.i("tag",String.valueOf(sections.length) );
		for (ContactSortModel contactSortModel : mSortList) { // 计算全部城市

			String firstCharacter = contactSortModel.getSortLetters();
			int index = ALL_CHARACTER.indexOf(firstCharacter);
			counts[index]++;
			Log.i("tag",String.valueOf(index) );
		}
		Log.i("tag", "counts");
		return mSortList;
	}

	private void findView() {

		mListView = (PinnedHeaderListView) findViewById(R.id.mListView);
//		mLetterListView = new  BladeView(QztMoreCountryActivity.this,sections);
		mLetterListView = (BladeView) findViewById(R.id.mLetterListView);

		mLetterListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(String s) {
				if (s != null) {

					int section = ALL_CHARACTER.indexOf(s);

					int position = mIndexer.getPositionForSection(section);

					Log.i(TAG, "s:" + s + ",section:" + section + ",position:"
							+ position);

					if (position != -1) {
						mListView.setSelection(position);
					} else {

					}
				}

			}
		});
	}

}
