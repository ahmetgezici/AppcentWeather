package com.ahmetgezici.appcentweather.View;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmetgezici.appcentweather.Adapter.CitiesAdapter;
import com.ahmetgezici.appcentweather.Model.LocationSearch;
import com.ahmetgezici.appcentweather.Network.ApiClient;
import com.ahmetgezici.appcentweather.Network.ApiInterface;
import com.ahmetgezici.appcentweather.R;
import com.ahmetgezici.appcentweather.databinding.ActivityCitySearchBinding;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitySearchActivity extends AppCompatActivity {

    String TAG = "aaa";

    ApiInterface apiInterface;
    LocationListener locationListener;

    ActivityCitySearchBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = ActivityCitySearchBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        ///////////////////////////////////////////////////////

        viewBinding.loadingAnim.startAnim();

        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        ArrayList<Integer> gradientList = new ArrayList<>();
        setupList(gradientList);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();

                    Log.e(TAG, decimalFormat.format(latitude) + "," + decimalFormat.format(longitude));

                    lm.removeUpdates(this);

                    apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

                    Call<List<LocationSearch>> locationSearchCall = apiInterface.getLocation(decimalFormat.format(latitude) + "," + decimalFormat.format(longitude));

                    locationSearchCall.enqueue(new Callback<List<LocationSearch>>() {
                        @Override
                        public void onResponse(@NotNull Call<List<LocationSearch>> call, @NotNull Response<List<LocationSearch>> response) {

                            if (response.code() == 200) {
                                List<LocationSearch> locationSearch = response.body();

                                CitiesAdapter citiesAdapter = new CitiesAdapter(locationSearch, gradientList, getApplicationContext());
                                viewBinding.citiesRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                viewBinding.citiesRecycler.setAdapter(citiesAdapter);
                                viewBinding.citiesRecycler.getAdapter().notifyDataSetChanged();

                                viewBinding.loadingLayout.animate().setDuration(400).alpha(0.0f);

                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<List<LocationSearch>> call, @NotNull Throwable t) {
                            Log.e(TAG, t.getLocalizedMessage());
                        }
                    });
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            };

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
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