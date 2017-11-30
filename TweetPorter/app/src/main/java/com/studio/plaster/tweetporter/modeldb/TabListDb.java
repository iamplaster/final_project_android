package com.studio.plaster.tweetporter.modeldb;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.studio.plaster.tweetporter.converter.KeyWordTypeConverter;
import com.studio.plaster.tweetporter.converter.PostTypeConverter;
import com.studio.plaster.tweetporter.model.Post;

import java.util.List;


@Entity
public class TabListDb {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private boolean isNotify;
    private int location;

    @TypeConverters(KeyWordTypeConverter.class)
    private List<String> keyWordList;

    @TypeConverters(PostTypeConverter.class)
    private List<Post> postList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNotify() {
        return isNotify;
    }

    public void setNotify(boolean notify) {
        isNotify = notify;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public List<String> getKeyWordList() {
        return keyWordList;
    }

    public void setKeyWordList(List<String> keyWordList) {
        this.keyWordList = keyWordList;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }
}

