package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.testinterface_two.Adapter.ShopAdapter;
import com.example.testinterface_two.databinding.ActivityShoesBinding;
import com.example.testinterface_two.models.Shop;
import com.example.testinterface_two.models.ShopListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class shoesActivity extends AppCompatActivity implements ShopListener {
    private ActivityShoesBinding binding;

    private List<Shop> shopShoesList;
    private ShopAdapter shopShoesAdapter;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        binding.shoesRecyclerView.setHasFixedSize(true);
        binding.shoesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        shopShoesList = new ArrayList<>();
        shopShoesAdapter = new ShopAdapter(this, shopShoesList, this);
        binding.shoesRecyclerView.setAdapter(shopShoesAdapter);
        binding.goCategory.setOnClickListener(v -> onBackPressed());
        showShoes();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void showShoes() {
        firebaseFirestore.collection("Item").whereEqualTo("itemCategory", "shoes").addSnapshotListener(this , (value, error) -> {
            if (value != null) {
                for (DocumentChange documentChange:value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        Shop shopBagItem = documentChange.getDocument().toObject(Shop.class).withId(id);
                        shopShoesList.add(shopBagItem);
                    }
                }
                shopShoesAdapter.notifyDataSetChanged();
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