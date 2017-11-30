package com.studio.plaster.tweetporter;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.studio.plaster.tweetporter.converter.TabConverter;
import com.studio.plaster.tweetporter.model.Post;
import com.studio.plaster.tweetporter.model.TabList;

import java.util.ArrayList;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAccessObject {

    String TWITTER_CONSUMER_KEY;
    String TWITTER_CONSUMER_SECRET;
    String twit_access_token;
    String twit_access_token_secret;
    ConfigurationBuilder mBuilder;
    private AsyncResponse delegate;
    Configuration mBuilded;

    public void setListener(AsyncResponse listener){
        delegate = listener;
    }

    public ConfigurationBuilder setup(){
        try{
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            builder.setOAuthAccessToken(twit_access_token);
            builder.setOAuthAccessTokenSecret(twit_access_token_secret);
            builder.setJSONStoreEnabled(true);
            builder.setIncludeEntitiesEnabled(true);
            builder.setIncludeMyRetweetEnabled(true);
            mBuilder = builder;
        }catch (Exception e){

        }
        return mBuilder;
    }


    @SuppressLint("StaticFieldLeak")
    public void getHomeTimeLine(){
        new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                AccessToken accessToken = new AccessToken(twit_access_token, twit_access_token_secret);
                Twitter twitter = new TwitterFactory(setup().build()).getInstance(accessToken);
                Paging page = new Paging (1,100);
                List<twitter4j.Status> statuses = null;
                try {
                    statuses = twitter.getHomeTimeline(page);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return statuses;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> statuses) {
                super.onPostExecute(statuses);
                System.out.println("Showing home timeline.");
                for (twitter4j.Status status : statuses) {
                    System.out.println(status.getUser().getName() + ":" +
                            status.getText());
                    MediaEntity[] media = status.getMediaEntities();
                    for(MediaEntity m : media){ //search trough your entities
                        System.out.println(m.getMediaURL()); //get your url!
                    }
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void getSearchTweet(List<String> keywords){
        String keySearch = "";
        for(int i=0; i < keywords.size(); i++){
            if(String.valueOf(keywords.get(i).charAt(0)).equals("#")){
                StringBuilder bulid = new StringBuilder(keywords.get(i));
                bulid.deleteCharAt(0);
            }
            keySearch += keywords.get(i);
            if(i < keywords.size()-1){
                keySearch += " OR ";
            }
        }
        final String finalKeySearch = keySearch;
        new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                AccessToken accessToken = new AccessToken(twit_access_token, twit_access_token_secret);
                Twitter twitter = new TwitterFactory(setup().build()).getInstance(accessToken);
                Query query = new Query(finalKeySearch);
                QueryResult result = null;
                try {
                    result = twitter.search(query);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return result.getTweets();
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> statuses) {
                super.onPostExecute(statuses);

            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateTabs(List<TabList> tabLists){
        final List<TabList> tl = tabLists;
        new AsyncTask<Void, Void, List<TabList>>() {
            @Override
            protected List<TabList> doInBackground(Void... voids) {
                for(int i=0; i < tl.size(); i++){
                    String keySearch = "";
                    List<String> keywords = tl.get(i).getKeywordList();
                    for(int j=0; j < keywords.size(); j++){
                        if(String.valueOf(keywords.get(j).charAt(0)).equals("#")){
                            StringBuilder bulid = new StringBuilder(keywords.get(j));
                            bulid.deleteCharAt(0);
                        }
                        keySearch += keywords.get(j);
                        if(j < keywords.size()-1){
                            keySearch += " OR ";
                        }
                    }
                    final String finalKeySearch = keySearch;
                    AccessToken accessToken = new AccessToken(twit_access_token, twit_access_token_secret);
                    Twitter twitter = new TwitterFactory(setup().build()).getInstance(accessToken);
                    Query query = new Query(finalKeySearch);
                    query.setCount(100);
                    QueryResult result = null;
                    try {
                        result = twitter.search(query);
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    if(keywords.size() <= 0){
                        List<Post> posts = new ArrayList<>();
                        Post post = new Post();
                        post.setName("Tweetporter Alert");
                        post.setContentText("You not have any topic, Please add topic in edit tab menu");
                        post.setProfileImg(null);
                        post.setContentImg(null);
                        posts.add(post);
                        tl.get(i).setPostList(posts);
                    }else{
                        tl.get(i).setPostList(new TabConverter().statusesToPosts(result.getTweets()));
                    }

                }
                return tl;
            }

            @Override
            protected void onPostExecute(List<TabList> tabLists) {
                super.onPostExecute(tabLists);
                delegate.updateTabsFinish(tabLists);
            }
        }.execute();


    }

    @SuppressLint("StaticFieldLeak")
    public void getHeaderInfo(){
        new AsyncTask<Void, Void, List<String>>(){

            @Override
            protected List<String> doInBackground(Void... voids) {
                List<String> headerInfo = new ArrayList<>();
                AccessToken accessToken = new AccessToken(twit_access_token, twit_access_token_secret);
                Twitter twitter = new TwitterFactory(setup().build()).getInstance(accessToken);
                try {
                    User user = twitter.showUser(twitter.getId());
                    String userName = user.getName();
                    String profileImg = user.getOriginalProfileImageURL();
                    String backGround = user.getProfileBackgroundImageURL();
                    headerInfo.add(userName);
                    headerInfo.add(profileImg);
                    headerInfo.add(backGround);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }

                return headerInfo;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                delegate.getHeaderInfoFinish(strings);
            }
        }.execute();
    }



    public void setTWITTER_CONSUMER_KEY(String TWITTER_CONSUMER_KEY) {
        this.TWITTER_CONSUMER_KEY = TWITTER_CONSUMER_KEY;
    }

    public void setTWITTER_CONSUMER_SECRET(String TWITTER_CONSUMER_SECRET) {
        this.TWITTER_CONSUMER_SECRET = TWITTER_CONSUMER_SECRET;
    }

    public void setTwit_access_token(String twit_access_token) {
        this.twit_access_token = twit_access_token;
    }

    public void setTwit_access_token_secret(String twit_access_token_secret) {
        this.twit_access_token_secret = twit_access_token_secret;
    }
}
