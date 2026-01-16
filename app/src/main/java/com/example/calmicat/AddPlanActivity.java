package com.example.calmicat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddPlanActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etHeadline, etText, etNotificationTime;
    private Spinner spinnerRepeat, spinnerDuration;
    private Button btnSave;
    private int selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        // Inisialisasi Views
        btnBack = findViewById(R.id.btnBack);
        etHeadline = findViewById(R.id.etHeadline);
        etText = findViewById(R.id.etText);
        spinnerRepeat = findViewById(R.id.spinnerRepeat);
        spinnerDuration = findViewById(R.id.spinnerDuration);
        etNotificationTime = findViewById(R.id.etNotificationTime);
        btnSave = findViewById(R.id.btnSave);

        // Setup Spinner Repeat
        String[] repeatOptions = {"Once", "Daily", "Weekly", "Monthly"};
        ArrayAdapter<String> repeatAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, repeatOptions);
        repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeat.setAdapter(repeatAdapter);

        // Setup Spinner Duration
        String[] durationOptions = {"15 mins", "30 mins", "1 hour", "2 hours", "1 day"};
        ArrayAdapter<String> durationAdapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, durationOptions);
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(durationAdapter);

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Time Picker
        etNotificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        // Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlan();
            }
        });
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minuteOfHour;
                    String time = String.format("%02d:%02d", hourOfDay, minuteOfHour);
                    etNotificationTime.setText(time);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void savePlan() {
        String headline = etHeadline.getText().toString().trim();
        String text = etText.getText().toString().trim();
        String repeat = spinnerRepeat.getSelectedItem().toString();
        String duration = spinnerDuration.getSelectedItem().toString();
        String notificationTime = etNotificationTime.getText().toString();

        if (headline.isEmpty()) {
            etHeadline.setError("Headline harus diisi");
            etHeadline.requestFocus();
            return;
        }

        if (notificationTime.isEmpty()) {
            Toast.makeText(this, "Pilih waktu notifikasi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simpan plan ke SharedPreferences
        PlanPreferences planPrefs = new PlanPreferences(this);
        planPrefs.savePlan(headline, text, repeat, duration, notificationTime);

        Toast.makeText(this, "Plan berhasil disimpan!", Toast.LENGTH_SHORT).show();
        finish();
    }
}

