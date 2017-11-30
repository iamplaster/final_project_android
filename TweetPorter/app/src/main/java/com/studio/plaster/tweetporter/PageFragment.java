package com.studio.plaster.tweetporter;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studio.plaster.tweetporter.adapter.PostAdapter;
import com.studio.plaster.tweetporter.model.TabList;


public class PageFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private TabList tabList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fragment, container, false);
        recyclerView = view.findViewById(R.id.postRecycler);
        postAdapter = new PostAdapter(getContext());
        postAdapter.setPostList(tabList.getPostList());
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public void setTabList(TabList tabList){
        this.tabList = tabList;
    }
}
