package com.example.testinterface_two;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.testinterface_two.Adapter.PostAdapter;
import com.example.testinterface_two.databinding.ActivityPostBinding;
import com.example.testinterface_two.models.Post;
import com.example.testinterface_two.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class postActivity extends AppCompatActivity {
    private ActivityPostBinding binding;
    private List<Post> list ;
    private PostAdapter postAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fireStore;
    private ListenerRegistration listenerRegistration;
    private List<User> userList;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        binding.floatingActionButton.setOnClickListener(v -> startActivity(new Intent(postActivity.this ,addPostActivity.class)));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(postActivity.this));

        list = new ArrayList<>();
        userList = new ArrayList<>();
        postAdapter = new PostAdapter(postActivity.this ,list ,userList);
        binding.recyclerView.setAdapter(postAdapter);

        if(firebaseUser != null ) {
            binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    boolean isBottom = !binding.recyclerView.canScrollVertically(1);
                    if(isBottom) {
                        Toast.makeText(postActivity.this ,"Reach bottom" ,Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Query query = fireStore.collection("Post").orderBy("Time", Query.Direction.DESCENDING);
            listenerRegistration = query.addSnapshotListener(postActivity.this, (value, error) -> {
                assert value != null;
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String postID = documentChange.getDocument().getId();
                        Post post = documentChange.getDocument().toObject(Post.class).withId(postID);
                        String postId = documentChange.getDocument().getString("User");
                        assert postId != null;
                        fireStore.collection("Users").document(postId).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                User user = Objects.requireNonNull(task.getResult()).toObject(User.class);
                                userList.add(user);
                                list.add(post);
                                postAdapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(postActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    postAdapter.notifyDataSetChanged();
                }
                listenerRegistration.remove();
            });
        }
    }





}