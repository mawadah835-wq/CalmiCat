package com.example.calmicat;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MusicPlayerActivity extends AppCompatActivity {

    private ImageView btnPlayPause;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private boolean isPlaying = false;
    private boolean useAlternativeSound = true;
    private ToneGenerator toneGenerator;
    private SoundPreferences soundPrefs;
    private LinearLayout containerPlaylist;
    private TextView tvCurrentSong;
    private int currentSoundIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        try {
            // 1. Inisialisasi View
            btnPlayPause = findViewById(R.id.btnPlayPause);
            seekBar = findViewById(R.id.seekBar);
            containerPlaylist = findViewById(R.id.containerPlaylist);
            tvCurrentSong = findViewById(R.id.tvCurrentSong);
            
            soundPrefs = new SoundPreferences(this);

            // 2. Load playlist
            loadPlaylist();

            // 3. Check if playlist has sounds
            int playlistCount = soundPrefs.getPlaylistCount();
            if (playlistCount == 0) {
                // No sounds in playlist, use alternative sound
                useAlternativeSound = true;
                toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 80);
                seekBar.setMax(100);
                seekBar.setProgress(0);
                if (tvCurrentSong != null) {
                    tvCurrentSong.setText("No sounds in playlist");
                }
            } else {
                // Has sounds, but start with alternative until user selects one
                useAlternativeSound = true;
                toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 80);
                seekBar.setMax(100);
                seekBar.setProgress(0);
                if (tvCurrentSong != null) {
                    tvCurrentSong.setText("Select a sound from playlist");
                }
            }

            // 4. Logika Tombol Play/Pause
            btnPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isPlaying) {
                        pauseMusic();
                    } else {
                        playMusic();
                    }
                }
            });
            
            // Add sound button
            ImageView btnAddSound = findViewById(R.id.btnAddSound);
            if (btnAddSound != null) {
                btnAddSound.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MusicPlayerActivity.this, AddSoundActivity.class);
                        startActivity(intent);
                    }
                });
            }
            
        } catch (Exception e) {
            Toast.makeText(this, "Error loading music player: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    
    private void loadPlaylist() {
        if (containerPlaylist == null) return;
        
        containerPlaylist.removeAllViews();
        
        int playlistCount = soundPrefs.getPlaylistCount();
        
        if (playlistCount == 0) {
            TextView tvEmpty = new TextView(this);
            tvEmpty.setText("No sounds in playlist.\nTap + to add sounds!");
            tvEmpty.setTextSize(14);
            tvEmpty.setTextColor(getResources().getColor(android.R.color.darker_gray));
            tvEmpty.setPadding(32, 24, 32, 24);
            tvEmpty.setGravity(android.view.Gravity.CENTER);
            containerPlaylist.addView(tvEmpty);
            return;
        }
        
        for (int i = 0; i < playlistCount; i++) {
            final int index = i;
            JSONObject sound = soundPrefs.getSound(i);
            
            if (sound != null) {
                View soundItem = createSoundItem(sound, index);
                containerPlaylist.addView(soundItem);
            }
        }
    }
    
    private View createSoundItem(final JSONObject sound, final int index) {
        LinearLayout item = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 12);
        item.setLayoutParams(params);
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setBackgroundResource(R.drawable.bg_cloud);
        item.setPadding(16, 16, 16, 16);
        item.setClickable(true);
        item.setFocusable(true);
        
        try {
            TextView tvName = new TextView(this);
            tvName.setText(sound.getString("name"));
            tvName.setTextSize(16);
            tvName.setTextColor(getResources().getColor(android.R.color.black));
            LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            tvName.setLayoutParams(nameParams);
            item.addView(tvName);
            
            // Play button
            TextView btnPlay = new TextView(this);
            btnPlay.setText("▶ Play");
            btnPlay.setTextSize(14);
            btnPlay.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            btnPlay.setPadding(16, 8, 16, 8);
            btnPlay.setBackgroundResource(R.drawable.btn_rounded);
            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSoundFromPlaylist(index);
                }
            });
            item.addView(btnPlay);
            
            // Delete button
            TextView btnDelete = new TextView(this);
            btnDelete.setText("🗑️");
            btnDelete.setTextSize(16);
            btnDelete.setPadding(12, 8, 12, 8);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    soundPrefs.deleteSound(index);
                    Toast.makeText(MusicPlayerActivity.this, "Sound removed from playlist", Toast.LENGTH_SHORT).show();
                    loadPlaylist();
                    if (currentSoundIndex == index) {
                        pauseMusic();
                        currentSoundIndex = -1;
                    }
                }
            });
            item.addView(btnDelete);
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return item;
    }
    
    private void playSoundFromPlaylist(int index) {
        try {
            // Stop current playback
            if (isPlaying) {
                pauseMusic();
            }
            
            JSONObject sound = soundPrefs.getSound(index);
            if (sound == null) {
                Toast.makeText(this, "Sound not found", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String source = sound.getString("source");
            String name = sound.getString("name");
            String type = sound.getString("type");
            
            currentSoundIndex = index;
            useAlternativeSound = false;
            
            // Release old media player
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            
            // Try to load based on type
            if (type.equals("file")) {
                // File URI
                try {
                    Uri uri = Uri.parse(source);
                    mediaPlayer = MediaPlayer.create(this, uri);
                } catch (Exception e) {
                    Toast.makeText(this, "Cannot load file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    useAlternativeSound = true;
                    return;
                }
            } else if (type.equals("asset")) {
                // Asset file (remove asset:// prefix)
                String assetName = source.replace("asset://", "");
                try {
                    // Try to get resource ID
                    int resId = getResources().getIdentifier(assetName.replace(".mp3", "").replace(".wav", ""), "raw", getPackageName());
                    if (resId != 0) {
                        mediaPlayer = MediaPlayer.create(this, resId);
                    } else {
                        throw new Exception("Asset not found: " + assetName);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Cannot load asset: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    useAlternativeSound = true;
                    return;
                }
            } else if (type.equals("url")) {
                // URL
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(source);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            setupSeekBar();
                            playMusic();
                        }
                    });
                    mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            Toast.makeText(MusicPlayerActivity.this, "Error loading URL", Toast.LENGTH_SHORT).show();
                            useAlternativeSound = true;
                            return true;
                        }
                    });
                    return; // Will play when prepared
                } catch (Exception e) {
                    Toast.makeText(this, "Cannot load URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    useAlternativeSound = true;
                    return;
                }
            }
            
            if (mediaPlayer == null) {
                Toast.makeText(this, "Cannot load sound file", Toast.LENGTH_SHORT).show();
                useAlternativeSound = true;
                return;
            }
            
            // Update UI
            if (tvCurrentSong != null) {
                tvCurrentSong.setText("Now Playing: " + name);
            }
            
            setupSeekBar();
            playMusic();
            
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading sound", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void setupSeekBar() {
        if (mediaPlayer == null) return;
        
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setEnabled(true);
        
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void playMusic() {
        if (useAlternativeSound) {
            // Play notification sound repeatedly
            isPlaying = true;
            btnPlayPause.setImageResource(R.drawable.img_10);
            playAlternativeSound();
            Toast.makeText(this, "Playing relaxing sound...", Toast.LENGTH_SHORT).show();
        } else if (mediaPlayer != null) {
            mediaPlayer.start();
            isPlaying = true;
            btnPlayPause.setImageResource(R.drawable.img_10);
            updateSeekBar();
        }
    }
    
    private void updateSeekBar() {
        if (mediaPlayer != null && isPlaying && !useAlternativeSound) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            }, 1000);
        }
    }

    private void pauseMusic() {
        isPlaying = false;
        btnPlayPause.setImageResource(R.drawable.img_10);
        
        if (!useAlternativeSound && mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
    
    private void playAlternativeSound() {
        if (useAlternativeSound && isPlaying && toneGenerator != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isPlaying && toneGenerator != null) {
                        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 300);
                        
                        // Update seekbar for visual effect
                        int progress = seekBar.getProgress() + 5;
                        if (progress > 100) progress = 0;
                        seekBar.setProgress(progress);
                        
                        // Repeat every 2 seconds
                        handler.postDelayed(this, 2000);
                    }
                }
            }, 100);
        }
    }

    // SeekBar hanya untuk visual effect dengan alternative sound

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPlaying = false;
        
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        
        if (toneGenerator != null) {
            toneGenerator.release();
            toneGenerator = null;
        }
        
        handler.removeCallbacksAndMessages(null);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (isPlaying) {
            pauseMusic();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadPlaylist(); // Refresh playlist when returning
    }
}