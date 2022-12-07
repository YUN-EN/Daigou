package com.example.testinterface_two;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.testinterface_two.Adapter.PastDealCommentAdapter;
import com.example.testinterface_two.databinding.ActivityPastDealCommentBinding;
import com.example.testinterface_two.models.DealComment;
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

public class PastDealCommentActivity extends AppCompatActivity {
    private ActivityPastDealCommentBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private Query query;
    private ListenerRegistration listenerRegistration;
    private List<DealComment> pastDealCommentList;
    private List<User> userList;
    private PastDealCommentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPastDealCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        binding.pastDealCommentRecyclerView.setHasFixedSize(true);
        binding.pastDealCommentRecyclerView.setLayoutManager(new LinearLayoutManager(PastDealCommentActivity.this));
        pastDealCommentList = new ArrayList<>();
        userList = new ArrayList<>();
        adapter = new PastDealCommentAdapter(pastDealCommentList, this, userList);
        binding.pastDealCommentRecyclerView.setAdapter(adapter);
        setListen();
        shoePast();
    }

    private void setListen() {
        binding.toItemDetail.setOnClickListener(v -> onBackPressed());
    }
    @SuppressLint("NotifyDataSetChanged")
    private void shoePast() {
        if(firebaseUser != null ) {
            binding.pastDealCommentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    boolean isBottom = !binding.pastDealCommentRecyclerView.canScrollVertically(1);
                    if (isBottom) {
                        Toast.makeText(PastDealCommentActivity.this, "Reach bottom", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Intent intent = getIntent();
            query = firebaseFirestore.collection("ItemComment").whereEqualTo("commentItemID", intent.getStringExtra("dealCommentItemID"));
            listenerRegistration = query.addSnapshotListener(PastDealCommentActivity.this, (value, error) -> {
                assert value != null;
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String pastDealCommentID = documentChange.getDocument().getId();
                        DealComment dealComment =documentChange.getDocument().toObject(DealComment.class).withId(pastDealCommentID);
                        String id = documentChange.getDocument().getString("user");
                        assert id != null;
                        firebaseFirestore.collection("Users").document(id).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                User user = Objects.requireNonNull(task.getResult()).toObject(User.class);
                                userList.add(user);
                                pastDealCommentList.add(dealComment);
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(PastDealCommentActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    adapter.notifyDataSetChanged();
                }
                listenerRegistration.remove();
            });
        }
    }
}