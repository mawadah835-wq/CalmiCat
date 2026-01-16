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

public class ListPlanActivity extends AppCompatActivity {

    private LinearLayout containerPlans;
    private PlanPreferences planPrefs;
    private TextView tvEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_plan);

        // Initialize views
        containerPlans = findViewById(R.id.containerPlans);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        ImageButton btnBack = findViewById(R.id.btnBack);

        planPrefs = new PlanPreferences(this);

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Load plans
        loadPlans();
    }

    private void loadPlans() {
        containerPlans.removeAllViews();
        
        int planCount = planPrefs.getPlansCount();
        
        if (planCount == 0) {
            tvEmptyState.setVisibility(View.VISIBLE);
            containerPlans.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            containerPlans.setVisibility(View.VISIBLE);
            
            for (int i = 0; i < planCount; i++) {
                final int index = i;
                JSONObject plan = planPrefs.getPlan(i);
                
                if (plan != null) {
                    View planCard = createPlanCard(plan, index);
                    containerPlans.addView(planCard);
                }
            }
        }
    }

    private View createPlanCard(final JSONObject plan, final int index) {
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
            // Title
            TextView tvTitle = new TextView(this);
            tvTitle.setText(plan.getString("headline"));
            tvTitle.setTextSize(18);
            tvTitle.setTextColor(getResources().getColor(android.R.color.black));
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            card.addView(tvTitle);

            // Description
            TextView tvDesc = new TextView(this);
            tvDesc.setText(plan.getString("text"));
            tvDesc.setTextSize(14);
            tvDesc.setTextColor(getResources().getColor(android.R.color.darker_gray));
            LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            descParams.setMargins(0, 8, 0, 12);
            tvDesc.setLayoutParams(descParams);
            card.addView(tvDesc);

            // Info row
            LinearLayout infoRow = new LinearLayout(this);
            infoRow.setOrientation(LinearLayout.HORIZONTAL);
            
            TextView tvRepeat = new TextView(this);
            tvRepeat.setText("🔁 " + plan.getString("repeat"));
            tvRepeat.setTextSize(12);
            tvRepeat.setTextColor(getResources().getColor(android.R.color.darker_gray));
            infoRow.addView(tvRepeat);
            
            TextView tvDuration = new TextView(this);
            tvDuration.setText("  ⏱️ " + plan.getString("duration"));
            tvDuration.setTextSize(12);
            tvDuration.setTextColor(getResources().getColor(android.R.color.darker_gray));
            infoRow.addView(tvDuration);
            
            TextView tvTime = new TextView(this);
            tvTime.setText("  ⏰ " + plan.getString("time"));
            tvTime.setTextSize(12);
            tvTime.setTextColor(getResources().getColor(android.R.color.darker_gray));
            infoRow.addView(tvTime);
            
            card.addView(infoRow);

            // Status badge
            TextView tvStatus = new TextView(this);
            String status = plan.getString("status");
            tvStatus.setText(status.toUpperCase());
            tvStatus.setTextSize(12);
            tvStatus.setPadding(16, 8, 16, 8);
            tvStatus.setBackgroundResource(R.drawable.bg_cloud);
            
            if (status.equals("ongoing")) {
                tvStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            } else if (status.equals("finished")) {
                tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
            
            LinearLayout.LayoutParams statusParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            statusParams.setMargins(0, 12, 0, 8);
            tvStatus.setLayoutParams(statusParams);
            card.addView(tvStatus);

            // Action buttons row
            LinearLayout actionRow = new LinearLayout(this);
            actionRow.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams actionParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            actionParams.setMargins(0, 12, 0, 0);
            actionRow.setLayoutParams(actionParams);

            // Mark as done button
            TextView btnDone = new TextView(this);
            btnDone.setText("✓ Done");
            btnDone.setTextSize(14);
            btnDone.setTextColor(getResources().getColor(android.R.color.white));
            btnDone.setBackgroundResource(R.drawable.btn_rounded);
            btnDone.setPadding(24, 12, 24, 12);
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    planPrefs.updatePlanStatus(index, "finished");
                    Toast.makeText(ListPlanActivity.this, "Plan marked as finished! 🎉", Toast.LENGTH_SHORT).show();
                    loadPlans();
                }
            });
            actionRow.addView(btnDone);

            // Delete button
            TextView btnDelete = new TextView(this);
            btnDelete.setText("🗑️ Delete");
            btnDelete.setTextSize(14);
            btnDelete.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            btnDelete.setBackgroundResource(R.drawable.bg_cloud);
            btnDelete.setPadding(24, 12, 24, 12);
            LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            deleteParams.setMargins(16, 0, 0, 0);
            btnDelete.setLayoutParams(deleteParams);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(ListPlanActivity.this)
                        .setTitle("Delete Plan")
                        .setMessage("Are you sure you want to delete this plan?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            planPrefs.deletePlan(index);
                            Toast.makeText(ListPlanActivity.this, "Plan deleted", Toast.LENGTH_SHORT).show();
                            loadPlans();
                        })
                        .setNegativeButton("No", null)
                        .show();
                }
            });
            actionRow.addView(btnDelete);

            card.addView(actionRow);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return card;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPlans(); // Refresh when returning to this activity
    }
}

