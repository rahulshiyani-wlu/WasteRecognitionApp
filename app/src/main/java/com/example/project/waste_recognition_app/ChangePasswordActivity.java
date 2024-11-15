package com.example.project.waste_recognition_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPasswordInput, newPasswordInput;
    private Button changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initializeUIComponents();
        setupChangePasswordListener();
    }

    private void initializeUIComponents() {
        oldPasswordInput = findViewById(R.id.user_old_password);
        newPasswordInput = findViewById(R.id.user_new_password);
        changePasswordButton = findViewById(R.id.user_change_password_btn);
    }

    private void setupChangePasswordListener() {
        changePasswordButton.setOnClickListener(v -> handleChangePassword());
    }

    private void handleChangePassword() {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String storedPassword = preferences.getString("password", null);

        String oldPassword = oldPasswordInput.getText().toString().trim();
        String newPassword = newPasswordInput.getText().toString().trim();

        if (storedPassword != null && storedPassword.equals(oldPassword)) {
            updatePassword(preferences, newPassword);
            showToast("Password successfully changed");

//            Navigate to the Settings page in MainActivity
            Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
            intent.putExtra("navigate_to", "settings"); // Add a flag for navigation
            startActivity(intent);
            finish();
        } else {
            showToast("Old password is incorrect");
        }
    }

    private void updatePassword(SharedPreferences preferences, String newPassword) {
        preferences.edit().putString("password", newPassword).apply();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}