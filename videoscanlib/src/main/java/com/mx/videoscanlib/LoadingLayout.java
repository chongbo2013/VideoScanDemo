package com.mx.videoscanlib;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * 加载动画封装
 * Created by xff on 2017/1/13.
 */

public class LoadingLayout extends FrameLayout {
    View dark_bg;
    ProgressBar progressBar;
    public LoadingLayout(Context context) {
        super(context);
        init();
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        View.inflate(getContext(), R.layout.loading_layout,this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dark_bg=findViewById(R.id.dark_bg);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        Drawable drawable= progressBar.getIndeterminateDrawable();
        if(drawable!=null&&drawable instanceof AnimationDrawable){
            AnimationDrawable animationDrawable= (AnimationDrawable) drawable;
            animationDrawable.start();
        }
    }

    public void show(){
        setVisibility(VISIBLE);
        dark_bg.setAlpha(0f);
        dark_bg.animate().alpha(1f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                dark_bg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).setDuration(120).start();
    }

    public void hide(){
        dark_bg.setAlpha(1f);
        dark_bg.animate().alpha(0f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                dark_bg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dark_bg.setVisibility(View.GONE);
                setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).setDuration(120).start();
    }
}
