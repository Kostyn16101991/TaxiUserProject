package com.example.vmm408.taxiuserproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vmm408.taxiuserproject.AuthenticationActivity;
import com.example.vmm408.taxiuserproject.R;
import com.example.vmm408.taxiuserproject.utils.Utils;

import butterknife.OnClick;

public class SettingsFragment extends BaseFragment {
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btn_sign_out)
    void btnSignOut() {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.alert_title_confirm_sign_out))
                .setPositiveButton(getString(R.string.positive_btn_confirm), (dialog, which) -> signOut())
                .setNegativeButton(getString(R.string.negative_btn_cancel), (dialog, which) -> dialog.dismiss())
                .create().show();
    }

    private void signOut() {
        new Utils().deleteFromShared(getContext());
        startActivity(new Intent(getContext(), AuthenticationActivity.class));
    }
}