package com.example.vmm408.taxiuserproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vmm408.taxiuserproject.R;
import com.example.vmm408.taxiuserproject.activities.MainActivity;

import butterknife.OnClick;

public class RatingFragment extends BaseFragment {
    public static RatingFragment newInstance() {
        return new RatingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rating, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.image_view_btn_back)
    void imViewBtnBack() {
        ((MainActivity) getContext()).changeFragment(MapFragment.newInstance());
    }
}
