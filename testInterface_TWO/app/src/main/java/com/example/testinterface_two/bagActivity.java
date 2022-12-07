package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.testinterface_two.Adapter.ShopAdapter;
import com.example.testinterface_two.databinding.ActivityBagBinding;
import com.example.testinterface_two.models.Shop;
import com.example.testinterface_two.models.ShopListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class bagActivity extends AppCompatActivity implements ShopListener {
    private ActivityBagBinding binding;
    private List<Shop> shopBagList;
    private ShopAdapter shopBagAdapter;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        binding.bagRecyclerView.setHasFixedSize(true);
        binding.bagRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        shopBagList = new ArrayList<>();
        shopBagAdapter = new ShopAdapter(this, shopBagList, this);
        binding.bagRecyclerView.setAdapter(shopBagAdapter);
        binding.goToCategory.setOnClickListener(v -> onBackPressed());
        showBag();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void showBag() {
        firebaseFirestore.collection("Item").whereEqualTo("itemCategory", "bag").addSnapshotListener(this , (value, error) -> {
            if (value != null) {
                for (DocumentChange documentChange:value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        Shop shopBagItem = documentChange.getDocument().toObject(Shop.class).withId(id);
                        shopBagList.add(shopBagItem);
                    }
                }
                shopBagAdapter.notifyDataSetChanged();
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