package com.ahmetgezici.appcentweather.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ahmetgezici.appcentweather.model.LocationSearch;
import com.ahmetgezici.appcentweather.repositories.CityRepository;

import java.util.List;

public class CitySearchViewModel extends ViewModel {

    MutableLiveData<List<LocationSearch>> locations;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<List<LocationSearch>> getLocations(double latitude, double longitude) {
        if (locations == null) {
            CityRepository repository = new CityRepository();
            locations = repository.requestLocationSearch(latitude, longitude);
        }
        return locations;
    }

}
