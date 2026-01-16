package com.example.calmicat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private ProgressBar progressBar;
    private TextView tvLoading;
    private String detectedLocation = "Unknown Location";
    private Handler handler = new Handler(Looper.getMainLooper());
    private int progressStatus = 0;
    private boolean hasNavigated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Inisialisasi View
        progressBar = findViewById(R.id.progressBar);
        tvLoading = findViewById(R.id.tv_loading);

        // Inisialisasi FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Mulai animasi loading
        startProgressAnimation();

        // Cek dan request permission lokasi
        checkLocationPermission();

        // Timeout fallback - jika dalam 7 detik belum navigasi, paksa navigasi
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasNavigated) {
                    detectedLocation = "Location timeout";
                    tvLoading.setText("Loading...");
                    navigateToWelcome();
                }
            }
        }, 7000);
    }

    private void startProgressAnimation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 2;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission sudah ada, ambil lokasi
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getLocation();
            } else {
                // Permission denied
                detectedLocation = "Location permission denied";
                tvLoading.setText(detectedLocation);
                navigateToWelcome();
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Dapatkan alamat dari koordinat
                            getAddressFromLocation(location);
                        } else {
                            detectedLocation = "Unable to detect location";
                            tvLoading.setText(detectedLocation);
                            navigateToWelcome();
                        }
                    }
                });
    }

    private void getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1
            );

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Ambil kota atau locality
                String city = address.getLocality();
                String country = address.getCountryName();
                
                if (city != null && country != null) {
                    detectedLocation = city + ", " + country;
                } else if (city != null) {
                    detectedLocation = city;
                } else if (country != null) {
                    detectedLocation = country;
                } else {
                    detectedLocation = "Location detected";
                }
            } else {
                detectedLocation = "Location detected";
            }
        } catch (IOException e) {
            e.printStackTrace();
            detectedLocation = "Location detected";
        }

        tvLoading.setText("Location: " + detectedLocation);
        navigateToWelcome();
    }

    private void navigateToWelcome() {
        if (hasNavigated) {
            return; // Sudah navigasi, jangan navigasi lagi
        }
        hasNavigated = true;

        // Tunggu sebentar untuk animasi
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, WelcomeActivity.class);
                intent.putExtra("location", detectedLocation);
                startActivity(intent);
                finish();
            }
        }, 1500); // Delay 1.5 detik
    }
}
