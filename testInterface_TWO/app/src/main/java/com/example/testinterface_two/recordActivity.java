package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.testinterface_two.Adapter.FragmentRecordAdapter;
import com.example.testinterface_two.databinding.ActivityRecordBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class    recordActivity extends AppCompatActivity {
    private ActivityRecordBinding binding;
    private final String[] titles = {"獲得紀錄","花費紀錄"};
    private FirebaseFirestore database;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FragmentRecordAdapter adapter = new FragmentRecordAdapter(this);
        binding.viewPagerRecord.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayoutRecord , binding.viewPagerRecord ,((tab, position) -> tab.setText(titles[position]))).attach();
        set();
    }
    private void set() {
        binding.g.setOnClickListener(v -> onBackPressed());
        database.collection("Users").document(firebaseUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String coin  = task.getResult().getString("userCoin");
                binding.numCoins.setText(coin);
            }
        });
    }
}