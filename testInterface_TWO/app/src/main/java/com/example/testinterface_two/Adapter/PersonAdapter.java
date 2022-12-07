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
import com.example.testinterface_two.models.UserPersonComment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>{
    private final List<UserPersonComment> userPersonComments;
    private final Activity content;
    private FirebaseFirestore firebaseFirestore;

    public PersonAdapter(List<UserPersonComment> userPersonComments, Activity content) {
        this.userPersonComments = userPersonComments;
        this.content = content;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(content).inflate(R.layout.each_person_comment, parent ,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserPersonComment userPersonComment = userPersonComments.get(position);
        holder.setCommentText(userPersonComment.getPersonComment());
        String i = userPersonComment.personSellerID;
        firebaseFirestore.collection("Users").document(i).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String name = task.getResult().getString("userName");
                    String image = task.getResult().getString("userImage");
                    holder.nameText.setText(name);
                    Glide.with(content).load(image).into(holder.userImage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userPersonComments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText ,commentText;
        CircleImageView userImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.dealSellerName);
           userImage = itemView.findViewById(R.id.dealSellerImage);
        }
        public void setCommentText(String comment){
            commentText = itemView.findViewById(R.id.dealPersonComment);
            commentText.setText(comment);
        }
    }
}
