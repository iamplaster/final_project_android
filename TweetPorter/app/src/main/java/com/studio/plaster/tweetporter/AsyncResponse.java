package com.studio.plaster.tweetporter;

import com.studio.plaster.tweetporter.model.TabList;

import java.util.List;

public interface AsyncResponse {
    void processFinish(TabMainDb output);
    void getFinish(List<TabList> tabList);
    void delFinish(String result, int lastPosition);
    void updateTabsFinish(List<TabList> tabLists);
    void getHeaderInfoFinish(List<String> output);
}
