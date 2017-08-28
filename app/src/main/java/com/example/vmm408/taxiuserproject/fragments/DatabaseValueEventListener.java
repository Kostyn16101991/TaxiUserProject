package com.example.vmm408.taxiuserproject.fragments;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public abstract class DatabaseValueEventListener implements ValueEventListener {
    public abstract void onDataChange(DataSnapshot dataSnapshot);

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.e("ERROR", databaseError + "");
    }
}
