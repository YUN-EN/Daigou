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
import com.example.testinterface_two.Adapter.MySpendCoinAdapter;
import com.example.testinterface_two.models.CoinSpend;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
public class MySpendFragment extends Fragment {
    private List<CoinSpend> coinSpendList;
    private MySpendCoinAdapter mySpendCoinAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root  =  inflater.inflate(R.layout.fragment_my_spend, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        RecyclerView recyclerViewCoinSpend = root.findViewById(R.id.RecordSpendRecyclerView);
        assert user != null;
        String ID = user.getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        recyclerViewCoinSpend.setHasFixedSize(true);
        recyclerViewCoinSpend.setLayoutManager(new LinearLayoutManager(getContext()));
        coinSpendList = new ArrayList<>();
        mySpendCoinAdapter = new MySpendCoinAdapter((Activity) getContext() ,coinSpendList);
        recyclerViewCoinSpend.setAdapter(mySpendCoinAdapter);
        database.collection("CoinSpend").whereEqualTo("userCoinSpend", ID).addSnapshotListener((value, error) -> {
            assert value != null;
            for (DocumentChange documentChange: value.getDocumentChanges()) {
                String coinSpendID = documentChange.getDocument().getId();
                CoinSpend coinSpend = documentChange.getDocument().toObject(CoinSpend.class).withId(coinSpendID);
                coinSpendList.add(coinSpend);
            }
            mySpendCoinAdapter.notifyDataSetChanged();

        });
        return root;
    }
}