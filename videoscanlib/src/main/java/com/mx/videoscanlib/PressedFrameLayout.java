package com.mx.videoscanlib;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

/**
 * 水波点击效果
 * Created by xff on 2017/1/13.
 */

public class PressedFrameLayout extends FrameLayout {
    Paint mPaint;
    ValueAnimator valueAnimator;
    float value=0;
    boolean enable=true;
    public void setEnableEffect(boolean enable){
        this.enable=enable;
    }
    public PressedFrameLayout(Context context) {
        super(context);
        init();
    }

    public PressedFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PressedFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        mPaint=new Paint();
        mPaint.setColor(Color.WHITE);
        valueAnimator=ValueAnimator.ofFloat(0f,1f);
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if(enable&&valueAnimator!=null&&valueAnimator.isRunning()) {
            int leftPadding = getPaddingLeft();
            int rightPadding = getPaddingRight();
            int topPadding = getPaddingTop();
            int buttomPadding = getPaddingBottom();
            int width = getWidth() - leftPadding - rightPadding;
            int height = getHeight() - topPadding - buttomPadding;
            double radius=Math.sqrt(width*width+height*height)/2;
            int radio = (int) (radius*value);
            int alpha= (int) (255f*(1f-value));
            mPaint.setAlpha(alpha);
            //绘制点击动画
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radio, mPaint);
            canvas.restore();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!enable)
            return super.dispatchTouchEvent(ev);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!valueAnimator.isRunning())
                valueAnimator.start();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                if(valueAnimator.isRunning())
//                    valueAnimator.cancel();
//                valueAnimator.start();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
