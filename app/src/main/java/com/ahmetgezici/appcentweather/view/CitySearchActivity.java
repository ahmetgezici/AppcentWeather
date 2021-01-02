package com.ahmetgezici.appcentweather.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmetgezici.appcentweather.R;
import com.ahmetgezici.appcentweather.adapter.CitiesAdapter;
import com.ahmetgezici.appcentweather.databinding.ActivityCitySearchBinding;
import com.ahmetgezici.appcentweather.model.LocationSearch;
import com.ahmetgezici.appcentweather.viewmodel.CitySearchViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class CitySearchActivity extends AppCompatActivity {

    ActivityCitySearchBinding binding;

    CitySearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(this).get(CitySearchViewModel.class);

        ///////////////////////////////////////////////////////

        viewModel.isLoading.setValue(true);

        ArrayList<Integer> gradientList = new ArrayList<>();
        setupList(gradientList);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLocations().get(0);

                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();

                    viewModel.getLocations(latitude, longitude).observe(CitySearchActivity.this, new Observer<List<LocationSearch>>() {
                        @Override
                        public void onChanged(List<LocationSearch> locationSearches) {

                            CitiesAdapter citiesAdapter = new CitiesAdapter(locationSearches, gradientList, getApplicationContext());
                            binding.citiesRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            binding.citiesRecycler.setAdapter(citiesAdapter);
                            binding.citiesRecycler.getAdapter().notifyDataSetChanged();

                            viewModel.isLoading.setValue(false);

                        }
                    });

                    locationProviderClient.removeLocationUpdates(this);

                }
            };
            locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }

        viewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.loadingAnim.startAnim();
                } else {
                    binding.loadingLayout.animate().setDuration(400).alpha(0.0f);
                }
            }
        });

    }


    void setupList(ArrayList<Integer> gradientList) {
        gradientList.add(R.drawable.gradient1);
        gradientList.add(R.drawable.gradient2);
        gradientList.add(R.drawable.gradient3);
        gradientList.add(R.drawable.gradient4);
        gradientList.add(R.drawable.gradient5);
        gradientList.add(R.drawable.gradient6);
        gradientList.add(R.drawable.gradient7);
        gradientList.add(R.drawable.gradient8);
        gradientList.add(R.drawable.gradient9);
    }
}