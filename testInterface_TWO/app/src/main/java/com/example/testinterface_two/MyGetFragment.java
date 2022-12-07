package com.example.testinterface_two;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.testinterface_two.Adapter.MyGetCoinAdapter;
import com.example.testinterface_two.models.CoinRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;


public class MyGetFragment extends Fragment {
    private List<CoinRecord> coinRecordList;
    private MyGetCoinAdapter myGetCoinAdapter;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_my_get, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        RecyclerView recyclerViewCoin = view.findViewById(R.id.RecordGetRecyclerView);
        assert user != null;
        String ID = user.getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        recyclerViewCoin.setHasFixedSize(true);
        recyclerViewCoin.setLayoutManager(new LinearLayoutManager(getContext()));
        coinRecordList = new ArrayList<>();
        myGetCoinAdapter = new MyGetCoinAdapter((Activity) getContext() ,coinRecordList);
        recyclerViewCoin.setAdapter(myGetCoinAdapter);
        database.collection("CoinRecord").whereEqualTo("userCoinRecord", ID).addSnapshotListener((value, error) -> {
            assert value != null;
            for (DocumentChange documentChange: value.getDocumentChanges()) {
                String coinID = documentChange.getDocument().getId();
                CoinRecord coinRecord = documentChange.getDocument().toObject(CoinRecord.class).withId(coinID);
                coinRecordList.add(coinRecord);
            }
            myGetCoinAdapter.notifyDataSetChanged();
        });
        return view;
    }
}