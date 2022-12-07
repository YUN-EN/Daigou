package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.testinterface_two.Adapter.FragmentAdapter;
import com.example.testinterface_two.databinding.ActivityClearDealBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class ClearDealActivity extends AppCompatActivity {
    private ActivityClearDealBinding binding;
    private final String[] titles = {"買家交易紀錄" ,"賣家交易紀錄"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClearDealBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FragmentAdapter adapter = new FragmentAdapter(this);
        binding.viewPagerClearDeal.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout , binding.viewPagerClearDeal ,((tab, position) -> tab.setText(titles[position]))).attach();
        setListen();
    }
    private void setListen() {
        binding.goToPerson.setOnClickListener(v -> onBackPressed());
    }
}