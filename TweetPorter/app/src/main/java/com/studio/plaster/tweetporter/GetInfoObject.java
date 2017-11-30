package com.studio.plaster.tweetporter;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.studio.plaster.tweetporter.converter.TabConverter;
import com.studio.plaster.tweetporter.model.TabList;
import com.studio.plaster.tweetporter.modeldb.TabListDb;

import java.util.ArrayList;
import java.util.List;

public class GetInfoObject {
    public List<TabList> tabLists = new ArrayList<>();
    public TabMainDb db;
    public TabList tabListAdd;
    public AsyncResponse delegate;
    public TabList tabListDel;
    public int lastPositionPage;
    public AsyncResponseEdit delegateEdit;

    public void setListener(AsyncResponse listener){
        delegate = listener;
    }
    public void setListenerEdit(AsyncResponseEdit listener){
        delegateEdit = listener;
    }

    public void setDb(TabMainDb db) {
        this.db = db;
    }

    @SuppressLint("StaticFieldLeak")
    public void getTabs(){
        new AsyncTask<Void, Void, List<TabListDb>>(){

            @Override
            protected List<TabListDb> doInBackground(Void... voids) {
                List<TabListDb> tabListDbs = db.getTabListDAO().getAll();
                return  tabListDbs;
            }
            @Override
            protected void onPostExecute(List<TabListDb> tabListDbs) {
                super.onPostExecute(tabListDbs);
                TabConverter tabConverter = new TabConverter();
                tabLists = tabConverter.tabListDbsToTabLists(tabListDbs);
                delegate.getFinish(tabLists);
            }
        }.execute();

    }
    @SuppressLint("StaticFieldLeak")
    public void addTab(TabList tabList){
        tabListAdd = tabList;
        new AsyncTask<Void, Void, TabList>() {
            @Override
            protected TabList doInBackground(Void... voids) {
                db.getTabListDAO().insert(new TabConverter().tabListToTabListDb(tabListAdd));
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteTab(TabList tabList, final int lastPositionPage){
        tabListDel = tabList;
        this.lastPositionPage = lastPositionPage;
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids) {
                if(tabListDel.getName().equals("HOME")){
                    return false;
                }
                db.getTabListDAO().delete(new TabConverter().tabListToTabListDb(tabListDel));
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if(delegate != null){
                    if(aBoolean){
                        delegate.delFinish("Completed delete tab", lastPositionPage);
                    }else {
                        delegate.delFinish("Cannot delete this tab", lastPositionPage);
                    }
                }
                if(delegateEdit != null){
                    if(aBoolean){
                        delegateEdit.delTabFinish("Completed delete tab", lastPositionPage);
                    }else {
                        delegateEdit.delTabFinish("Cannot delete this tab", lastPositionPage);
                    }
                }

            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateTab(final TabList tabList){
        new AsyncTask<Void, Void, TabList>() {
            @Override
            protected TabList doInBackground(Void... voids) {
                db.getTabListDAO().update(new TabConverter().tabListToTabListDb(tabList));
                return null;
            }

            @Override
            protected void onPostExecute(TabList tabList) {
                super.onPostExecute(tabList);
                delegateEdit.updateFinish();
            }
        }.execute();
    }


}
