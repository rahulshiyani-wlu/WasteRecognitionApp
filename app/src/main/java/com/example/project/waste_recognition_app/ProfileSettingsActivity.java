package com.example.project.waste_recognition_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettingsActivity extends AppCompatActivity {

    private EditText userNameInput, userEmailInput;
    private Button updateProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ProfileSettingsActivity", "onCreate() called"); // Log activity creation

        setContentView(R.layout.activity_profile_settings);

        initializeUIComponents();
        loadUserProfile();
        setupUpdateButtonListener();
    }

    private void initializeUIComponents() {
        userNameInput = findViewById(R.id.user_name_update);
        userEmailInput = findViewById(R.id.user_email_update);
        updateProfileButton = findViewById(R.id.update_profile_btn);
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userNameInput.setText(prefs.getString("username", "")); // Load username
        userEmailInput.setText(prefs.getString("email", "")); // Load email
    }

    private void setupUpdateButtonListener() {
        updateProfileButton.setOnClickListener(v -> updateProfile());
    }

    // Update and then Save user data
    private void updateProfile() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit()
                .putString("username", userNameInput.getText().toString().trim()) // Save updated username
                .putString("email", userEmailInput.getText().toString().trim()) // Save updated email
                .apply();

        showToast("Profile successfully updated"); // Show confirmation message
        finish();
    }

    // Short Toast
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}