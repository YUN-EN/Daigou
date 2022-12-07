package com.example.testinterface_two;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.testinterface_two.Adapter.CommentAdapter;
import com.example.testinterface_two.databinding.ActivityCommentBinding;
import com.example.testinterface_two.models.Comment;
import com.example.testinterface_two.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class commentActivity extends AppCompatActivity {
    private ActivityCommentBinding binding;
    private FirebaseFirestore database;
    private String post_id;
    private FirebaseUser user;
    private String ID;
    private CommentAdapter commentAdapter;
    private List<Comment> list;
    private List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        ID = user.getUid();
        post_id = getIntent().getStringExtra("PostId");

        list = new ArrayList<>();
        userList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentActivity.this ,list ,userList);
        binding.commentRecyclerView.setHasFixedSize(true);
        binding.commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.commentRecyclerView.setAdapter(commentAdapter);
        binding.goBackImage.setOnClickListener(v -> onBackPressed());

        binding.addComment.setOnClickListener(v -> {
            String comment = binding.commentText.getText().toString();
            if (!comment.isEmpty()) {
                Intent intent = getIntent();
                String posterID = intent.getStringExtra("posterId");
                HashMap<String , Object> commentMap = new HashMap<>();
                commentMap.put("comment" ,comment);
                commentMap.put("time" , FieldValue.serverTimestamp());
                commentMap.put("user" ,ID);
                database.collection("Post/" + post_id + "/Comments").add(commentMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap<String ,Object> map = new HashMap<>();
                        map.put("userId" , ID);
                        map.put("notifyMessage" , "comment :" +binding.commentText.getText().toString());
                        map.put("postId" , post_id);
                        map.put("isPost" ,true);
                        map.put("posterID", posterID);
                        database.collection("Notify").add(map);
                        Toast.makeText(commentActivity.this, "留言成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(commentActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(commentActivity.this, "請寫些文字", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        database.collection("Post/" + post_id + "/Comments").addSnapshotListener(commentActivity.this , (value, error) -> {
            assert value != null;
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Comment comment =documentChange.getDocument().toObject(Comment.class);
                    String userId = documentChange.getDocument().getString("user");
                    assert userId != null;
                    database.collection("Users").document(userId).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            User users = Objects.requireNonNull(task.getResult()).toObject(User.class);
                            userList.add(users);
                            list.add(comment);
                            commentAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(commentActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    commentAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}