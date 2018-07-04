package com.bocop.xfjr.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * description：自定义view不能满足需求
 * <p/>
 * Created by TIAN FENG on 2017/9/5
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class EditChangeUtils{
	
    public void addChangeListener(final EditText editText, final ImageView imageView){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    imageView.setVisibility(View.VISIBLE);
                }else {
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

}
