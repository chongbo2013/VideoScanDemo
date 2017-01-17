package com.mx.videoscanlib;

import java.io.File;

/**
 * Created by xff on 2017/1/12.
 */
public class MediaItem extends MediaBase {
    long duration;
    String data;
    String artist;
    String heightUrl;
    String desc;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getHeightUrl() {
        return heightUrl;
    }

    public void setHeightUrl(String heightUrl) {
        this.heightUrl = heightUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    @Override
    public String toString() {
        return "MediaItem{" +
                "duration=" + duration +
                ", data='" + data + '\'' +
                ", artist='" + artist + '\'' +
                ", heightUrl='" + heightUrl + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }




}