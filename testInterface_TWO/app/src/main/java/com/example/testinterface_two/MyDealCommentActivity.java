package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.testinterface_two.Adapter.FragmentPastDealCommentAdapter;
import com.example.testinterface_two.databinding.ActivityMyDealCommentBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyDealCommentActivity extends AppCompatActivity {
    private ActivityMyDealCommentBinding binding;
    private final String[] titles = {"商品評論" ,"個人評論"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyDealCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FragmentPastDealCommentAdapter adapter = new  FragmentPastDealCommentAdapter(this);
        binding.viewPagerPastDealComment.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayoutPastDealComment , binding.viewPagerPastDealComment ,((tab, position) -> tab.setText(titles[position]))).attach();
        binding.go.setOnClickListener(v -> onBackPressed());

    }

}