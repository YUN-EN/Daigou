package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.example.testinterface_two.Adapter.ShopAdapter;
import com.example.testinterface_two.databinding.ActivitySnackBinding;
import com.example.testinterface_two.models.Shop;
import com.example.testinterface_two.models.ShopListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class snackActivity extends AppCompatActivity implements ShopListener {
    private ActivitySnackBinding binding;
    private List<Shop> shopSnackList;
    private ShopAdapter shopSnackAdapter;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySnackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        binding.snackRecyclerView.setHasFixedSize(true);
        binding.snackRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        shopSnackList = new ArrayList<>();
        shopSnackAdapter = new ShopAdapter(this, shopSnackList,this);
        binding.snackRecyclerView.setAdapter(shopSnackAdapter);
        binding.toSnackCategory.setOnClickListener(v -> onBackPressed());
        showSnack();

    }
    @SuppressLint("NotifyDataSetChanged")
    private void showSnack() {
        firebaseFirestore.collection("Item").whereEqualTo("itemCategory", "snack").addSnapshotListener(this , (value, error) -> {
            if (value != null) {
                for (DocumentChange documentChange:value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        Shop shopItem = documentChange.getDocument().toObject(Shop.class).withId(id);
                        shopSnackList.add(shopItem);
                    }
                }
                shopSnackAdapter.notifyDataSetChanged();
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