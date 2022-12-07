package com.example.testinterface_two;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.Adapter.ShopAdapter;
import com.example.testinterface_two.models.Shop;
import com.example.testinterface_two.models.ShopListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PersonFragment extends Fragment  implements ShopListener {
    private String UserId;
    private List<Shop> shopList;
    private ShopAdapter shopAdapter;
    private ListenerRegistration listenerRegistration;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        UserId = user.getUid();
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        ImageView userImage = view.findViewById(R.id.sellerImage);
        TextView userMailText = view.findViewById(R.id.sellerUserMail);
        TextView userNameText = view.findViewById(R.id.sellerUserName);
        Button updateButton = view.findViewById(R.id.editMyButton);
        TextView stateText = view.findViewById(R.id.sellerUserDetail);
        ImageView goAssignment = view.findViewById(R.id.goAssignment);
        updateButton.setOnClickListener(v -> startActivity(new Intent(getContext() ,MyUpdateActivity.class)));
        fireStore.collection("Users").document(UserId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String position = task.getResult().getString("userSeller");
                assert position != null;
                if (position.equals("seller")) {
                    goAssignment.setVisibility(View.VISIBLE);
                    goAssignment.setOnClickListener(v -> startActivity(new Intent(getActivity() ,listActivity.class)));
                } else {
                    goAssignment.setVisibility(View.INVISIBLE);
                }
            }
        });
        RecyclerView recyclerViewMyItemPost = view.findViewById(R.id.recyclerViewSellerItemPost);
        recyclerViewMyItemPost.setHasFixedSize(true);
        recyclerViewMyItemPost.setLayoutManager(new GridLayoutManager(getContext() ,2));
        shopList = new ArrayList<>();
        shopAdapter = new ShopAdapter((Activity) getContext(), shopList , this);
        recyclerViewMyItemPost.setAdapter(shopAdapter);
        recyclerViewMyItemPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    String ItemUserID = documentChange.getDocument().getString("userID");
                    if (UserId.equals(ItemUserID)) {
                        Shop shop = documentChange.getDocument().toObject(Shop.class).withId(ItemID);
                        shopList.add(shop);
                        shopAdapter.notifyDataSetChanged();
                    }
                }
            }
            listenerRegistration.remove();
        });
        getData(userImage, userNameText, userMailText ,stateText);
        ImageView moreButton = view.findViewById(R.id.goAllDetail);
        moreButton.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.bottom_sheet_layout_my_item);
            LinearLayout dealLinearLayout = dialog.findViewById(R.id.layoutDeal);
            LinearLayout favoriteLinearLayout = dialog.findViewById(R.id.layoutFavorite);
            LinearLayout myDogCoinLinearLayout = dialog.findViewById(R.id.layoutMyDogCoin);
            LinearLayout ConvertEthLinearLayout = dialog.findViewById(R.id.layoutConvertEth);
            LinearLayout commentLinearLayout = dialog.findViewById(R.id.layoutComment);
            LinearLayout myDealLinearLayout = dialog.findViewById(R.id.layoutMyDeal);
            LinearLayout logOutLinearLayout = dialog.findViewById(R.id.layoutLogOut);
            LinearLayout onLineItemLinearLayout = dialog.findViewById(R.id.layoutOnLineItem);
            TextView coinNum = dialog.findViewById(R.id.dogCoinNum);
            fireStore.collection("Users").document(UserId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String num = task.getResult().getString("userCoin");
                    coinNum.setText(num);
                }
            });
            dealLinearLayout.setOnClickListener(v1 -> {
                startActivity(new Intent(getActivity() , ProgressActivity.class));
                dialog.dismiss();
            });
            favoriteLinearLayout.setOnClickListener(v12 -> {
                startActivity(new Intent(getActivity() , MyFavoritePostActivity.class));
                dialog.dismiss();
            });
            myDogCoinLinearLayout.setOnClickListener(v13 -> {
                startActivity(new Intent(getActivity() , recordActivity.class));
                dialog.dismiss();
            });
            ConvertEthLinearLayout.setOnClickListener(v18 -> {
                startActivity(new Intent(getActivity() ,ConvertActivity.class));
                dialog.dismiss();
            });
            commentLinearLayout.setOnClickListener(v14 -> {
                startActivity(new Intent(getActivity() , MyDealCommentActivity.class));
                dialog.dismiss();
            });
            myDealLinearLayout.setOnClickListener(v15 -> {
                startActivity(new Intent(getActivity() ,ClearDealActivity.class));
                dialog.dismiss();
            });
            onLineItemLinearLayout.setOnClickListener(v16 -> {
                startActivity(new Intent(getActivity() , AddItemActivity.class));
                dialog.dismiss();
            });
            logOutLinearLayout.setOnClickListener(v17 -> {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                DocumentReference documentReference =database.collection("Users").document(UserId);
                HashMap<String , Object> update = new HashMap<>();
                update.put("Token" , "no");
                documentReference.update(update)
                        .addOnSuccessListener(unused -> startActivity(new Intent(getActivity(), MainActivity.class))) .addOnFailureListener(e -> Toast.makeText(getActivity() , "Unable to signOut" , Toast.LENGTH_SHORT).show());
            });
            if (!((Activity) requireContext()).isFinishing()) {
                dialog.show();
            }
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT ,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationMore;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        });
        return view;
    }
    private void getData(ImageView imageView , TextView textView ,TextView text , TextView userState) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("Users").document(UserId);
        documentReference.get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).exists()) {
                String name = task.getResult().getString("userName");
                String mail = task.getResult().getString("userMail");
                String image = task.getResult().getString("userImage");
                String state = task.getResult().getString("userState");
                textView.setText(name);
                text.setText(mail);
                userState.setText(state);
                if (getActivity() != null) {
                    Glide.with(getActivity()).load(image).into(imageView);
                }
            }
        });
    }


    @Override
    public void onShopClicked(Shop shop) {
            Intent intent = new Intent(getActivity() ,MyShopItemActivity.class);
            intent.putExtra("myShopItem", shop);
            intent.putExtra("myShopID", shop.ItemID);
            startActivity(intent);
    }
}