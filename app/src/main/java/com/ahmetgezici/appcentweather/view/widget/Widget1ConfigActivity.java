package com.ahmetgezici.appcentweather.view.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahmetgezici.appcentweather.R;
import com.ahmetgezici.appcentweather.model.City;
import com.ahmetgezici.appcentweather.network.ApiClient;
import com.ahmetgezici.appcentweather.network.ApiInterface;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.text.BreakIterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Widget1ConfigActivity extends Activity {

    private static final String PREFS_NAME = "com.ahmetgezici.appcentweather.View.WeatherWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    String woeid = "";

    TextInputEditText cityName;
    ExtendedFloatingActionButton search, complete;
    LinearLayout resultLayout;
    TextView result;
    ImageView resultImg;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);

        setContentView(R.layout.widget_configure);

        cityName = findViewById(R.id.cityName);
        search = findViewById(R.id.search);
        resultLayout = findViewById(R.id.resultLayout);
        result = findViewById(R.id.result);
        resultImg = findViewById(R.id.resultImg);
        complete = findViewById(R.id.complete);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(Widget1ConfigActivity.this);

                ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

                Call<List<City>> cityCall = apiInterface.getCity(cityName.getText().toString());

                cityCall.enqueue(new Callback<List<City>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<City>> call, @NotNull Response<List<City>> response) {

                        if (response.code() == 200) {

                            resultLayout.setVisibility(View.VISIBLE);

                            List<City> city = response.body();
                            if (city.size() > 0) {

                                woeid = city.get(0).woeid;

                                cityName.setText("");
                                resultImg.setImageResource(R.drawable.ic_ok);
                                result.setText("Şehir\nBulundu");
                                complete.setEnabled(true);
                            } else {
                                resultImg.setImageResource(R.drawable.ic_cancel);
                                result.setText("Şehir\nBulunamadı!");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<City>> call, @NotNull Throwable t) {
                        Log.e("aaa", t.getLocalizedMessage());
                    }
                });
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = Widget1ConfigActivity.this;

                BreakIterator mAppWidgetText;
                String widgetText = woeid;
                saveTitlePref(context, mAppWidgetId, widgetText);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                Weather1Widget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

    }

    public Widget1ConfigActivity() {
        super();
    }

    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}