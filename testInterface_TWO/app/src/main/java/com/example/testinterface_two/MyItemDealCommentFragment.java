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
import com.example.testinterface_two.Adapter.DealCommentAdapter;
import com.example.testinterface_two.models.DealComment;
import com.example.testinterface_two.models.DealCommentListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class MyItemDealCommentFragment extends Fragment implements DealCommentListener {
    private List<DealComment> dealCommentList;
    private DealCommentAdapter dealCommentAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=  inflater.inflate(R.layout.fragment_my_item_deal_comment, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        RecyclerView recyclerView = view.findViewById(R.id.myDealCommentItemRecyclerView);
        assert user != null;
        String ID = user.getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dealCommentList = new ArrayList<>();
        dealCommentAdapter = new DealCommentAdapter((Activity) getContext() ,dealCommentList ,this);
        recyclerView.setAdapter(dealCommentAdapter);
        firebaseFirestore.collection("ItemComment").whereEqualTo("user" , ID).addSnapshotListener((value, error) -> {
            assert value != null;
            for (DocumentChange documentChange: value.getDocumentChanges()) {
                String commentID = documentChange.getDocument().getId();
                DealComment dealComment = documentChange.getDocument().toObject(DealComment.class).withId(commentID);
                dealCommentList.add(dealComment);
            }
            dealCommentAdapter.notifyDataSetChanged();
        });
        return view;
    }

    @Override
    public void clickDealComment(DealComment dealComment) {

    }
}