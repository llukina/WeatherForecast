package com.example.android.weatherforecast.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Pref {
    SharedPreferences preferences;

    public Pref(Activity activity) {
        preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public void setLocation(String location) {
        preferences.edit().putString("location", location).apply();
    }

    public String getLocation() {
        return preferences.getString("location", "Zagreb Croatia");
    }
}
