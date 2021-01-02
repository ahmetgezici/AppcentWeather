package com.ahmetgezici.appcentweather.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ahmetgezici.appcentweather.adapter.NextDayAdapter;
import com.ahmetgezici.appcentweather.databinding.ActivityMainBinding;
import com.ahmetgezici.appcentweather.model.ConsolidatedWeather;
import com.ahmetgezici.appcentweather.model.MinMax;
import com.ahmetgezici.appcentweather.model.WeatherResponse;
import com.ahmetgezici.appcentweather.viewmodel.MainViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.ahmadnemati.wind.enums.TrendType;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ahmetgezici.appcentweather.viewmodel.MainViewModel.getWeatherState;
import static com.ahmetgezici.appcentweather.viewmodel.MainViewModel.parseDate;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MainViewModel viewModel;

    CatLoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        ///////////////////////////////////////////////////////

        viewModel.isLoading.setValue(true);

        viewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {
                    getWindow().setStatusBarColor(Color.parseColor("#252330"));
                    loadingView = new CatLoadingView();
                    loadingView.show(getSupportFragmentManager(), "");
                    loadingView.setText("YÜKLENİYOR...");
                } else {
                    getWindow().setStatusBarColor(Color.parseColor("#0C294B"));
                    loadingView.dismiss();
                }
            }
        });


        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            String woeid = extras.getString("woeid");

            viewModel.getWeatherResponse(woeid).observe(this, new Observer<WeatherResponse>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onChanged(WeatherResponse weatherResponse) {
                    if (weatherResponse != null) {

                        ConsolidatedWeather currentWeathers = weatherResponse.consolidatedWeather.get(0);

                        binding.cityName.setText(weatherResponse.title);

                        binding.temp.setText(currentWeathers.theTemp.intValue() + "°");

                        String stateString = currentWeathers.weatherStateAbbr;
                        binding.weatherState.setText(getWeatherState(stateString));

                        Glide.with(MainActivity.this)
                                .load("https://www.metaweather.com/static/img/weather/png/" + stateString + ".png")
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .into(binding.weatherStateImg);


                        binding.min.setText("Min. " + currentWeathers.minTemp.intValue() + "°");
                        binding.max.setText("Maks. " + currentWeathers.maxTemp.intValue() + "°");

                        binding.visibility.setText(String.valueOf((int) (currentWeathers.visibility * 1.609344)));
                        binding.predict.setText(currentWeathers.predictability.toString());

                        binding.humidity.setText("%" + currentWeathers.humidity);
                        binding.windDirection.setText(currentWeathers.windDirectionCompass);

                        new Handler(getMainLooper()).postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ObjectAnimator anim = ObjectAnimator.ofFloat(binding.humidityProgress, "percent", 0, currentWeathers.humidity);
                                                anim.setInterpolator(new DecelerateInterpolator());
                                                anim.setDuration(2000);
                                                anim.start();
                                            }
                                        });
                                    }
                                }, 800);

                        new Handler(getMainLooper()).postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ObjectAnimator anim = ObjectAnimator.ofFloat(binding.windArrow, "rotation", 0, currentWeathers.windDirection.intValue() + 180);
                                                anim.setInterpolator(new BounceInterpolator());
                                                anim.setDuration(3000);
                                                anim.start();
                                            }
                                        });
                                    }
                                }, 800);

                        binding.windView.setPressure(currentWeathers.airPressure.intValue());
                        binding.windView.setPressureUnit("mb");
                        binding.windView.setWindSpeed(currentWeathers.windSpeed.intValue());
                        binding.windView.setWindSpeedUnit(" km/h");
                        binding.windView.setTrendType(TrendType.DOWN);
                        binding.windView.start();

                        binding.sunRise.setText(parseDate(weatherResponse.sunRise));
                        binding.sunSet.setText(parseDate(weatherResponse.sunSet));

                    }
                }
            });

            ArrayList<String> tempArrayList = new ArrayList<>();

            for (int i = 0; i < Objects.requireNonNull(viewModel.datesLiveData.getValue()).size(); i++) {

                int finalI = i;
                viewModel.getMinMax(woeid, viewModel.datesLiveData.getValue().get(i)).observe(this, new Observer<List<MinMax>>() {
                    @Override
                    public void onChanged(List<MinMax> minMax) {

                        tempArrayList.add(finalI + " " + minMax.get(0).minTemp.intValue() + ";" + minMax.get(0).maxTemp.intValue());
                        viewModel.temListLiveData.setValue(tempArrayList);
                    }
                });
            }

            viewModel.temListLiveData.observe(this, new Observer<ArrayList<String>>() {
                @Override
                public void onChanged(ArrayList<String> tempList) {
                    if (tempList.size() == viewModel.datesLiveData.getValue().size()) {

                        viewModel.calculate(tempList);

                        NextDayAdapter nextDayAdapter = new NextDayAdapter(viewModel.dayNamesLiveData.getValue(),
                                viewModel.datesLiveData.getValue(), viewModel.minMaxDifLiveData.getValue(),
                                viewModel.refMinHighLiveData.getValue(), woeid, getApplicationContext());
                        binding.nextDayRecycler.setAdapter(nextDayAdapter);
                        binding.nextDayRecycler.getAdapter().notifyDataSetChanged();

                        binding.progressLayout.animate().setDuration(400).alpha(0.0f);

                        viewModel.isLoading.setValue(false);
                    }
                }
            });
        }
    }
}