package com.example.calmicat;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReflectionPreferences {
    private static final String PREF_NAME = "ReflectionPrefs";
    private static final String KEY_REFLECTIONS = "reflections";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public ReflectionPreferences(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveReflection(String content) {
        try {
            JSONArray reflectionsArray = getReflectionsArray();
            
            JSONObject reflection = new JSONObject();
            reflection.put("content", content);
            reflection.put("timestamp", System.currentTimeMillis());
            
            // Format tanggal untuk display
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
            reflection.put("date", sdf.format(new Date()));

            reflectionsArray.put(reflection);
            
            editor.putString(KEY_REFLECTIONS, reflectionsArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getReflectionsArray() {
        String reflectionsJson = prefs.getString(KEY_REFLECTIONS, "[]");
        try {
            return new JSONArray(reflectionsJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public int getReflectionsCount() {
        return getReflectionsArray().length();
    }

    public void deleteReflection(int index) {
        JSONArray reflectionsArray = getReflectionsArray();
        if (index >= 0 && index < reflectionsArray.length()) {
            reflectionsArray.remove(index);
            editor.putString(KEY_REFLECTIONS, reflectionsArray.toString());
            editor.apply();
        }
    }

    public JSONObject getReflection(int index) {
        try {
            JSONArray reflectionsArray = getReflectionsArray();
            if (index >= 0 && index < reflectionsArray.length()) {
                return reflectionsArray.getJSONObject(index);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

