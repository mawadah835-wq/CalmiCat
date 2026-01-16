package com.example.calmicat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout btnOngoing, btnFinished, btnDelayed;
    private TextView tvUsername, tvEmail;
    private ImageButton btnBack, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inisialisasi View
        btnOngoing = findViewById(R.id.cardOngoing);
        btnFinished = findViewById(R.id.cardFinished);
        btnDelayed = findViewById(R.id.cardDelayed);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        btnBack = findViewById(R.id.btnBack);
        btnLogout = findViewById(R.id.btnLogout);

        // Load user data
        loadUserData();

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPreferences userPrefs = new UserPreferences(ProfileActivity.this);
                userPrefs.logout();
                
                Toast.makeText(ProfileActivity.this, "Logout berhasil", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Klik untuk Ongoing Tasks
        btnOngoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ListPlanActivity.class);
                startActivity(intent);
            }
        });

        // Klik untuk Finished Tasks
        btnFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ListPlanActivity.class);
                startActivity(intent);
            }
        });

        // Klik untuk Delayed Tasks
        btnDelayed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ListReflectionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadUserData() {
        UserPreferences userPrefs = new UserPreferences(this);
        String fullName = userPrefs.getFullName();
        String email = userPrefs.getEmail();

        tvUsername.setText(fullName);
        if (!email.isEmpty()) {
            tvEmail.setText(email);
        }

        // Load plan counts
        PlanPreferences planPrefs = new PlanPreferences(this);
        ReflectionPreferences reflectionPrefs = new ReflectionPreferences(this);
        
        int ongoingCount = planPrefs.getOngoingCount();
        int finishedCount = planPrefs.getFinishedCount();
        int reflectionCount = reflectionPrefs.getReflectionsCount();
        
        // Update card counts
        TextView tvOngoingCount = findViewById(R.id.tvOngoingCount);
        TextView tvFinishedCount = findViewById(R.id.tvFinishedCount);
        TextView tvDelayedCount = findViewById(R.id.tvDelayedCount);
        
        if (tvOngoingCount != null) {
            tvOngoingCount.setText(String.valueOf(ongoingCount));
        }
        if (tvFinishedCount != null) {
            tvFinishedCount.setText(String.valueOf(finishedCount));
        }
        if (tvDelayedCount != null) {
            tvDelayedCount.setText(String.valueOf(reflectionCount));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData(); // Refresh counts when returning to this activity
    }
}