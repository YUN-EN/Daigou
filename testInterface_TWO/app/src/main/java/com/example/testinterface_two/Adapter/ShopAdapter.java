package com.example.testinterface_two.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.R;
import com.example.testinterface_two.models.Shop;
import com.example.testinterface_two.models.ShopListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder>{
    private final Activity context;
    private List<Shop> list;
    private final ShopListener shopListener;

    public ShopAdapter(Activity context, List<Shop> list, ShopListener shopListener) {
        this.context = context;
        this.list = list;
        this.shopListener = shopListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.each_shopcart ,parent ,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shop shop = list.get(position);
        holder.setItemImage(shop.getItemImage());
        holder.setItemPrice(shop.getItemPrice());
        Calendar calendar = Calendar.getInstance();
        String date = DateFormat.getTimeInstance().format(calendar.getTime());
        holder.setItemDate(date);
        holder.setItemThing(shop.getItemThing());
        holder.itemView.setOnClickListener(v -> shopListener.onShopClicked(shop));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       ImageView ItemImage;
       TextView ItemPrice ,ItemName ,ItemDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItemImage(String image) {
            ItemImage = itemView.findViewById(R.id.myFavoritePostImage);
            Glide.with(context).load(image).into(ItemImage);
        }
        public void setItemPrice(String price) {
            ItemPrice = itemView.findViewById(R.id.shopPrice);
            ItemPrice.setText(price);
        }
        public void setItemThing(String thing) {
            ItemName = itemView.findViewById(R.id.myFavoritePostTitle);
            ItemName.setText(thing);
        }
        public void setItemDate(String date) {
            ItemDate = itemView.findViewById(R.id.shopDate);
            ItemDate.setText(date);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Shop> filterList) {
        list = filterList;
        notifyDataSetChanged();
    }
}
