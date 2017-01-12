package com.mx.videoscanlib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 编辑界面titlebar
 * Created by xff on 2016/11/29.
 */

public class TitleBarLayout extends RelativeLayout implements View.OnClickListener{

    private ImageView left_btn,right_btn;
    private IVideoPhotoInterface mIVideoPhotoInterface;
    private TextView title_tv;
    public TitleBarLayout(Context context) {
        super(context);
        init();
    }

    public TitleBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        View.inflate(getContext(), R.layout.titlebar_layout,this);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        left_btn=(ImageView)findViewById(R.id.left_btn);
        right_btn=(ImageView)findViewById(R.id.right_btn);
        title_tv=(TextView)findViewById(R.id.title_tv);
        left_btn.setOnClickListener(this);
        right_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.left_btn){
            if(mIVideoPhotoInterface!=null)
                mIVideoPhotoInterface.leftClick();
        }else if(id==R.id.right_btn){
            if(mIVideoPhotoInterface!=null)
                mIVideoPhotoInterface.rightClick();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(getContext() instanceof IVideoPhotoInterface){
            mIVideoPhotoInterface= (IVideoPhotoInterface) getContext();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(getContext() instanceof IVideoPhotoInterface){
            mIVideoPhotoInterface= null;
        }
    }

    public void setLeftDrawableResource(int resource){
        if(left_btn!=null)
            left_btn.setImageResource(resource);
    }

    public void setRightDrawableResource(int resource){
        if(right_btn!=null)
            right_btn.setImageResource(resource);
    }

    public void setText(String text){
        if(title_tv!=null)
        title_tv.setText(text);
    }

    public void setTextSize(float size){
        if(title_tv!=null)
            title_tv.setTextSize(size);
    }

    public void setTextColor(int color){
        if(title_tv!=null)
            title_tv.setTextColor(color);
    }
}