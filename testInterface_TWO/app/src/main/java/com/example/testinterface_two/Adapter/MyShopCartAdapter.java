package com.example.testinterface_two.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.R;
import com.example.testinterface_two.models.MyShopCart;
import com.example.testinterface_two.models.MyShopCartListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class MyShopCartAdapter extends RecyclerView.Adapter<MyShopCartAdapter.ViewHolder> {
    private final Activity context;
    private final List<MyShopCart> myShopCartList;
    private FirebaseFirestore firebaseFirestore;
    private final MyShopCartListener myShopCartListener;
    private int number;

    public MyShopCartAdapter(Activity context, List<MyShopCart> myShopCartList , MyShopCartListener myShopCartListener) {
        this.context = context;
        this.myShopCartList = myShopCartList;
        this.myShopCartListener = myShopCartListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_my_shop_cart ,parent ,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyShopCart myShopCart = myShopCartList.get(position);
        holder.setMyShopCartImage(myShopCart.getMyShopCartImage());
        holder.setMyShopCartName(myShopCart.getMyShopCartName());
        holder.setMyShopCartCount(myShopCart.getMyShopCartCount());
        holder.setMyShopCartPrice(myShopCart.getMyShopCartPrice());

        String myShopCartId = myShopCart.MyShopCartId;
        holder.itemView.setOnClickListener(v -> myShopCartListener.MyShopCartClick(myShopCart));
        holder.myShopCartDeleteImage.setOnClickListener(v -> {
            firebaseFirestore.collection("MyShopCart").document(myShopCartId).delete();
            myShopCartList.remove(holder.getAdapterPosition());
            notifyDataSetChanged();
            Toast.makeText(context, "remove successfully", Toast.LENGTH_SHORT).show();
        });

        int p = Integer.parseInt(myShopCart.MyShopCartPrice);

        holder.myShopCartAddImage.setOnClickListener(v -> {
            number++;
            holder.myShopCartCount.setText(String.valueOf(number));
            holder.myShopCartPrice.setText(String.valueOf(number*p));
        });
        holder.myShopCartRemoveImage.setOnClickListener(v -> {
            if (number == 0) {
                Toast.makeText(context, "can not lower zero", Toast.LENGTH_SHORT).show();
            }
            else {
                number--;
                holder.myShopCartCount.setText(String.valueOf(number));
                holder.myShopCartPrice.setText(String.valueOf(number*p));
            }
        });
    }
    @Override
    public int getItemCount() {
        return myShopCartList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView myShopCartImage ,myShopCartDeleteImage ,myShopCartAddImage , myShopCartRemoveImage;
        TextView myShopCartCount , myShopCartPrice  ,myShopCartName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myShopCartDeleteImage = itemView.findViewById(R.id.myShopCartItemDelete);
            myShopCartAddImage = itemView.findViewById(R.id.myShopCartItemAdd);
            myShopCartRemoveImage = itemView.findViewById(R.id.myShopCartItemRemove);
        }
        public void setMyShopCartImage(String image) {
            myShopCartImage = itemView.findViewById(R.id.myShopCartItemImage);
            Glide.with(context.getApplicationContext()).load(image).into(myShopCartImage);
        }
        public void setMyShopCartName(String name) {
            myShopCartName = itemView.findViewById(R.id.myShopCartItemName);
            myShopCartName.setText(name);
        }
        public void setMyShopCartCount(String count) {
            myShopCartCount = itemView.findViewById(R.id.myShopCartItemCount);
            myShopCartCount.setText(count);
        }
        public void setMyShopCartPrice(String price) {
            myShopCartPrice = itemView.findViewById(R.id.myShopCartItemMoney);
            myShopCartPrice.setText(price);
        }
    }
}
