package com.example.calmicat;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SoundPreferences {
    private static final String PREF_NAME = "SoundPrefs";
    private static final String KEY_PLAYLIST = "playlist";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SoundPreferences(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void addSound(String name, String source) {
        try {
            JSONArray playlist = getPlaylistArray();
            
            JSONObject sound = new JSONObject();
            sound.put("name", name);
            sound.put("source", source); // "file://" atau "asset://" atau URL
            sound.put("type", detectType(source)); // "file", "asset", "url"
            sound.put("timestamp", System.currentTimeMillis());

            playlist.put(sound);
            
            editor.putString(KEY_PLAYLIST, playlist.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String detectType(String source) {
        if (source.startsWith("file://")) {
            return "file";
        } else if (source.startsWith("asset://")) {
            return "asset";
        } else if (source.startsWith("http://") || source.startsWith("https://")) {
            return "url";
        }
        return "file";
    }

    public JSONArray getPlaylistArray() {
        String playlistJson = prefs.getString(KEY_PLAYLIST, "[]");
        try {
            return new JSONArray(playlistJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public int getPlaylistCount() {
        return getPlaylistArray().length();
    }

    public JSONObject getSound(int index) {
        try {
            JSONArray playlist = getPlaylistArray();
            if (index >= 0 && index < playlist.length()) {
                return playlist.getJSONObject(index);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteSound(int index) {
        JSONArray playlist = getPlaylistArray();
        if (index >= 0 && index < playlist.length()) {
            playlist.remove(index);
            editor.putString(KEY_PLAYLIST, playlist.toString());
            editor.apply();
        }
    }

    public List<String> getAllSoundNames() {
        List<String> names = new ArrayList<>();
        JSONArray playlist = getPlaylistArray();
        
        for (int i = 0; i < playlist.length(); i++) {
            try {
                JSONObject sound = playlist.getJSONObject(i);
                names.add(sound.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return names;
    }
}

