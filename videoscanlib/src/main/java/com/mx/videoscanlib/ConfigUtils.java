package com.mx.videoscanlib;

import android.os.Environment;

import java.io.File;

/**
 * Created by xff on 2017/1/12.
 */

public class ConfigUtils {

    public static File createThumbnailPath(){
        File sdcard=new File(Environment.getExternalStorageDirectory()+File.separator+"mediacache");
        if(!sdcard.exists())
            sdcard.mkdirs();
        return sdcard;
    }
}
