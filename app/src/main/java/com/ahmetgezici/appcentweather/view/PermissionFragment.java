package com.ahmetgezici.appcentweather.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ahmetgezici.appcentweather.R;
import com.ahmetgezici.appcentweather.databinding.FragmentPermissionBinding;
import com.ahmetgezici.appcentweather.viewmodel.PermissionViewModel;

import org.jetbrains.annotations.NotNull;

public class PermissionFragment extends Fragment {

    String TAG = "aaa";

    int REQUESTCODE = 1;
    boolean locationStatus = false;

    FragmentPermissionBinding binding;

    private PermissionViewModel mViewModel;

    CitySearchFragment citySearchFragment = new CitySearchFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentManager manager = getFragmentManager();

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isLocationPermCheck() && locationStatus) {
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                    .replace(R.id.fragmentLayout, citySearchFragment, citySearchFragment.getTag())
                    .commit();
        }

        binding = FragmentPermissionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

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

                manager.beginTransaction()
                        .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                        .replace(R.id.fragmentLayout, citySearchFragment, citySearchFragment.getTag())
                        .commit();
            }
        });

        binding.locPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isLocationPermCheck()) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUESTCODE);

                } else {
                    Toast.makeText(getContext(), "İzin zaten verilmiş", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
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
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUESTCODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "İzin Verildi", Toast.LENGTH_SHORT).show();
                    binding.locPermission.setText("İZİN VERİLDİ");
                }
            } else {
                Toast.makeText(getContext(), "İzin Verilmedi", Toast.LENGTH_SHORT).show();
                binding.locPermission.setText("İZİN VER");
            }
        }
    }
}