package com.example.testinterface_two.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testinterface_two.R;
import com.example.testinterface_two.models.Comment;
import com.example.testinterface_two.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final Activity context;
    private final List<Comment> commentList;
    private final List<User> userList;

    public CommentAdapter(Activity context,List<Comment> commentList ,List<User> userList) {
        this.context = context;
        this.commentList = commentList;
        this.userList = userList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_comment ,parent ,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.setComment(comment.getComment());
        User users = userList.get(position);
        holder.setUserComment(users.getUserName());
        holder.setUserCircleImage(users.getUserImage());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView CommentText ,userComment;
        CircleImageView userCircleImage;
        View view;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
        public void setComment(String comment) {
            CommentText = view.findViewById(R.id.dealCommentDateText);
            CommentText.setText(comment);
        }
        public void setUserComment(String user) {
            userComment = view.findViewById(R.id.commentUserName);
            userComment.setText(user);
        }
        public void setUserCircleImage(String userImage) {
            userCircleImage = view.findViewById(R.id.dealCommentImage);
            Glide.with(context).load(userImage).into(userCircleImage);
        }
    }
}
