package com.example.calmicat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi View
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Tombol Register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = etFullName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                // Validasi input
                if (fullName.isEmpty()) {
                    etFullName.setError("Nama lengkap harus diisi");
                    etFullName.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    etEmail.setError("Email harus diisi");
                    etEmail.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Format email tidak valid");
                    etEmail.requestFocus();
                    return;
                }

                if (phone.isEmpty()) {
                    etPhone.setError("Nomor telepon harus diisi");
                    etPhone.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    etPassword.setError("Password harus diisi");
                    etPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    etPassword.setError("Password minimal 6 karakter");
                    etPassword.requestFocus();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    etConfirmPassword.setError("Password tidak cocok");
                    etConfirmPassword.requestFocus();
                    return;
                }

                // Simulasi registrasi berhasil
                Toast.makeText(RegisterActivity.this, 
                    "Registrasi Berhasil! Selamat datang " + fullName, 
                    Toast.LENGTH_LONG).show();

                // Simpan data user ke SharedPreferences
                UserPreferences userPrefs = new UserPreferences(RegisterActivity.this);
                userPrefs.saveUserData(fullName, email, phone, password);

                // Pindah ke MainActivity (auto-login setelah register)
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Link ke halaman Login
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kembali ke LoginActivity
            }
        });
    }
}

