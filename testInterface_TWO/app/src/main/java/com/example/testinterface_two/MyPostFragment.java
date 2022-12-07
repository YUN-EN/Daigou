package com.example.testinterface_two;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.testinterface_two.Adapter.DealAdapter;
import com.example.testinterface_two.models.Deal;
import com.example.testinterface_two.models.DealListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

public class MyPostFragment extends Fragment implements DealListener {
    private List<Deal> sellerList;
    private DealAdapter sellerDealAdapter;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_post ,container ,false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String finishSellerID = user.getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = view.findViewById(R.id.clearSellerDealRecyclerView);
        sellerList = new ArrayList<>();
        sellerDealAdapter = new DealAdapter(sellerList, (Activity)getContext() ,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sellerDealAdapter);
        database.collection("Deals").whereEqualTo("sellerId", finishSellerID).whereEqualTo("isReceive", true).addSnapshotListener((value, error) -> {
            if (value != null) {
                for (DocumentChange documentChange:value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        Deal deal = documentChange.getDocument().toObject(Deal.class).withId(id);
                        sellerList.add(deal);
                    }
                }
                sellerDealAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void DealClick(Deal deal) {
        Intent intent = new Intent(getActivity() ,AllDetailOfUserActivity.class);
        intent.putExtra("dealSellerInformation", deal);
        intent.putExtra("dealSellerIDInformation", deal.DealId);
        startActivity(intent);
    }
}