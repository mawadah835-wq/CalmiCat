package com.example.calmicat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RelaxingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pastikan nama file XML kamu sesuai (misalnya activity_relaxing.xml)
        setContentView(R.layout.activity_relaxingambienece);

        // 1. Inisialisasi View dari XML
        Button btnBack = findViewById(R.id.btnBack);
        Button btnNext = findViewById(R.id.btnNext);
        TextView tvSkip = findViewById(R.id.tvSkip);

        // 2. Tombol Back: Kembali ke WelcomeActivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menutup activity ini untuk kembali ke activity sebelumnya
                finish();
            }
        });

        // 3. Tombol Next: Pindah ke LoginActivity
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelaxingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 4. Tombol Skip: Langsung ke LoginActivity
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelaxingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}