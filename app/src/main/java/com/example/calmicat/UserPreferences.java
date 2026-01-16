package com.example.calmicat;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String PREF_NAME = "CalmiCatPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FULL_NAME = "fullName";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public UserPreferences(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Simpan data registrasi
    public void saveUserData(String fullName, String email, String phone, String password) {
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_USERNAME, email); // username = email
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    // Login
    public boolean login(String identifier, String password) {
        String savedEmail = prefs.getString(KEY_EMAIL, "");
        String savedPhone = prefs.getString(KEY_PHONE, "");
        String savedUsername = prefs.getString(KEY_USERNAME, "admin");
        String savedPassword = prefs.getString(KEY_PASSWORD, "12345");

        // Cek apakah identifier cocok dengan email, phone, atau username
        boolean identifierMatch = identifier.equals(savedEmail) || 
                                  identifier.equals(savedPhone) || 
                                  identifier.equals(savedUsername);

        if (identifierMatch && password.equals(savedPassword)) {
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.apply();
            return true;
        }
        return false;
    }

    // Logout
    public void logout() {
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }

    // Cek apakah user sudah login
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Get user data
    public String getFullName() {
        return prefs.getString(KEY_FULL_NAME, "User");
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public String getPhone() {
        return prefs.getString(KEY_PHONE, "");
    }

    // Clear all data (untuk testing)
    public void clearAll() {
        editor.clear();
        editor.apply();
    }
}

