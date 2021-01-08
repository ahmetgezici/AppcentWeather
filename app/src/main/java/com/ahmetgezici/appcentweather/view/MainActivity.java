package com.ahmetgezici.appcentweather.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.ahmetgezici.appcentweather.R;
import com.ahmetgezici.appcentweather.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PermissionFragment permissionFragment = new PermissionFragment();

        manager.beginTransaction()
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .replace(binding.fragmentLayout.getId(), permissionFragment, permissionFragment.getTag())
                .commit();

    }
}