package com.example.testinterface_two;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.Adapter.ShopAdapter;
import com.example.testinterface_two.databinding.ActivitySellerDetailBinding;
import com.example.testinterface_two.models.Shop;
import com.example.testinterface_two.models.ShopListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SellerDetailActivity extends AppCompatActivity implements ShopListener {
    private ActivitySellerDetailBinding binding;
    private FirebaseFirestore database;
    private List<Shop> shopList;
    private ShopAdapter adapter;
    private ListenerRegistration listenerRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellerDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        binding.recyclerViewSellerItemPost.setHasFixedSize(true);
        binding.recyclerViewSellerItemPost.setLayoutManager(new GridLayoutManager(this ,2));
        shopList = new ArrayList<>();
        adapter  = new ShopAdapter(this, shopList, this);
        binding.recyclerViewSellerItemPost.setAdapter(adapter);
        binding.recyclerViewSellerItemPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottom = !recyclerView.canScrollVertically(1);
                if(isBottom) {
                    Toast.makeText(SellerDetailActivity.this ,"Reach bottom" ,Toast.LENGTH_SHORT).show();
                }
            }
        });
        Listener();
        getSeller();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void getSeller() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("SellerID");
        database.collection("Users").document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String sellerName  = Objects.requireNonNull(task.getResult()).getString("userName");
                String sellerMail = task.getResult().getString("userMail");
                String sellerImage = task.getResult().getString("userImage");
                binding.sellerUserName.setText(sellerName);
                binding.sellerUserMail.setText(sellerMail);
                Glide.with(SellerDetailActivity.this).load(sellerImage).into(binding.sellerImage);
            }
        });
        Query query = database.collection("Item").orderBy("itemDate", Query.Direction.DESCENDING);
        listenerRegistration = query.addSnapshotListener((value, error) -> {
            assert value != null;
            for (DocumentChange documentChange :value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String ItemID = documentChange.getDocument().getId();
                    String ItemUserID = documentChange.getDocument().getString("userID");
                    if (id.equals(ItemUserID)) {
                        Shop shop = documentChange.getDocument().toObject(Shop.class).withId(ItemID);
                        shopList.add(shop);
                        adapter.notifyDataSetChanged();
                    }

                }
            }
            listenerRegistration.remove();
        });
    }
        private void Listener() {
        binding.backItemAllDetail.setOnClickListener(v -> onBackPressed());
    }
    @Override
    public void onShopClicked(Shop shop) {}
}