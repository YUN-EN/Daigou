package com.example.testinterface_two;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.example.testinterface_two.databinding.ActivityAllDeakInformationBinding;
import com.example.testinterface_two.models.Deal;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AllDeakInformationActivity extends AppCompatActivity {
    private ActivityAllDeakInformationBinding binding;
    private Deal deal;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllDeakInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        getDeal();
        set();
    }
    private void set() {
        binding.goClear.setOnClickListener(v -> onBackPressed());
        binding.goShop.setOnClickListener(v -> startActivity(new Intent(this , shopActivity.class)));
    }
    private void getDeal() {
        deal = (Deal) getIntent().getSerializableExtra("dealInformation");
        Intent intent = getIntent();
        String id = intent.getStringExtra("dealIDInformation");
        binding.itemIDInformation.setText(id);
        binding.itemDateInformation.setText(deal.dealDate);
        binding.itemBuyNumInformation.setText(deal.dealItemCount);
        binding.itemSellerID.setText(deal.sellerId);
        int c = Integer.parseInt(deal.dealItemCount);
        int t = Integer.parseInt(deal.dealItemPrice);
        binding.itemBuyTotalInformation.setText(String.valueOf(t/c));
        binding.itemSellerNameInformation.setText(deal.sellerName);
        binding.itemReceiveDateInformation.setText(deal.dealFinishDate);
        binding.itemBuyAllInformation.setText(deal.dealItemPrice);
        String contract = deal.dealBlockAddress;
        binding.itemBAddressAllInformation.setText(contract);
        binding.itemBAddressAllInformation.setOnClickListener(v -> {
            String contract_http ="https://goerli.etherscan.io/address/"+contract;
            Uri contract_uri = Uri.parse(contract_http);
            Intent  webIntent = new Intent(Intent.ACTION_VIEW,contract_uri);
            startActivity(webIntent);
        });
        String ID = deal.sellerId;
        database.collection("Users").document(ID).get().addOnCompleteListener(task -> {
            String mail = Objects.requireNonNull(task.getResult()).getString("userMail");
            binding.itemSellerMailInformation.setText(mail);
        });
    }
}