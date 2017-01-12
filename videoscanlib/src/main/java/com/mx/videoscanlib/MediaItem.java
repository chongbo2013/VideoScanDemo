package com.mx.videoscanlib;

import java.io.File;

/**
 * Created by xff on 2017/1/12.
 */
public class MediaItem extends MediaBase{
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    String name ;
    long size ;
    long duration ;
    String data ;
    String artist ;
    String heightUrl;
    String desc;
    String imageUrl;
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
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
    public String getImageUrl() {
        return imageUrl;
    }
    File imageFile;
    public void createImageUrlFile(){
        if(imageUrl!=null)
        imageFile=new File(imageUrl);
    }

    @Override
    public String toString() {
        return "MediaItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", duration=" + duration +
                ", data='" + data + '\'' +
                ", artist='" + artist + '\'' +
                ", heightUrl='" + heightUrl + '\'' +
                ", desc='" + desc + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        createImageUrlFile();
    }

    public File getImageFile(){
        return imageFile;
    }


}