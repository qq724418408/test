package com.bocop.xfjr.util.image.loader;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.util.image.modle.IImgLoader;
import com.bocop.xfjr.util.image.modle.ImageOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import android.content.Context;
import android.widget.ImageView;

public class GlideLoader implements IImgLoader{

	@SuppressWarnings("deprecation")
	@Override
	public void load(Context context, String url, ImageView view, ImageOption option) {
		 Glide.with(context).load(url).placeholder(context.getResources().getDrawable(R.drawable.xfjr_pretrial_photo)).transform(new CenterCrop(context),new RoundCornerTransform(context,10)).into(view);
	}

}
