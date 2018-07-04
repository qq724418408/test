package com.bocop.jxplatform.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.jx.ab.view.sliding.AbSlidingPlayView;
import com.boc.jx.ab.view.sliding.AbSlidingPlayView.AbOnItemClickListener;
import com.boc.jx.base.BaseActivity;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.R.drawable;
import com.bocop.jxplatform.R.id;
import com.bocop.jxplatform.R.layout;
import com.bocop.jxplatform.activity.riders.CyhAdvDetailActivity;
import com.boc.jx.baseUtil.asynchttpclient.RequestParams;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.common.util.AbImageUtil;
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.activity.riders.RiderFristActivity;
import com.bocop.jxplatform.bean.Advertisement;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.LoginUtilAnother;
import com.bocop.xms.activity.ZqtActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ZqtFirstActivity extends BaseActivity implements LoginUtilAnother.ILoginListener {

    private LinearLayout zhongyinguoji;
    private LinearLayout haitong;
    private LinearLayout guosheng;
    private LinearLayout dongbei;
    private TextView tv_titleName;
    private ImageView iv_title_left;



    private  boolean adFirstDownFinish = false;
    protected BaseActivity baseActivity;
    private List<Advertisement> mAdvList = new ArrayList<Advertisement>();
    /*
    * 轮播图
    * */
    AbSlidingPlayView pv_playview;
    static final int ASPECT_X = 4, ASPECT_Y = 1;

    //广告个数
    private  boolean adfinishFlg = false;
    private Bitmap bitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initView();
        setListener();
    }

    private void initView() {
        setContentView(R.layout.activity_zqt_first);
        tv_titleName = (TextView) findViewById(R.id.tv_titleName);
        tv_titleName.setText("证券通");
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        pv_playview = (AbSlidingPlayView) findViewById(R.id.zqt_photos);
        zhongyinguoji = (LinearLayout) findViewById(R.id.zhongyinguoji);
        haitong = (LinearLayout) findViewById(R.id.haitong);
        guosheng = (LinearLayout) findViewById(R.id.guosheng);
        dongbei = (LinearLayout) findViewById(R.id.dongbei);
        try{
            initHeaderView();
        }
        catch(Exception ex){

        }
    }

    //线程获取图片后，hander来更新ui
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //完成主界面更新
                    View view = getLayoutInflater().inflate(R.layout.item_adpic, null);
                    ImageView iv = (ImageView) view.findViewById(R.id.iv_photo);
                    iv.setImageBitmap(bitmap);

                    if(!adFirstDownFinish){
                        pv_playview.removeAllViews();
                        adFirstDownFinish = true;
                    }

                    pv_playview.addView(iv);
                    if(adfinishFlg)pv_playview.startPlay();
                    break;
                default:
                    break;
            }
        }
    };

    private void initHeaderView() {
        pv_playview.setNavHorizontalGravity(Gravity.RIGHT);
        Drawable iv_playviewindex_off = getResources().getDrawable(
                R.drawable.iv_playviewindex_off);
        Drawable iv_playviewindex_on = getResources().getDrawable(
                R.drawable.iv_playviewindex_on);
        pv_playview.setPageLineImage(
                AbImageUtil.drawableToBitmap(iv_playviewindex_on),
                AbImageUtil.drawableToBitmap(iv_playviewindex_off));
        initPlayViewSize();
        initPlayViewContent();
        pv_playview.setPlayDuration(3000);
    }

    private void initPlayViewSize() {
        final ViewTreeObserver vto = pv_playview.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            boolean hasSetted = false;

            @Override
            public void onGlobalLayout() {
                if (hasSetted)
                    return;
                hasSetted = true;
                ViewGroup.LayoutParams params = pv_playview.getLayoutParams();
                int w = pv_playview.getMeasuredWidth();
                int h = w / ASPECT_X * ASPECT_Y;
                // 宽度放开的话就会根据宽高比进行适配
                // params.height = h;
                pv_playview.setLayoutParams(params);
                try {
                    vto.removeGlobalOnLayoutListener(this);
                } catch (Exception e) {
                }
            }
        });
    }

    private void initPlayViewContent() {
        getAdPhotos();
    }

    void getAdPhotos() {
        pv_playview.removeAllViews();
        // 第一张
        Advertisement adv1 = new Advertisement();
        adv1.setImageRes(R.drawable.zqt_photo);
        adv1.setContent(Constants.ZQTphoto_1);
        // // 网页url
        mAdvList.add(adv1);
        for (Advertisement advertisement : mAdvList) {
            View view = getLayoutInflater().inflate(R.layout.item_adpic,
                    null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_photo);
            iv.setImageResource(advertisement.getImageRes());
            pv_playview.addView(iv);
        }
        pv_playview.startPlay();

    }


    /**
     * 设置监听
     */
    private void setListener() {
        iv_title_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });


////         轮播图的点击事件
//         pv_playview.setOnItemClickListener(new AbOnItemClickListener() {
//         @Override
//         public void onClick(int position) {
//         Intent intent = new Intent(ZQTActivity.this,
//                 CyhAdvDetailActivity.class);
//         intent.putExtra("url", mAdvList.get(position).getContent());
//         startActivity(intent);
//         }
//         });

        //         轮播图的点击事件
        pv_playview.setOnItemClickListener(new AbOnItemClickListener() {
            @Override
            public void onClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("url", Constants.qztUrlForOpen);
                bundle.putString("name", "证券开户");
                Intent intent = new Intent(ZqtFirstActivity.this,
                        WebActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // 中银国际证券
        zhongyinguoji.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ZqtFirstActivity.this,
                        ZqtActivity.class);
                startActivity(intent);
            }
        });

        haitong.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Bundle bundleConsult = new Bundle();
                bundleConsult.putString("url", Constants.ZQThaitong);
                bundleConsult.putString("name", "海通证券");
                Intent intentConsult = new Intent(ZqtFirstActivity.this,
                        WebActivity.class);
                intentConsult.putExtras(bundleConsult);
                startActivity(intentConsult);

            }
        });

        guosheng.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Bundle bundleConsult = new Bundle();
                bundleConsult.putString("url", Constants.ZQTguosheng);
                bundleConsult.putString("name", "国盛证券");
                Intent intentConsult = new Intent(ZqtFirstActivity.this,
                        WebActivity.class);
                intentConsult.putExtras(bundleConsult);
                startActivity(intentConsult);

            }
        });
        
        dongbei.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Bundle bundleConsult = new Bundle();
                bundleConsult.putString("url", Constants.ZQTdongbei);
                bundleConsult.putString("name", "东北证券");
                Intent intentConsult = new Intent(ZqtFirstActivity.this,
                        WebActivity.class);
                intentConsult.putExtras(bundleConsult);
                startActivity(intentConsult);

            }
        });

    }


    // 实现登陆的接口
    @Override
    public void onLogin(int position) {

    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onCancle() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onException() {

    }

}

