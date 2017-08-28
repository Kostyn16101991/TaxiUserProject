package com.example.vmm408.taxiuserproject.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vmm408.taxiuserproject.R;
import com.example.vmm408.taxiuserproject.models.RatingModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapterRating extends
        RecyclerView.Adapter<RecyclerViewAdapterRating.CustomViewHolder> {
    private List<RatingModel> ratingModelList = new ArrayList<>();

    public void addList(List<RatingModel> ratingModelList) {
        this.ratingModelList.addAll(ratingModelList);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_with_avatar, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.itemImageAvatar.setImageResource(R.drawable.ic_person_black_24dp);
        holder.itemTextTitle.setText(ratingModelList.get(position).getIdUserRating());
        holder.itemTextComment.setText(ratingModelList.get(position).getCommentsRating());
    }

    @Override
    public int getItemCount() {
        return ratingModelList.size();
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_image_avatar)
        ImageView itemImageAvatar;
        @BindView(R.id.item_text_title)
        TextView itemTextTitle;
        @BindView(R.id.item_text_comment)
        TextView itemTextComment;

        CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
