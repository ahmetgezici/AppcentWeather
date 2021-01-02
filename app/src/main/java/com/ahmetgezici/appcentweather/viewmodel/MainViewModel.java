package com.ahmetgezici.appcentweather.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ahmetgezici.appcentweather.model.MinMax;
import com.ahmetgezici.appcentweather.model.WeatherResponse;
import com.ahmetgezici.appcentweather.repositories.MainRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainViewModel extends ViewModel {

    MainRepository repository;

    public MainViewModel() {
        setupNextDates();
        repository = new MainRepository();
    }

    MutableLiveData<WeatherResponse> weatherResponse;
    MutableLiveData<List<MinMax>> minMax;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> datesLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> dayNamesLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> temListLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> minMaxDifLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> refMinHighLiveData = new MutableLiveData<>();


    public LiveData<WeatherResponse> getWeatherResponse(String woeid) {

        if (weatherResponse == null) {
            weatherResponse = repository.requestWeatherResponse(woeid);
        }
        return weatherResponse;
    }

    public LiveData<List<MinMax>> getMinMax(String woeid, String date) {

        if (minMax == null) {
            minMax = repository.requestMinMax(woeid, date);
        }
        return minMax;
    }


    private void setupNextDates() {
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

        datesLiveData.setValue(dates);
        dayNamesLiveData.setValue(dayNames);
    }

    public void calculate(ArrayList<String> tempList) {

        Collections.sort(tempList);

        int minMaxDifference = 0;
        int refMinHigh = 0;

        for (int j = 0; j < tempList.size(); j++) {
            String[] temp = tempList.get(j).substring(2).split(";");
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

        minMaxDifLiveData.setValue(minMaxDifference);
        refMinHighLiveData.setValue(refMinHigh);
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

    public static String getWeatherState(String state) {
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

}
