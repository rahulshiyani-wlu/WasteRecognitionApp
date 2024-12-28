package com.example.project.waste_recognition_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettingsActivity extends AppCompatActivity {

    private TextView userNameInput, userEmailInput;
    private Button goBackButton;
    private TextView changePasswordLink;
    private TextView userInitialTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        // Initialize UI components
        initializeUIComponents();
        setupUserInitial();
        loadUserProfile();
        setupListeners();
    }

    // Method to initialize UI components
    private void initializeUIComponents() {
        userNameInput = findViewById(R.id.user_name_update);
        userEmailInput = findViewById(R.id.user_email_update);
        goBackButton = findViewById(R.id.update_profile_btn);
        changePasswordLink = findViewById(R.id.change_password_link);
        userInitialTextView = findViewById(R.id.user_initial);
    }

    // Set up the user initial displayed in the toolbar, using shared preferences to retrieve the username
    private void setupUserInitial() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "R");
        String initial = username.isEmpty() ? "R" : String.valueOf(username.charAt(0)).toUpperCase();
        userInitialTextView.setText(initial);

        // Set OnClickListener for the user initial to show a logout menu
        userInitialTextView.setOnClickListener(this::showUserOptionsMenu);
    }

    // Show a popup menu with user options, such as logging out
    private void showUserOptionsMenu(View anchorView) {
        PopupMenu popupMenu = new PopupMenu(ProfileSettingsActivity.this, anchorView);
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

    // Log out the user by clearing shared preferences and navigating back to LoginActivity
    private void logoutUser() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit().clear().apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Load the user's profile information from shared preferences
    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "N/A");
        String email = prefs.getString("email", "N/A");
        userNameInput.setText(username);
        userEmailInput.setText(email);
    }

    // Set up listeners for various UI elements
    private void setupListeners() {
        goBackButton.setOnClickListener(v -> finish()); // Navigate back to the previous screen
        changePasswordLink.setOnClickListener(v -> startActivity(new Intent(this, ChangePasswordActivity.class)));
    }
}