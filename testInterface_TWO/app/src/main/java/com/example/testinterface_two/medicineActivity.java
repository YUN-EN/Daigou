package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.testinterface_two.Adapter.ShopAdapter;
import com.example.testinterface_two.databinding.ActivityMedicineBinding;
import com.example.testinterface_two.models.Shop;
import com.example.testinterface_two.models.ShopListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class medicineActivity extends AppCompatActivity implements ShopListener {
    private ActivityMedicineBinding binding;
    private List<Shop> shopMedicineList;
    private ShopAdapter shopMedicineAdapter;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMedicineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        binding.bagRecyclerView.setHasFixedSize(true);
        binding.bagRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        shopMedicineList = new ArrayList<>();
        shopMedicineAdapter = new ShopAdapter(this, shopMedicineList, this);
        binding.bagRecyclerView.setAdapter(shopMedicineAdapter);
        binding.goMedicineCategory.setOnClickListener(v -> onBackPressed());
        showMedicine();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showMedicine() {
        firebaseFirestore.collection("Item").whereEqualTo("itemCategory", "medicine").addSnapshotListener(this , (value, error) -> {
            if (value != null) {
                for (DocumentChange documentChange:value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        Shop shopBagItem = documentChange.getDocument().toObject(Shop.class).withId(id);
                        shopMedicineList.add(shopBagItem);
                    }
                }
                shopMedicineAdapter.notifyDataSetChanged();
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