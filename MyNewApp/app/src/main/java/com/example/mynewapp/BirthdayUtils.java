package com.example.mynewapp;

import android.content.Context;
import android.content.SharedPreferences;

public class BirthdayUtils {

    private static final String PREFS_NAME = "BirthdayPrefs";
    private static final String KEY_BIRTHDAY = "birthday";

    public static boolean isBirthdaySet(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.contains(KEY_BIRTHDAY);
    }

    public static String getBirthday(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_BIRTHDAY, null);
    }

    public static void setBirthday(Context context, String birthday) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_BIRTHDAY, birthday);
        editor.apply();
    }

    public static boolean isValidBirthday(String birthday) {
        // 날짜 형식을 검증하는 로직을 추가하세요
        // 예시: "YYYYMMDD" 형식의 유효성 검사
        if (birthday == null || birthday.length() != 8) {
            return false;
        }
        try {
            int year = Integer.parseInt(birthday.substring(0, 4));
            int month = Integer.parseInt(birthday.substring(4, 6));
            int day = Integer.parseInt(birthday.substring(6, 8));
            if (month < 1 || month > 12 || day < 1 || day > 31) {
                return false;
            }
            // 추가적인 유효성 검사 로직 (예: 윤년 검사 등)
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
