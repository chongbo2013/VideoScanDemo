package com.mx.videoscanlib;

import java.io.File;
import java.io.Serializable;

/**
 * Created by x002 on 2017/1/12.
 */

public class MediaBase implements Serializable {
   protected long id;
    protected  String imageUrl;
    protected  String name;
    protected  long size;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "MediaBase{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }

    File imageFile;
    public void createImageUrlFile() {
        if (imageUrl != null)
            imageFile = new File(imageUrl);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        createImageUrlFile();
    }

    public File getImageFile() {
        return imageFile;
    }
}
