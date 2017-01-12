package com.mx.videoscanlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 自定义沉浸View，导航栏，状态栏
 * Created by xff on 2017/1/4.
 */

public class PermeateRootLayout extends FrameLayout implements
        ViewGroup.OnHierarchyChangeListener{
    protected Rect mInsets = new Rect();
    public PermeateRootLayout(Context context) {
        super(context);
        init();
    }

    public PermeateRootLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PermeateRootLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        setOnHierarchyChangeListener(this);
    }
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        setInsets(insets);
        return true;
    }

    public void setInsets(Rect insets) {
        final int n = getChildCount();
        for (int i = 0; i < n; i++) {
            final View child = getChildAt(i);
            setFrameLayoutChildInsets(child, insets, mInsets);
        }
        mInsets.set(insets);
    }
    public void setFrameLayoutChildInsets(View child, Rect newInsets, Rect oldInsets) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int left=child.getPaddingLeft();
        int right=child.getPaddingRight();
        int top=child.getPaddingTop();
        int bottom=child.getPaddingBottom();
        if (!lp.ignoreInsets) {//没有忽略偏移
            if (!lp.ignoreTopInsets) {//没有忽略top偏移
                top += (newInsets.top - oldInsets.top);
            }
            left += (newInsets.left - oldInsets.left);
            right += (newInsets.right - oldInsets.right);

            if (!lp.ignoreBottomInsets) {//没有bottom忽略偏移
                bottom += (newInsets.bottom - oldInsets.bottom);
            }
        }
        child.setPadding(left,top,right,bottom);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(mOnLayoutListem!=null)
            mOnLayoutListem.chage();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        setFrameLayoutChildInsets(child, mInsets, new Rect());
    }
    @Override
    public void onChildViewRemoved(View parent, View child) {
        setFrameLayoutChildInsets(child, new Rect(), mInsets);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new PermeateRootLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    // Override to allow type-checking of LayoutParams.
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof PermeateRootLayout.LayoutParams;
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }
    OnLayoutListem mOnLayoutListem;
    public void addOnLayoutListem(OnLayoutListem mOnLayoutListem) {
        this.mOnLayoutListem=mOnLayoutListem;
    }
    public interface OnLayoutListem{
        void chage();
    }
    public static class LayoutParams extends FrameLayout.LayoutParams {
        boolean ignoreInsets = true;
        boolean ignoreTopInsets = true;
        boolean ignoreBottomInsets = true;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs,
                    R.styleable.PermeateRootLayout);
            ignoreInsets = a.getBoolean(
                    R.styleable.PermeateRootLayout_permeateIgnoreInsets, true);
            ignoreTopInsets = a.getBoolean(
                    R.styleable.PermeateRootLayout_permeateIgnoreTopInsets, true);
            ignoreBottomInsets = a.getBoolean(
                    R.styleable.PermeateRootLayout_permeateIgnoreBottomInsets, true);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams lp) {
            super(lp);
        }
    }

}
