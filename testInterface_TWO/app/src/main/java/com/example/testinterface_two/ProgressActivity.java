package com.example.testinterface_two;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.example.testinterface_two.Adapter.DealAdapter;
import com.example.testinterface_two.databinding.ActivityProgressBinding;
import com.example.testinterface_two.models.Deal;
import com.example.testinterface_two.models.DealListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ProgressActivity extends AppCompatActivity implements DealListener {
    private ActivityProgressBinding binding;
    private List<Deal> list;
    private DealAdapter dealAdapter;
    private FirebaseFirestore database;
    private String dealUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        dealUserID = user.getUid();
        database = FirebaseFirestore.getInstance();
        binding.dealProgressRecyclerView.setHasFixedSize(true);
        binding.dealProgressRecyclerView.setLayoutManager(new LinearLayoutManager(ProgressActivity.this));
        list = new ArrayList<>();
        dealAdapter = new DealAdapter(list ,this ,this);
        binding.dealProgressRecyclerView.setAdapter(dealAdapter);
        setListener();
        showAllDeal();
    }

    private void setListener() {
        binding.goBackPerson.setOnClickListener(v -> onBackPressed());
    }
    @SuppressLint("NotifyDataSetChanged")
    private void showAllDeal() {
        database.collection("Deals").whereEqualTo("dealUserId" ,dealUserID).addSnapshotListener(this, (value, error) -> {
                if (value != null) {
                    for (DocumentChange documentChange:value.getDocumentChanges()) {
                        boolean check = Boolean.TRUE.equals(documentChange.getDocument().getBoolean("isReceive"));
                        if (!check) {
                            String id = documentChange.getDocument().getId();
                            Deal deal = documentChange.getDocument().toObject(Deal.class).withId(id);
                            list.add(deal);
                        }
                    }
                    dealAdapter.notifyDataSetChanged();
                }
        });
    }

    @Override
    public void DealClick(Deal deal) {

    }
}