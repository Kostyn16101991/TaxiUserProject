package com.example.vmm408.taxiuserproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSharedUtils {
    private static final String SHARED_KEY = "UserProfile";
    private static final String SHARED_STRING_USER_ID_KEY = "userId";

    public static String userSignedInApp(Context context) {
        return context.getSharedPreferences(SHARED_KEY, Context.MODE_APPEND)
                .getString(SHARED_STRING_USER_ID_KEY, null);
    }

    public static void saveUserToShared(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_KEY, Context.MODE_APPEND);
        preferences.edit().putString(SHARED_STRING_USER_ID_KEY, userId).apply();
    }

    public static void deleteUserFromShared(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_KEY, Context.MODE_APPEND);
        preferences.edit().remove(SHARED_STRING_USER_ID_KEY).apply();
    }
}
