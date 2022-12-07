package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.testinterface_two.databinding.ActivityAllDetailOfUserBinding;
import com.example.testinterface_two.models.Deal;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class AllDetailOfUserActivity extends AppCompatActivity {
    private ActivityAllDetailOfUserBinding binding;
    private Deal userDeal;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllDetailOfUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore  = FirebaseFirestore.getInstance();
        getData();
        setActionButton();
    }
    private void setActionButton() {
        binding.goClearActivity.setOnClickListener(v -> onBackPressed());
        binding.goOut.setOnClickListener(v -> startActivity(new Intent(this ,shopActivity.class)));
    }

    private void getData() {
        userDeal = (Deal)getIntent().getSerializableExtra("dealSellerInformation");
        binding.itemDateBuyerInformation.setText(userDeal.dealDate);
        Intent intent = getIntent();
        String buyerID = intent.getStringExtra("dealSellerIDInformation");
        binding.itemIDBuyerInformation.setText(buyerID);
        binding.itemIDBuyerInformation.setText(buyerID);
        binding.itemBuyNumBuyerInformation.setText(userDeal.dealItemCount);
        binding.itemBuyerBuyAllInformation.setText(userDeal.dealItemPrice);
        int c = Integer.parseInt(userDeal.dealItemCount);
        int t = Integer.parseInt(userDeal.dealItemPrice);
        binding.itemBuyTotalBuyerInformation.setText(String.valueOf(t/c));
        binding.itemReceiveDateBuyerInformation.setText(userDeal.dealFinishDate);
        binding.itemBuyerID.setText(userDeal.dealUserId);
        String id = userDeal.dealUserId;
        firebaseFirestore.collection("Users").document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String name = Objects.requireNonNull(task.getResult()).getString("userName");
                String mail = task.getResult().getString("userMail");
                binding.itemSBuyerNameInformation.setText(name);
                binding.itemBuyerMailInformation.setText(mail);
            }
        });
    }
}