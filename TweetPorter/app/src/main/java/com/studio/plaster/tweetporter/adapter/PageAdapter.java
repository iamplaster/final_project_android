package com.studio.plaster.tweetporter.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.studio.plaster.tweetporter.PageFragment;
import com.studio.plaster.tweetporter.model.TabList;

import java.util.List;

public class PageAdapter extends FragmentStatePagerAdapter{
    private List<TabList> tabLists = null;


    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        PageFragment pageFragment = new PageFragment();
        pageFragment.setTabList(tabLists.get(position));
        return pageFragment;
    }

    @Override
    public int getCount() {
        if(tabLists != null){
            return tabLists.size();
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabLists.get(position).getName();
    }

    public void setTabLists(List<TabList> tabLists) {
        this.tabLists = tabLists;
    }
}
