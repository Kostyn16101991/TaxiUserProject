package com.example.vmm408.taxiuserproject.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vmm408.taxiuserproject.R;
import com.example.vmm408.taxiuserproject.activities.MainActivity;
import com.example.vmm408.taxiuserproject.models.OrderModel;
import com.example.vmm408.taxiuserproject.models.UserModel;
import com.example.vmm408.taxiuserproject.service.FirebaseService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.vmm408.taxiuserproject.utils.FirebaseDataBaseKeys.CURRENT_ORDER_REF_KEY;
import static com.example.vmm408.taxiuserproject.utils.FirebaseDataBaseKeys.ORDERS_REF_KEY;
import static com.example.vmm408.taxiuserproject.models.OrderModel.CurrentOrder;
import static com.example.vmm408.taxiuserproject.models.UserModel.OrderAcceptedDriver;

public class BaseFragment extends Fragment {
    private Unbinder unbinder;
    public FirebaseDatabase database;
    public DatabaseReference reference;
    protected ProgressDialog progressDialog;
    protected GoogleApiClient googleApiClient;
    private EditText etOrderFromDialog;
    private EditText etOrderDestinationDialog;
    private EditText etOrderPriceDialog;
    private EditText etOrderCommentDialog;
    private AlertDialog alertDialog;
    private DialogInterface.OnShowListener showListener = new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface dialog) {
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                findWidgetsInView((AlertDialog) dialog);
                if (validate(etOrderFromDialog) &&
                        validate(etOrderDestinationDialog) &&
                        validate(etOrderPriceDialog)) {
                    saveNewOrder((AlertDialog) dialog);
                }
            });
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    protected void loadPhotoUrl(String url, CircleImageView avatar) {
        Glide.with(getContext()).load(url).into(avatar);
    }

    protected void makeToast(String string) {
        Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show();
    }

    protected void createNewOrderDialog() {
        alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.title_new_order))
                .setView(getActivity().getLayoutInflater().inflate(R.layout.item_create_order, null))
                .setCancelable(false)
                .setPositiveButton(getContext().getResources().getString(R.string.btn_create_dialog), null)
                .setNeutralButton(getContext().getResources().getString(R.string.btn_cancel_dialog), (dialog1, which) -> dialog1.dismiss())
                .create();
        alertDialog.setOnShowListener(showListener);
        alertDialog.show();
    }

    private void findWidgetsInView(AlertDialog dialog) {
        etOrderFromDialog = ButterKnife.findById(dialog, R.id.edit_text_order_from);
        etOrderDestinationDialog = ButterKnife.findById(dialog, R.id.edit_text_order_destination);
        etOrderPriceDialog = ButterKnife.findById(dialog, R.id.edit_text_order_price);
        etOrderCommentDialog = ButterKnife.findById(dialog, R.id.edit_text_order_comment);
    }

    private void saveNewOrder(AlertDialog dialog) {
        OrderModel model = createOrderModel();
        reference = database.getReference(CURRENT_ORDER_REF_KEY);
        reference.child(UserModel.SignedUser.getUserModel().getIdUser()).setValue(model);
        dialog.dismiss();
        getActivity().startService(new Intent(getContext(), FirebaseService.class));
    }

    private OrderModel createOrderModel() {
        OrderModel model = new OrderModel();
        model.setIdUserOrder(UserModel.SignedUser.getUserModel().getIdUser());
        model.setFromOrder(etOrderFromDialog.getText().toString());
        model.setDestinationOrder(etOrderDestinationDialog.getText().toString());
        model.setPriceOrder(etOrderPriceDialog.getText().toString());
        model.setCommentOrder(etOrderCommentDialog.getText().toString());
        model.setTimeOrder(String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000));
        model.setOrderAcceptedDriver(getResources().getString(R.string.text_status_order_not_accepted));
        CurrentOrder.setOrderModel(model);
        return model;
    }

    public void showCurrentOrderDialog(OrderModel model) {
        alertDialog = new AlertDialog.Builder(getContext())
                .setView(initOrderView(model))
                .setPositiveButton(getResources().getString(R.string.btn_edit_dialog), (dialog, which) ->
                        createNewOrderDialog())
                .setNegativeButton(getResources().getString(R.string.btn_delete_dialog), (dialog, which) -> deleteOrder(model))
                .setNeutralButton(getResources().getString(R.string.btn_back_dialog), (dialog, which) -> dialog.dismiss())
                .show();
    }

    protected View initOrderView(OrderModel model) {
        View currentOrderView = getActivity().getLayoutInflater().inflate(R.layout.item_current_order, null);
        TextView status = ButterKnife.findById(currentOrderView, R.id.text_status_order);
        checkOrderAccepted(model, status);
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

    private void checkOrderAccepted(OrderModel model, TextView status) {
        if (model.getOrderAcceptedDriver().equals(getResources().getString(R.string.text_status_order_not_accepted))) {
            status.setText(getResources().getString(R.string.text_status_order_not_accepted));
        } else {
            status.setText("Accepted by: " + OrderAcceptedDriver.getUserModel().getFullNameUser());
            status.setOnClickListener(v -> {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                ((MainActivity) getContext()).changeFragment(DriverProfileFragment.newInstance());
            });
        }
    }


    private void deleteOrder(OrderModel model) {
        String message;
        if (model.getOrderAcceptedDriver().equals(getResources()
                .getString(R.string.text_status_order_not_accepted))) {
            message = getResources().getString(R.string.text_confirm_delete);
        } else {
            message = getResources().getString(R.string.text_confirm_delete_order);
        }
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.btn_delete_dialog), (dialog, which) ->
                        moveOrderToArchive())
                .setNeutralButton(getResources().getString(R.string.btn_cancel_dialog), (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void moveOrderToArchive() {
        reference = database.getReference(ORDERS_REF_KEY)
                .child(UserModel.SignedUser.getUserModel().getIdUser())
                .child(CurrentOrder.getOrderModel().getTimeOrder());
        reference.setValue(CurrentOrder.getOrderModel());
        reference = database.getReference(CURRENT_ORDER_REF_KEY);
        reference.removeValue();
        CurrentOrder.setOrderModel(null);
    }

    public boolean validate(EditText editText) {
        String field = editText.getText().toString();
        if (field.isEmpty() || field.trim().length() == 0) {
            editText.setError(getResources().getString(R.string.text_error_empty_fields));
            return false;
        }
        return true;
    }
}
