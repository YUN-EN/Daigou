package com.example.testinterface_two.Adapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.CommentDealActivity;
import com.example.testinterface_two.R;
import com.example.testinterface_two.models.Deal;
import com.example.testinterface_two.models.DealListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.ViewHolder>{
    private final List<Deal> dealList;
    private final Activity context;
    private final DealListener dealListener;
    private FirebaseFirestore firebaseFirestore;
    public DealAdapter(List<Deal> dealList, Activity context , DealListener dealListener) {
        this.dealList = dealList;
        this.context = context;
        this.dealListener = dealListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.each_deal_progress ,parent ,false);
        firebaseFirestore  = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Deal deal = dealList.get(position);
        holder.setDealImage(deal.getDealItemImage());
        holder.setDealItemName(deal.getDealItemName());
        holder.setDealDateText(deal.getDealDate());
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = dateFormat.format(calendar.getTime());
        String dealId = deal.DealId;
        String dealCart = deal.dealCartID;
        String dealItemId = deal.dealItemID;
        String dealItemName = deal.dealItemName;
        holder.itemView.setOnClickListener(v -> dealListener.DealClick(deal));
        holder.checkButton.setOnClickListener(v -> {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            DocumentReference documentReference = database.collection("Deals").document(dealId);
            documentReference.update("isReceive" , true ,"dealFinishDate" ,date).addOnSuccessListener(unused -> {
                dealList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                firebaseFirestore.collection("MyShopCart").document(dealCart).delete();
                Toast.makeText(context, "deal finish", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.deal_comment)
                        .setPositiveButton(R.string.go_comment, (dialog, which) -> {
                            Intent intent = new Intent(context , CommentDealActivity.class);
                            intent.putExtra("dealItemID", dealItemId);
                            intent.putExtra("dealItemName", dealItemName);
                            intent.putExtra("deal" ,deal);
                            context.startActivity(intent);
                            context.finish();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
                builder.create().show();
            });
        });
        firebaseFirestore.collection("Deals").document(dealId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String no  = task.getResult().getString("isOut");
                if (Objects.equals(no, "no")) {
                    holder.dealShowText.setText(R.string.ing);
                }
            }
        });
        firebaseFirestore.collection("Deals").document(dealId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String yes = task.getResult().getString("isOut");
                if (Objects.equals(yes, "yes")) {
                    holder.dealShowText.setText(R.string.getting);
                }
            }
        });
        firebaseFirestore.collection("Deals").document(dealId).addSnapshotListener((value, error) -> {
            assert value != null;
            if (value.exists() && error ==null) {
                boolean ck = value.getBoolean("isReceive");
                if (ck) {
                    holder.okImage.setVisibility(View.VISIBLE);
                    holder.checkButton.setVisibility(View.GONE);
                    holder.dealShowText.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button checkButton;
        ImageView dealImage ,okImage;
        TextView dealDateText , dealItemName ,dealShowText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkButton = itemView.findViewById(R.id. getDealItemButton);
            okImage = itemView.findViewById(R.id.finishImage);
            dealShowText = itemView.findViewById(R.id.textShow);
        }
        public void setDealImage(String image) {
            dealImage = itemView.findViewById(R.id.dealCommentImage);
            Glide.with(context.getApplicationContext()).load(image).into(dealImage);
        }
        public void setDealDateText(String date) {
            dealDateText = itemView.findViewById(R.id.dealCommentDateText);
            dealDateText.setText(date);
        }
        public void setDealItemName(String name) {
            dealItemName = itemView.findViewById(R.id.dealCommentItemName);
            dealItemName.setText(name);
        }
    }
}
