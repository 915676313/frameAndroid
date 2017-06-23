package com.arlen.frame.common.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Arlen View跟着手指拖动
 */
public class DragSlideLayout extends RelativeLayout {

    private View mDragView;
    private ViewDragHelper mDragHelper;

    public DragSlideLayout(Context context) {
        super(context);
        init();
    }

    public DragSlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragSlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        mDragView = getChildAt(childCount - 1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        return true;
    }

    class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mDragView;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - mDragView.getHeight() - getPaddingBottom();
            return Math.min(Math.max(top, topBound), bottomBound);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            final int leftBound = getPaddingLeft();
            final int rightBound = getWidth() - mDragView.getWidth() - getPaddingRight();
            return Math.min(Math.max(left, leftBound), rightBound);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth() - getPaddingRight();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            int leftBound = getPaddingLeft();
            int rightBound = getViewHorizontalDragRange(releasedChild);
            int toTarget = releasedChild.getX() > (getMeasuredWidth() / 2 - releasedChild.getMeasuredWidth() / 2) ? rightBound : leftBound;
            mDragHelper.settleCapturedViewAt(toTarget, (int) releasedChild.getY());
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
