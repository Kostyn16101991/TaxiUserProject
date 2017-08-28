package com.example.vmm408.taxiuserproject.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.example.vmm408.taxiuserproject.R;
import com.example.vmm408.taxiuserproject.fragments.DatabaseValueEventListener;
import com.example.vmm408.taxiuserproject.models.UserModel;
import com.example.vmm408.taxiuserproject.receiver.NotificationClickReceiver;
import com.example.vmm408.taxiuserproject.utils.UserSharedUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.vmm408.taxiuserproject.utils.FirebaseDataBaseKeys.CURRENT_ORDER_REF_KEY;
import static com.example.vmm408.taxiuserproject.utils.FirebaseDataBaseKeys.USERS_REF_KEY;
import static com.example.vmm408.taxiuserproject.models.OrderModel.CurrentOrder;
import static com.example.vmm408.taxiuserproject.models.UserModel.OrderAcceptedDriver;

public class FirebaseService extends Service {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseValueEventListener currentOrderValue = new DatabaseValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() == null) {
                stopSelf();
                return;
            }
            if (dataSnapshot.getValue().equals(getResources().getString(R.string.text_status_order_not_accepted))) {
                return;
            }
            CurrentOrder.getOrderModel().setOrderAcceptedDriver(dataSnapshot.getValue().toString());
            loadOrderAcceptedDriver();
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .notify(111, createNotification());

        }
    };
    private DatabaseValueEventListener orderAcceptedDriver = new DatabaseValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            OrderAcceptedDriver.setUserModel(dataSnapshot.getValue(UserModel.class));
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        database = FirebaseDatabase.getInstance();
        initValueListener();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initValueListener() {
        reference = database
                .getReference(CURRENT_ORDER_REF_KEY)
                .child(UserSharedUtils.userSignedInApp(getApplicationContext()))
                .child("orderAcceptedDriver");
        reference.addValueEventListener(currentOrderValue);
    }

    private void loadOrderAcceptedDriver() {
        reference = database.getReference(USERS_REF_KEY)
                .child(CurrentOrder.getOrderModel().getOrderAcceptedDriver());
        reference.addListenerForSingleValueEvent(orderAcceptedDriver);
    }

    private Notification createNotification() {
        Intent intent = new Intent(this, NotificationClickReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 112, intent, Intent.FILL_IN_ACTION);
        return new NotificationCompat.Builder(this)
                .setContentTitle("Taxi Perfect")
                .setContentText("Your order was accepted")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .addAction(new NotificationCompat.Action(0, "OK", pendingIntent))
                .setSmallIcon(R.drawable.ic_chat_black_24dp)
                .build();
    }
}
