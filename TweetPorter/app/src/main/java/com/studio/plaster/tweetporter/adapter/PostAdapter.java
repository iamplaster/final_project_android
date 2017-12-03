package com.studio.plaster.tweetporter.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studio.plaster.tweetporter.R;
import com.studio.plaster.tweetporter.model.Post;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<Post> postList = new ArrayList<>();
    private int count;
    private Context context;
    private List<Post> savePl = new ArrayList<>();
    private Boolean isSaveMode = false;


    public PostAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.post_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Glide.with(context).load(postList.get(position).getProfileImg()).into(holder.imageProfile);
        holder.accName.setText(postList.get(position).getName());
        holder.contentText.setText(postList.get(position).getContentText());
        if(postList.get(position).getContentImg() != null){
            Glide.with(context).load(postList.get(position).getContentImg()).into(holder.contentImg);
            Resources resources = context.getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, resources.getDisplayMetrics());
            holder.contentImg.getLayoutParams().height = Math.round(px);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.itemView);
                popup.getMenuInflater().inflate(R.menu.press_option, popup.getMenu());
                popup.getMenu().findItem(R.id.delsave_button).setVisible(false);
                if(isSaveMode){
                    popup.getMenu().findItem(R.id.save_button).setVisible(false);
                    popup.getMenu().findItem(R.id.delsave_button).setVisible(true);
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.gotolink_button:
                                goTolink(position);
                                return true;
                            case R.id.save_button:
                                savePost(position);
                                return true;
                            case R.id.delsave_button:
                                delSavePost(position);
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        if(postList == null){
            count = 0;
        }else {
            count = postList.size();
        }
        return count;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageProfile;
        TextView accName;
        TextView contentText;
        ImageView contentImg;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imgProfile);
            accName = itemView.findViewById(R.id.accName);
            contentText = itemView.findViewById(R.id.contentText);
            contentImg = itemView.findViewById(R.id.contentImg);
        }
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
        for(int i = 0; i< postList.size(); i++){
            System.out.println(postList.get(i).getContentImg());
        }
    }

    public void goTolink(int position){
        Uri uri = Uri.parse(postList.get(position).getTweetLink());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public void savePost(int position){
        SharedPreferences sharedPref = context.getSharedPreferences("Savedkeeper", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String savedPostList = sharedPref.getString("savedpostlist", null);
        if(savedPostList != null){
            Type type = new TypeToken<List<Post>>(){}.getType();
            savePl = gson.fromJson(savedPostList, type);
        }
        savePl.add(postList.get(position));
        String json = gson.toJson(savePl);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("savedpostlist", json);
        editor.apply();

    }

    public void setSaveMode(Boolean input){
        this.isSaveMode = input;

    }

    public void delSavePost(int position){
        SharedPreferences sharedPref = context.getSharedPreferences("Savedkeeper", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        postList.remove(position);
        String json = gson.toJson(postList);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("savedpostlist", json);
        editor.apply();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, postList.size());

    }
}
