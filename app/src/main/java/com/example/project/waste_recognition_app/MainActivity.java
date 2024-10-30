package com.example.project.waste_recognition_app;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    enum FragmentType {
        INDEX,
        SCANNER,
        SETTINGS
    }

    public static Boolean startedFlag = true;
    private String TAG = "MainActivity";
    FragmentType currentFragmentType;
    BottomNavigationView bottomNavigation;

    // Fragments
    IndexFragment indexFragment;
    ScannerFragment scannerFragment;
    SettingsFragmentPreferenceScreen settingsFragmentPreferenceScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (startedFlag) {
            if (sharedPreferences.getBoolean("night_mode", false)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
            startedFlag = false;

            if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof IndexFragment) {
                ((IndexFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.container))).mProgressDialog.dismiss();
            }

            recreate();
        }

        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Listener for BottomNavigationView
        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
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
        };

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        // Open settings if it was the previous screen, otherwise open index
        if (SettingsFragmentPreferenceScreen.openSettingsNotifier) {
            openSettings(true);
            SettingsFragmentPreferenceScreen.openSettingsNotifier = false;
        } else {
            openIndex();
        }
    }

    void openIndex() {
        if (currentFragmentType != FragmentType.INDEX) {
            if (indexFragment == null) indexFragment = new IndexFragment();
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, indexFragment);

            if (currentFragmentType != null) transaction.addToBackStack(null);

            currentFragmentType = FragmentType.INDEX;
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    }

    void openScanner() {
        if (currentFragmentType != FragmentType.SCANNER) {
            currentFragmentType = FragmentType.SCANNER;

            if (scannerFragment == null) {
                scannerFragment = new ScannerFragment();
            }

            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, scannerFragment);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    }

    void openSettings(Boolean override) {
        if (currentFragmentType != FragmentType.SETTINGS || override) {
            currentFragmentType = FragmentType.SETTINGS;

            if (settingsFragmentPreferenceScreen == null) settingsFragmentPreferenceScreen = new SettingsFragmentPreferenceScreen();

            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, settingsFragmentPreferenceScreen);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    }
}
