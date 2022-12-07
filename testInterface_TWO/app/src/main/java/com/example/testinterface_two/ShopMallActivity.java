package com.example.testinterface_two;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.testinterface_two.Adapter.ShopAdapter;
import com.example.testinterface_two.databinding.ActivityShopMallBinding;
import com.example.testinterface_two.models.Shop;
import com.example.testinterface_two.models.ShopListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class ShopMallActivity extends AppCompatActivity implements ShopListener {
    private ActivityShopMallBinding binding;
    private List<Shop> shopList;
    private ShopAdapter shopAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fireStore;
    private Query query;
    private ListenerRegistration listenerRegistration;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopMallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        binding.floatingButton.setOnClickListener(v -> startActivity(new Intent(ShopMallActivity.this ,AddItemActivity.class)));
        //binding.includeToolbar.goAllMyShopCart.setOnClickListener(v -> startActivity(new Intent(ShopMallActivity.this , MyShopCartActivity.class)));
       // binding.recyclerViewShopCart.setHasFixedSize(true);
      //  binding.recyclerViewShopCart.setLayoutManager(new GridLayoutManager(getApplicationContext() ,2));
        shopList = new ArrayList<>();
        shopAdapter = new ShopAdapter(ShopMallActivity.this , shopList ,ShopMallActivity.this);
        binding.recyclerViewShopCart.setAdapter(shopAdapter);

        if (firebaseUser != null) {
            binding.recyclerViewShopCart.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean isBottom = !binding.recyclerViewShopCart.canScrollVertically(1);
                    if(isBottom) {
                        Toast.makeText(ShopMallActivity.this ,"Reach bottom" ,Toast.LENGTH_SHORT).show();
                    }
                }
            });
            query = fireStore.collection("Item").orderBy("itemDate"  ,Query.Direction.DESCENDING);
            listenerRegistration = query.addSnapshotListener(ShopMallActivity.this, (value, error) -> {
                assert value != null;
                for (DocumentChange documentChange :value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String ItemID = documentChange.getDocument().getId();
                        Shop shop = documentChange.getDocument().toObject(Shop.class).withId(ItemID);
                        shopList.add(shop);
                        shopAdapter.notifyDataSetChanged();
                    }
                }
                listenerRegistration.remove();
            });
        }
    }
    
    @Override
    public void onShopClicked(Shop shop) {
        Intent intent = new Intent(getApplicationContext() ,AllShopInformationActivity.class);
        intent.putExtra("shop" ,  shop);
        startActivity(intent);
        finish();
    }
}