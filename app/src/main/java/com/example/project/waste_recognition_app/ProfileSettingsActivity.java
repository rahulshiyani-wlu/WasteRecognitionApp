package com.example.project.waste_recognition_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettingsActivity extends AppCompatActivity {

    // Declare UI components
    private EditText userNameInput, userEmailInput;
    private Button updateProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ProfileSettingsActivity", "onCreate() called"); // Log activity creation

        setContentView(R.layout.activity_profile_settings);

        // Initialize UI components
        initializeUIComponents();
        // Load user data into input fields
        loadUserProfile();
        // Set up listener for updating profile
        setupUpdateButtonListener();
    }

    // Initialize UI components by linking them to layout IDs
    private void initializeUIComponents() {
        userNameInput = findViewById(R.id.user_name_update);
        userEmailInput = findViewById(R.id.user_email_update);
        updateProfileButton = findViewById(R.id.update_profile_btn);
    }

    // Load existing user data from SharedPreferences
    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userNameInput.setText(prefs.getString("username", "")); // Load username
        userEmailInput.setText(prefs.getString("email", "")); // Load email
    }

    // Set up the update profile button functionality
    private void setupUpdateButtonListener() {
        updateProfileButton.setOnClickListener(v -> updateProfile());
    }

    // Update and save the user's profile data
    private void updateProfile() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit()
                .putString("username", userNameInput.getText().toString().trim()) // Save updated username
                .putString("email", userEmailInput.getText().toString().trim()) // Save updated email
                .apply();

        showToast("Profile successfully updated"); // Show confirmation message
        finish(); // Close the activity
    }

    // Utility method to display toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}