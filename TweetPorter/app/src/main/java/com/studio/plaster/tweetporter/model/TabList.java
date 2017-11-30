package com.studio.plaster.tweetporter.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TabList implements Parcelable{
    private List<String> keywordList;
    private String name;
    private List<Post> postList;
    private boolean isNotify;
    private int location;
    private int id_db;

    public TabList(){}

    protected TabList(Parcel in) {
        keywordList = in.createStringArrayList();
        name = in.readString();
        isNotify = in.readByte() != 0;
        location = in.readInt();
        id_db = in.readInt();
    }

    public static final Creator<TabList> CREATOR = new Creator<TabList>() {
        @Override
        public TabList createFromParcel(Parcel in) {
            return new TabList(in);
        }

        @Override
        public TabList[] newArray(int size) {
            return new TabList[size];
        }
    };

    public int getId_db() {
        return id_db;
    }

    public void setId_db(int id_db) {
        this.id_db = id_db;
    }

    public List<String> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<String> keywordList) {
        this.keywordList = keywordList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(keywordList);
        parcel.writeString(name);
        parcel.writeByte((byte) (isNotify ? 1 : 0));
        parcel.writeInt(location);
        parcel.writeInt(id_db);
    }
}
