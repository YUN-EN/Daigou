package com.example.testinterface_two;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.testinterface_two.Adapter.ShopAdapter;
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
import java.util.Objects;

public class HomeFragment extends Fragment implements ShopListener {
    private List<Shop> shopList;
    private ShopAdapter shopAdapter;
    private ListenerRegistration listenerRegistration;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        ImageView messageImage = view.findViewById(R.id.goMessage);
        ImageView myShopCartImage = view.findViewById(R.id.goBackPerson);
        SearchView searchView = view.findViewById(R.id.searchView);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewShopCart);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext() ,2));
        shopList = new ArrayList<>();
        shopAdapter = new ShopAdapter((Activity) getContext(), shopList , this);


        messageImage.setOnClickListener(v -> startActivity(new Intent(getActivity() ,ConversationActivity.class)));
        myShopCartImage.setOnClickListener(v -> startActivity(new Intent(getActivity(), MyShopCartActivity.class)));
        recyclerView.setAdapter(shopAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        if (firebaseUser != null) {
           recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    boolean isBottom = !recyclerView.canScrollVertically(1);
                    if(isBottom) {
                        Toast.makeText(getContext() ,"Reach bottom" ,Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Query query = fireStore.collection("Item").orderBy("itemDate", Query.Direction.DESCENDING);
            listenerRegistration = query.addSnapshotListener((value, error) -> {
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
        return view;
    }

    private void filter(String newText) {

        List<Shop> list  = new ArrayList<>();
        for (Shop item:shopList) {
            if (item.getItemThing().toLowerCase().contains(newText.toLowerCase())) {
                list.add(item);
            }
        }
        shopAdapter.filterList(list);
    }

    @Override
    public void onShopClicked(Shop shop) {
        Intent intent = new Intent(getActivity() ,AllShopInformationActivity.class);
        intent.putExtra("shop" ,  shop);
        intent.putExtra("shopID", shop.ItemID);
        startActivity(intent);
    }
}