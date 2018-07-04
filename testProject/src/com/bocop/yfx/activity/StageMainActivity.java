package com.bocop.yfx.activity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;
import com.bocop.yfx.base.EShareBaseActivity;
import com.bocop.yfx.fragment.StageProDetailFragment;
import com.bocop.yfx.fragment.StageRecordFragment;

/**
 * 
 * 现金分期
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.yfx_activity_main_stage)
public class StageMainActivity extends EShareBaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.group)
	private RadioGroup group;
	@ViewInject(R.id.radioProDetail)
	private RadioButton radioProDetail;
	@ViewInject(R.id.radioRecords)
	private RadioButton radioRecords;

	public static boolean BACK_FLAG = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tvTitle.setText("现金分期");
		initListener();

		radioProDetail.setChecked(true);
	}

	private void initListener() {
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radioProDetail:
					replaceFragment(R.id.llFragment,
							StageProDetailFragment.class);
					break;

				case R.id.radioRecords:
					tvTitle.setText("分期记录");
					replaceFragment(R.id.llFragment, StageRecordFragment.class);
					break;
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (BACK_FLAG) {
			BACK_FLAG = false;
			radioRecords.setChecked(true);
		}
	}
}
