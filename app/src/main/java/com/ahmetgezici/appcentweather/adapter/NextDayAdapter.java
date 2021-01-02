package com.ahmetgezici.appcentweather.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmetgezici.appcentweather.R;
import com.ahmetgezici.appcentweather.model.ConsolidatedWeather;
import com.ahmetgezici.appcentweather.network.ApiClient;
import com.ahmetgezici.appcentweather.network.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ldoublem.loadingviewlib.view.LVCircularRing;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NextDayAdapter extends RecyclerView.Adapter<NextDayAdapter.ViewHolder> {

    ArrayList<String> dayNames;
    ArrayList<String> dates;
    int minMaxDifference;
    int refMinHigh;
    String woeid;
    Context context;

    ApiInterface apiInterface;

    public NextDayAdapter(ArrayList<String> dayNames, ArrayList<String> dates, int minMaxDifference, int refMinHigh, String woeid, Context context) {
        this.dayNames = dayNames;
        this.dates = dates;
        this.context = context;
        this.minMaxDifference = minMaxDifference;
        this.refMinHigh = refMinHigh;
        this.woeid = woeid;

        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nextday, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.progress.startAnim();

        holder.nextDay.setText(dayNames.get(position));

        Call<List<ConsolidatedWeather>> nextDayCall = apiInterface.getNextDate(woeid, dates.get(position));

        nextDayCall.enqueue(new Callback<List<ConsolidatedWeather>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<List<ConsolidatedWeather>> call, @NotNull Response<List<ConsolidatedWeather>> response) {
                if (response.code() == 200) {

                    ConsolidatedWeather weather = response.body().get(0);

                    holder.nextTemp.setText(weather.theTemp.intValue() + "°");
                    holder.nextMin.setText(" " + weather.minTemp.intValue() + "°");
                    holder.nextMax.setText(" " + weather.maxTemp.intValue() + "°");

                    String stateString = weather.weatherStateAbbr;
                    Glide.with(context)
                            .load("https://www.metaweather.com/static/img/weather/png/" + stateString + ".png")
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(holder.nextStateImg);

                    double minMaxHeight = (60 / (double) minMaxDifference) * (weather.maxTemp.intValue() - weather.minTemp.intValue());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpToPx(7), dpToPx(minMaxHeight));
                    holder.nextMinMax.setLayoutParams(layoutParams);
                    holder.nextMinMax.setCardBackgroundColor(Color.parseColor("#DCDCDC"));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(133)
                    );
                    params.setMargins(0, dpToPx((refMinHigh - weather.minTemp.intValue()) * (60 / (double) minMaxDifference)), 0, 0);
                    holder.layout.setLayoutParams(params);

                    holder.progressLayout.animate().setDuration(400).alpha(0.0f);

                }
            }

            @Override
            public void onFailure(@NotNull Call<List<ConsolidatedWeather>> call, @NotNull Throwable t) {
                Log.e("aaa", "onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dayNames.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nextDay, nextTemp, nextMax, nextMin;
        ImageView nextStateImg;
        CardView nextMinMax;
        LinearLayout layout;
        LVCircularRing progress;
        FrameLayout progressLayout;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            nextDay = itemView.findViewById(R.id.nextDay);
            nextTemp = itemView.findViewById(R.id.nextTemp);
            nextMax = itemView.findViewById(R.id.nextMax);
            nextMin = itemView.findViewById(R.id.nextMin);
            nextStateImg = itemView.findViewById(R.id.nextStateImg);
            nextMinMax = itemView.findViewById(R.id.nextMinMax);
            layout = itemView.findViewById(R.id.layout);
            progress = itemView.findViewById(R.id.progress);
            progressLayout = itemView.findViewById(R.id.progressLayout);

        }
    }

    int dpToPx(double dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (float) dp,
                context.getResources().getDisplayMetrics());
    }
}
