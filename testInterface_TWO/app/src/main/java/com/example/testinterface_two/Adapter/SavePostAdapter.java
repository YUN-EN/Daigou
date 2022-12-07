package com.example.testinterface_two.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testinterface_two.R;
import com.example.testinterface_two.models.SavePost;

import java.util.List;

public class SavePostAdapter extends RecyclerView.Adapter<SavePostAdapter.SavePostViewHolder> {
    private  final List<SavePost> savePostList;
    private final Activity context;

    public SavePostAdapter(List<SavePost> savePostList, Activity context) {
        this.savePostList = savePostList;
        this.context = context;
    }


    @NonNull
    @Override
    public SavePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.each_favorite , parent , false);
        return new SavePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavePostViewHolder holder, int position) {
        SavePost savePost = savePostList.get(position);
        holder.setSavePostImage(savePost.getPostImage());
        holder.setSavePostTitle(savePost.getPostTitle());
    }

    @Override
    public int getItemCount() {
        return savePostList.size();
    }

    public class SavePostViewHolder extends  RecyclerView.ViewHolder {
        ImageView savePostImage;
        TextView savePostTitle;
        public SavePostViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setSavePostImage(String image) {
            savePostImage = itemView.findViewById(R.id.myFavoritePostImage);
            Glide.with(context).load(image).into(savePostImage);
        }
        public void setSavePostTitle(String title) {
            savePostTitle = itemView.findViewById(R.id.myFavoritePostTitle);
            savePostTitle.setText(title);
        }
    }
}
