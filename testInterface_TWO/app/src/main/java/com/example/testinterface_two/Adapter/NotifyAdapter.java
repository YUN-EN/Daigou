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
import com.example.testinterface_two.models.Notify;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {
    private final Activity context;
    private final List<Notify> notifyList;
    private FirebaseFirestore firebaseFirestore;
    public NotifyAdapter(Activity context, List<Notify> notifyList)  {
        this.context = context;
        this.notifyList = notifyList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.each_notify ,parent ,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notify notify = notifyList.get(position);
        holder.NotifyMessage.setText(notify.getNotifyMessage());
        getUserInfo(holder.NotifyUserImage , holder.NotifyUserName, notify.getUserId());
        if (notify.isPost) {
            getPostImage(holder.NotifyImage , notify.getPostId());
        }
    }

    @Override
    public int getItemCount() {
        return notifyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView NotifyUserName ,NotifyMessage;
        public CircleImageView NotifyUserImage;
        public ImageView NotifyImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NotifyUserName = itemView.findViewById(R.id.notifyUserName);
            NotifyMessage = itemView.findViewById(R.id.notifyMessage);
            NotifyUserImage = itemView.findViewById(R.id.notifyUserImage);
            NotifyImage = itemView.findViewById(R.id.notifyPostImage);
        }
    }
    private void getUserInfo(ImageView imageView ,TextView userName ,String publisherId) {
        firebaseFirestore.collection("Users").document(publisherId).addSnapshotListener((value, error) -> {
                if (error==null && value!= null) {
                   String name = value.getString("userName");
                    String image = value.getString("userImage");
                    userName.setText(name);
                    Glide.with(context.getApplicationContext()).load(image).into(imageView);
                }
        });
    }
    private void getPostImage(ImageView imageView , String postId) {
        firebaseFirestore.collection("Post").document(postId).addSnapshotListener((value, error) -> {
            if (error ==null && value!= null) {
                String postImage = value.getString("postImage");
                Glide.with(context).load(postImage).into(imageView);
            }
        });
    }

}
