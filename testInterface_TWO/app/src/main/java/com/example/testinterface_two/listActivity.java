package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.testinterface_two.Adapter.dealListAdapter;
import com.example.testinterface_two.databinding.ActivityListBinding;
import com.example.testinterface_two.models.Deal;
import com.example.testinterface_two.models.DealListListener;
import com.example.testinterface_two.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class listActivity extends AppCompatActivity implements DealListListener {
    private ActivityListBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private List<Deal> dealList;
    private List<User> userList;
    private FirebaseUser user;
    private dealListAdapter dealListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dealList = new ArrayList<>();
        userList = new ArrayList<>();
        dealListAdapter = new dealListAdapter(dealList, userList, this, this);
        binding.dealListRecyclerView.setHasFixedSize(true);
        binding.dealListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.dealListRecyclerView.setAdapter(dealListAdapter);
        binding.b.setOnClickListener(v -> startActivity(new Intent(this ,shopActivity.class)));
        showList();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showList() {
        firebaseFirestore.collection("Deals").whereEqualTo("sellerId", user.getUid()).whereEqualTo("isReceive", false).addSnapshotListener((value, error) -> {
            if (value != null) {
                for (DocumentChange documentChange: value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String listID = documentChange.getDocument().getId();
                        String out = documentChange.getDocument().getString("isOut");
                        Deal deal = documentChange.getDocument().toObject(Deal.class).withId(listID);
                        String buyerId = documentChange.getDocument().getString("dealUserId");
                        assert buyerId != null;
                        firebaseFirestore.collection("Users").document(buyerId).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                assert out != null;
                                if (out.equals("no")) {
                                    User user = Objects.requireNonNull(task.getResult()).toObject(User.class);
                                    userList.add(user);
                                    dealList.add(deal);
                                    dealListAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(listActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    dealListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void dealListClick(Deal deal) {
        Intent intent = new Intent(this ,dealListAllInfoActivity.class);
        intent.putExtra("dealList", deal);
        startActivity(intent);
        finish();
    }
}