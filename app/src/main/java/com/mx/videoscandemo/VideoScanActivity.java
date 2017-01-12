package com.mx.videoscandemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mx.videoscanlib.VideoPhotoMergeActivity;

/**
 * 视频图片扫描整合
 */
public class VideoScanActivity extends AppCompatActivity {
    Button btn_scan_photo,btn_scan_video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_scan);
        btn_scan_photo= (Button) findViewById(R.id.btn_scan_photo);
        btn_scan_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(VideoScanActivity.this, VideoPhotoMergeActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);
            }
        });
        btn_scan_video= (Button) findViewById(R.id.btn_scan_video);
        btn_scan_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(VideoScanActivity.this, VideoPhotoMergeActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });
    }
}
