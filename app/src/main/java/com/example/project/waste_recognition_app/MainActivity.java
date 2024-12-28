package com.example.project.waste_recognition_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

    FragmentType currentFragmentType;
    private BottomNavigationView bottomNavigation;

    private IndexFragment indexFragment;
    private ScannerFragment scannerFragment;
    private SettingsFragment settingsFragment;

    private TextView userInitialTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the theme before calling super.onCreate
        initializeDefaultTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeUIComponents();
        setupUserInitial();
        setupBottomNavigation();
        restoreCurrentFragment();

        // Handle navigation if an intent is provided
        handleIntentNavigation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save the current fragment type in SharedPreferences
        saveCurrentFragmentType();
    }

    // Method to initialize default theme preferences if not set
    private void initializeDefaultTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkMode = prefs.getBoolean("night_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    // Method to initialize UI components
    private void initializeUIComponents() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        userInitialTextView = findViewById(R.id.user_initial);
    }

    // Method to set up the user initial displayed in the toolbar
    private void setupUserInitial() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "R");
        String initial = username.isEmpty() ? "R" : String.valueOf(username.charAt(0)).toUpperCase();

        userInitialTextView.setText(initial);
        userInitialTextView.setOnClickListener(this::showUserOptionsMenu);
    }

    // Show a popup menu with user options, such as logging out
    private void showUserOptionsMenu(View anchorView) {
        PopupMenu popupMenu = new PopupMenu(this, anchorView);
        popupMenu.getMenuInflater().inflate(R.menu.user_options_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_logout) {
                logoutUser();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    // Log out the user and navigate back to LoginActivity
    void logoutUser() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().clear().apply();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Set up bottom navigation functionality
    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_index) {
                openIndex();
            } else if (itemId == R.id.navigation_scanner) {
                openScanner();
            } else if (itemId == R.id.navigation_settings) {
                openSettings();
            }
            return true;
        });
    }

    // Method to open the initial fragment (Index)
    private void openInitialFragment() {
        openIndex();
    }

    // Restore the current fragment based on SharedPreferences
    private void restoreCurrentFragment() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String savedFragmentType = prefs.getString("currentFragment", "INDEX");

        if ("INDEX".equals(savedFragmentType)) {
            openIndex();
        } else if ("SCANNER".equals(savedFragmentType)) {
            openScanner();
        } else if ("SETTINGS".equals(savedFragmentType)) {
            openSettings();
        }
    }

    // Method to save the current fragment type to SharedPreferences
    private void saveCurrentFragmentType() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("currentFragment", currentFragmentType.name());
        editor.apply();
    }

    // Method to open the IndexFragment
    private void openIndex() {
        if (currentFragmentType != FragmentType.INDEX) {
            if (indexFragment == null) indexFragment = new IndexFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, indexFragment);
            transaction.commit();
            currentFragmentType = FragmentType.INDEX;
        }
    }

    // Method to open the ScannerFragment
    void openScanner() {
        if (currentFragmentType != FragmentType.SCANNER) {
            if (scannerFragment == null) scannerFragment = new ScannerFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, scannerFragment);
            transaction.commit();
            currentFragmentType = FragmentType.SCANNER;
        }
    }

    // Method to open the SettingsFragment
    private void openSettings() {
        if (currentFragmentType != FragmentType.SETTINGS) {
            if (settingsFragment == null) settingsFragment = new SettingsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, settingsFragment);
            transaction.commit();
            currentFragmentType = FragmentType.SETTINGS;
        }
    }

    // Method to handle intent and open the appropriate fragment
    private void handleIntentNavigation() {
        Intent intent = getIntent();
        if (intent != null && "settings".equals(intent.getStringExtra("navigateTo"))) {
            openSettings();
        }
    }
}