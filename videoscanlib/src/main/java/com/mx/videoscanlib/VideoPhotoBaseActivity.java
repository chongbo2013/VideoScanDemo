package com.mx.videoscanlib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by xff on 2017/1/12.
 */

public abstract class VideoPhotoBaseActivity extends Activity {
    public static final boolean ATLEAST_LOLLIPOP =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    public enum Type{
        VIDEO,PHOTO
    }
    Type typeEnum=Type.VIDEO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        //
        View rootView=getRootView();
        if(rootView!=null&&rootView instanceof PermeateRootLayout){
            PermeateRootLayout permeateRootLayout=((PermeateRootLayout)rootView);
            //设置界面全屏
            permeateRootLayout.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }

        if(getIntent()!=null){
           int typeValue= getIntent().getIntExtra("type",0);
            if(typeValue==0){
                typeEnum=Type.PHOTO;
            }else {
                typeEnum=Type.VIDEO;
            }
        }else {
            finish();
        }

        onCreateCustom(savedInstanceState);
    }
    private  View getRootView() throws NullPointerException
    {
        return ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
    }
    protected abstract void onCreateCustom(Bundle savedInstanceState);
    protected abstract int getContentView();

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setupTransparentSystemBarsForLollipop();
    }
    /**
     * Sets up transparent navigation and status bars in Lollipop.
     * This method is a no-op for other platform versions.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupTransparentSystemBarsForLollipop() {
        if (ATLEAST_LOLLIPOP) {
            Window window = getWindow();
            window.getAttributes().systemUiVisibility |=
                    (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}
