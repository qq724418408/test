package com.bocop.xyd.activity;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.xyd.adapter.ViewPageAdapter;
import com.bocop.xyd.base.XydBaseActivity;
import com.bocop.xyd.fragment.RepayFragment;
import com.bocop.yfx.utils.ToastUtils;

import android.R.bool;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 还款记录以及用款记录列表
 * 
 * @author formssi
 *
 */
public class Xyd_RepayActivity extends XydBaseActivity {

	@ViewInject(R.id.ll_repay_container)
	ViewPager viewPager;

	@ViewInject(R.id.rg_repay_select)
	RadioGroup rgRepaySelect;
	@ViewInject(R.id.rb_repay_nowrepay)
	RadioButton rbRepayNowRepay;
	@ViewInject(R.id.rb_repay_record)
	RadioButton rbRepayRecord;

	public static final int SELECT_NOW = 1;
	public static final int SELECT_RECORD = 2;
	private int tag = SELECT_NOW;

	List<Fragment> viewList = new ArrayList<>();

	@Override
	protected int getLoyoutId() {
		return R.layout.xyd_activity_repay;
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
		
		tag=getIntent().getIntExtra("STYLE", SELECT_NOW);
		Log.e("TGA=======>>", tag+"");
		initViewPager();
		if (tag == SELECT_RECORD) {
			changeSelect(1);
			viewPager.setCurrentItem(1);
		} else {
			changeSelect(0);
			viewPager.setCurrentItem(0);
		}
		
	}

	private void initViewPager() {
		Fragment fragment = new RepayFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("style", SELECT_NOW);
		fragment.setArguments(bundle);
		viewList.add(0, fragment);
		Fragment fragment2 = new RepayFragment();
		fragment2 = new RepayFragment();
		Bundle bundle2 = new Bundle();
		bundle2.putInt("style", SELECT_RECORD);
		fragment2.setArguments(bundle2);
		viewList.add(1, fragment2);
		ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager(), viewList);
		viewPager.setAdapter(adapter);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				changeSelect(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	private void changeSelect(int i) {
		if (i == 0) {
			rbRepayNowRepay.setChecked(true);
			setTextBold(rbRepayNowRepay, true);
			setTextBold(rbRepayRecord, false);
		} else {
			rbRepayRecord.setChecked(true);
			setTextBold(rbRepayNowRepay, false);
			setTextBold(rbRepayRecord, true);
		}
	}

	private void setTextBold(TextView tv, boolean boo) {
		TextPaint tp = tv.getPaint();
		tp.setFakeBoldText(boo);
	}

	@OnClick({ R.id.ll_repay_back, R.id.rb_repay_nowrepay ,R.id.rb_repay_record})
	public void back(View view) {
		switch (view.getId()) {
		case R.id.ll_repay_back:
			this.finish();
			break;
		case R.id.rb_repay_nowrepay:
			changeSelect(0);
			viewPager.setCurrentItem(0);
			break;
		case R.id.rb_repay_record:
			changeSelect(1);
			viewPager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}
	
	public static void  StartThisActivity(Context context,int style) {
		Intent i=new Intent(context,Xyd_RepayActivity.class);
		i.putExtra("STYLE", style);
		context.startActivity(i);
	}
}
