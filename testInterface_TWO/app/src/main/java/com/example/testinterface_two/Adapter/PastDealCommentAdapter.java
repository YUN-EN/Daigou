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
import com.example.testinterface_two.models.DealComment;
import com.example.testinterface_two.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PastDealCommentAdapter extends RecyclerView.Adapter<PastDealCommentAdapter.ViewHolder> {
    private final List<DealComment> list;
    private final Activity content;
    private final List<User> userList;

    public PastDealCommentAdapter(List<DealComment> list, Activity content ,List<User> userList) {
        this.list = list;
        this.content = content;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(content).inflate(R.layout.each_past_deal_comment, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DealComment dealComment = list.get(position);
        holder.setPastDealCommentImage(dealComment.getCommentImage());
        holder.setPastDealCommentDate(dealComment.getCommentDate());
        holder.setPastDealCommentItemName(dealComment.getCommentItemName());
        holder.setPastDealCommentStarNum(dealComment.getStar());
        holder.setPastDealCommentText(dealComment.getCommentText());
        String userImage = userList.get(position).getUserImage();
        String username = userList.get(position).getUserName();
        holder.setPastDealCommentUserImage(userImage);
        holder.setPastDealCommentUserName(username);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pastDealCommentImage;
        CircleImageView pastDealCommentUserImage;
        TextView pastDealCommentUserName , pastDealCommentDate ,pastDealCommentItemName ,pastDealCommentText , pastDealCommentStarNum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setPastDealCommentImage(String image) {
            pastDealCommentImage = itemView.findViewById(R.id.pastDealCommentImage);
            Glide.with(content.getApplicationContext()).load(image).into(pastDealCommentImage);
        }
        public void setPastDealCommentUserImage(String userImage) {
            pastDealCommentUserImage = itemView.findViewById(R.id.pastDealCommentUserImage);
            Glide.with(content.getApplicationContext()).load(userImage).into(pastDealCommentUserImage);
        }
        public void setPastDealCommentUserName(String name) {
            pastDealCommentUserName = itemView.findViewById(R.id.pastDealCommentUserName);
            pastDealCommentUserName.setText(name);
        }
        public void setPastDealCommentDate(String date) {
            pastDealCommentDate = itemView.findViewById(R.id.pastDealCommentDate);
            pastDealCommentDate.setText(date);
        }
        public void setPastDealCommentText(String text) {
            pastDealCommentText = itemView.findViewById(R.id.pastDealCommentText);
            pastDealCommentText.setText(text);
        }
        public void setPastDealCommentStarNum(String num) {
            pastDealCommentStarNum = itemView.findViewById(R.id.pastDealCommentStarNumber);
            pastDealCommentStarNum.setText(num);
        }
        public void setPastDealCommentItemName(String itemName) {
            pastDealCommentItemName = itemView.findViewById(R.id.pastDealCommentItemName);
            pastDealCommentItemName.setText(itemName);
        }
    }
}
