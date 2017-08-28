package com.example.vmm408.taxiuserproject.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vmm408.taxiuserproject.utils.MyKeys;
import com.example.vmm408.taxiuserproject.activities.MainActivity;
import com.example.vmm408.taxiuserproject.R;
import com.example.vmm408.taxiuserproject.models.OrderModel;
import com.example.vmm408.taxiuserproject.models.UserModel;
import com.example.vmm408.taxiuserproject.service.FirebaseService;
import com.example.vmm408.taxiuserproject.utils.UserSharedUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.vmm408.taxiuserproject.utils.FirebaseDataBaseKeys.CURRENT_ORDER_REF_KEY;
import static com.example.vmm408.taxiuserproject.utils.FirebaseDataBaseKeys.USERS_REF_KEY;
import static com.example.vmm408.taxiuserproject.models.UserModel.SignedUser;
import static com.example.vmm408.taxiuserproject.models.UserModel.OrderAcceptedDriver;
import static com.example.vmm408.taxiuserproject.models.OrderModel.CurrentOrder;

public class MapFragment extends BaseFragment {
    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @BindView(R.id.search_view_app_bar)
    SearchView searchViewAppBar;
    @BindView(R.id.im_btn_clear_search)
    ImageView imBtnClearSearch;
    @BindView(R.id.im_btn_profile)
    CircleImageView imBtnProfile;
    @BindView(R.id.search_view_container)
    LinearLayout searchViewContainer;
    @BindView(R.id.fab_new_order)
    FloatingActionButton fabNewOrder;
    private FusedLocationProviderClient client;
    private Location currentLocation;
    private GoogleMap map;
    private Geocoder geocoder;
    private SearchView.SearchAutoComplete searchAutoComplete;
    private List<String> searchAddressList = new ArrayList<>();
    private ValueEventListener userInBase = new DatabaseValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            SignedUser.setUserModel(dataSnapshot.getValue(UserModel.class));
        }
    };
    private ValueEventListener currentOrderInBase = new DatabaseValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {
                CurrentOrder.setOrderModel(dataSnapshot.getValue(OrderModel.class));
                getOrderAcceptedUserFromBase();
                getActivity().startService(new Intent(getContext(), FirebaseService.class));
            }
            imBtnProfile.setClickable(true);
            fabNewOrder.setClickable(true);
        }
    };

    private ValueEventListener orderAcceptedDriver = new DatabaseValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            OrderAcceptedDriver.setUserModel(new UserModel());
            OrderAcceptedDriver.setUserModel(dataSnapshot.getValue(UserModel.class));
        }
    };


    private GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            map.clear();
            searchViewAppBar.clearFocus();
        }
    };
    private GoogleMap.OnMarkerClickListener markerClickListener = marker -> false;
    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            searchViewAppBar.clearFocus();
            initMarker(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            imBtnClearSearch.setVisibility(newText.length() > 0 ? View.VISIBLE : View.GONE);
            if (newText.length() > 2) {
                initAutoCompleteList(newText);
            }
            return false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserFromBase();
        initAvatar();
        initMap();
        initAutoComplete();
        if (!checkSelfPermission()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    private void getUserFromBase() {
        if (SignedUser.getUserModel() == null) {
            reference = database.getReference(USERS_REF_KEY)
                    .child(UserSharedUtils.userSignedInApp(getContext()));
            reference.addListenerForSingleValueEvent(userInBase);
        }
        if (CurrentOrder.getOrderModel() == null) {
            reference = database.getReference(CURRENT_ORDER_REF_KEY)
                    .child(UserSharedUtils.userSignedInApp(getContext()));
            reference.addListenerForSingleValueEvent(currentOrderInBase);
        }
    }

    private void getOrderAcceptedUserFromBase() {
        if (!CurrentOrder.getOrderModel().getOrderAcceptedDriver()
                .equals(getResources().getString(R.string.text_status_order_not_accepted))) {
            reference = database.getReference(USERS_REF_KEY)
                    .child(CurrentOrder.getOrderModel().getOrderAcceptedDriver());
            reference.addListenerForSingleValueEvent(orderAcceptedDriver);
        }
    }

    private void initAvatar() {
        String avatar;
        if (SignedUser.getUserModel() == null) {
            imBtnProfile.setImageResource(R.drawable.ic_person_black_24dp);
            return;
        } else {
            avatar = SignedUser.getUserModel().getAvatarUser();
        }
        if (avatar == null) {
            imBtnProfile.setImageResource(R.drawable.ic_person_black_24dp);
            return;
        }
        if (avatar.startsWith("https://lh3")) {
            super.loadPhotoUrl(avatar, imBtnProfile);
        } else {
            byte[] imageBytes = Base64.decode(avatar, Base64.DEFAULT);
            imBtnProfile.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        }
    }

    private void initMap() {
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(googleMap -> {
            this.map = googleMap;
            this.map.setOnMapClickListener(mapClickListener);
            this.map.setOnMapLongClickListener(this::initMarker);
            this.map.setOnMarkerClickListener(markerClickListener);
        });
    }

    private void initAutoComplete() {
        searchAutoComplete = (SearchView.SearchAutoComplete) searchViewAppBar.findViewById(R.id.search_src_text);
        searchAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            searchViewAppBar.setQuery(searchAddressList.get(position), true);
            initMarker(searchAddressList.get(position));
        });
    }

    private void initAutoCompleteList(String newText) {
        if (geocoder == null) {
            geocoder = new Geocoder(getContext(), Locale.getDefault());
        }
        getAddressList(newText).subscribe(addressList -> {
                    searchAddressList.clear();
                    for (int i = 0; i < addressList.size(); i++) {
                        searchAddressList.add(addressList.get(i).getAddressLine(0));
                    }
                    searchAutoComplete.setAdapter(new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_list_item_1, searchAddressList));
                    searchAutoComplete.showDropDown();
                },
                throwable -> Log.e("TAG", throwable + ""));
    }

    // permission is checked before this method. This warning is okay.
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        client = LocationServices.getFusedLocationProviderClient(getContext());
        client.getLastLocation().addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                currentLocation = task.getResult();
            }
        });
    }

    private boolean checkSelfPermission() {
        return ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions((Activity) getContext(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
    }

    @OnClick(R.id.toolbar)
    void toolbar() {
        searchViewContainer.setVisibility(View.VISIBLE);
        searchViewAppBar.setIconified(false);
        searchViewAppBar.setQueryHint(getResources().getString(R.string.search_view_hint));
        searchViewAppBar.setOnQueryTextListener(onQueryTextListener);
    }

    @OnClick(R.id.image_view_btn_back)
    void imBtnBack() {
        searchViewContainer.setVisibility(View.GONE);
        searchViewAppBar.setQuery("", false);
        map.clear();
    }

    @OnClick(R.id.im_btn_clear_search)
    void imBtnClearSearch() {
        imBtnClearSearch.setVisibility(View.GONE);
        searchViewAppBar.setQuery("", false);
    }

    @OnClick(R.id.im_btn_profile)
    void imBtnProfile() {
        new AlertDialog.Builder(getContext()).setItems(R.array.menu_profile, (dialog, which) ->
                ((MainActivity) getContext()).changeFragment(initFragment(which))).show();
    }

    private Fragment initFragment(int which) {
        if (which == MyKeys.ITEM_PROFILE_KEY) {
            return SaveProfileFragment.newInstance(true);
        } else if (which == MyKeys.ITEM_RATING_KEY) {
            return RatingFragment.newInstance();
        } else if (which == MyKeys.ITEM_ORDERS_HISTORY_KEY) {
            return OrdersHistoryFragment.newInstance();
        } else if (which == MyKeys.ITEM_SETTINGS_KEY) {
            return SettingsFragment.newInstance();
        }
        return null;
    }

    @OnClick(R.id.fab_new_order)
    void fabNewOrder() {
        if (CurrentOrder.getOrderModel() == null) {
            super.createNewOrderDialog();
        } else {
            super.showCurrentOrderDialog(CurrentOrder.getOrderModel());
        }
    }

    @OnClick(R.id.fab_my_location)
    void fabMyLocation() {
        getLastLocation();
        try {
            initMarker(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        } catch (Exception e) {
            Toast.makeText(getContext(), getResources().getString(R.string.toast_turn_location), Toast.LENGTH_SHORT).show();
        }
    }

    private void initMarker(Object object) {
        map.clear();
        LatLngBounds.Builder markerBounds = new LatLngBounds.Builder();
        if (geocoder == null) {
            geocoder = new Geocoder(getContext(), Locale.getDefault());
        }
        getAddressList(object).subscribe(addressList -> {
            MarkerOptions markerOptions = null;
            for (int i = 0; i < addressList.size(); i++) {
                markerOptions = new MarkerOptions()
                        .position(new LatLng(addressList.get(i).getLatitude(), addressList.get(i).getLongitude()))
                        .title(addressList.get(i).getAddressLine(0));
                map.addMarker(markerOptions);
                markerBounds.include(markerOptions.getPosition());
            }
            if (addressList.size() == 1) {
                CameraPosition cameraPosition = new CameraPosition(markerOptions.getPosition(), 12f, 0f, 0f);
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                return;
            }
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(markerBounds.build(), 50, 50, 5));
        }, throwable -> Log.e("TAG", throwable + ""));
    }

    private Observable<List<Address>> getAddressList(Object object) {
        return Observable.just(object)
                .map(s -> fillAddressList(object))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<Address> fillAddressList(Object object) {
        List<Address> addressList = new ArrayList<>();
        try {
            if (object instanceof LatLng) {
                LatLng latLng = (LatLng) object;
                addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            } else {
                addressList = geocoder.getFromLocationName((String) object, 10);
            }
        } catch (IOException e) {
            Log.e("TAG", e + "");
        }
        return addressList;
    }
}
