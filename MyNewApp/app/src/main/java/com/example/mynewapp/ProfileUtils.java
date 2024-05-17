package com.example.mynewapp;

import android.content.Context;
import android.content.SharedPreferences;

public class ProfileUtils {
    private static final String PREFS_NAME = "profile_prefs";
    private static final String PROFILE_IMAGE_KEY = "profile_image";

    public static void setProfileImageUri(Context context, String uri) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROFILE_IMAGE_KEY, uri);
        editor.apply();
    }

    public static String getProfileImageUri(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PROFILE_IMAGE_KEY, null);
    }
}
