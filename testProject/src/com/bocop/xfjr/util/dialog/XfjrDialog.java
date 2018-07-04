package com.bocop.xfjr.util.dialog;

import com.bocop.jxplatform.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description : 消费金融，万能dialog
 * <p/>
 * Created : TIAN FENG
 * Date : 2017/7/31
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class XfjrDialog extends Dialog {

	private AlertController mAlert;

	public XfjrDialog(Context context, int themeResId) {
		super(context, themeResId);
		mAlert = new AlertController(this, getWindow());
	}

	/**
	 * 设置文本
	 *
	 * @param viewId
	 * @param text
	 */
	public void setText(int viewId, CharSequence text) {
		mAlert.setText(viewId, text);
	}

	public <T extends View> T getView(int viewId) {
		return mAlert.getView(viewId);
	}

	/**
	 * 设置点击事件
	 *
	 * @param viewId
	 * @param listener
	 */
	public void setOnclickListener(int viewId, View.OnClickListener listener) {
		mAlert.setOnclickListener(viewId, listener);
	}

	public static class Builder {

		protected final AlertController.AlertParams P;

		public Builder(Context context) {
			this(context, R.style.xfjr_dialog);
		}

		public Builder(Context context, int themeResId) {
			P = new AlertController.AlertParams(context, themeResId);
		}

		public Builder setContentView(View view) {
			P.mView = view;
			P.mViewLayoutResId = 0;
			return this;
		}

		// 设置布局内容的layoutId
		public Builder setContentView(int layoutId) {
			P.mView = null;
			P.mViewLayoutResId = layoutId;
			return this;
		}

		// 设置文本
		public Builder setText(int viewId, CharSequence text) {
			P.mTextArray.put(viewId, text);
			return this;
		}

		// 设置点击事件
		public Builder setOnClickListener(int viewId, View.OnClickListener listener) {
			P.mClickArray.put(viewId, listener);
			return this;
		}

		// 配置一些万能的参数
		public Builder fullWidth() {
			P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
			return this;
		}

		/**
		 * 从底部弹出
		 * 
		 * @param isAnimation
		 *            是否有动画
		 * @return
		 */
		public Builder formBottom(boolean isAnimation) {
			if (isAnimation) {
				P.mAnimations = R.style.xfjr_dialog_from_bottom_anim;
			}
			P.mGravity = Gravity.BOTTOM;
			return this;
		}

		/**
		 * 设置Dialog的宽高
		 * 
		 * @param width
		 * @param height
		 * @return
		 */
		public Builder setWidthAndHeight(int width, int height) {
			P.mWidth = width;
			P.mHeight = height;
			return this;
		}

		/**
		 * 添加默认动画
		 * 
		 * @return
		 */
		public Builder addDefaultAnimation() {
			P.mAnimations = R.style.xfjr_dialog_scale_anim;
			return this;
		}

		/**
		 * 设置动画
		 * 
		 * @param styleAnimation
		 * @return
		 */
		public Builder setAnimations(int styleAnimation) {
			P.mAnimations = styleAnimation;
			return this;
		}

		public Builder setCancelable(boolean cancelable) {
			P.mCancelable = cancelable;
			return this;
		}

		public Builder setOnCancelListener(OnCancelListener onCancelListener) {
			P.mOnCancelListener = onCancelListener;
			return this;
		}

		public Builder setOnDismissListener(OnDismissListener onDismissListener) {
			P.mOnDismissListener = onDismissListener;
			return this;
		}

		public Builder setOnKeyListener(OnKeyListener onKeyListener) {
			P.mOnKeyListener = onKeyListener;
			return this;
		}

		public XfjrDialog create() {
			if (P.mContext == null || P.mThemeResId == -1) {
				return null;
			}
			final XfjrDialog dialog = new XfjrDialog(P.mContext, P.mThemeResId);
			P.apply(dialog.mAlert);
			dialog.setCancelable(P.mCancelable);
			if (P.mCancelable) {
				dialog.setCanceledOnTouchOutside(true);
			}
			dialog.setOnCancelListener(P.mOnCancelListener);
			dialog.setOnDismissListener(P.mOnDismissListener);
			if (P.mOnKeyListener != null) {
				dialog.setOnKeyListener(P.mOnKeyListener);
			}
			return dialog;
		}

		public XfjrDialog show() {
			final XfjrDialog dialog = create();
			if (dialog != null)
				dialog.show();
			return dialog;
		}
	}
}
