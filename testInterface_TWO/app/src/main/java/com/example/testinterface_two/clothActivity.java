package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.example.testinterface_two.Adapter.ShopAdapter;
import com.example.testinterface_two.databinding.ActivityClothBinding;
import com.example.testinterface_two.models.Shop;
import com.example.testinterface_two.models.ShopListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class clothActivity extends AppCompatActivity implements ShopListener {
    private ActivityClothBinding binding;
    private List<Shop> shopList;
    private ShopAdapter shopAdapter;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityClothBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        binding.clothRecyclerView.setHasFixedSize(true);
        binding.clothRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        shopList = new ArrayList<>();
        shopAdapter = new ShopAdapter(this, shopList, this);
        binding.clothRecyclerView.setAdapter(shopAdapter);
        binding.toCategory.setOnClickListener(v -> onBackPressed());
        showResult();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void showResult() {
        firebaseFirestore.collection("Item").whereEqualTo("itemCategory", "clothes").addSnapshotListener(this , (value, error) -> {
            if (value != null) {
                for (DocumentChange documentChange:value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        Shop shopItem = documentChange.getDocument().toObject(Shop.class).withId(id);
                        shopList.add(shopItem);
                    }
                }
                shopAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onShopClicked(Shop shop) {
        Intent intent = new Intent(this ,AllShopInformationActivity.class);
        intent.putExtra("shop", shop);
        intent.putExtra("shopID", shop.ItemID);
        startActivity(intent);
    }
}