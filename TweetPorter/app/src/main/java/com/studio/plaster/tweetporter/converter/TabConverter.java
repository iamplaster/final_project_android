package com.studio.plaster.tweetporter.converter;


import android.util.Log;

import com.studio.plaster.tweetporter.model.Post;
import com.studio.plaster.tweetporter.model.TabList;
import com.studio.plaster.tweetporter.modeldb.TabListDb;

import java.util.ArrayList;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Status;

public class TabConverter {
    public TabList tabListDbToTabList(TabListDb input){
        TabList tabList = new TabList();
        tabList.setId_db(input.getId());
        tabList.setPostList(input.getPostList());
        tabList.setName(input.getName());
        tabList.setKeywordList(input.getKeyWordList());
        tabList.setLocation(input.getLocation());
        tabList.setNotify(input.isNotify());
        return  tabList;
    }
    public TabListDb tabListToTabListDb(TabList input){
        TabListDb tabListDb = new TabListDb();
        tabListDb.setId(input.getId_db());
        tabListDb.setKeyWordList(input.getKeywordList());
        tabListDb.setLocation(input.getLocation());
        tabListDb.setName(input.getName());
        tabListDb.setPostList(input.getPostList());
        tabListDb.setNotify(input.isNotify());
        return tabListDb;
    }

    public List<TabList> tabListDbsToTabLists(List<TabListDb> input){
        List<TabList> tabLists = new ArrayList<>();
        Log.d("input", String.valueOf(input.size()));
        for(int i=0; i < input.size(); i++){
            TabList tabList = new TabList();
            tabList.setId_db(input.get(i).getId());
            tabList.setPostList(input.get(i).getPostList());
            tabList.setName(input.get(i).getName());
            tabList.setKeywordList(input.get(i).getKeyWordList());
            tabList.setLocation(input.get(i).getLocation());
            tabList.setNotify(input.get(i).isNotify());
            tabLists.add(tabList);
        }
        Log.d("output", String.valueOf(tabLists.size()));
        return tabLists;
    }

    public Post statusToPost(Status status){
        Post post = new Post();
        post.setName(status.getUser().getName());
        post.setProfileImg(status.getUser().getProfileImageURL());
        post.setTweet_id(String.valueOf(status.getId()));
        post.setContentText(status.getText());
        MediaEntity[] media = status.getMediaEntities();
        for(MediaEntity m : media){
            post.setContentImg(m.getMediaURL());
        }
        return post;
    }

    public List<Post> statusesToPosts(List<Status> statuses){
        List<Post> posts = new ArrayList<>();
        for(int i=0; i < statuses.size(); i++){
            Post post = new Post();
            Status status = statuses.get(i);
            post.setName(status.getUser().getName());
            post.setProfileImg(status.getUser().getProfileImageURL());
            post.setTweet_id(String.valueOf(status.getId()));
            post.setContentText(status.getText());
            MediaEntity[] media = status.getMediaEntities();
            for(MediaEntity m : media){
                post.setContentImg(m.getMediaURL());
            }
            post.setTweetLink("https://twitter.com/" + status.getUser().getScreenName()
                    + "/status/" + status.getId());
            posts.add(post);
        }
        return posts;

    }
}
