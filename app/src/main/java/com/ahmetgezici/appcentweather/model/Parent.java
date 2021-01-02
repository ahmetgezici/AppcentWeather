package com.ahmetgezici.appcentweather.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Parent {

    @SerializedName("title")
    @Expose
    @Nullable
    public String title;
    @SerializedName("location_type")
    @Expose
    @Nullable
    public String locationType;
    @SerializedName("woeid")
    @Expose
    @Nullable
    public Integer woeid;
    @SerializedName("latt_long")
    @Expose
    @Nullable
    public String lattLong;

}