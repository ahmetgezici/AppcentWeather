package com.ahmetgezici.appcentweather.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmetgezici.appcentweather.R;
import com.ahmetgezici.appcentweather.model.LocationSearch;
import com.ahmetgezici.appcentweather.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

    List<LocationSearch> locationSearch;
    ArrayList<Integer> gradientList;
    Context context;

    public CitiesAdapter(List<LocationSearch> locationSearch, ArrayList<Integer> gradientList, Context context) {
        this.locationSearch = locationSearch;
        this.gradientList = gradientList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cities, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.cityItem.setBackground(ContextCompat.getDrawable(context, gradientList.get(position % 9)));
        holder.cityName.setText(locationSearch.get(position).title);
        holder.distance.setText(locationSearch.get(position).distance / 1000 + " km");
        holder.city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("woeid", locationSearch.get(position).woeid);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return locationSearch.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView cityName, distance;
        LinearLayout cityItem;
        CardView city;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            cityItem = itemView.findViewById(R.id.cityItem);
            cityName = itemView.findViewById(R.id.cityName);
            distance = itemView.findViewById(R.id.distance);
            city = itemView.findViewById(R.id.city);
        }
    }
}
