package com.arlen.frame.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

import com.arlen.frame.R;
import com.arlen.frame.common.utils.DensityUtils;


/***
 * 
 * @author Arlen
 *
 */
public class FlowLayout extends ViewGroup {

	private float mVerticalSpacing; // 每个item纵向间距
	private float mHorizontalSpacing;// 每个item横向间距
	private boolean mIsAnimation;

	public void setVerticalSpacing(float mVerticalSpacing) {
		this.mVerticalSpacing = mVerticalSpacing;
	}

	public void setHorizontalSpacing(float mHorizontalSpacing) {
		this.mHorizontalSpacing = mHorizontalSpacing;
	}

	public FlowLayout(Context context) {
		this(context, null);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.FlowLayout);
		if (ta != null) {
			mHorizontalSpacing = ta.getDimension(
					R.styleable.FlowLayout_HorizontalSpacing,
					DensityUtils.dp2px(getContext(), 12));
			mVerticalSpacing = ta.getDimension(
					R.styleable.FlowLayout_VerticalSpacing,
					DensityUtils.dp2px(getContext(), 8));
			mIsAnimation = ta.getBoolean(R.styleable.FlowLayout_isAnimation,false);
			ta.recycle();
		}

		if(mIsAnimation){
			setLayoutAnimation(getAnimationController());
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int selfWidth = resolveSize(0, widthMeasureSpec);

		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();

		int childLeft = paddingLeft;
		int childTop = paddingTop;
		int lineHeight = 0;

		// 通过计算每一个子控件的高度，得到自己的高度
		for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
			View childView = getChildAt(i);
			LayoutParams childLayoutParams = childView.getLayoutParams();
			childView.measure(
					getChildMeasureSpec(widthMeasureSpec, paddingLeft
							+ paddingRight, childLayoutParams.width),
					getChildMeasureSpec(heightMeasureSpec, paddingTop
							+ paddingBottom, childLayoutParams.height));
			int childWidth = childView.getMeasuredWidth();
			int childHeight = childView.getMeasuredHeight();

			lineHeight = Math.max(childHeight, lineHeight);

			if (childLeft + childWidth + paddingRight > selfWidth) {
				childTop += mVerticalSpacing + lineHeight;
				childLeft = paddingLeft;
				lineHeight = childHeight;
			} else {
				childLeft += childWidth + mHorizontalSpacing;
			}
		}

		int wantedHeight = childTop + lineHeight + paddingBottom+200;
		setMeasuredDimension(selfWidth,
				resolveSize(wantedHeight, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int myWidth = r - l;

		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();

		int childLeft = paddingLeft;
		int childTop = paddingTop;

		int lineHeight = 0;

		// 根据子控件的宽高，计算子控件应该出现的位置。
		for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
			View childView = getChildAt(i);

			if (childView.getVisibility() == View.GONE) {
				continue;
			}

			int childWidth = childView.getMeasuredWidth();
			int childHeight = childView.getMeasuredHeight();

			lineHeight = Math.max(childHeight, lineHeight);

			if (childLeft + childWidth + paddingRight > myWidth) {
				childLeft = paddingLeft;
				childTop += mVerticalSpacing + lineHeight;
				lineHeight = childHeight;
			}
			childView.layout(childLeft, childTop, childLeft + childWidth,
					childTop + childHeight);
			childLeft += childWidth + mHorizontalSpacing;
		}
	}

	/**
	 * layout动画
	 */
	protected LayoutAnimationController getAnimationController() {
		int duration = 300;
		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(duration);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(duration);
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.5f);
		controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
		return controller;
	}

}
