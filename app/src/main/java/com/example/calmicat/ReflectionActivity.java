package com.example.calmicat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReflectionActivity extends AppCompatActivity {

    // Deklarasi komponen UI
    private ImageButton btnBack;
    private EditText etReflection;
    private TextView btnCancel;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflection); // Sesuaikan nama file XML Anda

        // 1. Inisialisasi View berdasarkan ID di XML
        btnBack = findViewById(R.id.btnBack);
        etReflection = findViewById(R.id.etReflection);
        btnCancel = findViewById(R.id.btnCancel);
        btnDone = findViewById(R.id.btnDone);

        // 2. Logika tombol Back (Kembali)
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menutup activity saat ini dan kembali ke sebelumnya
                finish();
            }
        });

        // 3. Logika tombol Cancel (Batal)
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengosongkan inputan teks
                etReflection.setText("");
                Toast.makeText(ReflectionActivity.this, "Input dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Logika tombol Done (Selesai)
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = etReflection.getText().toString().trim();

                // Validasi jika input kosong
                if (result.isEmpty()) {
                    etReflection.setError("Jangan biarkan kosong, ya!");
                } else {
                    // Simpan reflection ke SharedPreferences
                    ReflectionPreferences reflectionPrefs = new ReflectionPreferences(ReflectionActivity.this);
                    reflectionPrefs.saveReflection(result);
                    
                    Toast.makeText(ReflectionActivity.this, "Reflection tersimpan! ✨", Toast.LENGTH_LONG).show();

                    // Clear input
                    etReflection.setText("");
                    
                    finish(); // Tutup setelah selesai
                }
            }
        });
    }
}