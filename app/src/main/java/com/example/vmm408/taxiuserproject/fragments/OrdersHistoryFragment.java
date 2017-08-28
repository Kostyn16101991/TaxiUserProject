package com.example.vmm408.taxiuserproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vmm408.taxiuserproject.R;
import com.example.vmm408.taxiuserproject.activities.MainActivity;
import com.example.vmm408.taxiuserproject.adapters.EndlessScrollListener;
import com.example.vmm408.taxiuserproject.adapters.RecyclerViewAdapterOrders;
import com.example.vmm408.taxiuserproject.models.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.vmm408.taxiuserproject.utils.FirebaseDataBaseKeys.ORDERS_REF_KEY;
import static com.example.vmm408.taxiuserproject.models.UserModel.SignedUser;
import static com.example.vmm408.taxiuserproject.models.OrderModel.CurrentOrder;

public class OrdersHistoryFragment extends BaseFragment {
    public static OrdersHistoryFragment newInstance() {
        return new OrdersHistoryFragment();
    }

    @BindView(R.id.text_current_order)
    TextView textCurrentOrder;
    @BindView(R.id.current_order_container)
    LinearLayout currentOrderContainer;
    @BindView(R.id.history_order_list)
    RecyclerView historyOrderList;
    private RecyclerViewAdapterOrders recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private String lastItemFromBase = "0";
    private EndlessScrollListener endlessScrollListener = new EndlessScrollListener(linearLayoutManager) {
        @Override
        public void onLoadMore() {
            loadMore();
        }
    };
    private ValueEventListener getArchiveOrders = new DatabaseValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<OrderModel> tempList = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                if (!lastItemFromBase.equals(snapshot.getKey())) {
                    lastItemFromBase = snapshot.getKey();
                    tempList.add(snapshot.getValue(OrderModel.class));
                }
            }
            recyclerViewAdapter.addList(tempList);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (CurrentOrder.getOrderModel() != null) {
            textCurrentOrder.setVisibility(View.VISIBLE);
            currentOrderContainer.addView(super.initOrderView(CurrentOrder.getOrderModel()));
        }
        initRecyclerView();
        loadMore();
    }

    private void initRecyclerView() {
        historyOrderList.setLayoutManager(linearLayoutManager);
        historyOrderList.setAdapter(recyclerViewAdapter = new RecyclerViewAdapterOrders());
        historyOrderList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        historyOrderList.addOnScrollListener(endlessScrollListener);
    }

    private void loadMore() {
        reference = database.getReference(ORDERS_REF_KEY).child(SignedUser.getUserModel().getIdUser());
        Query query = reference.orderByKey().startAt(lastItemFromBase).limitToFirst(5);
        query.addListenerForSingleValueEvent(getArchiveOrders);
    }

    @OnClick(R.id.image_view_btn_back)
    void imViewBtnBack(){
        ((MainActivity)getContext()).changeFragment(MapFragment.newInstance());
    }
}
