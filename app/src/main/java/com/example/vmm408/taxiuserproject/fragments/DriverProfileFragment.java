package com.example.vmm408.taxiuserproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vmm408.taxiuserproject.R;
import com.example.vmm408.taxiuserproject.activities.MainActivity;
import com.example.vmm408.taxiuserproject.models.UserModel;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.vmm408.taxiuserproject.models.UserModel.OrderAcceptedDriver;

public class DriverProfileFragment extends BaseFragment {
    public static DriverProfileFragment newInstance() {
        return new DriverProfileFragment();
    }

    @BindView(R.id.text_full_name_driver)
    TextView textFullNameDriver;
    @BindView(R.id.text_gender_driver)
    TextView textGenderDriver;
    @BindView(R.id.text_age_driver)
    TextView textAgeDriver;
    @BindView(R.id.text_phone_driver)
    TextView textPhoneDriver;
    @BindView(R.id.text_experience_driver)
    TextView textExperienceDriver;
    @BindView(R.id.text_car_model_driver)
    TextView textCarModelDriver;
    @BindView(R.id.text_num_plate_driver)
    TextView textNumPlateDriver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDataInWidgets();
    }

    private void initDataInWidgets() {
        UserModel model = OrderAcceptedDriver.getUserModel();
        textFullNameDriver.setText(model.getFullNameUser());
        textGenderDriver.setText(model.getGenderUser());
        textAgeDriver.setText(model.getAgeUser());
        textPhoneDriver.setText(model.getPhoneUser());
        textExperienceDriver.setText(String.valueOf(model.getExperienceDriver()) + " (years)");
        textCarModelDriver.setText(model.getCarModelDriver());
        textNumPlateDriver.setText(model.getNumPlateCarDriver());
    }

    @OnClick(R.id.image_view_btn_back)
    void imageViewBtnBack() {
        ((MainActivity) getContext()).changeFragment(MapFragment.newInstance());
    }
}
