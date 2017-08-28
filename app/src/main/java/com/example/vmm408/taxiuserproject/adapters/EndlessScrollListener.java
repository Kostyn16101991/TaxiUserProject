package com.example.vmm408.taxiuserproject.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private RecyclerView.LayoutManager layoutManager;

    protected EndlessScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();
        if (totalItemCount < previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }
        if (!loading && (lastVisibleItemPosition + 5) > totalItemCount) {
            onLoadMore();
            loading = true;
        }
    }

    public abstract void onLoadMore();
}
