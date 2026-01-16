package com.example.calmicat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddSoundActivity extends AppCompatActivity {

    private EditText etSoundName;
    private EditText etSoundPath;
    private Button btnChooseFile;
    private Button btnSave;
    private ImageButton btnBack;
    private TextView tvInfo;
    
    private static final int REQUEST_CODE_PICK_FILE = 100;
    private Uri selectedFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sound);

        // Initialize views
        etSoundName = findViewById(R.id.etSoundName);
        etSoundPath = findViewById(R.id.etSoundPath);
        btnChooseFile = findViewById(R.id.btnChooseFile);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        tvInfo = findViewById(R.id.tvInfo);

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Choose file button
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        // Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSound();
            }
        });

        // Info text
        tvInfo.setText("You can add sound files from:\n" +
                "• Local file (mp3, wav, ogg)\n" +
                "• Asset folder (asset://filename.mp3)\n" +
                "• URL (http://example.com/sound.mp3)");
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Audio File"), REQUEST_CODE_PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedFileUri = data.getData();
                etSoundPath.setText(selectedFileUri.toString());
                Toast.makeText(this, "File selected: " + selectedFileUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveSound() {
        String name = etSoundName.getText().toString().trim();
        String path = etSoundPath.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            etSoundName.setError("Sound name is required!");
            return;
        }

        if (path.isEmpty()) {
            etSoundPath.setError("Sound path is required!");
            return;
        }

        // Format path
        String formattedPath = formatPath(path);

        // Save to preferences
        SoundPreferences soundPrefs = new SoundPreferences(this);
        soundPrefs.addSound(name, formattedPath);

        Toast.makeText(this, "Sound added to playlist! 🎵", Toast.LENGTH_SHORT).show();
        
        // Clear fields
        etSoundName.setText("");
        etSoundPath.setText("");
        selectedFileUri = null;
        
        // Go back to music player
        finish();
    }

    private String formatPath(String path) {
        // If it's a file URI, keep it as is
        if (path.startsWith("file://") || path.startsWith("content://")) {
            return path;
        }
        
        // If it's an asset reference
        if (path.startsWith("asset://")) {
            return path;
        }
        
        // If it's a URL
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }
        
        // If it's just a filename, assume it's an asset
        if (!path.contains("/") && !path.contains("://")) {
            return "asset://" + path;
        }
        
        // Otherwise, treat as file path
        if (!path.startsWith("file://")) {
            return "file://" + path;
        }
        
        return path;
    }
}

