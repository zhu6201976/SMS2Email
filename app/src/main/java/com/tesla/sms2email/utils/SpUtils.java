package com.tesla.sms2email.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tesla.sms2email.global.Constants;

public class SpUtils {
    private SharedPreferences sp;

    public SpUtils(Context context) {
        this.sp = context.getSharedPreferences(Constants.SharedPreferencesName,
                Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        this.sp.edit().putString(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        this.sp.edit().putBoolean(key, value).apply();
    }

    public String getString(String key, String defValue) {
        return this.sp.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return this.sp.getBoolean(key, defValue);
    }

}
