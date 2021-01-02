package com.ahmetgezici.appcentweather.view.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.ahmetgezici.appcentweather.R;
import com.ahmetgezici.appcentweather.model.ConsolidatedWeather;
import com.ahmetgezici.appcentweather.model.WeatherResponse;
import com.ahmetgezici.appcentweather.network.ApiClient;
import com.ahmetgezici.appcentweather.network.ApiInterface;
import com.ahmetgezici.appcentweather.view.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Response;

public class Weather2Widget extends AppWidgetProvider {

    static String TAG = "aaa";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String woeid = Widget2ConfigActivity.loadTitlePref(context, appWidgetId);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather2_widget);

        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<WeatherResponse> weatherCall = apiInterface.getData(woeid);

        weatherCall.enqueue(new retrofit2.Callback<WeatherResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<WeatherResponse> call, @NotNull Response<WeatherResponse> response) {

                if (response.code() == 200) {

                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {

                        ConsolidatedWeather currentWeathers = weatherResponse.consolidatedWeather.get(0);

                        views.setTextViewText(R.id.cityName, weatherResponse.title);
                        views.setTextViewText(R.id.temp, currentWeathers.theTemp.intValue() + "°");
                        views.setTextViewText(R.id.min, "Min. " + currentWeathers.minTemp.intValue() + "°");
                        views.setTextViewText(R.id.max, "Maks. " + currentWeathers.maxTemp.intValue() + "°");

                        AppWidgetTarget awt = new AppWidgetTarget(context, R.id.weatherStateImg, views, appWidgetId);

                        String stateString = currentWeathers.weatherStateAbbr;

                        Glide.with(context.getApplicationContext())
                                .asBitmap()
                                .load("https://www.metaweather.com/static/img/weather/png/" + stateString + ".png")
                                .into(awt);

                        views.setViewVisibility(R.id.progress, View.GONE);
                        views.setViewVisibility(R.id.rootLayout2, View.VISIBLE);

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("woeid", woeid);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        views.setOnClickPendingIntent(R.id.rootLayout2, PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<WeatherResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Widget1ConfigActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}