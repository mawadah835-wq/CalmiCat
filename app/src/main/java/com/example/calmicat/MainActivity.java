package com.example.calmicat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Deklarasi variabel untuk komponen UI
    private EditText searchBar;
    private ImageView ivCatMain, ivPlus, ivNote, ivSmallIcon;
    private TextView tvMewGreeting, tvMewGreeting2, tvMewGreeting3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inisialisasi View
        searchBar = findViewById(R.id.searchBar);
        ivCatMain = findViewById(R.id.ivCatMain);
        tvMewGreeting = findViewById(R.id.tvMewGreeting);
        tvMewGreeting2 = findViewById(R.id.tvMewGreeting2);
        tvMewGreeting3 = findViewById(R.id.tvMewGreeting3);
        ivNote = findViewById(R.id.ivNote);
        ivSmallIcon = findViewById(R.id.ivSmallIcon);

        // 2. Logika Interaksi Kucing (Mew) - Buka Music Player
        ivCatMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                startActivity(intent);
            }
        });

        // 3. Logika Klik pada Sleep Reflection - Buka Reflection Activity (klik) atau List (long press)
        tvMewGreeting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReflectionActivity.class);
                startActivity(intent);
            }
        });
        
        tvMewGreeting2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListReflectionActivity.class);
                startActivity(intent);
                return true;
            }
        });

        // 4. Logika Klik pada Morning Workout (Goal dengan checkbox)
        tvMewGreeting3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Great! Keep up the good work! 💪", Toast.LENGTH_SHORT).show();
            }
        });

        // 5. Tombol Note/List - Buka AddPlanActivity (klik) atau ListPlanActivity (long press)
        ivNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPlanActivity.class);
                startActivity(intent);
            }
        });
        
        ivNote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListPlanActivity.class);
                startActivity(intent);
                return true;
            }
        });

        // 6. Icon kecil (avatar/profile) - Buka ProfileActivity
        ivSmallIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // 7. Icon Plus - Buka MusicPlayerActivity
        ImageView ivPlus = findViewById(R.id.ivPlus);
        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                startActivity(intent);
            }
        });

        // 7. Logika Search Bar
        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String query = searchBar.getText().toString();
                    if (!query.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Searching for: " + query, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}