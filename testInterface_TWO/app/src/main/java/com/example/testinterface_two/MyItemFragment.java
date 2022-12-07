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

public class MyItemFragment extends Fragment implements DealListener {

    private List<Deal> overList;
    private DealAdapter dealAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_my_item , container ,false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String finishID = user.getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = root.findViewById(R.id.clearDealRecyclerView);
        overList = new ArrayList<>();
        dealAdapter = new DealAdapter(overList , (Activity) getContext() ,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(dealAdapter);
        database.collection("Deals").whereEqualTo("dealUserId" , finishID).whereEqualTo("isReceive" ,true).addSnapshotListener( (value, error) -> {
            if (value != null) {
                for (DocumentChange documentChange:value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        Deal finishDeal = documentChange.getDocument().toObject(Deal.class).withId(id);
                        overList.add(finishDeal);
                    }
                }
                dealAdapter.notifyDataSetChanged();
            }
        });
        return root;
    }

    @Override
    public void DealClick(Deal deal) {
        Intent intent = new Intent(getActivity() ,AllDeakInformationActivity.class);
        intent.putExtra("dealInformation" ,deal);
        intent.putExtra("dealIDInformation", deal.DealId);
        startActivity(intent);
    }
}