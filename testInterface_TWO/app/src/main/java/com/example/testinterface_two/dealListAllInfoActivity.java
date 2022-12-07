package com.example.testinterface_two;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.databinding.ActivityDealListAllInfoBinding;
import com.example.testinterface_two.models.Deal;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class dealListAllInfoActivity extends AppCompatActivity {
    private ActivityDealListAllInfoBinding binding;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDealListAllInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        binding.backList.setOnClickListener(v -> startActivity(new Intent(this ,listActivity.class)));
        get();
    }
    private void get() {
        Deal dealList = (Deal) getIntent().getSerializableExtra("dealList");
        String id = dealList.dealUserId;
        firebaseFirestore.collection("Users").document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String name  = task.getResult().getString("userName");
                String image = task.getResult().getString("userImage");
                binding.textViewName.setText(name);
                Glide.with(dealListAllInfoActivity.this).load(image).into(binding.userImage);
            } else {
                Toast.makeText(dealListAllInfoActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.textItemName.setText(dealList.dealItemName);
        binding.textItemNum.setText(dealList.dealItemCount);
        binding.textItemOnePrice.setText(dealList.dealItemPrice);
        binding.textItemNotice.setText(dealList.dealItemFormat);
        binding.textItemAddress.setText(dealList.dealAddress);
        binding.textDate.setText(dealList.dealDate);
        int  a = Integer.parseInt(dealList.dealItemCount);
        int  b= Integer.parseInt(dealList.dealItemPrice);
        binding.textItemTotalPrice.setText(String.valueOf(a*b));
    }
}