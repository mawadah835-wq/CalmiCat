package com.example.calmicat;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlanPreferences {
    private static final String PREF_NAME = "PlanPrefs";
    private static final String KEY_PLANS = "plans";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public PlanPreferences(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void savePlan(String headline, String text, String repeat, String duration, String time) {
        try {
            JSONArray plansArray = getPlansArray();
            
            JSONObject plan = new JSONObject();
            plan.put("headline", headline);
            plan.put("text", text);
            plan.put("repeat", repeat);
            plan.put("duration", duration);
            plan.put("time", time);
            plan.put("status", "ongoing"); // ongoing, finished, delayed
            plan.put("timestamp", System.currentTimeMillis());

            plansArray.put(plan);
            
            editor.putString(KEY_PLANS, plansArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getPlansArray() {
        String plansJson = prefs.getString(KEY_PLANS, "[]");
        try {
            return new JSONArray(plansJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public List<String> getAllPlans() {
        List<String> plans = new ArrayList<>();
        JSONArray plansArray = getPlansArray();
        
        for (int i = 0; i < plansArray.length(); i++) {
            try {
                JSONObject plan = plansArray.getJSONObject(i);
                plans.add(plan.getString("headline"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return plans;
    }

    public int getPlansCount() {
        return getPlansArray().length();
    }

    public int getOngoingCount() {
        return getCountByStatus("ongoing");
    }

    public int getFinishedCount() {
        return getCountByStatus("finished");
    }

    public int getDelayedCount() {
        return getCountByStatus("delayed");
    }

    private int getCountByStatus(String status) {
        int count = 0;
        JSONArray plansArray = getPlansArray();
        for (int i = 0; i < plansArray.length(); i++) {
            try {
                JSONObject plan = plansArray.getJSONObject(i);
                if (plan.getString("status").equals(status)) {
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public void updatePlanStatus(int index, String newStatus) {
        try {
            JSONArray plansArray = getPlansArray();
            if (index >= 0 && index < plansArray.length()) {
                JSONObject plan = plansArray.getJSONObject(index);
                plan.put("status", newStatus);
                editor.putString(KEY_PLANS, plansArray.toString());
                editor.apply();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deletePlan(int index) {
        JSONArray plansArray = getPlansArray();
        if (index >= 0 && index < plansArray.length()) {
            plansArray.remove(index);
            editor.putString(KEY_PLANS, plansArray.toString());
            editor.apply();
        }
    }

    public JSONObject getPlan(int index) {
        try {
            JSONArray plansArray = getPlansArray();
            if (index >= 0 && index < plansArray.length()) {
                return plansArray.getJSONObject(index);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

