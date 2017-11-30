package com.studio.plaster.tweetporter;


import java.util.List;

public interface AsyncResponseEdit {
    void updateFinish();
    void processFinish(TabMainDb output);
    void delTabFinish(String result, int lastPosition);
    void delTopic(List<String> keywords);
}
