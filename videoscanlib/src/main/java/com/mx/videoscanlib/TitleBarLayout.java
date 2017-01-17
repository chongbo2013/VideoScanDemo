package com.mx.videoscanlib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 编辑界面titlebar
 * Created by xff on 2016/11/29.
 */

public class TitleBarLayout extends FrameLayout implements View.OnClickListener{

    private ImageView left_btn;
    private TextView right_btn;
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
        right_btn=(TextView)findViewById(R.id.right_btn);
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

    public void setShowLeft(boolean show){
        if(left_btn!=null)
            left_btn.setVisibility(show?View.VISIBLE: View.INVISIBLE);
    }
    public void setShowRight(boolean show){
        if(right_btn!=null)
            right_btn.setVisibility(show?View.VISIBLE: View.INVISIBLE);
    }
    boolean isSelectModel=false;
    public void setSelectModel(boolean bool) {
        this.isSelectModel=bool;
        requestLayout();
    }

    //更新选中
    public void updateLeftText(int size) {
        if(isSelectModel){
            right_btn.setText(size==0?"下一步":"下一步"+"("+size+")");
        }else{
            right_btn.setText("下一步");
        }
    }
}