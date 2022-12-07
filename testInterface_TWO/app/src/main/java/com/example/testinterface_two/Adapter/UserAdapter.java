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
import com.example.testinterface_two.models.User;
import com.example.testinterface_two.models.UserListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private final Activity context;
    private final List<User> userList;
    private final UserListener userListener;


    public UserAdapter(Activity context , List<User> userList , UserListener userListener) {
        this.context = context;
        this.userList = userList;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_user, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getUserName());
        holder.mailTextView.setText(user.getUserMail());
        Glide.with(context).load(user.getUserImage()).into(holder.userImageView);
        holder.itemView.setOnClickListener(v -> userListener.onUserClicked(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView ,mailTextView;
        public CircleImageView userImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView =itemView.findViewById(R.id.chatUserName);
            mailTextView =itemView.findViewById(R.id.mailTextView);
            userImageView =itemView.findViewById(R.id.chatUserImage);
        }
    }

}
