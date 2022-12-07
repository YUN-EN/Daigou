package com.example.testinterface_two.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.R;
import com.example.testinterface_two.models.Deal;
import com.example.testinterface_two.models.DealListListener;
import com.example.testinterface_two.models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class dealListAdapter  extends RecyclerView.Adapter<dealListAdapter.ViewHolder>{
    private final List<Deal> list;
    private final List<User> userList;
    private final Activity activity;
    private final DealListListener listListener;
    private FirebaseFirestore database;

    public dealListAdapter(List<Deal> list, List<User> userList, Activity activity, DealListListener listListener) {
        this.list = list;
        this.userList = userList;
        this.activity = activity;
        this.listListener = listListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_deal_list, parent ,false);
        database = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Deal deal = list.get(position);
        String itemName = deal.dealItemName;
        String name = userList.get(position).getUserName();
        String image = userList.get(position).getUserImage();
        holder.dealListItemName.setText(itemName);
        holder.dealListName.setText(name);
        Glide.with(activity).load(image).into(holder.dealListUserImage);
        String dealId = deal.DealId;
       /* String dealItemId = deal.dealItemID;
        String dealItemName = deal.dealItemName;
        String userID = deal.dealUserId;*/
        holder.extractButton.setOnClickListener(v -> {
            DocumentReference documentReference = database.collection("Deals").document(dealId);
            documentReference.update("isOut" ,"yes").addOnSuccessListener(unused -> {
                list.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(R.string.person_comment)
                        .setPositiveButton(R.string.go_comment, (dialog, which) -> {
                            Dialog personDialog = new Dialog(activity);
                            personDialog.setContentView(R.layout.alert_dialog_comment);
                            personDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            Button commentBtn = personDialog.findViewById(R.id.submitPersonButton);
                            Button cancelBtn = personDialog.findViewById(R.id.cancelPersonButton);
                            RatingBar personRatingBar = personDialog.findViewById(R.id.ratingBar2);
                            EditText commentPersonText = personDialog.findViewById(R.id.editPerson);
                            ProgressBar personProgressBar = personDialog.findViewById(R.id.progressBarPerson);
                            personDialog.show();
                            cancelBtn.setOnClickListener(v1 -> personDialog.dismiss());
                            commentBtn.setOnClickListener(v1 -> {
                                personProgressBar.setVisibility(View.VISIBLE);
                                String com = commentPersonText.getText().toString();
                                String star = String.valueOf(personRatingBar.getRating());
                                String id = deal.sellerId;
                                String buyerId = deal.dealUserId;
                                if (com.equals("")) {
                                    Toast.makeText(activity, "請填寫", Toast.LENGTH_SHORT).show();
                                } else {
                                    HashMap <String ,Object> commentPerson = new HashMap<>();
                                    commentPerson.put("personComment", com);
                                    commentPerson.put("personStar", star);
                                    commentPerson.put("personSellerID", id);
                                    commentPerson.put("personID", buyerId);
                                    database.collection("UserPersonComment").add(commentPerson).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(activity, "評論成功", Toast.LENGTH_SHORT).show();
                                        }
                                        personProgressBar.setVisibility(View.INVISIBLE);
                                        personDialog.dismiss();
                                    });
                                }
                            });
                            /*Intent intent = new Intent(activity , CommentPersonActivity.class);
                            intent.putExtra("dealItemID", dealItemId);
                            intent.putExtra("dealItemName", dealItemName);
                            intent.putExtra("dealUserID", userID);
                            activity.startActivity(intent);
                            activity.finish();*/
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
                builder.create().show();
            });
        });
        database.collection("Deals").document(dealId).addSnapshotListener((value, error) -> {
            assert value != null;
            if (value.exists() && error ==null) {
                String out = value.getString("isOut");
                assert out != null;
                if (out.equals("yes")) {
                    holder.fineImage.setVisibility(View.VISIBLE);
                    holder.show.setVisibility(View.INVISIBLE);
                    holder.extractButton.setVisibility(View.GONE);
                }
            }
        });
        holder.itemView.setOnClickListener(v -> listListener.dealListClick(deal));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dealListItemName , dealListName ,show;
        Button extractButton;
        CircleImageView dealListUserImage;
        ImageView fineImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dealListItemName = itemView.findViewById(R.id.dealListItemName);
            dealListName = itemView.findViewById(R.id.dealBuyerItemName);
            extractButton = itemView.findViewById(R.id.goShopPersonButton);
            dealListUserImage = itemView.findViewById(R.id.dealBuyerImage);
            show = itemView.findViewById(R.id.textShow);
            fineImage = itemView.findViewById(R.id.dImage);
        }
    }
}
