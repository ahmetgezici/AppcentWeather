package com.ahmetgezici.appcentweather.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MinMax {

    @SerializedName("min_temp")
    @Expose
    @Nullable
    public Double minTemp;
    @SerializedName("max_temp")
    @Expose
    @Nullable
    public Double maxTemp;

}