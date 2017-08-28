package com.example.vmm408.taxiuserproject.adapters;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vmm408.taxiuserproject.R;
import com.example.vmm408.taxiuserproject.activities.MainActivity;
import com.example.vmm408.taxiuserproject.models.OrderModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapterOrders extends
        RecyclerView.Adapter<RecyclerViewAdapterOrders.CustomViewHolder> {
    private List<OrderModel> orderModelList = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy", Locale.getDefault());
    Calendar calendar = Calendar.getInstance();

    public void addList(List<OrderModel> orderModelList) {
        this.orderModelList.addAll(orderModelList);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_order, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.textFromOrder.setText(orderModelList.get(position).getFromOrder());
        holder.textDestinationOrder.setText(orderModelList.get(position).getDestinationOrder());
        holder.itemTextTime.setText(formatTime(position));
    }

    private String formatTime(int position) {
        calendar.setTimeInMillis(Long.parseLong(orderModelList.get(position).getTimeOrder()) * 1000);
        return String.valueOf(dateFormat.format(calendar.getTime()));
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_from_order)
        TextView textFromOrder;
        @BindView(R.id.text_destination_order)
        TextView textDestinationOrder;
        @BindView(R.id.item_text_time)
        TextView itemTextTime;
        @BindView(R.id.item_btn_more)
        TextView itemBtnMore;

        CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                Log.d("TAG", getAdapterPosition() + "");
                new AlertDialog.Builder(itemView.getContext())
                        .setView(initOrderView(itemView, orderModelList.get(getAdapterPosition())))
                        .setNeutralButton(itemView.getResources().getString(R.string.btn_back_dialog), (dialog, which) -> dialog.dismiss())
                        .show();
            });
        }

        private View initOrderView(View itemView, OrderModel model) {
            View currentOrderView = ((MainActivity) itemView.getContext())
                    .getLayoutInflater().inflate(R.layout.item_current_order, null);
            TextView status = ButterKnife.findById(currentOrderView, R.id.text_status_order);
            status.setVisibility(View.GONE);
            TextView from = ButterKnife.findById(currentOrderView, R.id.text_from_order);
            from.setText(model.getFromOrder());
            TextView destination = ButterKnife.findById(currentOrderView, R.id.text_destination_order);
            destination.setText(model.getDestinationOrder());
            TextView price = ButterKnife.findById(currentOrderView, R.id.text_price_order);
            price.setText(model.getPriceOrder());
            TextView comment = ButterKnife.findById(currentOrderView, R.id.text_comment_order);
            comment.setText(model.getCommentOrder());
            return currentOrderView;
        }
    }
}
