package com.bocop.xfjr.activity;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.adapter.XfjrCommonSearchAdapter;
import com.bocop.xfjr.adapter.BusinessListSearchAdapter;
import com.bocop.xfjr.adapter.recycleradapter.CommonRecyclerAdapter;
import com.bocop.xfjr.base.XfjrBaseActivity;
import com.bocop.xfjr.bean.BusinessBean;
import com.bocop.xfjr.bean.ErrorBean;
import com.bocop.xfjr.bean.MerchantBusiness;
import com.bocop.xfjr.bean.TypeBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.util.LimitInputTextWatcher;
import com.bocop.xfjr.util.XFJRUtil;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 搜索页面
 * 
 * @author formssi
 *
 */
@ContentView(R.layout.xfjr_activity_search)
public class XFJRSearchActivity extends XfjrBaseActivity implements android.view.View.OnClickListener {
	// 普通搜索
	public static final int MANAGER_SEARCH = 1;
	// 详情搜索
	public static final int MERCHANT_SEARCH = 2;

	/**
	 * 跳转此页面
	 * 
	 * @param type
	 *            是普通搜索 还是明细搜索
	 */
	public static void goMe(Context context, int type) {
		Intent intent = new Intent(context, XFJRSearchActivity.class);
		intent.putExtra("TYPE", type);
		context.startActivity(intent);
	}

	/**********************************************************************************************/
	// 搜索框
	@ViewInject(R.id.et_search_content)
	private EditText etSearchContent;
	// 底部查询按钮
	@ViewInject(R.id.tv_search_button)
	private TextView tvSearchButton;
	// 取消按钮
	@ViewInject(R.id.tv_search_cancle)
	private TextView tvSearchCancle;
	// 列表
	@ViewInject(R.id.recyclerView)
	private RecyclerView recyclerView;
	@ViewInject(R.id.tvHint)
	private TextView tvHint;
	
	// 当前页码 上拉加载时注意
	private int mPage = 0;
	// 是从哪个页面跳转过来的
	private int mType = 0;
	// 客服经理列表 与商户列表数据一致
	private List<BusinessBean> listData = new ArrayList<>();
	// 是否自动加载
	private boolean mIsLoad = true;
	private MerchantBusiness mDataBean;
	private CommonRecyclerAdapter<BusinessBean> mAdapter;
	private TypeBean selectedType = null;
	private String fromData, toData;
	private LinearLayoutManager layoutManager;

//	@Override
//	protected int getLoyoutId() {
//		return R.layout.xfjr_activity_search;
//	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
		initView();
	}

//	@Override
	protected void initData() {
		mType = getIntent().getIntExtra("TYPE", -1);
		if (mType== -1) {
			throw new IllegalArgumentException("请携带跳转页面的参数 ");
		}
		// recyclerView为list样式
		recyclerView.setLayoutManager(layoutManager = new LinearLayoutManager(this));
		initListViewAdapter();
		initListner();
	}

//	@Override
	protected void initView() {
		/**
		 * 监听绘制结束
		 */
		etSearchContent.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				etSearchContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				// 设置hint的搜索按钮
				setHinit();
			}
		});
		
		// 捕获软键盘的确定按钮
		etSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {  
		    @Override  
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
		    	 switch (actionId) {
		            case EditorInfo.IME_ACTION_NONE:
		            case EditorInfo.IME_ACTION_GO:
		            case EditorInfo.IME_ACTION_SEARCH:
		            case EditorInfo.IME_ACTION_DONE:
		            case EditorInfo.IME_ACTION_SEND:
		            case EditorInfo.IME_ACTION_NEXT:
		            	if(!TextUtils.isEmpty(etSearchContent.getText().toString())){
			        		mPage = 0;
			    			getSearchResult(true);
			        	}
		            	// 隐藏软键盘
			        	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
			        	imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			        	 return true;  
		        }
//		        if (actionId == EditorInfo.IME_ACTION_SEARCH||actionId == EditorInfo.IME_ACTION_DONE){  
//		        	if(!TextUtils.isEmpty(etSearchContent.getText().toString())){
//		        		mPage = 0;
//		    			getSearchResult(true);
//		        	}
//		        	// 隐藏软键盘
//		        	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
//		        	imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//		            return true;  
//		        }  
		        return false;  
		    }  
		});  

		

	}

	private void initListner() {
//		tvSearchButton.setOnClickListener(this);

//		tvSearchCancle.setOnClickListener(this);
		etSearchContent.addTextChangedListener(new LimitInputTextWatcher(etSearchContent,LimitInputTextWatcher.a_zA_Z_0_9_CN_REGEX));
		/**
		 * 自动加载
		 */
		recyclerView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					int lastItemPosition = layoutManager.findLastVisibleItemPosition();
					if (lastItemPosition >= layoutManager.getItemCount() - 1) {
							++mPage;
							// 页面加一 自动加载
							if (mDataBean==null||mDataBean.getBusiness()==null||mDataBean.getBusiness().size()==0||!mIsLoad) {
								return ;
							}
							getSearchResult(false);
					}
				}
			}
		});
	}

	@OnClick({R.id.tv_search_button,R.id.tv_search_cancle})
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_search_button:
			mPage = 0;
			getSearchResult(true);
			break;
		case R.id.tv_search_cancle:
			startActivity(new Intent(this, XfjrIndexActivity.class));
			break;

		default:
			break;
		}

	}

	private void getSearchResult(final boolean isShowDialog) {
		
		if(XfjrMain.isNet){
			HttpRequest.getSearchResult(isShowDialog,this,etSearchContent.getText().toString(),mPage,mType==1,new IHttpCallback<MerchantBusiness>() {

				@Override
				public void onSuccess(String url, MerchantBusiness result) {
					// TODO Auto-generated method stub
					mDataBean=result;
					// 显示dialog代表的是点击查询按钮，不是自动加载
					if(isShowDialog){
						listData.clear();	
						// 无数据提示
						if(result==null||result.getBusiness()==null||result.getBusiness().size()<=0){
							showToast("没有找到相应数据");
						}
					}
					listData.addAll(result.getBusiness());
					mIsLoad = result.getBusiness()!=null&&result.getBusiness().size()!=0;
					mAdapter.notifyDataSetChanged();
					checkShowHint();	
				}

				

				@Override
				public void onError(String url, Throwable e) {
					if (mPage == 0) {
						String json = e.getMessage();
						ErrorBean error = new Gson().fromJson(json, ErrorBean.class);
						if (UrlConfig.emptyCode.equals("" + error.code)) {
							listData.clear();
							mAdapter.notifyDataSetChanged();
							showToast(error.msg);
							return;
						}
						UrlConfig.showErrorTips(XFJRSearchActivity.this, e, true);
					}
					
				}

				@Override
				public void onFinal(String url) {
					
				}
			});
		}else{
			if(listData.size()<=1){
				for(int i=0;i<50;i++){
					listData.add(new BusinessBean("11111", "模拟种类"+i, "张三", "1000.00"));
				}
			}
			mAdapter.notifyDataSetChanged();
			checkShowHint();
		}
		
	}

	/**
	 * 是否显示提示
	 */
	private void checkShowHint() {
		tvHint.setVisibility(listData.size()==0?View.VISIBLE:View.GONE);
	}
	
	private void initListViewAdapter() {
		// 获取传过来的type
		int type = getIntent().getIntExtra("TYPE", 0);
		// 显示列表
		recyclerView.setVisibility(View.VISIBLE);
		
		switch (type) {
		// 商户详情搜索
		case MERCHANT_SEARCH:
			recyclerView.setAdapter(mAdapter = new XfjrCommonSearchAdapter(this, listData));
			break;
		// 客户经理搜索搜索 
		case MANAGER_SEARCH:
			recyclerView.setAdapter(mAdapter = new BusinessListSearchAdapter(this, listData));
			break;
		default:
			recyclerView.setVisibility(View.GONE);
			break;
		}

	}

	/**
	 * hint显示
	 */
	private void setHinit() {
		SpannableStringBuilder builder = new SpannableStringBuilder("0请输入搜索条件");
		MyIm imageSpan = new MyIm(this, R.drawable.xfjr_search_demand);
		builder.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		etSearchContent.setHint(builder);
	}

	public class MyIm extends ImageSpan {
		public MyIm(Context arg0, int arg1) {
			super(arg0, arg1);
		}

		private int height;

		public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
			FontMetricsInt fmPaint = paint.getFontMetricsInt();
			height = fmPaint.descent - fmPaint.ascent;
			Drawable d = getDrawable();
			d.setBounds(0, 0, height, height);
			Rect rect = d.getBounds();
			if (fm != null) {
				int fontHeight = fmPaint.bottom - fmPaint.top;
				int drHeight = rect.bottom - rect.top;

				int top = drHeight / 2 - fontHeight / 4;
				int bottom = drHeight / 2 + fontHeight / 4;

				fm.ascent = -bottom;
				fm.top = -bottom;
				fm.bottom = top;
				fm.descent = top;
			}
			return rect.right;
		}

		@Override
		public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
				Paint paint) {
			Drawable b = getDrawable();
			b.setBounds(0, 0, height, height);
			canvas.save();
			int transY = 0;
			transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
			canvas.translate(x, transY);
			b.draw(canvas);
			canvas.restore();
		}
	}
	
	/**
	 * 空白处点击事件
	 * 
	 * @param view
	 */
	@OnClick(R.id.lltBlank)
	private void clickBlank(View view) {
		XFJRUtil.hideSoftInput(view);
	}

}
