package com.example.project.waste_recognition_app;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    enum FragmentType {
        INDEX,
        SCANNER,
        SETTINGS
    }

    public static boolean startedFlag = true;
    private FragmentType currentFragmentType;
    private BottomNavigationView bottomNavigation;

    // Fragments
    private IndexFragment indexFragment;
    private ScannerFragment scannerFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (startedFlag) {
            if (sharedPreferences.getBoolean("night_mode", false)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
            startedFlag = false;
        }

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_index) {
                openIndex();
                return true;
            } else if (itemId == R.id.navigation_scanner) {
                openScanner();
                return true;
            } else if (itemId == R.id.navigation_settings) {
                openSettings(false);
                return true;
            }
            return false;
        });

        // Open settings if it was the previous screen, otherwise open index
        if (SettingsFragment.isSettingsChanged) {
            openSettings(true);
            SettingsFragment.isSettingsChanged = false;
        } else {
            openIndex();
        }
    }

    private void openIndex() {
        if (currentFragmentType != FragmentType.INDEX) {
            if (indexFragment == null) indexFragment = new IndexFragment();
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, indexFragment);
            transaction.commit();
            currentFragmentType = FragmentType.INDEX;
        }
    }

    private void openScanner() {
        if (currentFragmentType != FragmentType.SCANNER) {
            if (scannerFragment == null) scannerFragment = new ScannerFragment();
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, scannerFragment);
            transaction.commit();
            currentFragmentType = FragmentType.SCANNER;
        }
    }

    private void openSettings(boolean override) {
        if (currentFragmentType != FragmentType.SETTINGS || override) {
            if (settingsFragment == null) settingsFragment = new SettingsFragment();
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, settingsFragment);
            transaction.commit();
            currentFragmentType = FragmentType.SETTINGS;
        }
    }
}