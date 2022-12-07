package com.example.testinterface_two.Adapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.R;
import com.example.testinterface_two.commentActivity;
import com.example.testinterface_two.editPostActivity;
import com.example.testinterface_two.models.Post;
import com.example.testinterface_two.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>  {
    private final Activity context;
    private List<Post> postList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private final List<User> userList;

    public PostAdapter(Activity context, List<Post> postList , List<User> userList) {
        this.context = context;
        this.postList = postList;
        this.userList = userList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.each_post ,parent ,false);
       firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return new PostViewHolder(v);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.setPostImage(post.getPostImage());
        holder.setPostCaption(post.getCaption());
        holder.setContent(post.getContent());
        Calendar calendar = Calendar.getInstance();
        String date = DateFormat.getTimeInstance().format(calendar.getTime());
        holder.setPostDate(date);
        String username = userList.get(position).getUserName();
        String image = userList.get(position).getUserImage();
        holder.setPostUsername(username);
        holder.setUserImage(image);

        String idUser = post.getUser();
        String posterID = post.getUser();
        String postId = post.PostId;
        String currentUserId = firebaseUser.getUid();
        holder.moreImageButton.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.bottom_sheet_layout);
            LinearLayout editLinearLayout = dialog.findViewById(R.id.layoutEdit);
            LinearLayout deleteLinearLayout = dialog.findViewById(R.id.layoutDelete);
            LinearLayout cancelLinearLayout = dialog.findViewById(R.id.layoutCancel);
            firebaseFirestore.collection("Post").document(postId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String id = task.getResult().getString("User");
                    assert id != null;
                    if (id.equals(firebaseUser.getUid())) {
                        editLinearLayout.setVisibility(View.VISIBLE);
                        deleteLinearLayout.setVisibility(View.VISIBLE);
                        editLinearLayout.setOnClickListener(v1 ->{
                            Intent intent = new Intent(context , editPostActivity.class);
                            intent.putExtra("postId", postId);
                            context.startActivity(intent);
                            dialog.dismiss();
                        });
                        deleteLinearLayout.setOnClickListener(v12 -> {
                           firebaseFirestore.collection("Post/" + postId +"/Comments").get().addOnCompleteListener(task1 -> {
                                for (QueryDocumentSnapshot queryDocumentSnapshot: Objects.requireNonNull(task1.getResult())) {
                                    firebaseFirestore.collection("Post/" + postId + "/Comments").document(queryDocumentSnapshot.getId()).delete();
                                }
                            });
                            firebaseFirestore.collection("Post/" + postId +"/Likes").get().addOnCompleteListener(task2 -> {
                                for (QueryDocumentSnapshot queryDocumentSnapshot: Objects.requireNonNull(task2.getResult())) {
                                    firebaseFirestore.collection("Post/" + postId + "/Likes").document(queryDocumentSnapshot.getId()).delete();
                                }
                            });
                            firebaseFirestore.collection("Post").document(postId).delete();
                            postList.remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                            Toast.makeText(context, "刪除成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }
                }
            });

            cancelLinearLayout.setOnClickListener(v13 -> dialog.dismiss());
            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT ,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationMore;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        });

        holder.saveBookmarkImage.setOnClickListener(v -> firebaseFirestore.collection("SavePost").document(postId).get().addOnCompleteListener(task -> {
            if (!Objects.requireNonNull(task.getResult()).exists()) {
                HashMap<String ,Object> save = new HashMap<>();
                save.put("saveUserId" ,currentUserId);
                save.put("PostId" ,postId);
                save.put("PostImage" ,post.getPostImage());
                save.put("PostTitle" ,post.getCaption());
                save.put("DateTime" , FieldValue.serverTimestamp());
                firebaseFirestore.collection("SavePost").document(postId).set(save);
            } else {
                firebaseFirestore.collection("SavePost").document(postId).delete();
            }
        }));

        holder.likeImage.setOnClickListener(v -> firebaseFirestore.collection("Post/" +postId+ "/Likes").document(currentUserId).get().addOnCompleteListener(task -> {
            if(!Objects.requireNonNull(task.getResult()).exists()) {
                HashMap<String , Object> likes = new HashMap<>();
                likes.put("timeStamp" , FieldValue.serverTimestamp());
                firebaseFirestore.collection("Post/" + postId + "/Likes").document(currentUserId).set(likes);
                HashMap<String ,Object> map = new HashMap<>();
                map.put("userId" , firebaseUser.getUid());
                map.put("posterID", posterID);
                map.put("notifyMessage" , "like your post");
                map.put("postId" , postId);
                map.put("isPost" ,true);
                firebaseFirestore.collection("Notify").add(map);
            } else {
                firebaseFirestore.collection("Post/" + postId + "/Likes").document(currentUserId).delete();
            }
        }));

        firebaseFirestore.collection("SavePost").document(postId).addSnapshotListener((value, error) -> {
            if (error== null) {
                assert value != null;
                if (value.exists()) {
                    Objects.requireNonNull(holder).saveBookmarkImage.setImageResource(R.drawable.ic_savebookmark);
                }else {
                    Objects.requireNonNull(holder).saveBookmarkImage.setImageResource(R.drawable.ic_bookmark);
                }
            }
        });
        firebaseFirestore.collection("Post/" + postId + "/Likes").document(currentUserId).addSnapshotListener((value, error) -> {
            if (error == null) {
                assert value != null;
                if (value.exists()) {
                    holder.likeImage.setImageResource(R.drawable.ic_favorite);

                } else {
                    holder.likeImage.setImageResource(R.drawable.ic_before_like);
                }
            }
        });

        firebaseFirestore.collection("Post/" + postId + "/Likes").addSnapshotListener((value, error) -> {
            if (error == null) {
                assert value != null;
                if (!value.isEmpty()) {
                    int count = value.size();
                    holder.setPostLikes(count);
                   if (count >= 100 && count <200) {
                        go("1" ,postId ,idUser);
                    } else if (count >= 200 && count <300) {
                        go("2" ,postId ,idUser);
                    } else if (count >=300 && count<400) {
                        go("3" ,postId ,idUser);
                    }
                } else {
                    holder.setPostLikes(0);
                }
            }
        });
        holder.commentImage.setOnClickListener(v -> {
            Intent commentIntent = new Intent(context , commentActivity.class);
            commentIntent.putExtra("PostId" ,postId);
            commentIntent.putExtra("posterId",posterID );
            context.startActivity(commentIntent);
        });
    }
    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends  RecyclerView.ViewHolder {
        ImageView postImage ,commentImage ,likeImage ,saveBookmarkImage;
        ImageButton moreImageButton;
        CircleImageView userImage;
        TextView postUsername ,postDate ,postCaption, postLikes , postContent;
        View view;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            moreImageButton = view.findViewById(R.id.moreButton);
            likeImage = view.findViewById(R.id.beforeLike);
            commentImage = view.findViewById(R.id.commentPost);
            saveBookmarkImage = view.findViewById(R.id.bookmarkImageView);
        }
        @SuppressLint("SetTextI18n")
        public void setPostLikes(int count) {
            postLikes = view.findViewById(R.id.likeCount);
            postLikes.setText(count + " Likes");
        }

        public void setPostImage(String image) {
            postImage = view.findViewById(R.id.imagePost);
            Glide.with(context).load(image).into(postImage);
        }
        public void setUserImage(String UserImageUri) {
            userImage = view.findViewById(R.id.profilePic);
            Glide.with(context).load(UserImageUri).into(userImage);
        }
        public void setPostUsername(String username) {
            postUsername = view.findViewById(R.id.usernameProfile);
            postUsername.setText(username);
        }
        public void setPostDate(String date) {
            postDate = view.findViewById(R.id.datePost);
            postDate.setText(date);
        }
        public void setPostCaption(String caption) {
            postCaption = view.findViewById(R.id.captionPost);
            postCaption.setText(caption);
        }
        public void setContent(String content) {
            postContent = view.findViewById(R.id.contentPost);
            postContent.setText(content);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Post> filterList) {
        postList = filterList;
        notifyDataSetChanged();
    }

    private void go(String v ,String id ,String userID) {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = dateFormat.format(calendar.getTime());
        HashMap<String ,Object> go = new HashMap<>();
        go.put("userCoinRecord" ,userID);
        go.put("dateCoinRecord" ,date);
        go.put("numCoinRecord", v);
        go.put("dealIdCoinRecord", id);
        go.put("categoryCoinRecord", "貼文獎勵");
        firebaseFirestore.collection("CoinRecord").add(go);
        firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int m  = Integer.parseInt(Objects.requireNonNull(task.getResult().getString("userCoin")));
                int vv = Integer.parseInt(v);
                firebaseFirestore.collection("Users").document(userID).update("userCoin" ,String.valueOf(m+vv));
            }
        });
    }
}

