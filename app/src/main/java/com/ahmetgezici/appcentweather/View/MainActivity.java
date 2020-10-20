package com.ahmetgezici.appcentweather.View;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmetgezici.appcentweather.Adapter.NextDayAdapter;
import com.ahmetgezici.appcentweather.Model.ConsolidatedWeather;
import com.ahmetgezici.appcentweather.Model.MinMax;
import com.ahmetgezici.appcentweather.Model.WeatherResponse;
import com.ahmetgezici.appcentweather.Network.ApiClient;
import com.ahmetgezici.appcentweather.Network.ApiInterface;
import com.ahmetgezici.appcentweather.databinding.ActivityMainBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.ahmadnemati.wind.enums.TrendType;
import com.roger.catloadinglibrary.CatLoadingView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String TAG = "aaa";

    ApiInterface apiInterface;
    ActivityMainBinding viewBinding;

    CatLoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        ///////////////////////////////////////////////////////

        loadingView = new CatLoadingView();
        loadingView.show(getSupportFragmentManager(), "");
        loadingView.setText("YÜKLENİYOR...");

        getWindow().setStatusBarColor(Color.parseColor("#0C294B"));

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            String woeid = extras.getString("woeid");


            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
            decimalFormatSymbols.setDecimalSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);


            apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

            Call<WeatherResponse> weatherCall = apiInterface.getData(woeid);

            weatherCall.enqueue(new Callback<WeatherResponse>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<WeatherResponse> call, @NotNull Response<WeatherResponse> response) {
                    if (response.code() == 200) {

                        WeatherResponse weatherResponse = response.body();
                        if (weatherResponse != null) {

                            ConsolidatedWeather currentWeathers = weatherResponse.consolidatedWeather.get(0);

                            viewBinding.cityName.setText(weatherResponse.title);

                            viewBinding.temp.setText(currentWeathers.theTemp.intValue() + "°");

                            String stateString = currentWeathers.weatherStateAbbr;
                            viewBinding.weatherState.setText(getWeatherState(stateString));

                            Glide.with(MainActivity.this)
                                    .load("https://www.metaweather.com/static/img/weather/png/" + stateString + ".png")
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .into(viewBinding.weatherStateImg);


                            viewBinding.min.setText("Min. " + currentWeathers.minTemp.intValue() + "°");
                            viewBinding.max.setText("Maks. " + currentWeathers.maxTemp.intValue() + "°");

                            viewBinding.visibility.setText(String.valueOf((int) (currentWeathers.visibility * 1.609344)));
                            viewBinding.predict.setText(currentWeathers.predictability.toString());

                            viewBinding.humidity.setText("%" + currentWeathers.humidity);
                            viewBinding.windDirection.setText(currentWeathers.windDirectionCompass);

                            new Handler().postDelayed(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ObjectAnimator anim = ObjectAnimator.ofFloat(viewBinding.humidityProgress, "percent", 0, currentWeathers.humidity);
                                                    anim.setInterpolator(new DecelerateInterpolator());
                                                    anim.setDuration(2000);
                                                    anim.start();
                                                }
                                            });
                                        }
                                    }, 800);

                            new Handler().postDelayed(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ObjectAnimator anim = ObjectAnimator.ofFloat(viewBinding.windArrow, "rotation", 0, currentWeathers.windDirection.intValue() + 180);
                                                    anim.setInterpolator(new BounceInterpolator());
                                                    anim.setDuration(3000);
                                                    anim.start();
                                                }
                                            });
                                        }
                                    }, 800);

                            viewBinding.windView.setPressure(currentWeathers.airPressure.intValue());
                            viewBinding.windView.setPressureUnit("mb");
                            viewBinding.windView.setWindSpeed(currentWeathers.windSpeed.intValue());
                            viewBinding.windView.setWindSpeedUnit(" km/h");
                            viewBinding.windView.setTrendType(TrendType.DOWN);
                            viewBinding.windView.start();

                            viewBinding.sunRise.setText(parseDate(weatherResponse.sunRise));
                            viewBinding.sunSet.setText(parseDate(weatherResponse.sunSet));

                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<WeatherResponse> call, @NotNull Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });

            ArrayList<String> dates = new ArrayList<>();
            ArrayList<String> dayNames = new ArrayList<>();

            Date currentTime = Calendar.getInstance().getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", new Locale("tr", "TR"));
            String[] days = new String[]{"Paz", "Pzt", "Sal", "Çar", "Per", "Cum", "Cmt"};

            Calendar c = Calendar.getInstance();
            c.setTime(currentTime);

            for (int i = 0; i < 7; i++) {
                c.add(Calendar.DATE, 1);

                dates.add(dateFormat.format(c.getTime()));
                dayNames.add(days[c.get(Calendar.DAY_OF_WEEK) - 1]);
            }

            dayNames.set(0, "Yarın");


            ArrayList<String> arrayList = new ArrayList<>();

            for (int i = 0; i < dates.size(); i++) {

                Call<List<MinMax>> minMaxCall = apiInterface.getMinMax(woeid, dates.get(i));

                int finalI = i;
                minMaxCall.enqueue(new Callback<List<MinMax>>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NotNull Call<List<MinMax>> call, @NotNull Response<List<MinMax>> response) {
                        if (response.code() == 200) {
                            List<MinMax> minMax = response.body();

                            arrayList.add(finalI + " " + minMax.get(0).minTemp.intValue() + ";" + minMax.get(0).maxTemp.intValue());
                            if (arrayList.size() == dates.size()) {
                                Collections.sort(arrayList);

                                int minMaxDifference = 0;
                                int refMinHigh = 0;

                                for (int j = 0; j < arrayList.size(); j++) {
                                    String[] temp = arrayList.get(j).substring(2).split(";");
                                    int minTemp = Integer.parseInt(temp[0]);
                                    int maxTemp = Integer.parseInt(temp[1]);

                                    int a = (maxTemp - minTemp);
                                    if (minMaxDifference < a) {
                                        minMaxDifference = a;
                                    }

                                    if (refMinHigh < minTemp) {
                                        refMinHigh = minTemp;
                                    }
                                }

                                NextDayAdapter nextDayAdapter = new NextDayAdapter(dayNames, dates, minMaxDifference, refMinHigh, woeid, getApplicationContext());
                                viewBinding.nextDayRecycler.setAdapter(nextDayAdapter);
                                viewBinding.nextDayRecycler.getAdapter().notifyDataSetChanged();

                                viewBinding.progressLayout.animate().setDuration(400).alpha(0.0f);
                                loadingView.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<MinMax>> call, @NotNull Throwable t) {
                        Log.e("aaa", "onFailure: " + t.getMessage());
                    }
                });
            }
        }
    }


    private String getWeatherState(String state) {
        switch (state) {
            case "sn":
                return "Kar Yağışlı";
            case "sl":
                return "Karla Karışık Yağmur";
            case "h":
                return "Dolu";
            case "t":
                return "Gök Gürültülü Sağanak";
            case "hr":
                return "Yoğun Yağmurlu";
            case "lr":
                return "Yağmurlu";
            case "s":
                return "Hafif Yağmurlu";
            case "hc":
                return "Çok Bulutlu";
            case "lc":
                return "Az Bulutlu";
            case "c":
                return "Güneşli";
        }
        return state;
    }

    public static String parseDate(String inputDateString) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("tr", "TR"));
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("HH:mm", new Locale("tr", "TR"));

        String outputDateString = "";

        try {
            Date date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }

}