package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.testinterface_two.Adapter.ShopAdapter;
import com.example.testinterface_two.databinding.ActivityAccessoriesBinding;
import com.example.testinterface_two.models.Shop;
import com.example.testinterface_two.models.ShopListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class accessoriesActivity extends AppCompatActivity implements ShopListener {
    private ActivityAccessoriesBinding binding;
    private List<Shop> shopAccessoriesList;
    private ShopAdapter shopAccessoriesAdapter;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccessoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        binding.accessoriesRecyclerView.setHasFixedSize(true);
        binding.accessoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        shopAccessoriesList = new ArrayList<>();
        shopAccessoriesAdapter = new ShopAdapter(this, shopAccessoriesList, this);
        binding.accessoriesRecyclerView.setAdapter(shopAccessoriesAdapter);
        binding.toAccessoriesCategory.setOnClickListener(v -> onBackPressed());
        showAccessories();

    }
    @SuppressLint("NotifyDataSetChanged")
    private void showAccessories() {
        firebaseFirestore.collection("Item").whereEqualTo("itemCategory", "accessories").addSnapshotListener(this , (value, error) -> {
            if (value != null) {
                for (DocumentChange documentChange:value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        Shop shopItem = documentChange.getDocument().toObject(Shop.class).withId(id);
                        shopAccessoriesList.add(shopItem);
                    }
                }
                shopAccessoriesAdapter.notifyDataSetChanged();
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