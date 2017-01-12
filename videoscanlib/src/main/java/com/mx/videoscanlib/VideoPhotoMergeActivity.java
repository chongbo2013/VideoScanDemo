package com.mx.videoscanlib;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 视频图片选取合并界面
 * Created by xff on 2017/1/12.
 */

public class VideoPhotoMergeActivity extends VideoPhotoBaseActivity implements IVideoPhotoInterface {
    PermeateRootLayout permeateRootLayout;
    MergeRecyclerView mRecyclerView;

    @Override
    protected int getContentView() {
        return R.layout.video_photo_merge_activity;
    }


    @Override
    protected void onCreateCustom(Bundle savedInstanceState) {
        permeateRootLayout = (PermeateRootLayout) findViewById(R.id.permeateRootLayout);
        mRecyclerView = (MergeRecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels/4;
        mRecyclerView.setItemWidth(width);
        if(mRecyclerView.getAdapter()!=null){
            MergeRecyclerView.MergeAdapter adapter= (MergeRecyclerView.MergeAdapter) mRecyclerView.getAdapter();
            adapter.setOnItemClickListener(new MergeRecyclerView.OnCustomItemClickListener() {
                @Override
                public boolean onItemClick(MergeRecyclerView.MergeAdapter adapter, int adapter_position) {
                    Toast.makeText(VideoPhotoMergeActivity.this,"position:"+adapter_position,Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
        if (typeEnum == Type.VIDEO) {
            taskVideo();
        }
    }

    //执行视频获取任务
    private void taskVideo() {
        Observable.create(new Observable.OnSubscribe<List<MediaItem>>() {
            @Override
            public void call(Subscriber<? super List<MediaItem>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                List<MediaItem> mediaItems = getLocalMediaVideo(VideoPhotoMergeActivity.this);
                if (mediaItems != null && mediaItems.size() > 0) {
                    subscriber.onNext(mediaItems);
                } else {
                    subscriber.onError(new IllegalStateException("no media"));
                }

                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<List<MediaItem>>() {
                            @Override
                            public void call(List<MediaItem> mMediaItems) {
                                //有数据
                                Toast.makeText(VideoPhotoMergeActivity.this, "size:" + mMediaItems.size(), Toast.LENGTH_SHORT).show();
                                if(mRecyclerView!=null){
                                    mRecyclerView.setMediaData(mMediaItems);
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                //显示空列表

                            }
                        }, new Action0() {
                            @Override
                            public void call() {
                                //任务执行完毕
                                Toast.makeText(VideoPhotoMergeActivity.this, "complite", Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    /**
     * 获取本地视频
     *
     * @param mContext
     * @return
     */
    private List<MediaItem> getLocalMediaVideo(Context mContext) {
        //初始化集合
        ArrayList<MediaItem> mediaItems = new ArrayList<MediaItem>();
        ContentResolver resolver = mContext.getContentResolver();
        //sdcard 的视频路径
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] objs = {
                MediaStore.Video.Media.DISPLAY_NAME,//在sdcard显示的视频名称
                MediaStore.Video.Media.DURATION,//视频的时长,毫秒
                MediaStore.Video.Media.SIZE,//文件大小-byte
                MediaStore.Video.Media.DATA,//在sdcard的路径-播放地址
                MediaStore.Video.Media.ARTIST,//艺术家
                MediaStore.Video.Media._ID
        };

        String[] thumbColumns = new String[]{
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID
        };

        Cursor cusor = resolver.query(uri, objs, null, null, null);
        if (cusor != null) {
            while (cusor.moveToNext()) {
                MediaItem mediaItem = new MediaItem();
                //添加到集合中
                String name = cusor.getString(0);
                mediaItem.setName(name);
                long duration = cusor.getLong(1);
                mediaItem.setDuration(duration);
                long size = cusor.getLong(2);
                mediaItem.setSize(size);
                String data = cusor.getString(3);//播放地址
                mediaItem.setData(data);
                String artist = cusor.getString(4);//艺术家
                mediaItem.setArtist(artist);

                long _id = cusor.getLong(5);//id
                mediaItem.setId(_id);

                //获取视频截图
                Cursor thumbCursor = resolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns,
                        MediaStore.Video.Thumbnails.VIDEO_ID + "=?",
                        new String[]{String.valueOf(mediaItem.getId())}, null);

                if (thumbCursor.moveToFirst()) {
                    String thumbPath = thumbCursor.getString(
                            thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                    mediaItem.setImageUrl(thumbPath);
                }

                thumbCursor.close();
                //如果视频文件存在
                if (mediaItem.getImageUrl() == null && mediaItem.getData() != null) {
                    File videoPath = new File(mediaItem.getData());
                    if (videoPath.exists()) {
                        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mediaItem.getData(), MediaStore.Video.Thumbnails.MINI_KIND);
                        if (bitmap != null) {
                            File saveImage = saveImage(ConfigUtils.createThumbnailPath(), bitmap);
                            mediaItem.setImageUrl(saveImage.getAbsolutePath());
                        }
                    } else {
                        //使用默认的图片
                    }

                }
                //获取视频截图结束

                //获取视频的时长
                if (mediaItem.getDuration() <= 0 && mediaItem.getData() != null) {
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(mediaItem.getData());
                    String durationTime = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    mediaItem.setDuration(Long.parseLong(durationTime));
                    mediaMetadataRetriever.release();
                    mediaMetadataRetriever = null;
                }
                //获取视频的时长

                mediaItems.add(mediaItem);//可以
            }

            cusor.close();
        }
        return mediaItems;
    }

    public File saveImage(File appDir, Bitmap bmp) {
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bmp != null && !bmp.isRecycled())
                bmp.recycle();
        }
        return file;
    }

    @Override
    public void cleanUp() {
        //清理缩略图
        Observable.create(new Observable.OnSubscribe<List<MediaItem>>() {
            @Override
            public void call(Subscriber<? super List<MediaItem>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                RecursionDeleteFile(ConfigUtils.createThumbnailPath());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<List<MediaItem>>() {
                            @Override
                            public void call(List<MediaItem> mMediaItems) {

                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                //显示空列表

                            }
                        }, new Action0() {
                            @Override
                            public void call() {

                            }
                        });
    }

    @Override
    public void leftClick() {

    }

    @Override
    public void rightClick() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanUp();
    }



    public  void RecursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }
}
