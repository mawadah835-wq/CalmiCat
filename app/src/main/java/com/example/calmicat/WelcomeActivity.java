package com.example.calmicat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Inisialisasi View dari XML
        Button btnNext = findViewById(R.id.btnNext);
        TextView tvSkip = findViewById(R.id.tvSkip);
        TextView tvLocation = findViewById(R.id.tvLocation);

        // Ambil lokasi dari Intent
        String location = getIntent().getStringExtra("location");
        if (location != null && !location.isEmpty()) {
            tvLocation.setText("Welcome aboard, User!\nYou're in " + location);
        } else {
            tvLocation.setText("Welcome aboard, User!");
        }

        // Tombol "Next" - Pindah ke RelaxingActivity
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, RelaxingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Tombol "SKIP" - Langsung ke LoginActivity
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}