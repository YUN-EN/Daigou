package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.example.testinterface_two.Adapter.MyShopCartAdapter;
import com.example.testinterface_two.databinding.ActivityMyShopCartBinding;
import com.example.testinterface_two.models.MyShopCart;
import com.example.testinterface_two.models.MyShopCartListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyShopCartActivity extends AppCompatActivity implements MyShopCartListener {
        private ActivityMyShopCartBinding binding;
        private MyShopCartAdapter myShopCartAdapter;
        private List<MyShopCart> list;
    private String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyShopCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        binding.myShopCartRecyclerView.setHasFixedSize(true);
        binding.myShopCartRecyclerView.setLayoutManager(new LinearLayoutManager(MyShopCartActivity.this));
        list = new ArrayList<>();
        myShopCartAdapter = new MyShopCartAdapter(MyShopCartActivity.this ,list ,this);
        binding.myShopCartRecyclerView.setAdapter(myShopCartAdapter);
        showAllMyShopCart();
    }

    private void setListener() {
        binding.goShopMallImage.setOnClickListener(v -> onBackPressed());
    }
    @SuppressLint("NotifyDataSetChanged")
    private void showAllMyShopCart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        ID = user.getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("MyShopCart").addSnapshotListener(this , (value, error) -> {
          if (value != null) {
              for (DocumentChange documentChange: value.getDocumentChanges()) {
                  if (documentChange.getType() == DocumentChange.Type.ADDED) {
                      if (Objects.requireNonNull(documentChange.getDocument().getString("UserId")).equals(ID)) {
                          String myShopCartID = documentChange.getDocument().getId();
                          MyShopCart myShopCart = documentChange.getDocument().toObject(MyShopCart.class).withId(myShopCartID);
                          list.add(myShopCart);
                          myShopCartAdapter.notifyDataSetChanged();
                      }
                  }
                  myShopCartAdapter.notifyDataSetChanged();
              }
          }
        });
    }

    @Override
    public void MyShopCartClick(MyShopCart myShopCart) {
        Intent intent = new Intent(getApplicationContext() ,DealActivity.class);
        intent.putExtra("MyShopCart" , myShopCart);
        intent.putExtra("Id", myShopCart.MyShopCartId);
        startActivity(intent);
        finish();
    }
}