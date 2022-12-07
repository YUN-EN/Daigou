package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.testinterface_two.databinding.ActivityAllButtonBinding;

public class AllButtonActivity extends AppCompatActivity {
    private ActivityAllButtonBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllButtonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

}