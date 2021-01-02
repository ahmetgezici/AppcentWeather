package com.ahmetgezici.appcentweather.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ahmetgezici.appcentweather.databinding.ActivityPermissionBinding;

public class PermissionActivity extends AppCompatActivity {

    String TAG = "aaa";

    int REQUESTCODE = 1;
    boolean locationStatus = false;

    ActivityPermissionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isLocationPermCheck() && locationStatus) {
            startActivity(new Intent(PermissionActivity.this, CitySearchActivity.class));
            finish();
        }

        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ///////////////////////////////////////////////////////

        binding.locSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        binding.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PermissionActivity.this, CitySearchActivity.class));
                finish();
            }
        });

        binding.locPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isLocationPermCheck()) {

                    ActivityCompat.requestPermissions(PermissionActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUESTCODE);

                } else {
                    Toast.makeText(PermissionActivity.this, "İzin zaten verilmiş", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            binding.gpsSwitch.setChecked(true);
            binding.gpsState.setText("Açık");
            binding.locSetting.setVisibility(View.INVISIBLE);
            locationStatus = true;
        } else {
            binding.gpsSwitch.setChecked(false);
            binding.gpsState.setText("Kapalı");
            binding.locSetting.setVisibility(View.VISIBLE);
            locationStatus = false;
        }

        if (isLocationPermCheck() && locationStatus) {
            binding.complete.setEnabled(true);
        }

    }

    public boolean isLocationPermCheck() {
        return ActivityCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUESTCODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(PermissionActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "İzin Verildi", Toast.LENGTH_SHORT).show();
                    binding.locPermission.setText("İZİN VERİLDİ");
                }
            } else {
                Toast.makeText(this, "İzin Verilmedi", Toast.LENGTH_SHORT).show();
                binding.locPermission.setText("İZİN VER");
            }
        }
    }
}