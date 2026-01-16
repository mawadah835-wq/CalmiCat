package com.example.calmicat;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class ListReflectionActivity extends AppCompatActivity {

    private LinearLayout containerReflections;
    private ReflectionPreferences reflectionPrefs;
    private TextView tvEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reflection);

        // Initialize views
        containerReflections = findViewById(R.id.containerReflections);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        ImageButton btnBack = findViewById(R.id.btnBack);

        reflectionPrefs = new ReflectionPreferences(this);

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Load reflections
        loadReflections();
    }

    private void loadReflections() {
        containerReflections.removeAllViews();
        
        int reflectionCount = reflectionPrefs.getReflectionsCount();
        
        if (reflectionCount == 0) {
            tvEmptyState.setVisibility(View.VISIBLE);
            containerReflections.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            containerReflections.setVisibility(View.VISIBLE);
            
            // Show in reverse order (newest first)
            for (int i = reflectionCount - 1; i >= 0; i--) {
                final int index = i;
                JSONObject reflection = reflectionPrefs.getReflection(i);
                
                if (reflection != null) {
                    View reflectionCard = createReflectionCard(reflection, index);
                    containerReflections.addView(reflectionCard);
                }
            }
        }
    }

    private View createReflectionCard(final JSONObject reflection, final int index) {
        LinearLayout card = new LinearLayout(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(cardParams);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.bg_cloud);
        card.setPadding(32, 24, 32, 24);
        card.setElevation(4f);

        try {
            // Date
            TextView tvDate = new TextView(this);
            tvDate.setText(reflection.getString("date"));
            tvDate.setTextSize(12);
            tvDate.setTextColor(getResources().getColor(android.R.color.darker_gray));
            card.addView(tvDate);

            // Content
            TextView tvContent = new TextView(this);
            tvContent.setText(reflection.getString("content"));
            tvContent.setTextSize(16);
            tvContent.setTextColor(getResources().getColor(android.R.color.black));
            LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            contentParams.setMargins(0, 12, 0, 16);
            tvContent.setLayoutParams(contentParams);
            card.addView(tvContent);

            // Delete button
            TextView btnDelete = new TextView(this);
            btnDelete.setText("🗑️ Delete");
            btnDelete.setTextSize(14);
            btnDelete.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            btnDelete.setBackgroundResource(R.drawable.bg_cloud);
            btnDelete.setPadding(24, 12, 24, 12);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(ListReflectionActivity.this)
                        .setTitle("Delete Reflection")
                        .setMessage("Are you sure you want to delete this reflection?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            reflectionPrefs.deleteReflection(index);
                            Toast.makeText(ListReflectionActivity.this, "Reflection deleted", Toast.LENGTH_SHORT).show();
                            loadReflections();
                        })
                        .setNegativeButton("No", null)
                        .show();
                }
            });
            card.addView(btnDelete);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return card;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReflections(); // Refresh when returning to this activity
    }
}

