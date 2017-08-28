package com.example.vmm408.taxiuserproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vmm408.taxiuserproject.activities.MainActivity;
import com.example.vmm408.taxiuserproject.R;
import com.example.vmm408.taxiuserproject.utils.UserSharedUtils;

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
                .setMessage(getResources().getString(R.string.text_confirm_sign_out))
                .setPositiveButton(getResources().getString(R.string.btn_confirm_dialog), (dialog, which) -> signOut())
                .setNegativeButton(getResources().getString(R.string.btn_cancel_dialog), (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void signOut() {
        UserSharedUtils.deleteUserFromShared(getContext());
        ((MainActivity) getContext()).changeFragment(SignInFragment.newInstance());
    }
}
