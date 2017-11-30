package com.studio.plaster.tweetporter.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.studio.plaster.tweetporter.AsyncResponseEdit;
import com.studio.plaster.tweetporter.R;

import java.util.List;

public class EditAdpater extends RecyclerView.Adapter<EditAdpater.Holder>{
    private LayoutInflater inflater;
    private View itemView;
    private List<String> keywords = null;
    private AsyncResponseEdit listener;

    public void setListener(AsyncResponseEdit listener){
        this.listener = listener;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.edit_layout, parent, false);
        Holder holder = new Holder(itemView);
        return holder;
    }



    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        ImageView typeImg = holder.typeImg;
        final TextView keyword = holder.keyword;
        if(Character.toString(keywords.get(position).charAt(0)).equals("#")){
            typeImg.setImageResource(R.drawable.ic_text_fields_black_36dp);
        }
        if(Character.toString(keywords.get(position).charAt(0)).equals("@")){
            typeImg.setImageResource(R.drawable.ic_face_black_36dp);
        }
        keyword.setText(keywords.get(position).substring(1));
        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keywords.remove(position);
                listener.delTopic(keywords);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,keywords.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        if(keywords == null){
            return 0;
        }
        return keywords.size();
    }

    static class Holder extends RecyclerView.ViewHolder{
        public ImageView typeImg;
        public TextView keyword;
        public ImageButton delButton;

        public Holder(View itemView) {
            super(itemView);
            typeImg = itemView.findViewById(R.id.typeImg);
            keyword = itemView.findViewById(R.id.keyWordText);
            delButton = itemView.findViewById(R.id.editDelButton);

        }
    }
}
