package com.example.vmm408.taxiuserproject.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.vmm408.taxiuserproject.AuthenticationActivity;
import com.example.vmm408.taxiuserproject.MainActivity;
import com.example.vmm408.taxiuserproject.R;
import com.example.vmm408.taxiuserproject.models.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment {
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @BindView(R.id.edit_text_login)
    EditText etLogin;
    @BindView(R.id.edit_text_password)
    EditText etPassword;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.text_link_forget_password)
    public void tvLinkForgetPassword() {
        // write code here
    }

    @OnClick(R.id.text_link_sign_up)
    public void tvLinkSignUp() {
        ((AuthenticationActivity) getActivity()).changeFragment(SignUpFragment.newInstance());
    }

    @OnClick(R.id.btn_login)
    public void btnLogin() {
        if (super.validate(etLogin) && super.validate(etPassword)) {
            initProgressDialog();
            getUserFromBase();
        }
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }

    private void getUserFromBase() {
        mReference = mDatabase.getReference("users");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (UserExistInBase(snapshot)) {
                        saveUser(snapshot);
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    } else {
                        makeToast("invalid email / password");
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private boolean UserExistInBase(DataSnapshot dataSnapshot) {
        return etLogin.getText().toString().equals(gson.fromJson(
                dataSnapshot.getValue().toString(), UserModel.class).getEmailUser()) &&
                etLogin.getText().toString().equals(gson.fromJson(
                        dataSnapshot.getValue().toString(), UserModel.class).getPhoneUser()) &&
                etPassword.getText().toString().equals(gson.fromJson(
                        dataSnapshot.getValue().toString(), UserModel.class).getPasswordUser());
    }

    private void saveUser(DataSnapshot dataSnapshot) {
        UserModel.User.setUserModel(gson.fromJson(
                dataSnapshot.getValue().toString(), UserModel.class));
        UserModel.User.getUserModel().setIdUser(Integer.parseInt(dataSnapshot.getKey()));
    }
}
