package com.example.testinterface_two.Adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.R;
import com.example.testinterface_two.models.DealComment;
import com.example.testinterface_two.models.DealCommentListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;


public class DealCommentAdapter extends RecyclerView.Adapter<DealCommentAdapter.ViewHolder> {
    private final Activity context;
    private final List<DealComment>  list;
    private final DealCommentListener dealCommentListener;
    private FirebaseFirestore database;

    public DealCommentAdapter(Activity context, List<DealComment> list,DealCommentListener dealCommentListener) {
        this.context = context;
        this.list = list;
        this.dealCommentListener = dealCommentListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_deal_comment ,parent , false);
        database = FirebaseFirestore.getInstance();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DealComment dealComment = list.get(position);
        holder.setRatingBar(dealComment.getStar());
        holder.setDealCommentImage(dealComment.getCommentImage());
        holder.setDealCommentDateText(dealComment.getCommentDate());
        holder.setRatingBar(dealComment.getStar());
        holder.setCommentText(dealComment.getCommentText());
        holder.setDealCommentItemName(dealComment.getCommentItemName());
        String id = list.get(position).getCommentItemID();
        database.collection("Item").document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                holder.dealCommentID.setText(id);
                String image = Objects.requireNonNull(task.getResult()).getString("itemImage");
                Glide.with(context).load(image).into(holder.commentDealImage);
            }

        });
        holder.itemView.setOnClickListener(v -> dealCommentListener.clickDealComment(dealComment));
        boolean visible = list.get(position).isVisible();
        holder.expandLayout.setVisibility(visible ? View.VISIBLE :View.GONE);
        if (visible) {
            holder.downImage.setImageResource(R.drawable.ic_keyboard_control_key);
        } else {
            holder.downImage.setImageResource(R.drawable.ic_keyboard_arrow_down);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView dealCommentImage ,downImage ,commentDealImage;
        ConstraintLayout expandLayout;
        TextView dealCommentItemName , dealCommentDateText ,dealCommentStarNum ,dealComment,dealCommentID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            downImage = itemView.findViewById(R.id.dealCommentDownImage);
            expandLayout = itemView.findViewById(R.id.expandableLayout);
            dealCommentID = itemView.findViewById(R.id.dealCommentID);
            commentDealImage = itemView.findViewById(R.id.commentDealImage);
            downImage.setOnClickListener(v -> {
                DealComment dealComment = list.get(getAdapterPosition());
                dealComment.setVisible(!dealComment.isVisible());
                notifyItemChanged(getAdapterPosition());
            });
        }
        public void setDealCommentImage(String Image) {
            dealCommentImage = itemView.findViewById(R.id.dealCommentImage);
            Glide.with(context).load(Image).into(dealCommentImage);
        }
        public void setDealCommentItemName(String name) {
            dealCommentItemName = itemView.findViewById(R.id.dealCommentItemName);
            dealCommentItemName.setText(name);
        }
        public void setCommentText(String comment) {
            dealComment =itemView.findViewById(R.id.deTailComment);
            dealComment.setText(comment);
        }

        public void setDealCommentDateText(String date) {
            dealCommentDateText = itemView.findViewById(R.id.dealCommentDateText);
            dealCommentDateText.setText(date);
        }
        public void setRatingBar(String star) {
           dealCommentStarNum = itemView.findViewById(R.id.starNumber);
           dealCommentStarNum.setText(star);
        }

    }
}
